package net.canadensys.dataportal.occurrence.integration;

import static org.junit.Assert.assertEquals;
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
}
