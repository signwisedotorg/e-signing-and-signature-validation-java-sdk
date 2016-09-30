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
import java.util.Date;


/**
 * PDF Signature confirmation info returned by SignWise services
 */
public class PdfSignatureConfirmationInfo {
	private String m_responderId;
	private Date m_producedAt;
	private CertificateInfo m_certificate;
		
		
	/**
	 * Constructor for PdfSignatureConfirmationInfo
	 * @param responderId OCSP responder id
	 * @param producedAt OCSP response producedAt time
	*/
	public PdfSignatureConfirmationInfo(String responderId, Date producedAt)
	{
		m_responderId = responderId;
		m_producedAt = producedAt;
	}
	
	/**
	 * Constructor for PdfSignatureConfirmationInfo
	 * @param jobj JSON object
	 */
	public PdfSignatureConfirmationInfo(JSONObject jobj)
	{
		m_responderId = jobj.optString("responderId");
		long lTime = jobj.optLong("producedAt");
		if(lTime > 0)
			m_producedAt = new Date(lTime);
		JSONObject joCert = jobj.optJSONObject("certificate");
		if(joCert != null) 
			m_certificate = new CertificateInfo(joCert);
	}
	
	/**
	 * Returns OCSP responder id
	 * @return OCSP responder id
	 */
	public String getResponderId() 
	{ 
		return m_responderId; 
	}
	
	/**
	 * Returns OCSP response producedAt time
	 * @return OCSP response producedAt time
	 */
	public Date getProducedAt() 
	{ 
		return m_producedAt; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("responderId", m_responderId);
		if(m_producedAt != null)
			jobj.put("producedAt", m_producedAt);
		if(m_certificate != null)
			jobj.put("certificate", m_certificate.toJSON());
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

