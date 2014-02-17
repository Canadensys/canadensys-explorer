package net.canadensys.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to express how to perform the i18n url translation.
 * Usage
 * Method annotation:@I18NTranslation(resourceName="occurrence", translateFormat = "/occurrence/{}/{}")
 * Then, this is accessible using I18NTranslationHandler.getTranslationFormat(resourceName).
 * @author canadensys
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface I18NTranslation {
	
	/**
	 * Used as a key to find this mapping back.
	 * Must be unique within the web app.
	 * @return
	 */
	String resourceName();
	
	/**
	 * resourceKey/{}
	 * where resourceKey is available in properties files and {} is a variable.
	 * @return
	 */
	String translateFormat();
}
