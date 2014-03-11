package net.canadensys.dataportal.occurrence.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service is only used on startup to trigger the loading of initial cache content.
 * Due to AOP limitation, we have this Service to call the real Service.
 * From Spring doc: "To asynchronously initialize Spring beans you currently have to use a 
 * separate initializing Spring bean that invokes the @Async annotated method on the target then."
 * @author cgendreau
 *
 */
@Service
public class CacheInitializationService implements InitializingBean{
	
	@Autowired
	private CacheManagementServiceIF cacheManagementService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		cacheManagementService.preLoadCache();
	}
}
