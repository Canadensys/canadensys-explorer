package net.canadensys.dataportal.occurrence.search.json;

import java.util.List;

import net.canadensys.query.SearchableField;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The mix-in annotation is used to decouple the model from its Jackson annotations.
 * It basically augments the annotations of a target class (or interface).
 * 
 * For the SearchQueryPart, we want to keep the database related data on the server side.
 * This information is useless on the client side.
 * 
 * To use it call something like : 
 * objectMapper.getSerializationConfig().addMixInAnnotations(Target.class, MixIn.class);
 * http://www.cowtowncoder.com/blog/archives/2009/08/entry_305.html
 * @author canadensys
 *
 */
public interface SearchQueryPartMixIn {
	@JsonIgnore SearchableField getSearchableField();
	@JsonIgnore String getSingleField();
	@JsonIgnore List<String> getFieldList();
}
