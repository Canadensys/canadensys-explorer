package net.canadensys;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class contains calls to initialization methods.
 * This class will be called at each deploy of the WebApp
 */
public class ServletInitializer extends HttpServlet{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(ServletInitializer.class);
	private static final long serialVersionUID = 800750579657221432L;
	
	@Override
	public void init() throws ServletException {
		//this method is working fine with the current version of Tomcat (6) but could not be reliable
		//on another Application Server
		ServletGlobalConfig.setServletRootPath(getServletContext().getRealPath(""));
		
		if(StringUtils.isBlank(ServletGlobalConfig.getServletRootPath())){
			LOGGER.fatal("ServletRootPath is null or empty, this could affect some modules");
		}
    }
}