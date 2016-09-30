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
package ee.signwise.sdk.service;

public class KeystoreInfo {
	private String m_fileNameAndPath;
	private String m_storePassword;
	private String m_storeType;
	
	public KeystoreInfo(String sFile, String sPswd, String sType)
	{
		m_fileNameAndPath = sFile;
		m_storePassword = sPswd;
		m_storeType = sType;
	}
	
	// accessors
	public String getFileName() { return m_fileNameAndPath; }
	public String getPassword() { return m_storePassword; }
	public String getType() { return m_storeType; }
	
}
