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

	private List<String> imageList;
	private List<String> otherMediaList;
	
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
	
	
}
