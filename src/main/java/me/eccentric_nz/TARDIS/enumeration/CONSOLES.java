/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public class CONSOLES {

    private static final HashMap<String, SCHEMATIC> byNames = new HashMap<String, SCHEMATIC>();
    private final static HashMap<String, SCHEMATIC> byPerms = new HashMap<String, SCHEMATIC>();
    private final static HashMap<String, SCHEMATIC> byMaterials = new HashMap<String, SCHEMATIC>();

    public CONSOLES() {
        byNames.put("ARS", new SCHEMATIC("QUARTZ_BLOCK", "ars", "ARS Console", true, false, true, false));
        byNames.put("BIGGER", new SCHEMATIC("GOLD_BLOCK", "bigger", "A Bigger Console", false, false, true, false));
        byNames.put("BUDGET", new SCHEMATIC("IRON_BLOCK", "budget", "Default Console", true, false, true, false));
        byNames.put("DELUXE", new SCHEMATIC("DIAMOND_BLOCK", "deluxe", "Supersized Deluxe Consol", false, true, true, false));
        byNames.put("ELEVENTH", new SCHEMATIC("EMERALD_BLOCK", "eleventh", "11th Doctor's Console", false, true, true, false));
        byNames.put("PLANK", new SCHEMATIC("BOOKSHELF", "plank", "Wood Console", true, false, false, false));
        byNames.put("REDSTONE", new SCHEMATIC("REDSTONE_BLOCK", "redstone", "Redstone Console", false, false, true, false));
        byNames.put("STEAMPUNK", new SCHEMATIC("COAL_BLOCK", "steampunk", "Steampunk Console", true, false, true, false));
        byNames.put("TOM", new SCHEMATIC("LAPIS_BLOCK", "tom", "4th Doctor's Console", true, false, false, false));
        byNames.put("WAR", new SCHEMATIC("STAINED_CLAY", "war", "War Doctor's Console", true, false, true, false));
    }

    public static HashMap<String, SCHEMATIC> getByNames() {
        return byNames;
    }

    /**
     * Attempts to get the SCHEMATIC with the given name.
     *
     * @param perm permission node of the SCHEMATIC to get
     * @return SCHEMATIC if found, or null
     */
    public static SCHEMATIC SCHEMATICFor(String perm) {
        return byPerms.get(perm);
    }

    /**
     * Attempts to get the SCHEMATIC with the given seed Material.
     *
     * @param mat Name of the SCHEMATIC to get
     * @return SCHEMATIC if found, or null
     */
    public static SCHEMATIC SCHEMATICFor(Material mat) {
        return byMaterials.get(mat.toString());
    }

    public static void loadLookups() {
        for (SCHEMATIC ts : byNames.values()) {
            if (!byPerms.containsKey(ts.getPermission())) {
                byPerms.put(ts.getPermission(), ts);
            }
            if (!byMaterials.containsKey(ts.getSeed())) {
                byMaterials.put(ts.getSeed(), ts);
            }
        }
    }

    public static HashMap<String, SCHEMATIC> getByPerms() {
        return byPerms;
    }

    public static HashMap<String, SCHEMATIC> getByMaterials() {
        return byMaterials;
    }

//    static {
//        for (SCHEMATIC tschm : byNames.values()) {
//            byPerms.put(tschm.getPermission(), tschm);
//            byMaterials.put(tschm.getSeed(), tschm);
//        }
//    }
}
