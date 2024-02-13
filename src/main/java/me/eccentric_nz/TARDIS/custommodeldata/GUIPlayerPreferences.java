/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIPlayerPreferences {

    // Player Prefs Menu
    ANNOUNCE_REPEATERS(53, 25, Material.REPEATER),
    AUTONOMOUS(20, 0, Material.REPEATER),
    AUTONOMOUS_SIEGE(21, 1, Material.REPEATER),
    AUTO_RESCUE(22, 2, Material.REPEATER),
    BEACON(23, 3, Material.REPEATER),
    CLOSE_GUI(50, 4, Material.REPEATER),
    DO_NOT_DISTURB(24, 5, Material.REPEATER),
    dynamic_lamps(54, 7, Material.REPEATER),
    EMERGENCY_PROGRAMME_ONE(25, 6, Material.REPEATER),
    HOSTILE_ACTION_DISPLACEMENT_SYSTEM(26, 8, Material.REPEATER),
    HADS_TYPE(27, 9, Material.REPEATER),
    WHO_QUOTES(28, 10, Material.REPEATER),
    EXTERIOR_RENDERING_ROOM(29, 11, Material.REPEATER),
    INTERIOR_SFX(30, 12, Material.REPEATER),
    SUBMARINE_MODE(31, 13, Material.REPEATER),
    COMPANION_BUILD(33, 15, Material.REPEATER),
    PRESET_SIGN(36, 16, Material.REPEATER),
    TRAVEL_BAR(37, 17, Material.REPEATER),
    MOB_FARMING(39, 18, Material.REPEATER),
    TELEPATHIC_CIRCUIT(40, 19, Material.REPEATER),
    JUNK_TARDIS(41, 20, Material.REPEATER),
    AUTO_POWER_UP(42, 21, Material.REPEATER),
    FORCE_FIELD(43, 22, Material.REPEATER),
    MINECART_SOUNDS(45, 23, Material.REPEATER),
    EASY_DIFFICULTY(46, 24, Material.REPEATER),
    LOCK_CONTAINERS(49, 25, Material.REPEATER),
    FLIGHT_MODE(1, 26, Material.ELYTRA),
    INTERIOR_HUM_SOUND(58, 27, Material.BOWL),
    HANDBRAKE(1, 28, Material.LEVER),
    TARDIS_MAP(3, 29, Material.MAP),
    AUTONOMOUS_PREFERENCES(145, 30, Material.BOWL),
    FARMING_PREFERENCES(155, 31, Material.BOWL),
    SONIC_CONFIGURATOR(135, 32, Material.BOWL),
    ADMIN_MENU(1, 35, Material.NETHER_STAR);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIPlayerPreferences(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        String s = toString();
        if (s.startsWith("TARDIS")) {
            return "TARDIS Map";
        } else if (s.startsWith("HADS")) {
            return "HADS Type";
        } else if (s.endsWith("TARDIS")) {
            return "Junk TARDIS";
        } else if (s.endsWith("RESCUE")) {
            return "Auto-rescue";
        } else if (s.endsWith("SFX")) {
            return "Interior SFX";
        } else if (s.endsWith("GUI")) {
            return "Close GUI";
        } else {
            return TARDISStringUtils.capitalise(s);
        }
    }
}
