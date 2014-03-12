package net.canadensys.dataportal.occurrence.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.canadensys.dataportal.occurrence.AbstractIntegrationTest;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
	public void selectAndAddFilter() {
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
		
		//validate current filter
		assertTrue(isCurrentFilterCategory(browser.findElement(By.id("search")),"Country"));
		
		//make sure footer is there
		assertEquals("div",footerDiv.getTagName());
	}
	
	/**
	 * Get a WebElement list representing the current filter under a specific parent.
	 * @param parent must contains the <form> element
	 * @return
	 */
	public static List<WebElement> getCurrentFilterElements(WebElement parent){
		WebElement formContent = parent.findElement(By.tagName("form"));
		return formContent.findElements(By.cssSelector("ul#filter_current li.filter"));
	}
	
	/**
	 * Check if a filter category is present under a specific parent.
	 * xpath is used since getText() will also returns the sub-elements and partialLinkText() may give
	 * false positive.
	 * @param parent must contains the <form> element
	 * @param filterCategory a filter category is like Country, Kingdom
	 * @return
	 */
	public static boolean isCurrentFilterCategory(WebElement parent, String filterCategory){
		WebElement formContent = parent.findElement(By.tagName("form"));
		try{
			formContent.findElement(By.xpath("ul/li[text() = '" + filterCategory + "']"));
		}
		catch(NoSuchElementException nseEx){
			return false;
		}
		return true;
	}
}
