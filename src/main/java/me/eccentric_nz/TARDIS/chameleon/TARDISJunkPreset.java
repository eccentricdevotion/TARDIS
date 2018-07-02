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
        // set blueprint WEST data strings
        blueprintWest[0][0] = "minecraft:";
        blueprintWest[0][1] = "minecraft:";
        blueprintWest[0][2] = "minecraft:";
        blueprintWest[0][3] = "minecraft:";
        blueprintWest[1][0] = "minecraft:";
        blueprintWest[1][1] = "minecraft:";
        blueprintWest[1][2] = "minecraft:";
        blueprintWest[1][3] = "minecraft:";
        blueprintWest[2][0] = "minecraft:";
        blueprintWest[2][1] = "minecraft:";
        blueprintWest[2][2] = "minecraft:";
        blueprintWest[2][3] = "minecraft:";
        blueprintWest[3][0] = "minecraft:";
        blueprintWest[3][1] = "minecraft:";
        blueprintWest[3][2] = "minecraft:";
        blueprintWest[3][3] = "minecraft:";
        blueprintWest[4][0] = "minecraft:";
        blueprintWest[4][1] = "minecraft:";
        blueprintWest[4][2] = "minecraft:";
        blueprintWest[4][3] = "minecraft:";
        blueprintWest[5][0] = "minecraft:";
        blueprintWest[5][1] = "minecraft:";
        blueprintWest[5][2] = "minecraft:";
        blueprintWest[5][3] = "minecraft:";
        blueprintWest[6][0] = "minecraft:";
        blueprintWest[6][1] = "minecraft:";
        blueprintWest[6][2] = "minecraft:";
        blueprintWest[6][3] = "minecraft:";
        blueprintWest[7][0] = "minecraft:";
        blueprintWest[7][1] = "minecraft:";
        blueprintWest[7][2] = "minecraft:";
        blueprintWest[7][3] = "minecraft:";
        blueprintWest[8][0] = "minecraft:";
        blueprintWest[8][1] = "minecraft:";
        blueprintWest[8][2] = "minecraft:";
        blueprintWest[8][3] = "minecraft:";
        blueprintWest[9][0] = "minecraft:";
        blueprintWest[9][1] = "minecraft:";
        blueprintWest[9][2] = "minecraft:";
        blueprintWest[9][3] = "minecraft:";
        // set stained WEST data strings
        stainedWest[0][0] = "minecraft:";
        stainedWest[0][1] = "minecraft:";
        stainedWest[0][2] = "minecraft:";
        stainedWest[0][3] = "minecraft:";
        stainedWest[1][0] = "minecraft:";
        stainedWest[1][1] = "minecraft:";
        stainedWest[1][2] = "minecraft:";
        stainedWest[1][3] = "minecraft:";
        stainedWest[2][0] = "minecraft:";
        stainedWest[2][1] = "minecraft:";
        stainedWest[2][2] = "minecraft:";
        stainedWest[2][3] = "minecraft:";
        stainedWest[3][0] = "minecraft:";
        stainedWest[3][1] = "minecraft:";
        stainedWest[3][2] = "minecraft:";
        stainedWest[3][3] = "minecraft:";
        stainedWest[4][0] = "minecraft:";
        stainedWest[4][1] = "minecraft:";
        stainedWest[4][2] = "minecraft:";
        stainedWest[4][3] = "minecraft:";
        stainedWest[5][0] = "minecraft:";
        stainedWest[5][1] = "minecraft:";
        stainedWest[5][2] = "minecraft:";
        stainedWest[5][3] = "minecraft:";
        stainedWest[6][0] = "minecraft:";
        stainedWest[6][1] = "minecraft:";
        stainedWest[6][2] = "minecraft:";
        stainedWest[6][3] = "minecraft:";
        stainedWest[7][0] = "minecraft:";
        stainedWest[7][1] = "minecraft:";
        stainedWest[7][2] = "minecraft:";
        stainedWest[7][3] = "minecraft:";
        stainedWest[8][0] = "minecraft:";
        stainedWest[8][1] = "minecraft:";
        stainedWest[8][2] = "minecraft:";
        stainedWest[8][3] = "minecraft:";
        stainedWest[9][0] = "minecraft:";
        stainedWest[9][1] = "minecraft:";
        stainedWest[9][2] = "minecraft:";
        stainedWest[9][3] = "minecraft:";
        // set glass WEST data strings
        glassWest[0][0] = "minecraft:";
        glassWest[0][1] = "minecraft:";
        glassWest[0][2] = "minecraft:";
        glassWest[0][3] = "minecraft:";
        glassWest[1][0] = "minecraft:";
        glassWest[1][1] = "minecraft:";
        glassWest[1][2] = "minecraft:";
        glassWest[1][3] = "minecraft:";
        glassWest[2][0] = "minecraft:";
        glassWest[2][1] = "minecraft:";
        glassWest[2][2] = "minecraft:";
        glassWest[2][3] = "minecraft:";
        glassWest[3][0] = "minecraft:";
        glassWest[3][1] = "minecraft:";
        glassWest[3][2] = "minecraft:";
        glassWest[3][3] = "minecraft:";
        glassWest[4][0] = "minecraft:";
        glassWest[4][1] = "minecraft:";
        glassWest[4][2] = "minecraft:";
        glassWest[4][3] = "minecraft:";
        glassWest[5][0] = "minecraft:";
        glassWest[5][1] = "minecraft:";
        glassWest[5][2] = "minecraft:";
        glassWest[5][3] = "minecraft:";
        glassWest[6][0] = "minecraft:";
        glassWest[6][1] = "minecraft:";
        glassWest[6][2] = "minecraft:";
        glassWest[6][3] = "minecraft:";
        glassWest[7][0] = "minecraft:";
        glassWest[7][1] = "minecraft:";
        glassWest[7][2] = "minecraft:";
        glassWest[7][3] = "minecraft:";
        glassWest[8][0] = "minecraft:";
        glassWest[8][1] = "minecraft:";
        glassWest[8][2] = "minecraft:";
        glassWest[8][3] = "minecraft:";
        glassWest[9][0] = "minecraft:";
        glassWest[9][1] = "minecraft:";
        glassWest[9][2] = "minecraft:";
        glassWest[9][3] = "minecraft:";
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
