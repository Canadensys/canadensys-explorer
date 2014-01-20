/*
	Copyright (c) 2012 Canadensys
*/
package net.canadensys.dataportal.occurrence.search.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import net.canadensys.chart.ChartModel;
import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.dao.OccurrenceDAO;
import net.canadensys.dataportal.occurrence.map.MapServerAccess;
import net.canadensys.dataportal.occurrence.model.DownloadLogModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.search.DownloadResultStatus;
import net.canadensys.dataportal.occurrence.search.DownloadResultStatus.DownloadResultMode;
import net.canadensys.dataportal.occurrence.search.DwcaBuilder;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.mail.TemplateMailSender;
import net.canadensys.query.LimitedResult;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableFieldTypeEnum;
import net.canadensys.utils.NumberUtils;
import net.canadensys.utils.ZipUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the Search service layer
 * @author canadensys
 */
@Service("occurrenceSearchService")
public class OccurrenceSearchServiceImpl implements OccurrenceSearchService {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(OccurrenceSearchServiceImpl.class);
	private static final Map<Locale,String> DOWNLOAD_EMAIL_TEMPLATE_PER_LOCALE = new HashMap<Locale,String>();
	private static final String EMAIL_DATE_FORMAT = "EEEE, yyyy-MM-dd HH:mm:ss z";
	private static final int MAX_STATS_PER_FIELD = 10;
	
	static{
		DOWNLOAD_EMAIL_TEMPLATE_PER_LOCALE.put(Locale.ENGLISH, "download-email-en.ftl");
		DOWNLOAD_EMAIL_TEMPLATE_PER_LOCALE.put(Locale.FRENCH, "download-email-fr.ftl");
	}
	
	private List<String> DWCA_HEADERS;
	
	@Autowired(required=true)
	private OccurrenceDAO occurrenceDAO;
	
	@Autowired
	@Qualifier("mapServerAccess")
	private MapServerAccess mapServerAccess;
	
	@Autowired
	@Qualifier("searchServiceConfig")
	private SearchServiceConfig searchServiceConfig;
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;
	
	@Autowired
	@Qualifier("DwcaBuilder")
	private DwcaBuilder dwcaBuilder; //used for async DwcA build
	
	@Autowired
	@Qualifier("executor")
	private TaskExecutor taskExecutor;
	
	@Autowired
	@Qualifier("templateMailSender")
	private TemplateMailSender mailSender;
	
	@PostConstruct
	protected void init(){
		DWCA_HEADERS = Arrays.asList(appConfig.getDwcaTermUsed().split(","));
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<OccurrenceModel> search(HashMap<String, List<String>> searchCriteria) {
		return occurrenceDAO.search(searchCriteria);
	}
	
	/**
	 * @return LimitedResult with rows as list of field:value map
	 */
	@Override
	@Transactional(readOnly=true)
	public LimitedResult<List<Map<String, String>>> searchWithLimit(Map<String, List<SearchQueryPart>> searchCriteria) {
		//if the searchCriteria is empty, the first n records are returned
		return occurrenceDAO.searchWithLimit(searchCriteria, SearchServiceConfig.OCCURRENCE_SEARCH_FIELDS);
	}
	
	@Override
	@Transactional(readOnly=true)
	public String getOccurrenceSummary(int auto_id) {
		return occurrenceDAO.getOccurrenceSummaryJson(auto_id,OccurrencePortalConfig.OCCURRENCE_MANAGED_ID_FIELD,SearchServiceConfig.OCCURENCE_SUMMARY_FIELDS);
	}
	
	@Override
	@Transactional(readOnly=true)
	public int getOccurrenceCount(Map<String, List<SearchQueryPart>> searchCriteria){
		return occurrenceDAO.getOccurrenceCount(searchCriteria);
	}
	
	@Override
	public SearchQueryPart getSearchQueryPartFromFieldName(String fieldName, String fieldValue){
		OccurrenceSearchableField searchField = (OccurrenceSearchableField)searchServiceConfig.getSearchableFieldbyName(fieldName);
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(searchField);
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue(fieldValue);
		sqp.addParsedValue(fieldValue, searchField.getRelatedField(), fieldValue);
		return sqp;
	}
	
	@Override
	@Transactional(readOnly=true)
	public int getGeoreferencedOccurrenceCount(Map<String, List<SearchQueryPart>> searchCriteria){
		return mapServerAccess.getGeoreferencedRecordCount(searchCriteria);
	}
	
	@Override
	public String getMapQuery(Map<String, List<SearchQueryPart>> searchCriteria) {
		return mapServerAccess.getMapQuery(searchCriteria);
	}
	
	@Override
	@Transactional(readOnly=true)
	public String[] getMapCenter(String sqlQuery){
		return mapServerAccess.getMapCenter(sqlQuery);
	}
	
//	/**
//	 * @deprecated This was used when the id used on the map were not the same as the
//	 * id used in the table with all the data. Could it be useful in the future?
//	 */
//	@Override
//	@Deprecated
//	public String getMapOccurrenceSummary(int id){
//		return mapServerAccess.getMapElementDetails(id,SearchServiceConfig.OCCURENCE_SUMMARY_FIELDS);
//	}

	@Override
	@Transactional(readOnly=true)
	public DownloadResultStatus searchAsDwca(Map<String, List<SearchQueryPart>> searchCriteria, String fileName, Map<DownloadPropertiesEnum,Object> extraProperties) {
		DownloadResultStatus result = new DownloadResultStatus();
		String email = (String)extraProperties.get(DownloadPropertiesEnum.EMAIL);
		if(!StringUtils.isBlank(email)){
			getResultAsDwcaAsync(searchCriteria,fileName,extraProperties);
			result.setMode(DownloadResultMode.ASYNC);
		}
		else{
			LOGGER.warn("email was null for async DarwinCore archive generation");
			result.setMode(DownloadResultMode.ERROR);
		}
		return result;
	}
	
	@Override
	public File getResultAsDwca(Map<String, List<SearchQueryPart>> searchCriteria, String fileName) {
		//not implemented
		return null;
	}
	
	/**
	 * This method will generate the DarwinCore archive and send an email containing the link to download it.
	 */
	@Override
	public void getResultAsDwcaAsync(final Map<String, List<SearchQueryPart>> searchCriteria, final String fileName,final Map<DownloadPropertiesEnum,Object> extraProperties) {
		//run this job in another Thread (via TaskExecutor)
		taskExecutor.execute(new Runnable() {
			final private Date now = new Date();
			@Override
			public void run() {
				
				String fullPath = FilenameUtils.concat(searchServiceConfig.getGeneratedContentFolder(), fileName);
				String fullFilePath = fullPath + ZipUtils.ZIP_EXT;
				String email = (String)extraProperties.get(DownloadPropertiesEnum.EMAIL);
				//log the download model prior the dwca generation
				DownloadLogModel downloadLogModel = dwcaBuilder.logDownload(email,searchCriteria.toString());
				
				File dwcaFile = dwcaBuilder.generatesDarwinCoreArchive(DWCA_HEADERS,searchCriteria, fullPath, fullFilePath);
				if(dwcaFile != null && dwcaFile.exists()){					
					//send email
					String fileURL = searchServiceConfig.getPublicDownloadURL() + dwcaFile.getName();
					Locale locale = (Locale)extraProperties.get(DownloadPropertiesEnum.LOCALE);
					ResourceBundle bundle = appConfig.getResourceBundle(locale);
					
					Map<String,Object> templateData = new HashMap<String,Object>();
					templateData.put("dwcaLink", fileURL);
					templateData.put("institutionCodeList", StringEscapeUtils.escapeHtml4(StringUtils.join(dwcaBuilder.getDistinctInstitutionCode(searchCriteria),", ")));
					templateData.put("requestTimestamp", now);
					templateData.put("requestTimestampText", new SimpleDateFormat(EMAIL_DATE_FORMAT, locale).format(now));
					templateData.put("requestURL", extraProperties.get(DownloadPropertiesEnum.SEARCH_URL));
					
					String templateName = DOWNLOAD_EMAIL_TEMPLATE_PER_LOCALE.get(locale);
					if(!mailSender.sendMessage(email, bundle.getString("download.dwca.email.subject"), templateData, templateName)){
						LOGGER.fatal("Supposed to send DarwinCore archive by email to " + email + " but it failed");
					}
					else{
						int occurrenceCount = dwcaBuilder.getOccurrenceCount(searchCriteria);
						downloadLogModel.setNumber_of_records(occurrenceCount);
						//complete the record when the mail is sent
						dwcaBuilder.updateDownloadLog(downloadLogModel);
					}
				}
				else{
					LOGGER.fatal("Supposed to send DarwinCore archive by email to " + email + " but it failed");
				}
			}
		});
	}
	

	/**
	 * Get all values and their count for a specific searchCriteria/column.
	 * @param searchCriteria
	 * @param column
	 * @param extraProperties StatsPropertiesEnum.RESOURCE_BUNDLE, StatsPropertiesEnum.MAX_RESULT
	 * @return 
	 */
	@Override
	@Transactional(readOnly=true)
	public ChartModel getValuesFrequencyDistribution(Map<String, List<SearchQueryPart>> searchCriteria, OccurrenceSearchableField column, Map<StatsPropertiesEnum,Object> extraProperties){
		ResourceBundle resourceBundle = (ResourceBundle)extraProperties.get(StatsPropertiesEnum.RESOURCE_BUNDLE);
		Integer maxResult = (Integer)extraProperties.get(StatsPropertiesEnum.MAX_RESULT);
		
		//compute the total before we add the non empty criteria
		int total = occurrenceDAO.getOccurrenceCount(searchCriteria);
		
		addNonEmptyCriteria(column, searchCriteria);
		List<SimpleImmutableEntry<String,Integer>> results = occurrenceDAO.getValueCount(searchCriteria, column.getRelatedField(), maxResult);
		
		int currCount = 0;
		ChartModel cm = new ChartModel();
		
		for(SimpleImmutableEntry<String,Integer> currRow : results){
			Object[] values = new Object[2];
			if(!StringUtils.isBlank(currRow.getKey())){
				if(column.getType().equals(String.class)){
					values[0] = currRow.getKey();
				}
				else{
					values[0] = NumberUtils.parseNumber(currRow.getKey(), column.getType());
					if(values[0] == null){
						LOGGER.fatal("Unparsable column type");
					}
				}
				values[1] = currRow.getValue();
				cm.addRow(values);
				currCount += currRow.getValue();
			}
			else{
				LOGGER.error("Empty value found! Column:"+currRow.getKey());
			}
		}
		
		//create an other/no value row (if necessary)
		if(currCount != total){
			Object[] values = new Object[2];
			//if the number of row is lower than maxResult, it means that the remaining rows are null or empty.
			if(maxResult != null && cm.getRowCount() < maxResult){
				values[0] = resourceBundle.getString("view.stats.novalue");
			}
			else{// here, the remaining rows contain other value including null or empty value
				values[0] = resourceBundle.getString("view.stats.other");
			}
			values[1] = (total - currCount);
			cm.addRow(values);
		}
		return cm;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer getDistinctValuesCount(Map<String, List<SearchQueryPart>> searchCriteria, OccurrenceSearchableField column){
		addNonEmptyCriteria(column, searchCriteria);
		return occurrenceDAO.getCountDistinct(searchCriteria,column.getRelatedField());
	}
	
	/**
	 * This function will add a SearchQueryPart representing a non-empty value.
	 * The column must not already be in the searchCriteria
	 * @param column
	 * @param searchCriteria
	 */
	private void addNonEmptyCriteria(OccurrenceSearchableField column, Map<String, List<SearchQueryPart>> searchCriteria){
		if(column.getType() == String.class && column.getSearchableFieldTypeEnum() == SearchableFieldTypeEnum.SINGLE_VALUE
				&& searchCriteria.get(column.getRelatedField()) == null){
			SearchQueryPart sqp = new SearchQueryPart();
			sqp.addValue("");
			sqp.setSearchableField(column);
			sqp.addParsedValue("", column.getRelatedField(), "");
			sqp.setOp(QueryOperatorEnum.NEQ);			
			List<SearchQueryPart> sqpList = new ArrayList<SearchQueryPart>();
			sqpList.add(sqp);
			searchCriteria.put(column.getRelatedField(), sqpList);
		}
	}
}
