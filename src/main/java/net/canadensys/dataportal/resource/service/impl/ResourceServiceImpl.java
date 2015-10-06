package net.canadensys.dataportal.resource.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.dataportal.occurrence.dao.DwcaResourceDAO;
import net.canadensys.dataportal.occurrence.dao.ResourceMetadataDAO;
import net.canadensys.dataportal.occurrence.model.DwcaResourceModel;
import net.canadensys.dataportal.resource.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the resources service layer
 * 
 * @author Pedro Guimarães
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private DwcaResourceDAO resourceDAO;

	@Autowired
	private ResourceMetadataDAO resourceInformationDAO;

	/**
	 * Fetch resource given its auto_id:
	 */
	@Override
	@Transactional(readOnly = true)
	public DwcaResourceModel loadResource(String auto_id) {
		return resourceDAO.load(Integer.parseInt(auto_id));
	}

	/**
	 * ResourceModel will be cached after calling this method. Those models are
	 * assumed to be 'almost static' so the current cache invalidation is
	 * handled by CacheInvalidationScheduled. This could potentially cause an
	 * issue if a ResourceModel is updated and no harvest are achieved.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DwcaResourceModel> loadResources() {
		return resourceDAO.loadResources();
	}

	public List<DwcaResourceModel> filterResourcesWithoutRecords(
			List<DwcaResourceModel> resources) {
		ArrayList<DwcaResourceModel> filledResources = new ArrayList<DwcaResourceModel>();
		for (DwcaResourceModel resource : resources) {
			if (resource.getRecord_count() > 0) {
				filledResources.add(resource);
			}
		}
		return filledResources;
	}
}
