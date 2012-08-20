package net.canadensys.dataportal.occurrence.search.config;

import static org.junit.Assert.*;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
import net.canadensys.query.QueryOperatorEnum;

import org.junit.Test;

/**
 * This test ensures OccurrenceSearchableFieldBuilder can actually build a OccurrenceSearchableField
 * @author canadensys
 *
 */
public class OccurrenceSearchableFieldBuilderTest {
	
	@Test
	public void testOccurrenceSearchableFieldBuilder(){
		OccurrenceSearchableFieldBuilder b = new OccurrenceSearchableFieldBuilder(1,"test-field");
		//make sure we never return an incomplete OccurrenceSearchableField
		assertNull(b.toOccurrenceSearchableField());
		
		//test method chaining
		OccurrenceSearchableField occField = b.eqOperator().singleValue("testField",String.class).toOccurrenceSearchableField();
		
		assertEquals(QueryOperatorEnum.EQ, occField.getSupportedOperator().get(0));
		assertEquals("testField", occField.getRelatedField());
		
		b.likeOperator(QueryOperatorEnum.ELIKE);
		//all LIKE operator should support partial match
		assertTrue(occField.isSupportPartialMatch());
		assertTrue(occField.getSupportedOperator().contains(QueryOperatorEnum.ELIKE));
	}
}
