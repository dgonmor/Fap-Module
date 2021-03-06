
package controllers.fap;

import play.*;
import play.mvc.*;
import controllers.fap.*;
import tags.ReflectionUtils;
import validation.*;
import models.*;
import java.util.*;

import messages.Messages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.config.PropertyGetter.PropertyCallback;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import emails.Mails;


public class LoggerController extends GenericController {

	public static void index(){
		renderTemplate( "fap/Admin/logs.html" );
	}


	public static void logs(long fecha){
		Date date = new Date(fecha);
		
		Gson gson = new Gson();
		BufferedReader br = null;
		try {
			FileReader file = new FileReader(nombreFichero(date));
			br = new BufferedReader(file);
		} catch (FileNotFoundException e) {
			Logger.error(e,"Fichero de log no encontrado");
		}	
		
		java.util.List<Log> rows = new ArrayList<Log>();

		if (br != null) {
			String linea;
			try {
				while ((linea = br.readLine()) != null) {
					rows.add(gson.fromJson(linea, Log.class)); 
				}
			} catch (JsonSyntaxException e) {
				Logger.error(e,"Error de formato en el fichero de log");
			} catch (IOException e) {
				Logger.error(e,"Error al leer el fichero de log");
			}
		}
		
		List<Log> rowsFiltered = rows; //Tabla sin permisos, no filtra

		tables.TableRenderResponse<Log> response = new tables.TableRenderResponse<Log>(rowsFiltered);

		flexjson.JSONSerializer flex = new flexjson.JSONSerializer().include("total", "rows.level", "rows.time", "rows.class_", "rows.user", "rows.message", "rows.trace").transform(new serializer.DateTimeTransformer(), org.joda.time.DateTime.class).exclude("*");
		String serialize = flex.serialize(response);
		renderJSON(serialize);

	}

	@Util
	@SuppressWarnings("deprecation")
	private static boolean esHoy(Date date) {
		Date hoy = new Date();
		if ((hoy.getDate() == date.getDate()) && (hoy.getMonth() == date.getMonth()) && (hoy.getYear() == date.getYear()))
			return true;
		return false;
	}

	
	@Util
	private static String nombreFichero(Date date) {
		
		String fileName = null;
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();
		
		// busca entre los appenders uno que sea de tipo DailyRollingFile,
		// preferentemente aquel cuyo nombre es "Daily", que es el configurado inicialmente en
		// los ficheros de configuracion
		
		for (Enumeration<Appender> e = logger.getAllAppenders(); e.hasMoreElements();){
			Appender appender = e.nextElement();
			if (appender instanceof DailyRollingFileAppender){
				fileName = ((DailyRollingFileAppender)appender).getFile();
				if (((DailyRollingFileAppender)appender).getName().equals("Daily")){
					break;
				}
			}
		}
		if (fileName == null){
			return null;
		}
		
		if (!esHoy(date)) {
			fileName += "."+(date.getYear()+1900)+"-";
			if (date.getMonth() < 9)
				fileName += "0";
			fileName += (date.getMonth()+1)+"-";
			if (date.getDate() < 10)
				fileName += "0";
			fileName += date.getDate();
		}
		return fileName;
	}
	
}
