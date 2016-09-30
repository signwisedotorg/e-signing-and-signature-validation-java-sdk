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
import java.util.Date;
import java.util.List;
import java.util.ArrayList;



public class TestShareCreate {
	public static void main(String[] args)
	{
		try {
			String sConfig = args[0];
			String sCnt = args[1];
			String sInputPath = args[2];
			String sName = args[3];
			String sDesc = args[4];
			String sFile = args[5];
			String sTmpPath = args[6];
			System.out.println("Create share: " + sCnt + " in: " + sInputPath + " name: " + sName + " desc: " + sDesc + " file: " + sFile + " tmp-path: " + sTmpPath);
			ConfigManager.init(sConfig);
			ConfigManager cfg = ConfigManager.instance();
			SignWiseConnection conn = cfg.getSignWiseConnection();
			List<RecipientInfo> recipients = new ArrayList<RecipientInfo>();
			RecipientInfo recv = new RecipientInfo(null, "Mari-Liis", "Mari" + System.currentTimeMillis(), null, "mari@recipient.com", "372100000", 
					"47101010033", "et-EE", "EE", sTmpPath, false, 0,
					new Date(System.currentTimeMillis() + 1000 * 60 * 50 * 24 * 3), // 3 days from now
					"viewer", "active", false, null, null);
			recipients.add(recv);
			SharerInfo sharer = new SharerInfo("Samson the Sharer", "samson@sharer.com");
			// create a new share
			ServerResponse sResp = conn.serviceShareCreate(sCnt, sInputPath, null, "https://www.sunsetsoftware.ee/fileproxy/preview/",
					"https://www.sunsetsoftware.ee/httprecv/json", new Date(System.currentTimeMillis() + 1000 * 60 * 50 * 24 * 5), 
					sName, sDesc, sFile, null, null, sharer, recipients, null);
			System.out.println("SHARE create: " + sResp);
			ShareInfo sh = (ShareInfo)sResp;
			
			// update this share
			recipients = new ArrayList<RecipientInfo>();
			// during recipient update send null as key value because it cannot be updated! So library sets it to null to prevent error
			recv = new RecipientInfo(sh.getRecipients().get(0).getId(), "Mari-Liis Männik", null, null, "mari-liis@recipient.com", "372100000", 
					"47101010033", "et-EE", "EE", sTmpPath, false, 0,
					new Date(System.currentTimeMillis() + 1000 * 60 * 50 * 24 * 3), // 3 days from now
					"viewer", "active", false, null, null);
			recipients.add(recv);
			sResp = conn.serviceShareUpdate(sh.getId(), "https://www.sunsetsoftware.ee/httprecv/json", 
					new Date(System.currentTimeMillis() + 1000 * 60 * 50 * 24 * 5), 
					sName, sDesc, sFile, null, null, "finished", recipients);
			System.out.println("SHARE upd: " + sResp);
			sh = (ShareInfo)sResp;
			
			// add a second recipient, don't change any other atribute of the share
			ServerResponse sResp2 = conn.serviceRecipientCreate(sh.getId(), "Mari-Jaana Männik", "Tanel" + System.currentTimeMillis(), "marij@recipient.com", "372200000", 
					"47101010033", "et-EE", "EE", sTmpPath, false, 0,
					new Date(System.currentTimeMillis() + 1000 * 60 * 50 * 24 * 3), // 3 days from now
					"viewer", "active", false, null, null);
			System.out.println("RECIPIENT create: " + sResp2);
			RecipientInfo recv2 = (RecipientInfo)sResp2;
			
			// update this recipient
			sResp2 = conn.serviceRecipientUpdate(sh.getId(), recv2.getId(), "Mari-Jaana Tammik", "marij2@recipient.com", "372300000", 
					"47101010033", "et-EE", "EE", sTmpPath, false, 0,
					new Date(System.currentTimeMillis() + 1000 * 60 * 50 * 24 * 3), // 3 days from now
					"viewer", "active", false, null, null);
			System.out.println("RECIPIENT update: " + sResp2);
			recv2 = (RecipientInfo)sResp2;
			
			// delete the second recipient
			sResp2 = conn.serviceRecipientDelete(sh.getId(), recv2.getId());
			System.out.println("RECIPIENT delete: " + sResp2);
			
			// delete this share
			MessageInfo msg = new MessageInfo("en_US", "deleting the share");
			sResp = conn.serviceShareDelete(sh.getId(), msg);
			System.out.println("SHARE del: " + sResp);
			
		} catch(Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
		}
	}
	
}
