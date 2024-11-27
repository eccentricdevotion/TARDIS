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

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIPlayerPreferences {

    // Player Prefs Menu
    AUTONOMOUS(Repeater.AUTONOMOUS_ON.getKey(), Repeater.AUTONOMOUS_OFF.getKey(), 0, Material.REPEATER),
    AUTONOMOUS_SIEGE(Repeater.AUTONOMOUS_SIEGE_ON.getKey(), Repeater.AUTONOMOUS_SIEGE_OFF.getKey(), 1, Material.REPEATER),
    AUTO_RESCUE(Repeater.AUTO_RESCUE_ON.getKey(), Repeater.AUTO_RESCUE_OFF.getKey(), 2, Material.REPEATER),
    BEACON(Repeater.BEACON_ON.getKey(), Repeater.BEACON_OFF.getKey(), 3, Material.REPEATER),
    CLOSE_GUI(Repeater.CLOSE_GUI_ON.getKey(), Repeater.CLOSE_GUI_OFF.getKey(), 4, Material.REPEATER),
    DO_NOT_DISTURB(Repeater.DO_NOT_DISTURB_ON.getKey(), Repeater.DO_NOT_DISTURB_OFF.getKey(), 5, Material.REPEATER),
    DYNAMIC_LAMPS(Repeater.DYNAMIC_LAMPS_ON.getKey(), Repeater.DYNAMIC_LAMPS_OFF.getKey(), 6, Material.REPEATER),
    EMERGENCY_PROGRAMME_ONE(Repeater.EMERGENCY_PROGRAM_ONE_ON.getKey(), Repeater.EMERGENCY_PROGRAM_ONE_OFF.getKey(), 7, Material.REPEATER),
    HOSTILE_ACTION_DISPLACEMENT_SYSTEM(Repeater.HOSTILE_ACTION_DISPLACEMENT_SYSTEM_ON.getKey(), Repeater.HOSTILE_ACTION_DISPLACEMENT_SYSTEM_OFF.getKey(), 8, Material.REPEATER),
    HADS_TYPE(Repeater.HADS_TYPE_DISPLACEMENT.getKey(), Repeater.HADS_TYPE_DISPERSAL.getKey(), 9, Material.REPEATER),
    WHO_QUOTES(Repeater.WHO_QUOTES_ON.getKey(), Repeater.WHO_QUOTES_OFF.getKey(), 10, Material.REPEATER),
    EXTERIOR_RENDERING_ROOM(Repeater.EXTERIOR_RENDERING_ROOM_ON.getKey(), Repeater.EXTERIOR_RENDERING_ROOM_OFF.getKey(), 11, Material.REPEATER),
    INTERIOR_SFX(Repeater.INTERIOR_SFX_ON.getKey(), Repeater.INTERIOR_SFX_OFF.getKey(), 12, Material.REPEATER),
    SUBMARINE_MODE(Repeater.SUBMARINE_MODE_ON.getKey(), Repeater.SUBMARINE_MODE_OFF.getKey(), 13, Material.REPEATER),
    COMPANION_BUILD(Repeater.COMPANION_BUILD_ON.getKey(), Repeater.COMPANION_BUILD_OFF.getKey(), 14, Material.REPEATER),
    PRESET_SIGN(Repeater.PRESET_SIGN_ON.getKey(), Repeater.PRESET_SIGN_OFF.getKey(), 15, Material.REPEATER),
    TRAVEL_BAR(Repeater.TRAVEL_BAR_ON.getKey(), Repeater.TRAVEL_BAR_OFF.getKey(), 16, Material.REPEATER),
    MOB_FARMING(Repeater.MOB_FARMING_ON.getKey(), Repeater.MOB_FARMING_OFF.getKey(), 17, Material.REPEATER),
    TELEPATHIC_CIRCUIT(Repeater.TELEPATHIC_CIRCUIT_ON.getKey(), Repeater.TELEPATHIC_CIRCUIT_OFF.getKey(), 18, Material.REPEATER),
    JUNK_TARDIS(Repeater.JUNK_TARDIS_ON.getKey(), Repeater.JUNK_TARDIS_OFF.getKey(), 19, Material.REPEATER),
    AUTO_POWER_UP(Repeater.AUTO_POWER_UP_ON.getKey(), Repeater.AUTO_POWER_UP_OFF.getKey(), 20, Material.REPEATER),
    FORCE_FIELD(Repeater.FORCE_FIELD_ON.getKey(), Repeater.FORCE_FIELD_OFF.getKey(), 21, Material.REPEATER),
    MINECART_SOUNDS(Repeater.MINECART_ON.getKey(), Repeater.MINECART_OFF.getKey(), 22, Material.REPEATER),
    LOCK_CONTAINERS(Repeater.LOCK_CONTAINERS_ON.getKey(), Repeater.LOCK_CONTAINERS_OFF.getKey(), 23, Material.REPEATER),
    INFO_GUI(Repeater.INFO_ON.getKey(), Repeater.INFO_OFF.getKey(), 24, Material.REPEATER),
    ANNOUNCE_REPEATERS(Repeater.ANNOUNCE_REPEATERS_ON.getKey(), Repeater.ANNOUNCE_REPEATERS_OFF.getKey(), 25, Material.REPEATER),
    FLIGHT_MODE(Elytra.FLIGHT_MODE.getKey(), null, 27, Material.ELYTRA),
    INTERIOR_HUM_SOUND(Bowl.INTERIOR_HUM_SOUND.getKey(), null, 28, Material.BOWL),
    AUTONOMOUS_PREFERENCES(Bowl.BUTTON_AUTO_PREFS.getKey(), null, 29, Material.BOWL),
    FARMING_PREFERENCES(Bowl.BUTTON_FARM_PREFS.getKey(), null, 30, Material.BOWL),
    PARTICLES(Bowl.BUTTON_PARTICLES.getKey(), null, 31, Material.BOWL),
    SONIC_CONFIGURATOR(Bowl.BUTTON_SONIC.getKey(), null, 32, Material.BOWL),
    HANDBRAKE(Lever.HANDBRAKE.getKey(), null, 33, Material.LEVER),
    TARDIS_MAP(Map.BUTTON_TARDIS_MAP.getKey(), null, 34, Material.MAP),
    ADMIN_MENU(NetherStar.ADMIN.getKey(), null, 35, Material.NETHER_STAR);

    private final NamespacedKey onModel;
    private final NamespacedKey offModel;
    private final int slot;
    private final Material material;

    GUIPlayerPreferences(NamespacedKey onModel, NamespacedKey offModel, int slot, Material material) {
        this.onModel = onModel;
        this.offModel = offModel;
        this.slot = slot;
        this.material = material;
    }

    public static GUIPlayerPreferences fromString(String s) {
        for (GUIPlayerPreferences g : values()) {
            if (g.getName().equals(s)) {
                return g;
            }
        }
        return ANNOUNCE_REPEATERS;
    }

    public NamespacedKey getOnModel() {
        return onModel;
    }

    public NamespacedKey getOffModel() {
        return offModel;
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
            return s.startsWith("INFO") ? "Info GUI" : "Close GUI";
        } else {
            return TARDISStringUtils.capitalise(s);
        }
    }
}
