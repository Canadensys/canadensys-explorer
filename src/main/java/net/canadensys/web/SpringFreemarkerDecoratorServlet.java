package net.canadensys.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.TemplateModelException;

/**
 * Light FreemarkerDecoratorServlet override to add our custom URLHelper.
 * At some point we should try to wrap it using Spring ServletWrappingController.
 * 
 * <bean id="freemarkerWrapperServletController" class="org.springframework.web.servlet.mvc.ServletWrappingController">
 * 	<property name="servletClass" value="net.canadensys.web.SpringFreemarkerDecoratorServlet" />
 * 	<property name="servletName" value="sitemesh-freemarker" />
 * 	<property name="initParameters">
 * 		<props>
 * 			<prop key="TemplatePath">/</prop><!--this is ignored by our custom implementation but keep it here-->
 * 			<prop key="default_encoding">ISO-8859-1</prop>
 * 		</props>
 * 	</property>
 * </bean>
 * So we could use:
 *  Autowired
 *  Configuration freemarkerConfiguration;
 *  
 *  TemplateLoader templateLoader = freemarkerConfiguration.getTemplateLoader();
 *  getConfiguration().setTemplateLoader(templateLoader);
 * @author canadensys
 *
 */
public class SpringFreemarkerDecoratorServlet extends FreemarkerServlet {

	private static final long serialVersionUID = 1942463095708194219L;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			getConfiguration().setSharedVariable("URLHelper",
					BeansWrapper.getDefaultInstance().getStaticModels().get("net.canadensys.web.freemarker.FreemarkerURLHelper"));
			
			//Since we are running in a different Servlet context we need to load the config ourself.
			Properties prop = new Properties();
			InputStream in = getServletContext().getResourceAsStream("/WEB-INF/portal-config.properties");
			if(in != null){
				prop.load(in);
				in.close();
			}

			getConfiguration().setSharedVariable("gaSiteVerification", StringUtils.defaultString(prop.getProperty("googleanalytics.siteVerification")));
			getConfiguration().setSharedVariable("gaAccount", StringUtils.defaultString(prop.getProperty("googleanalytics.account")));
		} catch (TemplateModelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
