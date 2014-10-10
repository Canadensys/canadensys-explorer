package net.canadensys.dataportal.occurrence.controller.formatter;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Locale;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for FormatterUtility class
 * @author cgendreau
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
public class FormatterUtilityTest {
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;
	
	/**
	 * Test the behavior of buildRecommendedCitation method of FormatterUtility
	 */
	@Test
	public void testBuildRecommendedCitation(){
		String now = FormatterUtility.ISO_UTC_DATE_FORMAT.format(Calendar.getInstance().getTime());
		
		OccurrenceModel occModel = new OccurrenceModel();
		occModel.setCatalognumber("ABC-123");
		occModel.setDatasetname("MT");
		occModel.setInstitutioncode("Biodivertsity Centre");
		
		assertEquals("ABC-123 from MT at Biodivertsity Centre, http://data.canadensys.net/ipt (accessed on " + now + ")" ,
				FormatterUtility.buildRecommendedCitation(occModel, 
						"http://data.canadensys.net/ipt", appConfig.getResourceBundle(Locale.ENGLISH)));
		
		//missing Institutioncode
		occModel.setInstitutioncode(null);
		assertEquals("ABC-123 from MT, http://data.canadensys.net/ipt (accessed on " + now + ")" ,
				FormatterUtility.buildRecommendedCitation(occModel, 
						"http://data.canadensys.net/ipt", appConfig.getResourceBundle(Locale.ENGLISH)));
		
		//missing Datasetname
		occModel.setInstitutioncode("Biodivertsity Centre");
		occModel.setDatasetname(null);
		assertEquals("ABC-123, Biodivertsity Centre, http://data.canadensys.net/ipt (accessed on " + now + ")" ,
				FormatterUtility.buildRecommendedCitation(occModel, 
						"http://data.canadensys.net/ipt", appConfig.getResourceBundle(Locale.ENGLISH)));
	}

}
