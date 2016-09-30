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
 * Placeholder info returned by SignWise services
 */
public class PlaceholderInfo {
	// placeholder name
	private String m_name;
	// placeholder label
	private String m_label;
	// placeholder type
	private String m_type;

	/**
	 * Constructor for PlaceholderInfo
	 * @param name placeholder name. Placeholder syntax in template file.
	 * @param label placeholder label. Placeholder label value for showing it on the future web application for template editing
	 * @param type placeholder type. Placeholder type is used to mark the placeholder for where the visual signature will be placed if used in share request. Types are: signature, placeholder. The default type is “placeholder”
	 */
	public PlaceholderInfo(String name, String label, String type) 
	{
		m_name = name;
		m_label = label;
		m_type = type;
	}

	/**
	 * Constructor for PlaceholderInfo
	 * @param jobj JSON object
	 */
	public PlaceholderInfo(JSONObject jobj)
	{
		m_name = jobj.optString("placeholder");
		m_label = jobj.optString("label");
		m_type = jobj.optString("type");
	}

	/**
	 * Returns placeholder name
	 * @return placeholder name
	 */
	public String getName() 
	{ 
		return m_name; 
	}

	/**
	 * Returns placeholder label
	 * @return placeholder label
	 */
	public String getLabel() 
	{ 
		return m_label; 
	}

	/**
	 * Returns placeholder type
	 * @return placeholder type
	 */
	public String getType() 
	{ 
		return m_type; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("placeholder", m_name);
		jobj.putOpt("label", m_label);
		jobj.putOpt("type", m_type);
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
