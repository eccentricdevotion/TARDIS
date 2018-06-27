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
public class TARDISColumnPreset extends TARDISPreset {

//    private final String blueprint_id = "[[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[109,109,109,44],[71,71,109,44],[0,0,4,44],[0,0,68,0]]";
//    private final String stained_id = "[[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[71,71,95,95],[0,0,95,95],[0,0,68,0]]";
//    private final String glass_id = "[[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[71,71,20,20],[0,0,0,20],[0,0,68,0]]";

    public TARDISColumnPreset() {
        // set blueprint data strings
        String[][] blueprintData = new String[10][4];
        blueprintData[0][0] = "";
        blueprintData[0][1] = "";
        blueprintData[0][2] = "";
        blueprintData[0][3] = "";
        blueprintData[1][0] = "";
        blueprintData[1][1] = "";
        blueprintData[1][2] = "";
        blueprintData[1][3] = "";
        blueprintData[2][0] = "";
        blueprintData[2][1] = "";
        blueprintData[2][2] = "";
        blueprintData[2][3] = "";
        blueprintData[3][0] = "";
        blueprintData[3][1] = "";
        blueprintData[3][2] = "";
        blueprintData[3][3] = "";
        blueprintData[4][0] = "";
        blueprintData[4][1] = "";
        blueprintData[4][2] = "";
        blueprintData[4][3] = "";
        blueprintData[5][0] = "";
        blueprintData[5][1] = "";
        blueprintData[5][2] = "";
        blueprintData[5][3] = "";
        blueprintData[6][0] = "";
        blueprintData[6][1] = "";
        blueprintData[6][2] = "";
        blueprintData[6][3] = "";
        blueprintData[7][0] = "";
        blueprintData[7][1] = "";
        blueprintData[7][2] = "";
        blueprintData[7][3] = "";
        blueprintData[8][0] = "";
        blueprintData[8][1] = "";
        blueprintData[8][2] = "";
        blueprintData[8][3] = "";
        blueprintData[9][0] = "";
        blueprintData[9][1] = "";
        blueprintData[9][2] = "";
        blueprintData[9][3] = "";
        setBlueprintData(blueprintData);
        // set stained data strings
        String[][] stainedData = new String[10][4];
        stainedData[0][0] = "";
        stainedData[0][1] = "";
        stainedData[0][2] = "";
        stainedData[0][3] = "";
        stainedData[1][0] = "";
        stainedData[1][1] = "";
        stainedData[1][2] = "";
        stainedData[1][3] = "";
        stainedData[2][0] = "";
        stainedData[2][1] = "";
        stainedData[2][2] = "";
        stainedData[2][3] = "";
        stainedData[3][0] = "";
        stainedData[3][1] = "";
        stainedData[3][2] = "";
        stainedData[3][3] = "";
        stainedData[4][0] = "";
        stainedData[4][1] = "";
        stainedData[4][2] = "";
        stainedData[4][3] = "";
        stainedData[5][0] = "";
        stainedData[5][1] = "";
        stainedData[5][2] = "";
        stainedData[5][3] = "";
        stainedData[6][0] = "";
        stainedData[6][1] = "";
        stainedData[6][2] = "";
        stainedData[6][3] = "";
        stainedData[7][0] = "";
        stainedData[7][1] = "";
        stainedData[7][2] = "";
        stainedData[7][3] = "";
        stainedData[8][0] = "";
        stainedData[8][1] = "";
        stainedData[8][2] = "";
        stainedData[8][3] = "";
        stainedData[9][0] = "";
        stainedData[9][1] = "";
        stainedData[9][2] = "";
        stainedData[9][3] = "";
        setStainedData(stainedData);
        // set glass data strings
        String[][] glassData = new String[10][4];
        glassData[0][0] = "";
        glassData[0][1] = "";
        glassData[0][2] = "";
        glassData[0][3] = "";
        glassData[1][0] = "";
        glassData[1][1] = "";
        glassData[1][2] = "";
        glassData[1][3] = "";
        glassData[2][0] = "";
        glassData[2][1] = "";
        glassData[2][2] = "";
        glassData[2][3] = "";
        glassData[3][0] = "";
        glassData[3][1] = "";
        glassData[3][2] = "";
        glassData[3][3] = "";
        glassData[4][0] = "";
        glassData[4][1] = "";
        glassData[4][2] = "";
        glassData[4][3] = "";
        glassData[5][0] = "";
        glassData[5][1] = "";
        glassData[5][2] = "";
        glassData[5][3] = "";
        glassData[6][0] = "";
        glassData[6][1] = "";
        glassData[6][2] = "";
        glassData[6][3] = "";
        glassData[7][0] = "";
        glassData[7][1] = "";
        glassData[7][2] = "";
        glassData[7][3] = "";
        glassData[8][0] = "";
        glassData[8][1] = "";
        glassData[8][2] = "";
        glassData[8][3] = "";
        glassData[9][0] = "";
        glassData[9][1] = "";
        glassData[9][2] = "";
        glassData[9][3] = "";
        setGlassData(glassData);
    }
}
