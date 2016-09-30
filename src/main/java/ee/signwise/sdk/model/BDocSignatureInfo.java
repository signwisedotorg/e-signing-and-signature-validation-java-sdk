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
 * BDOC Signature info returned by SignWise services
 */
public class BDocSignatureInfo {
	private String m_id;
	private String m_profile;
	private String m_status;
	// list of errors
	private List<BDocSignatureErrorInfo> m_errors;
	// list of warnings
	private List<BDocSignatureErrorInfo> m_warnings;
	// signing time and date
	private Date m_signingTime;
	// roles/manifests
	private List<String> m_roles;
	// signer location
	private BDocSignaturePlaceInfo m_place;
	// signer certificate info
	private CertificateInfo m_signerCertificate;
	// confirmation 
	private BDocSignatureConfirmationInfo m_confirmation;
	// timestamps
	private List<BDocSignatureTimestampInfo> m_timestamps;
	
	/**
	 * Constructor for BDocSignatureInfo
	 * @param id signature id
	 * @param profile siganture profile
	 * @param status signature status
	*/
	public BDocSignatureInfo(String id, String profile, String status)
	{
		m_id = id;
		m_profile = profile;
		m_status = status;
	}
	
	/**
	 * Constructor for BDocSignatureInfo
	 * @param jobj JSON object
	 */
	public BDocSignatureInfo(JSONObject jobj)
	{
		m_id = jobj.getString("id");
		m_profile = jobj.optString("profile");
		m_status = jobj.optString("status");
		JSONArray aErrors = jobj.optJSONArray("errors");
		if(aErrors != null) {
			m_errors = new ArrayList<BDocSignatureErrorInfo>();
			for(int i = 0; i < aErrors.length(); i++) {
				JSONObject jo3 = aErrors.getJSONObject(i);
				BDocSignatureErrorInfo err = new BDocSignatureErrorInfo(jo3);
				m_errors.add(err);
			}
		}
		JSONArray aWarnings = jobj.optJSONArray("warnings");
		if(aWarnings != null) {
			m_warnings = new ArrayList<BDocSignatureErrorInfo>();
			for(int i = 0; i < aWarnings.length(); i++) {
				JSONObject jo3 = aWarnings.getJSONObject(i);
				BDocSignatureErrorInfo err = new BDocSignatureErrorInfo(jo3);
				m_warnings.add(err);
			}
		}
		long lTime = jobj.optLong("signingTime");
		if(lTime > 0)
			m_signingTime = new Date(lTime);
		JSONArray aRoles = jobj.optJSONArray("roles");
		if(aRoles != null) {
			m_roles = new ArrayList<String>();
			for(int i = 0; i < aRoles.length(); i++) {
				m_roles.add(aRoles.getString(i));
			}
		}
		JSONObject joPlace = jobj.optJSONObject("place");
		if(joPlace != null) 
			m_place = new BDocSignaturePlaceInfo(joPlace);
		JSONObject joCert = jobj.optJSONObject("signerCertificate");
		if(joCert != null) 
			m_signerCertificate = new CertificateInfo(joCert);
		JSONObject joConfirmatiomn = jobj.optJSONObject("confirmation");
		if(joConfirmatiomn != null) 
			m_confirmation = new BDocSignatureConfirmationInfo(joConfirmatiomn);
		JSONArray aTimestamps = jobj.optJSONArray("timestamps");
		if(aTimestamps != null) {
			m_timestamps = new ArrayList<BDocSignatureTimestampInfo>();
			for(int i = 0; i < aTimestamps.length(); i++) {
				JSONObject jo3 = aTimestamps.getJSONObject(i);
				BDocSignatureTimestampInfo err = new BDocSignatureTimestampInfo(jo3);
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
	 * Returns signature profile
	 * @return signature profile
	 */
	public String getProfile() 
	{ 
		return m_profile; 
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
	public List<BDocSignatureErrorInfo> getErrors()
	{
		return m_errors;
	}

	/**
	 * Returns signature warnings
	 * @return signature warnings
	 */
	public List<BDocSignatureErrorInfo> getWarnings()
	{
		return m_warnings;
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
	 * Returns roles / manifests
	 * @return roles / manifests
	 */
	public List<String> getRoles()
	{
		return m_roles;
	}
	
	/**
	 * Returns signers location
	 * @return signers location
	 */
	public BDocSignaturePlaceInfo getPlace()
	{
		return m_place;
	}
	
	/**
	 * Returns signers certificate
	 * @return signers certificate
	 */
	public CertificateInfo getSignerCertificate()
	{
		return m_signerCertificate;
	}
	
	/**
	 * Returns confirmation (OCSP response info)
	 * @return confirmation (OCSP response info)
	 */
	public BDocSignatureConfirmationInfo getConfirmation()
	{
		return m_confirmation;
	}
	
	/**
	 * Returns signature timestamps
	 * @return signature timestamps
	 */
	public List<BDocSignatureTimestampInfo> getTimestamps()
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
		jobj.putOpt("profile", m_profile);
		jobj.putOpt("status", m_status);
		if(m_errors != null && m_errors.size() > 0) {
			JSONArray aErrors = new JSONArray();
			for(BDocSignatureErrorInfo err : m_errors) {
				JSONObject jerr = err.toJSON();
				aErrors.put(jerr);
			}
			jobj.put("errors", aErrors);
		}
		if(m_warnings != null && m_warnings.size() > 0) {
			JSONArray aErrors = new JSONArray();
			for(BDocSignatureErrorInfo err : m_errors) {
				JSONObject jerr = err.toJSON();
				aErrors.put(jerr);
			}
			jobj.put("warnings", aErrors);
		}
		if(m_signingTime != null)
			jobj.put("signingTime", m_signingTime);
		if(m_roles != null && m_roles.size() > 0) {
			JSONArray aRoles = new JSONArray();
			for(String sRole : m_roles) 
				aRoles.put(sRole);
			jobj.put("roles", aRoles);
		}
		if(m_place != null)
			jobj.put("place", m_place.toJSON());
		if(m_signerCertificate != null)
			jobj.put("signerCertificate", m_signerCertificate.toJSON());
		if(m_confirmation != null)
			jobj.put("confirmation", m_confirmation.toJSON());
		if(m_timestamps != null && m_timestamps.size() > 0) {
			JSONArray aTimestamps = new JSONArray();
			for(BDocSignatureTimestampInfo tsi : m_timestamps)
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
