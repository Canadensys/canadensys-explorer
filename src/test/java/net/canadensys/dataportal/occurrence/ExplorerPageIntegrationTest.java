package net.canadensys.dataportal.occurrence;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Very simple and basic Integration test to make sure we can rendered some pages.
 * @author canadensys
 *
 */
public class ExplorerPageIntegrationTest extends AbstractIntegrationTest {
	
	//the footer div presence ensure the page was all rendered by Freemarker
	@FindBy(css = "div#footer")
	private WebElement footerDiv;
	
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

	@Test
	public void testCookie() {
		browser.get(TESTING_SERVER_URL+"search?view=table");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement display_button = browser.findElement(By.linkText("Display"));
		display_button.click();
		
		WebElement toggle_scientific = browser.findElement(By.id("toggle-col-0"));
		toggle_scientific.click();
		
		List<WebElement> column_headers = browser.findElements(By.cssSelector("table#results thead tr th"));
		assertFalse(column_headers.get(0).isDisplayed());

		browser.navigate().refresh();
		
		List<WebElement> column_headers_refreshed = browser.findElements(By.cssSelector("table#results thead tr th"));
		assertFalse(column_headers_refreshed.get(0).isDisplayed());
	}
}
