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

/**
 * Holds servers response certificates status after an OCSP request
 */
public class CertificateOcsp 
	extends ServerResponse
{
	private String m_status;
	
	/**
	 * Constructor for CertificateOcsp
	 * @param status certificates OCSP status
	 */
	public CertificateOcsp(String status)
	{
		super(true);
		m_status = status;
	}
	
	/**
	 * Constructor for CertificateOcsp
	 * @param jobj JSON object
	 */
	public CertificateOcsp(JSONObject jobj)
	{
		super(true);
		m_status = jobj.getString("status");
	}
	
	/**
	 * Returns certificates OCSP status
	 * @return certificates OCSP status
	 */
	public String getStatus() 
	{ 
		return m_status; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("status", m_status);
		return jobj;
	}
}
