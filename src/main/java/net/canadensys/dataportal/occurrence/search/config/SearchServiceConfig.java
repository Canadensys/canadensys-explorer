package net.canadensys.dataportal.occurrence.search.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.canadensys.ServletGlobalConfig;
import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchableField;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class contains configurations related to the Search service in the Occurrence Portal.
 * @author canadensys
 *
 */
public class SearchServiceConfig {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(SearchServiceConfig.class);
		
	private Map<Integer,OccurrenceSearchableField> SEARCHABLE_FIELD_MAP = new HashMap<Integer,OccurrenceSearchableField>();
	//inverted map FieldName=>FieldID
	private Map<String,Integer> freemarkerMap = new HashMap<String, Integer>();
	
	public static final String START_YEAR_PROPERTY = "syear";
	public static final String START_MONTH_PROPERTY = "smonth";
	public static final String START_DAY_PROPERTY = "sday";
	
	private static final String DEFAULT_DOWNLOAD_FOLDER = "download";
	
	private String publicDownloadURL;
	private String generatedContentFolder;
	private String emailSenderAddress;
	
	/**
	 * List of all fields to be included on search result
	 */
	public static final List<String> OCCURRENCE_SEARCH_FIELDS = new ArrayList<String>();
	static{
		OCCURRENCE_SEARCH_FIELDS.add(OccurrencePortalConfig.OCCURRENCE_MANAGED_ID_FIELD);
		OCCURRENCE_SEARCH_FIELDS.add("catalognumber");
		OCCURRENCE_SEARCH_FIELDS.add("scientificname");
		OCCURRENCE_SEARCH_FIELDS.add("family");
		OCCURRENCE_SEARCH_FIELDS.add("country");
		OCCURRENCE_SEARCH_FIELDS.add("stateprovince");
		OCCURRENCE_SEARCH_FIELDS.add("collectioncode");
		OCCURRENCE_SEARCH_FIELDS.add("locality");
		OCCURRENCE_SEARCH_FIELDS.add("habitat");
		OCCURRENCE_SEARCH_FIELDS.add("syear");
		OCCURRENCE_SEARCH_FIELDS.add("hascoordinates");
		OCCURRENCE_SEARCH_FIELDS.add("hasmedia");
	}
	
	/**
	 * List of all fields to be included on occurrence summary
	 */
	public static final List<String> OCCURENCE_SUMMARY_FIELDS = new ArrayList<String>();
	static{
		OCCURENCE_SUMMARY_FIELDS.add("associatedmedia");
		OCCURENCE_SUMMARY_FIELDS.add("datasetname");
		OCCURENCE_SUMMARY_FIELDS.add("decimallatitude");
		OCCURENCE_SUMMARY_FIELDS.add("decimallongitude");
		OCCURENCE_SUMMARY_FIELDS.add("catalognumber");
		OCCURENCE_SUMMARY_FIELDS.add("country");
		OCCURENCE_SUMMARY_FIELDS.add("_class");
		OCCURENCE_SUMMARY_FIELDS.add("eday");
		OCCURENCE_SUMMARY_FIELDS.add("emonth");
		OCCURENCE_SUMMARY_FIELDS.add("eyear");
		OCCURENCE_SUMMARY_FIELDS.add("genus");
		OCCURENCE_SUMMARY_FIELDS.add("habitat");
		OCCURENCE_SUMMARY_FIELDS.add("family");
		OCCURENCE_SUMMARY_FIELDS.add("institutioncode");
		OCCURENCE_SUMMARY_FIELDS.add("kingdom");
		OCCURENCE_SUMMARY_FIELDS.add("locality");
		OCCURENCE_SUMMARY_FIELDS.add("minimumelevationinmeters");
		OCCURENCE_SUMMARY_FIELDS.add("maximumelevationinmeters");
		OCCURENCE_SUMMARY_FIELDS.add("phylum");
		OCCURENCE_SUMMARY_FIELDS.add("_order");
		OCCURENCE_SUMMARY_FIELDS.add("scientificname");
		OCCURENCE_SUMMARY_FIELDS.add("recordedby");
		OCCURENCE_SUMMARY_FIELDS.add("recordnumber");
		OCCURENCE_SUMMARY_FIELDS.add("sday");
		OCCURENCE_SUMMARY_FIELDS.add("smonth");
		OCCURENCE_SUMMARY_FIELDS.add("syear");
		OCCURENCE_SUMMARY_FIELDS.add("stateprovince");
		OCCURENCE_SUMMARY_FIELDS.add("scientificnameauthorship");
		OCCURENCE_SUMMARY_FIELDS.add("rawscientificname");
		OCCURENCE_SUMMARY_FIELDS.add("_references");
		OCCURENCE_SUMMARY_FIELDS.add("sourcefileid");
		OCCURENCE_SUMMARY_FIELDS.add("dwcaid");
	}
	
	public SearchServiceConfig(){
		SEARCHABLE_FIELD_MAP.put(1,
				new OccurrenceSearchableFieldBuilder(1,"country").singleValue("country",String.class).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(2,
			new OccurrenceSearchableFieldBuilder(2,"family").singleValue("family",String.class).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(3,
				new OccurrenceSearchableFieldBuilder(3,"continent").singleValue("continent",String.class).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(4,
				new OccurrenceSearchableFieldBuilder(4,"taxonrank").singleValue("taxonrank",String.class).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(5,
				new OccurrenceSearchableFieldBuilder(5,"daterange").startEndDate(START_YEAR_PROPERTY,START_MONTH_PROPERTY,START_DAY_PROPERTY).eqOperator().betweenOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(6,
				new OccurrenceSearchableFieldBuilder(6,"catalognumber").singleValue("catalognumber",String.class).likeOperator(QueryOperatorEnum.ELIKE).toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(7,
				new OccurrenceSearchableFieldBuilder(7,"collectioncode").singleValue("collectioncode",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(8,
				new OccurrenceSearchableFieldBuilder(8,"datasetname").singleValue("datasetname",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(9,
				new OccurrenceSearchableFieldBuilder(9,"stateprovince").singleValue("stateprovince",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(10,
				new OccurrenceSearchableFieldBuilder(10,"altituderange").minMaxNumber("minimumelevationinmeters", "maximumelevationinmeters", Double.class).eqOperator().betweenOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(11,
				new OccurrenceSearchableFieldBuilder(11,"locality").singleValue("locality",String.class).likeOperator(QueryOperatorEnum.CLIKE).toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(12,
				new OccurrenceSearchableFieldBuilder(12,"recordedby").singleValue("recordedby",String.class).likeOperator(QueryOperatorEnum.CLIKE).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(13,
				new OccurrenceSearchableFieldBuilder(13,"recordnumber").singleValue("recordnumber",String.class).likeOperator(QueryOperatorEnum.ELIKE).toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(14,
				new OccurrenceSearchableFieldBuilder(14,"kingdom").singleValue("kingdom",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(15,
				new OccurrenceSearchableFieldBuilder(15,"_order").singleValue("_order",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(16,
				new OccurrenceSearchableFieldBuilder(16,"scientificname").singleValue("scientificname",String.class).eqOperator().likeOperator(QueryOperatorEnum.SLIKE).supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(17,
				new OccurrenceSearchableFieldBuilder(17,"institutioncode").singleValue("institutioncode",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(18,
				new OccurrenceSearchableFieldBuilder(18,"_class").singleValue("_class",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(19,
				new OccurrenceSearchableFieldBuilder(19,"phylum").singleValue("phylum",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(20,
				new OccurrenceSearchableFieldBuilder(20,"hascoordinates").singleValue("hascoordinates",Boolean.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(21,
				new OccurrenceSearchableFieldBuilder(21,"hasmedia").singleValue("hasmedia",Boolean.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(22,
				new OccurrenceSearchableFieldBuilder(22,"county").singleValue("county",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(23,
				new OccurrenceSearchableFieldBuilder(23,"municipality").singleValue("municipality",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(28,
				new OccurrenceSearchableFieldBuilder(28,"boundingbox").geoCoordinates("the_geom").inOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(29,
				new OccurrenceSearchableFieldBuilder(29,"sourcefileid").singleValue("sourcefileid",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		
		//Those searchable fields are used for stats only
		SEARCHABLE_FIELD_MAP.put(24,
				new OccurrenceSearchableFieldBuilder(24,"genus").singleValue("genus",String.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(25,
				new OccurrenceSearchableFieldBuilder(25,"species").singleValue("species",String.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(26,
				new OccurrenceSearchableFieldBuilder(26,"decade").singleValue("decade",Integer.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(27,
				new OccurrenceSearchableFieldBuilder(27,"averagealtituderounded").singleValue("averagealtituderounded",Integer.class).eqOperator().toOccurrenceSearchableField());
		
		SEARCHABLE_FIELD_MAP = Collections.unmodifiableMap(SEARCHABLE_FIELD_MAP);
		
		createFreemarkerList();
	}
	
	@PostConstruct
	public void init(){
		if(StringUtils.isBlank(generatedContentFolder)){
			generatedContentFolder = FilenameUtils.concat(ServletGlobalConfig.getServletRootPath(),DEFAULT_DOWNLOAD_FOLDER);
		}
	}

	/**
	 * This is an inverted map for Freemarker to make sure the UI will use the same id as the backend.
	 */
	private void createFreemarkerList(){
		for(OccurrenceSearchableField currSearchableField : SEARCHABLE_FIELD_MAP.values()){
			freemarkerMap.put(currSearchableField.getSearchableFieldName(), currSearchableField.getSearchableFieldId());
		}
	}
	
	/**
	 * Get the SearchableField Map<id,SearchableField object>.
	 * @return the SearchableField map
	 */
	public Map<Integer,OccurrenceSearchableField> getSearchableFieldMap() {
		return SEARCHABLE_FIELD_MAP;
	}
	
	/**
	 * Get the OccurrenceSearchableField by its id.
	 * @param id
	 * @return the OccurrenceSearchableField or null if this id could not be found
	 */
	public OccurrenceSearchableField getSearchableFieldbyId(Integer id) {
		return SEARCHABLE_FIELD_MAP.get(id);
	}

	public SearchableField getSearchableFieldbyName(String name) {
		for(SearchableField curr : SEARCHABLE_FIELD_MAP.values()){
			if(curr.getSearchableFieldName().equalsIgnoreCase(name)){
				return curr;
			}
		}
		return null;
	}
	

	/**
	 * Get the Freemarker ready SearchableField map.
	 * This is an inverted map to make sure Freemarker uses the right id.
	 * @return inverted map FieldName=>FieldID
	 */
	public Map<String,Integer> getFreemarkerSearchableFieldMap(){
		return freemarkerMap;
	}
	
	public String getGeneratedContentFolder() {
		return generatedContentFolder;
	}
	public void setGeneratedContentFolder(String generatedContentFolder) {
		this.generatedContentFolder = generatedContentFolder;
	}

	public String getPublicDownloadURL() {
		return publicDownloadURL;
	}
	public void setPublicDownloadURL(String publicDownloadURL) {
		this.publicDownloadURL = publicDownloadURL;
	}
	
	public void setEmailSenderAdresse(String emailSenderAdresse){
		this.emailSenderAddress = emailSenderAdresse;
	}
	public String getEmailSenderAdresse(){
		return emailSenderAddress;
	}
	
}
