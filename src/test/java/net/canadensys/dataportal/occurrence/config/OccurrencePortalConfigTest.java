package net.canadensys.dataportal.occurrence.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test configuration loading from the configuration file.
 * @author cgendreau
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
public class OccurrencePortalConfigTest {
	
	@Autowired
    private OccurrencePortalConfig occurrencePortalConfig;
	
	@Test
	public void testConfigLoading(){
		//ensure locales are loaded
		assertEquals(2,occurrencePortalConfig.getSupportedLocale().size());
		assertNotNull(occurrencePortalConfig.getResourceBundle(Locale.ENGLISH));
		
		assertEquals("cc0",occurrencePortalConfig.getLicenseShortName("http://creativecommons.org/publicdomain/zero/1.0/"));
	}
	
	@Test
	public void testGetDownloadEmailTemplateName(){
		assertEquals("download-email_en.ftl",occurrencePortalConfig.getDownloadEmailTemplateName(Locale.ENGLISH));
	}

}
