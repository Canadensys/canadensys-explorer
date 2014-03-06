package net.canadensys.dataportal.occurrence.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchableFieldTypeEnum;

import org.junit.Test;

/**
 * Test OccurrenceSearchableField equals and hashCode.
 * @author cgendreau
 *
 */
public class OccurrenceSearchableFieldTest {
	
	
	/**
	 * Ensure that 2 different OccurrenceSearchableField, with the exact same content are equals.
	 */
	@Test
	public void testEquals(){
		OccurrenceSearchableField obj1 = new OccurrenceSearchableField();
		initObject(obj1);
		
		OccurrenceSearchableField obj2 = new OccurrenceSearchableField();
		initObject(obj2);
		
		assertEquals(obj1,obj2);
	}
	
	@Test
	public void testNotEquals(){
		OccurrenceSearchableField obj1 = new OccurrenceSearchableField();
		initObject(obj1);
		
		OccurrenceSearchableField obj2 = new OccurrenceSearchableField();
		initObject(obj2);
		obj2.addExtraProperty("key2", "value2");
		assertNotEquals(obj1,obj2);
	}
	
	@Test
	public void testHashCode(){
		OccurrenceSearchableField obj1 = new OccurrenceSearchableField();
		initObject(obj1);
		
		OccurrenceSearchableField obj2 = new OccurrenceSearchableField();
		initObject(obj2);
		
		List<OccurrenceSearchableField> occurrenceSearchableFieldList = new ArrayList<OccurrenceSearchableField>();
		occurrenceSearchableFieldList.add(obj1);
		assertTrue(occurrenceSearchableFieldList.contains(obj2));
		
		obj2.addExtraProperty("key2", "value2");
		assertFalse(occurrenceSearchableFieldList.contains(obj2));
	}
	
	private void initObject(OccurrenceSearchableField obj){
		obj.setSearchableFieldId(1);
		obj.setSearchableFieldName("searchField");
		obj.setType(String.class);
		obj.setSearchableFieldTypeEnum(SearchableFieldTypeEnum.SINGLE_VALUE);
		obj.setSupportPartialMatch(true);
		obj.setSupportSelectionList(false);
		obj.setSupportSuggestion(true);
		obj.addExtraProperty("key", "value");
		obj.addRelatedField("field1");
		List<QueryOperatorEnum> op = new ArrayList<QueryOperatorEnum>();
		op.add(QueryOperatorEnum.EQ);
		obj.setSupportedOperator(op);
	}

}
