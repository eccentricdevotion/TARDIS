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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum Monster {

    CYBERMAN("Cyberman", EntityType.ZOMBIE, "cyberman", 2, 4, Material.IRON_INGOT),
    DALEK("Dalek", EntityType.SKELETON, "dalek", 3, 10000005, Material.SLIME_BALL, 10000004),
    DALEK_SEC("Dalek Sec", EntityType.ZOMBIFIED_PIGLIN, "dalek_sec", 22, 4, Material.MANGROVE_PROPAGULE),
    DAVROS("Davros", EntityType.ZOMBIFIED_PIGLIN, "davros", 23, 4, Material.CRIMSON_BUTTON),
    EMPTY_CHILD("Empty Child", EntityType.ZOMBIE, "empty", 4, 4, Material.SUGAR),
    HATH("Hath", EntityType.ZOMBIFIED_PIGLIN, "hath", 16, 5, Material.PUFFERFISH, 4),
    HEADLESS_MONK("Headless Monk", EntityType.SKELETON, "monk", 17, 4, Material.RED_CANDLE),
    ICE_WARRIOR("Ice Warrior", EntityType.ZOMBIFIED_PIGLIN, "ice", 5, 5, Material.SNOWBALL, 4),
    JUDOON("Judoon", EntityType.ARMOR_STAND, "judoon", 14, 10, Material.YELLOW_DYE, 11),
    K9("K9", EntityType.ARMOR_STAND, "k9", 15, 1, Material.BONE),
    MIRE("Mire", EntityType.SKELETON, "mire", 18, 4, Material.NETHERITE_SCRAP),
    OOD("Ood", EntityType.ARMOR_STAND, "ood", 12, 29, Material.ROTTEN_FLESH, 30),
    RACNOSS("Racnoss", EntityType.PIGLIN_BRUTE, "racnoss", 21, 5, Material.SPIDER_EYE, 4),
    SEA_DEVIL("Sea Devil", EntityType.DROWNED, "devil", 19, 4, Material.KELP),
    SILENT("Silent", EntityType.SKELETON, "silent", 6, 11, Material.END_STONE, 5),
    SILURIAN("Silurian", EntityType.SKELETON, "silurian", 7, 4, Material.FEATHER),
    SLITHEEN("Slitheen", EntityType.ZOMBIE, "slitheen", 20, 4, Material.TURTLE_EGG),
    SONTARAN("Sontaran", EntityType.ZOMBIE, "sontaran", 8, 5, Material.POTATO, 4),
    STRAX("Strax", EntityType.ZOMBIFIED_PIGLIN, "strax", 9, 4, Material.BAKED_POTATO),
    TOCLAFANE("Toclafane", EntityType.ZOMBIE, "toclafane", 13, 2, Material.GUNPOWDER),
    VASHTA_NERADA("Vashta Nerada", EntityType.ZOMBIE, "vashta", 10, 5, Material.BOOK, 4),
    WEEPING_ANGEL("Weeping Angel", EntityType.SKELETON, "angel", 1, 5, Material.BRICK),
    ZYGON("Zygon", EntityType.ZOMBIE, "zygon", 11, 4, Material.PAINTING);

    private final String name;
    private final EntityType entityType;
    private final String permission;
    private final int persist;
    private final int customModelData;
    private final Material material;
    private final int headModelData;

    Monster(String name, EntityType entityType, String permission, int persist, int customModelData, Material material) {
        this.name = name;
        this.entityType = entityType;
        this.permission = permission;
        this.persist = persist;
        this.customModelData = customModelData;
        this.material = material;
        this.headModelData = 3;
    }

    Monster(String name, EntityType entityType, String permission, int persist, int customModelData, Material material, int headModelData) {
        this.name = name;
        this.entityType = entityType;
        this.permission = permission;
        this.persist = persist;
        this.customModelData = customModelData;
        this.material = material;
        this.headModelData = headModelData;
    }

    public String getName() {
        return name;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getPermission() {
        return permission;
    }

    public int getPersist() {
        return persist;
    }

    public int getCustomModelData() {
        return (this == DALEK) ? customModelData + TARDISConstants.RANDOM.nextInt(16) : customModelData;
    }

    public Material getMaterial() {
        return material;
    }

    public int getHeadModelData() {
        return headModelData;
    }

    public boolean isCustom() {
        switch (this) {
            case DAVROS, OOD, K9, JUDOON, DALEK, TOCLAFANE -> {
                return false;
            }
            default -> {
                return true;
            }
        }
    }
}
