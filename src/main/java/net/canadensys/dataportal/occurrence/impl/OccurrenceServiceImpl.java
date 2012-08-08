package net.canadensys.dataportal.occurrence.impl;

import java.util.List;

import net.canadensys.dataportal.occurrence.OccurrenceService;
import net.canadensys.dataportal.occurrence.dao.OccurrenceDAO;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;

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
	
	@Override
	@Transactional(readOnly=true)
	public boolean datasetExists(String dataset) {
		OccurrenceModel model = new OccurrenceModel();
		model.setSourcefileid(dataset);
		List<OccurrenceModel> occModelList = occurrenceDAO.search(model, 1);
		return !occModelList.isEmpty();
	}
	
	@Override
	@Transactional(readOnly=true)
	public OccurrenceModel loadOccurrenceModel(String dataset, String occurrenceId, boolean loadRawModel) {
		OccurrenceModel occModel = occurrenceDAO.load(dataset, occurrenceId, loadRawModel);
		return occModel;
	}

}
