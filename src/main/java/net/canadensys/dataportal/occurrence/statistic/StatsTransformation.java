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
	private static final int DECADE_STEP = 10;
	private static final String DECADE_SUFFIX = "s";
	
	private static final int MIN_ALTITUDE = 0;
	private static final int MAX_ALTITUDE = 2000;
	private static final int ALTITUDE_STEP = 100;
	private static final String ALTITUDE_SUFFIX = "m";
	
	/**
	 * Transform decadeData for display purpose.
	 * @param data key=decade, value=count
	 * @return ordered (by decade) map containing all decades from 1850 to the last decade provided with their matching count.
	 */
	public static Map<String,Integer> transformDecadeData(Map<Integer,Integer> decadeData, ResourceBundle resourceBundle){
		Map<String,Integer> formatedData = new LinkedHashMap<String, Integer>();
		int decade = START_DECADE;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int recordBeforeStartingDecade  = handleLowerThan(START_DECADE, decadeData);
		
		if(recordBeforeStartingDecade > 0){
			formatedData.put(resourceBundle.getString("view.stats.chart.decade.before") + " " + START_DECADE, recordBeforeStartingDecade);
		}
		
		int i = 0;
		while(i < decadeData.size() && decade < currentYear) {
			if(decadeData.containsKey(decade)){
				formatedData.put(decade+DECADE_SUFFIX, decadeData.get(decade));
				i++;
			}
			else{
				//if we have no data for this decade record it with 0
				formatedData.put(decade+DECADE_SUFFIX,0);
			}
			decade += DECADE_STEP;
	    }
		return formatedData;
	}
	
	/**
	 * Transform altitudeData for display purpose.
	 * @param data key=altitude, value=count
	 * @return ordered (by altitude) map containing all altitude from 0m to the last altitude provided or 2000m.
	 */
	public static Map<String,Integer> transformAltitudeData(Map<Integer,Integer> altitudeData, ResourceBundle resourceBundle){
		Map<String,Integer> formatedData = new LinkedHashMap<String, Integer>();
		
		int recordsBelowMinAltitude  = handleLowerThan(MIN_ALTITUDE, altitudeData);
		int recordsAboveMinAltitude  = handleHigherThan(MAX_ALTITUDE, altitudeData);
		
		formatedData.put(resourceBundle.getString("view.stats.chart.altitude.below") + " " + MIN_ALTITUDE + ALTITUDE_SUFFIX, recordsBelowMinAltitude);
		
		int currAltitude = MIN_ALTITUDE;
		int idx=0;
		while(idx < altitudeData.size() && currAltitude < MAX_ALTITUDE) {
			if(altitudeData.containsKey(currAltitude)){
				formatedData.put(currAltitude+ALTITUDE_SUFFIX, altitudeData.get(currAltitude));
				idx++;
			}
			else{
				//if we have no data for this altitude record it with 0
				formatedData.put(currAltitude+ALTITUDE_SUFFIX,0);
			}
			currAltitude += ALTITUDE_STEP;
	    }
		
		if(recordsAboveMinAltitude > 0){
			formatedData.put(resourceBundle.getString("view.stats.chart.altitude.above") + " " + MAX_ALTITUDE + ALTITUDE_SUFFIX, recordsAboveMinAltitude);
		}
		
		return formatedData;
	}
	
	/**
	 * Remove element from data where the Integer key is lower than lowestAcceptedKey.
	 * e.g. used when data contains decade:count.
	 * @param lowestAcceptedKey
	 * @param data
	 * @return sum of the value of the removed elements
	 */
	private static int handleLowerThan(final int lowestAcceptedKey, Map<Integer,Integer> data){
		int lowerThanAcceptedKeyCount = 0;

		Iterator<Map.Entry<Integer,Integer>> it = data.entrySet().iterator();
		Map.Entry<Integer,Integer> entry = null;
		
		while(it.hasNext()){
			entry = it.next();
			if(entry.getKey() < lowestAcceptedKey){
				lowerThanAcceptedKeyCount += entry.getValue();
				it.remove();
			}
		}
		return lowerThanAcceptedKeyCount;
	}
	
	/**
	 * Remove element from data where the Integer key is higher than highestAcceptedKey.
	 * e.g. used when data contains decade:count.
	 * @param highestAcceptedKey
	 * @param data
	 * @return sum of the value of the removed elements
	 */
	private static int handleHigherThan(final int highestAcceptedKey, Map<Integer,Integer> data){
		int higherThanAcceptedKeyCount = 0;

		Iterator<Map.Entry<Integer,Integer>> it = data.entrySet().iterator();
		Map.Entry<Integer,Integer> entry = null;
		
		while(it.hasNext()){
			entry = it.next();
			if(entry.getKey() > highestAcceptedKey){
				higherThanAcceptedKeyCount += entry.getValue();
				it.remove();
			}
		}
		return higherThanAcceptedKeyCount;
	}
}
