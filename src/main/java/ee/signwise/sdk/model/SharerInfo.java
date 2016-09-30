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
 * Sharer info returned by SignWise services
 */
public class SharerInfo {
	// sharer name
	private String m_name; 
	// sharer email
	private String m_email;
	
	
	/**
	 * Constructor for SharerInfo
	 * @param name sharer name
	 * @param email sharer email
	 */
	public SharerInfo(String name, String email) 
	{
		m_name = name;
		m_email = email;
	}
	
	/**
	 * Constructor for SharerInfo
	 * @param jobj JSON object
	 */
	public SharerInfo(JSONObject jobj)
	{
		m_name = jobj.optString("name");
		m_email = jobj.optString("email");
	}
	
	/**
	 * Returns sharer name
	 * @return sharer name
	 */
	public String getName() 
	{ 
		return m_name; 
	}
	
	/**
	 * Returns sharer email
	 * @return sharer email
	 */
	public String getEmail() 
	{ 
		return m_email; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("name", m_name);
		jobj.putOpt("email", m_email);
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
