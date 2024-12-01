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
import me.eccentric_nz.TARDIS.custommodels.keys.PrefsVariant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIConfiguration {

    // Admin Menu
    ALLOW(PrefsVariant.ALLOW_ON.getKey(), PrefsVariant.ALLOW_OFF.getKey(), 0),
    ARCH(PrefsVariant.ARCH_ON.getKey(), PrefsVariant.ARCH_OFF.getKey(), 22),
    ARCHIVE(PrefsVariant.ARCHIVE_ON.getKey(), PrefsVariant.ARCHIVE_OFF.getKey(), 25),
    BLUEPRINTS(PrefsVariant.BLUEPRINTS_ON.getKey(), PrefsVariant.BLUEPRINTS_OFF.getKey(), 23),
    CREATION(PrefsVariant.CREATION_ON.getKey(), PrefsVariant.CREATION_OFF.getKey(), 26),
    DIFFICULTY(PrefsVariant.DIFFICULTY_ON.getKey(), PrefsVariant.DIFFICULTY_OFF.getKey(), 38),
    EYE_OF_HARMONY(PrefsVariant.EYE_ON.getKey(), PrefsVariant.EYE_OFF.getKey(), 45),
    GROWTH(PrefsVariant.GROWTH_ON.getKey(), PrefsVariant.GROWTH_OFF.getKey(), 36),
    HANDLES(PrefsVariant.HANDLES_ON.getKey(), PrefsVariant.HANDLES_OFF.getKey(), 37),
    POLICE_BOX(PrefsVariant.POLICE_BOX_ON.getKey(), PrefsVariant.POLICE_BOX_OFF.getKey(), 38),
    PREFERENCES(PrefsVariant.PREFERENCES_ON.getKey(), PrefsVariant.PREFERENCES_OFF.getKey(), 41),
    ABANDON(PrefsVariant.ABANDON_ON.getKey(), PrefsVariant.ABANDON_OFF.getKey(), 0),
    CIRCUITS(PrefsVariant.CIRCUIT_ON.getKey(), PrefsVariant.CIRCUIT_OFF.getKey(), 0),
    DEBUG(PrefsVariant.DEBUG_ON.getKey(), PrefsVariant.DEBUG_OFF.getKey(), 0),
    DESKTOP(PrefsVariant.THEME_ON.getKey(), PrefsVariant.THEME_OFF.getKey(), 0),
    JUNK(PrefsVariant.JUNK_ON.getKey(), PrefsVariant.JUNK_OFF.getKey(), 0),
    SIEGE(PrefsVariant.SIEGE_ON.getKey(), PrefsVariant.SIEGE_OFF.getKey(), 0),
    SONIC(PrefsVariant.SONIC_ON.getKey(), PrefsVariant.SONIC_OFF.getKey(), 0),
    TRAVEL(PrefsVariant.TRAVEL_ON.getKey(), PrefsVariant.TRAVEL_OFF.getKey(), 0),
    NEXT(GuiVariant.NEXT.getKey(), null, 0, Material.BOWL),
    PREV(GuiVariant.PREV.getKey(), null, 0, Material.BOWL),
    PREFS(PrefsVariant.ADMIN.getKey(), null, 0, Material.NETHER_STAR);

    private final NamespacedKey onModel;
    private final NamespacedKey offModel;
    private final int slot;
    private final Material material;

    GUIConfiguration(NamespacedKey onModel, NamespacedKey offModel, int slot, Material material) {
        this.onModel = onModel;
        this.offModel = offModel;
        this.slot = slot;
        this.material = material;
    }

    GUIConfiguration(NamespacedKey onModel, NamespacedKey offModel, int slot) {
        this.onModel = onModel;
        this.offModel = offModel;
        this.slot = slot;
        this.material = Material.REPEATER;
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
}
