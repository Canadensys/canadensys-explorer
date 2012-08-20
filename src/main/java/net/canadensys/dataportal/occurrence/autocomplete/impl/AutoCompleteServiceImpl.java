package net.canadensys.dataportal.occurrence.autocomplete.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.canadensys.comparator.UnaccentStringComparator;
import net.canadensys.dataportal.occurrence.autocomplete.AutoCompleteService;
import net.canadensys.dataportal.occurrence.dao.OccurrenceAutoCompleteDAO;
import net.canadensys.dataportal.occurrence.model.UniqueValuesModel;
import net.canadensys.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the Occurrence auto-complete service layer.
 * @author canadensys
 *
 */
@Service("autoCompleteService")
public class AutoCompleteServiceImpl implements AutoCompleteService{
	
	@Autowired(required=true)
	private OccurrenceAutoCompleteDAO occDAO;
	
	/**
	 * Search for auto-completion.
	 * This implementation will sanitized the currValue for the comparison
	 * @param field
	 * @param currValue
	 * @return JSON string (number of result depends on DAO configuration)
	 */
	@Transactional(readOnly=true)
	public String search(String field, String currValue) {
		if(currValue != null){
			return occDAO.getSuggestionsFor(field, StringUtils.unaccent(currValue.toLowerCase()),true);
		}
		return occDAO.getSuggestionsFor(field,null,false);
	}
	
	/**
	 * Get all possible value for a field, sorted alphabetically.
	 * @param field
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Collection<UniqueValuesModel> getAllPossibleValues(String field) {
		List<UniqueValuesModel> possibleValues = occDAO.getAllPossibleValues(field);
		Map<String,UniqueValuesModel> sortedPossibleValues = new TreeMap<String,UniqueValuesModel>(new UnaccentStringComparator());
		for(UniqueValuesModel currVal : possibleValues){
			sortedPossibleValues.put(currVal.getValue().toLowerCase(),currVal);
		}
		return sortedPossibleValues.values();
	}

}
