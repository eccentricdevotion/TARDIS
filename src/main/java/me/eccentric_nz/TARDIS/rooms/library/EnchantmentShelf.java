package me.eccentric_nz.TARDIS.rooms.library;

import org.bukkit.enchantments.Enchantment;

public enum EnchantmentShelf {

    AQUA_AFFINITY(Enchantment.WATER_WORKER, "Aqua Affinity", 1, EnchantmentCategory.ARMOUR),
    BANE_OF_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods", 5, EnchantmentCategory.MELEE_WEAPONS),
    BLAST_PROTECTION(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection", 4, EnchantmentCategory.ARMOUR),
    CHANNELING(Enchantment.CHANNELING, "Channeling", 1, EnchantmentCategory.RANGED_WEAPONS),
    CURSE_OF_BINDING(Enchantment.BINDING_CURSE, "Curse of Binding", 1, EnchantmentCategory.ARMOUR),
    CURSE_OF_VANISHING(Enchantment.VANISHING_CURSE, "Curse of Vanishing", 1, EnchantmentCategory.ALL_PURPOSE),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER, "Depth Strider", 3, EnchantmentCategory.ARMOUR),
    EFFICIENCY(Enchantment.DIG_SPEED, "Efficiency", 5, EnchantmentCategory.MELEE_WEAPONS),
    FEATHER_FALLING(Enchantment.PROTECTION_FALL, "Feather Falling", 4, EnchantmentCategory.ARMOUR),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT, "Fire Aspect", 2, EnchantmentCategory.MELEE_WEAPONS),
    FIRE_PROTECTION(Enchantment.PROTECTION_FIRE, "Fire Protection", 4, EnchantmentCategory.ARMOUR),
    FLAME(Enchantment.ARROW_FIRE, "Flame", 1, EnchantmentCategory.RANGED_WEAPONS),
    FORTUNE(Enchantment.LOOT_BONUS_BLOCKS, "Fortune", 3, EnchantmentCategory.TOOLS),
    FROST_WALKER(Enchantment.FROST_WALKER, "Frost Walker", 2, EnchantmentCategory.ARMOUR),
    IMPALING(Enchantment.IMPALING, "Impaling", 5, EnchantmentCategory.MELEE_WEAPONS),
    INFINITY(Enchantment.ARROW_INFINITE, "Infinity", 1, EnchantmentCategory.RANGED_WEAPONS),
    KNOCKBACK(Enchantment.KNOCKBACK, "Knockback", 2, EnchantmentCategory.MELEE_WEAPONS),
    LOOTING(Enchantment.LOOT_BONUS_MOBS, "Looting", 3, EnchantmentCategory.MELEE_WEAPONS),
    LOYALTY(Enchantment.LOYALTY, "Loyalty", 3, EnchantmentCategory.RANGED_WEAPONS),
    LUCK_OF_THE_SEA(Enchantment.LUCK, "Luck of the Sea", 3, EnchantmentCategory.TOOLS),
    LURE(Enchantment.LURE, "Lure", 3, EnchantmentCategory.TOOLS),
    MENDING(Enchantment.MENDING, "Mending", 1, EnchantmentCategory.ALL_PURPOSE),
    MULTISHOT(Enchantment.MULTISHOT, "Multishot", 1, EnchantmentCategory.RANGED_WEAPONS),
    PIERCING(Enchantment.PIERCING, "Piercing", 4, EnchantmentCategory.RANGED_WEAPONS),
    POWER(Enchantment.ARROW_DAMAGE, "Power", 5, EnchantmentCategory.RANGED_WEAPONS),
    PROJECTILE_PROTECTION(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection", 4, EnchantmentCategory.ARMOUR),
    PROTECTION(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection", 4, EnchantmentCategory.ARMOUR),
    PUNCH(Enchantment.ARROW_KNOCKBACK, "Punch", 2, EnchantmentCategory.RANGED_WEAPONS),
    QUICK_CHARGE(Enchantment.QUICK_CHARGE, "Quick Charge", 3, EnchantmentCategory.RANGED_WEAPONS),
    RESPIRATION(Enchantment.OXYGEN, "Respiration", 3, EnchantmentCategory.ARMOUR),
    RIPTIDE(Enchantment.RIPTIDE, "Riptide", 3, EnchantmentCategory.ARMOUR),
    SHARPNESS(Enchantment.DAMAGE_ALL, "Sharpness", 5, EnchantmentCategory.MELEE_WEAPONS),
    SILK_TOUCH(Enchantment.SILK_TOUCH, "Silk Touch", 1, EnchantmentCategory.TOOLS),
    SMITE(Enchantment.DAMAGE_UNDEAD, "Smite", 5, EnchantmentCategory.MELEE_WEAPONS),
    SOUL_SPEED(Enchantment.SOUL_SPEED, "Soul Speed", 3, EnchantmentCategory.ARMOUR),
    SWEEPING_EDGE(Enchantment.SWEEPING_EDGE, "Sweeping Edge", 3, EnchantmentCategory.MELEE_WEAPONS),
    SWIFT_SNEAK(Enchantment.SWIFT_SNEAK, "Swift Sneak", 3, EnchantmentCategory.ARMOUR),
    THORNS(Enchantment.THORNS, "Thorns", 3, EnchantmentCategory.ARMOUR),
    UNBREAKING(Enchantment.DURABILITY, "Unbreaking", 3, EnchantmentCategory.ALL_PURPOSE);

    private final Enchantment enchantment;
    private final String minecraftName;
    private final int maxLevel;
    private final EnchantmentCategory category;

    EnchantmentShelf(Enchantment enchantment, String minecraftName, int maxLevel, EnchantmentCategory category) {
        this.enchantment = enchantment;
        this.minecraftName = minecraftName;
        this.maxLevel = maxLevel;
        this.category = category;
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
}
