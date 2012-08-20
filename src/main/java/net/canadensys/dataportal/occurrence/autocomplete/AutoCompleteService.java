package net.canadensys.dataportal.occurrence.autocomplete;

import java.util.Collection;

import net.canadensys.dataportal.occurrence.model.UniqueValuesModel;

/**
 * AutoCompleteService layer interface to handle auto-complete feature. This interface handles only high-level methods.
 * @author canadensys
 *
 */
public interface AutoCompleteService {
	
	/**
	 * Search for auto-completion on a specific field.
	 * @param field
	 * @param currValue
	 * @return search result as JSON string
	 */
	public String search(String field, String currValue);
	
	/**
	 * Return all possible values for a specific field.
	 * @param field
	 * @return
	 */
	public Collection<UniqueValuesModel> getAllPossibleValues(String field);
}
