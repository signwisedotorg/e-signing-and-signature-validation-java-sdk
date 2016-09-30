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
 * Abstract base class on SignWise server responses
 */
public abstract class ServerResponse {
	private boolean m_bSuccess;
	
	/**
	 * Constructor for ServerResponse
	 * @param bSuccess true for success
	 */
	public ServerResponse(boolean bSuccess)
	{
		m_bSuccess = bSuccess;
	}

	/**
	 * Returns server response status
	 * @return true if successful
	 */
	public boolean isSuccess() 
	{ 
		return m_bSuccess; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("success", m_bSuccess);
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
