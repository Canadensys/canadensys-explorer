package net.canadensys.dataportal.occurrence.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import net.canadensys.dataportal.occurrence.AbstractIntegrationTest;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Test links from previous Explorer versions that we still want to resolve.
 * @author cgendreau
 *
 */
public class LegacyPagesIntegrationTest extends AbstractIntegrationTest{
	
	//the footer div presence ensure the page was all rendered by Freemarker
	@FindBy(css = "div#footer")
	private WebElement footerDiv;
	
	@Test
	public void testLegacySearchPage() {
		browser.get(TESTING_SERVER_URL_LEGACY+"search?lang=fr");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		assertEquals(TESTING_SERVER_URL_LEGACY+"fr/rechercher",browser.getCurrentUrl());
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
		
	@Test
	public void testLegacyResourcePage() {
		browser.get(TESTING_SERVER_URL_LEGACY+"r/acad-specimens");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		//we can't easily test the response code so, use this 
		assertFalse(browser.getTitle().contains("404"));
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
	
	/**
	 * This URL should NOT be used since 'dataset' was used as 'resource'.
	 * The issue is that it was publish in our IPT as 'External links' so we 
	 * need to main it until we update all the resources.
	 */
	@Test
	public void testLegacyDatasetPage() {
		browser.get(TESTING_SERVER_URL_LEGACY+"d/acad-specimens");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		//we can't easily test the response code so, use this 
		assertFalse(browser.getTitle().contains("404"));
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}

}
