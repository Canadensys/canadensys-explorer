package net.canadensys.dataportal.occurrence.cache;

/**
 * Service used to manage Explorer cache.
 * @author cgendreau
 *
 */
public interface CacheManagementServiceIF {
	
	public static final String CACHE_NAME_DISTINCT_VALUES_COUNT = "distinctValuesCountCache";
	
	/**
	 * Preload elements in cache that are expensive in time to create but cheap in memory to hold.
	 */
	public void preLoadCache();
	
	/**
	 * Reload elements in cache that are expensive in time to create but cheap in memory to hold.
	 */
	public void reloadPreLoadedCache();
	
	/**
	 * Get the timestamp of the current preloaded cache content.
	 * @return
	 */
	public long getPreLoadedCacheTimestamp();

}
