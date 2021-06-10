/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.custommodeldata;

import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIControlCentre {

    // Control Centre
    BUTTON_CLOSE(1, 53, Material.BOWL),
    BUTTON_AREAS(12, 27, Material.BOWL),
    BUTTON_ARS(13, 2, Material.BOWL),
    BUTTON_ARTRON(14, 6, Material.BOWL),
    BUTTON_BACK(15, 18, Material.BOWL),
    BUTTON_CHAMELEON(17, 4, Material.BOWL),
    BUTTON_DIRECTION(19, 40, Material.BOWL),
    BUTTON_HIDE(20, 22, Material.BOWL),
    BUTTON_INFO(21, 24, Material.BOWL),
    BUTTON_PREFS(23, 17, Material.BOWL),
    BUTTON_RANDOM(24, 0, Material.BOWL),
    BUTTON_REBUILD(25, 31, Material.BOWL),
    BUTTON_SAVES(26, 9, Material.BOWL),
    BUTTON_SCANNER(27, 15, Material.BOWL),
    BUTTON_TARDIS_MAP(3, 47, Material.MAP),
    BUTTON_TEMP(29, 49, Material.BOWL),
    BUTTON_TERM(30, 36, Material.BOWL),
    BUTTON_THEME(31, 11, Material.BOWL),
    BUTTON_TRANSMAT(133, 33, Material.BOWL),
    BUTTON_ZERO(32, 8, Material.BOWL),
    COMPANIONS_MENU(45, 26, Material.BOWL),
    BUTTON_LIGHTS(16, 29, Material.REPEATER),
    BUTTON_POWER(17, 20, Material.REPEATER),
    BUTTON_SIEGE(18, 13, Material.REPEATER),
    BUTTON_TOGGLE(19, 38, Material.REPEATER);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIControlCentre(int customModelData, int slot, Material material) {
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
        return TARDISStringUtils.sentenceCase(s);
    }
}
