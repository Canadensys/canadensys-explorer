package net.canadensys.dataportal.occurrence.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Testing the Occurrence controller routing and make sure URLs are working.
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class OccurrenceControllerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
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
		//make sure the tables are empty
		jdbcTemplate.update("DELETE FROM occurrence");
		jdbcTemplate.update("DELETE FROM occurrence_raw");
		jdbcTemplate.update("INSERT INTO occurrence_raw (auto_id,occurrenceid,country,locality,sourcefileid) VALUES (1,'2','Mexico','Mexico','uom-occurrence')");
		jdbcTemplate.update("INSERT INTO occurrence (auto_id,occurrenceid,country,locality,sourcefileid,syear,smonth,sday) VALUES (1,'2','Mexico','Mexico','uom-occurrence',2001,03,21)");
    }

    @Test
    public void testDatasetURL() throws Exception {
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/d/uom-occurrence");
    	//test default view
    	Object handler = handlerMapping.getHandler(request).getHandler();    
    	ModelAndView mav = handlerAdapter.handle(request, response, handler);
    	assertTrue(((RedirectView)mav.getView()).getUrl().contains("search?dataset=uom-occurrence"));
    }

    @Test
    public void testOccurrenURL() throws Exception {
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/d/uom-occurrence/2");
    	//test default view
    	Object handler = handlerMapping.getHandler(request).getHandler();    	
        ModelAndView mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"occurrence");
    }

}
