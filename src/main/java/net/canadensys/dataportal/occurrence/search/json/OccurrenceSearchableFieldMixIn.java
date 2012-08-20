package net.canadensys.dataportal.occurrence.search.json;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The mix-in annotation is used to decouple the model from its Jackson annotations.
 * It basically augments the annotations of a target class (or interface).
 * 
 * For the OccurrenceSearchableField, we want to keep the database related data on the server side.
 * This information is useless on the client side.
 * 
 * To use it call something like : 
 * objectMapper.getSerializationConfig().addMixInAnnotations(Target.class, MixIn.class);
 * http://www.cowtowncoder.com/blog/archives/2009/08/entry_305.html
 * @author canadensys
 *
 */
public interface OccurrenceSearchableFieldMixIn {
	@JsonIgnore String getRelatedField();
	@JsonIgnore List<String> getRelatedFields();
	@JsonAnyGetter public Map<String, String> getExtraProperties();
}
