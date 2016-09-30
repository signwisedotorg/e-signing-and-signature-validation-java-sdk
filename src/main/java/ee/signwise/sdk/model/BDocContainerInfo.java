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
 * BDOC Container info returned by SignWise services
 */
public class BDocContainerInfo 
	extends ContainerInfo
{
	private long m_size;
	private String m_version;
	private String m_format;
	// list of data files
	private List<ContainerFileInfo> m_dataFiles;
	// list of signatures
	private List<BDocSignatureInfo> m_signatures;


	/**
	 * Constructor for BDocContainerInfo
	 * @param id container id
	 * @param user container creators userid
	 * @param path containers path
	 * @param type containers type
	 * @param size containers size in bytes
	 * @param format containers format name
	 * @param version containers format version
	 */
	public BDocContainerInfo(String id, String user, String path, String type,
			long size, String format, String version)
	{
		super(id, user, path, type);
		m_size = size;
		m_format = format;
		m_version = version;
	}

	/**
	 * Constructor for BDocContainerInfo
	 * @param jobj JSON object
	 */
	public BDocContainerInfo(JSONObject jobj)
	{
		super(jobj);
		JSONObject jo2 = jobj.optJSONObject("data");
		if(jo2 != null) {
			m_size = jo2.getLong("size");
			m_format = jo2.getString("format");
			m_version = jo2.getString("version");
			JSONArray aDataFiles = jo2.optJSONArray("dataFiles");
			if(aDataFiles != null) {
				m_dataFiles = new ArrayList<ContainerFileInfo>();
				for(int i = 0; i < aDataFiles.length(); i++) {
					JSONObject jo3 = aDataFiles.getJSONObject(i);
					ContainerFileInfo cfi = new ContainerFileInfo(jo3);
					m_dataFiles.add(cfi);
				}
			}
			JSONArray aSignatures = jo2.optJSONArray("signatures");
			if(aSignatures != null) {
				m_signatures = new ArrayList<BDocSignatureInfo>();
				for(int i = 0; i < aSignatures.length(); i++) {
					JSONObject jo3 = aSignatures.getJSONObject(i);
					BDocSignatureInfo sig = new BDocSignatureInfo(jo3);
					m_signatures.add(sig);
				}
			}
		}
	}

	/**
	 * Returns containers size in bytes
	 * @return containers size in bytes
	 */
	public long getSize() 
	{ 
		return m_size; 
	}

	/**
	 * Returns containers format version
	 * @return format version
	 */
	public String getVersion() 
	{ 
		return m_version; 
	}

	/**
	 * Returns containers format name
	 * @return format name
	 */
	public String getFormat() 
	{ 
		return m_format; 
	}
	
	/**
	 * Returns data files in container
	 * @return data file infos
	 */
	public List<ContainerFileInfo> getDataFiles()
	{
		return m_dataFiles;
	}

	/**
	 * Returns signatures in container
	 * @return signature infos
	 */
	public List<BDocSignatureInfo> getSignatures()
	{
		return m_signatures;
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = super.toJSON();
		jobj.put("size", m_size);
		jobj.put("format", m_format);
		jobj.put("version", m_version);
		if(m_dataFiles != null && m_dataFiles.size() > 0) {
			JSONArray aDataFiles = new JSONArray();
			for(ContainerFileInfo cfi : m_dataFiles) {
				JSONObject joDf = cfi.toJSON();
				aDataFiles.put(joDf);
			}
			jobj.put("dataFiles", aDataFiles);
		}
		if(m_signatures != null && m_signatures.size() > 0) {
			JSONArray aSignatures = new JSONArray();
			for(BDocSignatureInfo sig : m_signatures) {
				JSONObject joDf = sig.toJSON();
				aSignatures.put(joDf);
			}
			jobj.put("signatures", aSignatures);
		}
		return jobj;
	}
}
