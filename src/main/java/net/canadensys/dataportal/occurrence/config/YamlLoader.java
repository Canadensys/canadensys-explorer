package net.canadensys.dataportal.occurrence.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

/**
 * Loader to instantiate objects from yaml file.
 * @author cgendreau
 *
 */
public class YamlLoader {
	
	private static final Logger LOGGER = Logger.getLogger(YamlLoader.class);
	
	/**
	 * Load an Object from Yaml file.
	 * @param resource path to file inside the classpath
	 * @return Object or null
	 */
	public static Object fromFile(String resource){
		Object loadedObject = null;
		InputStream ios = null;
		try {
			ClassPathResource cpr = new ClassPathResource( resource );
			ios = new FileInputStream(cpr.getFile());
			
			Yaml yaml = new Yaml();
			loadedObject = yaml.load(ios);
	        ios.close();
		}
		catch (FileNotFoundException e) {
			LOGGER.fatal("Cant load file "+resource, e);
		}
		catch (IOException e) {
			LOGGER.fatal("Cant load file "+resource, e);
		}
		finally{
			IOUtils.closeQuietly(ios);
		}
		return loadedObject;
	}
}
