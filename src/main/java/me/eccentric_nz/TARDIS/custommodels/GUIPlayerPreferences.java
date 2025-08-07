/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.List;

public enum GUIPlayerPreferences {

    // Player Prefs Menu
    ANNOUNCE_REPEATERS(SwitchVariant.ANNOUNCE_REPEATERS_ON.getFloats(), SwitchVariant.ANNOUNCE_REPEATERS_OFF.getFloats(), 0, Material.REPEATER),
    AUTONOMOUS(SwitchVariant.AUTONOMOUS_ON.getFloats(), SwitchVariant.AUTONOMOUS_OFF.getFloats(), 1, Material.REPEATER),
    AUTO_RESCUE(SwitchVariant.AUTO_RESCUE_ON.getFloats(), SwitchVariant.AUTO_RESCUE_OFF.getFloats(), 2, Material.REPEATER),
    TRAVEL_BAR(SwitchVariant.TRAVEL_BAR_ON.getFloats(), SwitchVariant.TRAVEL_BAR_OFF.getFloats(), 3, Material.REPEATER),
    BEACON(SwitchVariant.BEACON_ON.getFloats(), SwitchVariant.BEACON_OFF.getFloats(), 4, Material.REPEATER),
    COMPANION_BUILD(SwitchVariant.COMPANION_BUILD_ON.getFloats(), SwitchVariant.COMPANION_BUILD_OFF.getFloats(), 5, Material.REPEATER),
    DIALOGS(SwitchVariant.DIALOGS_ON.getFloats(), SwitchVariant.DIALOGS_OFF.getFloats(), 6, Material.REPEATER),
    DO_NOT_DISTURB(SwitchVariant.DO_NOT_DISTURB_ON.getFloats(), SwitchVariant.DO_NOT_DISTURB_OFF.getFloats(), 7, Material.REPEATER),
    OPEN_DISPLAY_DOOR(SwitchVariant.OPEN_DISPLAY_DOOR_ON.getFloats(), SwitchVariant.OPEN_DISPLAY_DOOR_OFF.getFloats(), 8, Material.REPEATER),
    DYNAMIC_LAMPS(SwitchVariant.DYNAMIC_LAMPS_ON.getFloats(), SwitchVariant.DYNAMIC_LAMPS_OFF.getFloats(), 9, Material.REPEATER),
    EMERGENCY_PROGRAMME_ONE(SwitchVariant.EMERGENCY_PROGRAM_ONE_ON.getFloats(), SwitchVariant.EMERGENCY_PROGRAM_ONE_OFF.getFloats(), 10, Material.REPEATER),
    MOB_FARMING(SwitchVariant.MOB_FARMING_ON.getFloats(), SwitchVariant.MOB_FARMING_OFF.getFloats(), 11, Material.REPEATER),
    FORCE_FIELD(SwitchVariant.FORCE_FIELD_ON.getFloats(), SwitchVariant.FORCE_FIELD_OFF.getFloats(), 12, Material.REPEATER),
    CLOSE_GUI(SwitchVariant.CLOSE_GUI_ON.getFloats(), SwitchVariant.CLOSE_GUI_OFF.getFloats(), 13, Material.REPEATER),
    HOSTILE_ACTION_DISPLACEMENT_SYSTEM(SwitchVariant.HOSTILE_ACTION_DISPLACEMENT_SYSTEM_ON.getFloats(), SwitchVariant.HOSTILE_ACTION_DISPLACEMENT_SYSTEM_OFF.getFloats(), 14, Material.REPEATER),
    HADS_TYPE(SwitchVariant.HADS_TYPE_DISPLACEMENT.getFloats(), SwitchVariant.HADS_TYPE_DISPERSAL.getFloats(), 15, Material.REPEATER),
    INFO_GUI(SwitchVariant.INFO_ON.getFloats(), SwitchVariant.INFO_OFF.getFloats(), 16, Material.REPEATER),
    ISOMORPHIC(SwitchVariant.ISOMORPHIC_ON.getFloats(), SwitchVariant.ISOMORPHIC_OFF.getFloats(), 17, Material.REPEATER),
    JUNK_TARDIS(SwitchVariant.JUNK_TARDIS_ON.getFloats(), SwitchVariant.JUNK_TARDIS_OFF.getFloats(), 18, Material.REPEATER),
    CONSOLE_LABELS(SwitchVariant.CONSOLE_LABELS_ON.getFloats(), SwitchVariant.CONSOLE_LABELS_OFF.getFloats(), 19, Material.REPEATER),
    LOCK_CONTAINERS(SwitchVariant.LOCK_CONTAINERS_ON.getFloats(), SwitchVariant.LOCK_CONTAINERS_OFF.getFloats(), 20, Material.REPEATER),
    MINECART_SOUNDS(SwitchVariant.MINECART_ON.getFloats(), SwitchVariant.MINECART_OFF.getFloats(), 21, Material.REPEATER),
    AUTO_POWER_UP(SwitchVariant.AUTO_POWER_UP_ON.getFloats(), SwitchVariant.AUTO_POWER_UP_OFF.getFloats(), 22, Material.REPEATER),
    WHO_QUOTES(SwitchVariant.WHO_QUOTES_ON.getFloats(), SwitchVariant.WHO_QUOTES_OFF.getFloats(), 23, Material.REPEATER),
    EXTERIOR_RENDERING_ROOM(SwitchVariant.EXTERIOR_RENDERING_ROOM_ON.getFloats(), SwitchVariant.EXTERIOR_RENDERING_ROOM_OFF.getFloats(), 24, Material.REPEATER),
    AUTONOMOUS_SIEGE(SwitchVariant.AUTONOMOUS_SIEGE_ON.getFloats(), SwitchVariant.AUTONOMOUS_SIEGE_OFF.getFloats(), 25, Material.REPEATER),
    PRESET_SIGN(SwitchVariant.PRESET_SIGN_ON.getFloats(), SwitchVariant.PRESET_SIGN_OFF.getFloats(), 26, Material.REPEATER),
    INTERIOR_SFX(SwitchVariant.INTERIOR_SFX_ON.getFloats(), SwitchVariant.INTERIOR_SFX_OFF.getFloats(), 27, Material.REPEATER),
    SUBMARINE_MODE(SwitchVariant.SUBMARINE_MODE_ON.getFloats(), SwitchVariant.SUBMARINE_MODE_OFF.getFloats(), 28, Material.REPEATER),
    TELEPATHIC_CIRCUIT(SwitchVariant.TELEPATHIC_CIRCUIT_ON.getFloats(), SwitchVariant.TELEPATHIC_CIRCUIT_OFF.getFloats(), 29, Material.REPEATER),
    // main menu
    FLIGHT_MODE(null, null, 0, Material.ELYTRA),
    INTERIOR_HUM_SOUND(null, null, 2, Material.BOWL),
    AUTONOMOUS_PREFERENCES(null, null, 4, Material.BOWL),
    FARMING_PREFERENCES(null, null, 6, Material.BOWL),
    PARTICLES(null, null, 8, Material.BOWL),
    SONIC_CONFIGURATOR(null, null, 10, Material.BOWL),
    HANDBRAKE(null, null, 12, Material.LEVER),
    TARDIS_MAP(null, null, 14, Material.MAP),
    GENERAL_PREFERENCES_MENU(null, null, 24, Material.NETHER_STAR),
    ADMIN_MENU(null, null, 26, Material.NETHER_STAR);

    private final List<Float> onFloats;
    private final List<Float> offFloats;
    private final int slot;
    private final Material material;

    GUIPlayerPreferences(List<Float> onFloats, List<Float> offFloats, int slot, Material material) {
        this.onFloats = onFloats;
        this.offFloats = offFloats;
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

    public List<Float> getOnFloats() {
        return onFloats;
    }

    public List<Float> getOffFloats() {
        return offFloats;
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
