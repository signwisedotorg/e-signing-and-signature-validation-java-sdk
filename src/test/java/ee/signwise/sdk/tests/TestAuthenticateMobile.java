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

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.security.cert.X509Certificate;


public class TestAuthenticateMobile {
	public static void main(String[] args)
	{
		try {
			
			String sConfig = args[0];
			String sPhone = args[1];
			String sPerCode = args[2];
			String sLang = args[3];
			String sUrl = args[4];
			ConfigManager.init(sConfig);
			ConfigManager cfg = ConfigManager.instance();
			System.out.println("Authenticate user: " + sPerCode + " phone " + sPhone + " lang: " + sLang + " url: " + sUrl);
			SignWiseConnection conn = cfg.getSignWiseConnection();
			String sResp = conn.serviceAuthenticateMobile(sPhone, sPerCode, 0, sLang, sUrl);
			System.out.println("GOT: " + sResp);
		} catch(Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
		}
	}
}
