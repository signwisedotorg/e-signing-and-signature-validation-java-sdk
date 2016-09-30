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
import java.util.List;
import java.util.ArrayList;


/**
 * Template info returned by SignWise services
 */
public class TemplateInfo 
	extends ServerResponse
{
	// template identifier
	private String m_id; 
	// template name
	private String m_name;
	// list of placeholders
	private List<PlaceholderInfo> m_placeholders;


	/**
	 * Constructor for TemplateInfo
	 * @param id template id
	 * @param name template name
	 */
	public TemplateInfo(String id, String name)
	{
		super(true);
		m_id = id;
		m_name = name;
	}

	/**
	 * Constructor for TemplateInfo
	 * @param jobj JSON object
	 */
	public TemplateInfo(JSONObject jobj)
	{
		super(true);
		m_id = jobj.getString("id");
		m_name = jobj.optString("name");
		JSONArray aPlaceholders = jobj.optJSONArray("placeholders");
		if(aPlaceholders != null) {
			m_placeholders = new ArrayList<PlaceholderInfo>();
			for(int i = 0; i < aPlaceholders.length(); i++) {
				JSONObject jo3 = aPlaceholders.getJSONObject(i);
				PlaceholderInfo plc = new PlaceholderInfo(jo3);
				m_placeholders.add(plc);
			}
		}
	}

	/**
	 * Returns template id
	 * @return template id
	 */
	public String getId() 
	{ 
		return m_id; 
	}

	/**
	 * Returns template name
	 * @return template name
	 */
	public String getName() 
	{ 
		return m_name; 
	}

	/**
	 * Returns list of placeholders
	 * @return list of placeholders
	 */
	public List<PlaceholderInfo> getRecipients() 
	{ 
		return m_placeholders; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("id", m_id);
		jobj.putOpt("name", m_name);
		if(m_placeholders != null) {
			JSONArray aPlaceholders = new JSONArray();
			for(PlaceholderInfo plc : m_placeholders)
				aPlaceholders.put(plc.toJSON());
			jobj.put("placeholders", aPlaceholders);
		}
		return jobj;
	}
}
