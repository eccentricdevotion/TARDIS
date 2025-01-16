package me.eccentric_nz.TARDIS.rooms.debug;

import me.eccentric_nz.TARDIS.custommodels.keys.*;
import org.bukkit.Material;

import java.util.List;

public class DebugLazarus {

    public static final List<GuiPreview> ICONS = List.of(
            new GuiPreview(Material.BRICK, Features.WEEPING_ANGEL_WINGS.getKey(), "weeping_angel_wings"),
            new GuiPreview(Material.BROWN_STAINED_GLASS_PANE, Button.DOCTOR.getKey(), "doctor"),
            new GuiPreview(Material.BROWN_STAINED_GLASS_PANE, Button.COMPANION.getKey(), "companion"),
            new GuiPreview(Material.BROWN_STAINED_GLASS_PANE, Button.CHARACTER.getKey(), "character"),
            new GuiPreview(Material.BROWN_STAINED_GLASS_PANE, Button.MONSTER.getKey(), "monster"),
            new GuiPreview(Material.COD, Features.VAMPIRE_OF_VENICE_FAN.getKey(), "vampire_of_venice_fan"),
            new GuiPreview(Material.END_STONE, Features.SILENCE_SIDE_HEAD.getKey(), "silence_side_head"),
            new GuiPreview(Material.FEATHER, Features.SILURIAN_CREST.getKey(), "silurian_crest"),
            new GuiPreview(Material.GLASS, Whoniverse.TV.getKey(), "tv"),
            new GuiPreview(Material.IRON_INGOT, Features.CYBERMAN_FEATURES.getKey(), "cyberman_features"),
            new GuiPreview(Material.IRON_INGOT, Features.BLACK_CYBERMAN_FEATURES.getKey(), "black_cyberman_features"),
            new GuiPreview(Material.IRON_INGOT, Features.CYBER_LORD_FEATURES.getKey(), "cyber_lord_features"),
            new GuiPreview(Material.IRON_INGOT, Features.CYBERMAN_EARTHSHOCK_FEATURES.getKey(), "cyberman_earthshock_features"),
            new GuiPreview(Material.IRON_INGOT, Features.CYBERMAN_INVASION_FEATURES.getKey(), "cyberman_invasion_features"),
            new GuiPreview(Material.IRON_INGOT, CybermanVariant.CYBERMAN_INVASION_ARM.getKey(), "cyberman_invasion_arm"),
            new GuiPreview(Material.IRON_INGOT, Features.CYBERMAN_MOONBASE_FEATURES.getKey(), "cyberman_moonbase_features"),
            new GuiPreview(Material.IRON_INGOT, Features.CYBERMAN_RISE_FEATURES.getKey(), "cyberman_rise_features"),
            new GuiPreview(Material.IRON_INGOT, CybermanVariant.CYBERMAN_RISE_ARM.getKey(), "cyberman_rise_features"),
            new GuiPreview(Material.IRON_INGOT, Features.CYBERMAN_TENTH_PLANET_FEATURES.getKey(), "cyberman_tenth_planet_features"),
            new GuiPreview(Material.SPRUCE_BUTTON, Features.WOOD_CYBERMAN_FEATURES.getKey(), "wood_cyberman_features"),
            new GuiPreview(Material.IRON_INGOT, CybermanVariant.CYBER_WEAPON.getKey(), "cyber_weapon"),
            new GuiPreview(Material.SPRUCE_BUTTON, CybermanVariant.WOOD_CYBER_WEAPON.getKey(), "wood_cyber_weapon"),
            new GuiPreview(Material.KELP, Features.SEA_DEVIL_EARS.getKey(), "sea_devil_ears"),
            new GuiPreview(Material.LEATHER, Features.BRIGADIER_LETHBRIDGE_STEWART_HAT.getKey(), "brigadier_lethbridge_stewart_hat"),
            new GuiPreview(Material.LEATHER, Features.ACE_PONYTAIL.getKey(), "ace_ponytail"),
            new GuiPreview(Material.LEATHER, Features.JO_GRANT_HAIR.getKey(), "jo_grant_hair"),
            new GuiPreview(Material.LEATHER, Features.MARTHA_JONES_HAIR.getKey(), "martha_jones_hair"),
            new GuiPreview(Material.LEATHER, Features.TEGAN_HAT.getKey(), "tegan_hat"),
            new GuiPreview(Material.LEATHER, Features.SYCORAX_CAPE.getKey(), "sycorax_cape"),
            new GuiPreview(Material.LEATHER, Features.SUTEKH_FEATURES.getKey(), "sutekh_features"),
            new GuiPreview(Material.LEATHER, Features.THE_BEAST_HORNS.getKey(), "the_beast_horns"),
            new GuiPreview(Material.LEATHER, Features.CYBERSHADE_EARS.getKey(), "cybershade_ears"),
            new GuiPreview(Material.LEATHER, Features.OMEGA_FRILL.getKey(), "omega_frill"),
            new GuiPreview(Material.LEATHER, Features.ANGEL_OF_LIBERTY_CROWN.getKey(), "angel_of_liberty_crown"),
            new GuiPreview(Material.LEATHER, Features.RACNOSS_FEATURES.getKey(), "racnoss_features"),
            new GuiPreview(Material.LEATHER, Features.JENNY_FLINT_KATANA.getKey(), "jenny_flint_katana"),
            new GuiPreview(Material.MANGROVE_PROPAGULE, Features.DALEK_SEC_TENTACLES.getKey(), "dalek_sec_tentacles"),
            new GuiPreview(Material.NETHER_WART, Features.BANNAKAFFALATTA_SPIKES.getKey(), "bannakaffalatta_spikes"),
            new GuiPreview(Material.NETHERITE_SCRAP, Features.MIRE_HELMET.getKey(), "mire_helmet"),
            new GuiPreview(Material.NETHERITE_SCRAP, MireVariant.MIRE_RIGHT_ARM.getKey(), "mire_right_arm"),
            new GuiPreview(Material.NETHERITE_SCRAP, MireVariant.MIRE_LEFT_ARM.getKey(), "mire_left_arm"),
            new GuiPreview(Material.ORANGE_STAINED_GLASS_PANE, Features.IMPOSSIBLE_ASTRONAUT_PACK.getKey(), "impossible_astronaut_pack"),
            new GuiPreview(Material.PAINTING, Features.ZYGON_CREST.getKey(), "zygon_crest"),
            new GuiPreview(Material.POTATO, Features.SONTARAN_EARS.getKey(), "sontaran_ears"),
            new GuiPreview(Material.POTATO, Features.STRAX_EARS.getKey(), "strax_ears"),
            new GuiPreview(Material.PUFFERFISH, Features.HATH_FEATURES.getKey(), "hath_features"),
            new GuiPreview(Material.ROTTEN_FLESH, Features.OOD_FEATURES.getKey(), "ood_features"),
            new GuiPreview(Material.SNOWBALL, Features.ICE_WARRIOR_CREST.getKey(), "ice_warrior_crest"),
            new GuiPreview(Material.SUGAR, EmptyChildVariant.EMPTY_CHILD_MASK.getKey(), "empty_child_mask"),
            new GuiPreview(Material.TORCH, Features.ANGEL_OF_LIBERTY_TORCH.getKey(), "angel_of_liberty_torch"),
            new GuiPreview(Material.TURTLE_EGG, SlitheenVariant.SLITHEEN_HEAD.getKey(), "slitheen_head"),
            new GuiPreview(Material.TURTLE_EGG, SlitheenVariant.SLITHEEN_CLAW_LEFT.getKey(), "slitheen_claw_left"),
            new GuiPreview(Material.TURTLE_EGG, SlitheenVariant.SLITHEEN_CLAW_RIGHT.getKey(), "slitheen_claw_right"),
            new GuiPreview(Material.WHEAT, Features.SCARECROW_EARS.getKey(), "scarecrow_ears"),
            new GuiPreview(Material.YELLOW_DYE, Features.JUDOON_SNOUT.getKey(), "judoon_snout")
    );
}
