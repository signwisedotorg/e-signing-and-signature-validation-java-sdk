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




public class TestAuthenticate {
	public static void main(String[] args)
	{
		try {
			
			String sConfig = args[0];
			String sPin = args[1];
			int nSlot = Integer.parseInt(args[2]);
			ConfigManager.init(sConfig);
			ConfigManager cfg = ConfigManager.instance();
			String sDriver = cfg.getProperty("PKCS11_DRIVER");
			System.out.println("Init pkcs11 diver: " + sDriver + " slot: " + nSlot + " PIN: " + sPin);
			//SunPkcs11Connection pkcs11 = new SunPkcs11Connection(sDriver, sPin, nSlot);
			IaikPkcs11Connection pkcs11 = new IaikPkcs11Connection(sDriver);
			byte[] digest = pkcs11.digestOfType("Hello SignWise authentication".getBytes(), "SHA-1");
			List<X509Certificate> lcerts = pkcs11.getCertificates(nSlot, sPin);
			System.out.println("Got certs: " + ((lcerts != null) ? lcerts.size() : 0));
			byte[] signature = pkcs11.sign(digest, nSlot, sPin);
			System.out.println("Got signature: " + ((signature != null) ? signature.length : 0));
			System.out.println("Digest: " + pkcs11.bin2hex(digest) + " signature " + pkcs11.bin2hex(signature));
			SignWiseConnection conn = cfg.getSignWiseConnection();
			String sResp = conn.serviceAuthenticate(digest, signature, lcerts.get(0), null);
			System.out.println("GOT: " + sResp);
		} catch(Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
		}
	}
}
