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

	private String dataSourcePageURL;
	private String recommendedCitation;
	
	private List<MultimediaViewModel> multimediaViewModelList;
	
	//prefiltered list
	private List<MultimediaViewModel> imageViewModelList;
	private List<MultimediaViewModel> otherMediaViewModelList;
	
	private Map<String,List<Pair<String,String>>> associatedSequencesPerProviderMap;
		
	public void addMultimediaViewModel(MultimediaViewModel multimediaViewModel){
		if(multimediaViewModelList == null){
			multimediaViewModelList = new ArrayList<MultimediaViewModel>();
		}
		multimediaViewModelList.add(multimediaViewModel);
		
		if(multimediaViewModel.isImage()){
			if(imageViewModelList == null){
				imageViewModelList = new ArrayList<MultimediaViewModel>();
			}
			imageViewModelList.add(multimediaViewModel);
		}
		else{
			if(otherMediaViewModelList == null){
				otherMediaViewModelList = new ArrayList<MultimediaViewModel>();
			}
			otherMediaViewModelList.add(multimediaViewModel);
		}
	}
	
	public List<MultimediaViewModel> getMultimediaViewModelList(){
		return multimediaViewModelList;
	}
	
	/**
	 * Return List of MultimediaViewModel including only the MultimediaViewModel where isImage is true
	 * @return
	 */
	public List<MultimediaViewModel> getImageViewModelList(){
		return imageViewModelList;
	}
	
	/**
	 * Return List of MultimediaViewModel including only the MultimediaViewModel where isImage is false
	 * @return
	 */
	public List<MultimediaViewModel> getOtherMediaViewModelList(){
		return otherMediaViewModelList;
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
	
	public Map<String,List<Pair<String,String>>> getAssociatedSequencesPerProviderMap() {
		return associatedSequencesPerProviderMap;
	}

	public String getDataSourcePageURL() {
		return dataSourcePageURL;
	}
	public void setDataSourcePageURL(String dataSourcePageURL) {
		this.dataSourcePageURL = dataSourcePageURL;
	}

	public String getRecommendedCitation() {
		return recommendedCitation;
	}

	public void setRecommendedCitation(String recommendedCitation) {
		this.recommendedCitation = recommendedCitation;
	}
}
