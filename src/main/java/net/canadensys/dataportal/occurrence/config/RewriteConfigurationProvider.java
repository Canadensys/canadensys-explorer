package net.canadensys.dataportal.occurrence.config;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.springframework.stereotype.Component;

@Component
@RewriteConfiguration
public class RewriteConfigurationProvider extends HttpConfigurationProvider{

//	static Pattern notStartingWithLangPattern = Pattern.compile("/(?!(fr|en)).*");
//	static Pattern isNotAssetPattern = Pattern.compile("/(?!(assets)).*");
	
	/**
	 * Send the language as lang= to Spring LocaleChangeInterceptor.
	 */
	@Override
	public Configuration getConfiguration(final ServletContext context) {
		 return ConfigurationBuilder.begin()
//			---BACKWARD compatibility rule---
//			 .addRule()
//			 	.when(Direction.isInbound().and(new HttpCondition() {
//                  @Override
//                  public boolean evaluateHttp(HttpServletRewrite event, EvaluationContext context)
//                  {
//                	  if(DispatchType.isForward().evaluate(event, context)){
//                		  return false;
//                	  }
//                	  
//                	  String url = event.getInboundAddress().getPath();
//                	  if (url.startsWith(event.getContextPath()))
//                	         url = url.substring(event.getContextPath().length());
//                	  if((isNotAssetPattern.matcher(url).matches() && notStartingWithLangPattern.matcher(url).matches())){
//                		  System.out.println("REDIRECT :"+url);
//                	  }
//                	  return (isNotAssetPattern.matcher(url).matches() && notStartingWithLangPattern.matcher(url).matches());
//                  }
//               }).and(Path.matches("/{tail}")))
//               .perform(Redirect.permanent(context.getContextPath() +"/en/{tail}"))
//               .where("tail").matches(".*")
		 
			 .addRule()
			 	.when(Path.matches("/{lang}/{path}"))
			 	.perform(Join.path("/{lang}/{path}").to("/{path}"))
			 	.where("lang").matches("fr|en")
	           	.where("path").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}"))
			 	.perform(Join.path("/{lang}/{path1}/{id1}").to("/{path1}/{id1}"))
			 	.where("lang").matches("fr|en")
	           	.where("path1").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}/{path2}"))
			 	.perform(Join.path("/{lang}/{path1}/{id1}").to("/{path1}/{id1}/{path2}"))
			 	.where("lang").matches("fr|en")
	           	.where("path1").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	         .addRule()
			 	.when(Path.matches("/{lang}/{path1}/{id1}/{path2}/{id2}"))
			 	.perform(Join.path("/{lang}/{path1}/{id1}/{path2}/{id2}").to("/{path1}/{id1}/{path2}/{id2}"))
			 	.where("lang").matches("fr|en")
	           	.where("path1").transposedBy(LocaleTransposition.bundle("urlResource","lang"))
	           	.where("path2").transposedBy(LocaleTransposition.bundle("urlResource","lang"));
		     //this rule covers paths like /fr/r/acad-specimens/ACAD-1
//			 .addRule()
//			 	.when(Path.matches("/{lang}/{type}/{tail}"))
//			 	.perform(Join.path("/{lang}/{type}/{tail}").to("/{type}/{tail}"))
//			 	.where("lang").matches("fr|en")
//			 	.where("tail").matches(".*");
	}
	
	@Override
	public int priority() {
		return 10;
	}
}
