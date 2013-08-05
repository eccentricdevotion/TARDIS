/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.info;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.TreeMap;

/**
 * Articles in the TARDIS Information System were accessed by typing in an
 * initialism for the subject. Entering IF, for example, brought up the
 * database's index file.
 *
 * @author bootthanoo, eccentric_nz
 */
public enum TARDISInfoMenu {

    TIS("TARDIS Information System", "TIS|Commands|TARDIS Commands|add", "TARDIS"),
    ITEMS("TIS|Items", "TIS", "I"),
    KEY("TIS|Items|TARDIS Key", "ITEMS", "K"),
    KEY_INFO("TIS|Items|TARDIS Key|Info", "KEY", "I"),
    KEY_RECIPE("TIS|Items|TARDIS Key|Recipe", "KEY", "R"),
    SONIC("TIS|Items|Sonic Screwdriver", "ITEMS", "S"),
    SONIC_INFO("TIS|Items|Sonic Screwdriver|Info", "SONIC", "I"),
    SONIC_TYPES("TIS|Items|Sonic Screwdriver|Types", "SONIC", "T"),
    SONIC_Q("TIS|Items|Sonic Screwdriver|Types|Quartz Sonic", "SONIC_TYPES", "Q"),
    SONIC_Q_INFO("TIS|Items|Sonic Screwdriver|Types|Quartz Sonic|Info", "SONIC_Q", "I"),
    SONIC_Q_RECIPE("TIS|Items|Sonic Screwdriver|Types|Quartz Sonic Recipe", "SONIC_Q", "R"),
    SONIC_R("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic", "SONIC_TYPES", "R"),
    SONIC_R_INFO("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic|Info", "SONIC_R", "I"),
    SONIC_R_RECIPE("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic Recipe", "SONIC_R", "R"),
    SONIC_D("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic", "SONIC_TYPES", "D"),
    SONIC_D_INFO("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic|Info", "SONIC_D", "I"),
    SONIC_D_RECIPE("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic Recipe", "SONIC_D", "R"),
    SONIC_E("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic", "SONIC_TYPES", "m"),
    SONIC_E_INFO("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic|Info", "SONIC_E", "I"),
    SONIC_E_RECIPE("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic Recipe", "SONIC_E", "R"),
    SONIC_A("TIS|Items|Sonic Screwdriver|Types|Admin Sonic", "SONIC_TYPES", "A"),
    SONIC_A_INFO("TIS|Items|Sonic Screwdriver|Types|Admin Sonic|Info", "SONIC_A", "I"),
    SONIC_A_RECIPE("TIS|Items|Sonic Screwdriver|Types|Admin Sonic|Recipe", "SONIC_A", "R"),
    LOCATOR("TIS|Items|TARDIS Locator", "ITEMS", "L"),
    LOCATOR_INFO("TIS|Items|TARDIS Locator|Info", "LOCATOR", "I"),
    LOCATOR_RECIPE("TIS|Items|TARDIS Locator|Recipe", "LOCATOR", "R"),
    REMOTE("TIS|Items|Stattenheim Remote", "ITEMS", "R"),
    REMOTE_INFO("TIS|Items|Stattenheim Remote|Info", "REMOTE", "I"),
    REMOTE_RECIPE("TIS|Items|Stattenheim Remote|Recipe", "REMOTE", "R"),
    COMPONENTS("TIS|Components", "TIS", "C"),
    L_CIRCUIT("TIS|Components|Locator Circuit", "COMPONENTS", "L"),
    L_CIRCUIT_INFO("TIS|Components|Locator Circuit|Info", "L_CIRCUIT", "I"),
    L_CIRCUIT_RECIPE("TIS|Components|Locator Circuit|Recipe", "L_CIRCUIT", "R"),
    M_CIRCUIT("TIS|Components|Materialisation Circuit", "COMPONENTS", "M"),
    M_CIRCUIT_INFO("TIS|Components|Materialisation Circuit|Info", "M_CIRCUIT", "I"),
    M_CIRCUIT_RECIPE("TIS|Components|Materialisation Circuit|Recipe", "M_CIRCUIT", "R"),
    S_CIRCUIT("TIS|Components|Stattenheim Circuit", "COMPONENTS", "S"),
    S_CIRCUIT_INFO("TIS|Components|Stattenheim Circuit|Info", "S_CIRCUIT", "I"),
    S_CIRCUIT_RECIPE("TIS|Components|Stattenheim Circuit|Recipe", "S_CIRCUIT", "R"),
    TYPES("TIS|TARDIS Types", "TIS", "T"),
    BUDGET("TIS|TARDIS Types|Budget", "TYPES", "B"),
    BIGGER("TIS|TARDIS Types|Bigger", "TYPES", "i"),
    DELUXE("TIS|TARDIS Types|Deluxe", "TYPES", "D"),
    ELEVENTH("TIS|TARDIS Types|Eleventh", "TYPES", "l"),
    REDSTONE("TIS|TARDIS Types|Redstone", "TYPES", "R"),
    STEAMPUNK("TIS|TARDIS Types|Steampunk", "TYPES", "S"),
    PLANK("TIS|TARDIS Types|Plank", "TYPES", "P"),
    TOM("TIS|TARDIS Types|Tom", "TYPES", "T"),
    ARS("TIS|TARDIS Types|ARS", "TYPES", "A"),
    ROOMS("TIS|Rooms", "TIS", "R"),
    ANTIGRAVITY("TIS|Rooms|Anti-gravity", "ROOMS", "A"),
    ARBORETUM("TIS|Rooms|Arboretum", "ROOMS", "u"),
    BAKER("TIS|Rooms|Baker", "ROOMS", "B"),
    BEDROOM("TIS|Rooms|Bedroom", "ROOMS", "d"),
    EMPTY("TIS|Rooms|Empty", "ROOMS", "y"),
    FARM("TIS|Rooms|Farm", "ROOMS", "F"),
    GRAVITY("TIS|Rooms|Gravity", "ROOMS", "G"),
    GREENHOUSE("TIS|Rooms|Greenhouse", "ROOMS", "n"),
    HARMONY("TIS|Rooms|Harmony", "ROOMS", "H"),
    KITCHEN("TIS|Rooms|Kitchen", "ROOMS", "K"),
    LIBRARY("TIS|Rooms|Library", "ROOMS", "L"),
    MUSHROOM("TIS|Rooms|Mushroom", "ROOMS", "M"),
    PASSAGE("TIS|Rooms|Passage", "ROOMS", "P"),
    POOL("TIS|Rooms|Pool", "ROOMS", "o"),
    RAIL("TIS|Rooms|Rail", "ROOMS", "R"),
    STABLE("TIS|Rooms|Stable", "ROOMS", "S"),
    TRENZALORE("TIS|Rooms|Trenzalore", "ROOMS", "T"),
    VAULT("TIS|Rooms|Vault", "ROOMS", "V"),
    WOOD("TIS|Rooms|Wood", "ROOMS", "W"),
    WORKSHOP("TIS|Rooms|Workshop", "ROOMS", "h"),
    MANUAL("TIS|Manual", "TIS", "M"),
    COMMANDS("TIS|Commands", "TIS", "o"),
    TARDIS("TIS|Commands|TARDIS Commands", "COMMANDS", "T"),
    TARDIS_ABORT("TIS|Commands|TARDIS Commands|abort", "TARDIS", "ab"),
    TARDIS_ADD("TIS|Commands|TARDIS Commands|add", "TARDIS", "a"),
    TARDIS_CHAMELEON("TIS|Commands|TARDIS Commands|chameleon", "TARDIS", "c"),
    TARDIS_COMEHERE("TIS|Commands|TARDIS Commands|comehere", "TARDIS", "com"),
    TARDIS_DIRECTION("TIS|Commands|TARDIS Commands|direction", "TARDIS", "d"),
    TARDIS_EXTERMINATE("TIS|Commands|TARDIS Commands|exterminate", "TARDIS", "x"),
    TARDIS_FIND("TIS|Commands|TARDIS Commands|find", "TARDIS", "f"),
    TARDIS_HIDE("TIS|Commands|TARDIS Commands|hide", "TARDIS", "h"),
    TARDIS_HOME("TIS|Commands|TARDIS Commands|home", "TARDIS", "m"),
    TARDIS_INSIDE("TIS|Commands|TARDIS Commands|inside", "TARDIS", "i"),
    TARDIS_JETTISON("TIS|Commands|TARDIS Commands|jettison", "TARDIS", "j"),
    TARDIS_LAMPS("TIS|Commands|TARDIS Commands|lamps", "TARDIS", "la"),
    TARDIS_LIST("TIS|Commands|TARDIS Commands|list", "TARDIS", "l"),
    TARDIS_NAMEKEY("TIS|Commands|TARDIS Commands|namekey", "TARDIS", "k"),
    TARDIS_OCCUPY("TIS|Commands|TARDIS Commands|occupy", "TARDIS", "o"),
    TARDIS_REBUILD("TIS|Commands|TARDIS Commands|rebuild", "TARDIS", "b"),
    TARDIS_REMOVE("TIS|Commands|TARDIS Commands|remove", "TARDIS", "r"),
    TARDIS_REMOVESAVE("TIS|Commands|TARDIS Commands|removesave", "TARDIS", "rem"),
    TARDIS_RESCUE("TIS|Commands|TARDIS Commands|rescue", "TARDIS", "u"),
    TARDIS_ROOM("TIS|Commands|TARDIS Commands|room", "TARDIS", "roo"),
    TARDIS_SAVE("TIS|Commands|TARDIS Commands|save", "TARDIS", "s"),
    TARDIS_SECONDARY("TIS|Commands|TARDIS Commands|secondary", "TARDIS", "n"),
    TARDIS_SETDEST("TIS|Commands|TARDIS Commands|setdest", "TARDIS", "t"),
    TARDIS_UPDATE("TIS|Commands|TARDIS Commands|update", "TARDIS", "p"),
    TARDIS_VERSION("TIS|Commands|TARDIS Commands|version", "TARDIS", "v"),
    TARDISADMIN("TIS|Commands|Admin Commands", "COMMANDS", "A"),
    TARDISAREA("TIS|Commands|Area Commands", "COMMANDS", "C"),
    TARDISAREA_START("TIS|Commands|Area Commands|start", "TARDISAREA", "s"),
    TARDISAREA_END("TIS|Commands|Area Commands|end", "TARDISAREA", "n"),
    TARDISAREA_SHOW("TIS|Commands|Area Commands|show", "TARDISAREA", "h"),
    TARDISAREA_REMOVE("TIS|Commands|Area Commands|remove", "TARDISAREA", "r"),
    TARDISBIND("TIS|Commands|Bind Commands", "COMMANDS", "B"),
    TARDISBIND_SAVE("TIS|Commands|Bind Commands|save", "TARDISBIND", "s"),
    TARDISBIND_CMD("TIS|Commands|Bind Commands|cmd", "TARDISBIND", "c"),
    TARDISBIND_PLAYER("TIS|Commands|Bind Commands|player", "TARDISBIND", "p"),
    TARDISBIND_COORDS("TIS|Commands|Bind Commands|coordinates", "TARDISBIND", "o"),
    TARDISBIND_AREA("TIS|Commands|Bind Commands|area", "TARDISBIND", "a"),
    TARDISBIND_BIOME("TIS|Commands|Bind Commands|biome", "TARDISBIND", "b"),
    TARDISBIND_REMOVE("TIS|Commands|Bind Commands|remove", "TARDISBIND", "r"),
    TARDISBOOK("TIS|Commands|Book Commands", "COMMANDS", "k"),
    TARDISGRAVITY("TIS|Commands|Gravity Commands", "COMMANDS", "G"),
    TARDISPREFS("TIS|Commands|Player Preference Commands", "COMMANDS", "P"),
    TARDISPREFS_AUTO("TIS|Commands|Player Preference Commands|auto", "TARDISPREFS", "a"),
    TARDISPREFS_EPS("TIS|Commands|Player Preference Commands|eps", "TARDISPREFS", "p"),
    TARDISPREFS_FLOOR("TIS|Commands|Player Preference Commands|floor", "TARDISPREFS", "f"),
    TARDISPREFS_HADS("TIS|Commands|Player Preference Commands|hads", "TARDISPREFS", "h"),
    TARDISPREFS_ISOMORPHIC("TIS|Commands|Player Preference Commands|isomorphic", "TARDISPREFS", "i"),
    TARDISPREFS_KEY("TIS|Commands|Player Preference Commands|key", "TARDISPREFS", "k"),
    TARDISPREFS_LAMP("TIS|Commands|Player Preference Commands|lamp", "TARDISPREFS", "l"),
    TARDISPREFS_MESSAGE("TIS|Commands|Player Preference Commands|eps_message", "TARDISPREFS", "m"),
    TARDISPREFS_PLAIN("TIS|Commands|Player Preference Commands|plain", "TARDISPREFS", "n"),
    TARDISPREFS_PLATFORM("TIS|Commands|Player Preference Commands|platform", "TARDISPREFS", "t"),
    TARDISPREFS_QUOTES("TIS|Commands|Player Preference Commands|quotes", "TARDISPREFS", "q"),
    TARDISPREFS_SFX("TIS|Commands|Player Preference Commands|sfx", "TARDISPREFS", "s"),
    TARDISPREFS_SUBMARINE("TIS|Commands|Player Preference Commands|submarine", "TARDISPREFS", "u"),
    TARDISPREFS_WALL("TIS|Commands|Player Preference Commands|wall", "TARDISPREFS", "w"),
    TARDISRECIPE("TIS|Commands|Recipe Commands", "COMMANDS", "R"),
    TARDISROOM("TIS|Commands|Room Commands", "COMMANDS", "o"),
    TARDISROOM_ADD("TIS|Commands|Room Commands|add", "TARDISROOM", "a"),
    TARDISROOM_SEED("TIS|Commands|Room Commands|seed", "TARDISROOM", "s"),
    TARDISROOM_COST("TIS|Commands|Room Commands|cost", "TARDISROOM", "c"),
    TARDISROOM_OFFSET("TIS|Commands|Room Commands|offset", "TARDISROOM", "o"),
    TARDISROOM_ENABLED("TIS|Commands|Room Commands|enabled", "TARDISROOM", "n"),
    TARDISTEXTURE("TIS|Commands|Texture Commands", "COMMANDS", "x"),
    TARDISTEXTURE_ON("TIS|Commands|Texture Commands|on", "TARDISTEXTURE", "o"),
    TARDISTEXTURE_OFF("TIS|Commands|Texture Commands|off", "TARDISTEXTURE", "f"),
    TARDISTEXTURE_IN("TIS|Commands|Texture Commands|in", "TARDISTEXTURE", "i"),
    TARDISTEXTURE_OUT("TIS|Commands|Texture Commands|out", "TARDISTEXTURE", "u"),
    TARDISTRAVEL("TIS|Commands|Travel Commands", "COMMANDS", "v"),
    TARDISTRAVEL_HOME("TIS|Commands|Travel Commands|home", "TARDISTRAVEL", "h"),
    TARDISTRAVEL_PLAYER("TIS|Commands|Travel Commands|player", "TARDISTRAVEL", "p"),
    TARDISTRAVEL_COORDS("TIS|Commands|Travel Commands|coordinates", "TARDISTRAVEL", "c"),
    TARDISTRAVEL_DEST("TIS|Commands|Travel Commands|dest", "TARDISTRAVEL", "d"),
    TARDISTRAVEL_AREA("TIS|Commands|Travel Commands|area", "TARDISTRAVEL", "a"),
    TARDISTRAVEL_BIOME("TIS|Commands|Travel Commands|biome", "TARDISTRAVEL", "b"),
    TIME_TRAVEL("TIS|Manual|Time Travel", "MANUAL", "T"),
    CONSOLE_BLOCKS("TIS|Manual|Console Blocks", "MANUAL", "C"),
        CONSOLE_ARS("TIS|Manual|Console Blocks|ARS", "CONSOLE_BLOCKS", "A"),                    //A
        ARTRON("TIS|Manual|Console Blocks|Artron Energy Compacitator", "CONSOLE_BLOCKS", "r"),  //r
        BACKDOOR("TIS|Manual|Console Blocks|Backdoor", "CONSOLE_BLOCKS", "B"),                  //B
        BUTTON("TIS|Manual|Console Blocks|Button", "CONSOLE_BLOCKS", "u"),                      //u
        CHAMELEON("TIS|Manual|Console Blocks|Chameleon", "CONSOLE_BLOCKS", "C"),                //C
        CONDENSER("TIS|Manual|Console Blocks|Condenser", "CONSOLE_BLOCKS", "o"),                //o
        CREEPER("TIS|Manual|Console Blocks|Creeper", "CONSOLE_BLOCKS", "p"),                    //p
        DOOR("TIS|Manual|Console Blocks|Door", "CONSOLE_BLOCKS", "D"),                          //D
        EPS("TIS|Manual|Console Blocks|EPS", "CONSOLE_BLOCKS", "P"),                            //P
        CONSOLE_FARM("TIS|Manual|Console Blocks|Farm", "CONSOLE_BLOCKS", "m"),                  //m
        HANDBRAKE("TIS|Manual|Console Blocks|Handbrake", "CONSOLE_BLOCKS", "b"),                //a
        INFO("TIS|Manual|Console Blocks|Information System", "CONSOLE_BLOCKS", "I"),            //I
        KEYBOARD("TIS|Manual|Console Blocks|Keyboard", "CONSOLE_BLOCKS", "K"),                  //K
   CONSOLE_BLOCKS_2("TIS|Manual|Console Blocks 2", "MANUAL", "o"),
        LIGHT("TIS|Manual|Console Blocks 2|Light", "CONSOLE_BLOCKS_2", "L"),                    //L
        CONSOLE_RAIL("TIS|Manual|Console Blocks 2|Rails", "CONSOLE_BLOCKS_2", "R"),             //R
        SAVE_SIGN("TIS|Manual|Console Blocks 2|Save Sign", "CONSOLE_BLOCKS_2", "S"),            //S
        SCANNER("TIS|Manual|Console Blocks 2|Scanner", "CONSOLE_BLOCKS_2", "c"),                //c
        CONSOLE_STABLE("TIS|Manual|Console Blocks 2|Stable", "CONSOLE_BLOCKS_2", "t"),          //t
        TERMINAL("TIS|Manual|Console Blocks 2|Terminal", "CONSOLE_BLOCKS_2", "T"),              //T
        TEMPORAL("TIS|Manual|Console Blocks 2|Temporal Locator", "CONSOLE_BLOCKS_2", "m"),      //m
        WORLD_REPEATER("TIS|Manual|Console Blocks 2|World Repeater", "CONSOLE_BLOCKS_2", "W"),  //W
        X_REPEATER("TIS|Manual|Console Blocks 2|X Repeater", "CONSOLE_BLOCKS_2", "X"),          //X
        Y_REPEATER("TIS|Manual|Console Blocks 2|Y Repeater", "CONSOLE_BLOCKS_2", "Y"),          //Y
        Z_REPEATER("TIS|Manual|Console Blocks 2|Z Repeater", "CONSOLE_BLOCKS_2", "Z"),          //Z
   TARDIS_CONTROLS("TIS|Manual|TARDIS Controls", "MANUAL", "S"),
   MALFUNCTIONS("TIS|Manual|TARDIS Controls", "TARDIS_CONTROLS", "M"),
   ALT_CONTROLS("TIS|Manual|TARDIS Controls|Alternative Controls", "TARDIS_CONTROLS", "l"),
   ;
    private final String name;
    private final String parent;
    private final String key;
    private final static Map<String, TARDISInfoMenu> BY_NAME = Maps.newHashMap();

    private TARDISInfoMenu(String name, String parent, String key) {
        this.name = name;
        this.parent = parent;
        this.key = key;
    }

    /**
     * Gets the menu name of this TARDISInfoMenu
     *
     * @return name of this TARDISInfoMenu
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the parent menu of this TARDISInfoMenu
     *
     * @return parent of this TARDISInfoMenu
     */
    public String getParent() {
        return this.parent;
    }

    /**
     * Gets the menu key of this TARDISInfoMenu
     *
     * @return key of this TARDISInfoMenu
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Attempts to get the Menu with the given name. This is a normal lookup,
     * names must be the precise name they are given in the enum.
     *
     * @param name Name of the TARDISInfoMenu to get
     * @return TARDISInfoMenu if found, or null
     */
    public static TARDISInfoMenu getMenu(final String name) {
        return BY_NAME.get(name);
    }

    /**
     * Attempts to get the children of the parent menu.
     *
     * @param parent the parent menu TARDISInfoMenu.toString();
     * @return a HashMap<String, String> of child menu items, and their (ALT)key
     */
    public static TreeMap<String, String> getChildren(String parent) {
        TreeMap<String, String> children = new TreeMap<String, String>();
        for (TARDISInfoMenu tim : values()) {
            if (tim.getParent().equals(parent)) {
                String[] crumbs = tim.getName().split("[|]");
                children.put(crumbs[crumbs.length - 1], tim.getKey());
            }
        }
        return children;
    }
}
