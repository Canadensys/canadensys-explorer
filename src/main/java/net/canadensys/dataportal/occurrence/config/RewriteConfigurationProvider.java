package net.canadensys.dataportal.occurrence.config;

import java.util.Locale;

import javax.servlet.ServletContext;

import net.canadensys.web.i18n.I18nUrlBuilder;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.Forward;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.HttpOperation;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.springframework.stereotype.Component;

/**
 * URL Rewriting configuration.
 * @author cgendreau
 *
 */
@Component
@RewriteConfiguration
public class RewriteConfigurationProvider extends HttpConfigurationProvider{

	@Override
	public Configuration getConfiguration(final ServletContext context) {
		 return ConfigurationBuilder.begin()
			 //handle root path
			 .addRule()
					.when(Path.matches("/").and(Direction.isInbound()).andNot(DispatchType.isForward()))
					.perform(new RedirectHomePage())
			//some containers (e.g. Weblogic send nothing as web-app root
			.addRule()
					.when(Path.matches("").and(Direction.isInbound()).andNot(DispatchType.isForward()))
					.perform(new RedirectHomePage())
			  //only resolve path that begins with fr|en|assets
			 .addRule()
				.when(Path.matches("/{tail}").and(Direction.isInbound()).andNot(DispatchType.isForward()))
				.perform(Forward.to("/404"))
				.where("tail").matches("(?!(fr|en|assets)).*")
			 .addRule()
			 	.when(Path.matches("/{lang}/{path}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path}").to("/{path}"))
			 	.where("lang").matches("fr|en")
	           	.where("path").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path1}/{id1}").to("/{path1}/{id1}"))
			 	.where("lang").matches("fr|en")
	           	.where("path1").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}/{path2}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path1}/{id1}").to("/{path1}/{id1}/{path2}"))
			 	.where("lang").matches("fr|en")
	           	.where("path1").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}/{path2}/{id2}").and(Direction.isInbound()))
			 	.perform(Join.path("/{lang}/{path1}/{id1}/{path2}/{id2}").to("/{path1}/{id1}/{path2}/{id2}"))
			 	.where("lang").matches("fr|en")
	           	.where("path1").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	           	.where("path2").transposedBy(LocaleTransposition.bundle("urlResource","lang"));
	}
	
	@Override
	public int priority() {
		return 10;
	}
	
	/**
	 * HttpOperation to redirect to Explorer home page.
	 * @author cgendreau
	 *
	 */
	private class RedirectHomePage extends HttpOperation{
		public void performHttp(HttpServletRewrite event, EvaluationContext context) {
			String reqLanguage = event.getRequest().getLocale().getLanguage();
			if(!OccurrencePortalConfig.isSupportedLanguage(reqLanguage)){
				reqLanguage = Locale.ENGLISH.getLanguage();
			}
			String landingUrl = I18nUrlBuilder.generateI18nResourcePath(reqLanguage, OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat("search"), (String)null);
			Redirect.permanent(event.getContextPath()+landingUrl).perform(event, context);
		}
	}
}
