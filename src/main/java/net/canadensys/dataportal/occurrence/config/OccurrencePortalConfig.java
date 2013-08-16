package net.canadensys.dataportal.occurrence.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

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
	
	private String googleAnalyticsSiteVerification;
	private String googleAnalyticsAccount;
	
	//List of all terms to use in our DarwinCore archive
	private String dwcaTermUsed;
	
	//Unique key that is managed by the portal
	public static final String OCCURRENCE_MANAGED_ID_FIELD = "auto_id";
	
	private static List<Locale> supportedLocale = new ArrayList<Locale>(2);
	static{
		supportedLocale.add(Locale.ENGLISH);
		supportedLocale.add(Locale.FRENCH);
	}

	public OccurrencePortalConfig(){
		try{
			try {
				//TODO : move this to canadensys-core as class UTF8ResourceBundle
				enBundle = new PropertyResourceBundle(new InputStreamReader(OccurrencePortalConfig.class.getResourceAsStream("/"+BUNDLE_NAME+"_en.properties"), "UTF-8"));
				frBundle = new PropertyResourceBundle(new InputStreamReader(OccurrencePortalConfig.class.getResourceAsStream("/"+BUNDLE_NAME+"_fr.properties"), "UTF-8"));
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

	public String getGoogleAnalyticsAccount() {
		return googleAnalyticsAccount;
	}
	public void setGoogleAnalyticsAccount(String googleAnalyticsAccount) {
		this.googleAnalyticsAccount = googleAnalyticsAccount;
	}

	public String getGoogleAnalyticsSiteVerification() {
		return googleAnalyticsSiteVerification;
	}

	public void setGoogleAnalyticsSiteVerification(
			String googleAnalyticsSiteVerification) {
		this.googleAnalyticsSiteVerification = googleAnalyticsSiteVerification;
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
}
