package net.canadensys.dataportal.occurrence;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

/**
 * Test filter selections and behavior.
 * @author cgendreau
 *
 */
public class FilterIntegrationTest extends AbstractIntegrationTest{
	
	//the footer div presence ensure the page was all rendered by Freemarker
	@FindBy(css = "div#footer")
	private WebElement footerDiv;
		
	@Test
	public void testOccurrencePage() {
		browser.get(TESTING_SERVER_URL+"search?view=table");
		
		//bind the WebElement to the current page
		PageFactory.initElements(browser, this);
		
		Select dropdown = new Select(browser.findElement(By.id("key_select")));
		dropdown.selectByVisibleText("Country");

		WebElement filterContent = browser.findElement(By.id("filter_content"));
		WebElement filterInput = filterContent.findElement(By.tagName("input"));
		filterInput.sendKeys("canad");
		
		List<WebElement> suggestionList = filterContent.findElements(By.cssSelector("table#value_suggestions tbody tr td"));
		suggestionList.get(0).click();
		
		//TODO validate table content
		
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName());
	}

}
