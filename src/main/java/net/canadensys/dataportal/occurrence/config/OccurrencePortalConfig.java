package net.canadensys.dataportal.occurrence.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import net.canadensys.bundle.UTF8PropertyResourceBundle;
import net.canadensys.web.i18n.annotation.I18nTranslationHandler;

import org.apache.log4j.Logger;

/**
 * General configurations for the Occurrence Portal. Those configurations are not tied to a specific service.
 * @author canadensys
 *
 */
public class OccurrencePortalConfig {
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(OccurrencePortalConfig.class);
	
	public static final String CONFIG_FILENAME = "/WEB-INF/portal-config.properties";
	public static final String DEFAULT_LANGUAGE_KEY = "i18n.defaultLanguage";
	public static final String SUPPORTED_LANGUAGES_KEY = "i18n.supportedLanguages";
	
	//Associated sequences properties related
	public static final String SEQ_URL_FORMAT_SUFFIX = ".urlFormat";
	public static final String SEQ_DISPLAY_NAME_SUFFIX = ".displayName";
	
	public static String BUNDLE_NAME = "ApplicationResources";
	public static String URL_BUNDLE_NAME = "urlResource";
	
	private List<String> supportedLanguagesList;
	private Map<Locale,ResourceBundle> resourceBundleByLocale;
	
	private String currentVersion;
	private Boolean useMinified;
	
	//List of all terms to use in our DarwinCore archive
	private String dwcaTermUsed;
	
	private boolean hashEmailAddress = false;
	private String emailSalt;
	
	//Associated sequences related
	private Properties sequenceProvidersProperties;
	
	//Unique key that is managed by the portal
	public static final String OCCURRENCE_MANAGED_ID_FIELD = "auto_id";
	
	
	//Key used for models in view
	public static final String PAGE_ROOT_MODEL_KEY = "page";
	
	public static final I18nTranslationHandler I18N_TRANSLATION_HANDLER = new I18nTranslationHandler("net.canadensys.dataportal.occurrence.controller");


	public OccurrencePortalConfig(){
	}
	
	/**
	 * Set supported languages with a comma separated list of languages.
	 * @param supportedLanguages
	 */
	public void setSupportedLanguages(String supportedLanguages) {
		supportedLanguagesList = new ArrayList<String>();
		resourceBundleByLocale = new HashMap<Locale, ResourceBundle>();
		
		String[] languages = supportedLanguages.split(",");

		Locale currLocale;
		for(String currLang : languages){
			currLang = currLang.trim();
			try{
				currLocale = new Locale(currLang);
				if(currLocale.getISO3Language() != null){
					supportedLanguagesList.add(currLang.toLowerCase());
					resourceBundleByLocale.put(currLocale, UTF8PropertyResourceBundle.getBundle(BUNDLE_NAME, currLocale));
				}
			}
			catch(MissingResourceException mrEx){
				LOGGER.fatal("Can't load Language defined by " + currLang, mrEx);
			} catch (UnsupportedEncodingException e) {
				LOGGER.fatal("Can't load Language defined by " + currLang, e);
			} catch (IOException e) {
				LOGGER.fatal("Can't load Language defined by " + currLang, e);
			}
		}
	}
	
	public List<String> getSupportedLanguagesList(){
		return supportedLanguagesList;
	}
	
	public Collection<Locale> getSupportedLocale(){
		return resourceBundleByLocale.keySet();
	}
	
	/**
	 * Return the resource bundle defined by the Locale or null of no bundle is associated with the Locale.
	 * @param locale
	 * @return
	 */
	public ResourceBundle getResourceBundle(Locale locale) {
		return resourceBundleByLocale.get(locale);
	}
	
	/**
	 * Get a URL resource bundle used to support i18n URLs
	 * Warning: URL resource bundle are inverted resource, the translated term is the key and the 'key' is the value.
	 * e.g. Locale.FR, arbre=tree. 
	 * @param locale
	 * @return
	 */
	public ResourceBundle getURLResourceBundle(Locale locale) {
		return ResourceBundle.getBundle(URL_BUNDLE_NAME, locale);
	}

	public String getDwcaTermUsed() {
		return dwcaTermUsed;
	}
	public void setDwcaTermUsed(String dwcaTermUsed) {
		this.dwcaTermUsed = dwcaTermUsed;
	}
	
	public String getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
	
	public Boolean getUseMinified() {
		return useMinified;
	}
	public void setUseMinified(Boolean useMinified) {
		this.useMinified = useMinified;
	}

	public boolean isHashEmailAddress() {
		return hashEmailAddress;
	}
	public void setHashEmailAddress(boolean hashEmailAddress) {
		this.hashEmailAddress = hashEmailAddress;
	}

	public String getEmailSalt() {
		return emailSalt;
	}
	public void setEmailSalt(String emailSalt) {
		this.emailSalt = emailSalt;
	}

	/**
	 * 
	 * @param sequenceProviderProperties
	 */
	public void setSequenceProvidersProperties(Properties sequenceProviderProperties) {
		this.sequenceProvidersProperties = sequenceProviderProperties;
	}

	public String getSequenceProviderUrlFormat(String sequenceProvider) {
		return sequenceProvidersProperties.getProperty(sequenceProvider + SEQ_URL_FORMAT_SUFFIX);
	}
	public String getSequenceProviderDisplayName(String sequenceProvider) {
		return sequenceProvidersProperties.getProperty(sequenceProvider + SEQ_DISPLAY_NAME_SUFFIX);
	}

}
