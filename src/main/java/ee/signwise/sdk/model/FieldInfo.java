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
 * Field info returned by SignWise services
 */
public class FieldInfo 
{
	// field name/placeholder
	private String m_name;
	// field value
	private String m_value;

	/**
	 * Constructor for FieldInfo
	 * @param name field name/placeholder
	 * @param value field value
	 */
	public FieldInfo(String name, String value) 
	{
		m_name = name;
		m_value = value;
	}

	/**
	 * Constructor for FieldInfo
	 * @param jobj JSON object
	 */
	public FieldInfo(JSONObject jobj)
	{
		m_name = jobj.optString("placeholder");
		m_value = jobj.optString("value");
	}

	/**
	 * Returns field name
	 * @return field name
	 */
	public String getName() 
	{ 
		return m_name; 
	}

	/**
	 * Returns field value
	 * @return field value
	 */
	public String getValue() 
	{ 
		return m_value; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("placeholder", m_name);
		jobj.putOpt("value", m_value);
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
