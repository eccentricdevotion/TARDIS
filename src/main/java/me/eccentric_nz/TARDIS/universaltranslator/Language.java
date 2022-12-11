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

/**
 * Language - an enum of language codes supported by the Lingva API
 */
public enum Language {

    AUTO_DETECT("auto"),
    AFRIKAANS("af"),
    ALBANIAN("sq"),
    AMHARIC("am"),
    ARABIC("ar"),
    ARMENIAN("hy"),
    ASSAMESE("as"),
    AYMARA("ay"),
    AZERBAIJANI("az"),
    BAMBARA("bm"),
    BASQUE("eu"),
    BELARUSIAN("be"),
    BENGALI("bn"),
    BHOJPURI("bho"),
    BOSNIAN("bs"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CEBUANO("ceb"),
    CHICHEWA("ny"),
    CHINESE("zh"),
    CORSICAN("co"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DHIVEHI("dv"),
    DOGRI("doi"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESPERANTO("eo"),
    ESTONIAN("et"),
    EWE("ee"),
    FILIPINO("tl"),
    FINNISH("fi"),
    FRENCH("fr"),
    FRISIAN("fy"),
    GALICIAN("gl"),
    GEORGIAN("ka"),
    GERMAN("de"),
    GREEK("el"),
    GUARANI("gn"),
    GUJARATI("gu"),
    HAITIAN_CREOLE("ht"),
    HAUSA("ha"),
    HAWAIIAN("haw"),
    HEBREW("iw"),
    HINDI("hi"),
    HMONG("hmn"),
    HUNGARIAN("hu"),
    ICELANDIC("is"),
    IGBO("ig"),
    ILOCANO("ilo"),
    INDONESIAN("id"),
    IRISH("ga"),
    ITALIAN("it"),
    JAPANESE("ja"),
    JAVANESE("jw"),
    KANNADA("kn"),
    KAZAKH("kk"),
    KHMER("km"),
    KINYARWANDA("rw"),
    KONKANI("gom"),
    KOREAN("ko"),
    KRIO("kri"),
    KURDISH("ku"),
    KYRGYZ("ky"),
    LAO("lo"),
    LATIN("la"),
    LATVIAN("lv"),
    LINGALA("ln"),
    LITHUANIAN("lt"),
    LUGANDA("lg"),
    LUXEMBOURGISH("lb"),
    MACEDONIAN("mk"),
    MAITHILI("mai"),
    MALAGASY("mg"),
    MALAY("ms"),
    MALAYALAM("ml"),
    MALTESE("mt"),
    MAORI("mi"),
    MARATHI("mr"),
    MEITEILON("mni-mtei"),
    MIZO("lus"),
    MONGOLIAN("mn"),
    MYANMAR("my"),
    NEPALI("ne"),
    NORWEGIAN("no"),
    ODIA("or"),
    OROMO("om"),
    PASHTO("ps"),
    PERSIAN("fa"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    PUNJABI("pa"),
    QUECHUA("qu"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SAMOAN("sm"),
    SANSKRIT("sa"),
    SCOTS_GAELIC("gd"),
    SEPEDI("nso"),
    SERBIAN("sr"),
    SESOTHO("st"),
    SHONA("sn"),
    SINDHI("sd"),
    SINHALA("si"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SOMALI("so"),
    SPANISH("es"),
    SUNDANESE("su"),
    SWAHILI("sw"),
    SWEDISH("sv"),
    TAJIK("tg"),
    TAMIL("ta"),
    TATAR("tt"),
    TELUGU("te"),
    THAI("th"),
    TIGRINYA("ti"),
    TSONGA("ts"),
    TURKISH("tr"),
    TURKMEN("tk"),
    TWI("ak"),
    UKRAINIAN("uk"),
    URDU("ur"),
    UYGHUR("ug"),
    UZBEK("uz"),
    VIETNAMESE("vi"),
    WELSH("cy"),
    XHOSA("xh"),
    YIDDISH("yi"),
    YORUBA("yo"),
    ZULU("zu");

    /**
     * String representation of this language.
     */
    private final String language;

    /**
     * Enum constructor.
     *
     * @param language The language identifier.
     */
    Language(String language) {
        this.language = language;
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
     * Returns the language code.
     *
     * @return The language code.
     */
    public String getCode() {
        return language;
    }
}
