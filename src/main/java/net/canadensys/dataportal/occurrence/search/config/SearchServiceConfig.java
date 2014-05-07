package net.canadensys.dataportal.occurrence.search.config;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	public enum SearchableFieldEnum{
		COUNTRY(1),FAMILY(2),CONTINENT(3),TAXONRANK(4),
		DATE_RANGE(5),CATALOG_NUMBER(6),COLLECTION_CODE(7),
		DATASET_NAME(8),STATE_PROVINCE(9),ALTITUDE_RANGE(10),
		LOCALITY(11),RECORDED_BY(12),RECORD_NUMBER(13),
		KINGDOM(14),ORDER(15),SCIENTIFIC_NAME(16),
		INSTITUTION_CODE(17),
		CLASS(18),PHYLUM(19),HAS_COORDINATES(20),HAS_MEDIA(21),
		COUNTY(22),MUNICIPALITY(23),GENUS(24),SPECIES(25),
		DECADE(26),
		AVERAGE_ALTITUDE_ROUNDED(27),
		SOURCE_FILE_ID(29),
		START_YEAR(30),HAS_TYPE_STATUS(31),WKT(32),ELLIPSE(33)
		;
		
		private int id;
		private SearchableFieldEnum(int id){
			this.id = id;
		}
		
		/**
		 * Get a SearchableFieldEnum from its identifier.
		 * @param id
		 * @return SearchableFieldEnum or null if id can not be found within the enum.
		 */
		public static SearchableFieldEnum fromIdentifier(int id){
			for(SearchableFieldEnum curr : SearchableFieldEnum.values()){
				if(curr.id == id){
					return curr;
				}
			}
			return null;
		}
	}
	

	/**
	 * Enum that includes groups of different SearchableField.
	 * @author cgendreau
	 *
	 */
	public enum SearchableFieldGroupEnum{
		CLASSIFICATION(SearchableFieldEnum.KINGDOM,
			SearchableFieldEnum.PHYLUM,SearchableFieldEnum.CLASS,
			SearchableFieldEnum.ORDER,SearchableFieldEnum.FAMILY,
			SearchableFieldEnum.GENUS,SearchableFieldEnum.SCIENTIFIC_NAME),
		LOCATION(SearchableFieldEnum.CONTINENT,
			SearchableFieldEnum.COUNTRY,SearchableFieldEnum.STATE_PROVINCE,
			SearchableFieldEnum.COUNTY,SearchableFieldEnum.MUNICIPALITY),
		DATE(SearchableFieldEnum.DECADE),
		ALTITUDE(SearchableFieldEnum.AVERAGE_ALTITUDE_ROUNDED);
		
		private List<SearchableFieldEnum> content;
		private SearchableFieldGroupEnum(SearchableFieldEnum ... groupContent){
			content = Arrays.asList(groupContent);
		}
		
		/**
		 * Return the content of this SearchableField group
		 * @return
		 */
		public List<SearchableFieldEnum> getContent(){
			return content;
		}
		
		/**
		 * Get a SearchableFieldGroupEnum from a String representation.
		 * Matching is done against the name of the declared enum element.
		 * Case-insensitive, null safe.
		 * @param str
		 * @return SearchableFieldGroupEnum or null if str can not be found within the enum.
		 */
		public static SearchableFieldGroupEnum fromIdentifier(String str){
			for(SearchableFieldGroupEnum curr : SearchableFieldGroupEnum.values()){
				if(curr.toString().equalsIgnoreCase(str)){
					return curr;
				}
			}
			return null;
		}
	}
		
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
		OCCURENCE_SUMMARY_FIELDS.add("collectioncode");
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
		OCCURENCE_SUMMARY_FIELDS.add("typestatus");
	}
	
	public SearchServiceConfig(){
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.COUNTRY.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.COUNTRY.id,"country").singleValue("country",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.FAMILY.id,
			new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.FAMILY.id,"family").singleValue("family",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.CONTINENT.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.CONTINENT.id,"continent").singleValue("continent",String.class).eqOperator().likeOperator(QueryOperatorEnum.SLIKE).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.TAXONRANK.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.TAXONRANK.id,"taxonrank").singleValue("taxonrank",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.DATE_RANGE.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.DATE_RANGE.id,"daterange").startEndDate(START_YEAR_PROPERTY,START_MONTH_PROPERTY,START_DAY_PROPERTY).eqOperator().betweenOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.START_YEAR.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.START_YEAR.id,"syear").singleValue("syear",Integer.class).eqOperator().betweenOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.CATALOG_NUMBER.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.CATALOG_NUMBER.id,"catalognumber").singleValue("catalognumber",String.class).likeOperator(QueryOperatorEnum.ELIKE).toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.COLLECTION_CODE.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.COLLECTION_CODE.id,"collectioncode").singleValue("collectioncode",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.DATASET_NAME.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.DATASET_NAME.id,"datasetname").singleValue("datasetname",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.STATE_PROVINCE.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.STATE_PROVINCE.id,"stateprovince").singleValue("stateprovince",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).supportSuggestion().eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.ALTITUDE_RANGE.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.ALTITUDE_RANGE.id,"altituderange").minMaxNumber("minimumelevationinmeters", "maximumelevationinmeters", Double.class).eqOperator().betweenOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.LOCALITY.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.LOCALITY.id,"locality").singleValue("locality",String.class).likeOperator(QueryOperatorEnum.CLIKE).toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.RECORDED_BY.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.RECORDED_BY.id,"recordedby").singleValue("recordedby",String.class).likeOperator(QueryOperatorEnum.CLIKE).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.RECORD_NUMBER.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.RECORD_NUMBER.id,"recordnumber").singleValue("recordnumber",String.class).likeOperator(QueryOperatorEnum.ELIKE).toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.KINGDOM.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.KINGDOM.id,"kingdom").singleValue("kingdom",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.ORDER.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.ORDER.id,"_order").singleValue("_order",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.SCIENTIFIC_NAME.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.SCIENTIFIC_NAME.id,"scientificname").singleValue("scientificname",String.class).eqOperator().likeOperator(QueryOperatorEnum.SLIKE).supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.INSTITUTION_CODE.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.INSTITUTION_CODE.id,"institutioncode").singleValue("institutioncode",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.CLASS.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.CLASS.id,"_class").singleValue("_class",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.PHYLUM.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.PHYLUM.id,"phylum").singleValue("phylum",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.HAS_COORDINATES.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.HAS_COORDINATES.id,"hascoordinates").singleValue("hascoordinates",Boolean.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.HAS_MEDIA.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.HAS_MEDIA.id,"hasmedia").singleValue("hasmedia",Boolean.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.HAS_TYPE_STATUS.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.HAS_TYPE_STATUS.id,"hastypestatus").singleValue("hastypestatus",Boolean.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.COUNTY.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.COUNTY.id,"county").singleValue("county",String.class).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.MUNICIPALITY.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.MUNICIPALITY.id,"municipality").singleValue("municipality",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).eqOperator().supportSuggestion().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.SOURCE_FILE_ID.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.SOURCE_FILE_ID.id,"sourcefileid").singleValue("sourcefileid",String.class).eqOperator().supportSelectionList().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.WKT.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.WKT.id,"wkt").geoValue("the_geom", Boolean.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.ELLIPSE.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.ELLIPSE.id,"ellipse").geoValue("the_geom", Boolean.class).eqOperator().toOccurrenceSearchableField());

		//Those searchable fields are used for stats only
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.GENUS.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.GENUS.id,"genus").singleValue("genus",String.class).eqOperator().likeOperator(QueryOperatorEnum.CLIKE).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.SPECIES.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.SPECIES.id,"species").singleValue("species",String.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.DECADE.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.DECADE.id,"decade").singleValue("decade",Integer.class).eqOperator().toOccurrenceSearchableField());
		SEARCHABLE_FIELD_MAP.put(SearchableFieldEnum.AVERAGE_ALTITUDE_ROUNDED.id,
				new OccurrenceSearchableFieldBuilder(SearchableFieldEnum.AVERAGE_ALTITUDE_ROUNDED.id,"averagealtituderounded").singleValue("averagealtituderounded",Integer.class).eqOperator().toOccurrenceSearchableField());
		
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
	
	public OccurrenceSearchableField getSearchableField(SearchableFieldEnum searchableField) {
		return SEARCHABLE_FIELD_MAP.get(searchableField.id);
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
