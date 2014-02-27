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
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.search.DownloadResultStatus;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService.DownloadPropertiesEnum;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService.StatsPropertiesEnum;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
import net.canadensys.dataportal.occurrence.search.config.OccurrenceSearchableFieldLanguageSupport;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.dataportal.occurrence.search.json.OccurrenceSearchableFieldMixIn;
import net.canadensys.dataportal.occurrence.search.json.SearchQueryPartMixIn;
import net.canadensys.dataportal.occurrence.search.parameter.SearchParamHandler;
import net.canadensys.dataportal.occurrence.search.parameter.SearchURLHelper;
import net.canadensys.dataportal.occurrence.search.parameter.SearchURLHelper.ViewNameEnum;
import net.canadensys.exception.web.ResourceNotFoundException;
import net.canadensys.query.LimitedResult;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.sort.SearchSortPart;
import net.canadensys.web.i18n.annotation.I18nTranslation;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.view.RedirectView;

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
	
	public static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();
	static{
		//avoid sending irrelevant data on the client side
		JACKSON_MAPPER.addMixInAnnotations(SearchQueryPart.class, SearchQueryPartMixIn.class);
		JACKSON_MAPPER.addMixInAnnotations(OccurrenceSearchableField.class, OccurrenceSearchableFieldMixIn.class);
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
		for(Locale currLocale : appConfig.getSupportedLocale()){
			languageResourcesByLocale.put(currLocale, beanAsJSONString(osfLangSupport.getLanguageResources(appConfig.getResourceBundle(currLocale))));
		}
	}
	
	/**
	 * Redirect the root to the search page
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView handleRoot(HttpServletRequest request){
		RedirectView rv = new RedirectView(request.getContextPath()+"/search");
		rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		ModelAndView mv = new ModelAndView(rv);
		return mv;
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
		
		//Handle locale
		Locale locale = RequestContextUtils.getLocale(request);
		
		//Set common stuff (GoogleAnalytics, language, ...)
		ControllerHelper.setPageHeaderVariables(appConfig, modelRoot);
		
		modelRoot.put("languageResources", ObjectUtils.defaultIfNull(languageResourcesByLocale.get(locale),languageResourcesByLocale.get(Locale.ENGLISH)));
		modelRoot.put("availableFilters", searchServiceConfig.getFreemarkerSearchableFieldMap());
		modelRoot.put("availableFiltersMap", availableSearchFieldsMap);
		
		//handle search related parameters
		@SuppressWarnings("unchecked")
		Collection<SearchQueryPart> searchRelatedParams= searchParamHandler.getSearchQueryPartCollection(request.getParameterMap());
		
		//handle special parameters 'dataset','iptresource'
		handleDatasetParam(request.getParameter(SearchURLHelper.DATASET_PARAM),searchRelatedParams);
		handleIptResourceParam(request.getParameter(SearchURLHelper.IPT_RESOURCE_PARAM),searchRelatedParams);
		Map<String,List<SearchQueryPart>> searchCriteria = searchParamHandler.asMap(searchRelatedParams);

		//handling data related to the view
		if(currentView.equals(ViewNameEnum.MAP_VIEW_NAME.getViewName())){
			modelRoot.put("embeddedMapQuery",occurrenceSearchService.getMapQuery(searchCriteria));
			int georeferencedOccurrenceCount = occurrenceSearchService.getGeoreferencedOccurrenceCount(searchCriteria);
			modelRoot.put("georeferencedOccurrenceCount", georeferencedOccurrenceCount);
			
			//get regular count
			occurrenceCount = occurrenceSearchService.getOccurrenceCount(searchCriteria);
			modelRoot.put("occurrenceCount", occurrenceCount);
		}
		else if(currentView.equals(ViewNameEnum.TABLE_VIEW_NAME.getViewName())){
			//sorting only make sense for table view
			@SuppressWarnings("unchecked")
			SearchSortPart searchSortPart = searchParamHandler.getSearchQuerySort(request.getParameterMap());
			handleSearchTableView(modelRoot,searchCriteria,searchSortPart);
		}
		else if(currentView.equals(ViewNameEnum.STATS_VIEW_NAME.getViewName())){
			//get regular count
			occurrenceCount = occurrenceSearchService.getOccurrenceCount(searchCriteria);
			modelRoot.put("occurrenceCount", occurrenceCount);
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
		}
	}
	
	/**
	 * Auto-complete feature of the portal.
	 * @param fieldId
	 * @param curr current field value
	 * @return
	 */
	@RequestMapping(value="/livesearch", method=RequestMethod.GET)
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
	@RequestMapping(value="/getpossiblevalues", method=RequestMethod.GET)
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
	 * Return the map center based on a SQL query.
	 * @param q
	 * @return long,lat as JSON
	 */
	@RequestMapping(value="/mapcenter", method=RequestMethod.GET)
	public ResponseEntity<String> handleMapcenter(@RequestParam String q){
		String[] box = occurrenceSearchService.getMapCenter(q);
		
		String json =  beanAsJSONString(box);		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", JSON_CONTENT_TYPE);
		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * Handling the request for a download of the result of a query.
	 * For the moment, only DarwinCore archive format can be returned.
	 * The service will only return when the archive will be generated.
	 * TODO : option to received the link by email instead of waiting
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/downloadresult", method=RequestMethod.GET)
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
		
		//is this better than appConfig.getRootURL()?
		String baseURL = request.getScheme() + "://" + request.getServerName()+request.getContextPath();
		extraProperties.put(DownloadPropertiesEnum.SEARCH_URL, SearchURLHelper.createSearchTableViewURL(baseURL, request.getParameterMap()));
		
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
	
	@RequestMapping(value="/stats/unique/{fieldId}", method=RequestMethod.GET)
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
	
	@RequestMapping(value="/stats/chart/{fieldId}", method=RequestMethod.GET)
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
	 * This function handles URL parameter 'dataset=...'
	 * @param sourceFileId
	 * @param searchRelatedParams
	 */
	private void handleDatasetParam(String datasetName, Collection<SearchQueryPart> searchRelatedParams){
		if(!StringUtils.isBlank(datasetName)){
			SearchQueryPart sqp = occurrenceSearchService.getSearchQueryPartFromFieldName("datasetname", datasetName);
			if(sqp != null){
				//we need to copy the list to be able to add a new element
				searchRelatedParams.add(sqp);
			}
		}
	}
	
	/**
	 * This function handles URL parameter 'iptresource=...'
	 * @param sourceFileId
	 * @param searchRelatedParams
	 */
	private void handleIptResourceParam(String sourceFileId, Collection<SearchQueryPart> searchRelatedParams){
		if(!StringUtils.isBlank(sourceFileId)){
			SearchQueryPart sqp = occurrenceSearchService.getSearchQueryPartFromFieldName("sourcefileid", sourceFileId);
			if(sqp != null){
				//we need to copy the list to be able to add a new element
				searchRelatedParams.add(sqp);
			}
		}
	}
}
