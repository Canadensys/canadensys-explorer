package net.canadensys.dataportal.occurrence.cache.impl;

import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

/**
 * CacheManagementService implementation.
 * @author cgendreau
 *
 */
@Service
public class CacheManagementServiceImpl implements CacheManagementServiceIF{
	
	//cache keys managed by this CacheManagementServiceIF implementation
	public static final List<String> MANAGED_CACHE_KEY = new ArrayList<String>();
	static{
		MANAGED_CACHE_KEY.add(DISTINCT_VALUES_COUNT_CACHE_KEY);
		MANAGED_CACHE_KEY.add(RESOURCE_METADATA_MODEL_CACHE_KEY);
		MANAGED_CACHE_KEY.add(DWCA_RESOURCE_MODEL_CACHE_KEY);
	}
	
	@Autowired
	private OccurrenceSearchService occurrenceSearchService;
	
	@Autowired
	@Qualifier("searchServiceConfig")
	private SearchServiceConfig searchServiceConfig;
	
	private AtomicLong cacheTimestamp = new AtomicLong(0);
	
	/**
	 * Blocking function, caller is responsible to run this in a thread if needed.
	 */
	@Override
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
	}
	
	@Override
	public long getCacheTimestamp() {
		return cacheTimestamp.get();
	}

	@Override
	public void purgeCache() {
		Cache cache = null;
		for(String currCacheKey : MANAGED_CACHE_KEY){
			cache = CacheManager.getCacheManager(CacheManager.DEFAULT_NAME).getCache(currCacheKey);
			if(cache != null){
				cache.removeAll();
			}
		}
		cacheTimestamp.set(System.currentTimeMillis());
		
		//reload the preLoaded cache
		preLoadCache();
	}
}
