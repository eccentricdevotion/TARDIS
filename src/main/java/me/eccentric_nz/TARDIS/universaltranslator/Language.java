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
package me.eccentric_nz.tardis.universaltranslator;

/**
 * Language - an enum of language codes supported by the Yandex API
 */
public enum Language {
	AFRIKAANS("af"),
	ALBANIAN("sq"),
	ARABIC("ar"),
	ARMENIAN("hy"),
	AZERBAIJANI("az"),
	BASHKIR("ba"),
	BASQUE("eu"),
	BELARUSIAN("be"),
	BOSNIAN("bs"),
	BULGARIAN("bg"),
	CATALAN("ca"),
	CHINESE("zh"),
	CROATIAN("hr"),
	CZECH("cs"),
	DANISH("da"),
	DUTCH("nl"),
	ENGLISH("en"),
	ESTONIAN("et"),
	FINNISH("fi"),
	FRENCH("fr"),
	GALICIAN("gl"),
	GEORGIAN("ka"),
	GERMAN("de"),
	GREEK("el"),
	HAITIAN("ht"),
	HEBREW("he"),
	HUNGARIAN("hu"),
	ICELANDIC("is"),
	INDONESIAN("id"),
	IRISH("ga"),
	ITALIAN("it"),
	JAPANESE("ja"),
	KAZAKH("kk"),
	KIRGHIZ("ky"),
	KOREAN("ko"),
	LATIN("la"),
	LATVIAN("ly"),
	LITHUANIAN("lt"),
	MACEDONIAN("mk"),
	MALAGASY("mg"),
	MALAY("ms"),
	MALTESE("mt"),
	MONGOLIAN("mn"),
	NORWEGIAN("no"),
	PERSIAN("fa"),
	POLISH("pl"),
	PORTUGUESE("pt"),
	ROMANIAN("ro"),
	RUSSIAN("ru"),
	SERBIAN("sr"),
	SLOVAK("sk"),
	SLOVENIAN("sl"),
	SPANISH("es"),
	SWAHILI("sw"),
	SWEDISH("sv"),
	TAGALOG("tl"),
	TAJIK("tg"),
	TATAR("tt"),
	THAI("th"),
	TURKISH("tr"),
	UKRAINIAN("uk"),
	UZBEK("uz"),
	VIETNAMESE("vi"),
	WELSH("cy");

	/**
	 * String representation of this language.
	 */
	private final String language;

	/**
	 * Enum constructor.
	 *
	 * @param pLanguage The language identifier.
	 */
	Language(String pLanguage) {
		language = pLanguage;
	}

	public static Language fromString(String pLanguage) {
		for (Language l : values()) {
			if (l.toString().equals(pLanguage)) {
				return l;
			}
		}
		return null;
	}

	/**
	 * Returns the String representation of this language.
	 *
	 * @return The String representation of this language.
	 */
	@Override
	public String toString() {
		return language;
	}
}
