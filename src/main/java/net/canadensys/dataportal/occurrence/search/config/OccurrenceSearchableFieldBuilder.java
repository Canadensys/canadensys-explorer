package net.canadensys.dataportal.occurrence.search.config;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchableFieldTypeEnum;

/**
 * Class to build OccurrenceSearchableField object.
 * Set of chainable methods to allow easy building and configuration of a OccurrenceSearchableField.
 * @author canadensys
 *
 */
public class OccurrenceSearchableFieldBuilder {
	private OccurrenceSearchableField searchableField = null;
		
	public OccurrenceSearchableFieldBuilder(int id, String name){
		searchableField = new OccurrenceSearchableField();
		searchableField.setSearchableFieldId(id);
		searchableField.setSearchableFieldName(name);
		searchableField.setSupportSuggestion(false);
		searchableField.setSupportPartialMatch(false);
		searchableField.setSupportSelectionList(false);
		//this list is mandatory so, create an empty one
		searchableField.setSupportedOperator(new ArrayList<QueryOperatorEnum>());
	}
	
	/**
	 * Internal check to make sure we only return valid OccurrenceSearchableField.
	 * Since there is many way to configure a OccurrenceSearchableField we need to make at the
	 * end that the object is valid.
	 * @return
	 */
	private boolean isValid(){
		if(searchableField.getSupportedOperator().size() < 1){
			return false;
		}
		if(searchableField.getSearchableFieldTypeEnum() == null){
			return false;
		}
		//is SelectionList is supported, the other options can not be supported (SelectionList already
		//contains all the possible values)
		if(searchableField.isSupportSelectionList() &&
				(searchableField.isSupportPartialMatch() || searchableField.isSupportSuggestion())){
			return false;
		}
		return true;
	}
	
	public OccurrenceSearchableFieldBuilder singleValue(String relatedField, Class<?> type){
		searchableField.setSearchableFieldTypeEnum(SearchableFieldTypeEnum.SINGLE_VALUE);
		searchableField.addRelatedField(relatedField);
		searchableField.setType(type);
		return this;
	}
	public OccurrenceSearchableFieldBuilder startEndDate(String fieldYear, String fieldMonth, String fieldDay){
		searchableField.setSearchableFieldTypeEnum(SearchableFieldTypeEnum.START_END_DATE);
		searchableField.addRelatedField(fieldYear);
		searchableField.addRelatedField(fieldMonth);
		searchableField.addRelatedField(fieldDay);
		return this;
	}
	
	public OccurrenceSearchableFieldBuilder minMaxNumber(String minField, String maxField, Class<?> type){
		searchableField.setSearchableFieldTypeEnum(SearchableFieldTypeEnum.MIN_MAX_NUMBER);
		searchableField.addRelatedField(minField);
		searchableField.addRelatedField(maxField);
		searchableField.setType(type);
		return this;
	}
	
	public OccurrenceSearchableFieldBuilder insideEnvelope(String theGeomField){
		searchableField.setSearchableFieldTypeEnum(SearchableFieldTypeEnum.INSIDE_ENVELOPE_GEO);
		//type should be removed
		searchableField.setType(String.class);
		searchableField.addRelatedField(theGeomField);
		return this;
	}
	
	public OccurrenceSearchableFieldBuilder insidePolygon(String theGeomField){
		searchableField.setSearchableFieldTypeEnum(SearchableFieldTypeEnum.INSIDE_POLYGON_GEO);
		searchableField.setType(String.class);
		searchableField.addRelatedField(theGeomField);
		return this;
	}

	/**
	 * Chainable method to enable the LIKE operator and the supportPartialMatch on the OccurrenceSearchableField
	 * @param likeOp
	 * @return
	 */
	public OccurrenceSearchableFieldBuilder likeOperator(QueryOperatorEnum likeOp){
		searchableField.setSupportPartialMatch(true);
		List<QueryOperatorEnum> supportedOperator = searchableField.getSupportedOperator();
		supportedOperator.add(likeOp);
		return this;
	}
	/**
	 * Chainable method to enable the EQ operator on the OccurrenceSearchableField
	 * @return
	 */
	public OccurrenceSearchableFieldBuilder eqOperator(){
		List<QueryOperatorEnum> supportedOperator = searchableField.getSupportedOperator();
		supportedOperator.add(QueryOperatorEnum.EQ);
		return this;
	}
	/**
	 * Chainable method to enable the BETWEEN operator on the OccurrenceSearchableField
	 * @return
	 */
	public OccurrenceSearchableFieldBuilder betweenOperator(){
		List<QueryOperatorEnum> supportedOperator = searchableField.getSupportedOperator();
		supportedOperator.add(QueryOperatorEnum.BETWEEN);
		return this;
	}
	
	/**
	 * Chainable method to enable the IN operator on the OccurrenceSearchableField
	 * @return
	 */
	public OccurrenceSearchableFieldBuilder inOperator(){
		List<QueryOperatorEnum> supportedOperator = searchableField.getSupportedOperator();
		supportedOperator.add(QueryOperatorEnum.IN);
		return this;
	}
	
	public OccurrenceSearchableFieldBuilder supportSuggestion(){
		searchableField.setSupportSuggestion(true);
		return this;
	}
	
	public OccurrenceSearchableFieldBuilder supportSelectionList(){
		searchableField.setSupportSelectionList(true);
		return this;
	}
	
	public OccurrenceSearchableField toOccurrenceSearchableField(){
		if(isValid()){
			return searchableField;
		}
		return null;
	}
}
