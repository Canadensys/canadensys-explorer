package net.canadensys.dataportal.occurrence.config;

import java.lang.reflect.Field;

import javax.servlet.FilterConfig;

import com.opensymphony.sitemesh.webapp.ContainerTweaks;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

/**
 * SiteMeshFilter override to use our ContainerTweaksOverride.
 * 
 * @author cgendreau
 *
 */
public class SiteMeshFilterOverride extends SiteMeshFilter{
	
	@Override
	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);
		
		Field field;
		try {
			field = SiteMeshFilter.class.getDeclaredField("containerTweaks");
			//yes, we do change a private variable but we don't have the choice.
			field.setAccessible(true);
			field.set(this, new ContainerTweaksOverride(filterConfig.getServletContext().getServerInfo()));
			field.setAccessible(false);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private class ContainerTweaksOverride extends ContainerTweaks{
		boolean isTomcat = false;
		boolean isWebLogic = false;
		
		public ContainerTweaksOverride(String serverInfo){
			isTomcat = (serverInfo.toLowerCase().contains("tomcat"));
			System.out.println("isTomcat??" + isTomcat);
			isWebLogic = (serverInfo.toLowerCase().contains("weblogic"));
			System.out.println("isWebLogic??" + isWebLogic);
		}
		
		@Override
		public boolean shouldAutoCreateSession() {
	        return false;
	    }

		@Override
	    public boolean shouldLogUnhandledExceptions() {
	        return isTomcat;
	    }

		@Override
	    public boolean shouldIgnoreIllegalStateExceptionOnErrorPage() {
	        return isWebLogic;
	    }
	}
}
