package net.canadensys.dataportal.occurrence.autocomplete;

import static org.junit.Assert.assertEquals;

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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Integration tests for the Auto-Complete module
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class AutoCompleteServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
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
    public void testAutoComplete() throws Exception {
    	
    	//test a auto-complete on all countries
    	MockHttpServletRequest request = createMockHttpServletRequest(1,null);
    	MockHttpServletResponse response =  new MockHttpServletResponse();
    	Object handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        assertEquals("{\"total_rows\":\"3\",\"rows\":[{\"id\":3,\"key\":\"country\",\"occurrence_count\":125,\"value\":\"Canada\",\"unaccented_value\":\"canada\"},{\"id\":4,\"key\":\"country\",\"occurrence_count\":17,\"value\":\"China\",\"unaccented_value\":\"china\"},{\"id\":5,\"key\":\"country\",\"occurrence_count\":1,\"value\":\"Réunion\",\"unaccented_value\":\"reunion\"}]}",
        		response.getContentAsString());
    	
    	//test a  auto-complete on the letter "c" for the field country
    	request = createMockHttpServletRequest(1,"c");
    	response =  new MockHttpServletResponse();
    	handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        assertEquals("{\"total_rows\":\"2\",\"rows\":[{\"id\":3,\"key\":\"country\",\"occurrence_count\":125,\"value\":\"Canada\",\"unaccented_value\":\"canada\"},{\"id\":4,\"key\":\"country\",\"occurrence_count\":17,\"value\":\"China\",\"unaccented_value\":\"china\"}]}",
        		response.getContentAsString());
        
    	//test a  auto-complete on a country with an accent and make sure it's case insensitive
    	request = createMockHttpServletRequest(1,"RE");
    	response =  new MockHttpServletResponse();
    	handler = handlerMapping.getHandler(request).getHandler();
        handlerAdapter.handle(request, response, handler);
        assertEquals("{\"total_rows\":\"1\",\"rows\":[{\"id\":5,\"key\":\"country\",\"occurrence_count\":1,\"value\":\"Réunion\",\"unaccented_value\":\"reunion\"}]}",response.getContentAsString());
    }
    
    private MockHttpServletRequest createMockHttpServletRequest(Integer fieldId, String currValue){
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/ws/livesearch");
    	request.addParameter("fieldId", fieldId.toString());
    	if(currValue != null){
    		request.addParameter("curr", currValue);
    	}
    	return request;
    }
}
