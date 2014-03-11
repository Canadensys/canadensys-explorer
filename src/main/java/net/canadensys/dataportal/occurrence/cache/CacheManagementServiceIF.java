package net.canadensys.dataportal.occurrence.cache;

/**
 * Service used to manage Explorer cache.
 * @author cgendreau
 *
 */
public interface CacheManagementService {
	
	/**
	 * Preload elements in cache that are expensive in time to create but cheap in memory to hold.
	 */
	public void preLoadCache();

}
