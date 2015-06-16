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
public class OccurrencePageFunctionalTest extends AbstractFunctionalTest {
	//the footer div presence ensure the page was all rendered by Freemarker
	@FindBy(css = "div#footer")
	private WebElement footerDiv;
		
	@Test
	public void testOccurrencePage() {
		browser.get(TESTING_SERVER_URL+"resources/acad-specimens/occurrences/ACAD-1");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement h1 = browser.findElement(By.cssSelector("h1"));
		assertEquals("Acer pseudoplatanus", h1.getText());
		
		//make sure data coming from the extension are included
		WebElement imageDiv =  browser.findElement(By.cssSelector("div#occpage_image"));
		WebElement attributionElement = imageDiv.findElement(By.cssSelector("div.attribution a"));
		assertEquals("http://creativecommons.org/licenses/by/4.0/", attributionElement.getAttribute("href"));
		
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
	
	@Test
	public void testOccurrencePageWithSpaceChar() {
		browser.get(TESTING_SERVER_URL+"resources/acad-specimens/occurrences/ACAD%203");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement h1 = browser.findElement(By.cssSelector("h1"));
		assertEquals("Myosotis arvensis", h1.getText());
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
	
	@Test
	public void testOccurrencePageDwcView() {
		browser.get(TESTING_SERVER_URL+"resources/acad-specimens/occurrences/ACAD-1?view=dwc");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		WebElement h1 = browser.findElement(By.cssSelector("h1"));
		assertEquals("Acer pseudoplatanus", h1.getText());
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName().toLowerCase());
	}
}
