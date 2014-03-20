package net.canadensys.dataportal.occurrence.statistic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

/**
 * Test StatsTransformation behavior.
 * @author cgendreau
 *
 */
public class StatsTransformationTest {
	
	@Test
	public void testStatsTransformationDecade(){
		Map<Integer,Integer> decadeData = new HashMap<Integer,Integer>();
		//add some data before 1850
		decadeData.put(1600, 1);
		decadeData.put(1610, 1);
		
		decadeData.put(1920, 7);
		decadeData.put(2000, 10);
		
		Map<String,Integer> formatedData = StatsTransformation.transformDecadeData(
				decadeData,ResourceBundle.getBundle("ApplicationResources"));
		
		//First element shall be sum of counts before 1850
		assertEquals(new Integer(2),formatedData.entrySet().iterator().next().getValue());
		
		//check that we have values for other decades
		assertTrue(formatedData.size() > decadeData.size());
	}
	
	@Test
	public void testStatsTransformationAltitude(){
		Map<Integer,Integer> altitudeData = new HashMap<Integer,Integer>();
		//add some data before 1850
		altitudeData.put(-200, 1);
		altitudeData.put(-100, 1);
		
		altitudeData.put(100, 7);
		altitudeData.put(200, 10);
		
		Map<String,Integer> formatedData = StatsTransformation.transformAltitudeData(
				altitudeData,ResourceBundle.getBundle("ApplicationResources"));
		
		//First element shall be sum of counts before 1850
		assertEquals(new Integer(2),formatedData.entrySet().iterator().next().getValue());
		//check that we have values for other decades
		assertTrue(formatedData.size() > altitudeData.size());
	}

}
