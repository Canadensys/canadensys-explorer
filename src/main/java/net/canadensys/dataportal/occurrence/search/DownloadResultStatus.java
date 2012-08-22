package net.canadensys.dataportal.occurrence.search;

/**
 * A model representing the result of a download request.
 * @author canadensys
 *
 */
public class DownloadResultStatus {
	
	public enum DownloadResultMode{SYNC,ASYNC,ERROR};
	
	private DownloadResultMode mode;
	private String fileName;
	
	public DownloadResultMode getMode() {
		return mode;
	}
	public void setMode(DownloadResultMode mode) {
		this.mode = mode;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
