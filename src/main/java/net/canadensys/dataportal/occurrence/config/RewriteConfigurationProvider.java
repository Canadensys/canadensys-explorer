package net.canadensys.dataportal.occurrence.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.canadensys.web.i18n.I18nUrlBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.param.Parameters;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.HttpOperation;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.ocpsoft.rewrite.transposition.LocaleTransposition;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * URL Rewriting configuration.
 * supportedLocale are loaded from configuration file defined in OccurrencePortalConfig.
 * @author cgendreau
 *
 */
@Component
@RewriteConfiguration
public class RewriteConfigurationProvider extends HttpConfigurationProvider{
	
	private static final Logger LOGGER = Logger.getLogger(RewriteConfigurationProvider.class);

	private static final String LANG_PARAM = "lang";
	
	private String defaultLanguage;
	private final Set<String> supportedLocale = new HashSet<String>();
	
	@Override
	public Configuration getConfiguration(final ServletContext context) {
		//load supported languages from configuration file
		loadSupportedLocale(context);
		
		String languageRegexPart = StringUtils.join(supportedLocale,"|");
		
		 return ConfigurationBuilder.begin()
			 //handle root path
			 .addRule()
					.when(Path.matches("/").and(Direction.isInbound()).andNot(DispatchType.isForward()))
					.perform(new RedirectHomePage())
			 //some containers (e.g. Weblogic send nothing as web-app root
			 .addRule()
				.when(Path.matches("").and(Direction.isInbound()).andNot(DispatchType.isForward()))
				.perform(new RedirectHomePage())
			 .addRule()
				.when(Path.matches("/{tail}").and(Direction.isInbound()).andNot(DispatchType.isForward()))
				.perform(new HandleLegacyURL())
				.where("tail").matches("(?!("+languageRegexPart+"|assets|ws)).*")
			 .addRule()
			 	.when(Path.matches("/{lang}/{path}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path}").to("/{path}"))
			 	.where("lang").matches(languageRegexPart)
	           	.where("path").configuredBy(LocaleTransposition.bundle(OccurrencePortalConfig.URL_BUNDLE_NAME,"lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path1}/{id1}").to("/{path1}/{id1}"))
			 	.where("lang").matches(languageRegexPart)
	           	.where("path1").configuredBy(LocaleTransposition.bundle(OccurrencePortalConfig.URL_BUNDLE_NAME,"lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}/{path2}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path1}/{id1}").to("/{path1}/{id1}/{path2}"))
			 	.where("lang").matches(languageRegexPart)
	           	.where("path1").configuredBy(LocaleTransposition.bundle(OccurrencePortalConfig.URL_BUNDLE_NAME,"lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}/{path2}/{id2}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path1}/{id1}/{path2}/{id2}").to("/{path1}/{id1}/{path2}/{id2}"))
			 	.where("lang").matches(languageRegexPart)
	           	.where("path1").configuredBy(LocaleTransposition.bundle(OccurrencePortalConfig.URL_BUNDLE_NAME,"lang"))
	           	.where("path2").configuredBy(LocaleTransposition.bundle(OccurrencePortalConfig.URL_BUNDLE_NAME,"lang"));
	}
	
	@Override
	public int priority() {
		return 10;
	}
	
	/**
	 * Load supported languages from configuration file. Languages must be comma separated.
	 * This class is loaded before Spring web application so it is more reliable to read the configuration file ourselves.
	 * @param context
	 */
	private void loadSupportedLocale(final ServletContext context){
		final InputStream is = context.getResourceAsStream(OccurrencePortalConfig.CONFIG_FILENAME);
		
		Properties configProperties = new Properties();
        try {
        	configProperties.load(is);
        	defaultLanguage = StringUtils.defaultString(configProperties.getProperty(OccurrencePortalConfig.DEFAULT_LANGUAGE_KEY), Locale.ENGLISH.getLanguage());
            String supportedLanguages = configProperties.getProperty(OccurrencePortalConfig.SUPPORTED_LANGUAGES_KEY);
            
            if(StringUtils.isBlank(supportedLanguages)){
            	supportedLocale.add(Locale.ENGLISH.getLanguage());
            	LOGGER.warn("No supported language detected in configuration file. Using English(en) as fallback.");
            	return;
            }
            
            String[] languages = supportedLanguages.split(",");
    		for(String currLang : languages){
    			supportedLocale.add(currLang.trim().toLowerCase());
    		}
        } catch (IOException ioEx) {
        	LOGGER.fatal("Can't load supported language for Rewrite", ioEx);
		} finally {
			IOUtils.closeQuietly(is);
        }
	}
	
	/**
	 * HttpOperation to redirect to Explorer home page.
	 * @author cgendreau
	 *
	 */
	private class RedirectHomePage extends HttpOperation{
		public void performHttp(HttpServletRewrite event, EvaluationContext context) {
			String reqLanguage = event.getRequest().getLocale().getLanguage();
			if(!supportedLocale.contains(reqLanguage)){
				reqLanguage = defaultLanguage;
			}
			String landingUrl = I18nUrlBuilder.generateI18nResourcePath(reqLanguage, OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat("search"), (String)null);
			Redirect.permanent(event.getContextPath()+landingUrl).perform(event, context);
		}
	}
	
	/**
	 * Custom HttpOperation to handle legacy urls that were used in previous version of the Explorer.
	 * Those urls may have been bookmarked of referenced somewhere so we want still want to resolve them.
	 * @author cgendreau
	 *
	 */
	private class HandleLegacyURL extends HttpOperation{
		public void performHttp(HttpServletRewrite event, EvaluationContext context) {
			String tail = (String) Parameters.retrieve(context, "tail");
			String lang = Locale.ENGLISH.getLanguage();
			//check if language was provided as query parameter
			if(StringUtils.isNotBlank(event.getRequest().getParameter(LANG_PARAM))){
				if(supportedLocale.contains(event.getRequest().getParameter(LANG_PARAM))){
					lang = event.getRequest().getParameter(LANG_PARAM);
				}
			}

			String[] urlParts = tail.split("/");
			String landingUrl = null;
			
			if(urlParts.length >= 1){
				//if "search" was provided, we can handle it
				if(urlParts[0].equalsIgnoreCase("search")){
					landingUrl = I18nUrlBuilder.generateI18nResourcePath(lang, OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat("search"), (String)null);
					
					//if query string was provided, keep it if necessary
					if(StringUtils.isNotBlank(event.getRequest().getQueryString())){
						String queryString = getCleanedQueryString(event.getRequest());
						if(StringUtils.isNotBlank(queryString)){
							landingUrl += "?"+queryString;
						}
					}					
				}
				
				// "r" was used for "resources" until Version 1.2.5
				if(urlParts[0].equalsIgnoreCase("r") || urlParts[0].equalsIgnoreCase("d")){
					if((urlParts.length == 2) && StringUtils.isNotBlank(urlParts[1])){
						landingUrl = I18nUrlBuilder.generateI18nResourcePath(lang, OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat("resource"), urlParts[1]);
					}
				}
			}
			//if nothing was found, send a 404
			if(landingUrl == null){
				landingUrl = I18nUrlBuilder.generateI18nResourcePath(lang, OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat("404"));
			}

			Redirect.permanent(event.getContextPath()+landingUrl).perform(event, context);
		}
		
		/**
		 * Return query string without unnecessary elements.
		 * e.g. lang parameter is not used anymore.
		 * @param request
		 * @return
		 */
		private String getCleanedQueryString(HttpServletRequest request){
			UriComponentsBuilder bldr = ServletUriComponentsBuilder.fromRequest(request);
			bldr.replaceQueryParam(LANG_PARAM);
			return bldr.build().getQuery();
		}
	}
}
