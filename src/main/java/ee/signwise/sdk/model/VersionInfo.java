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
 * Holds version info returned by SignWise service
 */
public class VersionInfo
	extends ServerResponse
{
	private String m_name;
	private String m_version;
	
	/**
	 * Constructor for VersionInfo
	 * @param name service name
	 * @param ver service version number
	 */
	public VersionInfo(String name, String ver) 
	{
		super(true);
		m_name = name;
		m_version = ver;
	}
	
	/**
	 * Constructor for VersionInfo
	 * @param jobj JSON object
	 */
	public VersionInfo(JSONObject jobj)
	{
		super(true);
		m_name = jobj.optString("name");
		m_version = jobj.optString("version");
	}
	
	/**
	 * Returns service name
	 * @return service name
	 */
	public String getName() 
	{ 
		return m_name; 
	}
	
	/**
	 * Returns service version number
	 * @return service version number
	 */
	public String getVersion() 
	{ 
		return m_version; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("name", m_name);
		jobj.put("version", m_version);
		return jobj;
	}
	
}

