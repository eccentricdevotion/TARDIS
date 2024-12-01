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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Buttons;
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import me.eccentric_nz.TARDIS.custommodels.keys.PrefsVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIPlayerPreferences {

    // Player Prefs Menu
    AUTONOMOUS(PrefsVariant.AUTONOMOUS_ON.getKey(), PrefsVariant.AUTONOMOUS_OFF.getKey(), 0, Material.REPEATER),
    AUTONOMOUS_SIEGE(PrefsVariant.AUTONOMOUS_SIEGE_ON.getKey(), PrefsVariant.AUTONOMOUS_SIEGE_OFF.getKey(), 1, Material.REPEATER),
    AUTO_RESCUE(PrefsVariant.AUTO_RESCUE_ON.getKey(), PrefsVariant.AUTO_RESCUE_OFF.getKey(), 2, Material.REPEATER),
    BEACON(PrefsVariant.BEACON_ON.getKey(), PrefsVariant.BEACON_OFF.getKey(), 3, Material.REPEATER),
    CLOSE_GUI(PrefsVariant.CLOSE_GUI_ON.getKey(), PrefsVariant.CLOSE_GUI_OFF.getKey(), 4, Material.REPEATER),
    DO_NOT_DISTURB(PrefsVariant.DO_NOT_DISTURB_ON.getKey(), PrefsVariant.DO_NOT_DISTURB_OFF.getKey(), 5, Material.REPEATER),
    DYNAMIC_LAMPS(PrefsVariant.DYNAMIC_LAMPS_ON.getKey(), PrefsVariant.DYNAMIC_LAMPS_OFF.getKey(), 6, Material.REPEATER),
    EMERGENCY_PROGRAMME_ONE(PrefsVariant.EMERGENCY_PROGRAM_ONE_ON.getKey(), PrefsVariant.EMERGENCY_PROGRAM_ONE_OFF.getKey(), 7, Material.REPEATER),
    HOSTILE_ACTION_DISPLACEMENT_SYSTEM(PrefsVariant.HOSTILE_ACTION_DISPLACEMENT_SYSTEM_ON.getKey(), PrefsVariant.HOSTILE_ACTION_DISPLACEMENT_SYSTEM_OFF.getKey(), 8, Material.REPEATER),
    HADS_TYPE(PrefsVariant.HADS_TYPE_DISPLACEMENT.getKey(), PrefsVariant.HADS_TYPE_DISPERSAL.getKey(), 9, Material.REPEATER),
    WHO_QUOTES(PrefsVariant.WHO_QUOTES_ON.getKey(), PrefsVariant.WHO_QUOTES_OFF.getKey(), 10, Material.REPEATER),
    EXTERIOR_RENDERING_ROOM(PrefsVariant.EXTERIOR_RENDERING_ROOM_ON.getKey(), PrefsVariant.EXTERIOR_RENDERING_ROOM_OFF.getKey(), 11, Material.REPEATER),
    INTERIOR_SFX(PrefsVariant.INTERIOR_SFX_ON.getKey(), PrefsVariant.INTERIOR_SFX_OFF.getKey(), 12, Material.REPEATER),
    SUBMARINE_MODE(PrefsVariant.SUBMARINE_MODE_ON.getKey(), PrefsVariant.SUBMARINE_MODE_OFF.getKey(), 13, Material.REPEATER),
    COMPANION_BUILD(PrefsVariant.COMPANION_BUILD_ON.getKey(), PrefsVariant.COMPANION_BUILD_OFF.getKey(), 14, Material.REPEATER),
    PRESET_SIGN(PrefsVariant.PRESET_SIGN_ON.getKey(), PrefsVariant.PRESET_SIGN_OFF.getKey(), 15, Material.REPEATER),
    TRAVEL_BAR(PrefsVariant.TRAVEL_BAR_ON.getKey(), PrefsVariant.TRAVEL_BAR_OFF.getKey(), 16, Material.REPEATER),
    MOB_FARMING(PrefsVariant.MOB_FARMING_ON.getKey(), PrefsVariant.MOB_FARMING_OFF.getKey(), 17, Material.REPEATER),
    TELEPATHIC_CIRCUIT(PrefsVariant.TELEPATHIC_CIRCUIT_ON.getKey(), PrefsVariant.TELEPATHIC_CIRCUIT_OFF.getKey(), 18, Material.REPEATER),
    JUNK_TARDIS(PrefsVariant.JUNK_TARDIS_ON.getKey(), PrefsVariant.JUNK_TARDIS_OFF.getKey(), 19, Material.REPEATER),
    AUTO_POWER_UP(PrefsVariant.AUTO_POWER_UP_ON.getKey(), PrefsVariant.AUTO_POWER_UP_OFF.getKey(), 20, Material.REPEATER),
    FORCE_FIELD(PrefsVariant.FORCE_FIELD_ON.getKey(), PrefsVariant.FORCE_FIELD_OFF.getKey(), 21, Material.REPEATER),
    MINECART_SOUNDS(PrefsVariant.MINECART_ON.getKey(), PrefsVariant.MINECART_OFF.getKey(), 22, Material.REPEATER),
    LOCK_CONTAINERS(PrefsVariant.LOCK_CONTAINERS_ON.getKey(), PrefsVariant.LOCK_CONTAINERS_OFF.getKey(), 23, Material.REPEATER),
    INFO_GUI(PrefsVariant.INFO_ON.getKey(), PrefsVariant.INFO_OFF.getKey(), 24, Material.REPEATER),
    ANNOUNCE_REPEATERS(PrefsVariant.ANNOUNCE_REPEATERS_ON.getKey(), PrefsVariant.ANNOUNCE_REPEATERS_OFF.getKey(), 25, Material.REPEATER),
    FLIGHT_MODE(PrefsVariant.FLIGHT_MODE.getKey(), null, 27, Material.ELYTRA),
    INTERIOR_HUM_SOUND(GuiVariant.INTERIOR_HUM_SOUND.getKey(), null, 28, Material.BOWL),
    AUTONOMOUS_PREFERENCES(GuiVariant.BUTTON_AUTO_PREFS.getKey(), null, 29, Material.BOWL),
    FARMING_PREFERENCES(GuiVariant.BUTTON_FARM_PREFS.getKey(), null, 30, Material.BOWL),
    PARTICLES(GuiVariant.BUTTON_PARTICLES.getKey(), null, 31, Material.BOWL),
    SONIC_CONFIGURATOR(GuiVariant.BUTTON_SONIC.getKey(), null, 32, Material.BOWL),
    HANDBRAKE(ModelledControl.HANDBRAKE.getKey(), null, 33, Material.LEVER),
    TARDIS_MAP(Buttons.BUTTON_TARDIS_MAP.getKey(), null, 34, Material.MAP),
    ADMIN_MENU(PrefsVariant.ADMIN.getKey(), null, 35, Material.NETHER_STAR);

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
