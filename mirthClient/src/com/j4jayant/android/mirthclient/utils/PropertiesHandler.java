package com.j4jayant.android.mirthclient.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesHandler {

	public static final String url_prop_name = "url";
	public static final String user_prop_name = "user";
	public static final String password_prop_name = "password";
	public static final String ep_prop_name = "endpoint";
	
	public static final String prop_file = "client.properties";
	
	 public static Map<String, String> getProperties() throws IOException {
			
		 	Map<String, String> propMap = new HashMap<String, String>();
		 
			Properties props = new Properties();
			
			try
			{
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				URL url = classLoader.getResource(prop_file);
	
				if (url != null) {
				
				// Load the properties using the URL (from the CLASSPATH).
	
					props.load(url.openStream());
				}
				
				propMap.put(url_prop_name,  props.getProperty(url_prop_name) );
				propMap.put(user_prop_name,  props.getProperty(user_prop_name) );
				propMap.put(password_prop_name,  props.getProperty(password_prop_name) );
				propMap.put(ep_prop_name,  props.getProperty(ep_prop_name) );

				return propMap;
			}
			catch(Exception ex)
			{
				throw new IOException(ex);
			}
		}

	 public static boolean setProperties(Map<String, String> propMap) throws IOException {
		 
			Properties props = new Properties();
			
			try
			{
				props.setProperty(url_prop_name, propMap.get(url_prop_name) );
				props.setProperty(user_prop_name, propMap.get(user_prop_name) );
				props.setProperty(password_prop_name, propMap.get(password_prop_name) );
				props.setProperty(ep_prop_name, propMap.get(ep_prop_name) );
				
				//save properties to project root folder
				props.store(new FileOutputStream(prop_file), null);
				return true;
			}
			catch(Exception ex)
			{
				return false;
			}
		}

}
