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
 * Output file info for signer container
 */
public class OutputFileInfo {
	private String m_path;
	private String m_id;
	private String m_mime;
	
	public OutputFileInfo(String path, String id)
	{
		m_path = path;
		m_id = id;
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
	 * Returns file id
	 * @return file id
	 */
	public String getId() 
	{ 
		return m_id; 
	}
	
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("outputPath", m_path);
		jobj.put("fileId", m_id);
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

