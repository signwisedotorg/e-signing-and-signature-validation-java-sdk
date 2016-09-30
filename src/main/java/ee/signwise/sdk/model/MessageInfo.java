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
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Message info returned by SignWise services
 */
public class MessageInfo 
{
	// message language like en-US, ee_ET
	private Map<String, String> m_messages;

	/**
	 * Constructor for MessageInfo
	 * @param lang field name/placeholder
	 * @param message field value
	 */
	public MessageInfo(String lang, String message) 
	{
		m_messages = new TreeMap<String, String>();
		m_messages.put(lang, message);
	}

	/**
	 * Constructor for MessageInfo
	 * @param jobj JSON object
	 */
	public MessageInfo(JSONObject jobj)
	{
		m_messages = new TreeMap<String, String>();
		Iterator<String> it = jobj.keys();
		while(it.hasNext()) {
			String key = it.next();
			String msg = jobj.getString(key);
			m_messages.put(key, msg);
		}
	}

	/**
	 * Returns language codes
	 * @return language codes
	 */
	public Set<String> getLanguages() 
	{ 
		return m_messages.keySet(); 
	}

	/**
	 * Returns message in given language
	 * @param lang language code
	 * @return message in given language
	 */
	public String getMessage(String lang) 
	{ 
		return  m_messages.get(lang); 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		for(String key : m_messages.keySet()) {
			String msg = m_messages.get(key);
			jobj.put(key, msg);
		}
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
