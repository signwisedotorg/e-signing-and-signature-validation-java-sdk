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
 * BDOC Signature timestamp info returned by SignWise services
 */
public class BDocSignatureTimestampInfo {
	private String m_id;
	private int m_type;
	private String m_serialNumber;
	private Date m_created;
	private String m_policy;
	private String m_errorBound;
	private boolean m_ordered;
	private String m_tsa;
	private CertificateInfo m_certificate;
		
		
	/**
	 * Constructor for BDocSignatureTimestampInfo
	 * @param id timestamp id
	 * @param created timestamp creation time
	*/
	public BDocSignatureTimestampInfo(String id, Date created)
	{
		m_id = id;
		m_created = created;
	}
	
	/**
	 * Constructor for BDocSignatureTimestampInfo
	 * @param jobj JSON object
	 */
	public BDocSignatureTimestampInfo(JSONObject jobj)
	{
		m_id = jobj.optString("id");
		m_type = jobj.optInt("type");
		m_serialNumber = jobj.optString("serialNumber");
		long lTime = jobj.optLong("created");
		if(lTime > 0)
			m_created = new Date(lTime);
		m_policy = jobj.optString("policy");
		m_errorBound = jobj.optString("errorBound");
		m_ordered = jobj.optBoolean("ordered");
		m_tsa = jobj.optString("tsa");
		JSONObject joCert = jobj.optJSONObject("certificate");
		if(joCert != null) 
			m_certificate = new CertificateInfo(joCert);
	}
	
	/**
	 * Returns timestamp id
	 * @return timestamp id
	 */
	public String getId() 
	{ 
		return m_id; 
	}

	/**
	 * Returns timestamp type
	 * @return timestamp type
	 */
	public int getType() 
	{ 
		return m_type; 
	}

	/**
	 * Returns timestamp serial number
	 * @return timestamp serial number
	 */
	public String getSerialNumber() 
	{ 
		return m_serialNumber; 
	}

	
	/**
	 * Returns timestamp creation time
	 * @return timestamp creation time
	 */
	public Date getCreatedAt() 
	{ 
		return m_created; 
	}
	
	/**
	 * Returns timestamp policy
	 * @return timestamp policy
	 */
	public String getPolicy() 
	{ 
		return m_policy; 
	}
	
	/**
	 * Returns timestamp error bound
	 * @return timestamp error bound
	 */
	public String getErrorBound() 
	{ 
		return m_errorBound; 
	}
	
	/**
	 * Returns timestamp ordered
	 * @return timestamp ordered
	 */
	public boolean getOrdered() 
	{ 
		return m_ordered; 
	}
	
	/**
	 * Returns timestamp tsa
	 * @return timestamp tsa
	 */
	public String getTsa() 
	{ 
		return m_tsa; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("id", m_id);
		if(m_type > 0)
			jobj.put("type", m_type);
		jobj.putOpt("serialNumber", m_serialNumber);
		jobj.putOpt("ipolicy", m_policy);
		jobj.putOpt("errorBound", m_errorBound);
		if(m_ordered)
		jobj.put("ordered", m_ordered);
		jobj.putOpt("tsa", m_tsa);
		if(m_created != null)
			jobj.put("created", m_created);
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
