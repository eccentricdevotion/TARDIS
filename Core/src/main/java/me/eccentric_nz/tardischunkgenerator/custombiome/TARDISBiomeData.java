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
package me.eccentric_nz.tardischunkgenerator.custombiome;

import net.minecraft.core.particles.ParticleTypes;

public class TARDISBiomeData {

    public static final CustomBiomeData BADLANDS = new CustomBiomeData(
            "badlands",
            "gallifrey_badlands",
            2.0f,
            0,
            16771522, // light apricot fog colour
            4184548,
            340786,
            16763002,
            16309733,
            16714324,
            null,
            0.025f,
            false
    );

    public static final CustomBiomeData DESERT = new CustomBiomeData(
            "desert",
            "skaro_desert",
            2.0f,
            0,
            13615871, // lilac fog colour
            4187262,
            340757,
            10648061,
            16750848, // foliage
            16776960, // grass
            ParticleTypes.ASH,
            0.00625f,
            false
    );

    public static final CustomBiomeData EYE = new CustomBiomeData(
            "crimson_forest",
            "eye_of_harmony",
            2.0f,
            0,
            16744448, // orange fog color
            16744448, // water colour
            16744448, // water fog colour
            16744448, // sky colour
            16744448, // foliage
            16744448, // grass
            ParticleTypes.CRIMSON_SPORE,
            0.025f,
            false
    );
}
