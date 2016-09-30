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
import java.util.Date;


/**
 * Notification info returned by SignWise services
 */
public class NotificationInfo {
	// notification template
	private String m_template;
	// notification send timestamp
	private Date m_sendAt;
	// notification transports type
	private String m_transports;
	
	/**
	 * Constructor for NotificationInfo
	 * @param template notification template.  currently supports only share-reminder
	 * @param sendAt notification send timestamp
	 * @param transports notification transports type. Currently supported transports: “email”
	 */
	public NotificationInfo(String template, Date sendAt, String transports) 
	{
		m_template = template;
		m_sendAt = sendAt;
		m_transports = transports;
	}

	/**
	 * Constructor for NotificationInfo
	 * @param jobj JSON object
	 */
	public NotificationInfo(JSONObject jobj)
	{
		m_template = jobj.optString("template");
		m_transports = jobj.optString("transports");
		long l = jobj.optLong("sendAt");
		if(l > 0)
			m_sendAt = new Date(l);
	}

	/**
	 * Returns notification template
	 * @return notification template
	 */
	public String getTemplate() 
	{ 
		return m_template; 
	}

	/**
	 * Returns notification transports type
	 * @return notification transports type
	 */
	public String getTransports() 
	{ 
		return m_transports; 
	}

	/**
	 * Returns notification send timestamp
	 * @return notification send timestamp
	 */
	public Date getSendAt() 
	{ 
		return m_sendAt; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("template", m_template);
		jobj.putOpt("transports", m_transports);
		if(m_sendAt != null)
			jobj.put("sendAt", m_sendAt.getTime());
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
