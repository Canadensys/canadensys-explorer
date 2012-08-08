package net.canadensys.dataportal.occurrence;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;

/**
 * OccurrenceService layer interface to access occurrence related data. This interface handles only high-level methods.
 * @author canadensys
 *
 */
public interface OccurrenceService {
	
	/**
	 * Checks if a dataset exists or not in the database
	 * @param dataset
	 * @return the dataset exists or not
	 */
	public boolean datasetExists(String dataset);
	
	/**
	 * Load an occurrence model based on the unique key dataset(sourcefileid)/occurrenceId 
	 * @param dataset unique within the portal
	 * @param occurrenceId unique within the dataset
	 * @return
	 */
	public OccurrenceModel loadOccurrenceModel(String dataset, String occurrenceId, boolean loadRawModel);

}
