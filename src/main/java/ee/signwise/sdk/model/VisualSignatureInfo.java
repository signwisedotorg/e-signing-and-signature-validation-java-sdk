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
import org.json.JSONArray;


/**
 * Visual signature info (pdf) returned by SignWise services
 */
public class VisualSignatureInfo {
	// visual signature type - placeholder or coordinates
	private String m_type; 
	// visual signature placeholder text
	private String m_placeholder;
	private int m_x;
	private int m_y;
	private int m_page;

	/**
	 * Constructor for VisualSignatureInfo
	 * @param type visual signature type
	 * @param placeholder visual signature placeholder text
	 */
	public VisualSignatureInfo(String type, String placeholder, int x, int y, int page) 
	{
		m_type = type;
		m_placeholder = placeholder;
		m_x = x;
		m_y = y;
		m_page = page;
	}

	/**
	 * Constructor for VisualSignatureInfo
	 * @param jobj JSON object
	 */
	public VisualSignatureInfo(JSONObject jobj)
	{
		m_type = jobj.optString("type");
		m_placeholder = jobj.optString("placeholder");
		JSONArray aCoord = jobj.optJSONArray("coordinates");
		if(aCoord != null) {
			m_x = aCoord.optInt(0);
			m_y = aCoord.optInt(1);
		}
		m_page = (int)jobj.optLong("page");
	}

	/**
	 * Returns visual signature type
	 * @return visual signature type
	 */
	public String getType() 
	{ 
		return m_type; 
	}

	/**
	 * Returns visual signature placeholder text
	 * @return visual signature placeholder text
	 */
	public String getPlaceholder() 
	{ 
		return m_placeholder; 
	}

	/**
	 * Returns visual signature x coordinate
	 * @return visual signature x coordinate
	 */
	public int getX() 
	{ 
		return m_x; 
	}

	/**
	 * Returns visual signature y coordinate
	 * @return visual signature y coordinate
	 */
	public int getY() 
	{ 
		return m_y; 
	}

	/**
	 * Returns visual signature page number
	 * @return visual signature page number
	 */
	public int getPage() 
	{ 
		return m_page; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("type", m_type);
		jobj.putOpt("placeholder", m_placeholder);
		if(m_x > 0 || m_y > 0) {
			JSONArray aCoord = new JSONArray();
			aCoord.put(m_x);
			aCoord.put(m_y);
			jobj.put("coordinates", aCoord);
		}
		if(m_page > 0)
			jobj.put("page", m_page);
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
