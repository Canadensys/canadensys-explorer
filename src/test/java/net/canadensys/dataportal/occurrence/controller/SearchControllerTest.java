package net.canadensys.dataportal.occurrence.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.canadensys.dataportal.occurrence.TestDataHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Testing the Search controller routing and make sure URLs are working.
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
public class SearchControllerTest extends AbstractTransactionalJUnit4SpringContextTests{
		
	@Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    
    private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
    @Before
    public void setup() {
    	TestDataHelper.loadTestData(applicationContext, jdbcTemplate);
    }

    @Test
    public void testSearch() throws Exception {
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/search");
    	//test default view
    	Object handler = handlerMapping.getHandler(request).getHandler();    	
        ModelAndView mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"view-map");
        
        //test table view
    	request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/search");
    	request.addParameter("view", "table");
    	handler = handlerMapping.getHandler(request).getHandler();    	
        mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"view-table");
        
        //test stats view
    	request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/search");
    	request.addParameter("view", "stats");
    	handler = handlerMapping.getHandler(request).getHandler();    	
        mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"view-stats");
    }
    
    @Test
    public void testLiveSearch() throws Exception {
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/ws/livesearch");
    	request.addParameter("fieldId", "1");
    	request.addParameter("curr", "c");
    	
    	Object handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
    	assertTrue(response.getContentAsString().contains("\"occurrence_count\":125"));
    }
    
    /**
     * Possible values means all possible values so the searchable field must be configured that way.
     * @throws Exception
     */
    @Test
    public void testGetPossiblevalues() throws Exception{
    	//need to take a Searchable Field that support this feature (getAllPossibleValues)
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	request.setMethod("GET");
    	request.setRequestURI("/ws/getpossiblevalues");
    	request.addParameter("fieldId", "8");
    	Object handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        //We do not validate the id because it's a sequence
        assertTrue(response.getContentAsString().contains(",\"key\":\"datasetname\",\"occurrence_count\":40,\"value\":\"Cool-Specimens\",\"unaccented_value\":\"cool-speciens\"}"));
        assertTrue(response.getContentAsString().contains(",\"key\":\"datasetname\",\"occurrence_count\":55,\"value\":\"Cooler-Specimens\",\"unaccented_value\":\"cooler-specimens\"}"));
        assertTrue(response.getContentAsString().contains(",\"key\":\"datasetname\",\"occurrence_count\":3,\"value\":\"My-Specimens\",\"unaccented_value\":\"my-speciens\"}"));
    }
    
    @Test
    public void testStatisticsURL() throws Exception{
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	//Count the number of different countries
    	request.setMethod("GET");
    	request.setRequestURI("/ws/stats/unique/1");
    	Object handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        assertEquals("{\"count\":1}",response.getContentAsString());
        
        //Count the different countries
        request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/ws/stats/chart/1");
    	handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        assertEquals("{\"count\":1}{\"rows\":[[\"Canada\",6]],\"rowCount\":1}",response.getContentAsString());
    }
}
