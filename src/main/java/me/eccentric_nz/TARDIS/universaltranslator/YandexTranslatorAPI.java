/*
 * Copyright 2018 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.eccentric_nz.TARDIS.universaltranslator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Makes the generic Yandex API calls. Different service classes can then extend this to make the specific service
 * calls.
 */
public abstract class YandexTranslatorAPI {
	//Encoding type

	static final String ENCODING = "UTF-8";
	static final String PARAM_API_KEY = "key=";
	static final String PARAM_LANG_PAIR = "&lang=";
	static final String PARAM_TEXT = "&text=";
	static String apiKey;
	private static String referrer;

	/**
	 * Sets the API key.
	 *
	 * @param pKey The API key.
	 */
	public static void setKey(String pKey) {
		apiKey = pKey;
	}

	/**
	 * Sets the referrer field.
	 *
	 * @param pReferrer The referrer.
	 */
	public static void setReferrer(String pReferrer) {
		referrer = pReferrer;
	}

	/**
	 * Forms an HTTPS request, sends it using GET method and returns the result of the request as a String.
	 *
	 * @param url The URL to query for a String response.
	 * @return The translated String.
	 * @throws Exception on error.
	 */
	private static String retrieveResponse(URL url) throws Exception {
		HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
		if (referrer != null) {
			uc.setRequestProperty("referer", referrer);
		}
		uc.setRequestProperty("Content-Type", "text/plain; charset=" + ENCODING);
		uc.setRequestProperty("Accept-Charset", ENCODING);
		uc.setRequestMethod("GET");

		try {
			int responseCode = uc.getResponseCode();
			String result = inputStreamToString(uc.getInputStream());
			if (responseCode != 200) {
				throw new Exception("Error from Yandex API: " + result);
			}
			return result;
		} finally {
			if (uc != null) {
				uc.disconnect();
			}
		}
	}

	/**
	 * Forms a request, sends it using the GET method and returns the value with the given label from the resulting JSON
	 * response.
	 *
	 * @param url             a uniform resource locator
	 * @param jsonValProperty a JSON string
	 * @return a string
	 * @throws java.lang.Exception a generic exception
	 */
	protected static String retrievePropString(URL url, String jsonValProperty) throws Exception {
		String response = retrieveResponse(url);
		JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
		return jsonObj.get(jsonValProperty).toString();
	}

	/**
	 * Forms a request, sends it using the GET method and returns the contents of the array of strings with the given
	 * label, with multiple strings concatenated.
	 *
	 * @param url             a uniform resource locator
	 * @param jsonValProperty a JSON string
	 * @return a string of concatenated values
	 * @throws java.lang.Exception a generic exception
	 */
	static String retrievePropArrString(URL url, String jsonValProperty) throws Exception {
		String response = retrieveResponse(url);
		return jsonObjValToStringArr(response, jsonValProperty);
	}

	// Helper method to parse a JsonObject containing an array of Strings with the given label.
	private static String jsonObjValToStringArr(String inputString, String subObjPropertyName) {
		JsonObject jsonObj = new JsonParser().parse(inputString).getAsJsonObject();
		JsonArray jsonArr = jsonObj.get(subObjPropertyName).getAsJsonArray();
		return jsonArr.getAsString();
	}

	/**
	 * Reads an InputStream and returns its contents as a String. Also effects rate control.
	 *
	 * @param inputStream The InputStream to read from.
	 * @return The contents of the InputStream as a String.
	 * @throws Exception on error.
	 */
	private static String inputStreamToString(InputStream inputStream) throws Exception {
		StringBuilder outputBuilder = new StringBuilder();

		try {
			String string;
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
				while (null != (string = reader.readLine())) {
					// Need to strip the Unicode Zero-width Non-breaking Space. For some reason, the Microsoft AJAX
					// services prepend this to every response
					outputBuilder.append(string.replaceAll("\uFEFF", ""));
				}
			}
		} catch (IOException ex) {
			throw new Exception("[yandex-translator-api] Error reading translation stream.", ex);
		}
		return outputBuilder.toString();
	}

	//Check if ready to make request, if not, throw a RuntimeException
	static void validateServiceState() {
		if (apiKey == null || apiKey.length() < 27) {
			throw new RuntimeException("INVALID_API_KEY - Please set the API Key with your Yandex API Key");
		}
	}
}
