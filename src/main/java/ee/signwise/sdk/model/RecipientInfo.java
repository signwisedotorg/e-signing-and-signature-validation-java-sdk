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
import java.util.Date;


/**
 * Recipient info returned by SignWise services
 */
public class RecipientInfo 
	extends ServerResponse
{
	// Share identifier
	private String m_id; 
	// Unique recipient share key
	private String m_key;
	// Recipient name
	private String m_name;
	// Recipient message (etc. decline message)
	private String m_userMessage;
	// Recipient Signlink URL
	private String m_signlinkURL;
	// Redirect URL
	private String m_redirectURL;
	// Recipient email
	private String m_email;
	// Recipient msisdn/mobile phone number
	private String m_msisdn; // nr
	// Recipient social security number
	private String m_ssn;
	// Recipient language
	private String m_language;
	// Recipient country. Required if ssn is set
	private String m_country;
	// Recipient tmp path
	private String m_tmpPath;
	// Blind copy flag
	private boolean m_bcc;
	// Reason field flag
	private boolean m_disableReasonField;
	// Recipient modification date. UNIX timestamp with milliseconds
	private Date m_modified;
	// Recipient modification date. UNIX timestamp with milliseconds
	private Date m_expires;
	// Recipient status (active/queued/declined/signed/deleted/expired)
	private String m_status;
	// Recipient role (signer/viewer)
	private String m_role;
	// Recipient group. Defaults to 0. Used and required by sequential share.
	private int m_group;
	// visual signature info
	private VisualSignatureInfo m_visualSignature;
	// list of notifications
	private List<NotificationInfo> m_notifications;
		
	/**
	 * Constructor for RecipientInfo (to be used for sharet-create or update operations)
	 * @param id recipient id (optional) required to update recipient record
	 * @param name recipient name. (optional)  Example: Mati Kuusk
	 * @param key Unique recipient share key. (optional)  Example: recipientKey1
	 * @param redirectURL recipient redirect url (optional) Example: http://sl.singwise.org/sw/f5424125-6e0b-4d1e-9a36-ac618b62aaec
	 * @param email  recipient email. (optional) Example: recipient@recipient.com. Required if one of the notifications requires “email” transport.
	 * @param msisdn recipient phone number (optional) Example: 372192304554. Required if one of the notifications requires “sms” transport. Accepts numeric values only (without special characters).
	 * @param ssn recipient social security number. (optional) Example: 14212128025
	 * @param language recipient language. (required). Supported languages: et-EE, en-EE, ru-EE, en-LV, ru-LV, lv-LV, en-LT, ru-LT, lt-LT, en-AZ, az-AZ, en-FI, fi-FI.
	 * @param country recipient country. (required). Required if ssn is set. Supported countries: EE, LV, LT, AZ, FI
	 * @param tmpPath recipient temporary path (required). Example: http://fp/path/to/mati-tmp
	 * @param bcc blind copy flag. (optional). If set to true then recipient only sees himself and other recipients doesn’t see him.
	 * @param group recipient group. (optional) Defaults to 0. Used and required by sequential share.
	 * @param expires expiration date. (optional). UNIX timestamp with milliseconds.
	 * @param role recipient role (optional). (signer/viewer). Defaults to signer.
	 * @param status recipient status (optional) (active/queued/declined/signed/deleted/expired).
	 * @param disableReasonField reason field switch. (optional) If set to true then recipient cannot enter reason field in Signlink.
	 * @param visualSignature visual signature info for pdf (optional)
	 * @param notifications notifications for this recipient (optional)
	*/
	public RecipientInfo(String id, String name, String key, String redirectURL, String email, String msisdn, 
			String ssn, String language, String country, String tmpPath, boolean bcc, int group,
			Date expires, String role, String status, boolean disableReasonField,
			VisualSignatureInfo visualSignature, List<NotificationInfo> notifications)
	{
		super(true);
		m_id = id;
		m_name = name;
		m_key = key;
		m_redirectURL = redirectURL;
		m_email = email;
		m_msisdn = msisdn;
		m_ssn = ssn;
		m_language = language;
		m_country = country;
		m_tmpPath = tmpPath;
		m_bcc = bcc;
		m_disableReasonField = disableReasonField;
		m_expires = expires;
		m_status = status;
		m_role = role;
		m_group = group;
		m_visualSignature = visualSignature;
		m_notifications = notifications;
	}
	
	/**
	 * Constructor for RecipientInfo
	 * @param jobj JSON object
	 */
	public RecipientInfo(JSONObject jobj)
	{
		super(true);
		m_id = jobj.getString("id");
		m_key = jobj.optString("key");
		m_name = jobj.optString("name");
		m_userMessage = jobj.optString("userMessage");
		m_signlinkURL = jobj.optString("signlinkURL");
		m_redirectURL = jobj.optString("redirectURL");
		m_email = jobj.optString("email");
		m_msisdn = jobj.optString("msisdn");
		m_ssn = jobj.optString("ssn");
		m_language = jobj.optString("language");
		m_country = jobj.optString("country");
		m_tmpPath = jobj.optString("tmpPath");
		m_bcc = jobj.optBoolean("bcc");
		m_disableReasonField = jobj.optBoolean("disableReasonField");
		long l = jobj.optLong("modified");
		if(l > 0)
			m_modified = new Date(l);
		l = jobj.optLong("expires");
		if(l > 0)
			m_expires = new Date(l);
		m_role = jobj.optString("role");
		m_group = (int)jobj.optLong("group");
		JSONObject jo2 = jobj.optJSONObject("visualSignature");
		if(jo2 != null)
			m_visualSignature = new VisualSignatureInfo(jo2);
		JSONArray aNotifications = jobj.optJSONArray("notifications");
		if(aNotifications != null) {
			m_notifications = new ArrayList<NotificationInfo>();
			for(int i = 0; i < aNotifications.length(); i++) {
				JSONObject jo3 = aNotifications.getJSONObject(i);
				NotificationInfo not = new NotificationInfo(jo3);
				m_notifications.add(not);
			}
		}
		m_status = jobj.optString("status");
	}
	
	/**
	 * Returns share identifier
	 * @return share identifier
	 */
	public String getId() 
	{ 
		return m_id; 
	}
	
	
	/**
	 * Returns unique recipient share key
	 * @return unique recipient share key
	 */
	public String getKey() 
	{ 
		return m_key; 
	}
	
	/**
	 * Clears the key. Used before update operations as key must be null since it may not be updated
	 */
	public void clearKey()
	{
		m_key = null;
	}
	
	/**
	 * Returns recipient name
	 * @return recipient name
	 */
	public String getName() 
	{ 
		return m_name; 
	}
	
	/**
	 * Returns recipient message
	 * @return recipient message
	 */
	public String getUserMessage() 
	{ 
		return m_userMessage; 
	}
	
	/**
	 * Returns signlink URL
	 * @return signlink URL
	 */
	public String getSignlinkURL() 
	{ 
		return m_signlinkURL; 
	}
	
	/**
	 * Returns redirect URL
	 * @return redirect URL
	 */
	public String getRedirectURL() 
	{ 
		return m_redirectURL; 
	}
	
	/**
	 * Returns recipient email address
	 * @return recipient email address
	 */
	public String getEmail() 
	{ 
		return m_email; 
	}
	
	/**
	 * Returns recipient msisdn/mobile phone number
	 * @return recipient msisdn/mobile phone number
	 */
	public String getMsisdn() 
	{ 
		return m_msisdn; 
	}
	
	/**
	 * Returns recipient social security number
	 * @return recipient social security number
	 */
	public String getSsn() 
	{ 
		return m_ssn; 
	}
	
	/**
	 * Returns recipient language
	 * @return recipient language
	 */
	public String getLanguage() 
	{ 
		return m_language; 
	}
	
	/**
	 * Returns recipient country
	 * @return recipient country
	 */
	public String getCountry() 
	{ 
		return m_country; 
	}
	
	/**
	 * Returns recipient tmp path
	 * @return recipient tmp path
	 */
	public String getTmpPath() 
	{ 
		return m_tmpPath; 
	}
	
	/**
	 * Returns blind copy flag
	 * @return blind copy flag
	 */
	public boolean getBcc() 
	{ 
		return m_bcc; 
	}
	
	/**
	 * Returns reason field flag
	 * @return reason field flag
	 */
	public boolean getDisableReasonField() 
	{ 
		return m_disableReasonField; 
	}
	
	/**
	 * Returns recipient modification timestamp
	 * @return recipient modification timestamp
	 */
	public Date getModified() 
	{ 
		return m_modified; 
	}
	
	/**
	 * Returns recipient expiration timestamp
	 * @return recipient expiration timestamp
	 */
	public Date getExpires() 
	{ 
		return m_expires; 
	}
	
	/**
	 * Returns recipient status
	 * @return recipient status
	 */
	public String getStatus() 
	{ 
		return m_status; 
	}
	
	/**
	 * Returns recipient role
	 * @return recipient role
	 */
	public String getRole() 
	{ 
		return m_role; 
	}
	
	/**
	 * Returns recipient group
	 * @return recipient group
	 */
	public int getGroup() 
	{ 
		return m_group; 
	}
	
	/**
	 * Returns recipient notifications
	 * @return recipient notifications
	 */
	public  List<NotificationInfo> getNotifications()
	{
		return m_notifications;
	}
	
	/**
	 * Returns recipient visual signature info
	 * @return recipient visual signature info
	 */
	public VisualSignatureInfo getVisualSignature() 
	{
		return m_visualSignature;
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("id", m_id);
		jobj.putOpt("key", m_key);
		jobj.putOpt("name", m_name);
		jobj.putOpt("userMessage", m_userMessage);
		jobj.putOpt("signlinkURL", m_signlinkURL);
		jobj.putOpt("redirectURL", m_redirectURL);
		jobj.putOpt("email", m_email);
		jobj.putOpt("msisdn", m_msisdn);
		jobj.putOpt("ssn", m_ssn);
		jobj.putOpt("language", m_language);
		jobj.putOpt("country", m_country);
		jobj.putOpt("tmpPath", m_tmpPath);
		if(m_bcc)
			jobj.put("bcc", m_bcc);
		if(m_disableReasonField)
			jobj.put("disableReasonField", m_disableReasonField);
		if(m_modified != null)
			jobj.put("modified", m_modified.getTime());
		if(m_expires != null)
			jobj.put("expires", m_expires.getTime());
		jobj.putOpt("role", m_role);
		if(m_group > 0)
			jobj.put("group", m_group);
		if(m_visualSignature != null)
			jobj.put("visualSignature", m_visualSignature.toJSON());
		if(m_notifications != null) {
			JSONArray aNotifications = new JSONArray();
			for(NotificationInfo not : m_notifications)
				aNotifications.put(not.toJSON());
			jobj.put("notifications", aNotifications);
		}
		jobj.putOpt("status", m_status);
		return jobj;
	}

}

