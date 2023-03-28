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
package me.eccentric_nz.TARDIS.custommodeldata;

import java.util.HashMap;

/**
 *
 * @author eccentric_nz
 */
public class TARDISStoneDisplay {

    public static final HashMap<Integer, String> BY_DATA = new HashMap<>() {
        {
            put(10001, "ancient");
            put(10002, "ars");
            put(10003, "bigger");
            put(10004, "budget");
            put(10005, "cave");
            put(10006, "copper");
            put(10007, "coral");
            put(10008, "delta");
            put(10009, "deluxe");
            put(10010, "division");
            put(10011, "eleventh");
            put(10012, "ender");
            put(10013, "factory");
            put(10014, "fugitive");
            put(10015, "master");
            put(10016, "mechanical");
            put(10017, "original");
            put(10018, "plank");
            put(10019, "pyramid");
            put(10020, "redstone");
            put(10021, "rotor");
            put(10022, "steampunk");
            put(10023, "thirteenth");
            put(10024, "tom");
            put(10025, "twelfth");
            put(10026, "war");
            put(10027, "weathered");
            put(10028, "small");
            put(10029, "medium");
            put(10030, "tall");
            put(10031, "legacy_bigger");
            put(10032, "legacy_budget");
            put(10033, "legacy_deluxe");
            put(10034, "legacy_eleventh");
            put(10035, "legacy_redstone");
            put(10036, "custom");
            put(10037, "grow");
            put(10038, "advanced_console");
            put(10039, "blue_box");
            put(10040, "cog");
            put(10041, "disk_storage");
            put(10042, "hexagon");
            put(10043, "roundel");
            put(10044, "roundell_offset");
            put(10045, "blue_lamp");
            put(10046, "green_lamp");
            put(10047, "purple_lamp");
            put(10048, "red_lamp");
            put(10049, "blue_lamp_on");
            put(10050, "green_lamp_on");
            put(10051, "purple_lamp_on");
            put(10052, "red_lamp_on");
            put(10053, "pandorica");
            put(10054, "compound");
            put(10055, "constructor");
            put(10056, "creative");
            put(10057, "heat_block");
            put(10058, "lab");
            put(10059, "product");
            put(10060, "reducer");
            put(10061, "siege_cube");
            put(10062, "the_moment");
        }
    };

    public static final HashMap<String, Integer> BY_NAME = new HashMap<>() {
        {
            put("ancient", 10001);
            put("ars", 10002);
            put("bigger", 10003);
            put("budget", 10004);
            put("cave", 10005);
            put("copper", 10006);
            put("coral", 10007);
            put("delta", 10008);
            put("deluxe", 10009);
            put("division", 10010);
            put("eleventh", 10011);
            put("ender", 10012);
            put("factory", 10013);
            put("fugitive", 10014);
            put("master", 10015);
            put("mechanical", 10016);
            put("original", 10017);
            put("plank", 10018);
            put("pyramid", 10019);
            put("redstone", 10020);
            put("rotor", 10021);
            put("steampunk", 10022);
            put("thirteenth", 10023);
            put("tom", 10024);
            put("twelfth", 10025);
            put("war", 10026);
            put("weathered", 10027);
            put("small", 10028);
            put("medium", 10029);
            put("tall", 10030);
            put("legacy_bigger", 10031);
            put("legacy_budget", 10032);
            put("legacy_deluxe", 10033);
            put("legacy_eleventh", 10034);
            put("legacy_redstone", 10035);
            put("custom", 10036);
            put("grow", 10037);
            put("advanced_console", 10038);
            put("blue_box", 10039);
            put("cog", 10040);
            put("disk_storage", 10041);
            put("hexagon", 10042);
            put("roundel", 10043);
            put("roundell_offset", 10044);
            put("blue_lamp", 10045);
            put("green_lamp", 10046);
            put("purple_lamp", 10047);
            put("red_lamp", 10048);
            put("blue_lamp_on", 10049);
            put("green_lamp_on", 10050);
            put("purple_lamp_on", 10051);
            put("red_lamp_on", 10052);
            put("pandorica", 10053);
            put("compound", 10054);
            put("constructor", 10055);
            put("creative", 10056);
            put("heat_block", 10057);
            put("lab", 10058);
            put("product", 10059);
            put("reducer", 10060);
            put("siege_cube", 10061);
            put("the_moment", 10062);
        }
    };

    public static boolean isLight(String s) {
        return s.startsWith("blue_lamp") || s.startsWith("green_lamp") || s.startsWith("purple_lamp") || s.startsWith("red_lamp");
    }

    public static boolean isLit(String s) {
        return s.endsWith("_on");
    }
}
