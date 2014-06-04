package net.canadensys.dataportal.occurrence.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import net.canadensys.chart.ChartModel;
import net.canadensys.dataportal.occurrence.autocomplete.AutoCompleteService;
import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.config.OccurrenceSearchableFieldLanguageSupport;
import net.canadensys.dataportal.occurrence.model.MapInfoModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.search.DownloadResultStatus;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService.DownloadPropertiesEnum;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService.StatsPropertiesEnum;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig.SearchableFieldEnum;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig.SearchableFieldGroupEnum;
import net.canadensys.dataportal.occurrence.search.json.OccurrenceSearchableFieldMixIn;
import net.canadensys.dataportal.occurrence.search.json.SearchQueryPartMixIn;
import net.canadensys.dataportal.occurrence.search.parameter.SearchParamHandler;
import net.canadensys.dataportal.occurrence.search.parameter.SearchURLHelper;
import net.canadensys.dataportal.occurrence.search.parameter.SearchURLHelper.ViewNameEnum;
import net.canadensys.dataportal.occurrence.statistic.StatsTransformation;
import net.canadensys.exception.web.ResourceNotFoundException;
import net.canadensys.query.LimitedResult;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableFieldTypeEnum;
import net.canadensys.query.interpreter.InsidePolygonFieldInterpreter;
import net.canadensys.query.sort.SearchSortPart;
import net.canadensys.web.QueryStringBuilder;
import net.canadensys.web.i18n.I18nUrlBuilder;
import net.canadensys.web.i18n.annotation.I18nTranslation;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller of all search related features of the occurrence portal.
 * This controller must be stateless.
 * @author canadensys
 *
 */
@Controller
public class SearchController {
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(SearchController.class);
	
	public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
	public static final String DEFAULT_VIEW = "map";
	
	public static final String DWCA_STATUS_TAG = "status";
	public static final String DWCA_STATUS_DONE = "done";
	public static final String DWCA_STATUS_DEFERRED = "deferred";
	public static final String DWCA_STATUS_ERROR = "error";
	public static final String DWCA_FILENAME_TAG = "dwcaFileName";
	
	public static final String STATS_MAX_URL_PARAMETER = "max";
	
	//stats view related
	public static final String STATS_SELECTION_URL_PARAMETER = "stat_sel";
	public static final String STATS_GROUP_URL_PARAMETER = "stat_group";
	
	public static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();
	static{
		//avoid sending irrelevant data on the client side
		JACKSON_MAPPER.addMixInAnnotations(SearchQueryPart.class, SearchQueryPartMixIn.class);
		JACKSON_MAPPER.addMixInAnnotations(OccurrenceSearchableField.class, OccurrenceSearchableFieldMixIn.class);
		JACKSON_MAPPER.setSerializationInclusion(Include.NON_NULL);
	}
	private String availableSearchFieldsMap;
	
	//languageResources as JSON
	private Map<Locale,String> languageResourcesByLocale;
	
	//Services
	@Autowired
	private AutoCompleteService autoCompleteService;

	@Autowired
	private OccurrenceSearchService occurrenceSearchService;
	
	//Configuration
	@Autowired
	@Qualifier("searchServiceConfig")
	private SearchServiceConfig searchServiceConfig;
	
	@Autowired
	@Qualifier("SearchParamHandler")
	private SearchParamHandler searchParamHandler;
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;
	
	/**
	 * Write the Java bean as JSON String.
	 * All exceptions are transfered to the logger.
	 * @param value Java bean
	 * @return JSON string or null if an exception occurred
	 */
	private String beanAsJSONString(Object value){
		String json = null;
		try {
			json = JACKSON_MAPPER.writeValueAsString(value);
		} catch (JsonGenerationException e) {
			LOGGER.fatal("Bean to JSON error", e);
		} catch (JsonMappingException e) {
			LOGGER.fatal("Bean to JSON error", e);
		} catch (IOException e) {
			LOGGER.fatal("Bean to JSON error", e);
		}
		return json;
	}
	
	@PostConstruct
	public void init(){

		//keep this in memory since it is used in each "search" call
		availableSearchFieldsMap = beanAsJSONString(searchServiceConfig.getSearchableFieldMap());
		
		//load dynamic language resources for all locale
		languageResourcesByLocale = new HashMap<Locale, String>();
		OccurrenceSearchableFieldLanguageSupport osfLangSupport = new OccurrenceSearchableFieldLanguageSupport();
		
		Map<String,String> langResourceMap = null;
		for(Locale currLocale : appConfig.getSupportedLocale()){
			langResourceMap = osfLangSupport.buildLanguageResourcesMap(appConfig.getResourceBundle(currLocale));
			//also add URL translation resources
			langResourceMap.putAll(osfLangSupport.buildURLLanguageResourcesMap(appConfig.getURLResourceBundle(currLocale)));
			languageResourcesByLocale.put(currLocale, beanAsJSONString(langResourceMap));
		}
	}
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@I18nTranslation(resourceName="search", translateFormat = "/search")
	public ModelAndView handleSearch(HttpServletRequest request){
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		long occurrenceCount = 0;
		//Handle view param
		String currentView = request.getParameter(SearchURLHelper.VIEW_PARAM);
		if(StringUtils.isBlank(currentView) || !SearchURLHelper.isKnowViewName(currentView)){
			currentView=DEFAULT_VIEW;
		}
		modelRoot.put("currentView", currentView);
		modelRoot.put("contextURL", extractContextURL(request));
		
		//Handle locale
		Locale locale = RequestContextUtils.getLocale(request);
		
		//Set common stuff (version,minified, ...)
		ControllerHelper.setPageHeaderVariables(appConfig, modelRoot);
		
		modelRoot.put("languageResources", ObjectUtils.defaultIfNull(languageResourcesByLocale.get(locale),languageResourcesByLocale.get(Locale.ENGLISH)));
		modelRoot.put("availableFilters", searchServiceConfig.getFreemarkerSearchableFieldMap());
		modelRoot.put("availableFiltersMap", availableSearchFieldsMap);
		
		//handle search related parameters
		Collection<SearchQueryPart> searchRelatedParams= searchParamHandler.getSearchQueryPartCollection(request.getParameterMap());
		
		//handle alias parameters e.g. 'dataset','iptresource'
		handleSearchParameterAlias(request.getParameter(SearchURLHelper.DATASET_PARAM),"datasetname",searchRelatedParams);
		handleSearchParameterAlias(request.getParameter(SearchURLHelper.IPT_RESOURCE_PARAM),"sourcefileid",searchRelatedParams);
		
		//keep search related query string
		modelRoot.put("searchParameters",searchParamHandler.toQueryStringMap(searchRelatedParams));
		
		Map<String,List<SearchQueryPart>> searchCriteria = searchParamHandler.asMap(searchRelatedParams);

		handleGeospatialQuery(searchCriteria);
		//handling data related to the view
		if(currentView.equals(ViewNameEnum.MAP_VIEW_NAME.getViewName())){
			handleSearchMapView(modelRoot,searchCriteria);
		}
		else if(currentView.equals(ViewNameEnum.TABLE_VIEW_NAME.getViewName())){
			//sorting only make sense for table view
			SearchSortPart searchSortPart = searchParamHandler.getSearchQuerySort(request.getParameterMap());
			handleSearchTableView(modelRoot,searchCriteria,searchSortPart);
		}
		else if(currentView.equals(ViewNameEnum.STATS_VIEW_NAME.getViewName())){
			//get regular count
			occurrenceCount = occurrenceSearchService.getOccurrenceCount(searchCriteria);
			modelRoot.put("occurrenceCount", occurrenceCount);
			handleSearchStatsView(modelRoot,request,searchCriteria);
		}
		
		//let the view know that we search on all record
		modelRoot.put("allRecordsTargeted", searchCriteria.isEmpty());

		String searchCriteriaJson = beanAsJSONString(searchParamHandler.asList(searchRelatedParams));
		modelRoot.put("searchCriteria", searchCriteriaJson);
		
		modelRoot.put("debug", locale.toString());
		
		if(currentView.equals(ViewNameEnum.MAP_VIEW_NAME.getViewName())){
			return new ModelAndView("view-map","root",modelRoot);
		}
		else if(currentView.equals(ViewNameEnum.STATS_VIEW_NAME.getViewName())){
			return new ModelAndView("view-stats","root",modelRoot);
		}

		return new ModelAndView("view-table","root",modelRoot);
	}
	
	/**
	 * Handle map-view specific data
	 * @param model
	 * @param searchCriteria
	 * @param searchSortPart
	 */
	private void handleSearchMapView(HashMap<String,Object> model, Map<String,List<SearchQueryPart>> searchCriteria){
		
		
		//get regular count
		model.put("occurrenceCount", occurrenceSearchService.getOccurrenceCount(searchCriteria));
		model.put("embeddedMapQuery",occurrenceSearchService.getMapQuery(searchCriteria));
		int georeferencedOccurrenceCount = occurrenceSearchService.getGeoreferencedOccurrenceCount(searchCriteria);
		model.put("georeferencedOccurrenceCount", georeferencedOccurrenceCount);
	}
	
	/**
	 * Handle table-view specific data.
	 * @param model
	 * @param searchCriteria
	 * @param searchSortPart
	 */
	private void handleSearchTableView(HashMap<String,Object> model, Map<String,List<SearchQueryPart>> searchCriteria, SearchSortPart searchSortPart){
		//Handle search
		List<Map<String,String>> searchResult= new ArrayList<Map<String,String>>();
		LimitedResult<List<Map<String, String>>> qr =  occurrenceSearchService.searchWithLimit(searchCriteria,searchSortPart);			
		searchResult = qr.getRows();
		long occurrenceCount = qr.getTotal_rows();
		model.put("occurrenceList", searchResult);
		model.put("occurrenceCount", occurrenceCount);
		model.put("pageSize", occurrenceSearchService.getDefaultPageSize());
		
		if(searchSortPart != null){
			model.put("pageNumber", searchSortPart.getPageNumber());
			model.put("sortBy", searchSortPart.getOrderByColumn());
			model.put("sort", searchSortPart.getOrder());
		}
	}
	
	/**
	 * Handle stats-view specific data.
	 * @param model
	 * @param request
	 * @param searchCriteria
	 */
	private void handleSearchStatsView(HashMap<String,Object> model, HttpServletRequest request, Map<String,List<SearchQueryPart>> searchCriteria){
		//extract parameters
		String statsGroupParameter = request.getParameter(STATS_GROUP_URL_PARAMETER);
		String statSelection = request.getParameter(STATS_SELECTION_URL_PARAMETER);
		
		SearchableFieldGroupEnum statsGroup = SearchableFieldGroupEnum.fromIdentifier(statsGroupParameter);
		//set default stats group
		if(statsGroup == null){
			statsGroup = SearchableFieldGroupEnum.CLASSIFICATION;
		}

		OccurrenceSearchableField searchableField = null;
		if(!StringUtils.isBlank(statSelection)){
			try{
				Integer fieldId = Integer.parseInt(statSelection);
				searchableField = searchServiceConfig.getSearchableFieldbyId(fieldId);
			}
			catch (Exception e) {/*nothing to do*/}
		}
		
		OccurrenceSearchableField osf = null;
		Integer count = 0;
		//Only computes count for the entire group when there is more than one element in the group
		if(statsGroup.getContent().size() > 1){
			for(SearchableFieldEnum  currSf : statsGroup.getContent()){
				osf = searchServiceConfig.getSearchableField(currSf);
				count = occurrenceSearchService.getDistinctValuesCount(searchCriteria, osf);
				model.put(osf.getSearchableFieldName() + "_count", count);
			}
		}
		
		Locale locale = RequestContextUtils.getLocale(request);
		Map<StatsPropertiesEnum,Object> extraProperties = new HashMap<OccurrenceSearchService.StatsPropertiesEnum, Object>();
		extraProperties.put(StatsPropertiesEnum.RESOURCE_BUNDLE, appConfig.getResourceBundle(locale));

		SearchableFieldEnum searchableFieldEnum = null;
		if(searchableField != null){
			searchableFieldEnum = SearchableFieldEnum.fromIdentifier(searchableField.getSearchableFieldId());
		}
		
		//ensure that the searchableField is within the current statsGroup
		if( searchableField == null || !statsGroup.getContent().contains(searchableFieldEnum)){
			//set default searchableField
			switch(statsGroup){
				case CLASSIFICATION :
					searchableField = searchServiceConfig.getSearchableField(SearchableFieldEnum.FAMILY);
					break;
				case LOCATION : 
					searchableField = searchServiceConfig.getSearchableField(SearchableFieldEnum.COUNTRY);
					break;
				case DATE :
					searchableField = searchServiceConfig.getSearchableField(SearchableFieldEnum.DECADE);
					break;
				case ALTITUDE :
					searchableField = searchServiceConfig.getSearchableField(SearchableFieldEnum.AVERAGE_ALTITUDE_ROUNDED);
					break;
				default:
					searchableField = searchServiceConfig.getSearchableField(SearchableFieldEnum.FAMILY);
			}
			//load the matching enum value
			searchableFieldEnum = SearchableFieldEnum.fromIdentifier(searchableField.getSearchableFieldId());
		}
		
		Map<?,Integer> statsData = null;
		if(searchableFieldEnum == SearchableFieldEnum.DECADE || searchableFieldEnum == SearchableFieldEnum.AVERAGE_ALTITUDE_ROUNDED){
			Map<Object,Integer> histogramData = occurrenceSearchService.getValuesFrequencyDistributionAsMap(searchCriteria, searchableField, extraProperties);
			statsData = applyHistogramDataTransformation(searchableFieldEnum,locale,histogramData);
		}
		else{
			extraProperties.put(StatsPropertiesEnum.MAX_RESULT, 10);
			//sort by count desc
			statsData = StatsTransformation.sortByValue(occurrenceSearchService.getValuesFrequencyDistributionAsMap(searchCriteria, searchableField, extraProperties));
		}
		model.put("statsData", statsData);
		model.put("statsFieldKey", searchableField.getSearchableFieldName());
		model.put("statsGroupKey", statsGroup.toString());
		model.put("statsDataJSON",beanAsJSONString(statsData));
	}
	
	/**
	 * Responsible for handling the searchCriteria in case of GeoSpatial query.
	 * Main task is to set 'hint' if the query is crossing the IDL.
	 * Safe to call if searchCriteria contains no geospatial query.
	 * @param searchCriteria
	 */
	private void handleGeospatialQuery(Map<String,List<SearchQueryPart>> searchCriteria){
		//we need to check if we have a geospatial query
		List<SearchQueryPart> insidePolygonSqp = null;
		for(List<SearchQueryPart> sqpListByName: searchCriteria.values()){
			insidePolygonSqp = searchParamHandler.findSearchQueryPartByType(sqpListByName, SearchableFieldTypeEnum.INSIDE_POLYGON_GEO);
			//we can only have one geospatial query for now
			if(!insidePolygonSqp.isEmpty()){
				break;
			}
		}
		if(insidePolygonSqp != null && !insidePolygonSqp.isEmpty()){
			//we could save, in most of the cases, a call to the db by checking if we have a sign change
			//in the coordinates list.
			//if so, does it cross the IDL?
			boolean isCrossingIDL = occurrenceSearchService.isCrossingIDL(insidePolygonSqp.get(0));
			insidePolygonSqp.get(0).addHint(InsidePolygonFieldInterpreter.IS_CROSSING_IDL_HINT, isCrossingIDL);
		}
	}
	
	/**
	 * Occurrence summary as rendered HTML fragment.
	 * @param auto_id the database key
	 * @return
	 */	
	@RequestMapping(value="/occurrence-preview/{auto_id}", method=RequestMethod.GET)
	public ModelAndView handleOccurrencePreview(@PathVariable Integer auto_id){
		OccurrenceModel occModel = occurrenceSearchService.getOccurrenceSummary(auto_id);
		
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		if(occModel != null){
			modelRoot.put("occModel", occModel);
			modelRoot.put("occViewModel", OccurrenceController.buildOccurrenceViewModel(occModel));
		}
		else{
			throw new ResourceNotFoundException();
		}
		return new ModelAndView("fragment/occurrence-preview","root",modelRoot);
	}
	
	/**
	 * For histogram display, data transformation is required.
	 * @param searchableFieldEnum
	 * @param locale
	 * @param statsData
	 * @return
	 */
	private Map<String,Integer> applyHistogramDataTransformation(SearchableFieldEnum searchableFieldEnum, Locale locale, Map<Object,Integer> statsData){
		Map<String,Integer> formatedStatsData = null;
		//We only support histogram with Integer as key (decade, altitude)
		Map<Integer,Integer> statsDataCorrectedKey = new HashMap<Integer, Integer>(statsData.size());
		for(Object currKey :statsData.keySet()){
			if(NumberUtils.isNumber(currKey.toString())){
				statsDataCorrectedKey.put(Integer.valueOf(currKey.toString()), statsData.get(currKey));
			}
		}
		switch (searchableFieldEnum) {
			case DECADE:
				formatedStatsData = StatsTransformation.transformDecadeData(statsDataCorrectedKey, appConfig.getResourceBundle(locale));
				break;
			case AVERAGE_ALTITUDE_ROUNDED:
				formatedStatsData = StatsTransformation.transformAltitudeData(statsDataCorrectedKey, appConfig.getResourceBundle(locale));
				break;
			default:
				break;
		}
		
		if(formatedStatsData == null){
			LOGGER.error("No data transformation found for SearchableFieldEnum " + searchableFieldEnum);
		}
		return formatedStatsData;
	}
	
	/**
	 * Auto-complete feature of the portal.
	 * @param fieldId
	 * @param curr current field value
	 * @return
	 */
	@RequestMapping(value="/ws/livesearch", method=RequestMethod.GET)
	public ResponseEntity<String> handleSuggestion(@RequestParam Integer fieldId,
			@RequestParam(required=false) String curr){
		String suggestion = null;
		if(searchServiceConfig.getSearchableFieldbyId(fieldId) != null){
			suggestion = autoCompleteService.search(searchServiceConfig.getSearchableFieldbyId(fieldId).getRelatedField(), curr);
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		return new ResponseEntity<String>(suggestion, responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * Get all possible values for a specific field.
	 * @param fieldId
	 * @return all the values as JSON string
	 */
	@RequestMapping(value="/ws/getpossiblevalues", method=RequestMethod.GET)
	public ResponseEntity<String> handleSuggestion(@RequestParam Integer fieldId){
		String possibleValuesJson = null;
		if(searchServiceConfig.getSearchableFieldbyId(fieldId) != null &&
				searchServiceConfig.getSearchableFieldbyId(fieldId).isSupportSelectionList()){
			possibleValuesJson = beanAsJSONString(autoCompleteService.getAllPossibleValues(searchServiceConfig.getSearchableFieldbyId(fieldId).getRelatedField()));
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		return new ResponseEntity<String>(possibleValuesJson, responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * Returns the map center based on a SQL query.
	 * @param q
	 * @return long,lat as JSON
	 */
	@RequestMapping(value="/ws/mapcenter", method=RequestMethod.GET)
	public ResponseEntity<String> handleMapcenter(@RequestParam String q){
		String[] box = occurrenceSearchService.getMapCenter(q);
		
		String json =  beanAsJSONString(box);		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * Returns informations(extent, center) of the map based on a SQL query.
	 * @param q
	 * @return MapInfoModel as JSON in the form of
	 * {"extentMin":["-44","-40.0833333"],"extentMax":["39","103.4166667"],"centroid":["-2.5","31.6666667"]}
	 */
	@RequestMapping(value="/ws/mapinfo", method=RequestMethod.GET)
	public ResponseEntity<String> handleMapInfo(@RequestParam String q){
		MapInfoModel mapInfo = occurrenceSearchService.getMapInfo(q);
		
		String json =  beanAsJSONString(mapInfo);		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * Handling the request for a download of the result of a query.
	 * For the moment, only DarwinCore archive format can be returned.
	 * The service will only return when the archive will be generated.
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/ws/downloadresult", method=RequestMethod.GET)
	public ResponseEntity<String> handleDownloadResult(HttpServletRequest request){
		Collection<SearchQueryPart> searchRelatedParams= searchParamHandler.getSearchQueryPartCollection(request.getParameterMap());
		Map<String,List<SearchQueryPart>> searchCriteria = searchParamHandler.asMap(searchRelatedParams);		
		Map<String,String> resultMap = new HashMap<String, String>();
		//TODO, create a static dwca for all records
		String fileName = String.valueOf(UUID.randomUUID());
		String email = request.getParameter("e");
		Locale locale = RequestContextUtils.getLocale(request);
		
		Map<DownloadPropertiesEnum,Object> extraProperties = new HashMap<OccurrenceSearchService.DownloadPropertiesEnum, Object>();
		if(!StringUtils.isBlank(email)){
			extraProperties.put(DownloadPropertiesEnum.EMAIL, email);
		}
		extraProperties.put(DownloadPropertiesEnum.LOCALE, locale);
		
		String baseURL = request.getScheme() + "://" + request.getServerName()+request.getContextPath();
		String searchBaseUrlString = baseURL + I18nUrlBuilder.generateI18nResourcePath(locale.getLanguage(),OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat("search"),(String)null);
		QueryStringBuilder qsb = new QueryStringBuilder();
		qsb.add(searchParamHandler.getSearchQueryRelatedParameters(request.getParameterMap())).add(SearchURLHelper.VIEW_PARAM, SearchURLHelper.ViewNameEnum.TABLE_VIEW_NAME.getViewName());
		searchBaseUrlString += qsb.toQueryString();
		extraProperties.put(DownloadPropertiesEnum.SEARCH_URL, searchBaseUrlString);
		
		DownloadResultStatus downloadStatus = occurrenceSearchService.searchAsDwca(searchCriteria, fileName, extraProperties);
		switch(downloadStatus.getMode()){
			case SYNC : resultMap.put(DWCA_STATUS_TAG, DWCA_STATUS_DONE);
						resultMap.put(DWCA_FILENAME_TAG, downloadStatus.getFileName());
						break;
			case ASYNC : resultMap.put(DWCA_STATUS_TAG, DWCA_STATUS_DEFERRED);
						break;
			case ERROR : resultMap.put(DWCA_STATUS_TAG, DWCA_STATUS_ERROR);
						break;
		}
			
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		String jsonResponse =  beanAsJSONString(resultMap);

		return new ResponseEntity<String>(jsonResponse, responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="/ws/stats/unique/{fieldId}", method=RequestMethod.GET)
	public ResponseEntity<String> handleStatsUnique(@PathVariable Integer fieldId, HttpServletRequest request){
		Collection<SearchQueryPart> searchRelatedParams= searchParamHandler.getSearchQueryPartCollection(request.getParameterMap());
		Map<String,List<SearchQueryPart>> searchCriteria = searchParamHandler.asMap(searchRelatedParams);
		Integer count = occurrenceSearchService.getDistinctValuesCount(searchCriteria, searchServiceConfig.getSearchableFieldbyId(fieldId));
		
		Map<String, Integer> response = new HashMap<String, Integer>(1);
		response.put("count", count);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		String jsonResponse =  beanAsJSONString(response);
		return new ResponseEntity<String>(jsonResponse, responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="/ws/stats/chart/{fieldId}", method=RequestMethod.GET)
	public ResponseEntity<String> handleStats(@PathVariable Integer fieldId, HttpServletRequest request){
		Collection<SearchQueryPart> searchRelatedParams= searchParamHandler.getSearchQueryPartCollection(request.getParameterMap());
		Map<String,List<SearchQueryPart>> searchCriteria = searchParamHandler.asMap(searchRelatedParams);
		ChartModel chartModel = null;
		
		if(searchServiceConfig.getSearchableFieldbyId(fieldId) != null){
			Locale locale = RequestContextUtils.getLocale(request);
			Map<StatsPropertiesEnum,Object> extraProperties = new HashMap<OccurrenceSearchService.StatsPropertiesEnum, Object>();
			extraProperties.put(StatsPropertiesEnum.RESOURCE_BUNDLE, appConfig.getResourceBundle(locale));
			
			String maxStr = request.getParameter(STATS_MAX_URL_PARAMETER);
			if(!StringUtils.isBlank(maxStr)){
				try{
					extraProperties.put(StatsPropertiesEnum.MAX_RESULT, Integer.parseInt(maxStr));
				}
				catch (Exception e) {/*nothing to do*/}
			}
			chartModel = occurrenceSearchService.getValuesFrequencyDistribution(searchCriteria, searchServiceConfig.getSearchableFieldbyId(fieldId),extraProperties);
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		String jsonResponse =  beanAsJSONString(chartModel);
		return new ResponseEntity<String>(jsonResponse, responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * Get the context complete URL from a HttpServletRequest.
	 * @param request
	 * @return
	 */
	private String extractContextURL(HttpServletRequest request){
		//only add the port to the URL if it's different than 80
		if(request.getServerPort() != 80){
			return request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		}
		return request.getScheme() + "://" + request.getServerName()+request.getContextPath();
	}
	
	
	/**
	 * This function handles search parameter alias in URL parameter.
	 * @param aliasName
	 * @param aliasValue
	 * @param searchRelatedParams collection to add the new SearchQueryPart
	 */
	private void handleSearchParameterAlias(String aliasValue, String fieldName, Collection<SearchQueryPart> searchRelatedParams){
		if(!StringUtils.isBlank(aliasValue)){
			SearchQueryPart sqp = occurrenceSearchService.getSearchQueryPartFromFieldName(fieldName, aliasValue);
			if(sqp != null){
				//we need to copy the list to be able to add a new element
				searchRelatedParams.add(sqp);
			}
		}
	}
}
