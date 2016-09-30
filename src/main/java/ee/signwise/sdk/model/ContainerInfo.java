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
 * Container info returned by SignWise services
 */
public class ContainerInfo 
	extends ServerResponse
{
	private String m_id;
	private String m_user;
	private String m_path;
	private String m_type;
	private boolean m_locked;
	private Date m_lockModified;
	
	/**
	 * Constructor for ContainerInfo
	 * @param id container id
	 * @param user container creators userid
	 * @param path containers path
	 * @param type containers type
	 */
	public ContainerInfo(String id, String user, String path, String type)
	{
		super(true);
		m_id = id;
		m_user = user;
		m_path = path;
		m_type = type;
	}
	
	/**
	 * Constructor for ContainerInfo
	 * @param jobj JSON object
	 */
	public ContainerInfo(JSONObject jobj)
	{
		super(true);
		m_id = jobj.getString("id");
		m_user = jobj.getString("user");
		m_path = jobj.getString("path");
		m_type = jobj.getString("type");
		m_locked = jobj.optBoolean("locked");
		long lTime = jobj.optLong("lockModified");
		if(lTime > 0)
			m_lockModified = new Date(lTime);
	}
	
	/**
	 * Returns container id
	 * @return container id
	 */
	public String getId() 
	{ 
		return m_id; 
	}
	
	/**
	 * Returns container creators userid
	 * @return userid
	 */
	public String getUser() 
	{ 
		return m_user; 
	}

	/**
	 * Returns containers path
	 * @return containers path
	 */
	public String getPath() 
	{ 
		return m_path; 
	}
	
	/**
	 * Returns containers type
	 * @return containers type
	 */
	public String getType() 
	{ 
		return m_type; 
	}
	
	/**
	 * Returns container locked atribute
	 * @return container locked atribute
	 */
	public boolean getLocked() 
	{
		return m_locked;
	}
	
	/**
	 * Returns lock modified timestamp
	 * @return lock modified timestamp
	 */
	public Date getLockModified() 
	{
		return m_lockModified;
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("id", m_id);
		jobj.put("user", m_user);
		jobj.put("path", m_path);
		jobj.put("type", m_type);	
		if(m_locked)
			jobj.put("locked", m_locked);
		if(m_lockModified != null)
			jobj.put("lockModified", m_lockModified);
		return jobj;
	}
}
