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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Features {

    ACE_PONYTAIL(new NamespacedKey(TARDIS.plugin, "ace_ponytail")),
    ANGEL_OF_LIBERTY_CROWN(new NamespacedKey(TARDIS.plugin, "angel_of_liberty_crown")),
    ANGEL_OF_LIBERTY_TORCH(new NamespacedKey(TARDIS.plugin, "angel_of_liberty_torch")),
    BANNAKAFFALATTA_SPIKES(new NamespacedKey(TARDIS.plugin, "bannakaffalatta_spikes")),
    BRIGADIER_LETHBRIDGE_STEWART_HAT(new NamespacedKey(TARDIS.plugin, "brigadier_lethbridge_stewart_hat")),
    CYBERMAN_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_features")),
    BLACK_CYBERMAN_FEATURES(new NamespacedKey(TARDIS.plugin, "black_cyberman_features")),
    CYBERMAN_RISE_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_rise_features")),
    CYBERMAN_EARTHSHOCK_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_earthshock_features")),
    CYBERMAN_INVASION_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_invasion_features")),
    CYBERMAN_MOONBASE_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_moonbase_features")),
    CYBERMAN_TENTH_PLANET_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_tenth_planet_features")),
    CYBER_LORD_FEATURES(new NamespacedKey(TARDIS.plugin, "cyber_lord_features")),
    WOOD_CYBERMAN_FEATURES(new NamespacedKey(TARDIS.plugin, "wood_cyberman_features")),
    CYBERSHADE_EARS(new NamespacedKey(TARDIS.plugin, "cybershade_ears")),
    DALEK_SEC_TENTACLES(new NamespacedKey(TARDIS.plugin, "dalek_sec_tentacles")),
    EMPTY_CHILD_MASK(new NamespacedKey(TARDIS.plugin, "empty_child_mask")),
    HATH_FEATURES(new NamespacedKey(TARDIS.plugin, "hath_features")),
    HEAVENLY_HOST_FEATURES(new NamespacedKey(TARDIS.plugin, "heavenly_host_features")),
    ICE_WARRIOR_CREST(new NamespacedKey(TARDIS.plugin, "ice_warrior_crest")),
    IMPOSSIBLE_ASTRONAUT_PACK(new NamespacedKey(TARDIS.plugin, "impossible_astronaut_pack")),
    JENNY_FLINT_KATANA(new NamespacedKey(TARDIS.plugin, "jenny_flint_katana")),
    JO_GRANT_HAIR(new NamespacedKey(TARDIS.plugin, "jo_grant_hair")),
    JUDOON_SNOUT(new NamespacedKey(TARDIS.plugin, "judoon_snout")),
    MARTHA_JONES_HAIR(new NamespacedKey(TARDIS.plugin, "martha_jones_hair")),
    MELANIE_BUSH_HAIR(new NamespacedKey(TARDIS.plugin, "melanie_bush_hair")),
    MELANIE_BUSH_ARM_LEFT(new NamespacedKey(TARDIS.plugin, "melanie_bush_arm_left")),
    MELANIE_BUSH_ARM_RIGHT(new NamespacedKey(TARDIS.plugin, "melanie_bush_arm_right")),
    MIRE_HELMET(new NamespacedKey(TARDIS.plugin, "mire_helmet")),
    NIMON_HORNS(new NamespacedKey(TARDIS.plugin, "nimon_horns")),
    OMEGA_FRILL(new NamespacedKey(TARDIS.plugin, "omega_frill")),
    OOD_FEATURES(new NamespacedKey(TARDIS.plugin, "ood_features")),
    RACNOSS_FEATURES(new NamespacedKey(TARDIS.plugin, "racnoss_features")),
    ROMAN_RORY_CAPE(new NamespacedKey(TARDIS.plugin, "roman_rory_cape")),
    SATURNYNIAN_ARMS(new NamespacedKey(TARDIS.plugin, "saturnynian_frill")),
    SCARECROW_EARS(new NamespacedKey(TARDIS.plugin, "scarecrow_ears")),
    SEA_DEVIL_EARS(new NamespacedKey(TARDIS.plugin, "sea_devil_ears")),
    SILENCE_SIDE_HEAD(new NamespacedKey(TARDIS.plugin, "silence_side_head")),
    SILURIAN_CREST(new NamespacedKey(TARDIS.plugin, "silurian_crest")),
    SLITHEEN_HEAD(new NamespacedKey(TARDIS.plugin, "slitheen_head")),
    SONTARAN_EARS(new NamespacedKey(TARDIS.plugin, "sontaran_ears")),
    STRAX_EARS(new NamespacedKey(TARDIS.plugin, "strax_ears")),
    SUTEKH_FEATURES(new NamespacedKey(TARDIS.plugin, "sutekh_features")),
    SYCORAX_CAPE(new NamespacedKey(TARDIS.plugin, "sycorax_cape")),
    TEGAN_HAT(new NamespacedKey(TARDIS.plugin, "tegan_hat")),
    THE_BEAST_HORNS(new NamespacedKey(TARDIS.plugin, "the_beast_horns")),
    VAMPIRE_OF_VENICE_FAN(new NamespacedKey(TARDIS.plugin, "vampire_of_venice_fan")),
    WEEPING_ANGEL_WINGS(new NamespacedKey(TARDIS.plugin, "weeping_angel_wings")),
    ZYGON_CREST(new NamespacedKey(TARDIS.plugin, "zygon_crest"));

    private final NamespacedKey key;

    Features(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
