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
package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.List;

public enum SystemTree {

    // TARDIS System Upgrades
    T_UP(1),
    T_RIGHT(2),
    H_LINE(3),
    D_RIGHT(4),
    BLANK(5),
    CLOSE(6),
    CROSS(7),
    UPGRADE_TREE(1001, 2, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlock upgrades using", "your Time Lord", "Artron Energy."), "", ""),
    ARCHITECTURE(1002, 9, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "architecture branch."), "UPGRADE_TREE", "branch"),
    CHAMELEON_CIRCUIT(1005, 19, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "exterior to be changed."), "ARCHITECTURE", "architecture"),
    ROOM_GROWING(1005, 28, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows rooms", "to be grown."), "CHAMELEON_CIRCUIT", "architecture"),
    DESKTOP_THEME(1005, 37, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the desktop", "theme to be reconfigured."), "ROOM_GROWING", "architecture"),
    NAVIGATION(1003, 13, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "navigation branch."), "UPGRADE_TREE", "branch"),
    SAVES(1005, 23, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows you", "to create", "saved locations"), "NAVIGATION", "navigation"),
    DISTANCE_1(1006, 21, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the 2x", "distance multiplier."), "SAVES", "navigation"),
    DISTANCE_2(1006, 30, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the 3x", "distance multiplier."), "DISTANCE_1", "navigation"),
    DISTANCE_3(1006, 39, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the 4x", "distance multiplier."), "DISTANCE_2", "navigation"),
    TELEPATHIC_CIRCUIT(1005, 32, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks cave,", "structure and", "biome travel."), "DISTANCE_3", "navigation"),
    INTER_DIMENSIONAL_TRAVEL(1005, 41, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks travel to", "the Nether and End."), "DISTANCE_3", "navigation"),
    EXTERIOR_FLIGHT(1005, 50, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks flying the", "TARDIS exterior."), "INTER_DIMENSIONAL_TRAVEL", "navigation"),
    TOOLS(1004, 16, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "tools branch."), "UPGRADE_TREE", "branch"),
    TARDIS_LOCATOR(1005, 26, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "to be found."), "TOOLS", "tools"),
    BIOME_READER(1005, 35, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows biomes", "to be stored", "for travel."), "TARDIS_LOCATOR", "tools"),
    FORCE_FIELD(1005, 44, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "to have a monster", "repelling force field."), "BIOME_READER", "tools"),
    STATTENHEIM_REMOTE(1005, 53, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "to be remotely", "called to a location."), "FORCE_FIELD", "tools"),
    MONITOR(1006, 42, Material.LIME_GLAZED_TERRACOTTA, List.of("View the", "TARDIS exterior."), "FORCE_FIELD", "tools");

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final List<String> lore;
    private final String required;
    private final String branch;

    SystemTree(int customModelData, int slot, Material material, List<String> lore, String required, String branch) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.required = required;
        this.lore = lore;
        this.branch = branch;
    }

    SystemTree(int customModelData) {
        this.customModelData = customModelData;
        this.slot = -1;
        this.material = Material.MAGENTA_GLAZED_TERRACOTTA;
        this.lore = null;
        this.required = "";
        this.branch = "";
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

    public List<String> getLore() {
        return lore;
    }

    public String getRequired() {
        return required;
    }

    public String getBranch() {
        return branch;
    }

    public String getName() {
        String s = toString();
        return TARDISStringUtils.sentenceCase(s);
    }

    public String getDatabaseName() {
        switch (this) {
            case CHAMELEON_CIRCUIT -> {
                return "chameleon";
            }
            case ROOM_GROWING -> {
                return "rooms";
            }
            case DESKTOP_THEME -> {
                return "desktop";
            }
            case INTER_DIMENSIONAL_TRAVEL -> {
                return "inter_dimension";
            }
            case TARDIS_LOCATOR -> {
                return "locator";
            }
            default -> {
                return toString().toLowerCase();
            }
        }
    }
}
