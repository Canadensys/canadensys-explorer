package net.canadensys.dataportal.occurrence.search.config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.canadensys.query.QueryOperatorEnum;

import org.apache.commons.lang3.StringUtils;

/**
 * This class contains configurations related to the language support on client side.
 * We only keep language strings that could be used in the view dynamic parts.
 * TODO : prefil the map with OccurrencePortalConfig.getSupportedLocale() to avoid Map creation on each call.
 * @author canadensys
 *
 */
public class OccurrenceSearchableFieldLanguageSupport {
	
	private static final String OPERATOR_PREFIX = "operator.";
	private static final String FILTER_PREFIX = "filter.";
	private static final String INVALID_PREFIX = "control.invalid.";
	private static final String DOWNLOAD_PREFIX = "control.download.";
	private static final String CHART_PREFIX = "view.stats.chart.";
	private static final String OCC_PAGE_MENU_PREFIX = "occpage.menu.";
	
	/**
	 * This is used for dynamic components only. It doesn't contain all the resources.
	 * Contains language string for operators, filters, download and stats components.
	 * @param locale
	 * @return
	 */
	public Map<String,String> getLanguageResources(ResourceBundle resourceBundle){
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
			if(currKey.startsWith(FILTER_PREFIX) 
			    || currKey.startsWith(INVALID_PREFIX) 
	        || currKey.startsWith(DOWNLOAD_PREFIX) 
					|| currKey.startsWith(CHART_PREFIX) 
					|| currKey.startsWith(OCC_PAGE_MENU_PREFIX)){
				languageResources.put(currKey, resourceBundle.getString(currKey));
			}
		}
		return languageResources;
	}
}
