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

import java.util.Date;
import java.util.List;
import java.util.ArrayList;


/**
 * Shareing info returned by SignWise services
 */
public class ShareInfo 
	extends ServerResponse
{
	// Share identifier
	private String m_id; 
	// Share name
	private String m_name;
	// Share description
	private String m_description;
	// Share filename
	private String m_fileName;
	// Share metadata
	private String m_metadata; // object
	// Share creator metadata
	private SharerInfo m_sharer; // object
	// Authorization type (basic/residence)
	private String m_auth; // authorization: basic/residence
	// Callback URL
	private String m_callbackURL;
	// Share creation date. UNIX timestamp with milliseconds
	private Date m_created;
	// Share expiration date. UNIX timestamp with milliseconds
	private Date m_expires;
	// List of recipients
	private List<RecipientInfo> m_recipients; // array
	// Share type (sequential/parallel)
	private String m_type;
	// Share status (active/finished/expired/deleted)
	private String m_status;
	// list of notifications
	private List<NotificationInfo> m_notifications;
	
	
	/**
	 * Constructor for ShareInfo
	 * @param id share id
	 * @param name share name
	 * @param description share description
	*/
	public ShareInfo(String id, String name, String description)
	{
		super(true);
		m_id = id;
		m_name = name;
		m_description = description;
	}
	
	/**
	 * Constructor for ShareInfo
	 * @param jobj JSON object
	 */
	public ShareInfo(JSONObject jobj)
	{
		super(true);
		m_id = jobj.getString("id");
		m_name = jobj.optString("name");
		m_description = jobj.optString("description");
		m_fileName = jobj.optString("fileName");
		m_metadata = jobj.optString("metadata");
		JSONObject jo2 = jobj.optJSONObject("sharer");
		if(jo2 != null)
			m_sharer = new SharerInfo(jo2);
		m_auth = jobj.optString("auth");
		m_callbackURL = jobj.optString("callbackURL");
		long l = jobj.optLong("created");
		if(l > 0)
			m_created = new Date(l);
		l = jobj.optLong("expires");
		if(l > 0)
			m_expires = new Date(l);
		JSONArray aRecipients = jobj.optJSONArray("recipients");
		if(aRecipients != null) {
			m_recipients = new ArrayList<RecipientInfo>();
			for(int i = 0; i < aRecipients.length(); i++) {
				JSONObject jo3 = aRecipients.getJSONObject(i);
				RecipientInfo recv = new RecipientInfo(jo3);
				m_recipients.add(recv);
			}
		}
		m_type = jobj.optString("type");
		m_status = jobj.optString("status");
		JSONArray aNotifications = jobj.optJSONArray("notifications");
		if(aNotifications != null) {
			m_notifications = new ArrayList<NotificationInfo>();
			for(int i = 0; i < aNotifications.length(); i++) {
				JSONObject jo3 = aNotifications.getJSONObject(i);
				NotificationInfo not = new NotificationInfo(jo3);
				m_notifications.add(not);
			}
		}
	}
	
	/**
	 * Returns share id
	 * @return share id
	 */
	public String getId() 
	{ 
		return m_id; 
	}

	/**
	 * Returns share name
	 * @return share name
	 */
	public String getName() 
	{ 
		return m_name; 
	}

	/**
	 * Returns share description
	 * @return share description
	 */
	public String getDescription() 
	{ 
		return m_description; 
	}

	/**
	 * Returns share filename
	 * @return share filename
	 */
	public String getFileName() 
	{ 
		return m_fileName; 
	}

	/**
	 * Returns share metadata
	 * @return share metadata
	 */
	public String getMetadata() 
	{ 
		return m_metadata; 
	}

	/**
	 * Returns share creator metadata
	 * @return share creator metadata
	 */
	public SharerInfo getSharer() 
	{ 
		return m_sharer; 
	}

	/**
	 * Returns authorization type (basic/residence)
	 * @return authorization type (basic/residence)
	 */
	public String getAuth() 
	{ 
		return m_auth; 
	}
	
	/**
	 * Returns callback URL
	 * @return callback URL
	 */
	public String getCallbackURL() 
	{ 
		return m_callbackURL; 
	}
	
	/**
	 * Returns share creation date. UNIX timestamp with milliseconds
	 * @return share creation date. UNIX timestamp with milliseconds
	 */
	public Date getCreated() 
	{ 
		return m_created; 
	}
	
	/**
	 * Returns share expiration date. UNIX timestamp with milliseconds
	 * @return share expiration date. UNIX timestamp with milliseconds
	 */
	public Date getExpires() 
	{ 
		return m_expires; 
	}
	
	/**
	 * Returns list of recipients
	 * @return list of recipients
	 */
	public List<RecipientInfo> getRecipients() 
	{ 
		return m_recipients; 
	}

	/**
	 * Returns share type (sequential/parallel)
	 * @return share type (sequential/parallel)
	 */
	public String getType() 
	{ 
		return m_type; 
	}

	/**
	 * Returns share status (active/finished/expired/deleted)
	 * @return share status (active/finished/expired/deleted)
	 */
	public String getStatus() 
	{ 
		return m_status; 
	}

	/**
	 * Returns share notifications
	 * @return share notifications
	 */
	public  List<NotificationInfo> getNotifications()
	{
		return m_notifications;
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
		jobj.putOpt("description", m_description);
		jobj.putOpt("fileName", m_fileName);
		jobj.putOpt("metadata", m_metadata);
		if(m_sharer != null)
			jobj.put("sharer", m_sharer.toJSON());
		jobj.putOpt("auth", m_auth);
		jobj.putOpt("callbackURL", m_callbackURL);
		jobj.putOpt("created", m_created);
		jobj.putOpt("expires", m_expires);
		if(m_recipients != null) {
			JSONArray aRecipients = new JSONArray();
			for(RecipientInfo recv : m_recipients)
				aRecipients.put(recv.toJSON());
			jobj.put("recipients", aRecipients);
		}
		if(m_notifications != null) {
			JSONArray aNotifications = new JSONArray();
			for(NotificationInfo not : m_notifications)
				aNotifications.put(not.toJSON());
			jobj.put("notifications", aNotifications);
		}
		jobj.putOpt("type", m_type);
		jobj.putOpt("status", m_status);
		return jobj;
	}


}



