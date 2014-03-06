package net.canadensys.dataportal.occurrence.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


/**
 * Configuration class for Explorer caching.
 * @author cgendreau
 *
 */
@Configuration
@EnableCaching
public class CachingConfig {
	
	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
	    EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
	    ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
	    return ehCacheManagerFactoryBean;
	}
	
	@Bean
	public CacheManager cacheManager() {
	    EhCacheCacheManager cacheManager = new EhCacheCacheManager();
	    cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
	    return cacheManager;
	}
}
