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
 * Input file info for signer container
 */
public class InputFileInfo {
	private String m_path;
	private String m_name;
	private String m_mime;
	
	public InputFileInfo(String path, String name, String mime)
	{
		m_path = path;
		m_name = name;
		m_mime = mime;
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
	public String getName() 
	{ 
		return m_name; 
	}
	
	/**
	 * Returns files mime type
	 * @return files mime type
	 */
	public String getMime() 
	{ 
		return m_mime; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("inputPath", m_path);
		jobj.put("fileName", m_name);
		jobj.put("fileType", m_mime);
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
