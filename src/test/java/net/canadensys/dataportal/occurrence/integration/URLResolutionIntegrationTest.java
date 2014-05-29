package net.canadensys.dataportal.occurrence.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.canadensys.dataportal.occurrence.AbstractIntegrationTest;

import org.junit.Test;

/**
 * Test URL resolving only, this class only look at response code and won't render the page.
 * @author cgendreau
 *
 */
public class URLResolutionIntegrationTest {
		
	@Test
	public void testURLThatShouldNotResolve() {
		//wrong URL with no language specified
		assertResponseCode(AbstractIntegrationTest.TESTING_SERVER_URL_LEGACY+"toto",404);
		//wrong URL with language specified
		assertResponseCode(AbstractIntegrationTest.TESTING_SERVER_URL+"toto",404);
		//conflicted language URL
		assertResponseCode(AbstractIntegrationTest.TESTING_SERVER_URL_LEGACY+"fr/search?view=table",404);
	}
	
	
	@Test
	public void testResolveLegacyURL() {
		assertResponseCode(AbstractIntegrationTest.TESTING_SERVER_URL_LEGACY+"r/acad-specimens",200);
		
		//This URL should NOT be used since 'dataset' was used as 'resource'.
		//The issue is that it was publish in our IPT as 'External links' so we 
		//need to main it until we update all the resources.
		assertResponseCode(AbstractIntegrationTest.TESTING_SERVER_URL_LEGACY+"d/acad-specimens",200);
	}
	
	/**
	 * Inner helper to assert the response code of a URL
	 * @param urlStr
	 * @param code
	 */
	private void assertResponseCode(String urlStr, int code){
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			assertEquals(code,connection.getResponseCode());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
