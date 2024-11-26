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

import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.NetherStar;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Repeater;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIConfiguration {

    // Admin Menu
    ALLOW(Repeater.ALLOW_ON.getKey(), Repeater.ALLOW_OFF.getKey(), 0),
    ARCH(Repeater.ARCH_ON.getKey(), Repeater.ARCH_OFF.getKey(), 22),
    ARCHIVE(Repeater.ARCHIVE_ON.getKey(), Repeater.ARCHIVE_OFF.getKey(), 25),
    BLUEPRINTS(Repeater.BLUEPRINTS_ON.getKey(), Repeater.BLUEPRINTS_OFF.getKey(), 23),
    CREATION(Repeater.CREATION_ON.getKey(), Repeater.CREATION_OFF.getKey(), 26),
    DIFFICULTY(Repeater.DIFFICULTY_ON.getKey(), Repeater.DIFFICULTY_OFF.getKey(), 38),
    EYE_OF_HARMONY(Repeater.EYE_ON.getKey(), Repeater.EYE_OFF.getKey(), 45),
    GROWTH(Repeater.GROWTH_ON.getKey(), Repeater.GROWTH_OFF.getKey(), 36),
    HANDLES(Repeater.HANDLES_ON.getKey(), Repeater.HANDLES_OFF.getKey(), 37),
    POLICE_BOX(Repeater.POLICE_BOX_ON.getKey(), Repeater.POLICE_BOX_OFF.getKey(), 38),
    PREFERENCES(Repeater.PREFERENCES_ON.getKey(), Repeater.PREFERENCES_OFF.getKey(), 41),
    ABANDON(Repeater.ABANDON_ON.getKey(), Repeater.ABANDON_OFF.getKey(), 0),
    CIRCUITS(Repeater.CIRCUIT_ON.getKey(), Repeater.CIRCUIT_OFF.getKey(), 0),
    DEBUG(Repeater.DEBUG_ON.getKey(), Repeater.DEBUG_OFF.getKey(), 0),
    DESKTOP(Repeater.THEME_ON.getKey(), Repeater.THEME_OFF.getKey(), 0),
    JUNK(Repeater.JUNK_ON.getKey(), Repeater.JUNK_OFF.getKey(), 0),
    SIEGE(Repeater.SIEGE_ON.getKey(), Repeater.SIEGE_OFF.getKey(), 0),
    SONIC(Repeater.SONIC_ON.getKey(), Repeater.SONIC_OFF.getKey(), 0),
    TRAVEL(Repeater.TRAVEL_ON.getKey(), Repeater.TRAVEL_OFF.getKey(), 0),
    NEXT(Bowl.NEXT.getKey(), null, 0, Material.BOWL),
    PREV(Bowl.PREV.getKey(), null, 0, Material.BOWL),
    PREFS(NetherStar.ADMIN.getKey(), null, 0, Material.NETHER_STAR);

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
