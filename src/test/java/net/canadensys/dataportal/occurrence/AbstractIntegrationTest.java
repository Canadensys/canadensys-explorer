package net.canadensys.dataportal.occurrence;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Abstract integration testing class.
 * 
 * @author canadensys
 *
 */
public abstract class AbstractIntegrationTest {
	
	//TODO : read from a config file or set with DI
	protected static final String TESTING_SERVER_URL = "http://localhost:9966/explorer/";
	protected WebDriver browser;

	@Before
	public void setup() {
		browser = new FirefoxDriver();
	}
	@After
	public void tearDown() {
		browser.close();
	}
}
