package net.canadensys.dataportal.occurrence.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceViewModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import org.springframework.web.servlet.view.RedirectView;

/**
 * Testing the Occurrence controller routing and make sure URLs are working.
 * TODO move to new testing framework
 * @WebAppConfiguration()
 * 
 *  @Autowired
 *  private WebApplicationContext wac;
 *  @Before
 *   public void setup() {
 *   mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
 *   .build();
 *    }
 *     @Test
 *      public void test404HttpErrorPage() throws Exception {
 *        mockMvc.perform(get("/not_found"))
 *                .andExpect(status().isNotFound())
 *                          .andExpect(content().string("handleNotFound"));
 *      }
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
public class OccurrenceControllerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    
    @Autowired
    private OccurrenceController occurrenceController;
     
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
		jdbcTemplate.update("DELETE FROM resource_contact");
		jdbcTemplate.update("INSERT INTO occurrence_raw (auto_id,dwcaId,country,locality,datasetname,sourcefileid) VALUES (1,'2','Mexico','Mexico','University of Mexico (UOM)','uom-occurrence')");
		jdbcTemplate.update("INSERT INTO occurrence_raw (auto_id,dwcaId,country,locality,datasetname,sourcefileid) VALUES (2,'2.2','Mexico','Mexico','University of Mexico (UOM)','uom-occurrence')");
		jdbcTemplate.update("INSERT INTO occurrence (auto_id,dwcaId,country,locality,datasetname,sourcefileid,syear,smonth,sday) VALUES (1,'2','Mexico','Mexico','University of Mexico (UOM)','uom-occurrence',2001,03,21)");
		jdbcTemplate.update("INSERT INTO occurrence (auto_id,dwcaId,country,locality,datasetname,sourcefileid,syear,smonth,sday) VALUES (2,'2.2','Mexico','Mexico','University of Mexico (UOM)','uom-occurrence',2001,03,21)");
		jdbcTemplate.update("INSERT INTO resource_contact (id,sourcefileid,name) VALUES (1,'uom-occurrence','Jim')");
    }

    @Test
    public void testIptResourceURL() throws Exception {
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/resources/uom-occurrence");
    	//test default view
    	Object handler = handlerMapping.getHandler(request).getHandler();    
    	ModelAndView mav = handlerAdapter.handle(request, response, handler);
    	assertTrue(((RedirectView)mav.getView()).getUrl().contains("search?iptresource=uom-occurrence"));
    }

    @Test
    public void testOccurrenceURL() throws Exception {
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/resources/uom-occurrence/occurrences/2");
    	//test default view
    	Object handler = handlerMapping.getHandler(request).getHandler();    	
        ModelAndView mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"occurrence");
        
        //using a dot in dwcaid
        response = new MockHttpServletResponse();
    	request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/resources/uom-occurrence/occurrences/2.2");
    	//test default view
    	handler = handlerMapping.getHandler(request).getHandler();    	
        mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"occurrence");
        
        HashMap<String,Object> modelRoot = (HashMap<String,Object>)mav.getModel().get(OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY);
        OccurrenceModel occModel = (OccurrenceModel)modelRoot.get("occModel");
        assertEquals("2.2", occModel.getDwcaid());
    }
    
    @Test
    public void testOccurrenceContactURL() throws Exception {
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setMethod("GET");
    	request.setRequestURI("/resources/uom-occurrence/contact");
    	Object handler = handlerMapping.getHandler(request).getHandler();    	
        ModelAndView mav = handlerAdapter.handle(request, response, handler);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertViewName(mav,"resource-contact");
    }
    
    /**
     * Test the model used for display purpose.
     * associatedSequences urls format are defined in src/main/resources/references/sequenceProviders.properties
     */
    @Test
    public void buildOccurrenceViewModel(){
    	OccurrenceModel occModel = new OccurrenceModel();
    	occModel.setAssociatedsequences("BOLD :1234|bold: 2345|unknown:3456");
    	
    	OccurrenceViewModel occViewModel = occurrenceController.buildOccurrenceViewModel(occModel,null,null);
    	
    	Map<String,List<Pair<String,String>>> associatedSequencesPerProviderMap = occViewModel.getAssociatedSequencesPerProviderMap();
    	
    	assertEquals(2,associatedSequencesPerProviderMap.keySet().size());
    	
    	Iterator<String> keyIt = associatedSequencesPerProviderMap.keySet().iterator();
    	String firstKey = keyIt.next();
    	assertEquals(2,associatedSequencesPerProviderMap.get(firstKey).size());
    	
    	//ensure we have urls for known provider
    	assertTrue(StringUtils.isNotBlank(associatedSequencesPerProviderMap.get(firstKey).get(0).getValue()));
    	assertTrue(StringUtils.isNotBlank(associatedSequencesPerProviderMap.get(firstKey).get(1).getValue()));
    	
    	//ensure we do not have urls for unknown provider
    	String secondKey = keyIt.next();
    	assertTrue(StringUtils.isBlank(associatedSequencesPerProviderMap.get(secondKey).get(0).getValue()));
    }
    
}
