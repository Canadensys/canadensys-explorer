package net.canadensys.dataportal.occurrence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Ensure Occurrence page can be rendered correctly
 * @author canadensys
 *
 */
public class OccurrencePageIntegrationTest extends AbstractIntegrationTest {
	//the footer div presence ensure the page was all rendered by Freemarker
	@FindBy(css = "div#footer")
	private WebElement footerDiv;
		
	@Test
	public void testOccurrencePage() {
		browser.get(TESTING_SERVER_URL+"r/acad-specimens/ACAD-1");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement h1 = browser.findElement(By.cssSelector("h1"));
		assertEquals("Acer pseudoplatanus (ACAD ECS019597)", h1.getText());
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
	
	@Test
	public void testOccurrencePageDwcView() {
		browser.get(TESTING_SERVER_URL+"r/acad-specimens/ACAD-1?view=dwc");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement h1 = browser.findElement(By.cssSelector("h1"));
		assertEquals("Acer pseudoplatanus (ACAD ECS019597)", h1.getText());
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
	
	@Test
	public void testOccurrenceContactPage() {
		browser.get(TESTING_SERVER_URL+"r/acad-specimens/contact");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement h1 = browser.findElement(By.cssSelector("h1"));
		assertEquals("E. C. Smith Herbarium (ACAD)", h1.getText());
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
}
