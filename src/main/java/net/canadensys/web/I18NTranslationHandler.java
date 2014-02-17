package net.canadensys.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * Configured at runtime based on I18NTranslation.
 * @author canadensys
 *
 */
public class I18NTranslationHandler {
	
	static Map<String,String> resourceTranslateFormatMap = new HashMap<String,String>();
	
	static{
		Reflections reflections = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forPackage("net.canadensys.dataportal.occurrence.controller"))
			.setScanners( new MethodAnnotationsScanner() ));
		
		Set<Method> annotated = reflections.getMethodsAnnotatedWith(I18NTranslation.class);
		Iterator<Method> annotatedIt = annotated.iterator();
		
		I18NTranslation annotation;
		while(annotatedIt.hasNext()){
			annotation = annotatedIt.next().getAnnotation(I18NTranslation.class);
			resourceTranslateFormatMap.put(annotation.resourceName(), annotation.translateFormat());
		}
	}
	
	public static String getTranslationFormat(String resourceName){
		return resourceTranslateFormatMap.get(resourceName);
	}

}
