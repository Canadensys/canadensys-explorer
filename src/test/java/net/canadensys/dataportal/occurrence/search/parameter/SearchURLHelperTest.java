package net.canadensys.dataportal.occurrence.search.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.canadensys.dataportal.occurrence.search.parameter.SearchURLHelper.ViewNameEnum;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * This test checks that we can use the SearchURLHelper to generate some URL.
 * @author canadensys
 *
 */
public class SearchURLHelperTest {
	
	@Test
	public void testSearchURLHandler(){
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		request.setServerName("canadensys.net");
    	request.setRequestURI("/search");
    	request.setQueryString("view=table");
    	assertTrue("search?view=table give "+SearchURLHelper.getViewURL(request, ViewNameEnum.MAP_VIEW_NAME),
    			"http://canadensys.net/search?view=map".equals(SearchURLHelper.getViewURL(request, ViewNameEnum.MAP_VIEW_NAME)));
    	
    	request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setServerName("canadensys.net");
    	request.setRequestURI("/search");
    	request.setQueryString("testparam=test");
    	assertTrue("search?testparam=test give "+SearchURLHelper.getViewURL(request,ViewNameEnum.MAP_VIEW_NAME),
    			"http://canadensys.net/search?testparam=test&view=map".equals(SearchURLHelper.getViewURL(request,ViewNameEnum.MAP_VIEW_NAME)));
    	
    	request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setServerName("canadensys.net");
    	request.setRequestURI("/search");
    	assertTrue("search give "+SearchURLHelper.getViewURL(request,ViewNameEnum.MAP_VIEW_NAME),
    			"http://canadensys.net/search?view=map".equals(SearchURLHelper.getViewURL(request,ViewNameEnum.MAP_VIEW_NAME)));
	}
	
	@Test
	public void testCreateSearchTableViewURL(){
		MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setServerName("data.canadensys.net");
    	request.setMethod("http");
    	request.setContextPath("/occurrence");
    	request.setRequestURI("/downloadresult");
    	request.addParameter("3_f", "1");
    	request.addParameter("3_o", "EQ");
    	request.addParameter("3_v_1", "Cuba");
    	request.addParameter("e", "toto@toto.to");
    	//should include http:// but the mock failed to include it
    	assertEquals("data.canadensys.net/occurrence/search?3_f=1&3_o=EQ&3_v_1=Cuba&view=table",
    			SearchURLHelper.createSearchTableViewURL(request.getServerName()+request.getContextPath(), request.getParameterMap()));
	}

}
