package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.classloading.enhancers.LocalvariablesNamesEnhancer.LocalVariablesNamesTracer;

public class TemplateUtils {

	public static Map<String,Object> obtenerArg(Object... args) {
        Map<String, Object> templateBinding = new HashMap<String, Object>();
        for (Object o : args) {
            List<String> names = LocalVariablesNamesTracer.getAllLocalVariableNames(o);
            if (names.isEmpty())
            	play.Logger.info("vacio");
            for (String name : names) {
                templateBinding.put(name, o);
            }
        }
        return templateBinding;
	}

}
