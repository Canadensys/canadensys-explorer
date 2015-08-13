package net.canadensys.dataportal.occurrence.model;

import java.util.ArrayList;
import java.util.List;

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
	private List<String> associatedSequences;
	
	//prefiltered list
	private List<MultimediaViewModel> imageViewModelList;
	private List<MultimediaViewModel> otherMediaViewModelList;
		
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

	public List<String> getAssociatedSequences() {
		return associatedSequences;
	}

	public void setAssociatedSequences(List<String> associatedSequences) {
		this.associatedSequences = associatedSequences;
	}

}
