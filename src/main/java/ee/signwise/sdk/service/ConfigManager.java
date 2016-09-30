/*
Copyright 2016 SignWise Corporation Ltd.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ee.signwise.sdk.service;
import java.util.Map;
import java.util.Properties;
import java.util.Hashtable;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;



public class ConfigManager {
	/** Resource bundle */
    private static Properties m_props = null;
    /** singleton instance */
    private static ConfigManager m_instance = null;
    /** logger */
    private static Logger m_logger = Logger.getLogger(ConfigManager.class);
    
    /**
     * Singleton accessor
     */
    public static ConfigManager instance() {
        if(m_instance == null)
            m_instance = new ConfigManager();
        return m_instance;
    }
    
    /**
     * ConfigManager default constructor
     */
    private ConfigManager() {
    	// initialize logging
    	if(getProperty("LOG4J_CONFIG") != null) {
    		PropertyConfigurator.configure(getProperty("LOG4J_CONFIG"));
    	}
    	m_logger = Logger.getLogger(ConfigManager.class);
    }
    
    /**
     * Resets the configuration table
     */
    public void reset() {
    	m_props = new Properties();
    }
    
         
    /**
     * Init method for reading the config data
     * from a properties file. Note that this method
     * doesn't reset the configuration table held in
     * memory. Thus you can use it multpile times and
     * add constantly new configuration entries. Use the
     * reset() method to reset the configuration table.
     * @param cfgFileName config file anme or URL
     * @return success flag
     */
    public static boolean init(String cfgFileName) {
    	boolean bOk = false;
        try {
        	if(m_props == null)
        		m_props = new Properties();
            InputStream isCfg = null;
            URL url = null;
            if(cfgFileName.startsWith("http")) {
                url = new URL(cfgFileName);
                isCfg = url.openStream();
            } else if(cfgFileName.startsWith("jar://")) {
            	ClassLoader cl = ConfigManager.class.getClassLoader();
                isCfg = cl.getResourceAsStream(cfgFileName.substring(6));
            } else {
                isCfg = new FileInputStream(cfgFileName);
            }
            m_props.load(isCfg);
            isCfg.close();
            url = null; 
			bOk = true;
        } catch (Exception ex) {            
            m_logger.error("Cannot read config file: " + 
                cfgFileName + " Reason: " + ex.toString());
        }
        // initialize
        return bOk;
    }
         
    /**
     * Init method for settings the config data
     * from a any user defined source
     * @param hProps config data
     */
    public static void init(Map<String, String> hProps) {
    	m_props = new Properties();
      	m_props.putAll(hProps);
    }
    
    public int findServiceWithCode(String sCode) {
    	int nService = 0;
    	int nServices = getIntProperty("SERVICE_COUNT", 0);
    	for(int i = 1; i <= nServices; i++) {
    		String sCd = getProperty("SERVICE_" + i + "_CODE");
    		if(sCd != null && sCode != null && sCd.equals(sCode))
    			return i;
    	}
    	return nService;
    }
    
    /**
     * Retrieves a connection to SignWise server using the parameters in configuration file
     * @return connection to SignWise server
     */
    public SignWiseConnection getSignWiseConnection()
    {
    	KeystoreInfo keyStore = new KeystoreInfo(getProperty("KEYSTORE_FILE"), getProperty("KEYSTORE_PSWD"), getProperty("KEYSTORE_TYPE"));
    	KeystoreInfo trustStore = new KeystoreInfo(getProperty("TRUSTSTORE_FILE"), getProperty("TRUSTSTORE_PSWD"), getProperty("TRUSTSTORE_TYPE"));
    	SignWiseConnection conn = new SignWiseConnection(getProperty("SERVER_URL"), keyStore, trustStore);
    	return conn;
    }
    
    /**
     * Retrieves the value for the spcified key
     * @param key property name
     */
    public String getProperty(String key) {
        return m_props.getProperty(key);        
    }
   
    
    /**
     * Retrieves a string value for the specified key
     * @param key property name
     * @param def default value
     */
    public String getStringProperty(String key, String def) {
        return m_props.getProperty(key, def);        
    }
    
    public void setStringProperty(String key, String value) {
    	if(m_props != null)
    		m_props.put(key, value);
    }
   
    /**
     * Retrieves an int value for the specified key
     * @param key property name
     * @param def default value
     */
    public int getIntProperty(String key, int def) {
        int rc = def;
        try {
        	String s = m_props.getProperty(key);
        	if(s != null && s.trim().length() > 0)
        		rc = Integer.parseInt(s);    
        } catch(NumberFormatException ex) {
            m_logger.error("Error parsing number: " + key, ex);
        }
        return rc;
    }

    /**
     * Retrieves a long value for the specified key
     * @param key property name
     * @param def default value
     */
    public long getLongProperty(String key, long def) {
    	long rc = def;
        try {
        	String s = m_props.getProperty(key);
        	if(s != null && s.trim().length() > 0)
        		rc = Long.parseLong(s);    
        } catch(NumberFormatException ex) {
            m_logger.error("Error parsing number: " + key, ex);
        }
        return rc;
    }
    
    /**
     * Retrieves a boolean value for the specified key
     * @param key property name
     * @param def default value
     */
    public boolean getBooleanProperty(String key, boolean def) {
    	boolean rc = def;
        try {
        	String s = m_props.getProperty(key);
        	if(s != null) {
        		if(s.trim().equalsIgnoreCase("TRUE"))
        		   rc = true;  
        		if(s.trim().equalsIgnoreCase("FALSE"))
         		   rc = false;
        	}
        } catch(NumberFormatException ex) {
            m_logger.error("Error parsing boolean: " + key, ex);
        }
        return rc;
    }
}
