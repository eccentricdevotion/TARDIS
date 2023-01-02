/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;
/*
pluralizer.java v0.1.1 - A tool that returns plural form of English words
Original javascript file created by Ron Royston, https: // rack.pub
https: // github.com/rhroyston/pluralizer-js
License: MIT
*/

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pluraliser {

    private static final HashMap<String, String> irregular = new HashMap<>() {
        {
            put("child", "children");
            put("die", "dice");
            put("foot", "feet");
            put("goose", "geese");
            put("louse", "lice");
            put("man", "men");
            put("mouse", "mice");
            put("ox", "oxen");
            put("person", "people");
            put("that", "those");
            put("this", "these");
            put("tooth", "teeth");
            put("woman", "women");
        }
    };
    private static final HashMap<String, String> xExceptions = new HashMap<>() {
        {
            put("axis", "axes");
            put("barrier", "barrier blocks");
            put("carrot on a stick", "carrots on a stick");
            put("heart of the sea", "hearts of the sea");
            put("jigsaw", "jigsaw blocks");
            put("light", "light blocks");
            put("lily of the valley", "lilies of the valley");
            put("ox", "oxen");
            put("potted lily of the valley", "potted lilies of the valley");
            put("totem of undying", "totems of undying");
            put("warped fungus on a stick", "warped fungi on a stick");
        }
    };
    private static final List<String> endsWithExceptions = Arrays.asList(
            "air", "andesite", "armor", "bamboo", "bars", "basalt", "beans", "berries", "bedrock", "boots", "bricks",
            "cactus", "calcite", "carrots", "crystals", "coal", "concrete", "copper", "debris", "deepslate", "diorite",
            "dirt", "dust", "fire", "fish", "flesh", "foot", "fungus", "glass", "gold", "granite", "grass", "gravel",
            "hyphae", " ice", "iron", "leaves", "leggings", "lichen", "meal", "mutton", "nylium", "obsidian", "ore",
            "planks", "podzol", "potatoes", "powder", "prismarine", "quartz", "roots", "salmon", "sand", "seeds", "snow",
            "soil", "sprouts", "stairs", "stone", "terracotta", "tiles", "tuff", "vines", "water", "wood", "wool"
    );
    private static final List<String> endsWithExceptionsAddS = Arrays.asList(
            "allium", "azalea", "button", "cauldron", "grindstone", "piston", "potion"
    );
    private static final HashMap<String, String> fExceptions = new HashMap<>() {
        {
            put("belief", "beliefs");
            put("chef", "chefs");
            put("chief", "chiefs");
            put("dwarf", "dwarfs");
            put("grief", "griefs");
            put("gulf", "gulfs");
            put("handkerchief", "handkerchiefs");
            put("kerchief", "kerchiefs");
            put("mischief", "mischiefs");
            put("muff", "muffs");
            put("oaf", "oafs");
            put("proof", "proofs");
            put("roof", "roofs");
            put("safe", "safes");
            put("turf", "turfs");
        }
    };
    private static final HashMap<String, String> feExceptions = new HashMap<>() {
        {
            put(" safe", "safes");
        }
    };
    private static final HashMap<String, String> oExceptions = new HashMap<>() {
        {
            put("albino", "albinos");
            put("armadillo", "armadillos");
            put("auto", "autos");
            put("cameo", "cameos");
            put("cello", "cellos");
            put("combo", "combos");
            put("duo", "duos");
            put("ego", "egos");
            put("folio", "folios");
            put("halo", "halos");
            put("inferno", "infernos");
            put("lasso", "lassos");
            put("memento", "mementos");
            put("memo", "memos");
            put("piano", "pianos");
            put("photo", "photos");
            put("portfolio", "portfolios");
            put("pro", "pros");
            put("silo", "silos");
            put("solo", "solos");
            put("stereo", "stereos");
            put("studio", "studios");
            put("taco", "tacos");
            put("tattoo", "tattoos");
            put("tuxedo", "tuxedos");
            put("typo", "typos");
            put("veto", "vetoes");
            put("video", "videos");
            put("yo", "yos");
            put("zoo", "zoos");
        }
    };
    private static final HashMap<String, String> usExceptions = new HashMap<>() {
        {
            put("abacus", "abacuses");
            put("cactus", "cacti");
            put("crocus", "crocuses");
            put("fungus", "fungi");
            put("genus", "genera");
            put("octopus", "octopuses");
            put("rhombus", "rhombuses");
            put("walrus", "walruses");
        }
    };
    private static final HashMap<String, String> umExceptions = new HashMap<>() {
        {
            put("album", "albums");
            put("stadium", "stadiums");
            put("allium", "alliums");
            put("nylium", "nylium");
        }
    };
    private static final HashMap<String, String> aExceptions = new HashMap<>() {
        {
            put("agenda", "agendas");
            put("alfalfa", "alfalfas");
            put("azalea", "azaleas");
            put("aurora", "auroras");
            put("banana", "bananas");
            put("barracuda", "barracudas");
            put("cornea", "corneas");
            put("nova", "novas");
            put("phobia", "phobias");
        }
    };
    private static final HashMap<String, String> onExceptions = new HashMap<>() {
        {
            put("balloon", "balloons");
            put("beacon", "beacons");
            put("button", "buttons");
            put("carton", "cartons");
            put("cauldron", "cauldrons");
            put("dandelion", "dandelions");
            put("potion", "potions");
            put("piston", "pistons");
            put("potted dandelion", "potted dandelions");
        }
    };
    private static final HashMap<String, String> exExceptions = new HashMap<>() {
        {
            put("annex", "annexes");
            put("complex", "complexes");
            put("duplex", "duplexes");
            put("hex", "hexes");
            put("index", "indices");
        }
    };
    private static final List<String> unchanging = Arrays.asList(
            "advice", "aircraft", "bamboo", "bison", "bread", "carrots", "clay", "cobweb", "cocoa", "cod", "corn",
            "debris", "deer", "elytra", "equipment", "evidence", "farmland", "fish", "flint", "glowstone", "gold",
            "gunpowder", "ice", "information", "jewelry", "kin", "lava", "leather", "legislation", "luck", "luggage",
            "moose", "music", "mutton", "mycelium", "netherrack", "obsidian", "offspring", "paper", "potatoes",
            "quartz", "salmon", "scaffolding", "shears", "sheep", "silver", "snow", "string", "sugar", "swine",
            "trousers", "tnt", "trout", "wheat", "blaze powder", "magma cream"
    );
    private static final List<String> onlyPlurals = Arrays.asList(
            "barracks", "bellows", "cattle", "congratulations", "deer", "dregs", "eyeglasses", "gallows", "headquarters",
            "mathematics", "means", "measles", "mumps", "news", "oats", "pants", "pliers", "pajamas", "scissors",
            "series", "shears", "shorts", "species", "tongs", "tweezers", "vespers"
    );

    public static String pluralise(String str) {
        // look for unchanging first
        for (String u : unchanging) {
            if (str.equals(u)) {
                return str;
            }
        }
        // look for exceptions in xExceptions
        for (Map.Entry<String, String> map : xExceptions.entrySet()) {
            if (str.equals(map.getKey())) {
                return map.getValue();
            }
        }
        // music disc special case
        if (str.startsWith("music disc")) {
            String[] split = str.split(" ");
            return "music discs " + split[2];
        }
        // ends with exceptions which should just have an 's' plural
        for (String e : endsWithExceptionsAddS) {
            if (str.endsWith(e)) {
                return str + "s";
            }
        }
        // ends with exceptions which should be (largely) unchanged
        for (String e : endsWithExceptions) {
            if (str.endsWith(e)) {
                if (str.endsWith(" foot")) {
                    return removeEnd(str, 4) + "feet";
                }
                if (str.endsWith("cactus") || str.endsWith("fungus")) {
                    return removeEnd(str, 2) + "i";
                }
                return str;
            }
        }
        // word ends in s, x, ch, z, or sh
        if (str.endsWith("s") || str.endsWith("x") || str.endsWith("ch") || str.endsWith("sh") || str.endsWith("z")) {
            // look for usExceptions
            for (Map.Entry<String, String> map : usExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            str = str + "es";
            return str;
        }
        // Ending in "y"
        else if (str.endsWith("y")) {
            String s = removeEnd(str, 1);
            // preceded by a vowel
            if (s.endsWith("a") || s.endsWith("e") || s.endsWith("i") || s.endsWith("o") || s.endsWith("u")) {
                str = str + "s";
            } else {
                // drop the y and add ies
                str = s + "ies";
            }
            return str;
        }
        // Ends with "ff" or "ffe"
        else if (str.endsWith("ff") || str.endsWith("ffe")) {
            str = str + "s";
            return str;
        }
        // Ends with "f" (but not "ff")
        else if (str.endsWith("f")) {
            // look for exceptions first fExceptions
            for (Map.Entry<String, String> map : fExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Change the "f" to "ves"
            str = removeEnd(str, 1) + "ves";
            return str;
        }
        // Ends with "fe" (but not ffe")
        else if (str.endsWith("fe")) {
            // look for exceptions first feExceptions
            for (Map.Entry<String, String> map : feExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Change the "fe" to "ves"
            str = removeEnd(str, 2) + "ves";
            return str;
        }
        // Ends with "o"
        else if (str.endsWith("o")) {
            // look for exceptions first oExceptions
            for (Map.Entry<String, String> map : oExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Add "es"
            str += "es";
            return str;
        }
        // Ends with "is"
        else if (str.endsWith("is")) {
            // Change final "is" to "es"
            str = removeEnd(str, 2) + "es";
            return str;
        }
        // Ends with "us"
        else if (str.endsWith("us")) {
            // look for exceptions first usExceptions
            for (Map.Entry<String, String> map : usExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Change final "us" to "i"
            str = removeEnd(str, 2) + "i";
            return str;
        }
        // Ends with "um"
        else if (str.endsWith("um")) {
            // look for exceptions first umExceptions
            for (Map.Entry<String, String> map : umExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Change final "um" to "a"
            str = removeEnd(str, 2) + "a";
            return str;
        }
        // Ends with "a" but not "ia"
        else if (str.endsWith("a")) {
            // not ending is "ia"
            if (str.endsWith("ia")) {
                str = str + "s";
                return str;
            }
            // look for exceptions first aExceptions
            for (Map.Entry<String, String> map : aExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Change final "a" to "ae"
            str = removeEnd(str, 2) + "a";
            return str;
        }
        // Ends with "on"  Change final "on" to "a"
        else if (str.endsWith("on")) {
            // look for exceptions first onExceptions
            for (Map.Entry<String, String> map : onExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Change final "um" to "a"
            str = removeEnd(str, 2) + "a";
            return str;
        }
        // Ends with "ex"
        else if (str.endsWith("ex")) {
            // look for exceptions first exExceptions
            for (Map.Entry<String, String> map : exExceptions.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            // Change final "ex" to "ices"
            str = removeEnd(str, 2) + "ices";
            return str;
        } else {
            // check onlyPlurals
            for (String p : onlyPlurals) {
                if (str.equals(p)) {
                    return str;
                }
            }
            // check irregular
            for (Map.Entry<String, String> map : irregular.entrySet()) {
                if (str.equals(map.getKey())) {
                    return map.getValue();
                }
            }
            str = str + "s";
            return str;
        }
    }

    private static String removeEnd(String str, int numChars) {
        return str.substring(0, str.length() - numChars);
    }
}
