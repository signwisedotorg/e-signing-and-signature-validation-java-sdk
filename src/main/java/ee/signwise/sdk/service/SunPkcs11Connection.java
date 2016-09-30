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
import java.util.*;
import java.security.*; 
import java.security.KeyStore.ProtectionParameter;
import java.security.cert.*; 
import iaik.pkcs.pkcs11.*; 
import iaik.pkcs.pkcs11.objects.*; 
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Vector;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import org.apache.log4j.Logger;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.ArrayList;
import java.security.Signature;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;


/**
 * PKCS#11 based signature implementation using
 * Sun pkcs11 API.
 */
public class SunPkcs11Connection {
	private static Logger m_logger = Logger.getLogger(SunPkcs11Connection.class);
    private Provider m_provider;
    private KeyStore m_keyStore;
    public String m_alias = null;
    
    
    /**
     * Constructor for SunPkcs11Connection
     * @param driver path and filename of pkcs11 driver
     * @param passwd PIN code
     * @param nSlot slot number
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public SunPkcs11Connection(String driver, String passwd, int nSlot)
    		throws	ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    		m_provider = null;
        	m_keyStore = null;
    		String config = "name=OpenSC\n" + "library=" + driver + "\n" +
    				"slotListIndex=" + nSlot; // + "disabledMechanisms = { CKM_SHA1_RSA_PKCS }\n";
    		if(m_logger.isDebugEnabled())
    			m_logger.debug("init driver with config:\n---\n" + config + "\n---\n");
            ByteArrayInputStream confStream = new ByteArrayInputStream(config.getBytes());
            sun.security.pkcs11.SunPKCS11 pkcs11 = new sun.security.pkcs11.SunPKCS11(confStream);
            m_provider = (Provider)pkcs11;
            Security.addProvider(m_provider);
            if(m_logger.isDebugEnabled())
    			m_logger.debug("Driver inited");
            String sProv = "org.bouncycastle.jce.provider.BouncyCastleProvider";
			m_logger.info("Init BC provider " + sProv);
			System.out.println("Init provider " + sProv);
			Provider prv = (Provider)Class.forName(sProv).newInstance();
			Security.addProvider(prv);
    }
    
    /**
     * Computes a digest
     * @param data input data
     * @param digType digest type
     * @return digest value
     */
    public byte[] digestOfType(byte[] data, String digType)
    		throws NoSuchAlgorithmException, NoSuchProviderException
    {
        byte[] dig = null;
        MessageDigest sha = MessageDigest.getInstance(digType, "BC");
        sha.update(data);
        dig = sha.digest();
        return dig;
    }
    
    /**
     * Retrieves users certificates in the current slot
     * @param passwd PIN code
     * @return list of certificates
     */
    public List<X509Certificate> getCertificates(String passwd)
    {
    	List<X509Certificate> lcerts = new ArrayList<X509Certificate>();
    	try {
    		String javaLibPath = System.getProperty("java.library.path");
            if(m_logger.isDebugEnabled())
    			m_logger.debug("init keystore" + " in: " + javaLibPath + " provider: " + ((m_provider != null) ? "OK" : "NULL"));
    		if(m_provider == null)
    			return null;
    		// load keystore
    		m_keyStore = KeyStore.getInstance("PKCS11", m_provider);
    		if(m_logger.isDebugEnabled())
    			m_logger.debug("Load keystore: " + m_provider.getName() + " - " + m_provider.getInfo());
    		m_keyStore.load(null, passwd.toCharArray());
    		// list keystore
    		Enumeration eAliases = m_keyStore.aliases();
			while(eAliases.hasMoreElements()) {
				String al = (String)eAliases.nextElement();
				if(m_logger.isDebugEnabled())
	    			m_logger.debug("Alias: " + al);
				X509Certificate cert = (X509Certificate)m_keyStore.getCertificate(al);
				if(cert != null)
					lcerts.add(cert);
				if(m_alias == null)
					m_alias = al;
			}
			if(m_logger.isDebugEnabled())
    			m_logger.debug("Keystore loaded");
    	} catch(Exception ex) {
    		if (ex instanceof sun.security.pkcs11.wrapper.PKCS11Exception) {
                if ("CKR_PIN_INCORRECT".equals(ex.getMessage())) {
                	m_logger.error("Invalid PIN: ");
                	return null;
                }
            }
    		m_logger.error("Error init keystore: " + ex);
    	}
    	return lcerts;
    }
    
    /**
     * Method returns a digital signature. It finds the RSA private 
     * key object from the active token and
     * then signs the given data with this key and RSA mechanism.
     * @param digest digest of the data to be signed.
     * @param pin users pin code
     * @return an array of bytes containing digital signature.
     * @throws NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException if signing the data fails.
     */
    public byte[] sign(byte[] digest, String pin) 
    		throws RuntimeException
    {
    	try {
    		if(m_keyStore == null) {
    			m_logger.error("Failed to load keystore");
    			return null;
    		}
    			
    		try {
    			if(m_logger.isDebugEnabled() && digest != null)
	    			m_logger.debug("Signing: " + bin2hex(digest) + " len: " + digest.length + " with: " + m_alias + " on: " + m_provider.getName());
    			System.out.println("Signing: " + bin2hex(digest) + " len: " + digest.length + " with: " + m_alias + " on: " + m_provider.getName());
    			/*Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, m_keyStore.getKey(m_alias, pin.toCharArray()));
                byte[] sdata = cipher.doFinal(digest);
                if(m_logger.isDebugEnabled())
	    			m_logger.debug("Signature: " + bin2hex(sdata) + " len: " + ((sdata != null) ? sdata.length : 0));
                return sdata;*/
    			m_keyStore = KeyStore.getInstance("PKCS11", m_provider);
    			m_keyStore.load(null, pin.toCharArray());
    			System.out.println("get key for " + m_alias);
    			PrivateKey privateKey = (PrivateKey) m_keyStore.getKey(m_alias, pin.toCharArray());
    			System.out.println("got key " + privateKey);
    			Signature signature = Signature.getInstance("SHA1withRSA");
    			signature.initSign(privateKey);
    			signature.update(digest);
    			return signature.sign();
    			
            /*} catch (NoSuchPaddingException e) {
                throw new RuntimeException(e);*/
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } /*catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e) {
                // More likely bad password
            	throw new RuntimeException("Invalid PIN", e);
            }*/
    	} catch(Exception ex) {
    		m_logger.error("Error init provider: " + ex);
    		ex.printStackTrace();
    	}
    	return null;
    }
 
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
