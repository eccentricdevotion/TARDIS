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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.custommodels.keys.Sample;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.NamespacedKey;

public enum Screen {

    EMPTY_SCREEN(Sample.EMPTY_SCREEN.getKey()),
    ARBOREAL_FIELD(Sample.ARBOREAL_FIELD.getKey()),
    BEETLE(Sample.BEETLE.getKey()),
    BEETLE_HEAD(Sample.BEETLE_HEAD.getKey()),
    ENDEOSTIG(Sample.ENDEOSTIG.getKey()),
    FEATHER(Sample.FEATHER.getKey()),
    FRUCTICOSA_FLOWER(Sample.FRUCTICOSA_FLOWER.getKey()),
    FUNGUS_1(Sample.FUNGUS_1.getKey()),
    FUNGUS_2(Sample.FUNGUS_2.getKey()),
    GIARDIA_MURIS(Sample.GIARDIA_MURIS.getKey()),
    GLUE(Sample.GLUE.getKey()),
    INSECT_LEG(Sample.INSECT_LEG.getKey()),
    INSECT_PALP(Sample.INSECT_PALP.getKey()),
    LIZARD_PARASITE(Sample.LIZARD_PARASITE.getKey()),
    POLYESTER(Sample.POLYESTER.getKey()),
    RAYON(Sample.RAYON.getKey()),
    RED_BLOOD_CELLS(Sample.RED_BLOOD_CELLS.getKey()),
    SEED(Sample.SEED.getKey()),
    SPONGE(Sample.SPONGE.getKey()),
    TUBERCULOSIS_BACTERIA(Sample.TUBERCULOSIS_BACTERIA.getKey()),
    WILD_STRAWBERRY(Sample.WILD_STRAWBERRY.getKey());

    private final NamespacedKey model;

    Screen(NamespacedKey model) {
        this.model = model;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
