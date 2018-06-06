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
package com.rmtheis.yandtran.translate;

import java.net.URL;
import java.net.URLEncoder;

/**
 * Makes calls to the Yandex machine translation web service API
 */
public final class Translate extends YandexTranslatorAPI {

    private static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    private static final String TRANSLATION_LABEL = "text";

    //prevent instantiation
    private Translate() {
    }

    /**
     * Translates text from a given Language to another given Language using Yandex.
     *
     * @param text The String to translate.
     * @param from The language code to translate from.
     * @param to   The language code to translate to.
     * @return The translated String.
     * @throws Exception on error.
     */
    public static String execute(String text, Language from, Language to) throws Exception {
        validateServiceState(text);
        String params
                = PARAM_API_KEY + URLEncoder.encode(apiKey, ENCODING)
                + PARAM_LANG_PAIR + URLEncoder.encode(from.toString(), ENCODING) + URLEncoder.encode("-", ENCODING) + URLEncoder.encode(to.toString(), ENCODING)
                + PARAM_TEXT + URLEncoder.encode(text, ENCODING);
        URL url = new URL(SERVICE_URL + params);
        return retrievePropArrString(url, TRANSLATION_LABEL).trim();
    }

    private static void validateServiceState(String text) throws Exception {
        int byteLength = text.getBytes(ENCODING).length;
        if (byteLength > 10240) {
            throw new RuntimeException("TEXT_TOO_LARGE");
        }
        validateServiceState();
    }
}
