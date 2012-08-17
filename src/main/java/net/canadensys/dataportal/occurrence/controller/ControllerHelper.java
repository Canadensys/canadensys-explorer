package net.canadensys.dataportal.occurrence.controller;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.search.parameter.SearchURLHelper;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for common Controller operations.
 * @author canadensys
 *
 */
public class ControllerHelper {
	
	/**
	 * Add common header variables to modelRoot.
	 * Including languageSwitcherURL, locale, rootURL and GoogleAnalytics stuff.
	 * @param request
	 * @param locale
	 * @param appConfig
	 * @param modelRoot
	 */
	public static void setPageHeaderVariables(HttpServletRequest request, Locale locale, OccurrencePortalConfig appConfig, HashMap<String,Object> modelRoot){
		String targetLang;
		if(locale.equals(Locale.ENGLISH)){
			targetLang = Locale.FRENCH.getLanguage();
		}
		else if(locale.equals(Locale.FRENCH)){
			targetLang = Locale.ENGLISH.getLanguage();
		}
		else{
			locale = Locale.ENGLISH;
			targetLang = Locale.FRENCH.getLanguage();
		}
		
		modelRoot.put("languageSwitcherURL",SearchURLHelper.getLanguageSwitcherURL(request, targetLang));
		modelRoot.put("locale",appConfig.getResourceBundle(locale));
		
		//Google Analytics
		if(!StringUtils.isBlank(appConfig.getGoogleAnalyticsAccount())){
			modelRoot.put("gaAccount", appConfig.getGoogleAnalyticsAccount());
			modelRoot.put("gaSiteVerification", appConfig.getGoogleAnalyticsSiteVerification());
		}
		
		//Root URL of the web page
		if(!StringUtils.isBlank(appConfig.getRootURL())){
			modelRoot.put("rootURL", appConfig.getRootURL());
		}
	}

}
