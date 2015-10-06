package net.canadensys.dataportal.occurrence.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.web.i18n.I18nUrlBuilder;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Utility class for common Controller operations.
 * @author canadensys
 *
 */
public class ControllerHelper {
		
	/**
	 * Add common header variables to modelRoot.
	 * Including locale, rootURL, currentVersion and useMinified
	 * @param request
	 * @param locale
	 * @param appConfig
	 * @param modelRoot
	 */
	public static void setPageHeaderVariables(HttpServletRequest request, String resourceName, String[] resourceParams, OccurrencePortalConfig appConfig, HashMap<String,Object> modelRoot){
		//Are we using versioning?
		if(!StringUtils.isBlank(appConfig.getCurrentVersion())){
			modelRoot.put("currentVersion", appConfig.getCurrentVersion());
		}
		//Are we using minified files?
		modelRoot.put("useMinified", BooleanUtils.toBoolean(appConfig.getUseMinified()));
		
		//Add path to access this resource in other languages
		Locale locale = RequestContextUtils.getLocale(request);
		String currLanguage = locale.getLanguage();
		String otherLanguagePath;
		Map<String,String> languagePathMap = new HashMap<String,String>();
		
		for(String currSupportedLang : appConfig.getSupportedLanguagesList()){
			if(!currSupportedLang.equalsIgnoreCase(currLanguage)){
				otherLanguagePath = I18nUrlBuilder.generateI18nResourcePath(currSupportedLang, 
						OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat(resourceName), resourceParams);
				//prepend the context path (e.g. /explorer)
				otherLanguagePath = request.getContextPath() + otherLanguagePath;
				
				if(StringUtils.isNotBlank(request.getQueryString())){
					otherLanguagePath += "?"+request.getQueryString();
				}
				languagePathMap.put(currSupportedLang, otherLanguagePath);
			}
		}
		
		modelRoot.put("otherLanguage", languagePathMap);
	}

	/**
	 * Add common header variables to modelRoot to the resource view. Including
	 * locale, rootURL, currentVersion and useMinified
	 * 
	 * @param request
	 * @param locale
	 * @param appConfig
	 * @param modelRoot
	 */
	public static void setResourceVariables(HttpServletRequest request,
			String resourceName, String resourceParam,
			OccurrencePortalConfig appConfig, HashMap<String, Object> modelRoot) {
		// Are we using versioning?
		if (!StringUtils.isBlank(appConfig.getCurrentVersion())) {
			modelRoot.put("currentVersion", appConfig.getCurrentVersion());
		}
		// Are we using minified files?
		modelRoot.put("useMinified",
				BooleanUtils.toBoolean(appConfig.getUseMinified()));

		// Add path to access this resource in other languages
		Locale locale = RequestContextUtils.getLocale(request);
		String currLanguage = locale.getLanguage();
		String otherLanguagePath;
		Map<String, String> languagePathMap = new HashMap<String, String>();

		for (String currSupportedLang : appConfig.getSupportedLanguagesList()) {
			if (!currSupportedLang.equalsIgnoreCase(currLanguage)) {
				otherLanguagePath = I18nUrlBuilder.generateI18nResourcePath(
						currSupportedLang,
						OccurrencePortalConfig.I18N_TRANSLATION_HANDLER
								.getTranslationFormat(resourceName),
						resourceParam);
				// prepend the context path (e.g. /explorer)
				otherLanguagePath = request.getContextPath()
						+ otherLanguagePath;

				if (StringUtils.isNotBlank(request.getQueryString())) {
					otherLanguagePath += "?" + request.getQueryString();
				}
				languagePathMap.put(currSupportedLang, otherLanguagePath);
			}
		}
		modelRoot.put("otherLanguage", languagePathMap);
	}
}
