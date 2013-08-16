package net.canadensys.dataportal.occurrence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Very simple and basic Integration test to make sure we can rendered a page.
 * Needs to be expanded when test data will be included in the test suite.
 * @author canadensys
 *
 */
public class ExplorerPageIntegrationTest extends AbstractIntegrationTest {
	
	@FindBy(css = "div#footer")
	private WebElement footerDiv;
	
	@Before
	public void setup() {
		browser = new FirefoxDriver();
	}
	@After
	public void tearDown() {
		browser.close();
	}
	
	@Test
	public void testTablePage() {
		browser.get(TESTING_SERVER_URL+"search?view=table");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement resultTable = browser.findElement(By.cssSelector("table#results"));
		
		assertEquals("table",resultTable.getTagName());
		
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName());
	}
	
	@Test
	public void testErrorPage() {
		browser.get(TESTING_SERVER_URL+"searchh");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement h1 = browser.findElement(By.cssSelector("h1"));
		assertTrue(h1.getText().contains("404"));
		
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName());
	}
	
	@Test
	public void testFrenchPageEncoding() {
		browser.get(TESTING_SERVER_URL+"search?view=table&lang=fr");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement search_h3 = browser.findElement(By.cssSelector("#search h3"));
		assertEquals("Créer un nouveau filtre",search_h3.getText());
		
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName());
	}
}
