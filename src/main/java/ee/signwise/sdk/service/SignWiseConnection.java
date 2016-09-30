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
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.security.cert.CertificateException;
import ee.signwise.sdk.model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.scheme.Scheme;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.X509KeyManager;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException; 
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;
import java.security.KeyStore;




public class SignWiseConnection {
	private static Logger m_logger = Logger.getLogger(SignWiseConnection.class);
	private String m_url;
	private KeystoreInfo m_trustStore, m_keyStore;
	
	/**
	 * Constructor for SignWiseConnection
	 * @param url service url
	 * @param keyStore cliet keystore
	 * @param trustStore service truststore
	 */
	public SignWiseConnection(String url, KeystoreInfo keyStore, KeystoreInfo trustStore)
	{
		m_url = url;
		m_trustStore = trustStore;
		m_keyStore = keyStore;
	}
	
	/**
	 * Invokes SignWise services API to retrieve service version number
	 * @return service response is JSON
	 */
	public ServerResponse serviceVersion()
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/version";
			JSONObject jo = new JSONObject();
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "GET", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				JSONObject jo2 = jo.optJSONObject("pkg");
				if(jo2 != null)
					sResp = new VersionInfo(jo2);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling server version: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to have it parse certificate 
	 * and return certificate info
	 * @param cert certificate object
	 * @return service response is JSON
	 */
	public ServerResponse serviceCertificateParse(X509Certificate cert)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/certificate/parse";
			String sCertPem = new String(writeCertToPem(cert));
			JSONObject jo = new JSONObject();
			jo.put("certificate", sCertPem);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				JSONObject jo2 = jo.optJSONObject("certificate");
				if(jo2 != null)
					sResp = new CertificateInfo(jo2);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling certificate parse: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to have it parse certificate 
	 * and return OCSP status info for this certificate
	 * @param cert certificate object
	 * @return service response is JSON
	 */
	public ServerResponse serviceCertificateOcsp(X509Certificate cert)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/certificate/ocsp";
			String sCertPem = new String(writeCertToPem(cert));
			JSONObject jo = new JSONObject();
			jo.put("certificate", sCertPem);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo.optString("status") != null)
					sResp = new CertificateOcsp(jo);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling certificate ocsp: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API retrieve users MID certficate(s)
	 * @param sCntType container type - bdoc / edoc / pdf / asan
	 * @param sMsIsdn users mobile phone number (required)
	 * @param sSsn users social security number  (required for ddoc/bdoc/edoc/pdf)
	 * @param sUserid users userid (required for asan)
	 * @return service response is JSON
	 */
	public String serviceCertificateMobile(String sCntType, String sMsIsdn, String sSsn, String sUserid)
	{
		String sResponse = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/certificate/mobile";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			JSONObject joUi = new JSONObject();
			joUi.put("msisdn", sMsIsdn);
			if(sUserid != null)
				joUi.put("id", sUserid);
			if(sSsn != null)
				joUi.put("ssn", sSsn);
			jo.put("userInfo", joUi);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			
		} catch(Exception ex) {
			m_logger.error("Error calling certificate mobile: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResponse;
	}
	
	/**
	 * Invokes SignWise services API to create a container
	 * @param sCntType container type - bdoc / edoc / pdf / asan
	 * @param sOutputPath  (https) path/url for new container file (in your fileproxy)
	 * @param bOverwrite true if overwrite existing file
	 * @param files initial datafiles to add to the container
	 * @return service response is JSON
	 */
	public ServerResponse serviceContainerCreate(String sCntType, String sOutputPath, boolean bOverwrite, List<InputFileInfo> files)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("outputPath", sOutputPath);
			jo.put("overwrite", bOverwrite);
			if(files != null && files.size() > 0) {
				JSONArray jarr = new JSONArray();
				for(InputFileInfo fil : files) {
					JSONObject jof = fil.toJSON();
					jarr.put(jof);
				}
				jo.put("files", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				JSONObject jo2 = jo.optJSONObject("container");
				if(jo2 != null && jo2.getString("type").equals("bdoc"))
					sResp = new BDocContainerInfo(jo2);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling container create: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to create a container
	 * @param sCntType container type - bdoc / edoc / pdf / asan
	 * @param sInputPath  (https) path/url for input container file (in your fileproxy)
	 * @return service response is JSON
	 */
	public ServerResponse serviceContainerInfo(String sCntType, String sInputPath)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container/info";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				JSONObject jo2 = jo.optJSONObject("container");
				if(jo2 != null && jo2.getString("type").equals("bdoc"))
					sResp = new BDocContainerInfo(jo2);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling container info: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to download/extract a datafile from signed container
	 * @param sCntType container type - bdoc / edoc / asan
	 * @param sInputPath  (https) path/url for input container file (in your fileproxy)
	 * @param fileId datafile id
	 * @return service response is JSON
	 */
	public byte[] serviceContainerDownload(String sCntType, String sInputPath, String fileId)
	{
		byte[] bData = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container/download";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			jo.put("fileId", fileId);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			bData = callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE len: " + bData.length);
		} catch(Exception ex) {
			m_logger.error("Error calling container download: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return bData;
	}
	
	
	/**
	 * Invokes SignWise services API to add a datafile to container
	 * @param sCntType container type - bdoc / edoc / asan
	 * @param sInputPath  (https) path/url for container file (in your fileproxy)
	 * @param files initial datafiles to add to the container
	 * @return service response is JSON
	 */
	public String serviceContainerAddFile(String sCntType, String sInputPath, List<InputFileInfo> files)
	{
		String sResponse = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container/file";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			if(files != null && files.size() > 0) {
				JSONArray jarr = new JSONArray();
				for(InputFileInfo fil : files) {
					JSONObject jof = fil.toJSON();
					jarr.put(jof);
				}
				jo.put("files", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			sResponse = new String(callUrl(sUrl, sRequest, "PUT", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			/*if(sResponse != null) {
				jo = new JSONObject(sResponse);
				JSONObject jo2 = jo.optJSONObject("container");
				if(jo2 != null && jo2.getString("type").equals("bdoc"))
					sResp = new BDocContainerInfo(jo2);
				else
					sResp = new ErrorResponse(jo);
			}*/
		} catch(Exception ex) {
			m_logger.error("Error calling container add file: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResponse;
	}
	
	/**
	 * Invokes SignWise services API to extract a datafile from container.
	 * Extracted file(s) will be store in outputPath and removed from container.
	 * @param sCntType container type - bdoc / edoc / asan
	 * @param sInputPath  (https) path/url for container file (in your fileproxy)
	 * @param files initial datafiles to add to the container
	 * @return service response is JSON
	 */
	public ServerResponse serviceContainerExtractFile(String sCntType, String sInputPath, List<OutputFileInfo> files)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container/file";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			if(files != null && files.size() > 0) {
				JSONArray jarr = new JSONArray();
				for(OutputFileInfo fil : files) {
					JSONObject jof = fil.toJSON();
					jarr.put(jof);
				}
				jo.put("files", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				JSONObject jo2 = jo.optJSONObject("container");
				if(jo2 != null && jo2.getString("type").equals("bdoc"))
					sResp = new BDocContainerInfo(jo2);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling container extract: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to delete a datafile from container.
	 * @param sCntType container type - bdoc / edoc / asan
	 * @param sInputPath  (https) path/url for container file (in your fileproxy)
	 * @param files initial datafiles to add to the container
	 * @return service response is JSON
	 */
	public ServerResponse serviceContainerDeleteFile(String sCntType, String sInputPath, List<String> fileIds)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container/file";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			if(fileIds != null && fileIds.size() > 0) {
				JSONArray jarr = new JSONArray();
				for(String sId : fileIds) {
					jarr.put(sId);
				}
				jo.put("fileIds", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "DELETE", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				JSONObject jo2 = jo.optJSONObject("container");
				if(jo2 != null && jo2.getString("type").equals("bdoc"))
					sResp = new BDocContainerInfo(jo2);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling container delete: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to authenticate a user by decrypting the
	 * signature of the given hash using the given certificate and verifying
	 * certificate status
	 * @param digest signed hash
	 * @param signature signature value
	 * @param cert users certificate
	 * @param caCerts user certificates CA certificate chain (optional)
	 * @return certificate status GOOD / REVOKED / UNKNOWN
	 */
	public String serviceAuthenticate(byte[] digest, byte[] signature, X509Certificate cert, List<X509Certificate> caCerts)
	{
		String sResponse = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/authentication/verify";
			JSONObject jo = new JSONObject();
			jo.put("digest", bin2hex(digest));
			jo.put("signature", bin2hex(signature));
			jo.put("certificate", new String(writeCertToPem(cert)));
			if(caCerts != null && caCerts.size() > 0) {
				JSONArray jarr = new JSONArray();
				for(X509Certificate ca : caCerts) {
					jarr.put(new String(writeCertToPem(ca)));
				}
				jo.put("certificateChain", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null)
					return jo.getString("status");
			}
		} catch(Exception ex) {
			m_logger.error("Error calling authenticate: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResponse;
	}
	
	/**
	 * Invokes SignWise services API to authenticate a user by mobile phone
	 * @param sPhone phone number all numbers with country code
	 * @param perCode personal code or social security code
	 * @param nUserId users id by ASAN
	 * @param sLang language code
	 * @param sCallbackUrl callback url
	 * @return service verification code that displays on users mobile phone
	 */
	public String serviceAuthenticateMobile(String sPhone, String perCode, int nUserId, String sLang, String sCallbackUrl)
	{
		String sResponse = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/authentication/mobile";
			JSONObject jo = new JSONObject();
			jo.put("callbackURL", sCallbackUrl);
			JSONObject jo2 = new JSONObject();
			jo2.put("msisdn", sPhone);
			jo2.put("ssn", perCode);
			if(sLang != null)
				jo2.put("language", sLang);
			if(nUserId > 0)
				jo2.put("id", nUserId);
			jo.put("userInfo", jo2);
			
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null)
					return jo.getString("verificationCode");
			}
		} catch(Exception ex) {
			m_logger.error("Error calling authenticate: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResponse;
	}
	
	/**
	 * Invokes SignWise services API to start signing 
	 * @param sCntType conainer type (required)
	 * @param sInputPath input container path  (required)
	 * @param sTmpPath temporary path for half-ready container (required)
	 * @param sShareId share id (optional)
	 * @param sRecipient recipient id (optional)
	 * @param hashTypes list of suported hash types (optional)
	 * @param cert users certificate in base64/pem (required)
	 * @param caCerts list of ca certs (optional)
	 * @param sCountry postal address - country (optional)
	 * @param sState postal address - state/province (optional)
	 * @param sCity postal address - city/locality (optional)
	 * @param sPostalCode postal address - postal code (optional)
	 * @param sRole role/manifest (optional)
	 * @param sPlaceholder visual signature parameters (pdf) - visual badge location placehoder - string in document (optional)
	 * @param nPage visual signature parameters (pdf) - visual badge location page number (optional, doesn't use together with placeholder)
	 * @param nX visual signature parameters (pdf) - visual badge location X coordinate (optional, doesn't use together with placeholder)
	 * @param nY visual signature parameters (pdf) - visual badge location Y coordinate (optional, doesn't use together with placeholder)
	 * @return service response
	 */
	public ServerResponse serviceSignPrepare(String sCntType, String sInputPath, String sTmpPath, 
			String sShareId, String sRecipient, List<String> hashTypes, 
			X509Certificate cert, List<X509Certificate> caCerts, 
			String sCountry, String sState, String sCity, String sPostalCode, String sRole,
			String sPlaceholder, int nPage, int nX, int nY )
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container/sign/prepare";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			jo.put("tmpPath", sTmpPath);
			if(sShareId != null)
				jo.put("shareId", sShareId);
			if(sRecipient != null)
				jo.put("recipientId", sRecipient);
			if(hashTypes != null) {
				JSONArray jarr = new JSONArray();
				for(String sHash : hashTypes) {
					jarr.put(sHash);
				}
				jo.put("supportedHashes", jarr);
			}
			JSONObject jo2 = new JSONObject();
			jo2.put("certificate", new String(writeCertToPem(cert)));
			if(caCerts != null && caCerts.size() > 0) {
				JSONArray jarr = new JSONArray();
				for(X509Certificate ca : caCerts) {
					jarr.put(new String(writeCertToPem(ca)));
				}
				jo2.put("certificateChain", jarr);
			}
			if(sCountry != null)
				jo2.put("country", sCountry);
			if(sState != null)
				jo2.put("state", sState);
			if(sCity != null)
				jo2.put("city", sCity);
			if(sPostalCode != null)
				jo2.put("postalCode", sPostalCode);
			if(sRole != null)
				jo2.put("role", sRole);
			if(sPlaceholder != null || nPage > 0 || nX > 0 || nY > 0) {
				JSONObject jo3 = new JSONObject();
				if(sCountry != null)
					jo3.put("placeholder", sPlaceholder);
				if(nPage > 0)
					jo3.put("page", nPage);
				if(nX > 0)
					jo3.put("x", nX);
				if(nY > 0)
					jo3.put("y", nY);
				jo2.put("visualSignature", jo3);
			}
			jo.put("signerInfo", jo2);
			
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("signatureId") != null)
					sResp = new SignaturePrepareResponse(jo);
				else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling sign prepare: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to start signing 
	 * @param sCntType conainer type (required)
	 * @param sInputPath input container path  (required)
	 * @param sSignatureValue signature value in hex (required)
	 * @param sPlugin plugin id (ee or sw) (optional)
	 * @return service response
	 */
	public ServerResponse serviceSignFinalize(String sCntType, String sInputPath, String sSignatureValue, String sPlugin)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/container/sign/finalize";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			jo.put("signatureValue", sSignatureValue);
			jo.put("plugin", sPlugin);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("container") != null) {
					JSONObject jo2 = jo.getJSONObject("container");
					if(jo2 != null) {
						if(jo2.getString("type").equals("bdoc")) 
							sResp = new BDocContainerInfo(jo2);
						if(jo2.getString("type").equals("pdf")) 
							sResp = new PdfContainerInfo(jo2);
					}
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling sign finalize: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	
	/**
	 * Invokes SignWise services API to create a share.
	 * Share is an entry that enables asking other users to sign
	 * the given document. 
	 * @param sCntType container type (required)
	 * @param sInputPath input container path  (required). If shareable object is SignWise document then inputPath should be empty.
	 * @param sDocument document id (hex) (optional). If you are sharing SignWise document you have to use this parameter instead of inputPath
	 * @param sDocPreviewPath path to directory in your fileproxy to store preview images (optional)
	 * @param sCallbackUrl url to receice notifications (optional) Endpoint which receives message when share state changes.
	 * @param dExpires share expiration timestamp. Expiration date. UNIX timestamp with milliseconds.
	 * @param sName share name (required)
	 * @param sDescription share description (optional)
	 * @param sFileName share container file name (optional)
	 * @param sAuth authorization type (basic/residence). (optional)
	 * @param sType share type, defaults to “parallel”. (parallel/sequential) (optional)
	 * @param sSharer sharer data (optional)
	 * @param recipients list of recipients (optional)
	 * @param notifications list of notifications(optional)
	 * @return service response
	 */
	public ServerResponse serviceShareCreate(String sCntType, String sInputPath, String sDocument, 
			String sDocPreviewPath, String sCallbackUrl, Date dExpires, String sName, String sDescription,
			String sFileName, String sAuth, String sType, SharerInfo sharer,
			List<RecipientInfo> recipients, List<NotificationInfo> notifications)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/share";
			JSONObject jo = new JSONObject();
			jo.put("containerType", sCntType);
			jo.put("inputPath", sInputPath);
			jo.putOpt("document", sDocument);
			jo.putOpt("documentPreviewPath", sDocPreviewPath);
			jo.putOpt("callbackURL", sCallbackUrl);
			jo.put("expires", dExpires.getTime());
			jo.put("name", sName);
			jo.putOpt("description", sDescription);
			jo.putOpt("fileName", sFileName);
			jo.putOpt("auth", sAuth);
			jo.putOpt("type", sType);
			if(sharer != null)
				jo.put("sharer", sharer.toJSON());
			if(recipients != null) {
				JSONArray jarr = new JSONArray();
				for(RecipientInfo recv : recipients)
					jarr.put(recv.toJSON());
				jo.put("recipients", jarr);
			}
			if(notifications != null) {
				JSONArray jarr = new JSONArray();
				for(NotificationInfo not : notifications)
					jarr.put(not.toJSON());
				jo.put("notifications", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new ShareInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling share create: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to update a share.
	 * @param shareId share id (required)
	 * @param sCallbackUrl url to receice notifications (optional) Endpoint which receives message when share state changes.
	 * @param dExpires share expiration timestamp. Expiration date. UNIX timestamp with milliseconds.
	 * @param sName share name (required)
	 * @param sDescription share description (optional)
	 * @param sFileName share container file name (optional)
	 * @param sAuth authorization type (basic/residence). (optional)
	 * @param sType share type, defaults to “parallel”. (parallel/sequential) (optional)
	 * @param sStatus share status (active/finished/expired/deleted) (optional)
	 * @param recipients list of recipients (optional)
	 * @param notifications list of notifications(optional)
	 * @return service response
	 */
	public ServerResponse serviceShareUpdate(String shareId, String sCallbackUrl, Date dExpires, 
			String sName, String sDescription, String sFileName, String sAuth, String sType, 
			String sStatus, List<RecipientInfo> recipients)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/share/" + shareId;
			JSONObject jo = new JSONObject();
			jo.putOpt("callbackURL", sCallbackUrl);
			jo.put("expires", dExpires.getTime());
			jo.put("name", sName);
			jo.putOpt("description", sDescription);
			jo.putOpt("fileName", sFileName);
			jo.putOpt("auth", sAuth);
			jo.putOpt("type", sType);
			jo.putOpt("status", sStatus);
			if(recipients != null) {
				JSONArray jarr = new JSONArray();
				for(RecipientInfo recv : recipients) {
					recv.clearKey(); // clear key to prevent error
					jarr.put(recv.toJSON());
				}
				jo.put("recipients", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "PATCH", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new ShareInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling share update: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to delete a share.
	 * @param shareId share id (required)
	 * @param message share deletion messages (optional) 
	 * @return service response
	 */
	public ServerResponse serviceShareDelete(String shareId, MessageInfo message)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/share/" + shareId;
			JSONObject jo = new JSONObject();
			if(message != null)
			jo.putOpt("message", message.toJSON());
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "DELETE", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new ShareInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling share update: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to add a new recipient to a share
	 * @param shareId share id (required)
	 * @param name recipient name. (optional)  Example: Mati Kuusk
	 * @param key Unique recipient share key. (optional)  Example: recipientKey1
	 * @param email  recipient email. (optional) Example: recipient@recipient.com. Required if one of the notifications requires “email” transport.
	 * @param msisdn recipient phone number (optional) Example: 372192304554. Required if one of the notifications requires “sms” transport. Accepts numeric values only (without special characters).
	 * @param ssn recipient social security number. (optional) Example: 14212128025
	 * @param language recipient language. (required). Supported languages: et-EE, en-EE, ru-EE, en-LV, ru-LV, lv-LV, en-LT, ru-LT, lt-LT, en-AZ, az-AZ, en-FI, fi-FI.
	 * @param country recipient country. (required). Required if ssn is set. Supported countries: EE, LV, LT, AZ, FI
	 * @param tmpPath recipient temporary path (required). Example: http://fp/path/to/mati-tmp
	 * @param bcc blind copy flag. (optional). If set to true then recipient only sees himself and other recipients doesn’t see him.
	 * @param group recipient group. (optional) Defaults to 0. Used and required by sequential share.
	 * @param expires expiration date. (optional). UNIX timestamp with milliseconds.
	 * @param role recipient role (optional). (signer/viewer). Defaults to signer.
	 * @param status recipient status (optional) (active/queued/declined/signed/deleted/expired).
	 * @param disableReasonField reason field switch. (optional) If set to true then recipient cannot enter reason field in Signlink.
	 * @param visualSignature visual signature info for pdf (optional)
	 * @param notifications notifications for this recipient (optional)
	 * @return service response
	 */
	public ServerResponse serviceRecipientCreate(String shareId, String name, String key, String email, String msisdn, 
			String ssn, String language, String country, String tmpPath, boolean bcc, int group,
			Date expires, String role, String status, boolean disableReasonField,
			VisualSignatureInfo visualSignature, List<NotificationInfo> notifications)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/share/" + shareId + "/recipient";
			JSONObject jo = new JSONObject();
			jo.putOpt("shareId", shareId);
			jo.putOpt("key", key);
			jo.putOpt("name", name);
			jo.putOpt("email", email);
			jo.putOpt("msisdn", msisdn);
			jo.putOpt("ssn", ssn);
			jo.putOpt("language", language);
			jo.putOpt("country", country);
			jo.putOpt("tmpPath", tmpPath);
			if(bcc)
				jo.put("bcc", bcc);
			if(disableReasonField)
				jo.put("disableReasonField", disableReasonField);
			if(expires != null)
				jo.put("expires", expires.getTime());
			jo.putOpt("role", role);
			if(group > 0)
				jo.put("group", group);
			if(visualSignature != null)
				jo.put("visualSignature", visualSignature.toJSON());
			if(notifications != null) {
				JSONArray aNotifications = new JSONArray();
				for(NotificationInfo not : notifications)
					aNotifications.put(not.toJSON());
				jo.put("notifications", aNotifications);
			}
			jo.putOpt("status", status);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new RecipientInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling recipient create: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to update a recipient record
	 * @param shareId share id (required)
	 * @param sRecipientId recipient id (required)
	 * @param name recipient name. (optional)  Example: Mati Kuusk
	 * @param email  recipient email. (optional) Example: recipient@recipient.com. Required if one of the notifications requires “email” transport.
	 * @param msisdn recipient phone number (optional) Example: 372192304554. Required if one of the notifications requires “sms” transport. Accepts numeric values only (without special characters).
	 * @param ssn recipient social security number. (optional) Example: 14212128025
	 * @param language recipient language. (required). Supported languages: et-EE, en-EE, ru-EE, en-LV, ru-LV, lv-LV, en-LT, ru-LT, lt-LT, en-AZ, az-AZ, en-FI, fi-FI.
	 * @param country recipient country. (required). Required if ssn is set. Supported countries: EE, LV, LT, AZ, FI
	 * @param tmpPath recipient temporary path (required). Example: http://fp/path/to/mati-tmp
	 * @param bcc blind copy flag. (optional). If set to true then recipient only sees himself and other recipients doesn’t see him.
	 * @param group recipient group. (optional) Defaults to 0. Used and required by sequential share.
	 * @param expires expiration date. (optional). UNIX timestamp with milliseconds.
	 * @param role recipient role (optional). (signer/viewer). Defaults to signer.
	 * @param status recipient status (optional) (active/queued/declined/signed/deleted/expired).
	 * @param disableReasonField reason field switch. (optional) If set to true then recipient cannot enter reason field in Signlink.
	 * @param visualSignature visual signature info for pdf (optional)
	 * @param notifications notifications for this recipient (optional)
	 * @return service response
	 */
	public ServerResponse serviceRecipientUpdate(String shareId, String sRecipientId, String name, String email, String msisdn, 
			String ssn, String language, String country, String tmpPath, boolean bcc, int group,
			Date expires, String role, String status, boolean disableReasonField,
			VisualSignatureInfo visualSignature, List<NotificationInfo> notifications)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/share/" + shareId + "/recipient/" + sRecipientId;
			JSONObject jo = new JSONObject();
			jo.putOpt("shareId", shareId);
			jo.putOpt("recipientId", sRecipientId);
			jo.putOpt("name", name);
			jo.putOpt("email", email);
			jo.putOpt("msisdn", msisdn);
			jo.putOpt("ssn", ssn);
			jo.putOpt("language", language);
			jo.putOpt("country", country);
			jo.putOpt("tmpPath", tmpPath);
			if(bcc)
				jo.put("bcc", bcc);
			if(disableReasonField)
				jo.put("disableReasonField", disableReasonField);
			if(expires != null)
				jo.put("expires", expires.getTime());
			jo.putOpt("role", role);
			if(group > 0)
				jo.put("group", group);
			if(visualSignature != null)
				jo.put("visualSignature", visualSignature.toJSON());
			if(notifications != null) {
				JSONArray aNotifications = new JSONArray();
				for(NotificationInfo not : notifications)
					aNotifications.put(not.toJSON());
				jo.put("notifications", aNotifications);
			}
			jo.putOpt("status", status);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "PATCH", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new RecipientInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling recipient update: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to delete a recipient record
	 * @param shareId share id (required)
	 * @param sRecipientId recipient id (required)
	 * @return service response
	 */
	public ServerResponse serviceRecipientDelete(String shareId, String sRecipientId)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/share/" + shareId + "/recipient/" + sRecipientId;
			JSONObject jo = new JSONObject();
			//jo.putOpt("shareId", shareId);
			//jo.putOpt("recipientId", sRecipientId);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "DELETE", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new RecipientInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling recipient update: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to create a template.
	 * @param sInputPath template file path.  (required). 
	 * @param sName template name (required).
	 * @param placeholders list of placeholders (optional)
	 * @return service response
	 */
	public ServerResponse serviceTemplateCreate(String sInputPath, String sName, 
			List<PlaceholderInfo> placeholders)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/template";
			JSONObject jo = new JSONObject();
			jo.put("inputPath", sInputPath);
			jo.putOpt("name", sName);
			if(placeholders != null) {
				JSONArray jarr = new JSONArray();
				for(PlaceholderInfo plc : placeholders)
					jarr.put(plc.toJSON());
				jo.put("placeholders", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new TemplateInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling template create: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to update a template.
	 * @param sTemplateId template id  (required). 
	 * @param sName template name (required).
	 * @param placeholders list of placeholders (optional)
	 * @return service response
	 */
	public ServerResponse serviceTemplateUpdate(String sTemplateId, String sName, 
			List<PlaceholderInfo> placeholders)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/template/" + sTemplateId;
			JSONObject jo = new JSONObject();
			jo.put("templateId", sTemplateId);
			jo.putOpt("name", sName);
			if(placeholders != null) {
				JSONArray jarr = new JSONArray();
				for(PlaceholderInfo plc : placeholders)
					jarr.put(plc.toJSON());
				jo.put("placeholders", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "PATCH", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new TemplateInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling template update: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to delete a template.
	 * @param sTemplateId template id  (required). 
	 * @return service response
	 */
	public ServerResponse serviceTemplateDelete(String sTemplateId)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/template/" + sTemplateId;
			JSONObject jo = new JSONObject();
			jo.put("templateId", sTemplateId);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "DELETE", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new TemplateInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling template delete: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to create a document
	 * Creates a document entity based on template that is given as input and returns the object of the prepared document. PDF is generated when document entity is shared via share method..
	 * @param sTemplateId template id  (required).
	 * @param sOutputPath Output path where the document will be saved after sharing it..  (required). 
	 * @param sName document name (required).
	 * @param fields list of fields (optional)
	 * @return service response
	 */
	public ServerResponse serviceDocumentCreate(String sTemplateId, String sOutputPath, String sName, 
			List<FieldInfo> fields)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/document";
			JSONObject jo = new JSONObject();
			jo.put("template", sTemplateId);
			jo.put("outputPath", sOutputPath);
			jo.putOpt("name", sName);
			if(fields != null) {
				JSONArray jarr = new JSONArray();
				for(FieldInfo fld : fields)
					jarr.put(fld.toJSON());
				jo.put("fields", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "POST", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new DocumentInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling document create: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to update a document
	 * Updates the document and returns the object of the updated document
	 * @param sDocumentId document id  (required).
	 * @param sName document name (required).
	 * @param fields list of fields (optional)
	 * @return service response
	 */
	public ServerResponse serviceDocumentUpdate(String sDocumentId, String sName, List<FieldInfo> fields)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/document/" + sDocumentId;
			JSONObject jo = new JSONObject();
			jo.put("documentId", sDocumentId);
			jo.putOpt("name", sName);
			if(fields != null) {
				JSONArray jarr = new JSONArray();
				for(FieldInfo fld : fields)
					jarr.put(fld.toJSON());
				jo.put("fields", jarr);
			}
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "PATCH", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new DocumentInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling document delete: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	/**
	 * Invokes SignWise services API to delete a document
	 * @param sDocumentId document id  (required).
	 * @return service response
	 */
	public ServerResponse serviceDocumentDelete(String sDocumentId)
	{
		ServerResponse sResp = null;
		try {
			ConfigManager cfg = ConfigManager.instance();
			String sUrl = cfg.getProperty("SERVER_URL") + "/document/" + sDocumentId;
			JSONObject jo = new JSONObject();
			jo.put("documentId", sDocumentId);
			String sRequest = jo.toString();
			if(m_logger.isDebugEnabled())
				m_logger.debug("Calling: " + sUrl + " REQUEST\n---\n" + sRequest + "\n---\n");
			String sResponse = new String(callUrl(sUrl, sRequest, "DELETE", cfg.getIntProperty("SERVER_TIMEOUT", 0)));
			if(m_logger.isDebugEnabled())
				m_logger.debug("RESPONSE\n---\n" + sResponse + "\n---\n");
			if(sResponse != null) {
				jo = new JSONObject(sResponse);
				if(jo != null && jo.opt("id") != null) {
					sResp = new DocumentInfo(jo);
				} else
					sResp = new ErrorResponse(jo);
			}
		} catch(Exception ex) {
			m_logger.error("Error calling document update: " + ex);
			m_logger.error("Trace: " + getTrace(ex));
		}
		return sResp;
	}
	
	private static String getTrace(Throwable ex) 
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}
	
	/**
	 * Reads certificate from stream (FileInputStream or other stream)
	 * @param isCert certificate input stream
	 * @return parsed certificate
	 * @throws CertificateException
	 * @throws IOException
	 */
	public X509Certificate readCertFromStream(InputStream isCert)
			throws CertificateException
	{
		X509Certificate cert = null;
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate)certificateFactory.generateCertificate(isCert);
		} catch(CertificateException ex) {
            throw ex;
        } 
		return cert;
	}
	
	private static final String PEM_HDR1 = "-----BEGIN CERTIFICATE-----\r\n";
    private static final String PEM_HDR2 = "\r\n-----END CERTIFICATE-----";

    /**
     * Writes the cert to pem (base64 format)
     * @param cert certificate
     * @param certFile certificates file name
     * @return true for success
     */
    private byte[] writeCertToPem(X509Certificate cert)
        throws IOException, CertificateEncodingException
    {
    	ByteArrayOutputStream bos = null;
    	byte[] data = null;
        try {
        	bos = new ByteArrayOutputStream();
        	bos.write(PEM_HDR1.getBytes());
        	bos.write(Base64.encodeBase64(cert.getEncoded()));
        	/*byte[] cdata = Base64.encodeBase64(cert.getEncoded());
        	for(int i = 0; i < cdata.length; i += 64) {
        		int j = 64;
        		if(i + j > cdata.length)
        			j = cdata.length - i;
        		bos.write(cdata, i, j);
        		bos.write("\r\n".getBytes());
        	}*/
        	bos.write(PEM_HDR2.getBytes());
        	data = bos.toByteArray();
        	bos.close();
        	bos = null;
        } catch(IOException ex) {
            //DigiDocException.handleException(ex, DigiDocException.ERR_READ_FILE);
        } finally {
        	if(bos != null) {
        		try {
        			bos.close();
        		} catch(Exception ex2) {
        			m_logger.error("Error closing streams: " + ex2);
        		}
        	}
        }
        return data;
    }
    

	/**
	 * Send / receive json request
	 * @param url service access location
	 * @param request json request
	 * @param method http method
	 * @param nTimeout http timout in milliseconds
	 * @return service sresponse
	 * @throws IOException
	 */
	/*private byte[] callUrl(String url, String request, String method, int nTimeout)
			throws IOException
	{
		URL uUrl = new URL(url);
    	if(m_logger.isDebugEnabled())
				m_logger.debug("Connecting to server url: " + url);
    	if(m_keyStore != null) {
    		if(m_logger.isDebugEnabled())
				m_logger.debug("Using keystore " + m_keyStore.getFileName());
    		System.setProperty("javax.net.ssl.keyStore", m_keyStore.getFileName());
    		System.setProperty("javax.net.ssl.keyStorePassword", m_keyStore.getPassword());
    		System.setProperty("javax.net.ssl.keyStoreType", m_keyStore.getType());
    	}
    	if(m_trustStore != null) {
    		if(m_logger.isDebugEnabled())
				m_logger.debug("Using truststore " + m_trustStore.getFileName());
    		System.setProperty("javax.net.ssl.trustStore", m_trustStore.getFileName());
    		System.setProperty("javax.net.ssl.trustStorePassword", m_trustStore.getPassword());
    		System.setProperty("javax.net.ssl.trustStoreType", m_trustStore.getType());
    	}
    	//System.setProperty("javax.net.debug", "ssl");
    	//URLConnection con = uUrl.openConnection();
    	HttpURLConnection conn = (HttpURLConnection) uUrl.openConnection();
    	if("PATCH".equals(method)) {
    		conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
    		conn.setRequestMethod("POST");
    	} else
    		conn.setRequestMethod(method);
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("content-type", "application/json");
    	if(nTimeout >= 0) {
    		if(m_logger.isDebugEnabled())
				m_logger.debug("Setting connection and read timeout to: " + nTimeout + " [ms]");
    		conn.setConnectTimeout(nTimeout);
    		conn.setReadTimeout(nTimeout);
    	}
    	conn.setAllowUserInteraction(false);
    	conn.setUseCaches(false);
    	conn.setDoOutput(true);
    	conn.setDoInput(true);
    	OutputStream os = conn.getOutputStream();
    	os.write(request.getBytes("UTF-8"));
    	os.close();
    	//if (conn.getResponseCode() != 200) 
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		
    	// read the response
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	if (conn != null && conn.getInputStream() != null) {
    		InputStream is = conn.getInputStream();
    		byte[] data = new byte[1024];
    		int nRead = 0;
    		while((nRead = is.read(data)) > 0) {
    			bos.write(data, 0, nRead);
    		}
    		is.close();
    	}
    	if(m_logger.isDebugEnabled())
				m_logger.debug("Received: " + new String(bos.toByteArray()));
    	return bos.toByteArray();
	} */
	
    /**
	 * Send / receive json request
	 * @param url service access location
	 * @param request json request
	 * @param method http method
	 * @param nTimeout http timout in milliseconds
	 * @return service sresponse
	 * @throws IOException
	 */
	private byte[] callUrl(String url, String request, String method, int nTimeout)
			throws IOException, NoSuchAlgorithmException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException
	{
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		//try {
    	if(m_logger.isDebugEnabled())
			m_logger.debug("Connecting to server url: " + url);
    	if(m_logger.isDebugEnabled())
			m_logger.debug("Using keystore " + m_keyStore.getFileName());
    	KeyStore keystore = KeyStore.getInstance(m_keyStore.getType());
    	keystore.load(new FileInputStream(m_keyStore.getFileName()), m_keyStore.getPassword().toCharArray());
    	KeyStore truststore = KeyStore.getInstance(m_trustStore.getType());
    	truststore.load(new FileInputStream(m_trustStore.getFileName()), m_trustStore.getPassword().toCharArray());
    	SSLSocketFactory sslsf = new SSLSocketFactory(keystore, m_keyStore.getPassword(), truststore);
        Scheme https = new Scheme("https", 443, sslsf);
        client.getConnectionManager().getSchemeRegistry().register(https);
        if(m_logger.isDebugEnabled())
			m_logger.debug("Method: " + method + " JSON\n---\n" + request + "\n---\n");
    	if("GET".equals(method)) { 
    		HttpGet get = new HttpGet(url);
    		response = client.execute(get);
    		//System.out.println(get.getStatusLine());
    	} else if("POST".equals(method)) {
    		HttpPost post = new HttpPost(url);
    		post.addHeader("Accept", "application/json");
    		post.addHeader("content-type", "application/json");
    		post.setEntity(new StringEntity(request));
    		response = client.execute(post);
    		//System.out.println(post.getStatusLine());
    	} else if("PATCH".equals(method)) {
    		HttpPatch patch = new HttpPatch(url);
    		patch.setEntity(new StringEntity(request));
    		response = client.execute(patch);
    	} else if("DELETE".equals(method)) {
    		HttpDelete del = new HttpDelete(url);
    		//del.setEntity(new StringEntity(request));
    		response = client.execute(del);
    	} //else
    		
		
		
    	// read the response
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	if (response != null && response.getEntity().getContent() != null) {
    		InputStream is = response.getEntity().getContent();
    		byte[] data = new byte[1024];
    		int nRead = 0;
    		while((nRead = is.read(data)) > 0) {
    			bos.write(data, 0, nRead);
    		}
    		is.close();
    	}
    	if(m_logger.isDebugEnabled())
				m_logger.debug("Received: " + new String(bos.toByteArray()));
		/*} finally {
			
		}*/
    	return bos.toByteArray();
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
    
    
}
