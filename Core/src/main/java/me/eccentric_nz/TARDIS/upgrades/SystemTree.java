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
package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.custommodels.keys.SystemTreeVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;
import java.util.Locale;

public enum SystemTree {

    // TARDIS System Tree structure
    BOTH_DOWN(SystemTreeVariant.BOTH_DOWN.getKey(), null),
    LEFT_DOWN(SystemTreeVariant.LEFT_DOWN.getKey(), null),
    H_LINE(SystemTreeVariant.HORIZONTAL.getKey(), null),
    RIGHT_DOWN(SystemTreeVariant.RIGHT_DOWN.getKey(), null),
    BLANK(SystemTreeVariant.BLANK.getKey(), null),
    CLOSE(SystemTreeVariant.CLOSE.getKey(), null),
    VERTICAL(SystemTreeVariant.VERTICAL.getKey(), null),
    // TARDIS System Upgrades
    UPGRADE_TREE(SystemTreeVariant.LOCKED_TREE.getKey(), SystemTreeVariant.UNLOCKED_TREE.getKey(), 4, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlock upgrades using", "your Time Lord", "Artron Energy."), "", ""),
    ARCHITECTURE(SystemTreeVariant.LOCKED_BRANCH_DEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_BRANCH_DEPENDENT.getKey(), 9, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "architecture branch."), "UPGRADE_TREE", "branch"),
    CHAMELEON_CIRCUIT(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 18, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "exterior to be changed."), "ARCHITECTURE", "architecture"),
    ROOM_GROWING(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 27, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows rooms", "to be grown."), "CHAMELEON_CIRCUIT", "architecture"),
    DESKTOP_THEME(SystemTreeVariant.LOCKED_END.getKey(), SystemTreeVariant.UNLOCKED_END.getKey(), 36, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the desktop", "theme to be reconfigured."), "ROOM_GROWING", "architecture"),
    FEATURE(SystemTreeVariant.LOCKED_BRANCH_INDEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_BRANCH_INDEPENDENT.getKey(), 11, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "feature branch.", "Features are independent", "and don't require", "previous unlocks."), "UPGRADE_TREE", "branch"),
    SAVES(SystemTreeVariant.LOCKED_INDEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_INDEPENDENT.getKey(), 20, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows you", "to create", "saved locations"), "FEATURE", "feature"),
    MONITOR(SystemTreeVariant.LOCKED_INDEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_INDEPENDENT.getKey(), 29, Material.LIME_GLAZED_TERRACOTTA, List.of("View the", "TARDIS exterior."), "FEATURE", "feature"),
    FORCE_FIELD(SystemTreeVariant.LOCKED_INDEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_INDEPENDENT.getKey(), 38, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "to have a monster", "repelling force field."), "FEATURE", "feature"),
//    QUESTION(SystemTreeItem.SYS_LOCKED_END.getKey(), SystemTreeItem.SYS_LOCKED_END.getKey(), 47, Material.LIME_GLAZED_TERRACOTTA, List.of("Don't know", "what this is yet."), "FEATURE", "feature"),
    NAVIGATION(SystemTreeVariant.LOCKED_BRANCH_DEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_BRANCH_DEPENDENT.getKey(), 15, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "navigation branch."), "UPGRADE_TREE", "branch"),
    DISTANCE_1(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 24, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the 2x", "distance multiplier."), "NAVIGATION", "navigation"),
    DISTANCE_2(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 33, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the 3x", "distance multiplier."), "DISTANCE_1", "navigation"),
    DISTANCE_3(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 42, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the 4x", "distance multiplier."), "DISTANCE_2", "navigation"),
    INTER_DIMENSIONAL_TRAVEL(SystemTreeVariant.LOCKED_END.getKey(), SystemTreeVariant.UNLOCKED_END.getKey(), 51, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks travel to", "the Nether and End."), "DISTANCE_3", "navigation"),
    THROTTLE(SystemTreeVariant.LOCKED_BRANCH_DEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_BRANCH_DEPENDENT.getKey(), 17, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "flight branch."), "UPGRADE_TREE", "branch"),
    FASTER(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 26, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks faster", "travel speed."), "THROTTLE", "throttle"),
    RAPID(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 35, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks rapid", "travel speed."), "FASTER", "throttle"),
    WARP(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 44, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks warp", "travel speed."), "RAPID", "throttle"),
    EXTERIOR_FLIGHT(SystemTreeVariant.LOCKED_END.getKey(), SystemTreeVariant.UNLOCKED_END.getKey(), 53, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks flying the", "TARDIS exterior."), "WARP", "throttle"),
    TOOLS(SystemTreeVariant.LOCKED_BRANCH_DEPENDENT.getKey(), SystemTreeVariant.UNLOCKED_BRANCH_DEPENDENT.getKey(), 22, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks the", "tools branch."), "UPGRADE_TREE", "branch"),
    TARDIS_LOCATOR(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 31, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "to be found."), "TOOLS", "tools"),
    TELEPATHIC_CIRCUIT(SystemTreeVariant.LOCKED_DOWN.getKey(), SystemTreeVariant.UNLOCKED_DOWN.getKey(), 40, Material.LIME_GLAZED_TERRACOTTA, List.of("Unlocks cave,", "structure and", "biome travel."), "TARDIS_LOCATOR", "tools"),
    STATTENHEIM_REMOTE(SystemTreeVariant.LOCKED_END.getKey(), SystemTreeVariant.UNLOCKED_END.getKey(), 49, Material.LIME_GLAZED_TERRACOTTA, List.of("Allows the TARDIS", "to be remotely", "called to a location."), "TELEPATHIC_CIRCUIT", "tools");

    private final NamespacedKey locked;
    private final NamespacedKey unlocked;
    private final int slot;
    private final Material material;
    private final List<String> lore;
    private final String required;
    private final String branch;

    SystemTree(NamespacedKey locked, NamespacedKey unlocked, int slot, Material material, List<String> lore, String required, String branch) {
        this.locked = locked;
        this.unlocked = unlocked;
        this.slot = slot;
        this.material = material;
        this.required = required;
        this.lore = lore;
        this.branch = branch;
    }

    SystemTree(NamespacedKey locked, NamespacedKey unlocked) {
        this.locked = locked;
        this.unlocked = unlocked;
        this.slot = -1;
        this.material = Material.MAGENTA_GLAZED_TERRACOTTA;
        this.lore = null;
        this.required = "";
        this.branch = "";
    }

    public NamespacedKey getLocked() {
        return locked;
    }

    public NamespacedKey getUnlocked() {
        return unlocked;
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
            case EXTERIOR_FLIGHT -> {
                return "flight";
            }
            case INTER_DIMENSIONAL_TRAVEL -> {
                return "inter_dimension";
            }
            case TARDIS_LOCATOR -> {
                return "locator";
            }
            case TELEPATHIC_CIRCUIT -> {
                return "telepathic";
            }
            default -> {
                return toString().toLowerCase(Locale.ROOT);
            }
        }
    }
}
