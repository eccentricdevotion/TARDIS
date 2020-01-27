/*
 * Copyright (C) 2020 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

/**
 * @author eccentric_nz
 */
public enum LANGUAGE {

    ar("ARABIC"),
    bg("BULGARIAN"),
    ca("CATALAN"),
    zh("CHINESE"),
    cs("CZECH"),
    da("DANISH"),
    nl("DUTCH"),
    en("ENGLISH"),
    et("ESTONIAN"),
    fi("FINNISH"),
    fr("FRENCH"),
    de("GERMAN"),
    el("GREEK"),
    ht("HAITIAN CREOLE"),
    he("HEBREW"),
    hi("HINDI"),
    mww("HMONG DAW"),
    hu("HUNGARIAN"),
    id("INDONESIAN"),
    it("ITALIAN"),
    ja("JAPANESE"),
    ko("KOREAN"),
    lv("LATVIAN"),
    lt("LITHUANIAN"),
    ms("MALAY"),
    no("NORWEGIAN"),
    fa("PERSIAN"),
    pl("POLISH"),
    pt("PORTUGUESE"),
    ro("ROMANIAN"),
    ru("RUSSIAN"),
    sk("SLOVAK"),
    sl("SLOVENIAN"),
    es("SPANISH"),
    sv("SWEDISH"),
    th("THAI"),
    tr("TURKISH"),
    uk("UKRAINIAN"),
    ur("URDU"),
    vi("VIETNAMESE");

    private final String lang;

    LANGUAGE(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }
}
