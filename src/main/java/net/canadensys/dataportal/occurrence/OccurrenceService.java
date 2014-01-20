package net.canadensys.dataportal.occurrence;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.ResourceContactModel;

/**
 * OccurrenceService layer interface to access occurrence related data. This interface handles only high-level methods.
 * @author canadensys
 *
 */
public interface OccurrenceService {
	
	/**
	 * Checks if a resourceName exists or not in the database.
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
	 * Load an occurrence model based on the unique key dataset(sourcefileid)/dwcaId 
	 * @param datasetShortName unique within the portal
	 * @param dwcaId unique within the dataset
	 * @return
	 */
	public OccurrenceModel loadOccurrenceModel(String datasetShortName, String dwcaId, boolean loadRawModel);
	
	/**
	 * Load a ResourceContactModel based on the datasetShortName
	 * @param datasetShortName
	 * @return
	 */
	public ResourceContactModel loadResourceContactModel(String datasetShortName);

}
