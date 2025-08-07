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
import org.bukkit.Material;

import java.util.List;

public enum GUIConfiguration {

    // Admin Menu
    ALLOW(SwitchVariant.ALLOW_ON.getFloats(), SwitchVariant.ALLOW_OFF.getFloats(), 0),
    ARCH(SwitchVariant.ARCH_ON.getFloats(), SwitchVariant.ARCH_OFF.getFloats(), 22),
    ARCHIVE(SwitchVariant.ARCHIVE_ON.getFloats(), SwitchVariant.ARCHIVE_OFF.getFloats(), 25),
    BLUEPRINTS(SwitchVariant.BLUEPRINTS_ON.getFloats(), SwitchVariant.BLUEPRINTS_OFF.getFloats(), 23),
    CREATION(SwitchVariant.CREATION_ON.getFloats(), SwitchVariant.CREATION_OFF.getFloats(), 26),
    DIFFICULTY(SwitchVariant.DIFFICULTY_ON.getFloats(), SwitchVariant.DIFFICULTY_OFF.getFloats(), 38),
    EYE_OF_HARMONY(SwitchVariant.EYE_ON.getFloats(), SwitchVariant.EYE_OFF.getFloats(), 45),
    GROWTH(SwitchVariant.GROWTH_ON.getFloats(), SwitchVariant.GROWTH_OFF.getFloats(), 36),
    HANDLES(SwitchVariant.HANDLES_ON.getFloats(), SwitchVariant.HANDLES_OFF.getFloats(), 37),
    POLICE_BOX(SwitchVariant.POLICE_BOX_ON.getFloats(), SwitchVariant.POLICE_BOX_OFF.getFloats(), 38),
    PREFERENCES(SwitchVariant.PREFERENCES_ON.getFloats(), SwitchVariant.PREFERENCES_OFF.getFloats(), 41),
    ABANDON(SwitchVariant.ABANDON_ON.getFloats(), SwitchVariant.ABANDON_OFF.getFloats(), 0),
    CIRCUITS(SwitchVariant.CIRCUIT_ON.getFloats(), SwitchVariant.CIRCUIT_OFF.getFloats(), 0),
    DEBUG(SwitchVariant.DEBUG_ON.getFloats(), SwitchVariant.DEBUG_OFF.getFloats(), 0),
    DESKTOP(SwitchVariant.THEME_ON.getFloats(), SwitchVariant.THEME_OFF.getFloats(), 0),
    JUNK(SwitchVariant.JUNK_ON.getFloats(), SwitchVariant.JUNK_OFF.getFloats(), 0),
    SIEGE(SwitchVariant.SIEGE_ON.getFloats(), SwitchVariant.SIEGE_OFF.getFloats(), 0),
    SONIC(SwitchVariant.SONIC_ON.getFloats(), SwitchVariant.SONIC_OFF.getFloats(), 0),
    TRAVEL(SwitchVariant.TRAVEL_ON.getFloats(), SwitchVariant.TRAVEL_OFF.getFloats(), 0),
    NEXT(null, null, 0, Material.BOWL),
    PREV(null, null, 0, Material.BOWL),
    PREFS(null, null, 0, Material.NETHER_STAR);
    
    private final List<Float> onFloats;
    private final List<Float> offFloats;
    private final int slot;
    private final Material material;

    GUIConfiguration(List<Float> onFloats, List<Float> offFloats, int slot, Material material) {
        this.onFloats = onFloats;
        this.offFloats = offFloats;
        this.slot = slot;
        this.material = material;
    }

    GUIConfiguration(List<Float> onFloats, List<Float> offFloats, int slot) {
        this.onFloats = onFloats;
        this.offFloats = offFloats;
        this.slot = slot;
        this.material = Material.REPEATER;
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
}
