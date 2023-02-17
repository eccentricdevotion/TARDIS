/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public enum GUIGeneticManipulator {

    // Genetic Manipulator
    BUTTON_MASTER(3, 45, Material.COMPARATOR),
    BUTTON_AGE(2, 47, Material.HOPPER),
    BUTTON_TYPE(1, 48, Material.CYAN_DYE),
    BUTTON_OPTS(1, 49, Material.LEAD),
    BUTTON_RESTORE(1, 51, Material.APPLE),
    BUTTON_DNA(1, 52, Material.WRITABLE_BOOK),
    BUTTON_CANCEL(16, 53, Material.BOWL),
    BUTTON_TWA(147, 44, Material.BOWL),

    CYBERMAN(1, 1, Material.IRON_INGOT),
    DALEK(1, 1, Material.SLIME_BALL),
    DALEK_SEC(1,1, Material.MANGROVE_PROPAGULE),
//    DAVROS(1,1, Material.),
    EMPTY_CHILD(1, 1, Material.SUGAR),
    ICE_WARRIOR(1, 1, Material.SNOWBALL),
    HATH(1,1, Material.PUFFERFISH),
    HEADLESS_MONK(1,1, Material.RED_CANDLE),
    K9(6, 1, Material.BONE),
    JUDOON(1, 1, Material.YELLOW_DYE),
    MIRE(1,1, Material.NETHERITE_SCRAP),
    OOD(1, 1, Material.ROTTEN_FLESH),
    RACNOSS(1,1, Material.SPIDER_EYE),
    SEA_DEVIL(1,1, Material.KELP),
    SILENT(4, 1, Material.END_STONE),
    SILURIAN(1, 1, Material.FEATHER),
    SLITHEEN(1,1, Material.TURTLE_EGG),
    SONTARAN(1, 1, Material.POTATO),
    STRAX(1, 1, Material.BAKED_POTATO),
    TOCLAFANE(3, 1, Material.GUNPOWDER),
    VASHTA_NERADA(2, 1, Material.BOOK),
    WEEPING_ANGEL(1, 1, Material.BRICK),
    ZYGON(1, 1, Material.PAINTING);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIGeneticManipulator(int customModelData, int slot, Material material) {
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
        return TARDIS.plugin.getLanguage().getString(s);
    }
}
