package net.canadensys;

/**
 * Holding the global configuration for this WebApp.
 * @author canadensys
 *
 */
public class ServletGlobalConfig {
	
	private static String servletRootPath = null;

	public static String getServletRootPath() {
		return servletRootPath;
	}

	public static void setServletRootPath(String servletRootPath) {
		ServletGlobalConfig.servletRootPath = servletRootPath;
	}
}