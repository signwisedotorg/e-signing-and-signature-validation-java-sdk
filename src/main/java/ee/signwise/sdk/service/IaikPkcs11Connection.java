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
import java.security.*; 
import java.security.cert.*; 

import iaik.pkcs.pkcs11.*; 
import iaik.pkcs.pkcs11.objects.*; 
import iaik.pkcs.pkcs11.wrapper.PKCS11Constants;

import java.io.IOException;
//import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;




public class IaikPkcs11Connection {
	/** Object represent a current PKCS#11 module. */
    private Module m_pkcs11Module;
    /** security provider */
    private Provider m_secProvider;
	/** log4j logger */
    private static Logger m_logger = Logger.getLogger(IaikPkcs11Connection.class);
    /** PKCS#11 module  is initialized */
    private static boolean m_isInitialized;
    
    
    public IaikPkcs11Connection(String sDriver)
    throws PrivilegedActionException, TokenException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	if(m_logger.isInfoEnabled())
   			m_logger.info(sDriver +
                 " libpath: " + System.getProperty("java.library.path"));
        // load PKCS11 module
    	final String moduleName = sDriver;
        m_pkcs11Module = (Module)AccessController.doPrivileged(
            new PrivilegedExceptionAction()  {
                public java.lang.Object run() throws IOException {
                    Module m = Module.getInstance(moduleName);
                    return m;
                }
            }
        );
        try {
          if (!m_isInitialized) {
        	m_pkcs11Module.initialize(null); // initializes the module
        	m_isInitialized = true;
          }
        } catch(iaik.pkcs.pkcs11.wrapper.PKCS11Exception ex) {
        	m_logger.error("Pkcs11 error: " + ex);
        	if(ex.getErrorCode() == PKCS11Constants.CKR_CRYPTOKI_ALREADY_INITIALIZED) {
        		m_logger.error("PKCS11 already loaded ok");
        		m_isInitialized = true;
        	} else
        		throw new RuntimeException("Error loading driver: " + ex);
        }
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
    public List<X509Certificate> getCertificates(int nSlot, String passwd)
    {
    	List<X509Certificate> lcerts = new ArrayList<X509Certificate>();
    	try {
    		CertificateFactory certFac = CertificateFactory.getInstance("X.509");
      	  	Slot[] slots = m_pkcs11Module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT); 
      	  if(nSlot < slots.length) {
      		 SlotInfo si = slots[nSlot].getSlotInfo(); // get information about this slot object
             if(m_logger.isDebugEnabled())
             	m_logger.debug("Slot " + nSlot + ": " + si);
             if(si.isTokenPresent()) {
            	 Token tok = slots[nSlot].getToken();
                 if(m_logger.isDebugEnabled())
               	m_logger.debug("Token: " + tok);
                 Session sess = tok.openSession(Token.SessionType.SERIAL_SESSION,
                         Token.SessionReadWriteBehavior.RO_SESSION,null,null);
                 X509PublicKeyCertificate templCert = new X509PublicKeyCertificate();
                 sess.findObjectsInit(templCert);
                 iaik.pkcs.pkcs11.objects.Object[] certs = null;
                 do {
               	  certs = sess.findObjects(1); // find next cert
               	  if(certs != null && certs.length > 0) {
               		  if(m_logger.isDebugEnabled())
               			  m_logger.debug("Certs: " + certs.length);
               		  for(int j = 0; (certs != null) && (j < certs.length); j++) {
               			  X509PublicKeyCertificate x509 = (X509PublicKeyCertificate)certs[j];
               			  byte[] derCert = x509.getValue().getByteArrayValue(); 
               			  X509Certificate cert = (X509Certificate)certFac.generateCertificate(new ByteArrayInputStream(derCert));
               			  lcerts.add(cert);
               		  }
               	  } // loop until all certs read
                 } while(certs != null && certs.length > 0);
                 sess.closeSession();
                 sess = null;
             }
      	  }
    	} catch(Exception ex) {
    		m_pkcs11Module = null; // reset since we had an error
    		m_logger.error("Error read cert: " + ex);
    	}
    	return lcerts;
    }
 
    /**
     * Method returns a digital signature. It finds the RSA private 
     * key object from the active token and
     * then signs the given data with this key and RSA mechanism.
     * @param digest digest of the data to be signed.
     * @param nSlot slot index
     * @param pin users pin code
     * @return an array of bytes containing digital signature.
     * @throws DigiDocException if signing the data fails.
     */
    public byte[] sign(byte[] digest, int nSlot, String pin) 
        throws TokenException 
    {
        byte[] sigVal = null;
        //try {
        	if(m_logger.isDebugEnabled())
       			m_logger.debug("Sign with slot: " + nSlot + " pin: " + pin);
        	Slot[] slots = m_pkcs11Module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT); 
        	if(nSlot < slots.length) {
        		Token tok = slots[nSlot].getToken();
        		Session sess = tok.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RO_SESSION,null,null);
        		sess.login(Session.UserType.USER, pin.toCharArray()); 
	            RSAPrivateKey tempKey = new RSAPrivateKey(); 
	            sess.findObjectsInit(tempKey); 
	            iaik.pkcs.pkcs11.objects.Object[] keys = null;
                
	            RSAPrivateKey sigKey = null;
	            boolean bFound = false;
	            do {
	            	keys = sess.findObjects(1); 
	            	if(keys != null && keys.length > 0) {
            		for(int i = 0; !bFound && i < keys.length; i++) {
            			sigKey = (RSAPrivateKey)keys[i];
            			String keyIdHex = bin2hex(sigKey.getId().getByteArrayValue());
            			if(m_logger.isDebugEnabled())
            				m_logger.debug("Key " + i + " id: " + keyIdHex);
            			// add digest asn.1 structure prefix
            			digest = addDigestAsn1Prefix(digest);
            			Mechanism sigMech = Mechanism.RSA_PKCS;
            			sess.signInit(sigMech, sigKey); 
            			sigVal = sess.sign(digest); // signs the given data with the key and mechanism given to the signInit method
            			if(m_logger.isDebugEnabled())
            					m_logger.debug("Signature len: " + ((sigVal != null) ? sigVal.length : 0));
            			break;
            		} // for
            	} // if keys found
	            } while(!bFound && keys != null && keys.length > 0);
	            sess.findObjectsFinal(); // finalizes a find operation
            	// close session
            	sess.closeSession();
            	sess = null;
        	}
        	
        /*} catch(TokenException e) {
            DigiDocException.handleException(e, DigiDocException.ERR_SIGN);
        } */
        return sigVal;
    } 
    
    
    /** 
     * Converts a hex string to byte array
     * @param hexString input data
     * @return byte array
     */
    public byte[] hex2bin(String hexString)
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
    public String bin2hex(byte[] arr)
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
    
    /** SHA1 digest data is allways 20 bytes */
    public static final int SHA1_DIGEST_LENGTH = 20;
    /** SHA224 digest data is allways 28 bytes */
    public static final int SHA224_DIGEST_LENGTH = 28;
    /** SHA256 digest data is allways 32 bytes */
    public static final int SHA256_DIGEST_LENGTH = 32;
    /** SHA512 digest data is allways 64 bytes */
    public static final int SHA512_DIGEST_LENGTH = 64;
    
    /** SHA1 algortihm prefix - 00 30 21 30 09 06 05 2b 0e 03 02 1a 05 00 04 14  */
    private static final byte[] sha1AlgPrefix1 = { // long
        0x30, 0x21, 0x30, 0x09, 0x06, 
        0x05, 0x2b, 0x0e, 0x03, 0x02, 
        0x1a, 0x05, 0x00, 0x04, 0x14 };
    private static final byte[] sha1AlgPrefix2 = { // short
    	0x30, 0x1f, 0x30, 0x07, 0x06, 
    	0x05, 0x2b, 0x0e, 0x03, 0x02, 
    	0x1a, 0x04, 0x14 };
    /** SHA224 prefix - 00302d300d06096086480165030402040500041c */
    private static final byte[] sha224AlgPrefix1 = { // long
        0x30, 0x2d, 0x30, 0x0d, 0x06, 0x09, 0x60,
        (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 
        0x04, 0x05, 0x00, 0x04, 0x1c };
    private static final byte[] sha224AlgPrefix2 = { // short
    	0x30, 0x2b, 0x30, 0x0b, 0x06, 0x09, 0x60,
        (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 
        0x04, 0x04, 0x1c }; 
    /** sha256 alg prefix - 003031300d060960864801650304020105000420 5ad8f86f90558d973aba4ce9be116646efd2c57758e5238b841d50abe788bae9 */
    private static final byte[] sha256AlgPrefix1 = { // long
        0x30, 0x31, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x05, 0x00, 0x04, 0x20
    };
    private static final byte[] sha256AlgPrefix2 = { // short
    	0x30, 0x2f, 0x30, 0x0b, 0x06, 0x09, 0x60, (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x04, 0x20
    };
    private static final byte[] sha512AlgPrefix1 =   // long
    { 0x30, 0x51, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x03, 0x05, 0x00, 0x04, 0x40 };
    private static final byte[] sha512AlgPrefix2 =    // short
    { 0x30, 0x4f, 0x30, 0x0b, 0x06, 0x09, 0x60, (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x03, 0x04, 0x40 };
    
    /**
     * Adds ASN.1 structure prefix to digest value to be signed
     * @param digest digest value to be signed
     * @return prefixed digest value
     */
    private byte[] addDigestAsn1Prefix(byte[] digest)
    {
    	byte[] ddata = null;
    	if(digest.length == SHA1_DIGEST_LENGTH) {
    	  ddata = new byte[sha1AlgPrefix1.length + digest.length];
    	  System.arraycopy(sha1AlgPrefix1, 0, ddata, 0, sha1AlgPrefix1.length);
    	  System.arraycopy(digest, 0, ddata, 
    			sha1AlgPrefix1.length, digest.length);
    	}
    	if(digest.length == SHA224_DIGEST_LENGTH) {
        	  ddata = new byte[sha224AlgPrefix1.length + digest.length];
        	  System.arraycopy(sha224AlgPrefix1, 0, ddata, 0, sha224AlgPrefix1.length);
        	  System.arraycopy(digest, 0, ddata, 
        			  sha224AlgPrefix1.length, digest.length);
        }
    	if(digest.length == SHA256_DIGEST_LENGTH) {
      	  ddata = new byte[sha256AlgPrefix1.length + digest.length];
      	  System.arraycopy(sha256AlgPrefix1, 0, ddata, 0, sha256AlgPrefix1.length);
      	  System.arraycopy(digest, 0, ddata, 
      			sha256AlgPrefix1.length, digest.length);
      	}
    	if(digest.length == SHA512_DIGEST_LENGTH) {
      	  ddata = new byte[sha512AlgPrefix1.length + digest.length];
      	  System.arraycopy(sha512AlgPrefix1, 0, ddata, 0, sha512AlgPrefix1.length);
      	  System.arraycopy(digest, 0, ddata, 
      			sha512AlgPrefix1.length, digest.length);
      	}
    	return ddata;
    }
}
