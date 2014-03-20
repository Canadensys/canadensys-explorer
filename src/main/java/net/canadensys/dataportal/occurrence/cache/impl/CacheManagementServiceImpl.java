package net.canadensys.dataportal.occurrence.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import net.canadensys.dataportal.occurrence.cache.CacheManagementServiceIF;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig.SearchableFieldEnum;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig.SearchableFieldGroupEnum;
import net.canadensys.query.SearchQueryPart;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * CacheManagementService implementation.
 * @author cgendreau
 *
 */
@Service
public class CacheManagementServiceImpl implements CacheManagementServiceIF{
	
	@Autowired
	private OccurrenceSearchService occurrenceSearchService;
	
	@Autowired
	@Qualifier("searchServiceConfig")
	private SearchServiceConfig searchServiceConfig;
	
	private AtomicLong preLoadedCacheTimestamp = new AtomicLong(0);
	
	@Override
	@Async
	public void preLoadCache(){
		//to avoid any issue with the cache, simply call the service directly.
		Map<String,List<SearchQueryPart>> emptySearchCriteria = new HashMap<String, List<SearchQueryPart>>();
		OccurrenceSearchableField osf = null;
		for(SearchableFieldEnum  currSf : SearchableFieldGroupEnum.CLASSIFICATION.getContent()){
			osf = searchServiceConfig.getSearchableField(currSf);
			occurrenceSearchService.getDistinctValuesCount(emptySearchCriteria, osf);
		}
		for(SearchableFieldEnum  currSf : SearchableFieldGroupEnum.LOCATION.getContent()){
			osf = searchServiceConfig.getSearchableField(currSf);
			occurrenceSearchService.getDistinctValuesCount(emptySearchCriteria, osf);
		}
		preLoadedCacheTimestamp.set(System.currentTimeMillis());
	}
	
	@Override
	public void reloadPreLoadedCache(){
		
		//clear cache
		Cache cache = CacheManager.getCacheManager(CacheManager.DEFAULT_NAME).getCache(CACHE_NAME_DISTINCT_VALUES_COUNT);
		if(cache != null){
			cache.removeAll();
		}
		//reload
		preLoadCache();
	}
	
	@Override
	public long getPreLoadedCacheTimestamp() {
		return preLoadedCacheTimestamp.get();
	}
}
