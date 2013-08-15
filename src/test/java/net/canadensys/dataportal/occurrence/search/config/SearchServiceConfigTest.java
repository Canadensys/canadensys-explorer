package net.canadensys.dataportal.occurrence.search.config;

import static org.junit.Assert.*;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.query.interpreter.QueryPartInterpreter;
import net.canadensys.query.interpreter.QueryPartInterpreterResolver;

import org.junit.Test;

/**
 * This test ensures the SearchServiceConfig is correctly setup.
 * @author canadensys
 *
 */
public class SearchServiceConfigTest {
	
	/**
	 * Test that all Search fields actually exist
	 */
	@Test
	public void testOccurrenceSearchFieldsConstant(){
		Class<OccurrenceModel> occClass = OccurrenceModel.class;
		for(String fieldName : SearchServiceConfig.OCCURRENCE_SEARCH_FIELDS){
			try {
				occClass.getDeclaredField(fieldName);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				fail(fieldName + " is not a member of class OccurrenceModel");
			}
		}
	}
	
	/**
	 * Test that all Summary fields actually exist
	 */
	@Test
	public void testOccurrenceSummaryFieldsConstant(){
		Class<OccurrenceModel> occClass = OccurrenceModel.class;
		for(String fieldName : SearchServiceConfig.OCCURENCE_SUMMARY_FIELDS){
			try {
				occClass.getDeclaredField(fieldName);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				fail(fieldName + " is not a member of class OccurrenceModel");
			}
		}
	}
	
	/**
	 * Make sure everything is setup correctly by calling the right Interpreter on all Searchable Fields.
	 */
	@Test
	public void testSearchableFieldMap(){
		SearchServiceConfig searchServiceConfig = new SearchServiceConfig();
		
		QueryPartInterpreter interpreter;
		for(Integer key : searchServiceConfig.getSearchableFieldMap().keySet()){
			interpreter = QueryPartInterpreterResolver.getQueryPartInterpreter(searchServiceConfig.getSearchableFieldbyId(key).getSearchableFieldTypeEnum());
			if(!interpreter.canHandleSearchableField(searchServiceConfig.getSearchableFieldbyId(key))){
				fail("OccurrenceSearchableField :" + key + " can't be handled by the proper Interpreter");
			}
		}
	}

}
