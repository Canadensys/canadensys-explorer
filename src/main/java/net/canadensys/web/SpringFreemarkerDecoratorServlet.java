package net.canadensys.web;

import javax.servlet.ServletException;

import com.opensymphony.module.sitemesh.freemarker.FreemarkerDecoratorServlet;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModelException;

/**
 * Light FreemarkerDecoratorServlet override to add our custom URLHelper.
 * At some point we could try to reuse the Bean created by Spring.
 * 
 *  Autorired
 *  Configuration freemarkerConfiguration;
 *  
 *  TemplateLoader templateLoader = freemarkerConfiguration.getTemplateLoader();
 *  getConfiguration().setTemplateLoader(templateLoader);
 * @author canadensys
 *
 */
public class SpringFreemarkerDecoratorServlet extends FreemarkerDecoratorServlet {

	private static final long serialVersionUID = 1942463095708194219L;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			getConfiguration().setSharedVariable("URLHelper",
					BeansWrapper.getDefaultInstance().getStaticModels().get("net.canadensys.web.freemarker.FreemarkerURLHelper"));
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}
}
