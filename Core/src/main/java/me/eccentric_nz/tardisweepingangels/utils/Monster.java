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
import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.skins.CharacterSkins;
import me.eccentric_nz.TARDIS.skins.MonsterSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;

public enum Monster {

    CLOCKWORK_DROID("Clockwork Droid", EntityType.ZOMBIE, "clockwork", 27, DroidVariant.CLOCKWORK_DROID_DISGUISE.getKey(), Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, DroidVariant.CLOCKWORK_DROID_HEAD.getKey(), MonsterSkins.CLOCKWORK_DROID_MALE),
    CYBERMAN("Cyberman", EntityType.ZOMBIE, "cyberman", 2, CybermanVariant.CYBERMAN_DISGUISE.getKey(), Material.IRON_INGOT, CybermanVariant.CYBERMAN_HEAD.getKey(), MonsterSkins.CYBERMAN),
    DALEK("Dalek", EntityType.SKELETON, "dalek", 3, DalekVariant.DALEK_BRASS.getKey(), Material.SLIME_BALL, DalekVariant.DALEK_HEAD.getKey(), null),
    DALEK_SEC("Dalek Sec", EntityType.ZOMBIFIED_PIGLIN, "dalek_sec", 22, DalekSecVariant.DALEK_SEC_DISGUISE.getKey(), Material.MANGROVE_PROPAGULE, DalekSecVariant.DALEK_SEC_HEAD.getKey(), CharacterSkins.DALEK_SEC),
    DAVROS("Davros", EntityType.ZOMBIFIED_PIGLIN, "davros", 23, DavrosVariant.DAVROS_DISGUISE.getKey(), Material.CRIMSON_BUTTON, DavrosVariant.DAVROS_HEAD.getKey(),null),
    EMPTY_CHILD("Empty Child", EntityType.ZOMBIE, "empty", 4, EmptyChildVariant.EMPTY_CHILD_DISGUISE.getKey(), Material.SUGAR, EmptyChildVariant.EMPTY_CHILD_HEAD.getKey(), MonsterSkins.EMPTY_CHILD),
    HATH("Hath", EntityType.ZOMBIFIED_PIGLIN, "hath", 16, HathVariant.HATH_DISGUISE.getKey(), Material.PUFFERFISH, HathVariant.HATH_HEAD.getKey(), null),
    HEADLESS_MONK("Headless Monk", EntityType.SKELETON, "monk", 17, MonkVariant.HEADLESS_MONK_DISGUISE.getKey(), Material.RED_CANDLE, MonkVariant.HEADLESS_MONK_HEAD.getKey(), MonsterSkins.HEADLESS_MONK),
    ICE_WARRIOR("Ice Warrior", EntityType.ZOMBIFIED_PIGLIN, "ice", 5, IceWarriorVariant.ICE_WARRIOR_DISGUISE.getKey(), Material.SNOWBALL, IceWarriorVariant.ICE_WARRIOR_HEAD.getKey(), MonsterSkins.ICE_WARRIOR),
    JUDOON("Judoon", EntityType.HUSK, "judoon", 14, JudoonVariant.JUDOON_DISGUISE.getKey(), Material.YELLOW_DYE, JudoonVariant.JUDOON_MONSTER_HEAD.getKey(), CharacterSkins.JUDOON),
    K9("K9", EntityType.HUSK, "k9", 15, K9Variant.K9.getKey(), Material.BONE, null,null),
    MIRE("Mire", EntityType.SKELETON, "mire", 18, MireVariant.THE_MIRE_DISGUISE.getKey(), Material.NETHERITE_SCRAP, MireVariant.THE_MIRE_HEAD.getKey(),null),
    OOD("Ood", EntityType.HUSK, "ood", 12, OodVariant.OOD_DISGUISE.getKey(), Material.ROTTEN_FLESH, OodVariant.OOD_MONSTER_HEAD.getKey(), CharacterSkins.OOD),
    OSSIFIED("Ossified Time Zombie", EntityType.ZOMBIE, "ossified", 24, OssifiedVariant.OSSIFIED_DISGUISE.getKey(), Material.CHARCOAL, OssifiedVariant.OSSIFIED_HEAD.getKey(), null),
    RACNOSS("Racnoss", EntityType.PIGLIN_BRUTE, "racnoss", 21, RacnossVariant.RACNOSS_DISGUISE.getKey(), Material.SPIDER_EYE, RacnossVariant.RACNOSS_HEAD.getKey(), MonsterSkins.RACNOSS),
    SCARECROW("Scarecrow", EntityType.ZOMBIE, "scarecrow", 26, ScarecrowVariant.SCARECROW_DISGUISE.getKey(), Material.WHEAT, ScarecrowVariant.SCARECROW_HEAD.getKey(), MonsterSkins.SCARECROW),
    SEA_DEVIL("Sea Devil", EntityType.DROWNED, "devil", 19, SeaDevilVariant.SEA_DEVIL_DISGUISE.getKey(), Material.KELP, SeaDevilVariant.SEA_DEVIL_HEAD.getKey(), MonsterSkins.SEA_DEVIL),
    SILENT("Silent", EntityType.SKELETON, "silent", 6, SilentVariant.SILENT_BEAMING.getKey(), Material.END_STONE, SilentVariant.SILENT_BEAMING.getKey(), MonsterSkins.SILENCE),
    SILURIAN("Silurian", EntityType.SKELETON, "silurian", 7, SilurianVariant.SILURIAN_DISGUISE.getKey(), Material.FEATHER, SilurianVariant.SILURIAN_HEAD.getKey(), MonsterSkins.SILURIAN),
    SLITHEEN("Slitheen", EntityType.ZOMBIE, "slitheen", 20, SlitheenVariant.SLITHEEN_DISGUISE.getKey(), Material.TURTLE_EGG, SlitheenVariant.SLITHEEN_HEAD.getKey(), MonsterSkins.SLITHEEN),
    SONTARAN("Sontaran", EntityType.ZOMBIE, "sontaran", 8, SontaranVariant.SONTARAN_DISGUISE.getKey(), Material.POTATO, SontaranVariant.SONTARAN_HEAD.getKey(), MonsterSkins.SONTARAN),
    STRAX("Strax", EntityType.ZOMBIFIED_PIGLIN, "strax", 9, StraxVariant.STRAX_DISGUISE.getKey(), Material.BAKED_POTATO, StraxVariant.STRAX_HEAD.getKey(), null),
    SYCORAX("Sycorax", EntityType.ZOMBIE, "sycorax", 25, SycoraxVariant.SYCORAX_DISGUISE.getKey(), Material.BONE_MEAL, SycoraxVariant.SYCORAX_HEAD.getKey(), MonsterSkins.SYCORAX),
    TOCLAFANE("Toclafane", EntityType.ZOMBIE, "toclafane", 13, ToclafaneVariant.TOCLAFANE.getKey(), Material.GUNPOWDER, null, null),
    VASHTA_NERADA("Vashta Nerada", EntityType.ZOMBIE, "vashta", 10, VashtaNeradaVariant.VASHTA_NERADA_DISGUISE.getKey(), Material.BOOK, VashtaNeradaVariant.VASHTA_NERADA_HEAD.getKey(), MonsterSkins.VASHTA_NERADA),
    WEEPING_ANGEL("Weeping Angel", EntityType.SKELETON, "angel", 1, WeepingAngelVariant.WEEPING_ANGEL_DISGUISE.getKey(), Material.BRICK, WeepingAngelVariant.WEEPING_ANGEL_HEAD.getKey(), MonsterSkins.WEEPING_ANGEL),
    ZYGON("Zygon", EntityType.ZOMBIE, "zygon", 11, ZygonVariant.ZYGON_DISGUISE.getKey(), Material.PAINTING, ZygonVariant.ZYGON_HEAD.getKey(), MonsterSkins.ZYGON);

    private final String name;
    private final EntityType entityType;
    private final String permission;
    private final int persist;
    private final NamespacedKey model;
    private final Material material;
    private final NamespacedKey headModel;
    private final Skin skin;

//    Monster(String name, EntityType entityType, String permission, int persist, NamespacedKey model, Material material, Skin skin) {
//        this.name = name;
//        this.entityType = entityType;
//        this.permission = permission;
//        this.persist = persist;
//        this.model = model;
//        this.material = material;
//        this.headModel = 3;
//        this.skin = skin;
//    }

    Monster(String name, EntityType entityType, String permission, int persist, NamespacedKey model, Material material, NamespacedKey headModel, Skin skin) {
        this.name = name;
        this.entityType = entityType;
        this.permission = permission;
        this.persist = persist;
        this.model = model;
        this.material = material;
        this.headModel = headModel;
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

    public NamespacedKey getModel() {
        if (this == DALEK) {
            NamespacedKey key;
            switch (TARDISConstants.RANDOM.nextInt(17)) {
                case 15 -> key = DalekVariant.DALEK_BLACK.getKey();
                case 14 -> key = DalekVariant.DALEK_RED.getKey();
                case 13 -> key = DalekVariant.DALEK_GREEN.getKey();
                case 12 -> key = DalekVariant.DALEK_BROWN.getKey();
                case 11 -> key = DalekVariant.DALEK_BLUE.getKey();
                case 10 -> key = DalekVariant.DALEK_PURPLE.getKey();
                case 9 -> key = DalekVariant.DALEK_CYAN.getKey();
                case 8 -> key = DalekVariant.DALEK_LIGHT_GRAY.getKey();
                case 7 -> key = DalekVariant.DALEK_GRAY.getKey();
                case 6 -> key = DalekVariant.DALEK_PINK.getKey();
                case 5 -> key = DalekVariant.DALEK_LIME.getKey();
                case 4 -> key = DalekVariant.DALEK_YELLOW.getKey();
                case 3 -> key = DalekVariant.DALEK_LIGHT_BLUE.getKey();
                case 2 -> key = DalekVariant.DALEK_MAGENTA.getKey();
                case 1 -> key = DalekVariant.DALEK_ORANGE.getKey();
                case 0 -> key = DalekVariant.DALEK_WHITE.getKey();
                default -> key = DalekVariant.DALEK_BRASS.getKey();
            }
            return key;
        } else {
            return model;
        }
    }

    public Material getMaterial() {
        return material;
    }

    public NamespacedKey getHeadModel() {
        return headModel;
    }

    public boolean isAnimated() {
        switch (this) {
            case DALEK, DAVROS, TOCLAFANE -> {
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

    public boolean hasTrim() {
        switch (this) {
//            case DALEK, DAVROS, FLYER, HEADLESS_MONK, JUDOON, K9, MIRE, OOD, OSSIFIED, RACNOSS, SILENT, SLITHEEN, TOCLAFANE -> {
            case CYBERMAN, EMPTY_CHILD, HATH, ICE_WARRIOR, SILENT, SILURIAN, SONTARAN, STRAX, VASHTA_NERADA, WEEPING_ANGEL, ZYGON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
