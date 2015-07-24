package io.github.darbycomputerclub;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.darbycomputerclub.message.response.Help;

/**
 * Retrieves the configuration property from config.properties.
 */
public class Configuration {
	
	/**
	 * This class should not be created as an object.
	 */
	protected Configuration() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws IOException
	 * Retrieves the authentication token from config.properties.
	 * @param property to retrieve
	 * @return property
	 */
	public static final String getConfig(final String property) 
			throws IOException {
		 
		InputStream inputStream = null;
		String value = null;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = new FileInputStream(new File(propFileName));
 
			prop.load(inputStream);
 
			// get the property value and print it out
			value = prop.getProperty(property);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return value;
	}
}
