/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms.library;

import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.util.Vector;

import java.util.HashMap;

public enum EnchantmentShelf {

    // bottom
    // all-purpose
    CURSE_OF_VANISHING(Enchantment.VANISHING_CURSE, "Curse of Vanishing", 1, EnchantmentCategory.ALL_PURPOSE, new Vector(3, 3, 2), BlockFace.SOUTH),
    MENDING(Enchantment.MENDING, "Mending", 1, EnchantmentCategory.ALL_PURPOSE, new Vector(4, 3, 2), BlockFace.SOUTH),
    UNBREAKING(Enchantment.UNBREAKING, "Unbreaking", 3, EnchantmentCategory.ALL_PURPOSE, new Vector(5, 3, 2), BlockFace.SOUTH),
    // armour
    AQUA_AFFINITY(Enchantment.AQUA_AFFINITY, "Aqua Affinity", 1, EnchantmentCategory.ARMOUR, new Vector(6, 3, 2), BlockFace.SOUTH),
    BLAST_PROTECTION(Enchantment.BLAST_PROTECTION, "Blast Protection", 4, EnchantmentCategory.ARMOUR, new Vector(7, 3, 2), BlockFace.SOUTH),
    CURSE_OF_BINDING(Enchantment.BINDING_CURSE, "Curse of Binding", 1, EnchantmentCategory.ARMOUR, new Vector(9, 3, 2), BlockFace.SOUTH),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER, "Depth Strider", 3, EnchantmentCategory.ARMOUR, new Vector(10, 3, 2), BlockFace.SOUTH),
    FEATHER_FALLING(Enchantment.FEATHER_FALLING, "Feather Falling", 4, EnchantmentCategory.ARMOUR, new Vector(11, 3, 2), BlockFace.SOUTH),
    FIRE_PROTECTION(Enchantment.FIRE_PROTECTION, "Fire Protection", 4, EnchantmentCategory.ARMOUR, new Vector(12, 3, 2), BlockFace.SOUTH),
    FROST_WALKER(Enchantment.FROST_WALKER, "Frost Walker", 2, EnchantmentCategory.ARMOUR, new Vector(13, 3, 2), BlockFace.SOUTH),
    // left
    PROJECTILE_PROTECTION(Enchantment.PROJECTILE_PROTECTION, "Projectile Protection", 4, EnchantmentCategory.ARMOUR, new Vector(14, 3, 3), BlockFace.WEST),
    PROTECTION(Enchantment.PROTECTION, "Protection", 4, EnchantmentCategory.ARMOUR, new Vector(14, 3, 4), BlockFace.WEST),
    RESPIRATION(Enchantment.RESPIRATION, "Respiration", 3, EnchantmentCategory.ARMOUR, new Vector(14, 3, 5), BlockFace.WEST),
    RIPTIDE(Enchantment.RIPTIDE, "Riptide", 3, EnchantmentCategory.ARMOUR, new Vector(14, 3, 6), BlockFace.WEST),
    SOUL_SPEED(Enchantment.SOUL_SPEED, "Soul Speed", 3, EnchantmentCategory.ARMOUR, new Vector(14, 3, 7), BlockFace.WEST),
    SWIFT_SNEAK(Enchantment.SWIFT_SNEAK, "Swift Sneak", 3, EnchantmentCategory.ARMOUR, new Vector(14, 3, 9), BlockFace.WEST),
    THORNS(Enchantment.THORNS, "Thorns", 3, EnchantmentCategory.ARMOUR, new Vector(14, 3, 10), BlockFace.WEST),
    // melee weapons
    BANE_OF_ARTHROPODS(Enchantment.BANE_OF_ARTHROPODS, "Bane of Arthropods", 5, EnchantmentCategory.MELEE_WEAPONS, new Vector(14, 3, 11), BlockFace.WEST),
    EFFICIENCY(Enchantment.EFFICIENCY, "Efficiency", 5, EnchantmentCategory.MELEE_WEAPONS, new Vector(14, 3, 12), BlockFace.WEST),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT, "Fire Aspect", 2, EnchantmentCategory.MELEE_WEAPONS, new Vector(14, 3, 13), BlockFace.WEST),
    // top
    IMPALING(Enchantment.IMPALING, "Impaling", 5, EnchantmentCategory.MELEE_WEAPONS, new Vector(13, 3, 14), BlockFace.NORTH),
    KNOCKBACK(Enchantment.KNOCKBACK, "Knockback", 2, EnchantmentCategory.MELEE_WEAPONS, new Vector(12, 3, 14), BlockFace.NORTH),
    LOOTING(Enchantment.LOOTING, "Looting", 3, EnchantmentCategory.MELEE_WEAPONS, new Vector(11, 3, 14), BlockFace.NORTH),
    SHARPNESS(Enchantment.SHARPNESS, "Sharpness", 5, EnchantmentCategory.MELEE_WEAPONS, new Vector(10, 3, 14), BlockFace.NORTH),
    SMITE(Enchantment.SMITE, "Smite", 5, EnchantmentCategory.MELEE_WEAPONS, new Vector(9, 3, 14), BlockFace.NORTH),
    SWEEPING_EDGE(Enchantment.SWEEPING_EDGE, "Sweeping Edge", 3, EnchantmentCategory.MELEE_WEAPONS, new Vector(7, 3, 14), BlockFace.NORTH),
    // ranged weapons
    CHANNELING(Enchantment.CHANNELING, "Channeling", 1, EnchantmentCategory.RANGED_WEAPONS, new Vector(6, 3, 14), BlockFace.NORTH),
    FLAME(Enchantment.FLAME, "Flame", 1, EnchantmentCategory.RANGED_WEAPONS, new Vector(5, 3, 14), BlockFace.NORTH),
    INFINITY(Enchantment.INFINITY, "Infinity", 1, EnchantmentCategory.RANGED_WEAPONS, new Vector(4, 3, 14), BlockFace.NORTH),
    LOYALTY(Enchantment.LOYALTY, "Loyalty", 3, EnchantmentCategory.RANGED_WEAPONS, new Vector(3, 3, 14), BlockFace.NORTH),
    // right
    MULTISHOT(Enchantment.MULTISHOT, "Multishot", 1, EnchantmentCategory.RANGED_WEAPONS, new Vector(2, 3, 13), BlockFace.EAST),
    PIERCING(Enchantment.PIERCING, "Piercing", 4, EnchantmentCategory.RANGED_WEAPONS, new Vector(2, 3, 12), BlockFace.EAST),
    POWER(Enchantment.POWER, "Power", 5, EnchantmentCategory.RANGED_WEAPONS, new Vector(2, 3, 11), BlockFace.EAST),
    PUNCH(Enchantment.PUNCH, "Punch", 2, EnchantmentCategory.RANGED_WEAPONS, new Vector(2, 3, 10), BlockFace.EAST),
    QUICK_CHARGE(Enchantment.QUICK_CHARGE, "Quick Charge", 3, EnchantmentCategory.RANGED_WEAPONS, new Vector(2, 3, 9), BlockFace.EAST),
    // tools
    FORTUNE(Enchantment.FORTUNE, "Fortune", 3, EnchantmentCategory.TOOLS, new Vector(2, 3, 7), BlockFace.EAST),
    LUCK_OF_THE_SEA(Enchantment.LUCK_OF_THE_SEA, "Luck of the Sea", 3, EnchantmentCategory.TOOLS, new Vector(2, 3, 6), BlockFace.EAST),
    LURE(Enchantment.LURE, "Lure", 3, EnchantmentCategory.TOOLS, new Vector(2, 3, 5), BlockFace.EAST),
    SILK_TOUCH(Enchantment.SILK_TOUCH, "Silk Touch", 1, EnchantmentCategory.TOOLS, new Vector(2, 3, 4), BlockFace.EAST),
    LEFTOVER(null, "Leftovers", 5, EnchantmentCategory.BOOKS, new Vector(2, 3, 3), BlockFace.EAST),
    BOOKS(null, "Books", 4, EnchantmentCategory.BOOKS, new Vector(8, 3, 5), BlockFace.NORTH),
    WRITTEN_BOOKS(null, "Written books", 4, EnchantmentCategory.BOOKS, new Vector(11, 3, 8), BlockFace.EAST),
    BOOK_AND_QUILLS(null, "Book and Quills", 4, EnchantmentCategory.BOOKS, new Vector(8, 3, 11), BlockFace.SOUTH),
    KNOWLEDGE_BOOKS(null, "Knowledge Books", 4, EnchantmentCategory.BOOKS, new Vector(5, 3, 8), BlockFace.WEST);

    public static final HashMap<Enchantment, EnchantmentShelf> BY_ENCHANTMENT = new HashMap<>();

    static {
        for (EnchantmentShelf e : values()) {
            if (e.getEnchantment() != null) {
                BY_ENCHANTMENT.put(e.getEnchantment(), e);
            }
        }
    }

    private final Enchantment enchantment;
    private final String minecraftName;
    private final int maxLevel;
    private final EnchantmentCategory category;
    private final Vector position;
    private final BlockFace facing;

    EnchantmentShelf(Enchantment enchantment, String minecraftName, int maxLevel, EnchantmentCategory category, Vector position, BlockFace facing) {
        this.enchantment = enchantment;
        this.minecraftName = minecraftName;
        this.maxLevel = maxLevel;
        this.category = category;
        this.position = position;
        this.facing = facing;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public EnchantmentCategory getCategory() {
        return category;
    }

    public Vector getPosition() {
        return position;
    }

    public BlockFace getFacing() {
        return facing;
    }
}
