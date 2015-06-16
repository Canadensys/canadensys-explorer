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
public abstract class AbstractFunctionalTest {
	
	//TODO : read from a config file or set with DI
	public static final String TESTING_SERVER_URL = "http://localhost:9966/explorer/en/";
	public static final String TESTING_SERVER_URL_FR = "http://localhost:9966/explorer/fr/";
	public static final String TESTING_SERVER_URL_LEGACY = "http://localhost:9966/explorer/";
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
