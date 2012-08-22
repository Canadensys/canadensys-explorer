package net.canadensys.dataportal.occurrence.search;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.canadensys.comparator.UnaccentStringComparator;
import net.canadensys.dataportal.occurrence.dao.DownloadLogDAO;
import net.canadensys.dataportal.occurrence.dao.OccurrenceDAO;
import net.canadensys.dataportal.occurrence.dwc.OccurrenceDwcWriter;
import net.canadensys.dataportal.occurrence.model.DownloadLogModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.utils.ZipUtils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is an utility class to allow creation of a DarwinCore archive in a separate class.
 * This class resides at the Service layer.
 * The main goal is to allow the method to run asynchronously with its own transaction.
 * @author canadensys
 *
 */
@Component("DwcaBuilder")
public class DwcaBuilder {
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(DwcaBuilder.class);
		
	@Autowired(required=true)
	private OccurrenceDAO occurrenceDAO;
	
	@Autowired(required=true)
	private DownloadLogDAO downloadDAO;
	
	@Transactional(readOnly=true)
	public File generatesDarwinCoreArchive(List<String> headers, Map<String, List<SearchQueryPart>> searchCriteria, String fullPath, String fullFilePath) {
		
		Iterator<OccurrenceModel> it = null;
		try{
			it = occurrenceDAO.searchIteratorRaw(searchCriteria,null);
		}
		catch(Exception e){
			LOGGER.fatal("EXCEPTION",e);
		}
		
		File dwcaFolder = new File(fullPath);
		File dwcaFile = new File(fullFilePath);
		if(OccurrenceDwcWriter.write(headers,dwcaFolder, it)){
			if(ZipUtils.zipFolder(dwcaFolder, dwcaFile.getAbsolutePath())){
				try{
					FileUtils.deleteDirectory(dwcaFolder);
				} catch (IOException e) {
					LOGGER.fatal("Failed to delete Dwca working dir at " + dwcaFolder, e);
				}
			}
			else{
				LOGGER.fatal("Failed to zip the Dwca at " + fullPath);
				return null;
			}
		}
		else{
			LOGGER.fatal("Failed to write Dwca at " + fullPath);
			return null;
		}
		return dwcaFile;
	}
	
	@Transactional(readOnly=true)
	public int getOccurrenceCount(Map<String, List<SearchQueryPart>> searchCriteria){
		return occurrenceDAO.getOccurrenceCount(searchCriteria);
	}
	
	@Transactional(readOnly=true)
	public List<String> getDistinctInstitutionCode(Map<String, List<SearchQueryPart>> searchCriteria){
		 List<String> result = occurrenceDAO.getDistinctInstitutionCode(searchCriteria);
		 Collections.sort(result, new UnaccentStringComparator());
		 return result;
	}
	
	/**
	 * This is done in this class because it's mainly used in a Thread
	 * @param searchCriteria
	 * @param numberOfRecord
	 * @param email
	 */
	@Transactional
	public DownloadLogModel logDownload(String email,String searchCriteria) {
		DownloadLogModel downloadLogModel = new DownloadLogModel();
		downloadLogModel.setEvent_date(new Date());
		downloadLogModel.setSearch_criteria(searchCriteria);
		downloadLogModel.setEmail(email);
		downloadDAO.save(downloadLogModel);
		return downloadLogModel;
	}
	
	@Transactional
	public void updateDownloadLog(DownloadLogModel downloadLogModel) {
		downloadDAO.save(downloadLogModel);
	}
	
}
