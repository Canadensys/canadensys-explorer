package net.canadensys.dataportal.occurrence.search.config;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.canadensys.query.QueryOperatorEnum;

import org.apache.commons.lang3.StringUtils;

/**
 * This class contains configurations related to the language support on client side.
 * @author canadensys
 *
 */
public class OccurrenceSearchableFieldLanguageSupport {
	
	private static final String OPERATOR_PREFIX = "operator.";
	private static final List<String> PREFIX_FILTER_LIST = new ArrayList<String>();
	static{
		PREFIX_FILTER_LIST.add("filter.");
		PREFIX_FILTER_LIST.add("control.invalid.");
		PREFIX_FILTER_LIST.add("control.download.");
		PREFIX_FILTER_LIST.add("view.stats.chart.");
		PREFIX_FILTER_LIST.add("occpage.menu.");
	}
	
	/**
	 * This is used for dynamic components on client-side. It doesn't contain all the language resources.
	 * @param locale
	 * @return
	 */
	public Map<String,String> buildLanguageResourcesMap(ResourceBundle resourceBundle){
		Map<String,String> languageResources = new HashMap<String, String>();
		for(QueryOperatorEnum op : QueryOperatorEnum.values()){
			String key = OPERATOR_PREFIX + op.toString().toLowerCase();
			if(!StringUtils.isBlank(resourceBundle.getString(key))){
				languageResources.put(key, resourceBundle.getString(key));
			}
		}
		
		Enumeration<String> rbKeys = resourceBundle.getKeys();
		String currKey = null;
		while(rbKeys.hasMoreElements()){
			currKey = rbKeys.nextElement();
			if(shouldInclude(currKey)){
				languageResources.put(currKey, resourceBundle.getString(currKey));
			}
		}
		return languageResources;
	}
	
	/**
	 * Check if we should include this languageResource based on the PREFIX_FILTER_LIST.
	 * @param languageResource
	 * @return true if languageResource starts with a prefix in PREFIX_FILTER_LIST, otherwise, false.
	 */
	private boolean shouldInclude(String languageResource){
		for(String curr:PREFIX_FILTER_LIST){
			if(languageResource.startsWith(curr)){
				return true;
			}
		}
		return false;
	}
}
