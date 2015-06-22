package net.canadensys.dataportal.occurrence.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.canadensys.dataportal.occurrence.OccurrenceService;
import net.canadensys.dataportal.occurrence.TestDataHelper;
import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.model.DwcaResourceModel;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchService;
import net.canadensys.dataportal.occurrence.search.OccurrenceSearchableField;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
    
    private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Before
    public void setup() {       
		TestDataHelper.loadTestData(applicationContext, jdbcTemplate);
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
    	Cache cache = CacheManager.getCacheManager(CacheManager.DEFAULT_NAME).getCache(CacheManagementServiceIF.DISTINCT_VALUES_COUNT_CACHE_KEY);
    	Integer countFromCache = (Integer)cache.get(countrySearchableField.getSearchableFieldId()).getObjectValue();
    	
    	assertEquals(count, countFromCache);
    }
    
    /**
     * Test distinctValuesCountCache
     */
    @Test
    public void testResourceModelCache(){
    	OccurrenceService occService = (OccurrenceService)applicationContext.getBean("occurrenceService");

    	DwcaResourceModel resourceModel = occService.loadDwcaResource(1);
    	assertNotNull(resourceModel);
    	
    	//extract value from cache
    	Cache cache = CacheManager.getCacheManager(CacheManager.DEFAULT_NAME).getCache(CacheManagementServiceIF.DWCA_RESOURCE_MODEL_CACHE_KEY);
    	DwcaResourceModel resourceModelFromCache = (DwcaResourceModel)cache.get(1).getObjectValue();
    	assertNotNull(resourceModelFromCache);
    	
    	assertEquals(resourceModel.getName(), resourceModelFromCache.getName());
    }
    
    /**
     * Test OccurrenceCountCache
     */
    @Test
    public void testOccurrenceCountCache(){
    	OccurrenceSearchService searchService = (OccurrenceSearchService)applicationContext.getBean("occurrenceSearchService");
    	Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
  
		//count all different countries with empty searchCriteria 
    	Integer count = searchService.getOccurrenceCount(searchCriteria);
    	assertTrue(count.intValue() > 0);
    	
    	//extract value from cache
    	Cache cache = CacheManager.getCacheManager(CacheManager.DEFAULT_NAME).getCache(CacheManagementServiceIF.OCCURRENCE_COUNT_CACHE_KEY);
    	//the key is the method name
    	Integer countFromCache = (Integer)cache.get("getOccurrenceCount").getObjectValue();
    	assertEquals(count, countFromCache);
    }

}
