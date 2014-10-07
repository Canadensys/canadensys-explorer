package net.canadensys.dataportal.occurrence.controller.formatter;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;

import org.apache.commons.lang3.StringUtils;

/**
 * Formatter utility class until we have enough formatters to create more cohesive classes.
 * @author cgendreau
 *
 */
public class FormatterUtility {
	
	public static final DateFormat ISO_UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'GMT'Z");

	
	public static final String CITATION_DATE_REGEX =  "[\\[{(]date[\\])}]";
	
	/**
	 * Build the Recommended Citation based on an OccurrenceModel
	 * As defined by https://github.com/Canadensys/canadensys-explorer/issues/28
	 * @param occModel
	 * @param urlToResource
	 * @param bundle
	 * @return
	 */
	public static String buildRecommendedCitation(OccurrenceModel occModel, String urlToResource, ResourceBundle bundle){
		String now = ISO_UTC_DATE_FORMAT.format(Calendar.getInstance().getTime());
		
		//if Bibliographiccitation is provided simply check if we have a 'date' variable to replace
		if(StringUtils.isNotBlank(occModel.getBibliographiccitation())){
			return StringUtils.replacePattern(occModel.getBibliographiccitation(), CITATION_DATE_REGEX, now);
		}
	
		String catalogNumber = occModel.getCatalognumber();
		String datasetName = occModel.getDatasetname();
		String institutionCode = occModel.getInstitutioncode();
		
		if(StringUtils.isNoneBlank(catalogNumber,datasetName,institutionCode)){
			return MessageFormat.format(bundle.getString("occpage.recommendedcitation.complete"), catalogNumber, datasetName, institutionCode, urlToResource, now);
		}
		
		if(StringUtils.isNoneBlank(catalogNumber,datasetName)){
			return MessageFormat.format(bundle.getString("occpage.recommendedcitation.missingic"), catalogNumber, datasetName, urlToResource, now);
		}
		
		if(StringUtils.isNoneBlank(catalogNumber,institutionCode)){
			return MessageFormat.format(bundle.getString("occpage.recommendedcitation.missingdn"), catalogNumber, institutionCode, urlToResource, now);
		}
		
		return "";
	}

}
