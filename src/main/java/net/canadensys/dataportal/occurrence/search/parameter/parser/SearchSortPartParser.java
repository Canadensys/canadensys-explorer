package net.canadensys.dataportal.occurrence.search.parameter.parser;

import java.util.Map;

import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.query.OrderEnum;
import net.canadensys.query.SearchableField;
import net.canadensys.query.SearchableFieldTypeEnum;
import net.canadensys.query.sort.SearchSortPart;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * This class is responsible to create a SearchSortPart based
 * on URL parameters. This class is designed for Occurrence Portal search.
 * @author canadensys
 *
 */
public class SearchSortPartParser {
	
	public static String PARAM_PAGE = "page";
	public static String PARAM_SORT_BY = "sortby";
	public static String PARAM_SORT = "sort";
	
	private final SearchServiceConfig searchConfig;
	
	public SearchSortPartParser(SearchServiceConfig searchConfig){
		this.searchConfig = searchConfig;
	}
	
	/**
	 * Create a SearchSortPart from query parameters if they contain at least one SearchSortPart related parameter.
	 * @param parametersMap
	 * @return SearchSortPart instance or null is no SearchSortPart related parameter were found.
	 */
	public SearchSortPart parse(Map<String,String[]> parametersMap){
		
		if(searchConfig == null){
			throw new IllegalArgumentException("SearchServiceConfig must not be null.");
		}
	
		SearchSortPart searchSortPart = null;
		String currentParameterValue;
		for(String currParameterKey : parametersMap.keySet()){
			currentParameterValue = parametersMap.get(currParameterKey)[0];
			if(PARAM_PAGE.equalsIgnoreCase(currParameterKey)){
				if(searchSortPart == null){
					searchSortPart = new SearchSortPart();
				}
				//page number starts at 1, 0 will be treated as page 1.
				searchSortPart.setPageNumber(NumberUtils.toInt(currentParameterValue, 1));
			}
			else if(PARAM_SORT_BY.equalsIgnoreCase(currParameterKey)){
				//ensure it's a know field
				SearchableField searchableField = searchConfig.getSearchableFieldbyName(currentParameterValue);
				//only support sort for 'SINGLE_VALUE' otherwise it's kind of hard to define
				if(searchableField != null && searchableField.getSearchableFieldTypeEnum() == SearchableFieldTypeEnum.SINGLE_VALUE){
					if(searchSortPart == null){
						searchSortPart = new SearchSortPart();
					}
					searchSortPart.setOrderByColumn(searchableField.getRelatedField());
				}
			}
			else if(PARAM_SORT.equalsIgnoreCase(currParameterKey)){
				if(EnumUtils.isValidEnum(OrderEnum.class, currentParameterValue.toUpperCase())){
					if(searchSortPart == null){
						searchSortPart = new SearchSortPart();
					}
					searchSortPart.setOrder(OrderEnum.valueOf(currentParameterValue.toUpperCase()));
				}
			}
		}
		return searchSortPart;
	}

}
