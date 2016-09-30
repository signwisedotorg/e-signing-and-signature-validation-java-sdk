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
 * Signer container datafile info
 */
public class ContainerFileInfo 
{
	private String m_path;
	private String m_id;
	private String m_fileName;
	private String m_mimeType;
	private long m_size;
	private String m_digestType;
	private String m_digestValue;
	
	
	/**
	 * Constructor for ContainerFileInfo
	 * @param path datafile path
	 * @param id datafile id
	 * @param fileName file name in container
	 * @param mimeType datafile mime type
	 * @param size datafile size
	 * @param digestType digest type
	 * @param digestValue digest value
	 */
	public ContainerFileInfo(String path, String id, String fileName, String mimeType, long size, String digestType, String digestValue)
	{
		m_id = id;
		m_path = path;
		m_fileName = fileName;
		m_mimeType = mimeType;
		m_size = size;
		m_digestType = digestType;
		m_digestValue = digestValue;
	}
	
	/**
	 * Constructor for ContainerFileInfo
	 * @param jobj JSON object
	 */
	public ContainerFileInfo(JSONObject jobj)
	{
		m_id = jobj.getString("id");
		m_path = jobj.optString("path");
		m_fileName = jobj.optString("fileName");
		m_mimeType = jobj.optString("mimeType");
		m_size = jobj.optLong("size");
		m_digestType = jobj.optString("digestType");
		m_digestValue = jobj.optString("digestValue");
	}
	
	/**
	 * Returns file id
	 * @return file id
	 */
	public String getId() 
	{ 
		return m_id; 
	}
	
	/**
	 * Returns file path
	 * @return file path
	 */
	public String getPath() 
	{ 
		return m_path; 
	}
	
	/**
	 * Returns file name
	 * @return file name
	 */
	public String getFileName() 
	{ 
		return m_fileName; 
	}
	
	/**
	 * Returns files mime type
	 * @return files mime type
	 */
	public String getMimeType() 
	{ 
		return m_mimeType; 
	}
	
	/**
	 * Returns file size in bytes
	 * @return file size in bytes
	 */
	public long getSize() 
	{ 
		return m_size; 
	}

	/**
	 * Returns file digest type
	 * @return file digest type
	 */
	public String getDigestType() 
	{ 
		return m_digestType; 
	}

	/**
	 * Returns file digest value
	 * @return file digest value
	 */
	public String getDigestValue() 
	{ 
		return m_digestValue; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("id", m_id);
		jobj.putOpt("path", m_path);
		jobj.putOpt("fileName", m_fileName);
		jobj.putOpt("mimeType", m_mimeType);
		jobj.put("size", m_size);
		jobj.putOpt("digestType", m_digestType);
		jobj.putOpt("digestValue", m_digestValue);
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
