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

public enum Specimen {

    EMPTY_SLIDE(new NamespacedKey(TARDIS.plugin, "slide_empty_slide")),
    ACORN_BARNACLE(new NamespacedKey(TARDIS.plugin, "slide_acorn_barnacle")),
    ALGAE(new NamespacedKey(TARDIS.plugin, "slide_algae")),
    ASCORBIC_ACID_BUBBLE(new NamespacedKey(TARDIS.plugin, "slide_ascorbic_acid_bubble")),
    BELL_FLOWER_STIGMA(new NamespacedKey(TARDIS.plugin, "slide_bell_flower_stigma")),
    BROCCOLI(new NamespacedKey(TARDIS.plugin, "slide_broccoli")),
    BUTTERFLY_WING(new NamespacedKey(TARDIS.plugin, "slide_butterfly_wing")),
    CHAMELEON_EMBRYO(new NamespacedKey(TARDIS.plugin, "slide_chameleon_embryo")),
    COPPER_CRYSTALS(new NamespacedKey(TARDIS.plugin, "slide_copper_crystals")),
    CURTAIN_FABRIC(new NamespacedKey(TARDIS.plugin, "slide_curtain_fabric")),
    DANDELION(new NamespacedKey(TARDIS.plugin, "slide_dandelion")),
    DANDELION_LEAF(new NamespacedKey(TARDIS.plugin, "slide_dandelion_leaf")),
    DANDELION_POLLEN(new NamespacedKey(TARDIS.plugin, "slide_dandelion_pollen")),
    EMPEROR_BUTTERFLY(new NamespacedKey(TARDIS.plugin, "slide_emperor_butterfly")),
    FERN_SORUS(new NamespacedKey(TARDIS.plugin, "slide_fern_sorus")),
    FLEA(new NamespacedKey(TARDIS.plugin, "slide_flea")),
    FLOWER(new NamespacedKey(TARDIS.plugin, "slide_flower")),
    FLOWER_BUDS(new NamespacedKey(TARDIS.plugin, "slide_flower_buds")),
    FLOWER_SEED_HEAD(new NamespacedKey(TARDIS.plugin, "slide_flower_seed_head")),
    FLY_EYE(new NamespacedKey(TARDIS.plugin, "slide_fly_eye")),
    FOSSIL_ZOOPLANKTON(new NamespacedKey(TARDIS.plugin, "slide_fossil_zooplankton")),
    FOUND_IN_WATER(new NamespacedKey(TARDIS.plugin, "slide_found_in_water")),
    GEOTHITE_IRON_OXIDE_NEEDLES(new NamespacedKey(TARDIS.plugin, "slide_geothite_iron_oxide_needles")),
    GLYCERINE_BASED_SOAPY_SOLUTION(new NamespacedKey(TARDIS.plugin, "slide_glycerine_based_soapy_solution")),
    HUMAN_NEURAL_ROSETTE_PRIMORDIAL_BRAIN_CELLS(new NamespacedKey(TARDIS.plugin, "slide_human_neural_rosette_primordial_brain_cells")),
    KARLSBAD_SPRUDELSTEIN_SEDIMENTARY_ROCK(new NamespacedKey(TARDIS.plugin, "slide_karlsbad_sprudelstein_sedimentary_rock")),
    LILY_POLLEN(new NamespacedKey(TARDIS.plugin, "slide_lily_pollen")),
    LIQUID_CRYSTAL_RIBBON_FILAMENT(new NamespacedKey(TARDIS.plugin, "slide_liquid_crystal_ribbon_filament")),
    MOTH_PROBISCUS(new NamespacedKey(TARDIS.plugin, "slide_moth_probiscus")),
    MOUSE_MAMMARY_GLAND_ORGANOID(new NamespacedKey(TARDIS.plugin, "slide_mouse_mammary_gland_organoid")),
    MOUSE_OVIDUCT_VASCULATURE(new NamespacedKey(TARDIS.plugin, "slide_mouse_oviduct_vasculature")),
    PLANT_HOPPER_NYMPH(new NamespacedKey(TARDIS.plugin, "slide_plant_hopper_nymph")),
    PLASTIC_FRACTURING_ON_CREDIT_CARD_HOLOGRAM(new NamespacedKey(TARDIS.plugin, "slide_plastic_fracturing_on_credit_card_hologram")),
    POLISHED_AGATE(new NamespacedKey(TARDIS.plugin, "slide_polished_agate")),
    PYROMORPHITE_MINERAL(new NamespacedKey(TARDIS.plugin, "slide_pyromorphite_mineral")),
    RED_SPECKLED_JEWELLED_BEETLE(new NamespacedKey(TARDIS.plugin, "slide_red_speckled_jewelled_beetle")),
    SNOWFLAKE(new NamespacedKey(TARDIS.plugin, "slide_snowflake")),
    TITMOUSE_DOWN_FEATHER(new NamespacedKey(TARDIS.plugin, "slide_titmouse_down_feather")),
    WATER_BLOBS(new NamespacedKey(TARDIS.plugin, "slide_water_blobs"));

    private final NamespacedKey key;

    Specimen(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
