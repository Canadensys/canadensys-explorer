package net.canadensys.dataportal.occurrence.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.canadensys.bundle.UTF8PropertyResourceBundle;
import net.canadensys.web.i18n.annotation.I18nTranslationHandler;

/**
 * General configurations for the Occurrence Portal. Those configurations are not tied to a specific service.
 * @author canadensys
 *
 */
public class OccurrencePortalConfig {
		
	public static String BUNDLE_NAME = "ApplicationResources";
	
	private String currentVersion;
	private Boolean useMinified;
	
	private ResourceBundle enBundle;
	private ResourceBundle frBundle;
	
	//List of all terms to use in our DarwinCore archive
	private String dwcaTermUsed;
	
	private boolean hashEmailAddress = false;
	private String emailSalt;
	
	//Unique key that is managed by the portal
	public static final String OCCURRENCE_MANAGED_ID_FIELD = "auto_id";
	
	public static final I18nTranslationHandler I18N_TRANSLATION_HANDLER = new I18nTranslationHandler("net.canadensys.dataportal.occurrence.controller");

	private static List<Locale> supportedLocale = new ArrayList<Locale>(2);
	static{
		supportedLocale.add(Locale.ENGLISH);
		supportedLocale.add(Locale.FRENCH);
	}

	public OccurrencePortalConfig(){
		try{
			try {
				enBundle = UTF8PropertyResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
				frBundle = UTF8PropertyResourceBundle.getBundle(BUNDLE_NAME, Locale.FRENCH);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(MissingResourceException e){
		    System.out.println(e);
		}
	}
	
	public List<Locale> getSupportedLocale(){
		return supportedLocale;
	}
	
	public ResourceBundle getResourceBundle(Locale locale) {
		if(locale.equals(Locale.ENGLISH)){
			 return enBundle;
		}
		else if(locale.equals(Locale.FRENCH)){
			 return frBundle;
		}
		return null;
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
}
