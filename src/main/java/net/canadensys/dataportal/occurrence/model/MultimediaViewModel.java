package net.canadensys.dataportal.occurrence.model;

/**
 * This immutable model is tailored to display data about multimedia.
 * It contains additional information that is relevant for display purpose.
 * @author canadensys
 *
 */
public class MultimediaViewModel {
	
	private final String references;
	private final String identifier;
	private final String title;
	private final String license;
	private final String creator;
	
	private final boolean isImage;
	private final String licenseShortname;
	
	public MultimediaViewModel(String references, String identifier, String title, String license, String creator, boolean isImage, String licenseShortname){
		this.references = references;
		this.identifier = identifier;
		this.title = title;
		this.license = license;
		this.creator = creator;
		
		this.isImage = isImage;
		this.licenseShortname = licenseShortname;
	}
	
	public String getReferences() {
		return references;
	}
	public String getIdentifier() {
		return identifier;
	}
	public String getTitle() {
		return title;
	}
	public String getLicense() {
		return license;
	}
	public String getCreator() {
		return creator;
	}
	public boolean isImage() {
		return isImage;
	}
	public String getLicenseShortname() {
		return licenseShortname;
	}
	
}
