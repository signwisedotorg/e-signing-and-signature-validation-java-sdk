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



public class TestSign {
	public static void main(String[] args)
	{
		try {
			String sConfig = args[0];
			String sCntType = args[1];
			String sInputPath = args[2];
			String sTmpPath = args[3];
			String sPin = args[4];
			int nSlot = Integer.parseInt(args[5]);
			
			ConfigManager.init(sConfig);
			ConfigManager cfg = ConfigManager.instance();
			String sDriver = cfg.getProperty("PKCS11_DRIVER");
			System.out.println("Init pkcs11 diver: " + sDriver + " slot: " + nSlot + " PIN: " + sPin);
			//SunPkcs11Connection pkcs11 = new SunPkcs11Connection(sDriver, sPin, nSlot);
			IaikPkcs11Connection pkcs11 = new IaikPkcs11Connection(sDriver);
			List<X509Certificate> lcerts = pkcs11.getCertificates(nSlot, sPin);
			System.out.println("Got certs: " + ((lcerts != null) ? lcerts.size() : 0));
			SignWiseConnection conn = cfg.getSignWiseConnection();
			ServerResponse sResp = conn.serviceSignPrepare(sCntType, sInputPath, sTmpPath, 
					null, null, null, lcerts.get(0), null, 
					"EE", "Harjumaa", "Tallinn", "12345", "Testing signing",
					null, 0, 0, 0); // use for pdf:  1, 200, 200);
			System.out.println("GOT: " + sResp);
			if(sResp != null && sResp instanceof SignaturePrepareResponse) {
				SignaturePrepareResponse sigresp = (SignaturePrepareResponse)sResp;
				byte[] digest = ConvertUtils.hex2bin(sigresp.getDigest());
				System.out.println("Digest: " + sigresp.getDigest() + " type: " + sigresp.getDigestType());
				byte[] signature = pkcs11.sign(digest, nSlot, sPin);
				System.out.println("Got signature: " + ((signature != null) ? signature.length : 0));
				ServerResponse sResp2 = conn.serviceSignFinalize(sCntType, sInputPath, ConvertUtils.bin2hex(signature), "ee");
				System.out.println("GOT: " + sResp2);
			}
				
		} catch(Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
		}
	}
}
