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
 * Retrieves the authentication token from config.properties.
 */
public class AuthToken {
	
	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Help.class);
	
	/**
	 * This class should not be created as an object.
	 */
	protected AuthToken() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws IOException
	 * Retrieves the authentication token from config.properties.
	 * @return authentication token
	 */
	public static final String getAuthToken() throws IOException {
		 
		InputStream inputStream = null;
		String authenticationToken = null;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = new FileInputStream(new File(propFileName));
 
			prop.load(inputStream);
 
			// get the property value and print it out
			authenticationToken = prop.getProperty("authenticationtoken");
			
			logger.info("Found config with property " + authenticationToken);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return authenticationToken;
	}
}
