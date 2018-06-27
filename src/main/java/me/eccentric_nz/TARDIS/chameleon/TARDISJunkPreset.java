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

import me.eccentric_nz.TARDIS.enumeration.COMPASS;

/**
 * The Junk TARDIS is a makeshift TARDIS created by the Eleventh Doctor and Idris (the embodied matrix of his TARDIS).
 * <p>
 * They built it from bits of TARDISes in the Bubble universe's junkyard, a task which Idris called "impossible". This
 * TARDIS consisted solely of a console (complete with time rotor) and only three walls. Because it lacked a proper
 * shell, the Doctor said it would be very dangerous to fly.
 *
 * @author eccentric_nz
 */
public class TARDISJunkPreset extends TARDISPreset {

//    private final String blueprint_id = "[[35,98,35,98],[35,98,35,98],[35,35,35,35],[35,35,35,35],[35,35,35,35],[44,0,68,139],[159,44,69,35],[44,0,0,139],[35,0,0,0],[0,0,0,0]]";
//    private final String stained_id = "[[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,0,68,95],[95,95,95,95],[95,0,0,95],[95,0,0,0],[0,0,0,0]]";
//    private final String glass_id = "[[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,0,68,20],[20,20,20,20],[20,0,0,20],[20,0,0,0],[0,0,0,0]]";
//    private final String west_blueprint_id = "[[35,35,35,35],[44,0,68,139],[159,44,69,35],[44,0,0,139],[35,98,35,98],[35,98,35,98],[35,35,35,35],[35,35,35,35],[35,0,0,0],[0,0,0,0]]";
//    private final String west_stained_id = "[[95,95,95,95],[95,0,68,95],[95,95,95,95],[95,0,0,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,0,0,0],[0,0,0,0]]";
//    private final String west_glass_id = "[[20,20,20,20],[20,0,68,20],[20,20,20,20],[20,0,0,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,0,0,0],[0,0,0,0]]";

    String[][] blueprintData = new String[10][4];
    String[][] stainedData = new String[10][4];
    String[][] glassData = new String[10][4];
    String[][] blueprintWest = new String[10][4];
    String[][] stainedWest = new String[10][4];
    String[][] glassWest = new String[10][4];

    public TARDISJunkPreset() {
        // set blueprint data strings
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
        // set blueprint WEST data strings
        blueprintWest[0][0] = "";
        blueprintWest[0][1] = "";
        blueprintWest[0][2] = "";
        blueprintWest[0][3] = "";
        blueprintWest[1][0] = "";
        blueprintWest[1][1] = "";
        blueprintWest[1][2] = "";
        blueprintWest[1][3] = "";
        blueprintWest[2][0] = "";
        blueprintWest[2][1] = "";
        blueprintWest[2][2] = "";
        blueprintWest[2][3] = "";
        blueprintWest[3][0] = "";
        blueprintWest[3][1] = "";
        blueprintWest[3][2] = "";
        blueprintWest[3][3] = "";
        blueprintWest[4][0] = "";
        blueprintWest[4][1] = "";
        blueprintWest[4][2] = "";
        blueprintWest[4][3] = "";
        blueprintWest[5][0] = "";
        blueprintWest[5][1] = "";
        blueprintWest[5][2] = "";
        blueprintWest[5][3] = "";
        blueprintWest[6][0] = "";
        blueprintWest[6][1] = "";
        blueprintWest[6][2] = "";
        blueprintWest[6][3] = "";
        blueprintWest[7][0] = "";
        blueprintWest[7][1] = "";
        blueprintWest[7][2] = "";
        blueprintWest[7][3] = "";
        blueprintWest[8][0] = "";
        blueprintWest[8][1] = "";
        blueprintWest[8][2] = "";
        blueprintWest[8][3] = "";
        blueprintWest[9][0] = "";
        blueprintWest[9][1] = "";
        blueprintWest[9][2] = "";
        blueprintWest[9][3] = "";
        // set stained WEST data strings
        stainedWest[0][0] = "";
        stainedWest[0][1] = "";
        stainedWest[0][2] = "";
        stainedWest[0][3] = "";
        stainedWest[1][0] = "";
        stainedWest[1][1] = "";
        stainedWest[1][2] = "";
        stainedWest[1][3] = "";
        stainedWest[2][0] = "";
        stainedWest[2][1] = "";
        stainedWest[2][2] = "";
        stainedWest[2][3] = "";
        stainedWest[3][0] = "";
        stainedWest[3][1] = "";
        stainedWest[3][2] = "";
        stainedWest[3][3] = "";
        stainedWest[4][0] = "";
        stainedWest[4][1] = "";
        stainedWest[4][2] = "";
        stainedWest[4][3] = "";
        stainedWest[5][0] = "";
        stainedWest[5][1] = "";
        stainedWest[5][2] = "";
        stainedWest[5][3] = "";
        stainedWest[6][0] = "";
        stainedWest[6][1] = "";
        stainedWest[6][2] = "";
        stainedWest[6][3] = "";
        stainedWest[7][0] = "";
        stainedWest[7][1] = "";
        stainedWest[7][2] = "";
        stainedWest[7][3] = "";
        stainedWest[8][0] = "";
        stainedWest[8][1] = "";
        stainedWest[8][2] = "";
        stainedWest[8][3] = "";
        stainedWest[9][0] = "";
        stainedWest[9][1] = "";
        stainedWest[9][2] = "";
        stainedWest[9][3] = "";
        // set glass WEST data strings
        glassWest[0][0] = "";
        glassWest[0][1] = "";
        glassWest[0][2] = "";
        glassWest[0][3] = "";
        glassWest[1][0] = "";
        glassWest[1][1] = "";
        glassWest[1][2] = "";
        glassWest[1][3] = "";
        glassWest[2][0] = "";
        glassWest[2][1] = "";
        glassWest[2][2] = "";
        glassWest[2][3] = "";
        glassWest[3][0] = "";
        glassWest[3][1] = "";
        glassWest[3][2] = "";
        glassWest[3][3] = "";
        glassWest[4][0] = "";
        glassWest[4][1] = "";
        glassWest[4][2] = "";
        glassWest[4][3] = "";
        glassWest[5][0] = "";
        glassWest[5][1] = "";
        glassWest[5][2] = "";
        glassWest[5][3] = "";
        glassWest[6][0] = "";
        glassWest[6][1] = "";
        glassWest[6][2] = "";
        glassWest[6][3] = "";
        glassWest[7][0] = "";
        glassWest[7][1] = "";
        glassWest[7][2] = "";
        glassWest[7][3] = "";
        glassWest[8][0] = "";
        glassWest[8][1] = "";
        glassWest[8][2] = "";
        glassWest[8][3] = "";
        glassWest[9][0] = "";
        glassWest[9][1] = "";
        glassWest[9][2] = "";
        glassWest[9][3] = "";
    }

    @Override
    public void makePresets(boolean assymetric, boolean duck) {
        for (COMPASS d : COMPASS.values()) {
            if (d.equals(COMPASS.WEST)) {
                getBlueprint().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, blueprintWest, false, false));
                getStained().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, stainedWest, false, false));
                getGlass().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, glassWest, false, false));
            } else {
                getBlueprint().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, blueprintData, false, false));
                getStained().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, stainedData, false, false));
                getGlass().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, glassData, false, false));
            }
        }
    }
}
