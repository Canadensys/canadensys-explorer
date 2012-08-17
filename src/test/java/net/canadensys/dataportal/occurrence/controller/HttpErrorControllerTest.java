package net.canadensys.dataportal.occurrence.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.ModelAndViewAssert.*;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * This test ensures the mapping for the 404 page is correct.
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
public class HttpErrorControllerTest {
	
	@Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    
    @Test
    public void test404HttpErrorPage() throws Exception {
    	//Test http GET
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	request.setMethod("GET");
    	request.setRequestURI("/errors/404.html");
    	
    	Object handler = handlerMapping.getHandler(request).getHandler();
    	ModelAndView mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"error/404");
    }
}
