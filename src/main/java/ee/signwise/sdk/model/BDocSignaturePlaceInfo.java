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
 * BDOC Signature address info (signer location) returned by SignWise services
 */
public class BDocSignaturePlaceInfo {
	private String m_city;
	private String m_state;
	private String m_postaLCode;
	private String m_country;
	
	/**
	 * Constructor for BDocSignatureErrorInfo
	 * @param city city name
	 * @param state state name
	 * @param postalCode postal code
	 * @param country country name
	*/
	public BDocSignaturePlaceInfo(String city, String state, String postalCode, String country)
	{
		m_city = city;
		m_state = state;
		m_postaLCode = postalCode;
		m_country = country;
	}
	
	/**
	 * Constructor for BDocSignatureErrorInfo
	 * @param jobj JSON object
	 */
	public BDocSignaturePlaceInfo(JSONObject jobj)
	{
		m_city = jobj.optString("city");
		m_state = jobj.optString("state");
		m_postaLCode = jobj.optString("postalCode");
		m_country = jobj.optString("country");
	}
	
	/**
	 * Returns city name
	 * @return city name
	 */
	public String getCity() 
	{ 
		return m_city; 
	}
	
	/**
	 * Returns state name
	 * @return state name
	 */
	public String getState() 
	{ 
		return m_state; 
	}
	
	/**
	 * Returns postal code
	 * @return postal code
	 */
	public String getPostalCode() 
	{ 
		return m_postaLCode; 
	}

	/**
	 * Returns country name
	 * @return country name
	 */
	public String getCountry() 
	{ 
		return m_country; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.putOpt("city", m_city);
		jobj.putOpt("state", m_state);
		jobj.putOpt("postalCode", m_postaLCode);
		jobj.putOpt("country", m_country);
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
