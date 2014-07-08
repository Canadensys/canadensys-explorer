package net.canadensys.dataportal.occurrence.search.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.query.OrderEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.sort.SearchSortPart;

import org.junit.Test;

/**
 * This test checks that we can parse some URL parameters into SearchQueryPart.
 * @author canadensys
 *
 */
public class SearchParamHandlerTest {
		
	@Test
	public void testSearchParamHandler(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1 (valid)
		parametersMap.put("1_f", new String[]{"3"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"Laval"});
		
		//Group #2 (invalid)
		parametersMap.put("2_f", new String[]{"6"});
		parametersMap.put("2_v_1", new String[]{"Titi"});
		parametersMap.put("2_v_2", new String[]{"Toto"});
		
		SearchParamHandler paramHandler = new SearchParamHandler();
		paramHandler.setSearchServiceConfig(new SearchServiceConfig());
		paramHandler.initialize();
		
		Collection<SearchQueryPart> sqpColl = paramHandler.getSearchQueryPartCollection(parametersMap);
		assertEquals(1, sqpColl.size()); //group 2 should be dropped
		
		Map<String,List<SearchQueryPart>> result = paramHandler.asMap(sqpColl);
		assertTrue(result.get("continent") != null);
		assertEquals("Laval",result.get("continent").get(0).getSingleValue());
	}
	
	@Test
	public void testSearchParamHandlerSortPath(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1 (valid)
		parametersMap.put("1_f", new String[]{"3"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"Laval"});
		
		//Sorting options
		parametersMap.put("sortby", new String[]{"country"});
		parametersMap.put("sort", new String[]{"desc"});
		
		SearchParamHandler paramHandler = new SearchParamHandler();
		paramHandler.setSearchServiceConfig(new SearchServiceConfig());
		paramHandler.initialize();
		
		
		SearchSortPart sortPart = paramHandler.getSearchQuerySort(parametersMap);
		assertEquals(OrderEnum.DESC, sortPart.getOrder());
		assertEquals("country", sortPart.getOrderByColumn());
		
		//try with non-valid sorting column
		parametersMap.put("sortby", new String[]{"thisisnotvalid"});
		sortPart = paramHandler.getSearchQuerySort(parametersMap);
		assertEquals(OrderEnum.DESC, sortPart.getOrder());
		assertNull(sortPart.getOrderByColumn());
	}
	
	@Test
	public void testGetSearchQueryRelatedParameters(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		//Group #1 (valid)
		parametersMap.put("1_f", new String[]{"3"});
		parametersMap.put("1_o", new String[]{"eq"});
		parametersMap.put("1_v", new String[]{"Laval"});
		parametersMap.put("view", new String[]{"stats"});
		
		SearchParamHandler paramHandler = new SearchParamHandler();
		paramHandler.setSearchServiceConfig(new SearchServiceConfig());
		paramHandler.initialize();
		
		Map<String,String> queryRelatedParams = paramHandler.getSearchQueryRelatedParameters(parametersMap);
		assertEquals(3, queryRelatedParams.size());
		assertTrue(queryRelatedParams.containsKey("1_f"));
		assertTrue(queryRelatedParams.containsKey("1_o"));
		assertTrue(queryRelatedParams.containsKey("1_v"));
	}
	
	@Test
	public void testEmptyParams(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		parametersMap.put("lang", new String[]{"fr"});
		
		SearchParamHandler paramHandler = new SearchParamHandler();
		paramHandler.setSearchServiceConfig(new SearchServiceConfig());
		paramHandler.initialize();
		
		Collection<SearchQueryPart> queryRelatedParams = paramHandler.getSearchQueryPartCollection(parametersMap);
		assertEquals(0, queryRelatedParams.size());
	}
	
	@Test
	public void testWrongParams(){
		Map<String,String[]> parametersMap = new HashMap<String, String[]>();
		parametersMap.put("3_3_3", new String[]{"fr"});
		
		SearchParamHandler paramHandler = new SearchParamHandler();
		paramHandler.setSearchServiceConfig(new SearchServiceConfig());
		paramHandler.initialize();
		
		Collection<SearchQueryPart> queryRelatedParams = paramHandler.getSearchQueryPartCollection(parametersMap);
		assertEquals(0, queryRelatedParams.size());
	}
}
