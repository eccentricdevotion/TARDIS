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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.skins.CharacterSkins;
import me.eccentric_nz.TARDIS.skins.CyberSkins;
import me.eccentric_nz.TARDIS.skins.MonsterSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public enum Monster {

    ANGEL_OF_LIBERTY("Angel Of Liberty", EntityType.ZOMBIE, "liberty", AngelOfLibertyVariant.ANGEL_OF_LIBERTY_STATIC.getKey(), Material.OXIDIZED_COPPER_GRATE, AngelOfLibertyVariant.ANGEL_OF_LIBERTY_HEAD.getKey(), MonsterSkins.ANGEL_OF_LIBERTY),
    CLOCKWORK_DROID("Clockwork Droid", EntityType.ZOMBIE, "clockwork", DroidVariant.CLOCKWORK_DROID_STATIC.getKey(), Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, DroidVariant.CLOCKWORK_DROID_HEAD.getKey(), MonsterSkins.CLOCKWORK_DROID_MALE),
    CYBERMAN("Cyberman", EntityType.ZOMBIE, "cyberman", CybermanVariant.CYBERMAN_STATIC.getKey(), Material.IRON_INGOT, CybermanVariant.CYBERMAN_HEAD.getKey(), CyberSkins.CYBERMAN),
    CYBERSHADE("Cybershade", EntityType.ZOMBIE, "cybershade", CybermanVariant.CYBERSHADE_STATIC.getKey(), Material.BROWN_WOOL, CybermanVariant.CYBERSHADE_HEAD.getKey(), CyberSkins.CYBERSHADE),
    DALEK("Dalek", EntityType.SKELETON, "dalek", DalekVariant.DALEK_BRASS.getKey(), Material.SLIME_BALL, DalekVariant.DALEK_HEAD.getKey(), null),
    DALEK_SEC("Dalek Sec", EntityType.ZOMBIFIED_PIGLIN, "dalek_sec", DalekSecVariant.DALEK_SEC_STATIC.getKey(), Material.MANGROVE_PROPAGULE, DalekSecVariant.DALEK_SEC_HEAD.getKey(), CharacterSkins.DALEK_SEC),
    DAVROS("Davros", EntityType.ZOMBIFIED_PIGLIN, "davros", DavrosVariant.DAVROS.getKey(), Material.CRIMSON_BUTTON, DavrosVariant.DAVROS_HEAD.getKey(), null),
    EMPTY_CHILD("Empty Child", EntityType.ZOMBIE, "empty", EmptyChildVariant.EMPTY_CHILD_STATIC.getKey(), Material.SUGAR, EmptyChildVariant.EMPTY_CHILD_HEAD.getKey(), MonsterSkins.EMPTY_CHILD),
    HATH("Hath", EntityType.ZOMBIFIED_PIGLIN, "hath", HathVariant.HATH_STATIC.getKey(), Material.PUFFERFISH, HathVariant.HATH_HEAD.getKey(), null),
    HEADLESS_MONK("Headless Monk", EntityType.SKELETON, "monk", MonkVariant.HEADLESS_MONK_STATIC.getKey(), Material.RED_CANDLE, MonkVariant.HEADLESS_MONK_HEAD.getKey(), MonsterSkins.HEADLESS_MONK),
    HEAVENLY_HOST("Heavenly Host", EntityType.ZOMBIE, "heavenly_host", HeavenlyHostVariant.HEAVENLY_HOST_STATIC.getKey(), Material.GOLD_INGOT, HeavenlyHostVariant.HEAVENLY_HOST_HEAD.getKey(), MonsterSkins.HEAVENLY_HOST),
    ICE_WARRIOR("Ice Warrior", EntityType.ZOMBIFIED_PIGLIN, "ice", IceWarriorVariant.ICE_WARRIOR_STATIC.getKey(), Material.SNOWBALL, IceWarriorVariant.ICE_WARRIOR_HEAD.getKey(), MonsterSkins.ICE_WARRIOR),
    JUDOON("Judoon", EntityType.HUSK, "judoon", JudoonVariant.JUDOON_STATIC.getKey(), Material.YELLOW_DYE, JudoonVariant.JUDOON_HEAD.getKey(), CharacterSkins.JUDOON),
    K9("K9", EntityType.HUSK, "k9", K9Variant.K9.getKey(), Material.BONE, null, null),
    MIRE("Mire", EntityType.SKELETON, "mire", MireVariant.THE_MIRE_STATIC.getKey(), Material.NETHERITE_SCRAP, MireVariant.THE_MIRE_HEAD.getKey(), null),
    NIMON("Nimon", EntityType.ZOMBIE, "nimon", NimonVariant.NIMON_STATIC.getKey(), Material.BEEF, NimonVariant.NIMON_HEAD.getKey(), MonsterSkins.NIMON),
    OMEGA("Omega", EntityType.SKELETON, "omega", OmegaVariant.OMEGA_STATIC.getKey(), Material.GOLD_INGOT, OmegaVariant.OMEGA_HEAD.getKey(), MonsterSkins.OMEGA),
    OOD("Ood", EntityType.HUSK, "ood", OodVariant.OOD_BLACK_STATIC.getKey(), Material.ROTTEN_FLESH, OodVariant.OOD_HEAD.getKey(), CharacterSkins.OOD),
    OSSIFIED("Ossified Time Zombie", EntityType.ZOMBIE, "ossified", OssifiedVariant.OSSIFIED_STATIC.getKey(), Material.CHARCOAL, OssifiedVariant.OSSIFIED_HEAD.getKey(), null),
    RACNOSS("Racnoss", EntityType.PIGLIN_BRUTE, "racnoss", RacnossVariant.RACNOSS_STATIC.getKey(), Material.SPIDER_EYE, RacnossVariant.RACNOSS_HEAD.getKey(), MonsterSkins.RACNOSS),
    SATURNYNIAN("Saturnynian", EntityType.DROWNED, "saturnynian", VampireOfVeniceVariant.SATURNYNIAN_STATIC.getKey(), Material.COD, VampireOfVeniceVariant.SATURNYNIAN_MONSTER_HEAD.getKey(), MonsterSkins.SATURNYNIAN),
    SCARECROW("Scarecrow", EntityType.ZOMBIE, "scarecrow", ScarecrowVariant.SCARECROW_STATIC.getKey(), Material.WHEAT, ScarecrowVariant.SCARECROW_HEAD.getKey(), MonsterSkins.SCARECROW),
    SEA_DEVIL("Sea Devil", EntityType.DROWNED, "devil", SeaDevilVariant.SEA_DEVIL_STATIC.getKey(), Material.KELP, SeaDevilVariant.SEA_DEVIL_HEAD.getKey(), MonsterSkins.SEA_DEVIL),
    SILENT("Silent", EntityType.SKELETON, "silent", SilentVariant.SILENT_STATIC.getKey(), Material.END_STONE, SilentVariant.SILENT_HEAD.getKey(), MonsterSkins.SILENCE),
    SILURIAN("Silurian", EntityType.SKELETON, "silurian", SilurianVariant.SILURIAN_STATIC.getKey(), Material.FEATHER, SilurianVariant.SILURIAN_HEAD.getKey(), MonsterSkins.SILURIAN),
    SLITHEEN("Slitheen", EntityType.ZOMBIE, "slitheen", SlitheenVariant.SLITHEEN_STATIC.getKey(), Material.TURTLE_EGG, SlitheenVariant.SLITHEEN_HEAD.getKey(), MonsterSkins.SLITHEEN),
    SMILER("Smiler", EntityType.ZOMBIE, "smiler", SmilerVariant.SMILER_STATIC.getKey(), Material.RED_STAINED_GLASS_PANE, SmilerVariant.SMILER_HEAD.getKey(), MonsterSkins.SMILER),
    SONTARAN("Sontaran", EntityType.ZOMBIE, "sontaran", SontaranVariant.SONTARAN_STATIC.getKey(), Material.POTATO, SontaranVariant.SONTARAN_HEAD.getKey(), MonsterSkins.SONTARAN),
    STRAX("Strax", EntityType.ZOMBIFIED_PIGLIN, "strax", StraxVariant.STRAX_STATIC.getKey(), Material.BAKED_POTATO, StraxVariant.STRAX_HEAD.getKey(), null),
    SUTEKH("Sutekh", EntityType.STRAY, "sutekh", SutekhVariant.SUTEKH_STATIC.getKey(), Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, SutekhVariant.SUTEKH_HEAD.getKey(), MonsterSkins.SUTEKH),
    SYCORAX("Sycorax", EntityType.ZOMBIE, "sycorax", SycoraxVariant.SYCORAX_STATIC.getKey(), Material.BONE_MEAL, SycoraxVariant.SYCORAX_HEAD.getKey(), MonsterSkins.SYCORAX),
    THE_BEAST("The Beast", EntityType.ZOMBIE, "beast", TheBeastVariant.THE_BEAST_STATIC.getKey(), Material.FIRE_CHARGE, TheBeastVariant.THE_BEAST_HEAD.getKey(), MonsterSkins.THE_BEAST),
    TOCLAFANE("Toclafane", EntityType.ZOMBIE, "toclafane", ToclafaneVariant.TOCLAFANE.getKey(), Material.GUNPOWDER, null, null),
    VAMPIRE_OF_VENICE("Vampire Of Venice", EntityType.DROWNED, "vampire", VampireOfVeniceVariant.VAMPIRE_STATIC.getKey(), Material.COD, VampireOfVeniceVariant.VAMPIRE_HEAD.getKey(), MonsterSkins.VAMPIRE_OF_VENICE),
    VASHTA_NERADA("Vashta Nerada", EntityType.ZOMBIE, "vashta", VashtaNeradaVariant.VASHTA_NERADA_STATIC.getKey(), Material.BOOK, VashtaNeradaVariant.VASHTA_NERADA_HEAD.getKey(), MonsterSkins.VASHTA_NERADA),
    WEEPING_ANGEL("Weeping Angel", EntityType.SKELETON, "angel", WeepingAngelVariant.WEEPING_ANGEL_STATIC.getKey(), Material.BRICK, WeepingAngelVariant.WEEPING_ANGEL_PLAYER_HEAD.getKey(), MonsterSkins.WEEPING_ANGEL),
    ZYGON("Zygon", EntityType.ZOMBIE, "zygon", ZygonVariant.ZYGON_STATIC.getKey(), Material.PAINTING, ZygonVariant.ZYGON_HEAD.getKey(), MonsterSkins.ZYGON);

    private final String name;
    private final EntityType entityType;
    private final String permission;
    private final NamespacedKey model;
    private final Material material;
    private final NamespacedKey headModel;
    private final Skin skin;
    private final List<NamespacedKey> cyberVariants = new ArrayList<>();

    Monster(String name, EntityType entityType, String permission, NamespacedKey model, Material material, NamespacedKey headModel, Skin skin) {
        this.name = name;
        this.entityType = entityType;
        this.permission = permission;
        this.model = model;
        this.material = material;
        this.headModel = headModel;
        this.skin = skin;
        cyberVariants.add(ArmourVariant.CYBERMAN.getKey());
        cyberVariants.add(ArmourVariant.BLACK_CYBERMAN.getKey());
        cyberVariants.add(ArmourVariant.CYBERMAN_EARTHSHOCK.getKey());
        cyberVariants.add(ArmourVariant.CYBERMAN_INVASION.getKey());
        cyberVariants.add(ArmourVariant.CYBERMAN_MOONBASE.getKey());
        cyberVariants.add(ArmourVariant.CYBERMAN_RISE.getKey());
        cyberVariants.add(ArmourVariant.CYBERMAN_TENTH_PLANET.getKey());
        cyberVariants.add(ArmourVariant.CYBER_LORD.getKey());
        cyberVariants.add(ArmourVariant.WOOD_CYBERMAN.getKey());
        cyberVariants.add(ArmourVariant.CYBERSHADE.getKey());
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

    public NamespacedKey getArmourKey() {
        NamespacedKey key = null;
        switch (this) {
            case ANGEL_OF_LIBERTY -> key = ArmourVariant.ANGEL_OF_LIBERTY.getKey();
            case CLOCKWORK_DROID -> {
                if (TARDISConstants.RANDOM.nextBoolean()) {
                    key = ArmourVariant.CLOCKWORK_DROID.getKey();
                } else {
                    key = ArmourVariant.CLOCKWORK_DROID_FEMALE.getKey();
                }
            }
            case CYBERMAN -> key = cyberVariants.get(TARDISConstants.RANDOM.nextInt(cyberVariants.size()));
            case CYBERSHADE -> key = ArmourVariant.CYBERSHADE.getKey();
            case DALEK_SEC -> key = ArmourVariant.DALEK_SEC.getKey();
            case EMPTY_CHILD -> key = ArmourVariant.EMPTY_CHILD.getKey();
            case HATH -> key = ArmourVariant.HATH.getKey();
            case HEADLESS_MONK -> key = ArmourVariant.HEADLESS_MONK.getKey();
            case HEAVENLY_HOST -> key = ArmourVariant.HEAVENLY_HOST.getKey();
            case ICE_WARRIOR -> key = ArmourVariant.ICE_WARRIOR.getKey();
            case JUDOON -> key = ArmourVariant.JUDOON.getKey();
            case MIRE -> key = ArmourVariant.MIRE.getKey();
            case NIMON -> key = ArmourVariant.NIMON.getKey();
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
            case OMEGA -> key = ArmourVariant.OMEGA.getKey();
            case OSSIFIED -> key = ArmourVariant.OSSIFIED.getKey();
            case RACNOSS -> key = ArmourVariant.RACNOSS.getKey();
            case SATURNYNIAN -> key = ArmourVariant.SATURNYNIAN.getKey();
            case SCARECROW -> key = ArmourVariant.SCARECROW.getKey();
            case SEA_DEVIL -> key = ArmourVariant.SEA_DEVIL.getKey();
            case SILENT -> key = ArmourVariant.SILENCE.getKey();
            case SILURIAN -> key = ArmourVariant.SILURIAN.getKey();
            case SLITHEEN -> key = ArmourVariant.SLITHEEN.getKey();
            case SMILER -> key = ArmourVariant.SMILER.getKey();
            case SONTARAN -> key = ArmourVariant.SONTARAN.getKey();
            case STRAX -> key = ArmourVariant.STRAX.getKey();
            case SUTEKH -> key = ArmourVariant.SUTEKH.getKey();
            case SYCORAX -> key = ArmourVariant.SYCORAX.getKey();
            case THE_BEAST -> key = ArmourVariant.THE_BEAST.getKey();
            case VAMPIRE_OF_VENICE -> key = ArmourVariant.VAMPIRE_OF_VENICE.getKey();
            case VASHTA_NERADA -> key = ArmourVariant.VASHTA_NERADA.getKey();
            case WEEPING_ANGEL -> key = ArmourVariant.WEEPING_ANGEL.getKey();
            case ZYGON -> key = ArmourVariant.ZYGON.getKey();
            default -> {
            }
        }
        return key;
    }
}
