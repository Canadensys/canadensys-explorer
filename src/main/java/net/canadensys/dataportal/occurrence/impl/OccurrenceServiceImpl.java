package net.canadensys.dataportal.occurrence.impl;

import java.util.List;

import net.canadensys.dataportal.occurrence.OccurrenceService;
import net.canadensys.dataportal.occurrence.cache.CacheManagementServiceIF;
import net.canadensys.dataportal.occurrence.dao.OccurrenceDAO;
import net.canadensys.dataportal.occurrence.dao.OccurrenceExtensionDAO;
import net.canadensys.dataportal.occurrence.dao.ResourceContactDAO;
import net.canadensys.dataportal.occurrence.dao.ResourceDAO;
import net.canadensys.dataportal.occurrence.model.OccurrenceExtensionModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.ResourceContactModel;
import net.canadensys.dataportal.occurrence.model.ResourceModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the Occurrence service layer
 * @author canadensys
 */
@Service("occurrenceService")
public class OccurrenceServiceImpl implements OccurrenceService {
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(OccurrenceServiceImpl.class);
		
	@Autowired
	private OccurrenceDAO occurrenceDAO;
	
	@Autowired
	private OccurrenceExtensionDAO occurrenceExtensionDAO;
	
	@Autowired
	private ResourceContactDAO resourceContactDAO;
	
	@Autowired
	private ResourceDAO resourceDAO;
	
	@Override
	@Transactional(readOnly=true)
	public boolean resourceExists(String resourceName) {
		OccurrenceModel model = new OccurrenceModel();
		model.setSourcefileid(resourceName);
		List<OccurrenceModel> occModelList = occurrenceDAO.search(model, 1);
		return !occModelList.isEmpty();
	}

	@Override
	@Transactional(readOnly=true)
	public boolean datasetExists(String datasetName) {
		OccurrenceModel model = new OccurrenceModel();
		model.setDatasetname(datasetName);
		List<OccurrenceModel> occModelList = occurrenceDAO.search(model, 1);
		return !occModelList.isEmpty();
	}
	
	@Override
	@Transactional(readOnly=true)
	public OccurrenceModel loadOccurrenceModel(String sourcefileid, String dwcaId, boolean loadRawModel) {
		OccurrenceModel occModel = occurrenceDAO.load(sourcefileid, dwcaId, loadRawModel);
		return occModel;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<OccurrenceExtensionModel> loadOccurrenceExtensionModel(String extensionType, String resourceUUID, String dwcaId) {
		return occurrenceExtensionDAO.load(extensionType, resourceUUID, dwcaId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ResourceContactModel loadResourceContactModel(String sourceFileId) {
		List<ResourceContactModel> resourceContactModelList = resourceContactDAO.load(sourceFileId);
		if(resourceContactModelList.size() > 0){
			return resourceContactModelList.get(0);
		}
		return null;
	}

	/**
	 * ResourceModel will be cached after calling this method.
	 * Those models are assumed to be 'almost static' so the current cache invalidation is handled by CacheInvalidationScheduled.
	 * This could potentially cause an issue if a ResourceModel is updated and no harvest are achieved.
	 */
	@Override
	@Transactional(readOnly=true)
	@Cacheable(value=CacheManagementServiceIF.RESOURCE_MODEL_CACHE_KEY, key="#sourcefileid", condition="#sourcefileid != null")
	public ResourceModel loadResourceModel(String sourcefileid) {
		return resourceDAO.load(sourcefileid);
	}
}
