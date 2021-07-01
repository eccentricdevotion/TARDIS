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

import me.eccentric_nz.tardis.utility.TardisStringUtils;
import org.bukkit.Material;

public enum GuiChemistry {

    // TARDIS Areas
    CLOSE(1, 1, Material.BOWL),
    INFO(57, 1, Material.BOWL),
    CHECK(89, 1, Material.BOWL),
    CRAFT(90, 1, Material.BOWL),
    ELECTRONS(91, 1, Material.BOWL),
    NEUTRONS(92, 1, Material.BOWL),
    PROTONS(93, 1, Material.BOWL),
    REDUCE(94, 1, Material.BOWL),
    SCROLL_DOWN(7, 1, Material.ARROW),
    SCROLL_UP(8, 1, Material.ARROW),
    PLUS(9, 1, Material.ARROW),
    MINUS(10, 1, Material.ARROW),
    ELEMENTS(10000999, 1, Material.FEATHER),
    COMPOUNDS(1, 1, Material.GLASS_BOTTLE),
    PRODUCTS(1, 1, Material.CRAFTING_TABLE);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GuiChemistry(int customModelData, int slot, Material material) {
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
        return TardisStringUtils.sentenceCase(s);
    }
}
