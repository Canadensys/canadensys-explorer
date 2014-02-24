package net.canadensys.dataportal.occurrence.search.parameter;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.search.parameter.parser.SearchParamParser;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This class helps dealing with some parameters in the URL of the search service.
 * @author canadensys
 *
 */
public class SearchURLHelper {
	//TODO move to configuration?
	public static final String VIEW_PARAM = "view";
	public static final String DATASET_PARAM = "dataset";
	public static final String IPT_RESOURCE_PARAM = "iptresource";
		
	public enum ViewNameEnum {
		TABLE_VIEW_NAME("table"), MAP_VIEW_NAME("map"), IMAGE_VIEW_NAME("image"), STATS_VIEW_NAME("stats");

		private String viewName;
		private ViewNameEnum(String name){
			viewName = name;
		}
		public String getViewName(){
			return viewName;
		}
		public static boolean contains(String name){
			for(ViewNameEnum curr : ViewNameEnum.values()){
				if(curr.getViewName().equalsIgnoreCase(name)){
					return true;
				}
			}
			return false;
		}
	}
	
	public static final String LANGUAGE_PARAM = "lang";
	public static final String SEARCH_CONTEXT = "/search";
	
	/**
	 * Checks if the viewName is a valid view name.
	 * @param viewName
	 * @return
	 */
	public static boolean isKnowViewName(String viewName){
		return ViewNameEnum.contains(viewName);
	}
	
	/**
	 * This method return a generated URL for the Map view of the search.
	 * It only changes (adds) the view parameter.
	 * @param request
	 * @param viewName
	 * @return
	 */
	public static String getViewURL(HttpServletRequest request, ViewNameEnum viewName){
		ServletUriComponentsBuilder compBuilder = ServletUriComponentsBuilder.fromRequest(request);
		compBuilder.replaceQueryParam(VIEW_PARAM, viewName.getViewName());
		return compBuilder.build().toUriString();
	}
	
	/**
	 * This method will create a search table view URL based on baseUrl and queryParams.
	 * Only the search query parameters will be kept and the view=table will be added
	 * Useful to keep the search query string among different pages
	 * @param baseUrl
	 * @param queryParams
	 * @return
	 */
	public static String createSearchTableViewURL(String baseUrl, Map<String,String[]> queryParams){
		UriComponentsBuilder compBuilder = ServletUriComponentsBuilder.fromPath(baseUrl+SEARCH_CONTEXT);
		
		Iterator<String> keyIt = queryParams.keySet().iterator();
		String currKey;
		while(keyIt.hasNext()){
			currKey = keyIt.next();
			if(SearchParamParser.isSearchParam(currKey)){
				compBuilder.queryParam(currKey, (Object[])queryParams.get(currKey));
			}
		}
		compBuilder.queryParam(VIEW_PARAM, ViewNameEnum.TABLE_VIEW_NAME.getViewName());
		return compBuilder.build().toUriString();
	}
}
