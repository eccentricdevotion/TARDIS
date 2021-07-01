/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
class TARDISLampPostPreset extends TARDISPreset {

    TARDISLampPostPreset() {
        // set blueprint data strings
        String[][] blueprintData = new String[10][4];
        blueprintData[0][0] = "minecraft:air";
        blueprintData[0][1] = "minecraft:air";
        blueprintData[0][2] = "minecraft:air";
        blueprintData[0][3] = "minecraft:oak_trapdoor[half=top,facing=north,open=true]";
        blueprintData[1][0] = "minecraft:air";
        blueprintData[1][1] = "minecraft:air";
        blueprintData[1][2] = "minecraft:air";
        blueprintData[1][3] = "minecraft:air";
        blueprintData[2][0] = "minecraft:air";
        blueprintData[2][1] = "minecraft:air";
        blueprintData[2][2] = "minecraft:air";
        blueprintData[2][3] = "minecraft:air";
        blueprintData[3][0] = "minecraft:air";
        blueprintData[3][1] = "minecraft:air";
        blueprintData[3][2] = "minecraft:air";
        blueprintData[3][3] = "minecraft:air";
        blueprintData[4][0] = "minecraft:air";
        blueprintData[4][1] = "minecraft:air";
        blueprintData[4][2] = "minecraft:air";
        blueprintData[4][3] = "minecraft:air";
        blueprintData[5][0] = "minecraft:air";
        blueprintData[5][1] = "minecraft:air";
        blueprintData[5][2] = "minecraft:air";
        blueprintData[5][3] = "minecraft:air";
        blueprintData[6][0] = "minecraft:air";
        blueprintData[6][1] = "minecraft:air";
        blueprintData[6][2] = "minecraft:air";
        blueprintData[6][3] = "minecraft:oak_trapdoor[half=top,facing=south,open=true]";
        blueprintData[7][0] = "minecraft:nether_brick_fence";
        blueprintData[7][1] = "minecraft:nether_brick_fence";
        blueprintData[7][2] = "minecraft:hopper[facing=down]";
        blueprintData[7][3] = "minecraft:glowstone";
        blueprintData[8][0] = "minecraft:air";
        blueprintData[8][1] = "minecraft:air";
        blueprintData[8][2] = "minecraft:air";
        blueprintData[8][3] = "minecraft:oak_trapdoor[half=top,facing=east,open=true]";
        blueprintData[9][0] = "minecraft:air";
        blueprintData[9][1] = "minecraft:air";
        blueprintData[9][2] = "minecraft:air";
        blueprintData[9][3] = "minecraft:oak_trapdoor[half=top,facing=west,open=true]";
        setBlueprintData(blueprintData);
        // set stained data strings
        String[][] stainedData = new String[10][4];
        stainedData[0][0] = "minecraft:air";
        stainedData[0][1] = "minecraft:air";
        stainedData[0][2] = "minecraft:air";
        stainedData[0][3] = "minecraft:oak_trapdoor[half=top,facing=north,open=true]";
        stainedData[1][0] = "minecraft:air";
        stainedData[1][1] = "minecraft:air";
        stainedData[1][2] = "minecraft:air";
        stainedData[1][3] = "minecraft:air";
        stainedData[2][0] = "minecraft:air";
        stainedData[2][1] = "minecraft:air";
        stainedData[2][2] = "minecraft:air";
        stainedData[2][3] = "minecraft:air";
        stainedData[3][0] = "minecraft:air";
        stainedData[3][1] = "minecraft:air";
        stainedData[3][2] = "minecraft:air";
        stainedData[3][3] = "minecraft:air";
        stainedData[4][0] = "minecraft:air";
        stainedData[4][1] = "minecraft:air";
        stainedData[4][2] = "minecraft:air";
        stainedData[4][3] = "minecraft:air";
        stainedData[5][0] = "minecraft:air";
        stainedData[5][1] = "minecraft:air";
        stainedData[5][2] = "minecraft:air";
        stainedData[5][3] = "minecraft:air";
        stainedData[6][0] = "minecraft:air";
        stainedData[6][1] = "minecraft:air";
        stainedData[6][2] = "minecraft:air";
        stainedData[6][3] = "minecraft:oak_trapdoor[half=top,facing=south,open=true]";
        stainedData[7][0] = "minecraft:red_stained_glass";
        stainedData[7][1] = "minecraft:red_stained_glass";
        stainedData[7][2] = "minecraft:gray_stained_glass";
        stainedData[7][3] = "minecraft:yellow_stained_glass";
        stainedData[8][0] = "minecraft:air";
        stainedData[8][1] = "minecraft:air";
        stainedData[8][2] = "minecraft:air";
        stainedData[8][3] = "minecraft:oak_trapdoor[half=top,facing=east,open=true]";
        stainedData[9][0] = "minecraft:air";
        stainedData[9][1] = "minecraft:air";
        stainedData[9][2] = "minecraft:air";
        stainedData[9][3] = "minecraft:oak_trapdoor[half=top,facing=west,open=true]";
        setStainedData(stainedData);
        // set glass data strings
        String[][] glassData = new String[10][4];
        glassData[0][0] = "minecraft:air";
        glassData[0][1] = "minecraft:air";
        glassData[0][2] = "minecraft:air";
        glassData[0][3] = "minecraft:oak_trapdoor[half=top,facing=north,open=true]";
        glassData[1][0] = "minecraft:air";
        glassData[1][1] = "minecraft:air";
        glassData[1][2] = "minecraft:air";
        glassData[1][3] = "minecraft:air";
        glassData[2][0] = "minecraft:air";
        glassData[2][1] = "minecraft:air";
        glassData[2][2] = "minecraft:air";
        glassData[2][3] = "minecraft:air";
        glassData[3][0] = "minecraft:air";
        glassData[3][1] = "minecraft:air";
        glassData[3][2] = "minecraft:air";
        glassData[3][3] = "minecraft:air";
        glassData[4][0] = "minecraft:air";
        glassData[4][1] = "minecraft:air";
        glassData[4][2] = "minecraft:air";
        glassData[4][3] = "minecraft:air";
        glassData[5][0] = "minecraft:air";
        glassData[5][1] = "minecraft:air";
        glassData[5][2] = "minecraft:air";
        glassData[5][3] = "minecraft:air";
        glassData[6][0] = "minecraft:air";
        glassData[6][1] = "minecraft:air";
        glassData[6][2] = "minecraft:air";
        glassData[6][3] = "minecraft:oak_trapdoor[half=top,facing=south,open=true]";
        glassData[7][0] = "minecraft:glass";
        glassData[7][1] = "minecraft:glass";
        glassData[7][2] = "minecraft:glass";
        glassData[7][3] = "minecraft:glass";
        glassData[8][0] = "minecraft:air";
        glassData[8][1] = "minecraft:air";
        glassData[8][2] = "minecraft:air";
        glassData[8][3] = "minecraft:oak_trapdoor[half=top,facing=east,open=true]";
        glassData[9][0] = "minecraft:air";
        glassData[9][1] = "minecraft:air";
        glassData[9][2] = "minecraft:air";
        glassData[9][3] = "minecraft:oak_trapdoor[half=top,facing=west,open=true]";
        setGlassData(glassData);
    }
}
