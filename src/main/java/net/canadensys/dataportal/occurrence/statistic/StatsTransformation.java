package net.canadensys.dataportal.occurrence.statistic;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Collection of functions used to transform occurrence statistic data.
 * This should be used as a preparation step before sending the data to template engine.
 * @author cgendreau
 *
 */
public class StatsTransformation {
	private static final int START_DECADE = 1850;
	
	/**
	 * Transform decadeData for display purpose.
	 * @param data key:decade, value:count
	 * @return ordered (by decade) map containing all decades from 1850 to the last decade provided with their matching count.
	 */
	public static Map<String,Integer> transformDecadeData(Map<Integer,Integer> decadeData, ResourceBundle resourceBundle){
		Map<String,Integer> formatedData = new LinkedHashMap<String, Integer>();
		int decade = START_DECADE;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int recordBeforeStartingDecade  = handleBeforeDecade(START_DECADE, decadeData);
		
		if(recordBeforeStartingDecade > 0){
			formatedData.put(resourceBundle.getString("view.stats.chart.decade.before") + " " + START_DECADE, recordBeforeStartingDecade);
		}
		
		int i = 0;
		while(i < decadeData.size() && decade < currentYear) {
			if(decadeData.containsKey(decade)){
				formatedData.put(decade+"s", decadeData.get(decade));
				i++;
			}
			else{
				//if we have no data for this decade recode it with 0
				formatedData.put(decade+"s",0);
			}
			decade = decade+10;
	    }
		return formatedData;
	}
	
	/**
	 * Remove element from decadeData where the decade is lower than lowestAcceptedDecade.
	 * @param lowestAcceptedDecade
	 * @param data
	 * @return sum of the counts of the removed element
	 */
	private static int handleBeforeDecade(final int lowestAcceptedDecade, Map<Integer,Integer> decadeData){
		int beforeLowestAcceptedDecadeCount = 0;

		Iterator<Map.Entry<Integer,Integer>> it = decadeData.entrySet().iterator();
		Map.Entry<Integer,Integer> entry = null;
		
		while(it.hasNext()){
			entry = it.next();
			if(entry.getKey() < lowestAcceptedDecade){
				beforeLowestAcceptedDecadeCount += entry.getValue();
				it.remove();
			}
		}
		return beforeLowestAcceptedDecadeCount;
	}

//	
//	// Method to prepare the Altitude data before display
//	function transformAltitudeData(json){
//		var MAX_ALTITUDE = 2000;
//		var formattedJson = [];
//		json = _.sortBy(json, function(data){ 
//			if(isNaN(parseInt(data[0]))){
//				return 0;
//			}
//			return data[0];
//		});
//		
//		var aboveMaxMetersCount = 0;
//		formattedJson.push([languageResources.getLanguageResource('view.stats.chart.altitude.below') +" 0",0]);
//		for(i=0;i < json.length;i++) {
//			if(canadensysUtils.isInteger(json[i][0])){
//				if(json[i][0] < 0){
//					formattedJson[0][1] = formattedJson[0][1] + json[i][1];
//				}
//				else if(json[i][0] >= MAX_ALTITUDE){
//					aboveMaxMetersCount = aboveMaxMetersCount + json[i][1];
//				}
//				else{
//					formattedJson.push(json[i]);
//				}
//			}
//	    }
//		formattedJson.push([languageResources.getLanguageResource('view.stats.chart.altitude.above')+" 2000",aboveMaxMetersCount]);
//		
//		formattedJson = _.map(formattedJson, function(data){ 
//			data[0] = data[0].toString()+' m';
//			return data; 
//		});
//		return formattedJson;
//	}
	

}
