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
 * Holds the data of an X500Principal. This structure is used 
 * as part of many server responses
 */
public class X500PrincipalInfo {
	private String m_raw;
	private String m_country;
	private String m_commonName;
	private String m_unitName;
	private String m_identificationCode;
	private String m_firstName;
	private String m_lastName;
	private String m_email;
	private String m_address;
	private String m_telephone;
	private String m_locality;
	private String m_state;
	
	
	/**
	 * Constructor for X500PrincipalInfo
	 * @param raw certificates distinguished name (DN)
	 * @param country certificates country code
	 * @param cn certificates common name (CN)
	 * @param unit unit name
	 * @param identCode identification / social security code
	 * @param firstName first name
	 * @param lastName last name
	 * @param email email address
	 * @param address postal address
	 * @param telephone phone number
	 * @param locality locality / city
	 * @param state state / province
	 */
	public X500PrincipalInfo(String raw, String country, String cn, String unit,
			String identCode, String firstName, String lastName, String email, 
			String address, String telephone, String locality, String state)
	{
		m_raw = raw;
		m_country = country;
		m_commonName = cn;
		m_unitName = unit;
		m_identificationCode = identCode;
		m_firstName = firstName;
		m_lastName = lastName;
		m_email = email;
		m_address = address;
		m_telephone = telephone;
		m_locality = locality;
		m_state = state;
	}
	
	/**
	 * Constructor for X500PrincipalInfo
	 * @param jobj JSON object
	 */
	public X500PrincipalInfo(JSONObject jobj)
	{
		m_raw = jobj.getString("raw");
		m_country = jobj.optString("country");
		m_commonName = jobj.optString("commonName");
		m_unitName = jobj.optString("uniteName");
		m_identificationCode = jobj.optString("identificationCode");
		m_firstName = jobj.optString("firstName");
		m_lastName = jobj.optString("lastName");
		m_email = jobj.optString("email");
		m_address = jobj.optString("address");
		m_telephone = jobj.optString("telephone");
		m_locality = jobj.optString("locality");
		m_state = jobj.optString("state");
	}
	
	/**
	 * Returns certificates distinguished name (DN)
	 * @return distinguished name (DN)
	 */
	public String getRaw() 
	{ 
		return m_raw; 
	}
	
	/**
	 * Returns certificates country code
	 * @return country code
	 */
	public String getCountry() 
	{ 
		return m_country; 
	}
	
	/**
	 * Returns certificates common name (CN)
	 * @return common name (CN)
	 */
	public String getCommonName() 
	{ 
		return m_commonName; 
	}
	
	/**
	 * Returns unit name
	 * @return unit name
	 */
	public String getUnitName() 
	{ 
		return m_unitName; 
	}

	/**
	 * Returns users identification / social security code
	 * @return identification / social security code
	 */
	public String getIdentificationCode() 
	{ 
		return m_identificationCode; 
	}

	/**
	 * Returns users first name
	 * @return first name
	 */
	public String getFirstName() 
	{ 
		return m_firstName; 
	}

	/**
	 * Returns users last name
	 * @return last name
	 */
	public String getLastName() 
	{ 
		return m_lastName; 
	}

	/**
	 * Returns users email address
	 * @return email address
	 */
	public String getEmail() 
	{ 
		return m_email; 
	}

	
	/**
	 * Returns users postal address
	 * @return postal address
	 */
	public String getAddress() 
	{ 
		return m_address; 
	}
	
	/**
	 * Returns users telephone number
	 * @return telephone number
	 */
	public String getTelephone() 
	{ 
		return m_telephone; 
	}

	/**
	 * Returns users locality / city name
	 * @return locality / city name
	 */
	public String getLocality() 
	{ 
		return m_locality; 
	}

	/**
	 * Returns users state / province name
	 * @return state / province name
	 */
	public String getState() 
	{ 
		return m_state; 
	}

	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = new JSONObject();
		jobj.put("raw", m_raw);
		jobj.put("country", m_country);
		jobj.put("commonName", m_commonName);
		jobj.put("identificationCode", m_identificationCode);
		jobj.put("firstName", m_firstName);
		jobj.put("lastName", m_lastName);
		jobj.put("email", m_email);
		jobj.put("address", m_address);
		jobj.put("telephone", m_telephone);
		jobj.put("locality", m_locality);
		jobj.put("state", m_state);			
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
