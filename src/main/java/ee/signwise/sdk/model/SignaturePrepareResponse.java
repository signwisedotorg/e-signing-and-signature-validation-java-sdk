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
 * Signature prepare response returned by SignWise services
 */
public class SignaturePrepareResponse 
	extends ServerResponse
{
	private String m_sigId;
	private String m_digest;
	private String m_digestType;
	
	
	/**
	 * Constructor for SignaturePrepareResponse
	 * @param id signature id
	 * @param digest generated digest to sign
	 * @param type digest type
	 */
	public SignaturePrepareResponse(String id, String digest, String type)
	{
		super(true);
		m_sigId = id;
		m_digest = digest;
		m_digestType = type;
	}
	
	/**
	 * Constructor for SignaturePrepareResponse
	 * @param jobj JSON object
	 */
	public SignaturePrepareResponse(JSONObject jobj)
	{
		super(true);
		m_sigId = jobj.getString("signatureId");
		m_digest = jobj.getString("digest");
		m_digestType = jobj.getString("digestType");
	}
	

	/**
	 * Returns new signature id
	 * @return signature id
	 */
	public String getId() 
	{ 
		return m_sigId; 
	}

	/**
	 * Returns digest to sign
	 * @return digets to sign
	 */
	public String getDigest() 
	{ 
		return m_digest; 
	}
	
	/**
	 * Returns digest type
	 * @return digest type
	 */
	public String getDigestType() 
	{ 
		return m_digestType; 
	}
	

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("signatureId", m_sigId);
		jobj.put("digest", m_digest);
		jobj.put("digestType", m_digestType);		
		return jobj;
	}
}
