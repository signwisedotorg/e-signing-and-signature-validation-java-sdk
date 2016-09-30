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
 * Error response from server
 */
public class ErrorResponse 
	extends ServerResponse
{
	private String m_status;
	private String m_code;
	private String m_message;
	private String m_exceptionMessage;
	
	/**
	 * Constructor for ErrorResponse
	 * @param status http status code
	 * @param code error code
	 * @param message error message
	 * @param exceptionMessage java exception message
	 */
	public ErrorResponse(String status, String code, String message, String exceptionMessage)
	{
		super(false);
		m_status = status;
		m_code = code;
		m_message = message;
		m_exceptionMessage = exceptionMessage;
	}
	
	/**
	 * Constructor for ErrorResponse
	 * @param jobj JSON object
	 */
	public ErrorResponse(JSONObject jobj)
	{
		super(false);
		m_status = jobj.optString("statusCode");
		m_code = jobj.optString("code");
		m_message = jobj.optString("message");
		m_exceptionMessage = jobj.optString("exceptionMessage");
		JSONObject jo2 = jobj.optJSONObject("body");
		if(jo2 != null) {
			m_code = jo2.optString("code");
			m_message = jo2.optString("message");
			m_exceptionMessage = jo2.optString("exceptionMessage");
		}
	}
	
	/**
	 * Returns error code
	 * @return error code
	 */
	public String getCode() 
	{ 
		return m_code; 
	}
	
	/**
	 * Returns error message
	 * @return error message
	 */
	public String getMessage() 
	{ 
		return m_message; 
	}
	
	/**
	 * Returns java exception message
	 * @return java exception message
	 */
	public String getExceptionMessage() 
	{ 
		return m_exceptionMessage; 
	}
	
	/**
	 * Returns JSON form for transmission
	 * @return objects JSON form
	 */
	public JSONObject toJSON()
	{
		JSONObject jobj = super.toJSON();
		JSONObject jerr = new JSONObject();
		jobj.put("status", m_status);
		jerr.put("code", m_code);
		jerr.put("message", m_message);
		jerr.put("exceptionMessage", m_exceptionMessage);
		jobj.put("error", jerr);
		return jobj;
	}
	
}

