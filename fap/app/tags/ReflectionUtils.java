package tags;

import static play.templates.JavaExtensions.capFirst;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Nip;
import models.PersonaFisica;
import models.Solicitante;

import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.templates.JavaExtensions;
import validation.ValueFromTable;

public class ReflectionUtils {

	public static Field getFieldByName(String fieldname) {
		String[] pieces = fieldname.split("\\.");

		String baseClazz = "models." + capFirst(pieces[0]);
		try {
			Class<?> clazz = Class.forName(baseClazz);
			if (clazz != null) {
				if (pieces.length > 1) {
					for (int i = 1; i < pieces.length; i++) {
						try {
							Field f = clazz.getField(pieces[i]);

							if (i == (pieces.length - 1)) {
								return f;
							} else {
								clazz = f.getClass();
							}
						} catch (Exception e) {
							// if there is a problem reading the field we dont
							// set any value
						}
					}
				} else {
					// field.put("value", obj);
				}
			}
		} catch (ClassNotFoundException e) {
			Logger.error("No existe la clase %s", baseClazz);
		}
		return null;
	}

	public static Class<?> getClassByName(String fieldname) {
		String[] pieces = fieldname.split("\\.");

		String baseClazz = "models." + capFirst(pieces[0]);
		Class<?> clazz = null;
		try {
			clazz = Class.forName(baseClazz);
		}
		catch (ClassNotFoundException e) {
			Logger.error("No existe la clase %s", baseClazz);
		}
		return clazz;
	}
	
	/**
	 * Busca la entidad cuyo ID es el pasado por parametro, y la devuelve en una instancia de play.db.jpa.Model
	 * @param fieldname por ejemplo "solicitud.documentacion.doc". La entidad que busca es Solicitud
	 * @param id ID a buscar
	 * @return
	 */
	public static Model findById(String fieldname, Long id) {
		Model model = null;
		try {
			Class<Model> modelClass = (Class<Model>) getClassByName(fieldname);
			model = (Model) modelClass.getMethod("findById", Object.class).invoke(null, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	public static Class getClassFromGenericField(Field field) {
		ParameterizedType type = (ParameterizedType) field.getGenericType();
		Type[] typeArguments = type.getActualTypeArguments();
		Class clazz = (Class) typeArguments[0];
		return clazz;
	}

	public static List<String> getFieldsNamesForClass(Class clazz) {
		List<String> names = new ArrayList<String>();
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			names.add(fields[i].getName());
		}
		return names;
	}

	/**
	 * Dada una clase, y un acceso a parametros deveuelve el Field 
	 * Ejemplo:
	 *    getFieldRecusively(Solicitud.class, "solicitud.solicitante.fisica.nombre")
	 * 
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Field getFieldRecursively(Class clazz, String field) {
		String[] pieces = field.split("\\.");
		Class clazzActual = clazz;
		
		if(clazz == null) throw new IllegalArgumentException("clazz no puede ser nulo");
		
		if (pieces.length > 1) {
			for (int i = 1; i < pieces.length; i++) {
				try {
					String campoActual = pieces[i];
					Field f = clazzActual.getField(campoActual);
					if (i == (pieces.length - 1)) {
						return f;
					} else {
						clazzActual = f.getType();
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		} else {
			// TODO posible problema, cuando se renderice un campo simple
			return null;
		}
		return null;
	}

	/**
	 * Dada una clase, y un acceso a parametros deveuelve el valor del campo accedido
	 * Ejemplo:
	 *    getValueRecusively(solicitud, "solicitud.solicitante.fisica.nombre")
	 * 
	 * @param o
	 * @param field
	 * @return
	 */
	public static Object getValueRecursively(Object o, String field) {
		Object obj = o;
		String[] pieces = field.split("\\.");
		if (obj != null) {
			if (pieces.length > 1) {
				for (int i = 1; i < pieces.length; i++) {
					try {
						Field f = obj.getClass().getField(pieces[i]);
						try {
							Method getter = obj.getClass().getMethod("get" + JavaExtensions.capFirst(f.getName()));
							obj = getter.invoke(obj, new Object[0]);
						} catch (NoSuchMethodException e) {
							obj = f.get(obj);
						}
						
						if (i == (pieces.length - 1)) {
							return obj;
						}
					} catch (Exception e) {
						// if there is a problem reading the field we dont set
						// any value
						//Logger.warn("" + e);
						return null;
					}
				}
			} else {
				return obj;
			}
		}
		return null;
	}
	
	public static List<Field> getFieldsOfType(Class clazz, String field){
		Field f = getFieldRecursively(clazz, field);
		if(f == null) return null;
		
		Class typeClass = f.getType();
		List<Field> fields =  new ArrayList<Field>(Arrays.asList(typeClass.getDeclaredFields()));
		if(typeClass.getSuperclass() != play.db.Model.class){
			fields.addAll(Arrays.asList(typeClass.getSuperclass().getDeclaredFields()));
		}
		
		return fields;
	}
	
	public static Class getListClass(Class clazz, String field){
		Field f = getFieldRecursively(clazz, field);
		return getListClass(f);
	}
	
	public static Class getListClass(Field f){
		if(f == null) return null;
		
		Type genericFieldType = f.getGenericType();
	    
		if(genericFieldType instanceof ParameterizedType){
		    ParameterizedType aType = (ParameterizedType) genericFieldType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    Class ret = (Class)fieldArgTypes[0];
		    System.out.println(ret.getSimpleName());
		    return ret;
		}
		return null;
	}
	
	/**
	 * Llama al método de un controlador Manual si existe
	 * El método debe ser estático, accesible y sin parámetros
	 * @param method
	 * @return
	 */
	public static Object callControllerMethodIfExists(String method){
		String controller = (String)play.mvc.Scope.RenderArgs.current().get("controllerName");
		
		//Elimina el sufijo Gen del nombre del controlador, para llamar al controlador manual
		if(controller.endsWith("Gen"))
			controller = controller.substring(0, controller.length() - 3);
		
		//Busca la clase del contorllador, puede ser un controlador de página o de popup
		String pageController = "controllers." + controller;
		String popupController = "controllers.popups." + controller;
		Class clazz = null;
		clazz = Play.classloader.getClassIgnoreCase(pageController);
		if(clazz == null){
			clazz = Play.classloader.getClassIgnoreCase(popupController);
		}
				
		//Si encuentra la clase, invoca el método si existe en la clase
		if(clazz != null){
			try {
				Method m = clazz.getMethod(method);
				Object o = m.invoke(null);
				return o;
			} catch (Exception e){
				//Method not found
			}
		}
		
		return null;
	}
	
}
