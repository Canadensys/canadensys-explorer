package net.canadensys.dataportal.occurrence;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.jdbc.JdbcTestUtils;

/**
 * Helper class to manipulate test data between tests.
 * 
 * @author cgendreau
 * 
 */
public class TestDataHelper {

	public static final String TEST_DATA_SCRIPT_LOCATION = "classpath:insert_test_data.sql";

	/**
	 * Delete and insert data from the insert-test-data.sql script.
	 * 
	 * @param appContext
	 * @param template
	 */
	public static void loadTestData(ApplicationContext appContext, JdbcTemplate jdbcTemplate) {
		Resource testDataScript = appContext.getResource(TEST_DATA_SCRIPT_LOCATION);

		try {
			JdbcTestUtils.deleteFromTables(jdbcTemplate, "occurrence", "occurrence_raw","occurrence_extension");
			JdbcTestUtils.deleteFromTables(jdbcTemplate, "contact");
			JdbcTestUtils.deleteFromTables(jdbcTemplate, "resource_metadata");
			JdbcTestUtils.deleteFromTables(jdbcTemplate, "dwca_resource");
			JdbcTestUtils.deleteFromTables(jdbcTemplate, "unique_values");
		
			//Get the same connection
			Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
			ScriptUtils.executeSqlScript(con, testDataScript);
		}
		catch (ScriptException e) {
			e.printStackTrace();
			fail();
		}
	}

}
