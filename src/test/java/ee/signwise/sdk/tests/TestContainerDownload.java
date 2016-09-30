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
package ee.signwise.sdk.tests;
import ee.signwise.sdk.model.*;
import ee.signwise.sdk.service.*;
import java.io.FileOutputStream;


public class TestContainerDownload {
	public static void main(String[] args)
	{
		try {
			String sConfig = args[0];
			String sCnt = args[1];
			String sInPath = args[2];
			String sFileId = args[3];
			String sFileOut = args[4];
			System.out.println("Info download: " + sCnt + " in: " + sInPath + " id: " + sFileId);
			ConfigManager.init(sConfig);
			ConfigManager cfg = ConfigManager.instance();
			SignWiseConnection conn = cfg.getSignWiseConnection();
			byte[] data = conn.serviceContainerDownload(sCnt, sInPath, sFileId);
			System.out.println("GOT: " + ((data != null) ? data.length : 0) + " bytes, wrote to: " + sFileOut);
			if(data != null && data.length > 0) {
				FileOutputStream fos = new FileOutputStream(sFileOut);
				fos.write(data);
				fos.close();
			}
		} catch(Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
		}
	}
}
