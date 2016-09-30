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
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Holds servers response about one parsed certifiate.
 * This can be returned as the content of /certificate/parse request or 
 * as a part of another bigger response
 */
public class CertificateInfo 
	extends ServerResponse
{
	private X500PrincipalInfo m_issuer;
	private X500PrincipalInfo m_subject;
	private Date m_validFrom;
	private Date m_validTo;
	private String m_serial;
	private String m_personalCode;
	private String m_electronicCode;
	
	/**
	 * Constructor for CertificateInfo
	 * @param issuer
	 * @param subject
	 * @param validFrom
	 * @param validTo
	 * @param serial
	 * @param perCode
	 * @param electronicCode
	 */
	public CertificateInfo(X500PrincipalInfo issuer, X500PrincipalInfo subject, 
			Date validFrom, Date validTo, String serial, 
			String perCode, String electronicCode) 
	{
		super(true);
		m_issuer = issuer;
		m_subject = subject;
		m_validFrom = validFrom;
		m_validTo = validTo;
		m_serial = serial;
		m_personalCode = perCode;
		m_electronicCode = electronicCode;
	}
	
	/**
	 * Constructor for CertificateInfo
	 * @param jobj JSON object
	 */
	public CertificateInfo(JSONObject jobj)
	{
		super(true);
		JSONObject j1 = jobj.getJSONObject("issuer");
		if(j1 != null)
			m_issuer = new X500PrincipalInfo(j1);
		j1 = jobj.getJSONObject("subject");
		if(j1 != null)
			m_subject = new X500PrincipalInfo(j1);
		m_validFrom = new Date(jobj.getLong("validFrom"));
		m_validTo = new Date(jobj.getLong("validTo"));
		m_serial = jobj.getString("serial");
		m_personalCode = jobj.optString("personalCode");
		m_electronicCode = jobj.optString("electronicCode");
	}
	
	/**
	 * Returns certificates issuer info
	 * @return certificates issuer info
	 */
	public X500PrincipalInfo getIssuer() 
	{ 
		return m_issuer; 
	}
	
	/**
	 * Returns certificates subject info
	 * @return certificates subject info
	 */
	public X500PrincipalInfo getSubject() 
	{ 
		return m_subject; 
	}

	/**
	 * Returns certificates validity start timestamp
	 * @return certificates validity start timestamp
	 */
	public Date getValidFrom() 
	{ 
		return m_validFrom; 
	}
	
	/**
	 * Returns certificates validity end timestamp
	 * @return certificates validity end timestamp
	 */
	public Date getValidTo() 
	{ 
		return m_validTo; 
	}
	
	/**
	 * Returns certificates serial number
	 * @return certificates serial number
	 */
	public String getSerial() 
	{ 
		return m_serial; 
	}

	/**
	 * Returns subjects personal code (In Finland HETU)
	 * @return subjects personal code (In Finland HETU)
	 */
	public String getPersonalCode() 
	{ 
		return m_personalCode; 
	}

	/**
	 * Returns subjects personal code (In Finland SATU)
	 * @return subjects personal code (In Finland SATU)
	 */
	public String getElectronicCode() 
	{ 
		return m_electronicCode; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("issuer", m_issuer.toJSON());
		jobj.put("subject", m_subject.toJSON());
		jobj.put("validFrom", m_validFrom);
		jobj.put("validTo", m_validTo);
		jobj.put("serial", m_serial);
		jobj.put("personalCode", m_personalCode);
		jobj.put("electronicCode", m_electronicCode);
		return jobj;
	}
	
}
