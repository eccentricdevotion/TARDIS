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

import me.eccentric_nz.TARDIS.custommodels.keys.Button;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIConfiguration {

    // Admin Menu
    ALLOW(SwitchVariant.ALLOW_ON.getKey(), SwitchVariant.ALLOW_OFF.getKey(), 0),
    ARCH(SwitchVariant.ARCH_ON.getKey(), SwitchVariant.ARCH_OFF.getKey(), 22),
    ARCHIVE(SwitchVariant.ARCHIVE_ON.getKey(), SwitchVariant.ARCHIVE_OFF.getKey(), 25),
    BLUEPRINTS(SwitchVariant.BLUEPRINTS_ON.getKey(), SwitchVariant.BLUEPRINTS_OFF.getKey(), 23),
    CREATION(SwitchVariant.CREATION_ON.getKey(), SwitchVariant.CREATION_OFF.getKey(), 26),
    DIFFICULTY(SwitchVariant.DIFFICULTY_ON.getKey(), SwitchVariant.DIFFICULTY_OFF.getKey(), 38),
    EYE_OF_HARMONY(SwitchVariant.EYE_ON.getKey(), SwitchVariant.EYE_OFF.getKey(), 45),
    GROWTH(SwitchVariant.GROWTH_ON.getKey(), SwitchVariant.GROWTH_OFF.getKey(), 36),
    HANDLES(SwitchVariant.HANDLES_ON.getKey(), SwitchVariant.HANDLES_OFF.getKey(), 37),
    POLICE_BOX(SwitchVariant.POLICE_BOX_ON.getKey(), SwitchVariant.POLICE_BOX_OFF.getKey(), 38),
    PREFERENCES(SwitchVariant.PREFERENCES_ON.getKey(), SwitchVariant.PREFERENCES_OFF.getKey(), 41),
    ABANDON(SwitchVariant.ABANDON_ON.getKey(), SwitchVariant.ABANDON_OFF.getKey(), 0),
    CIRCUITS(SwitchVariant.CIRCUIT_ON.getKey(), SwitchVariant.CIRCUIT_OFF.getKey(), 0),
    DEBUG(SwitchVariant.DEBUG_ON.getKey(), SwitchVariant.DEBUG_OFF.getKey(), 0),
    DESKTOP(SwitchVariant.THEME_ON.getKey(), SwitchVariant.THEME_OFF.getKey(), 0),
    JUNK(SwitchVariant.JUNK_ON.getKey(), SwitchVariant.JUNK_OFF.getKey(), 0),
    SIEGE(SwitchVariant.SIEGE_ON.getKey(), SwitchVariant.SIEGE_OFF.getKey(), 0),
    SONIC(SwitchVariant.SONIC_ON.getKey(), SwitchVariant.SONIC_OFF.getKey(), 0),
    TRAVEL(SwitchVariant.TRAVEL_ON.getKey(), SwitchVariant.TRAVEL_OFF.getKey(), 0),
    NEXT(GuiVariant.NEXT.getKey(), null, 0, Material.BOWL),
    PREV(GuiVariant.PREV.getKey(), null, 0, Material.BOWL),
    PREFS(Button.ADMIN.getKey(), null, 0, Material.NETHER_STAR);

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
