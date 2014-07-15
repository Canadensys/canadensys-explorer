package net.canadensys.dataportal.occurrence.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig.SearchableFieldEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * The the caching strategy of the Search Service.
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-dispatcher-servlet.xml"})
public class CachingTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;
	
	@Autowired
	@Qualifier("searchServiceConfig")
	private SearchServiceConfig searchServiceConfig;
	
	@Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Before
    public void setup() {       
		//make sure the table is empty
		jdbcTemplate.update("DELETE FROM occurrence");
		jdbcTemplate.update("DELETE FROM occurrence_raw");
		jdbcTemplate.update("DELETE FROM resource_contact");
		
    	Resource resource = new ClassPathResource("/insert_test_data.sql");
    	JdbcTestUtils.executeSqlScript(jdbcTemplate, resource, true);
    }
    
    /**
     * Test distinctValuesCountCache
     */
    @Test
    public void testDistinctValuesCountCache(){
    	OccurrenceSearchService searchService = (OccurrenceSearchService)applicationContext.getBean("occurrenceSearchService");
    	Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
    
		SearchableField countrySearchableField = searchServiceConfig.getSearchableField(SearchableFieldEnum.COUNTRY);

		//count all different countries with empty searchCriteria 
    	Integer count = searchService.getDistinctValuesCount(searchCriteria, (OccurrenceSearchableField)countrySearchableField);
    	assertTrue(count.intValue() > 0);
    	
    	//extract value from cache
    	Cache cache = CacheManager.getCacheManager(CacheManager.DEFAULT_NAME).getCache("distinctValuesCountCache");
    	Integer countFromCache = (Integer)cache.get(countrySearchableField.getSearchableFieldId()).getObjectValue();
    	
    	assertEquals(count, countFromCache);
    }

}
