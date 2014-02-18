package net.canadensys.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * i18n resource path builder/formatter.
 * Using a InMemoryResourceBundle to translate a resource name into the i18n version.
 * @author canadensys
 *
 */
public class I18NUrlBuilder {
	public static final String RESOURCE_BUNDLE_BASE_NAME = "urlResource";
	public static final String LANG_PARAM = "lang";
	public static final String PARAM_MARKER = "{}";
	
	private static Map<String,InMemoryResourceBundle> resourceBundleMap = new HashMap<String, InMemoryResourceBundle>();
	
	/**
	 * 
	 * @param lang
	 * @param translationFormat where '/' is a separator, '{}' represents variable and all text represent a value
	 * to use for inverse lookup.
	 * @param params all variables needed for translationFormat in their order of appearance or null if no variable are needed.
	 * @return
	 */
	public static String generateI18nResourcePath(String lang, String translationFormat, String ... params){
		//TODO check it's a known lang
		if(!resourceBundleMap.containsKey(lang)){
			resourceBundleMap.put(lang, InMemoryResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, new Locale(lang)));
		}
		
		StringBuilder url = new StringBuilder("/" + lang.toLowerCase());
		//does it require some parameters?
		if(translationFormat.contains(PARAM_MARKER)){
			if((params == null) || (StringUtils.countMatches(translationFormat,PARAM_MARKER) != params.length)){
				throw new IllegalArgumentException("translationFormat marker(s) does not match number of parameters.");
			}
		}
		
		String[] pathParts = translationFormat.split("/");
		
		int paramId = 0;
		for(String currPathPart : pathParts){
			if(StringUtils.isNotBlank(currPathPart)){
				url.append("/");
				if(PARAM_MARKER.equals(currPathPart)){
					url.append(params[paramId]);
					paramId++;
				}
				else{
					url.append(resourceBundleMap.get(lang).inverseLookup(currPathPart));
				}
			}
		}
		return url.toString();
	}
}
