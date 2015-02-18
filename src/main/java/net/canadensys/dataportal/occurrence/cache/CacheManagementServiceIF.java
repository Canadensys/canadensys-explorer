package net.canadensys.dataportal.occurrence.cache;


/**
 * Service used to manage Explorer cache.
 * @author cgendreau
 *
 */
public interface CacheManagementServiceIF {
	
	// Must be defined in ehcache.xml file
	public static final String DISTINCT_VALUES_COUNT_CACHE_KEY = "distinctValuesCountCache";
	public static final String DWCA_RESOURCE_MODEL_CACHE_KEY = "dwcaResourceModelCache";
	public static final String RESOURCE_METADATA_MODEL_CACHE_KEY = "resourceMetadataCache";
		
	/**
	 * Preload elements in cache that are expensive in time to create but cheap in memory to hold.
	 */
	public void preLoadCache();

	
	/**
	 * Purge all elements in the cache managed by the CacheManagementServiceIF implementation.
	 * The implementation could also trigger other methods (e.g. reload a pre-loaded cache)
	 */
	public void purgeCache();
	

	/**
	 * Get the timestamp of the current cache content. The timestamp is defined by when the cache was last purged.
	 * The initial value is 0.
	 * @return
	 */
	public long getCacheTimestamp();

}
