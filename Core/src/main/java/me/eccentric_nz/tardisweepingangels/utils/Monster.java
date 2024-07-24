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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.skins.CharacterSkins;
import me.eccentric_nz.TARDIS.skins.MonsterSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum Monster {

    CYBERMAN("Cyberman", EntityType.ZOMBIE, "cyberman", 2, 4, Material.IRON_INGOT, MonsterSkins.CYBERMAN),
    DALEK("Dalek", EntityType.SKELETON, "dalek", 3, 10000005, Material.SLIME_BALL, 10000004, null),
    DALEK_SEC("Dalek Sec", EntityType.ZOMBIFIED_PIGLIN, "dalek_sec", 22, 4, Material.MANGROVE_PROPAGULE, MonsterSkins.DALEK_SEC),
    DAVROS("Davros", EntityType.ZOMBIFIED_PIGLIN, "davros", 23, 4, Material.CRIMSON_BUTTON, null),
    EMPTY_CHILD("Empty Child", EntityType.ZOMBIE, "empty", 4, 4, Material.SUGAR, MonsterSkins.EMPTY_CHILD),
    HATH("Hath", EntityType.ZOMBIFIED_PIGLIN, "hath", 16, 5, Material.PUFFERFISH, 4, null),
    HEADLESS_MONK("Headless Monk", EntityType.SKELETON, "monk", 17, 4, Material.RED_CANDLE, MonsterSkins.HEADLESS_MONK),
    ICE_WARRIOR("Ice Warrior", EntityType.ZOMBIFIED_PIGLIN, "ice", 5, 5, Material.SNOWBALL, 4, MonsterSkins.ICE_WARRIOR),
    JUDOON("Judoon", EntityType.HUSK, "judoon", 14, 10, Material.YELLOW_DYE, 11, CharacterSkins.JUDOON),
    K9("K9", EntityType.HUSK, "k9", 15, 1, Material.BONE, null),
    MIRE("Mire", EntityType.SKELETON, "mire", 18, 4, Material.NETHERITE_SCRAP, null),
    OOD("Ood", EntityType.HUSK, "ood", 12, 29, Material.ROTTEN_FLESH, 30, CharacterSkins.OOD),
    OSSIFIED("Ossified Time Zombie", EntityType.ZOMBIE, "ossified", 24, 4, Material.CHARCOAL, null),
    RACNOSS("Racnoss", EntityType.PIGLIN_BRUTE, "racnoss", 21, 5, Material.SPIDER_EYE, 4, MonsterSkins.RACNOSS),
    SEA_DEVIL("Sea Devil", EntityType.DROWNED, "devil", 19, 4, Material.KELP, MonsterSkins.SEA_DEVIL),
    SILENT("Silent", EntityType.SKELETON, "silent", 6, 11, Material.END_STONE, 5, MonsterSkins.SILENCE),
    SILURIAN("Silurian", EntityType.SKELETON, "silurian", 7, 4, Material.FEATHER, MonsterSkins.SILURIAN),
    SLITHEEN("Slitheen", EntityType.ZOMBIE, "slitheen", 20, 4, Material.TURTLE_EGG, MonsterSkins.SLITHEEN),
    SONTARAN("Sontaran", EntityType.ZOMBIE, "sontaran", 8, 5, Material.POTATO, 4, MonsterSkins.SONTARAN),
    STRAX("Strax", EntityType.ZOMBIFIED_PIGLIN, "strax", 9, 4, Material.BAKED_POTATO, null),
    TOCLAFANE("Toclafane", EntityType.ZOMBIE, "toclafane", 13, 2, Material.GUNPOWDER, null),
    VASHTA_NERADA("Vashta Nerada", EntityType.ZOMBIE, "vashta", 10, 5, Material.BOOK, 4, MonsterSkins.VASHTA_NERADA),
    WEEPING_ANGEL("Weeping Angel", EntityType.SKELETON, "angel", 1, 5, Material.BRICK, MonsterSkins.WEEPING_ANGEL),
    ZYGON("Zygon", EntityType.ZOMBIE, "zygon", 11, 4, Material.PAINTING, MonsterSkins.ZYGON),
    FLYER("Flyer", EntityType.CHICKEN, "flyer", 99, 99, Material.CHICKEN_SPAWN_EGG, null);

    private final String name;
    private final EntityType entityType;
    private final String permission;
    private final int persist;
    private final int customModelData;
    private final Material material;
    private final int headModelData;
    private final Skin skin;

    Monster(String name, EntityType entityType, String permission, int persist, int customModelData, Material material, Skin skin) {
        this.name = name;
        this.entityType = entityType;
        this.permission = permission;
        this.persist = persist;
        this.customModelData = customModelData;
        this.material = material;
        this.headModelData = 3;
        this.skin = skin;
    }

    Monster(String name, EntityType entityType, String permission, int persist, int customModelData, Material material, int headModelData, Skin skin) {
        this.name = name;
        this.entityType = entityType;
        this.permission = permission;
        this.persist = persist;
        this.customModelData = customModelData;
        this.material = material;
        this.headModelData = headModelData;
        this.skin = skin;
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

    public boolean isAnimated() {
        switch (this) {
            case DALEK, DAVROS, TOCLAFANE, FLYER -> {
                return false;
            }
            default -> {
                return true;
            }
        }
    }

    public boolean isFollower() {
        switch (this) {
            case K9, JUDOON, OOD -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public Skin getSkin() {
        return skin;
    }
}
