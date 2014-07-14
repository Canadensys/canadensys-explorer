package net.canadensys.dataportal.occurrence.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

/**
 * This model is tailored for views.
 * It contains additional information that is relevant for display purpose.
 * @author canadensys
 *
 */
public class OccurrenceViewModel {

	private List<String> imageList;
	private List<String> otherMediaList;
	
	private Map<String,List<Pair<String,String>>> associatedSequencesPerProviderMap;
	
	public void addImage(String image){
		if(imageList == null){
			imageList = new ArrayList<String>();
		}
		imageList.add(image);
	}
	
	public void addOtherMedia(String otherMedia){
		if(otherMediaList == null){
			otherMediaList = new ArrayList<String>();
		}
		otherMediaList.add(otherMedia);
	}
	
	/**
	 * @param sequenceProvider
	 * @param providedSequenceId identifier for the sequence including the provider e.g. GenBank:KC251652
	 * @param link
	 */
	public void addAssociatedSequenceLink(String sequenceProvider, String providedSequenceId, String link){
		if(associatedSequencesPerProviderMap == null){
			associatedSequencesPerProviderMap = new HashMap<String, List<Pair<String,String>>>();
		}
		
		if(associatedSequencesPerProviderMap.get(sequenceProvider) == null){
			associatedSequencesPerProviderMap.put(sequenceProvider, new ArrayList<Pair<String,String>>());
		}
		associatedSequencesPerProviderMap.get(sequenceProvider).add(Pair.of(providedSequenceId, link));
	}
	
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
	public List<String> getOtherMediaList() {
		return otherMediaList;
	}
	public void setOtherMediaList(List<String> otherMediaList) {
		this.otherMediaList = otherMediaList;
	}
	
	public Map<String,List<Pair<String,String>>> getAssociatedSequencesPerProviderMap() {
		return associatedSequencesPerProviderMap;
	}
	
}
