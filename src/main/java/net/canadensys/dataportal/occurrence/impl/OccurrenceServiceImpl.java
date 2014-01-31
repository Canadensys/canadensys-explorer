package net.canadensys.dataportal.occurrence.impl;

import java.util.List;

import net.canadensys.dataportal.occurrence.OccurrenceService;
import net.canadensys.dataportal.occurrence.dao.OccurrenceDAO;
import net.canadensys.dataportal.occurrence.dao.ResourceContactDAO;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.ResourceContactModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	private ResourceContactDAO resourceContactDAO;
	
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
	public ResourceContactModel loadResourceContactModel(String sourceFileId) {
		List<ResourceContactModel> resourceContactModelList = resourceContactDAO.load(sourceFileId);
		if(resourceContactModelList.size() > 0){
			return resourceContactModelList.get(0);
		}
		return null;
	}
}
