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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.custommodels.keys.Specimen;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.NamespacedKey;

public enum Slide {

    EMPTY_SLIDE(Specimen.EMPTY_SLIDE.getKey()),
    ACORN_BARNACLE(Specimen.ACORN_BARNACLE.getKey()),
    ALGAE(Specimen.ALGAE.getKey()),
    ASCORBIC_ACID_BUBBLE(Specimen.ASCORBIC_ACID_BUBBLE.getKey()),
    BELL_FLOWER_STIGMA(Specimen.BELL_FLOWER_STIGMA.getKey()),
    BROCCOLI(Specimen.BROCCOLI.getKey()),
    BUTTERFLY_WING(Specimen.BUTTERFLY_WING.getKey()),
    CHAMELEON_EMBRYO(Specimen.CHAMELEON_EMBRYO.getKey()),
    COPPER_CRYSTALS(Specimen.COPPER_CRYSTALS.getKey()),
    CURTAIN_FABRIC(Specimen.CURTAIN_FABRIC.getKey()),
    DANDELION(Specimen.DANDELION.getKey()),
    DANDELION_LEAF(Specimen.DANDELION_LEAF.getKey()),
    DANDELION_POLLEN(Specimen.DANDELION_POLLEN.getKey()),
    EMPEROR_BUTTERFLY(Specimen.EMPEROR_BUTTERFLY.getKey()),
    FERN_SORUS(Specimen.FERN_SORUS.getKey()),
    FLEA(Specimen.FLEA.getKey()),
    FLOWER(Specimen.FLOWER.getKey()),
    FLOWER_BUDS(Specimen.FLOWER_BUDS.getKey()),
    FLOWER_SEED_HEAD(Specimen.FLOWER_SEED_HEAD.getKey()),
    FLY_EYE(Specimen.FLY_EYE.getKey()),
    FOSSIL_ZOOPLANKTON(Specimen.FOSSIL_ZOOPLANKTON.getKey()),
    FOUND_IN_WATER(Specimen.FOUND_IN_WATER.getKey()),
    GEOTHITE_IRON_OXIDE_NEEDLES(Specimen.GEOTHITE_IRON_OXIDE_NEEDLES.getKey()),
    GLYCERINE_BASED_SOAPY_SOLUTION(Specimen.GLYCERINE_BASED_SOAPY_SOLUTION.getKey()),
    HUMAN_NEURAL_ROSETTE_PRIMORDIAL_BRAIN_CELLS(Specimen.HUMAN_NEURAL_ROSETTE_PRIMORDIAL_BRAIN_CELLS.getKey()),
    KARLSBAD_SPRUDELSTEIN_SEDIMENTARY_ROCK(Specimen.KARLSBAD_SPRUDELSTEIN_SEDIMENTARY_ROCK.getKey()),
    LILY_POLLEN(Specimen.LILY_POLLEN.getKey()),
    LIQUID_CRYSTAL_RIBBON_FILAMENT(Specimen.LIQUID_CRYSTAL_RIBBON_FILAMENT.getKey()),
    MOTH_PROBISCUS(Specimen.MOTH_PROBISCUS.getKey()),
    MOUSE_MAMMARY_GLAND_ORGANOID(Specimen.MOUSE_MAMMARY_GLAND_ORGANOID.getKey()),
    MOUSE_OVIDUCT_VASCULATURE(Specimen.MOUSE_OVIDUCT_VASCULATURE.getKey()),
    PLANT_HOPPER_NYMPH(Specimen.PLANT_HOPPER_NYMPH.getKey()),
    PLASTIC_FRACTURING_ON_CREDIT_CARD_HOLOGRAM(Specimen.PLASTIC_FRACTURING_ON_CREDIT_CARD_HOLOGRAM.getKey()),
    POLISHED_AGATE(Specimen.POLISHED_AGATE.getKey()),
    PYROMORPHITE_MINERAL(Specimen.PYROMORPHITE_MINERAL.getKey()),
    RED_SPECKLED_JEWELLED_BEETLE(Specimen.RED_SPECKLED_JEWELLED_BEETLE.getKey()),
    SNOWFLAKE(Specimen.SNOWFLAKE.getKey()),
    TITMOUSE_DOWN_FEATHER(Specimen.TITMOUSE_DOWN_FEATHER.getKey()),
    WATER_BLOBS(Specimen.WATER_BLOBS.getKey());

    private final NamespacedKey model;

    Slide(NamespacedKey model) {
        this.model = model;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
