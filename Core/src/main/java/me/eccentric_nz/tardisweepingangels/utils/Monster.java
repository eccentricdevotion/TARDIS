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

    CLOCKWORK_DROID("Clockwork Droid", EntityType.ZOMBIE, "clockwork", 27, DroidVariant.CLOCKWORK_DROID_STATIC.getKey(), Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, DroidVariant.CLOCKWORK_DROID_HEAD.getKey(), MonsterSkins.CLOCKWORK_DROID_MALE),
    CYBERMAN("Cyberman", EntityType.ZOMBIE, "cyberman", 2, CybermanVariant.CYBERMAN_STATIC.getKey(), Material.IRON_INGOT, CybermanVariant.CYBERMAN_HEAD.getKey(), MonsterSkins.CYBERMAN),
    DALEK("Dalek", EntityType.SKELETON, "dalek", 3, DalekVariant.DALEK_BRASS.getKey(), Material.SLIME_BALL, DalekVariant.DALEK_HEAD.getKey(), null),
    DALEK_SEC("Dalek Sec", EntityType.ZOMBIFIED_PIGLIN, "dalek_sec", 22, DalekSecVariant.DALEK_SEC_STATIC.getKey(), Material.MANGROVE_PROPAGULE, DalekSecVariant.DALEK_SEC_HEAD.getKey(), CharacterSkins.DALEK_SEC),
    DAVROS("Davros", EntityType.ZOMBIFIED_PIGLIN, "davros", 23, DavrosVariant.DAVROS.getKey(), Material.CRIMSON_BUTTON, DavrosVariant.DAVROS_HEAD.getKey(), null),
    EMPTY_CHILD("Empty Child", EntityType.ZOMBIE, "empty", 4, EmptyChildVariant.EMPTY_CHILD_STATIC.getKey(), Material.SUGAR, EmptyChildVariant.EMPTY_CHILD_HEAD.getKey(), MonsterSkins.EMPTY_CHILD),
    HATH("Hath", EntityType.ZOMBIFIED_PIGLIN, "hath", 16, HathVariant.HATH_STATIC.getKey(), Material.PUFFERFISH, HathVariant.HATH_HEAD.getKey(), null),
    HEADLESS_MONK("Headless Monk", EntityType.SKELETON, "monk", 17, MonkVariant.HEADLESS_MONK_STATIC.getKey(), Material.RED_CANDLE, MonkVariant.HEADLESS_MONK_HEAD.getKey(), MonsterSkins.HEADLESS_MONK),
    ICE_WARRIOR("Ice Warrior", EntityType.ZOMBIFIED_PIGLIN, "ice", 5, IceWarriorVariant.ICE_WARRIOR_STATIC.getKey(), Material.SNOWBALL, IceWarriorVariant.ICE_WARRIOR_HEAD.getKey(), MonsterSkins.ICE_WARRIOR),
    JUDOON("Judoon", EntityType.HUSK, "judoon", 14, JudoonVariant.JUDOON_STATIC.getKey(), Material.YELLOW_DYE, JudoonVariant.JUDOON_MONSTER_HEAD.getKey(), CharacterSkins.JUDOON),
    K9("K9", EntityType.HUSK, "k9", 15, K9Variant.K9.getKey(), Material.BONE, null, null),
    MIRE("Mire", EntityType.SKELETON, "mire", 18, MireVariant.THE_MIRE_STATIC.getKey(), Material.NETHERITE_SCRAP, MireVariant.THE_MIRE_HEAD.getKey(), null),
    OOD("Ood", EntityType.HUSK, "ood", 12, OodVariant.OOD_BLACK_STATIC.getKey(), Material.ROTTEN_FLESH, OodVariant.OOD_MONSTER_HEAD.getKey(), CharacterSkins.OOD),
    OSSIFIED("Ossified Time Zombie", EntityType.ZOMBIE, "ossified", 24, OssifiedVariant.OSSIFIED_STATIC.getKey(), Material.CHARCOAL, OssifiedVariant.OSSIFIED_HEAD.getKey(), null),
    RACNOSS("Racnoss", EntityType.PIGLIN_BRUTE, "racnoss", 21, RacnossVariant.RACNOSS_STATIC.getKey(), Material.SPIDER_EYE, RacnossVariant.RACNOSS_HEAD.getKey(), MonsterSkins.RACNOSS),
    SCARECROW("Scarecrow", EntityType.ZOMBIE, "scarecrow", 26, ScarecrowVariant.SCARECROW_STATIC.getKey(), Material.WHEAT, ScarecrowVariant.SCARECROW_HEAD.getKey(), MonsterSkins.SCARECROW),
    SEA_DEVIL("Sea Devil", EntityType.DROWNED, "devil", 19, SeaDevilVariant.SEA_DEVIL_STATIC.getKey(), Material.KELP, SeaDevilVariant.SEA_DEVIL_HEAD.getKey(), MonsterSkins.SEA_DEVIL),
    SILENT("Silent", EntityType.SKELETON, "silent", 6, SilentVariant.SILENT_BEAMING.getKey(), Material.END_STONE, SilentVariant.SILENT_BEAMING.getKey(), MonsterSkins.SILENCE),
    SILURIAN("Silurian", EntityType.SKELETON, "silurian", 7, SilurianVariant.SILURIAN_STATIC.getKey(), Material.FEATHER, SilurianVariant.SILURIAN_HEAD.getKey(), MonsterSkins.SILURIAN),
    SLITHEEN("Slitheen", EntityType.ZOMBIE, "slitheen", 20, SlitheenVariant.SLITHEEN_STATIC.getKey(), Material.TURTLE_EGG, SlitheenVariant.SLITHEEN_HEAD.getKey(), MonsterSkins.SLITHEEN),
    SONTARAN("Sontaran", EntityType.ZOMBIE, "sontaran", 8, SontaranVariant.SONTARAN_STATIC.getKey(), Material.POTATO, SontaranVariant.SONTARAN_HEAD.getKey(), MonsterSkins.SONTARAN),
    STRAX("Strax", EntityType.ZOMBIFIED_PIGLIN, "strax", 9, StraxVariant.STRAX_STATIC.getKey(), Material.BAKED_POTATO, StraxVariant.STRAX_HEAD.getKey(), null),
    SYCORAX("Sycorax", EntityType.ZOMBIE, "sycorax", 25, SycoraxVariant.SYCORAX_STATIC.getKey(), Material.BONE_MEAL, SycoraxVariant.SYCORAX_HEAD.getKey(), MonsterSkins.SYCORAX),
    TOCLAFANE("Toclafane", EntityType.ZOMBIE, "toclafane", 13, ToclafaneVariant.TOCLAFANE.getKey(), Material.GUNPOWDER, null, null),
    VASHTA_NERADA("Vashta Nerada", EntityType.ZOMBIE, "vashta", 10, VashtaNeradaVariant.VASHTA_NERADA_STATIC.getKey(), Material.BOOK, VashtaNeradaVariant.VASHTA_NERADA_HEAD.getKey(), MonsterSkins.VASHTA_NERADA),
    WEEPING_ANGEL("Weeping Angel", EntityType.SKELETON, "angel", 1, WeepingAngelVariant.WEEPING_ANGEL_STATIC.getKey(), Material.BRICK, WeepingAngelVariant.WEEPING_ANGEL_HEAD.getKey(), MonsterSkins.WEEPING_ANGEL),
    ZYGON("Zygon", EntityType.ZOMBIE, "zygon", 11, ZygonVariant.ZYGON_STATIC.getKey(), Material.PAINTING, ZygonVariant.ZYGON_HEAD.getKey(), MonsterSkins.ZYGON),
//     ANGEL_OF_LIBERTY("Angel Of Liberty", EntityType.ZOMBIE, "liberty", 28, AngelOfLibertyVariant.ANGEL_OF_LIBERTY_STATIC.getKey(), Material.OXIDIZED_COPPER_GRATE, AngelOfLibertyVariant.ANGEL_OF_LIBERTY_HEAD.getKey(), MonsterSkins.ANGEL_OF_LIBERTY),
//     CYBERSHADE("Cybershade", EntityType.ZOMBIE, "cybershade", 29, CybershadeVariant.CYBERSHADE_STATIC.getKey(), Material.BROWN_WOOL, CybershadeVariant.CYBERSHADE_HEAD.getKey(), MonsterSkins.CYBERSHADE),
//     OMEGA("Omega", EntityType.SKELETON, "omega", 30, OmegaVariant.OMEGA_STATIC.getKey(), Material.GOLD_INGOT, OmegaVariant.OMEGA_HEAD.getKey(), MonsterSkins.OMEGA),
//     SMILER("Smiler", EntityType.ZOMBIE, "smiler", 31, SmilerVariant.SMILER_STATIC.getKey(), Material.RED_STAINED_GLASS_PANE, SmilerVariant.SMILER_HEAD.getKey(), MonsterSkins.SMILER),
//     SUTEKH("Sutekh", EntityType.STRAY, "sutekh", 32, SutekhVariant.SUTEKH_STATIC.getKey(), Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, SutekhVariant.SUTEKH_HEAD.getKey(), MonsterSkins.SUTEKH),
//     THE_BEAST("The Beast", EntityType.ZOMBIE, "beast", 33, TheBeastVariant.THE_BEAST_STATIC.getKey(), Material.FIRE_CHARGE, TheBeastVariant.THE_BEAST_HEAD.getKey(), MonsterSkins.THE_BEAST),
//     VAMPIRE_OF_VENICE("Vampire Of Venice", EntityType.DROWNED, "vampire", 34, VampireOfVeniceVariant.VAMPIRE_STATIC.getKey(), Material.COD, VampireOfVeniceVariant.VAMPIRE_HEAD.getKey(), MonsterSkins.VAMPIRE_OF_VENICE)
    ;

    private final String name;
    private final EntityType entityType;
    private final String permission;
    private final int persist;
    private final NamespacedKey model;
    private final Material material;
    private final NamespacedKey headModel;
    private final Skin skin;

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
            case CYBERMAN, EMPTY_CHILD, HATH, ICE_WARRIOR, SILENT, SILURIAN, SONTARAN, STRAX, VASHTA_NERADA,
                 WEEPING_ANGEL, ZYGON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public NamespacedKey getArmourKey() {
        NamespacedKey key = null;
        switch (this) {
            case CLOCKWORK_DROID -> key = ArmourVariant.CLOCKWORK_DROID.getKey();
            case CYBERMAN -> key = ArmourVariant.CYBERMAN.getKey();
            case DALEK_SEC -> key = ArmourVariant.DALEK_SEC.getKey();
            case EMPTY_CHILD -> key = ArmourVariant.EMPTY_CHILD.getKey();
            case HATH -> key = ArmourVariant.HATH.getKey();
            case HEADLESS_MONK -> key = ArmourVariant.HEADLESS_MONK.getKey();
            case ICE_WARRIOR -> key = ArmourVariant.ICE_WARRIOR.getKey();
            case JUDOON -> key = ArmourVariant.JUDOON.getKey();
            case MIRE -> key = ArmourVariant.MIRE.getKey();
            case OOD -> {
                int r = TARDISConstants.RANDOM.nextInt(100);
                if (r < 15) {
                    key = ArmourVariant.OOD_BLUE.getKey();
                } else if (r > 84) {
                    key = ArmourVariant.OOD_BROWN.getKey();
                } else {
                    key = ArmourVariant.OOD_BLACK.getKey();
                }
            }
            case OSSIFIED -> key = ArmourVariant.OSSIFIED.getKey();
            case RACNOSS -> key = ArmourVariant.RACNOSS.getKey();
            case SCARECROW -> key = ArmourVariant.SCARECROW.getKey();
            case SEA_DEVIL -> key = ArmourVariant.SEA_DEVIL.getKey();
            case SILENT -> key = ArmourVariant.SILENCE.getKey();
            case SILURIAN -> key = ArmourVariant.SILURIAN.getKey();
            case SLITHEEN -> key = ArmourVariant.SLITHEEN.getKey();
            case SONTARAN -> key = ArmourVariant.SONTARAN.getKey();
            case STRAX -> key = ArmourVariant.STRAX.getKey();
            case SYCORAX -> key = ArmourVariant.SYCORAX.getKey();
            case VASHTA_NERADA -> key = ArmourVariant.VASHTA_NERADA.getKey();
            case WEEPING_ANGEL -> key = ArmourVariant.WEEPING_ANGEL.getKey();
            case ZYGON -> key = ArmourVariant.ZYGON.getKey();
            default -> { }
        }
        return key;
    }
}
