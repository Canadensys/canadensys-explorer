package net.canadensys;

import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.mapping.Set;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;

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
	
	/**
	 * Close the C3P0 connections explicitly, it seems to leak.
	 * If we don't, we get this error:
	 * The web application [/explorer] appears to have started a thread named [com.mchange.v2.async.ThreadPoolAsynchronousRunner$PoolThread-#2] but has failed to stop it. This is very likely to create a memory leak.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void destroy() {
		Iterator<Set> it = C3P0Registry.getPooledDataSources().iterator();
		PooledDataSource dataSource;
		while (it.hasNext()) {
		    try {
		           dataSource = (PooledDataSource) it.next();
		           dataSource.close();
		    } catch (Exception e) {
		    	LOGGER.error(e);
		    }
		}
	}
}