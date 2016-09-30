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
import java.io.FileInputStream;
import java.security.cert.X509Certificate;

public class TestCertificateParse {

	public static void main(String[] args)
	{
		try {
			String sConfig = args[0];
			String sCert = args[1];
			System.out.println("Parsing cert: " + sCert + " config: " + sConfig);
			ConfigManager.init(sConfig);
			ConfigManager cfg = ConfigManager.instance();
			SignWiseConnection conn = cfg.getSignWiseConnection();
			X509Certificate cert = conn.readCertFromStream(new FileInputStream(sCert));
			ServerResponse sResp = conn.serviceCertificateParse(cert);
			System.out.println("GOT: " + sResp);
		} catch(Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
		}
	}
}
