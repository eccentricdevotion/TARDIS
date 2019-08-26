/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon.coloured;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class PoliceBoxBlueSouthPreset extends PoliceBoxPreset {

    public PoliceBoxBlueSouthPreset() {

        // set blueprint data strings
        String[][] blueprintData = new String[10][4];
        for (int c = 0; c < 10; c++) {
            for (int r = 0; r < 4; r++) {
                blueprintData[c][r] = "minecraft:air";
            }
        }
        blueprintData[8][0] = "minecraft:oak_trapdoor[facing=east,half=bottom,open=false,powered=false,waterlogged=false]";
        blueprintData[8][1] = "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=true,up=false,west=false]";
        setBlueprintData(blueprintData);
        // set stained data strings
        String[][] stainedData = new String[10][4];
        for (int c = 0; c < 10; c++) {
            for (int r = 0; r < 4; r++) {
                stainedData[c][r] = "minecraft:air";
            }
        }
        stainedData[8][0] = "minecraft:blue_stained_glass";
        stainedData[8][1] = "minecraft:blue_stained_glass";
        stainedData[8][2] = "minecraft:blue_stained_glass";
        setStainedData(stainedData);
        // set glass data strings
        String[][] glassData = new String[10][4];
        for (int c = 0; c < 10; c++) {
            for (int r = 0; r < 4; r++) {
                glassData[c][r] = "minecraft:air";
            }
        }
        glassData[8][0] = "minecraft:glass";
        glassData[8][1] = "minecraft:glass";
        glassData[8][2] = "minecraft:glass";
        setGlassData(glassData);
    }
}
