/*
 * Copyright 2013 Robert Theis
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
package com.rmtheis.yandtran.translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Makes the generic Yandex API calls. Different service classes can then extend
 * this to make the specific service calls.
 */
public abstract class YandexTranslatorAPI {
    //Encoding type

    protected static final String ENCODING = "UTF-8";

    protected static String apiKey;
    private static String referrer;

    protected static final String PARAM_API_KEY = "key=",
            PARAM_LANG_PAIR = "&lang=",
            PARAM_TEXT = "&text=";

    /**
     * Sets the API key.
     *
     * @param pKey The API key.
     */
    public static void setKey(final String pKey) {
        apiKey = pKey;
    }

    /**
     * Sets the referrer field.
     *
     * @param pReferrer The referrer.
     */
    public static void setReferrer(final String pReferrer) {
        referrer = pReferrer;
    }

    /**
     * Forms an HTTPS request, sends it using GET method and returns the result
     * of the request as a String.
     *
     * @param url The URL to query for a String response.
     * @return The translated String.
     * @throws Exception on error.
     */
    private static String retrieveResponse(final URL url) throws Exception {
        final HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
        if (referrer != null) {
            uc.setRequestProperty("referer", referrer);
        }
        uc.setRequestProperty("Content-Type", "text/plain; charset=" + ENCODING);
        uc.setRequestProperty("Accept-Charset", ENCODING);
        uc.setRequestMethod("GET");

        try {
            final int responseCode = uc.getResponseCode();
            final String result = inputStreamToString(uc.getInputStream());
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
     * Forms a request, sends it using the GET method and returns the value with
     * the given label from the resulting JSON response.
     *
     * @param url
     * @param jsonValProperty
     * @return
     * @throws java.lang.Exception
     */
    protected static String retrievePropString(final URL url, final String jsonValProperty) throws Exception {
        final String response = retrieveResponse(url);
        JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
        return jsonObj.get(jsonValProperty).toString();
    }

    /**
     * Forms a request, sends it using the GET method and returns the contents
     * of the array of strings with the given label, with multiple strings
     * concatenated.
     *
     * @param url
     * @param jsonValProperty
     * @return
     * @throws java.lang.Exception
     */
    protected static String retrievePropArrString(final URL url, final String jsonValProperty) throws Exception {
        final String response = retrieveResponse(url);
        String[] translationArr = jsonObjValToStringArr(response, jsonValProperty);
        String combinedTranslations = "";
        for (String s : translationArr) {
            combinedTranslations += s;
        }
        return combinedTranslations.trim();
    }

    // Helper method to parse a JSONObject containing an array of Strings with the given label.
    private static String[] jsonObjValToStringArr(final String inputString, final String subObjPropertyName) throws Exception {
        JSONObject jsonObj = (JSONObject) JSONValue.parse(inputString);
        JSONArray jsonArr = (JSONArray) jsonObj.get(subObjPropertyName);
        return jsonArrToStringArr(jsonArr.toJSONString(), null);
    }

    // Helper method to parse a JSONArray. Reads an array of JSONObjects and returns a String Array
    // containing the toString() of the desired property. If propertyName is null, just return the String value.
    private static String[] jsonArrToStringArr(final String inputString, final String propertyName) throws Exception {
        final JSONArray jsonArr = (JSONArray) JSONValue.parse(inputString);
        String[] values = new String[jsonArr.size()];

        int i = 0;
        for (Object obj : jsonArr) {
            if (propertyName != null && propertyName.length() != 0) {
                final JSONObject json = (JSONObject) obj;
                if (json.containsKey(propertyName)) {
                    values[i] = json.get(propertyName).toString();
                }
            } else {
                values[i] = obj.toString();
            }
            i++;
        }
        return values;
    }

    /**
     * Reads an InputStream and returns its contents as a String. Also effects
     * rate control.
     *
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     * @throws Exception on error.
     */
    private static String inputStreamToString(final InputStream inputStream) throws Exception {
        final StringBuilder outputBuilder = new StringBuilder();

        try {
            String string;
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
                while (null != (string = reader.readLine())) {
                    // TODO Can we remove this?
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
    protected static void validateServiceState() throws Exception {
        if (apiKey == null || apiKey.length() < 27) {
            throw new RuntimeException("INVALID_API_KEY - Please set the API Key with your Yandex API Key");
        }
    }
}
