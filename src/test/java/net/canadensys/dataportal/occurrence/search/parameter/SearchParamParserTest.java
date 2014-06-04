package net.canadensys.dataportal.occurrence.search.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.dataportal.occurrence.search.parameter.parser.SearchParamParser;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

/**
 * This test checks that we can parse some URL parameters into SearchQueryPart.
 * This test will validate the parsed values inside SearchQueryPart.
 * @author canadensys
 *
 */
public class SearchParamParserTest {
	
	/**
	 * General test
	 */
	@Test
	public void testSearchParamParser(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"3"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"Laval"});
		
		//Group #2
		parametersMap.put("2_f", new String[]{"4"});
		parametersMap.put("2_o", new String[]{"in"});
		parametersMap.put("2_v_1", new String[]{"Titi"});
		parametersMap.put("2_v_2", new String[]{"Toto"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		while(searchQueryPartIt.hasNext()){
			SearchQueryPart searchQueryPart = searchQueryPartIt.next();
			if(searchQueryPart.getSearchableFieldId().equals(3)){
				assertEquals(QueryOperatorEnum.fromString("eq"),searchQueryPart.getOp());
				assertEquals("Laval",searchQueryPart.getSingleValue());
			}
			else if(searchQueryPart.getSearchableFieldId().equals(4)){
				assertEquals(QueryOperatorEnum.fromString("in"),searchQueryPart.getOp());
				assertEquals("Titi",searchQueryPart.getValueList().get(0));
				assertEquals("Toto",searchQueryPart.getValueList().get(1));
			}
			else{
				fail();
			}
		}
		
		//test inverse operation
		Map<String,String> queryString = paramParser.toQueryStringMap(result);
		assertEquals(parametersMap.get("1_f")[0], queryString.get("1_f"));
		assertEquals(parametersMap.get("2_f")[0], queryString.get("2_f"));
		
		assertEquals(parametersMap.get("2_v_1")[0], queryString.get("2_v_1"));
		assertEquals(parametersMap.get("2_v_2")[0], queryString.get("2_v_2"));
	}
	
	@Test
	public void testSearchParamParserDate(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"5"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"2009-08-"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		
		//the raw value should be the same
		assertEquals("2009-08-",searchQueryPart.getSingleValue());
		
		assertEquals(2009,searchQueryPart.getParsedValue("2009-08-", "syear"));
		assertEquals(8,searchQueryPart.getParsedValue("2009-08-", "smonth"));
		assertEquals(null,searchQueryPart.getParsedValue("2009-08-", "sday"));
	}
	
	@Test
	public void testSearchParamParserDateInterval(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"5"});
		parametersMap.put("1_o", new String[]{"between"});
		parametersMap.put("1_v_1", new String[]{"2009-08-"});
		parametersMap.put("1_v_2", new String[]{"2009-11-"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		
		//the raw value should be the right order
		assertEquals("2009-08-",searchQueryPart.getValueList().get(0));
		assertEquals("2009-11-",searchQueryPart.getValueList().get(1));
		
		assertEquals(2009,searchQueryPart.getParsedValue("2009-08-", "syear"));
		assertEquals(8,searchQueryPart.getParsedValue("2009-08-", "smonth"));
		assertEquals(null,searchQueryPart.getParsedValue("2009-08-", "sday"));
		
		assertEquals(2009,searchQueryPart.getParsedValue("2009-11-", "syear"));
		assertEquals(11,searchQueryPart.getParsedValue("2009-11-", "smonth"));
		assertEquals(null,searchQueryPart.getParsedValue("2009-11-", "sday"));
	}
	
	@Test
	public void testSearchParamParserWrongDate(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"5"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"2009-08"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		//wanted behavior: make this SearchQueryPart invalid
		assertEquals(0,searchQueryPart.getValueList().size());
	}
		
	@Test
	public void testSearchParamParserMinMax(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"10"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"4"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		//wanted behavior: make this SearchQueryPart invalid
		assertEquals(4d,searchQueryPart.getParsedValue("4", "minimumelevationinmeters"));
	}
	
	@Test
	public void testSearchParamParserSingleValueParsed(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"20"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"true"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		//wanted behavior: make this SearchQueryPart invalid
		assertEquals(Boolean.TRUE,searchQueryPart.getParsedValue("true", "hascoordinates"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSearchGeoRectangle(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"32"}); //GEO_RECTANGLE
		parametersMap.put("1_o", new String[]{"in"});
		
		parametersMap.put("1_v_1", new String[]{"-25.363882,131.044922"});
		parametersMap.put("1_v_2", new String[]{"-26.985412,130.053265"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		Pair<String,String> resultPair = (Pair<String,String>)(searchQueryPart.getParsedValue("-25.363882,131.044922", "the_geom"));
		assertEquals(resultPair, Pair.of("-25.363882","131.044922"));

		resultPair = (Pair<String,String>)(searchQueryPart.getParsedValue("-26.985412,130.053265", "the_geom"));
		assertEquals(resultPair, Pair.of("-26.985412","130.053265"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSearchGeoPolygon(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"33"});
		parametersMap.put("1_o", new String[]{"in"});
		
		parametersMap.put("1_v_1", new String[]{"-25.363882,131.044922"});
		parametersMap.put("1_v_2", new String[]{"-26.985412,130.053265"});
		parametersMap.put("1_v_3", new String[]{"-26.979865,130.987986"});
		parametersMap.put("1_v_4", new String[]{"-25.363882,131.044922"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		Pair<String,String> resultPair = (Pair<String,String>)(searchQueryPart.getParsedValue("-25.363882,131.044922", "the_geom"));
		assertEquals(resultPair, Pair.of("-25.363882","131.044922"));

		resultPair = (Pair<String,String>)(searchQueryPart.getParsedValue("-26.985412,130.053265", "the_geom"));
		assertEquals(resultPair, Pair.of("-26.985412","130.053265"));
		
		resultPair = (Pair<String,String>)(searchQueryPart.getParsedValue("-26.979865,130.987986", "the_geom"));
		assertEquals(resultPair, Pair.of("-26.979865","130.987986"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSearchGeoEllipse(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1
		parametersMap.put("1_f", new String[]{"34"}); //GEO_ELLIPSE
		parametersMap.put("1_o", new String[]{"in"});
		
		parametersMap.put("1_v_1", new String[]{"-25.363882,131.044922"});
		parametersMap.put("1_v_2", new String[]{"255"});

		SearchServiceConfig searchConfig = new SearchServiceConfig();
		SearchParamParser paramParser = new SearchParamParser();
		paramParser.setSearchConfig(searchConfig);
		
		Collection<SearchQueryPart> result = paramParser.parse(parametersMap);
		
		Iterator<SearchQueryPart> searchQueryPartIt = result.iterator();
		SearchQueryPart searchQueryPart = searchQueryPartIt.next();
		Pair<String,String> resultPair = (Pair<String,String>)(searchQueryPart.getParsedValue("-25.363882,131.044922", "the_geom"));
		assertEquals(resultPair, Pair.of("-25.363882","131.044922"));

		Integer radius = (Integer)(searchQueryPart.getParsedValue("255", "the_geom"));
		assertEquals(255,radius.intValue());
	}
}
