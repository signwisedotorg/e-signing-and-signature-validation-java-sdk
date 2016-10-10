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
import java.io.ByteArrayOutputStream;
import org.apache.log4j.Logger;

/**
 * Utility functions
 */
public class ConvertUtils {
	private static Logger m_logger = Logger.getLogger(ConvertUtils.class);
	
	/** 
     * Converts a hex string to byte array
     * @param hexString input data
     * @return byte array
     */
    public static byte[] hex2bin(String hexString)
    {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try {
    		for(int i = 0; (hexString != null) && 
    			(i < hexString.length()); i += 2) {
				String tmp = hexString.substring(i, i+2);  
				Integer x = new Integer(Integer.parseInt(tmp, 16));
    			bos.write(x.byteValue());    			
    		}
    	} catch(Exception ex) {
    		m_logger.error("Error converting hex string: " + ex);
    	}
    	return bos.toByteArray();
    }
    
    /**
     * Converts a byte array to hex string
     * @param arr byte array input data
     * @return hex string
     */
    public static String bin2hex(byte[] arr)
    {
    	StringBuffer sb = new StringBuffer();
    	for(int i = 0; i < arr.length; i++) {
    		String str = Integer.toHexString((int)arr[i]);
    		if(str.length() == 2)
    			sb.append(str);
    		if(str.length() < 2) {
    			sb.append("0");
    			sb.append(str);
    		}
    		if(str.length() > 2)
    			sb.append(str.substring(str.length()-2));
    	}
    	return sb.toString();
    }
}
