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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ArmourVariant {

    ANGEL_OF_LIBERTY(new NamespacedKey(TARDIS.plugin, "angel_of_liberty")),
    CLOCKWORK_DROID(new NamespacedKey(TARDIS.plugin, "clockwork_droid")),
    CLOCKWORK_DROID_FEMALE(new NamespacedKey(TARDIS.plugin, "clockwork_droid_female")),
    CYBERMAN(new NamespacedKey(TARDIS.plugin, "cyberman")),
    CYBER_LORD(new NamespacedKey(TARDIS.plugin, "cyber_lord")),
    CYBERMAN_RISE(new NamespacedKey(TARDIS.plugin, "cyberman_rise")),
    BLACK_CYBERMAN(new NamespacedKey(TARDIS.plugin, "black_cyberman")),
    CYBERMAN_EARTHSHOCK(new NamespacedKey(TARDIS.plugin, "cyberman_earthshock")),
    CYBERMAN_INVASION(new NamespacedKey(TARDIS.plugin, "cyberman_invasion")),
    CYBERMAN_MOONBASE(new NamespacedKey(TARDIS.plugin, "cyberman_moonbase")),
    CYBERMAN_TENTH_PLANET(new NamespacedKey(TARDIS.plugin, "cyberman_tenth_planet")),
    WOOD_CYBERMAN(new NamespacedKey(TARDIS.plugin, "wood_cyberman")),
    CYBERSHADE(new NamespacedKey(TARDIS.plugin, "cybershade")),
    DALEK_SEC(new NamespacedKey(TARDIS.plugin, "dalek_sec")),
    EMPTY_CHILD(new NamespacedKey(TARDIS.plugin, "empty_child")),
    HATH(new NamespacedKey(TARDIS.plugin, "hath")),
    HEADLESS_MONK(new NamespacedKey(TARDIS.plugin, "headless_monk")),
    ICE_WARRIOR(new NamespacedKey(TARDIS.plugin, "ice_warrior")),
    JUDOON(new NamespacedKey(TARDIS.plugin, "judoon")),
    MIRE(new NamespacedKey(TARDIS.plugin, "mire")),
    OMEGA(new NamespacedKey(TARDIS.plugin, "omega")),
    OOD_BLACK(new NamespacedKey(TARDIS.plugin, "ood_black")),
    OOD_BLUE(new NamespacedKey(TARDIS.plugin, "ood_blue")),
    OOD_BROWN(new NamespacedKey(TARDIS.plugin, "ood_brown")),
    OSSIFIED(new NamespacedKey(TARDIS.plugin, "ossified")),
    RACNOSS(new NamespacedKey(TARDIS.plugin, "racnoss")),
    SCARECROW(new NamespacedKey(TARDIS.plugin, "scarecrow")),
    SEA_DEVIL(new NamespacedKey(TARDIS.plugin, "sea_devil")),
    SILENCE(new NamespacedKey(TARDIS.plugin, "silence")),
    SILURIAN(new NamespacedKey(TARDIS.plugin, "silurian")),
    SLITHEEN(new NamespacedKey(TARDIS.plugin, "slitheen")),
    SMILER(new NamespacedKey(TARDIS.plugin, "smiler")),
    SONTARAN(new NamespacedKey(TARDIS.plugin, "sontaran")),
    STRAX(new NamespacedKey(TARDIS.plugin, "strax")),
    SUTEKH(new NamespacedKey(TARDIS.plugin, "sutekh")),
    SYCORAX(new NamespacedKey(TARDIS.plugin, "sycorax")),
    THE_BEAST(new NamespacedKey(TARDIS.plugin, "the_beast")),
    VAMPIRE_OF_VENICE(new NamespacedKey(TARDIS.plugin, "vampire_of_venice")),
    VASHTA_NERADA(new NamespacedKey(TARDIS.plugin, "vashta_nerada")),
    WEEPING_ANGEL(new NamespacedKey(TARDIS.plugin, "weeping_angel")),
    ZYGON(new NamespacedKey(TARDIS.plugin, "zygon")),
    CHESTPLATE(new NamespacedKey(TARDIS.plugin, "monster_chestplate")),
    LEGGINGS(new NamespacedKey(TARDIS.plugin, "monster_leggings"));

    private final NamespacedKey key;

    ArmourVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
