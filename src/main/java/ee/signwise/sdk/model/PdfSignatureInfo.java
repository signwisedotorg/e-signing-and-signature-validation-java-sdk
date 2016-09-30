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
package ee.signwise.sdk.model;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;


/**
 * PDF Signature info returned by SignWise services
 */
public class PdfSignatureInfo {
	private String m_id;
	private boolean m_documentTimestamp;
	private boolean m_ltv;
	private String m_status;
	// list of errors
	private List<PdfSignatureErrorInfo> m_errors;
	// signing time and date
	private Date m_signingTime;
	// role / reason
	private String m_reason;
	private String m_location;
	private String m_level;
	private String m_format;
	// signer certificate info
	private CertificateInfo m_certificate;
	// confirmation 
	private PdfSignatureConfirmationInfo m_confirmation;
	// timestamps
	private List<PdfSignatureTimestampInfo> m_timestamps;
	
	
	
	/**
	 * Constructor for PdfSignatureInfo
	 * @param id signature id
	 * @param status signature status
	*/
	public PdfSignatureInfo(String id, String status)
	{
		m_id = id;
		m_status = status;
	}
	
	/**
	 * Constructor for PdfSignatureInfo
	 * @param jobj JSON object
	 */
	public PdfSignatureInfo(JSONObject jobj)
	{
		m_id = jobj.getString("id");
		m_documentTimestamp = jobj.optBoolean("documentTimestamp");
		m_status = jobj.optString("status");
		m_ltv = jobj.optBoolean("ltv");
		m_reason = jobj.optString("reason");
		m_location = jobj.optString("location");
		m_level = jobj.optString("level");
		m_format = jobj.optString("format");
		JSONArray aErrors = jobj.optJSONArray("errors");
		if(aErrors != null) {
			m_errors = new ArrayList<PdfSignatureErrorInfo>();
			for(int i = 0; i < aErrors.length(); i++) {
				JSONObject jo3 = aErrors.getJSONObject(i);
				PdfSignatureErrorInfo err = new PdfSignatureErrorInfo(jo3);
				m_errors.add(err);
			}
		}
		long lTime = jobj.optLong("signingTime");
		if(lTime > 0)
			m_signingTime = new Date(lTime);
		JSONObject joCert = jobj.optJSONObject("signerCertificate");
		if(joCert != null) 
			m_certificate = new CertificateInfo(joCert);
		JSONObject joConfirmatiomn = jobj.optJSONObject("confirmation");
		if(joConfirmatiomn != null) 
			m_confirmation = new PdfSignatureConfirmationInfo(joConfirmatiomn);
		JSONArray aTimestamps = jobj.optJSONArray("timestamps");
		if(aTimestamps != null) {
			m_timestamps = new ArrayList<PdfSignatureTimestampInfo>();
			for(int i = 0; i < aTimestamps.length(); i++) {
				JSONObject jo3 = aTimestamps.getJSONObject(i);
				PdfSignatureTimestampInfo err = new PdfSignatureTimestampInfo(jo3);
				m_timestamps.add(err);
			}
		}
	}
	
	/**
	 * Returns signature id
	 * @return signature id
	 */
	public String getId() 
	{ 
		return m_id; 
	}
	
	/**
	 * Returns signature documentTimestamp atribute
	 * @return signature documentTimestamp atribute
	 */
	public boolean getDocumentTimestamp() 
	{ 
		return m_documentTimestamp; 
	}
	
	/**
	 * Returns signature ltv atribute
	 * @return signature ltv atribute
	 */
	public boolean getLtv() 
	{ 
		return m_ltv; 
	}
	
	/**
	 * Returns signature status
	 * @return signature status
	 */
	public String getStatus() 
	{ 
		return m_status; 
	}
	
	/**
	 * Returns signature errors
	 * @return signature errors
	 */
	public List<PdfSignatureErrorInfo> getErrors()
	{
		return m_errors;
	}

	/**
	 * Returns signing timestamp
	 * @return signing timestamp
	 */
	public Date getSigningTime() 
	{
		return m_signingTime;
	}
	
	/**
	 * Returns reason / manifest
	 * @return reason / manifest
	 */
	public String getReason()
	{
		return m_reason;
	}
	
	/**
	 * Returns signer location
	 * @return signer location
	 */
	public String getLocation()
	{
		return m_location;
	}
	
	/**
	 * Returns signature level
	 * @return signature level
	 */
	public String getLevel()
	{
		return m_level;
	}
	
	/**
	 * Returns signature format
	 * @return signature format
	 */
	public String getFormat()
	{
		return m_format;
	}
	
	/**
	 * Returns signers certificate
	 * @return signers certificate
	 */
	public CertificateInfo getSignerCertificate()
	{
		return m_certificate;
	}
	
	/**
	 * Returns confirmation (OCSP response info)
	 * @return confirmation (OCSP response info)
	 */
	public PdfSignatureConfirmationInfo getConfirmation()
	{
		return m_confirmation;
	}
	
	/**
	 * Returns signature timestamps
	 * @return signature timestamps
	 */
	public List<PdfSignatureTimestampInfo> getTimestamps()
	{
		return m_timestamps;
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("id", m_id);
		if(m_documentTimestamp)
			jobj.put("documentTimestamp", m_documentTimestamp);
		if(m_ltv)
			jobj.put("ltv", m_ltv);
		jobj.putOpt("status", m_status);
		jobj.putOpt("reason", m_reason);
		jobj.putOpt("location", m_location);
		jobj.putOpt("level", m_level);
		jobj.putOpt("format", m_format);
		if(m_errors != null && m_errors.size() > 0) {
			JSONArray aErrors = new JSONArray();
			for(PdfSignatureErrorInfo err : m_errors) {
				JSONObject jerr = err.toJSON();
				aErrors.put(jerr);
			}
			jobj.put("errors", aErrors);
		}
		if(m_signingTime != null)
			jobj.put("signingTime", m_signingTime);
		
		if(m_certificate != null)
			jobj.put("signerCertificate", m_certificate.toJSON());
		if(m_confirmation != null)
			jobj.put("confirmation", m_confirmation.toJSON());
		if(m_timestamps != null && m_timestamps.size() > 0) {
			JSONArray aTimestamps = new JSONArray();
			for(PdfSignatureTimestampInfo tsi : m_timestamps)
				aTimestamps.put(tsi.toJSON());
			jobj.put("timestamps", aTimestamps);
		}
		return jobj;
	}

	/**
	 * Returns stringified form for info
	 * @return objects stringified form
	 */
	public String toString()
	{
		return toJSON().toString();
	}
	
}
