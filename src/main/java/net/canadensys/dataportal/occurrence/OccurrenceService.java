package net.canadensys.dataportal.occurrence;

import java.util.List;

import net.canadensys.dataportal.occurrence.model.DwcaResourceModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceExtensionModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.ResourceMetadataModel;

/**
 * OccurrenceService layer interface to access occurrence related data. This interface handles only high-level methods.
 * @author canadensys
 *
 */
public interface OccurrenceService {
	
	/**
	 * Checks if a resourceName(sourcefileid) exists or not in the database.
	 * @param dataset
	 * @return the resourceName exists or not
	 */
	public boolean resourceExists(String resourceName);
	
	/**
	 * Checks if a dataset exists or not in the database.
	 * @param dataset
	 * @return the resourceName exists or not
	 */
	public boolean datasetExists(String datasetName);
	
	/**
	 * Load an occurrence model based on the unique key sourcefileid/dwcaId 
	 * @param sourcefileid unique within the portal
	 * @param dwcaId unique within the dataset
	 * @return
	 */
	public OccurrenceModel loadOccurrenceModel(String sourcefileid, String dwcaId, boolean loadRawModel);
	
	/**
	 * Load all OccurrenceExtensionModel of the specified type based on the unique key sourceFileId/dwcaId.
	 * @param extensionType
	 * @param sourceFileId
	 * @param dwcaId
	 * @return
	 */
	public List<OccurrenceExtensionModel> loadOccurrenceExtensionModel(String extensionType, String sourceFileId, String dwcaId);
	
	/**
	 * Load a ResourceMetadataModel based on the resourceId.
	 * 
	 * @param resourceId
	 * @return
	 */
	public ResourceMetadataModel loadResourceMetadata(Integer resourceId);
	
	/**
	 * Load a DwcaResourceModel based on its id.
	 * 
	 * @param resourceId
	 * @return
	 */
	public DwcaResourceModel loadDwcaResource(Integer resourceId);

}
