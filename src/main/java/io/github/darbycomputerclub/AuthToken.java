package io.github.darbycomputerclub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthToken {
	
	/**
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * Retrieves the authentication token from the config.properties file.
	 * @return authentication token
	 * @throws  
	 */
	public final String getAuthToken() throws IOException, FileNotFoundException {
		 
		InputStream inputStream = null;
		String authenticationToken = null;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader()
					.getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}
 
			// get the property value and print it out
			authenticationToken = prop.getProperty("user");
			
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return authenticationToken;
	}
}
