---
layout: default
title: Universal Translator
---

# TARDIS Universal Translator

The translation circuit is a part of the TARDIS that allows instantaneous translation of most languages spoken or
written in the universe.

You can send translated text in chat using the `/tardissay [language] [message]`, for example:

    /tardissay SWEDISH hello world

## How it works

The command uses the free Lingva translation API to translate your message — see the list of supported languages below.

- `[language]` — You need to specify the language you want to translate into
- `[message]` — The plugin tries to auto-detect the language that you are typing in, however you can force the language
  it uses by setting a player preference:

  /tardisprefs language [language]

### Supported languages

    AUTO_DETECT
	AFRIKAANS
	ALBANIAN
	AMHARIC
	ARABIC
	ARMENIAN
	ASSAMESE
	AYMARA
	AZERBAIJANI
	BAMBARA
	BASQUE
	BELARUSIAN
	BENGALI
	BHOJPURI
	BOSNIAN
	BULGARIAN
	CATALAN
	CEBUANO
	CHICHEWA
	CHINESE
	CORSICAN
	CROATIAN
	CZECH
	DANISH
	DHIVEHI
	DOGRI
	DUTCH
	ENGLISH
	ESPERANTO
	ESTONIAN
	EWE
	FILIPINO
	FINNISH
	FRENCH
	FRISIAN
	GALICIAN
	GEORGIAN
	GERMAN
	GREEK
	GUARANI
	GUJARATI
	HAITIAN_CREOLE
	HAUSA
	HAWAIIAN
	HEBREW
	HINDI
	HMONG
	HUNGARIAN
	ICELANDIC
	IGBO
	ILOCANO
	INDONESIAN
	IRISH
	ITALIAN
	JAPANESE
	JAVANESE
	KANNADA
	KAZAKH
	KHMER
	KINYARWANDA
	KONKANI
	KOREAN
	KRIO
	KURDISH
	KYRGYZ
	LAO
	LATIN
	LATVIAN
	LINGALA
	LITHUANIAN
	LUGANDA
	LUXEMBOURGISH
	MACEDONIAN
	MAITHILI
	MALAGASY
	MALAY
	MALAYALAM
	MALTESE
	MAORI
	MARATHI
	MEITEILON
	MIZO
	MONGOLIAN
	MYANMAR
	NEPALI
	NORWEGIAN
	ODIA
	OROMO
	PASHTO
	PERSIAN
	POLISH
	PORTUGUESE
	PUNJABI
	QUECHUA
	ROMANIAN
	RUSSIAN
	SAMOAN
	SANSKRIT
	SCOTS_GAELIC
	SEPEDI
	SERBIAN
	SESOTHO
	SHONA
	SINDHI
	SINHALA
	SLOVAK
	SLOVENIAN
	SOMALI
	SPANISH
	SUNDANESE
	SWAHILI
	SWEDISH
	TAJIK
	TAMIL
	TATAR
	TELUGU
	THAI
	TIGRINYA
	TSONGA
	TURKISH
	TURKMEN
	TWI
	UKRAINIAN
	URDU
	UYGHUR
	UZBEK
	VIETNAMESE
	WELSH
	XHOSA
	YIDDISH
	YORUBA
	ZULU

![TARDIS Universal Translator](images/docs/universaltranslator.jpg)


## Auto-translating player messages

You can setup the TARDIS plugin to automatically translate messages from a specified player. Use the command:

```
/tardisprefs translate [language to] [language from] [player]
```

To turn off auto-translation, use the command:

```
/tardisprefs translate off
```
