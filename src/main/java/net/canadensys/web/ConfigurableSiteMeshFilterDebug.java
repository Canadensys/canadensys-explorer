package net.canadensys.web;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class ConfigurableSiteMeshFilterDebug extends ConfigurableSiteMeshFilter {

	@Override
	 protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		//builder.setIncludeErrorPages(true);
		 System.out.println("include error page??????"+builder.isIncludeErrorPages());
	    }
}
