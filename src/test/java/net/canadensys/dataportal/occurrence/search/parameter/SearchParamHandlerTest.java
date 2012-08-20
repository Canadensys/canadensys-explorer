package net.canadensys.dataportal.occurrence.search.parameter;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.dataportal.occurrence.search.parameter.SearchParamHandler;
import net.canadensys.query.SearchQueryPart;

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

}
