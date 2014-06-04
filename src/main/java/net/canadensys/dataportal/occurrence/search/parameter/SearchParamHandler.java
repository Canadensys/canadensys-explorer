package net.canadensys.dataportal.occurrence.search.parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.dataportal.occurrence.search.parameter.parser.SearchParamParser;
import net.canadensys.dataportal.occurrence.search.parameter.parser.SearchSortPartParser;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchQueryPartValidator;
import net.canadensys.query.SearchableFieldTypeEnum;
import net.canadensys.query.sort.SearchSortPart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This function is the entry point to safely get a collection of SearchQueryPart based
 * on URL parameters.
 * Thread safe after configuration
 * @author canadensys
 *
 */
@Component ("SearchParamHandler")
public class SearchParamHandler {
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(SearchParamHandler.class);
	
	@Autowired
	private SearchServiceConfig searchServiceConfig;
	
	private SearchParamParser paramParser;
	private SearchSortPartParser sortParser;
	
	@PostConstruct
	public void initialize() {
		 paramParser = new SearchParamParser();
		 paramParser.setSearchConfig(searchServiceConfig);
		 
		 sortParser = new SearchSortPartParser();
		 sortParser.setSearchConfig(searchServiceConfig);
	}
	
	/**
	 * This functions will return a Collection of valid SearchQueryPart (according to SearchQueryPartValidator).
	 * @param parametersMap
	 * @return
	 */
	public Collection<SearchQueryPart> getSearchQueryPartCollection(Map<String,String[]> parametersMap){
		Collection<SearchQueryPart> searchRelatedParams= paramParser.parse(parametersMap);
		Iterator<SearchQueryPart> searchParamsIt = searchRelatedParams.iterator();
		while(searchParamsIt.hasNext()){
			SearchQueryPart current = searchParamsIt.next();
			if(!SearchQueryPartValidator.validate(current)){
				searchParamsIt.remove();
				LOGGER.warn("Ignoring invalid SearchQueryPart : " + current);
			}
		}
		return searchRelatedParams;
	}
	
	/**
	 * @see SearchParamParser
	 * @param searchQueryParts
	 * @return
	 */
	public Map<String,String> toQueryStringMap(Collection<SearchQueryPart> searchQueryParts){
		return paramParser.toQueryStringMap(searchQueryParts);
	}
	
	/**
	 * Get the parameters that are related to a search query.
	 * @param parametersMap
	 * @return empty map or map containing the parameters(and the value) related to a search query.
	 */
	public Map<String,String> getSearchQueryRelatedParameters(Map<String,String[]> parametersMap){
		Map<String,String> queryParameters = new HashMap<String, String>();
		for(String currParamKey : parametersMap.keySet()){
			if(SearchParamParser.isSearchParam(currParamKey)){
				queryParameters.put(currParamKey, parametersMap.get(currParamKey)[0]);
			}
		}
		return queryParameters;
	}
	
	public SearchSortPart getSearchQuerySort(Map<String,String[]> parametersMap){
		return sortParser.parse(parametersMap);
	}
	
	/**
	 * Returns the queryPartColl as list of SearchQueryPart mapped by field name.
	 * If your work with the fieldId, you can provide a replacement map. This will allow the function to provide
	 * the matching name in the SearchQueryPart objects.
	 * String.
	 * @param queryPartColl validated result of SearchParamParser.parse(...)
	 * @param fieldIdNameMap optional if the parametersMap contains the field name and not the field id
	 * @return
	 */
	public Map<String,List<SearchQueryPart>> asMap(Collection<SearchQueryPart> queryPartColl){
		HashMap<String, List<SearchQueryPart>> map = new HashMap<String, List<SearchQueryPart>>();
		Iterator<SearchQueryPart> it = queryPartColl.iterator();
		SearchQueryPart currSearchQueryPart;
		String fieldName;
		
		while(it.hasNext()){
			currSearchQueryPart = it.next();
			fieldName = currSearchQueryPart.getSearchableFieldName();
			if(map.get(fieldName)==null){
				map.put(fieldName, new ArrayList<SearchQueryPart>());
			}
			map.get(fieldName).add(currSearchQueryPart);
		}
		return map;
	}
	
	/**
	 * Returns the queryPartColl as list of SearchQueryPart.
	 * If your work with the fieldId, you can provide a replacement map. This will allow the function to provide
	 * the matching name in the SearchQueryPart objects.
	 * String.
	 * @param queryPartColl validated result of SearchParamParser.parse(...)
	 * @param fieldIdNameMap optional if the parametersMap contains the field name and not the field id
	 * @return
	 */
	public List<SearchQueryPart> asList(Collection<SearchQueryPart> queryPartColl){
		List<SearchQueryPart> list = new ArrayList<SearchQueryPart>();
		Iterator<SearchQueryPart> it = queryPartColl.iterator();
		SearchQueryPart currSearchQueryPart;
		
		while(it.hasNext()){
			currSearchQueryPart = it.next();
			list.add(currSearchQueryPart);
		}
		return list;
	}

	/**
	 * Find SearchQueryPart in a list based of the type of its SearchableField.
	 * @param sqpList
	 * @param targetType
	 * @return list of SearchQueryPart matching targetType or empty list if none. Never null
	 */
	public List<SearchQueryPart> findSearchQueryPartByType(List<SearchQueryPart> sqpList, SearchableFieldTypeEnum targetType){
		List<SearchQueryPart> result = new ArrayList<SearchQueryPart>();
		for(SearchQueryPart sqp: sqpList){
			if(sqp.getSearchableField().getSearchableFieldTypeEnum() == targetType){
				result.add(sqp);
			}
		}
		return result;
	}
	
	public void setSearchServiceConfig(SearchServiceConfig searchServiceConfig) {
		this.searchServiceConfig = searchServiceConfig;
	}

}
