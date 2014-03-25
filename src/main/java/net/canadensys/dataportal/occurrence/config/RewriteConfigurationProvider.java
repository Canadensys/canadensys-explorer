package net.canadensys.dataportal.occurrence.config;

import javax.servlet.ServletContext;

import net.canadensys.web.i18n.I18nUrlBuilder;
import net.canadensys.web.i18n.annotation.I18nTranslationHandler;

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

	private static final I18nTranslationHandler I18N_TRANSLATION_HANDLER = new I18nTranslationHandler("net.canadensys.dataportal.occurrence.controller");
	
	@Override
	public Configuration getConfiguration(final ServletContext context) {
		 return ConfigurationBuilder.begin()
			 //handle root path
			 .addRule()
					.when(Path.matches("/").and(Direction.isInbound()).andNot(DispatchType.isForward()))
					.perform(new HttpOperation() {
						@Override
						public void performHttp(HttpServletRewrite event, EvaluationContext context) {
							String lang = event.getRequest().getLocale().getLanguage();
							String landingUrl = I18nUrlBuilder.generateI18nResourcePath(lang,I18N_TRANSLATION_HANDLER.getTranslationFormat("search"),(String)null);
							Redirect.permanent(event.getContextPath()+landingUrl).perform(event, context);
						}
					})
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
}
