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
package me.eccentric_nz.TARDIS.chameleon.preset;

import me.eccentric_nz.TARDIS.chameleon.TARDISPreset;

/**
 * The Pandorica was a prison hidden under Stonehenge. It was built to hold the Doctor and ensure the safety of the
 * Alliance. Despite being described time and again as the "perfect prison" by the Eleventh Doctor, it also proved to be
 * "easy" to open from the outside; Rory Williams, on instruction from the Doctor, opened it with the sonic
 * screwdriver.
 *
 * @author eccentric_nz
 */
public class TARDISPandoricaPreset extends TARDISPreset {

    public TARDISPandoricaPreset() {
        // set blueprint data strings
        String[][] blueprintData = new String[10][4];
        for (int c = 0; c < 10; c++) {
            for (int r = 0; r < 4; r++) {
                blueprintData[c][r] = "minecraft:air";
            }
        }
        blueprintData[8][0] = "minecraft:oak_trapdoor[facing=east,half=bottom,open=false,powered=false,waterlogged=false]";
        blueprintData[8][1] = "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=true,west=false]";
        setBlueprintData(blueprintData);
        // set stained data strings
        String[][] stainedData = new String[10][4];
        stainedData[0][0] = "minecraft:black_stained_glass";
        stainedData[0][1] = "minecraft:black_stained_glass";
        stainedData[0][2] = "minecraft:black_stained_glass";
        stainedData[0][3] = "minecraft:air";
        stainedData[1][0] = "minecraft:black_stained_glass";
        stainedData[1][1] = "minecraft:black_stained_glass";
        stainedData[1][2] = "minecraft:black_stained_glass";
        stainedData[1][3] = "minecraft:air";
        stainedData[2][0] = "minecraft:black_stained_glass";
        stainedData[2][1] = "minecraft:black_stained_glass";
        stainedData[2][2] = "minecraft:black_stained_glass";
        stainedData[2][3] = "minecraft:air";
        stainedData[3][0] = "minecraft:black_stained_glass";
        stainedData[3][1] = "minecraft:black_stained_glass";
        stainedData[3][2] = "minecraft:black_stained_glass";
        stainedData[3][3] = "minecraft:air";
        stainedData[4][0] = "minecraft:black_stained_glass";
        stainedData[4][1] = "minecraft:black_stained_glass";
        stainedData[4][2] = "minecraft:black_stained_glass";
        stainedData[4][3] = "minecraft:air";
        stainedData[5][0] = "minecraft:black_stained_glass";
        stainedData[5][1] = "minecraft:black_stained_glass";
        stainedData[5][2] = "minecraft:black_stained_glass";
        stainedData[5][3] = "minecraft:air";
        stainedData[6][0] = "minecraft:black_stained_glass";
        stainedData[6][1] = "minecraft:black_stained_glass";
        stainedData[6][2] = "minecraft:black_stained_glass";
        stainedData[6][3] = "minecraft:air";
        stainedData[7][0] = "minecraft:black_stained_glass";
        stainedData[7][1] = "minecraft:black_stained_glass";
        stainedData[7][2] = "minecraft:black_stained_glass";
        stainedData[7][3] = "minecraft:air";
        stainedData[8][0] = "minecraft:black_stained_glass";
        stainedData[8][1] = "minecraft:air";
        stainedData[8][2] = "minecraft:black_stained_glass";
        stainedData[8][3] = "minecraft:air";
        stainedData[9][0] = "minecraft:air";
        stainedData[9][1] = "minecraft:air";
        stainedData[9][2] = "minecraft:air";
        stainedData[9][3] = "minecraft:air";
        setStainedData(stainedData);
        // set glass data strings
        String[][] glassData = new String[10][4];
        glassData[0][0] = "minecraft:glass";
        glassData[0][1] = "minecraft:glass";
        glassData[0][2] = "minecraft:glass";
        glassData[0][3] = "minecraft:air";
        glassData[1][0] = "minecraft:glass";
        glassData[1][1] = "minecraft:glass";
        glassData[1][2] = "minecraft:glass";
        glassData[1][3] = "minecraft:air";
        glassData[2][0] = "minecraft:glass";
        glassData[2][1] = "minecraft:glass";
        glassData[2][2] = "minecraft:glass";
        glassData[2][3] = "minecraft:air";
        glassData[3][0] = "minecraft:glass";
        glassData[3][1] = "minecraft:glass";
        glassData[3][2] = "minecraft:glass";
        glassData[3][3] = "minecraft:air";
        glassData[4][0] = "minecraft:glass";
        glassData[4][1] = "minecraft:glass";
        glassData[4][2] = "minecraft:glass";
        glassData[4][3] = "minecraft:air";
        glassData[5][0] = "minecraft:glass";
        glassData[5][1] = "minecraft:glass";
        glassData[5][2] = "minecraft:glass";
        glassData[5][3] = "minecraft:air";
        glassData[6][0] = "minecraft:glass";
        glassData[6][1] = "minecraft:glass";
        glassData[6][2] = "minecraft:glass";
        glassData[6][3] = "minecraft:air";
        glassData[7][0] = "minecraft:glass";
        glassData[7][1] = "minecraft:glass";
        glassData[7][2] = "minecraft:glass";
        glassData[7][3] = "minecraft:air";
        glassData[8][0] = "minecraft:glass";
        glassData[8][1] = "minecraft:air";
        glassData[8][2] = "minecraft:glass";
        glassData[8][3] = "minecraft:air";
        glassData[9][0] = "minecraft:air";
        glassData[9][1] = "minecraft:air";
        glassData[9][2] = "minecraft:air";
        glassData[9][3] = "minecraft:air";
        setGlassData(glassData);
    }
}
