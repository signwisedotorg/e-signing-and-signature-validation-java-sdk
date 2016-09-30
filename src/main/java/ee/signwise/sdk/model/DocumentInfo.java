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
 * Document info returned by SignWise services
 */
public class DocumentInfo
	extends ServerResponse
{
	// document identifier
	private String m_documentId; 
	// template identifier
	private String m_templateId; 
	// document name
	private String m_name;
	// list of placeholders
	private List<FieldInfo> m_fields;


	/**
	 * Constructor for DocumentInfo
	 * @param documentId document id
	 * @param id templateId id
	 * @param name document name
	 */
	public DocumentInfo(String documentId, String templateId, String name)
	{
		super(true);
		m_documentId = documentId;
		m_templateId = templateId;
		m_name = name;
	}

	/**
	 * Constructor for DocumentInfo
	 * @param jobj JSON object
	 */
	public DocumentInfo(JSONObject jobj)
	{
		super(true);
		m_documentId = jobj.optString("id");
		m_templateId = jobj.optString("templateId");
		m_name = jobj.optString("name");
		JSONArray aFields = jobj.optJSONArray("fields");
		if(aFields != null) {
			m_fields = new ArrayList<FieldInfo>();
			for(int i = 0; i < aFields.length(); i++) {
				JSONObject jo3 = aFields.getJSONObject(i);
				FieldInfo fld = new FieldInfo(jo3);
				m_fields.add(fld);
			}
		}
	}

	/**
	 * Returns document id
	 * @return document id
	 */
	public String getId() 
	{ 
		return m_documentId; 
	}
	
	/**
	 * Returns template id
	 * @return template id
	 */
	public String getTemplateId() 
	{ 
		return m_templateId; 
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
	 * Returns list of fields
	 * @return list of fields
	 */
	public List<FieldInfo> getFields() 
	{ 
		return m_fields; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("id", m_documentId);
		jobj.putOpt("templateId", m_templateId);
		jobj.putOpt("name", m_name);
		if(m_fields != null) {
			JSONArray aFields = new JSONArray();
			for(FieldInfo fld : m_fields)
				aFields.put(fld.toJSON());
			jobj.put("fields", aFields);
		}
		return jobj;
	}
}
