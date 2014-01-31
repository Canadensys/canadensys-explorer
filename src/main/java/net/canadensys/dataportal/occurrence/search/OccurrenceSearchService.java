/*
	Copyright (c) 2012 Canadensys
*/
package net.canadensys.dataportal.occurrence.search;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canadensys.chart.ChartModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.query.LimitedResult;
import net.canadensys.query.SearchQueryPart;

/**
 * Interface for the Search service layer
 * @author canadensys
 */
public interface OccurrenceSearchService {
	//some enum to ease parameters passing
	public enum DownloadPropertiesEnum {EMAIL,LOCALE,SEARCH_URL}
	public enum StatsPropertiesEnum {RESOURCE_BUNDLE,MAX_RESULT}
	
	public List<OccurrenceModel> search(HashMap<String, List<String>> searchCriteria);
	public LimitedResult<List<Map<String, String>>> searchWithLimit(Map<String, List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Get the occurrence summary object
	 * @param auto_id
	 * @return
	 */
	public OccurrenceModel getOccurrenceSummary(int auto_id);
	
	/**
	 * Count the number of occurrence for these searchCriteria
	 * @param searchCriteria
	 * @return
	 */
	public int getOccurrenceCount(Map<String, List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Count the number of georeferenced occurrence for these searchCriteria
	 * @param searchCriteria
	 * @return
	 */
	public int getGeoreferencedOccurrenceCount(Map<String, List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Build a SearchQueryPart for a specific field defined in SearchServiceConfig.
	 * The SearchQueryPart will be built with an EQ operator for String.
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public SearchQueryPart getSearchQueryPartFromFieldName(String fieldName, String fieldValue);
	
	
	/**
	 * Get all values and their count for a specific searchCriteria/column.
	 * @param searchCriteria
	 * @param column
	 * @param extraProperties (see implementation)
	 * @return 
	 */
	public ChartModel getValuesFrequencyDistribution(Map<String, List<SearchQueryPart>> searchCriteria, OccurrenceSearchableField column, Map<StatsPropertiesEnum,Object> extraProperties);
	
	
	/**
	 * Returns the number of distinct values for a specific searchCriteria/field.
	 * @param searchCriteria
	 * @param column
	 * @return
	 */
	public Integer getDistinctValuesCount(Map<String, List<SearchQueryPart>> searchCriteria, OccurrenceSearchableField column);
	
	/**
	 * Search and get the result as DarwinCore archive. This method will decide if we should return the file
	 * immediately or create it asynchronously.
	 * @param searchCriteria
	 * @param fileName the file name only (not the path)
	 * @param email 
	 * @return 
	 */
	public DownloadResultStatus searchAsDwca(Map<String, List<SearchQueryPart>> searchCriteria, String fileName, Map<DownloadPropertiesEnum,Object> extraProperties);
	public File getResultAsDwca(Map<String, List<SearchQueryPart>> searchCriteria, String fileName);
	public void getResultAsDwcaAsync(Map<String, List<SearchQueryPart>> searchCriteria, String fileName, Map<DownloadPropertiesEnum,Object> extraProperties);
	
	/**
	 * Get the SQL query of this searchCriteria to display the correct map.
	 * @param searchCriteria
	 * @return
	 */
	public String getMapQuery(Map<String, List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Get the map center coordinates for a query.
	 * @param sqlQuery
	 * @return long,lat
	 */
	public String[] getMapCenter(String sqlQuery);
	
	/**
	 * This is not used anymore. We will keep this for now in case we need to change our map service.
	 * @param id id of the occurrence in the map context
	 * @return The summary of the occurrence in the map context as JSON
	 */
//	public String getMapOccurrenceSummary(int id);
	
}
