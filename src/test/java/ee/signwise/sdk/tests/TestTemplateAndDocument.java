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



public class TestTemplateAndDocument {
	public static void main(String[] args)
	{
		try {
			String sConfig = args[0];
			String sInputPath = args[1];
			String sName = args[2];
			String sOutputPath = args[3];
			String sDocName = args[4];
			System.out.println("Template in: " + sInputPath + " name: " + sName + " document path: " + sOutputPath + " name: " + sDocName);
			ConfigManager.init(sConfig);
			ConfigManager cfg = ConfigManager.instance();
			SignWiseConnection conn = cfg.getSignWiseConnection();
			List<PlaceholderInfo> placeholders = new ArrayList<PlaceholderInfo>();
			// the placeholder text (1. argument) must exist in the input document
			placeholders.add(new PlaceholderInfo("Headline1", "First headline:", "placeholder"));
			placeholders.add(new PlaceholderInfo("text1", "Test1:", "placeholder"));
			// create a new template
			ServerResponse sResp = conn.serviceTemplateCreate(sInputPath, sName, placeholders);
			System.out.println("TEMPLATE create: " + sResp);
			TemplateInfo ti = (TemplateInfo)sResp;

			// update template
			placeholders.add(new PlaceholderInfo("text2", "Test2:", "placeholder"));
			ServerResponse sResp2 = conn.serviceTemplateUpdate(ti.getId(), "Modified template name", placeholders);
			System.out.println("TEMPLATE update: " + sResp2);
			ti = (TemplateInfo)sResp2;
			
			// create a document base on this template
			List<FieldInfo> fields = new ArrayList<FieldInfo>();
			fields.add(new FieldInfo("Headline1", "My favourite j2ee server"));
			fields.add(new FieldInfo("text1", "FileProxy servlet"));
			fields.add(new FieldInfo("text2", "ASICE container"));
			ServerResponse sResp3 = conn.serviceDocumentCreate(ti.getId(), sOutputPath, sDocName, fields);
			System.out.println("DOCUMENT create: " + sResp3);
			DocumentInfo di = (DocumentInfo)sResp3;
				
			// update a document
			ServerResponse sResp4 = conn.serviceDocumentUpdate(di.getId(), sDocName + "-modified", fields);
			System.out.println("DOCUMENT update: " + sResp4);
			di = (DocumentInfo)sResp3;
			
			// delete the document
			ServerResponse sResp5 = conn.serviceDocumentDelete(di.getId());
			System.out.println("DOCUMENT delete: " + sResp5);
			
			// delete the template
			ServerResponse sResp6 = conn.serviceTemplateDelete(ti.getId());
			System.out.println("TEMPLATE delete: " + sResp6);
			
		} catch(Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
		}
	}
	
}
