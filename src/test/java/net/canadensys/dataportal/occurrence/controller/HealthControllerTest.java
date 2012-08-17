package net.canadensys.dataportal.occurrence.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * This test checks that the health URL is available
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
public class HealthControllerTest {

	@Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    
    @Test
    public void testHealth() throws Exception {
    	//Test http GET
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	request.setMethod("GET");
    	request.setRequestURI("/health");
    	
    	Object handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertTrue(response.getContentAsString().contains("OK"));
        
        //Test http HEAD
    	request = new MockHttpServletRequest();
    	response = new MockHttpServletResponse();
    	request.setMethod("HEAD");
    	request.setRequestURI("/health");
    	
    	handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertEquals(0,response.getContentLength());
    }
}
