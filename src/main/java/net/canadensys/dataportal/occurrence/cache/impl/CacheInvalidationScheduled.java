package net.canadensys.dataportal.occurrence.cache.impl;

import net.canadensys.dataportal.occurrence.cache.CacheInvalidationStrategyIF;
import net.canadensys.dataportal.occurrence.cache.CacheManagementServiceIF;
import net.canadensys.dataportal.occurrence.dao.ImportLogDAO;
import net.canadensys.dataportal.occurrence.model.ImportLogModel;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * CacheInvalidation strategy using Spring @Scheduled and ImportLogDAO.
 * At a fixed rate, we get the timestamp of the last import log and update the cache if needed.
 * @author cgendreau
 *
 */
@Component
public class CacheInvalidationScheduled implements CacheInvalidationStrategyIF {
	
	private static final Logger LOGGER = Logger.getLogger(CacheInvalidationScheduled.class);
	private final long CHECK_IMPORT_LOG_DELAY = DateUtils.MILLIS_PER_MINUTE*15;
	
	@Autowired
	private CacheManagementServiceIF cacheManagementService;
	
	@Autowired
	private ImportLogDAO importLogDAO;
	
	@Scheduled(fixedDelay=CHECK_IMPORT_LOG_DELAY)
	@Transactional(readOnly=true)
	public void checkCacheState(){
		ImportLogModel lastImport = importLogDAO.loadLast();
		if(lastImport == null){
			LOGGER.error("Can not check the state of the preLoaded cache. No ImportLogModel found.");
			return;
		}
		
		long lastImportTimestamp = lastImport.getEvent_end_date_time().getTime();
		long cacheTimestamp = cacheManagementService.getPreLoadedCacheTimestamp();
		
		if(lastImportTimestamp > cacheTimestamp){
			cacheManagementService.reloadPreLoadedCache();
		}
	}
}
