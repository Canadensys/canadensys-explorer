package net.canadensys.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * TODO Mode to canadensys-core
 * ResourceBundle loaded as a in-memory map with inverse lookup support.
 * Due to inverse lookup support all keys must be unique but also all values.
 * You should only use this class for small resource bundle since it will all be loaded in memory.
 * @author canadensys
 *
 */
public class InMemoryResourceBundle {

	private ResourceBundle innerBundle;
	
	private Map<String, String> lookup;
	private Map<String, String> inverseLookup;
	
	private InMemoryResourceBundle(String baseName, Locale locale) throws MissingResourceException, IllegalArgumentException {
		innerBundle = ResourceBundle.getBundle(baseName, locale);
		initBundle();
	}
	
	public static InMemoryResourceBundle getBundle(String baseName, Locale locale) throws MissingResourceException, IllegalArgumentException{
		return new InMemoryResourceBundle(baseName, locale);
	}
	
	private void initBundle() throws IllegalArgumentException{
		lookup = new HashMap<String, String>();
		inverseLookup = new HashMap<String, String>();
		
		Enumeration<String> keys = innerBundle.getKeys();
		String key;
		String value;
		
		while(keys.hasMoreElements()){
			key = keys.nextElement();
			value = innerBundle.getString(key);
			
			//since the value now becomes the key, it must be unique.
			if(inverseLookup.containsKey(value) || lookup.containsKey(key)){
				//empty map
				lookup.clear();
				inverseLookup.clear();
				throw new IllegalArgumentException("InMemoryResourceBundle lookup creation failed. Keys AND values must be unique.");
			}
			lookup.put(key, value);
			inverseLookup.put(value, key);
		}
		//discard resource bundle;
		innerBundle = null;
	}
	
	public String lookup(String key){
		return lookup.get(key);
	}
	
	public String inverseLookup(String key){
		return inverseLookup.get(key);
	}
}
