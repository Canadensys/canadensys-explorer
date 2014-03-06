package net.canadensys.dataportal.occurrence.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchableField;
import net.canadensys.query.SearchableFieldTypeEnum;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * SearchableField implementation for the Occurrence Portal search.
 * @author canandesys
 *
 */
public class OccurrenceSearchableField implements SearchableField{
	private int searchableFieldId;
	private String searchableFieldName;
	private List<QueryOperatorEnum> supportedOperator;
	private Class<?> type;
	private SearchableFieldTypeEnum searchableFieldTypeEnum;
	private List<String> relatedFields;

	private boolean supportSuggestion;
	private boolean supportPartialMatch;
	private boolean supportSelectionList;
	
	private Map<String,String> extraProperties;
	
	/**
	 * Default constructor
	 */
	public OccurrenceSearchableField(){}
	
	/**
	 * Copy constructor
	 * @param toCopy
	 */
	public OccurrenceSearchableField(OccurrenceSearchableField toCopy){
		this.searchableFieldId = toCopy.searchableFieldId;
		this.searchableFieldName = toCopy.searchableFieldName;
		this.type = toCopy.type;
		this.searchableFieldTypeEnum = toCopy.searchableFieldTypeEnum;
		this.supportSuggestion = toCopy.supportSuggestion;
		this.supportPartialMatch = toCopy.supportPartialMatch;
		this.supportSelectionList = toCopy.supportSelectionList;
		
		//copy lists and maps
		if(toCopy.supportedOperator != null){
			this.supportedOperator = new ArrayList<QueryOperatorEnum>(toCopy.supportedOperator);
		}
		if(toCopy.relatedFields != null){
			this.relatedFields = new ArrayList<String>(toCopy.relatedFields);
		}
		if(toCopy.extraProperties != null){
			this.extraProperties = new HashMap<String, String>(toCopy.extraProperties);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
		     return false;
		}
		
		OccurrenceSearchableField osf = (OccurrenceSearchableField) obj;
		return new EqualsBuilder()
			.append(searchableFieldId, osf.searchableFieldId)
			.append(searchableFieldName, osf.searchableFieldName)
			.append(type, osf.type)
			.append(searchableFieldTypeEnum, osf.searchableFieldTypeEnum)
			.append(supportSuggestion, osf.supportSuggestion)
			.append(supportPartialMatch, osf.supportPartialMatch)
			.append(supportSelectionList, osf.supportSelectionList)
			.append(supportedOperator, osf.supportedOperator)
			.append(relatedFields, osf.relatedFields)
			.append(extraProperties, osf.extraProperties)
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(searchableFieldId)
				.append(searchableFieldName)
				.append(type)
				.append(searchableFieldTypeEnum)
				.append(supportSuggestion)
				.append(supportPartialMatch)
				.append(supportSelectionList)
				.append(supportedOperator)
				.append(relatedFields)
				.append(extraProperties)
			    .toHashCode();
	}
	
	@Override
	public int getSearchableFieldId() {
		return searchableFieldId;
	}
	public void setSearchableFieldId(int searchableFieldId) {
		this.searchableFieldId =  searchableFieldId;
	}

	@Override
	public String getSearchableFieldName() {
		return searchableFieldName;
	}
	public void setSearchableFieldName(String searchableFieldName) {
		this.searchableFieldName = searchableFieldName;
	}
	
	@Override
	public List<QueryOperatorEnum> getSupportedOperator() {
		return supportedOperator;
	}
	public void setSupportedOperator(List<QueryOperatorEnum> supportedOperator) {
		this.supportedOperator = supportedOperator;
	}
	
	@Override
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public SearchableFieldTypeEnum getSearchableFieldTypeEnum() {
		return searchableFieldTypeEnum;
	}
	public void setSearchableFieldTypeEnum(SearchableFieldTypeEnum searchableFieldTypeEnum) {
		this.searchableFieldTypeEnum = searchableFieldTypeEnum;
	}
	
	@Override
	public String getRelatedField() {
		if(relatedFields != null && relatedFields.size() >= 1){
			return relatedFields.get(0);
		}
		return null;
	}

	@Override
	public List<String> getRelatedFields() {
		return relatedFields;
	}
	public void addRelatedField(String field) {
		if(relatedFields == null){
			relatedFields = new ArrayList<String>();
		}
		relatedFields.add(field);
	}
	
	public boolean isSupportSuggestion() {
		return supportSuggestion;
	}
	public void setSupportSuggestion(boolean supportSuggestion) {
		this.supportSuggestion = supportSuggestion;
	}
	
	public boolean isSupportPartialMatch() {
		return supportPartialMatch;
	}
	public void setSupportPartialMatch(boolean supportPartialMatch) {
		this.supportPartialMatch = supportPartialMatch;
	}
	
	public boolean isSupportSelectionList() {
		return supportSelectionList;
	}
	public void setSupportSelectionList(boolean supportSelectionList) {
		this.supportSelectionList = supportSelectionList;
	}
	
	public Map<String, String> getExtraProperties() {
		return extraProperties;
	}
	
	/**
	 * Add an extra property to the OccurrenceSearchableField.
	 * @param key
	 * @param value
	 */
	public void addExtraProperty(String key, String value) {
		if(extraProperties == null){
			extraProperties = new HashMap<String, String>();
		}
		extraProperties.put(key, value);
	}
}
