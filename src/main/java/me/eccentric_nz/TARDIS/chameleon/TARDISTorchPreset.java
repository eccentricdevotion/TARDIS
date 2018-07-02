/*
 * Copyright (C) 2018 eccentric_nz
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
public class TARDISTorchPreset extends TARDISPreset {

//    private final String blueprint_id = "[[0,0,0,0],[109,0,109,109],[0,0,0,0],[109,0,109,109],[96,0,0,0],[109,0,109,109],[0,0,0,0],[109,0,109,109],[98,98,98,87],[0,0,0,0]]";
//    private final String stained_id = "[[0,0,0,0],[95,0,95,95],[0,0,0,0],[95,0,95,95],[96,0,0,0],[95,0,95,95],[0,0,0,0],[95,0,95,95],[95,95,95,95],[0,0,0,0]]";
//    private final String glass_id = "[[0,0,0,0],[20,0,20,20],[0,0,0,0],[20,0,20,20],[96,0,0,0],[20,0,20,20],[0,0,0,0],[20,0,20,20],[20,20,20,20],[0,0,0,0]]";

    public TARDISTorchPreset() {
        // set blueprint data strings
        String[][] blueprintData = new String[10][4];
        blueprintData[0][0] = "minecraft:";
        blueprintData[0][1] = "minecraft:";
        blueprintData[0][2] = "minecraft:";
        blueprintData[0][3] = "minecraft:";
        blueprintData[1][0] = "minecraft:";
        blueprintData[1][1] = "minecraft:";
        blueprintData[1][2] = "minecraft:";
        blueprintData[1][3] = "minecraft:";
        blueprintData[2][0] = "minecraft:";
        blueprintData[2][1] = "minecraft:";
        blueprintData[2][2] = "minecraft:";
        blueprintData[2][3] = "minecraft:";
        blueprintData[3][0] = "minecraft:";
        blueprintData[3][1] = "minecraft:";
        blueprintData[3][2] = "minecraft:";
        blueprintData[3][3] = "minecraft:";
        blueprintData[4][0] = "minecraft:";
        blueprintData[4][1] = "minecraft:";
        blueprintData[4][2] = "minecraft:";
        blueprintData[4][3] = "minecraft:";
        blueprintData[5][0] = "minecraft:";
        blueprintData[5][1] = "minecraft:";
        blueprintData[5][2] = "minecraft:";
        blueprintData[5][3] = "minecraft:";
        blueprintData[6][0] = "minecraft:";
        blueprintData[6][1] = "minecraft:";
        blueprintData[6][2] = "minecraft:";
        blueprintData[6][3] = "minecraft:";
        blueprintData[7][0] = "minecraft:iron_door[half=lower,hinge=right,facing=east,open=false]";
        blueprintData[7][1] = "minecraft:iron_door[half=upper,hinge=right,facing=east,open=false]";
        blueprintData[7][2] = "minecraft:";
        blueprintData[7][3] = "minecraft:";
        blueprintData[8][0] = "minecraft:";
        blueprintData[8][1] = "minecraft:";
        blueprintData[8][2] = "minecraft:";
        blueprintData[8][3] = "minecraft:";
        blueprintData[9][0] = "minecraft:air";
        blueprintData[9][1] = "minecraft:air";
        blueprintData[9][2] = "minecraft:wall_sign[facing=east]";
        blueprintData[9][3] = "minecraft:air";
        setBlueprintData(blueprintData);
        // set stained data strings
        String[][] stainedData = new String[10][4];
        stainedData[0][0] = "minecraft:";
        stainedData[0][1] = "minecraft:";
        stainedData[0][2] = "minecraft:";
        stainedData[0][3] = "minecraft:";
        stainedData[1][0] = "minecraft:";
        stainedData[1][1] = "minecraft:";
        stainedData[1][2] = "minecraft:";
        stainedData[1][3] = "minecraft:";
        stainedData[2][0] = "minecraft:";
        stainedData[2][1] = "minecraft:";
        stainedData[2][2] = "minecraft:";
        stainedData[2][3] = "minecraft:";
        stainedData[3][0] = "minecraft:";
        stainedData[3][1] = "minecraft:";
        stainedData[3][2] = "minecraft:";
        stainedData[3][3] = "minecraft:";
        stainedData[4][0] = "minecraft:";
        stainedData[4][1] = "minecraft:";
        stainedData[4][2] = "minecraft:";
        stainedData[4][3] = "minecraft:";
        stainedData[5][0] = "minecraft:";
        stainedData[5][1] = "minecraft:";
        stainedData[5][2] = "minecraft:";
        stainedData[5][3] = "minecraft:";
        stainedData[6][0] = "minecraft:";
        stainedData[6][1] = "minecraft:";
        stainedData[6][2] = "minecraft:";
        stainedData[6][3] = "minecraft:";
        stainedData[7][0] = "minecraft:iron_door[half=lower,hinge=right,facing=east,open=false]";
        stainedData[7][1] = "minecraft:iron_door[half=upper,hinge=right,facing=east,open=false]";
        stainedData[7][2] = "minecraft:";
        stainedData[7][3] = "minecraft:";
        stainedData[8][0] = "minecraft:";
        stainedData[8][1] = "minecraft:";
        stainedData[8][2] = "minecraft:";
        stainedData[8][3] = "minecraft:";
        stainedData[9][0] = "minecraft:air";
        stainedData[9][1] = "minecraft:air";
        stainedData[9][2] = "minecraft:wall_sign[facing=east]";
        stainedData[9][3] = "minecraft:air";
        setStainedData(stainedData);
        // set glass data strings
        String[][] glassData = new String[10][4];
        glassData[0][0] = "minecraft:";
        glassData[0][1] = "minecraft:";
        glassData[0][2] = "minecraft:";
        glassData[0][3] = "minecraft:";
        glassData[1][0] = "minecraft:";
        glassData[1][1] = "minecraft:";
        glassData[1][2] = "minecraft:";
        glassData[1][3] = "minecraft:";
        glassData[2][0] = "minecraft:";
        glassData[2][1] = "minecraft:";
        glassData[2][2] = "minecraft:";
        glassData[2][3] = "minecraft:";
        glassData[3][0] = "minecraft:";
        glassData[3][1] = "minecraft:";
        glassData[3][2] = "minecraft:";
        glassData[3][3] = "minecraft:";
        glassData[4][0] = "minecraft:";
        glassData[4][1] = "minecraft:";
        glassData[4][2] = "minecraft:";
        glassData[4][3] = "minecraft:";
        glassData[5][0] = "minecraft:";
        glassData[5][1] = "minecraft:";
        glassData[5][2] = "minecraft:";
        glassData[5][3] = "minecraft:";
        glassData[6][0] = "minecraft:";
        glassData[6][1] = "minecraft:";
        glassData[6][2] = "minecraft:";
        glassData[6][3] = "minecraft:";
        glassData[7][0] = "minecraft:iron_door[half=lower,hinge=right,facing=east,open=false]";
        glassData[7][1] = "minecraft:iron_door[half=upper,hinge=right,facing=east,open=false]";
        glassData[7][2] = "minecraft:";
        glassData[7][3] = "minecraft:";
        glassData[8][0] = "minecraft:";
        glassData[8][1] = "minecraft:";
        glassData[8][2] = "minecraft:";
        glassData[8][3] = "minecraft:";
        glassData[9][0] = "minecraft:";
        glassData[9][1] = "minecraft:";
        glassData[9][2] = "minecraft:";
        glassData[9][3] = "minecraft:";
        setGlassData(glassData);
    }
}
