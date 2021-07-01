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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.enumeration.CardinalDirection;

/**
 * The Junk TARDIS is a makeshift TARDIS created by the Eleventh Doctor and Idris (the embodied matrix of his TARDIS).
 * <p>
 * They built it from bits of TARDISes in the Bubble universe's junkyard, a task which Idris called "impossible". This
 * TARDIS consisted solely of a console (complete with time rotor) and only three walls. Because it lacked a proper
 * shell, the Doctor said it would be very dangerous to fly.
 *
 * @author eccentric_nz
 */
public class TardisJunkPreset extends TardisPreset {

    final String[][] blueprintData = new String[10][4];
    final String[][] stainedData = new String[10][4];
    final String[][] glassData = new String[10][4];
    final String[][] blueprintWest = new String[10][4];
    final String[][] stainedWest = new String[10][4];
    final String[][] glassWest = new String[10][4];

    public TardisJunkPreset() {
        // set blueprint data strings
        blueprintData[0][0] = "minecraft:light_gray_wool";
        blueprintData[0][1] = "minecraft:stone_bricks";
        blueprintData[0][2] = "minecraft:orange_wool";
        blueprintData[0][3] = "minecraft:stone_bricks";
        blueprintData[1][0] = "minecraft:light_gray_wool";
        blueprintData[1][1] = "minecraft:orange_wool";
        blueprintData[1][2] = "minecraft:stone_bricks";
        blueprintData[1][3] = "minecraft:orange_wool";
        blueprintData[2][0] = "minecraft:light_gray_wool";
        blueprintData[2][1] = "minecraft:air";
        blueprintData[2][2] = "minecraft:air";
        blueprintData[2][3] = "minecraft:air";
        blueprintData[3][0] = "minecraft:light_gray_wool";
        blueprintData[3][1] = "minecraft:stone_bricks";
        blueprintData[3][2] = "minecraft:orange_wool";
        blueprintData[3][3] = "minecraft:stone_bricks";
        blueprintData[4][0] = "minecraft:light_gray_wool";
        blueprintData[4][1] = "minecraft:orange_wool";
        blueprintData[4][2] = "minecraft:stone_bricks";
        blueprintData[4][3] = "minecraft:orange_wool";
        blueprintData[5][0] = "minecraft:smooth_stone_slab[type=bottom,waterlogged=false]";
        blueprintData[5][1] = "minecraft:air";
        blueprintData[5][2] = "minecraft:oak_wall_sign[facing=west,waterlogged=false]";
        blueprintData[5][3] = "minecraft:cobblestone_wall[east=low,north=none,south=none,up=false,waterlogged=false,west=low]";
        blueprintData[6][0] = "minecraft:red_terracotta";
        blueprintData[6][1] = "minecraft:smooth_stone_slab[type=top,waterlogged=false]";
        blueprintData[6][2] = "minecraft:lever[face=floor,facing=north,powered=true]";
        blueprintData[6][3] = "minecraft:light_gray_wool";
        blueprintData[7][0] = "minecraft:smooth_stone_slab[type=bottom,waterlogged=false]";
        blueprintData[7][1] = "minecraft:air";
        blueprintData[7][2] = "minecraft:air";
        blueprintData[7][3] = "minecraft:cobblestone_wall[east=none,north=low,south=low,up=false,waterlogged=false,west=none]";
        blueprintData[8][0] = "minecraft:light_gray_wool";
        blueprintData[8][1] = "minecraft:air";
        blueprintData[8][2] = "minecraft:air";
        blueprintData[8][3] = "minecraft:air";
        blueprintData[9][0] = "minecraft:air";
        blueprintData[9][1] = "minecraft:air";
        blueprintData[9][2] = "minecraft:air";
        blueprintData[9][3] = "minecraft:air";
        setBlueprintData(blueprintData);
        // set stained data strings
        stainedData[0][0] = "minecraft:light_gray_stained_glass";
        stainedData[0][1] = "minecraft:light_gray_stained_glass";
        stainedData[0][2] = "minecraft:orange_stained_glass";
        stainedData[0][3] = "minecraft:light_gray_stained_glass";
        stainedData[1][0] = "minecraft:light_gray_stained_glass";
        stainedData[1][1] = "minecraft:orange_stained_glass";
        stainedData[1][2] = "minecraft:light_gray_stained_glass";
        stainedData[1][3] = "minecraft:orange_stained_glass";
        stainedData[2][0] = "minecraft:light_gray_stained_glass";
        stainedData[2][1] = "minecraft:air";
        stainedData[2][2] = "minecraft:air";
        stainedData[2][3] = "minecraft:air";
        stainedData[3][0] = "minecraft:light_gray_stained_glass";
        stainedData[3][1] = "minecraft:light_gray_stained_glass";
        stainedData[3][2] = "minecraft:orange_stained_glass";
        stainedData[3][3] = "minecraft:light_gray_stained_glass";
        stainedData[4][0] = "minecraft:light_gray_stained_glass";
        stainedData[4][1] = "minecraft:orange_stained_glass";
        stainedData[4][2] = "minecraft:light_gray_stained_glass";
        stainedData[4][3] = "minecraft:orange_stained_glass";
        stainedData[5][0] = "minecraft:light_gray_stained_glass";
        stainedData[5][1] = "minecraft:air";
        stainedData[5][2] = "minecraft:oak_wall_sign[facing=west,waterlogged=false]";
        stainedData[5][3] = "minecraft:light_gray_stained_glass";
        stainedData[6][0] = "minecraft:red_stained_glass";
        stainedData[6][1] = "minecraft:light_gray_stained_glass";
        stainedData[6][2] = "minecraft:light_gray_stained_glass";
        stainedData[6][3] = "minecraft:light_gray_stained_glass";
        stainedData[7][0] = "minecraft:light_gray_stained_glass";
        stainedData[7][1] = "minecraft:air";
        stainedData[7][2] = "minecraft:air";
        stainedData[7][3] = "minecraft:light_gray_stained_glass";
        stainedData[8][0] = "minecraft:light_gray_stained_glass";
        stainedData[8][1] = "minecraft:air";
        stainedData[8][2] = "minecraft:air";
        stainedData[8][3] = "minecraft:air";
        stainedData[9][0] = "minecraft:air";
        stainedData[9][1] = "minecraft:air";
        stainedData[9][2] = "minecraft:air";
        stainedData[9][3] = "minecraft:air";
        setStainedData(stainedData);
        // set glass data strings
        glassData[0][0] = "minecraft:glass";
        glassData[0][1] = "minecraft:glass";
        glassData[0][2] = "minecraft:glass";
        glassData[0][3] = "minecraft:glass";
        glassData[1][0] = "minecraft:glass";
        glassData[1][1] = "minecraft:glass";
        glassData[1][2] = "minecraft:glass";
        glassData[1][3] = "minecraft:glass";
        glassData[2][0] = "minecraft:glass";
        glassData[2][1] = "minecraft:air";
        glassData[2][2] = "minecraft:air";
        glassData[2][3] = "minecraft:air";
        glassData[3][0] = "minecraft:glass";
        glassData[3][1] = "minecraft:glass";
        glassData[3][2] = "minecraft:glass";
        glassData[3][3] = "minecraft:glass";
        glassData[4][0] = "minecraft:glass";
        glassData[4][1] = "minecraft:glass";
        glassData[4][2] = "minecraft:glass";
        glassData[4][3] = "minecraft:glass";
        glassData[5][0] = "minecraft:glass";
        glassData[5][1] = "minecraft:air";
        glassData[5][2] = "minecraft:oak_wall_sign[facing=west,waterlogged=false]";
        glassData[5][3] = "minecraft:glass";
        glassData[6][0] = "minecraft:glass";
        glassData[6][1] = "minecraft:glass";
        glassData[6][2] = "minecraft:glass";
        glassData[6][3] = "minecraft:glass";
        glassData[7][0] = "minecraft:glass";
        glassData[7][1] = "minecraft:air";
        glassData[7][2] = "minecraft:air";
        glassData[7][3] = "minecraft:glass";
        glassData[8][0] = "minecraft:glass";
        glassData[8][1] = "minecraft:air";
        glassData[8][2] = "minecraft:air";
        glassData[8][3] = "minecraft:air";
        glassData[9][0] = "minecraft:air";
        glassData[9][1] = "minecraft:air";
        glassData[9][2] = "minecraft:air";
        glassData[9][3] = "minecraft:air";
        setGlassData(glassData);
        // set blueprint WEST data strings
        blueprintWest[0][0] = "minecraft:light_gray_wool";
        blueprintWest[0][1] = "minecraft:orange_wool";
        blueprintWest[0][2] = "minecraft:stone_bricks";
        blueprintWest[0][3] = "minecraft:orange_wool";
        blueprintWest[1][0] = "minecraft:smooth_stone_slab[type=bottom,waterlogged=false]";
        blueprintWest[1][1] = "minecraft:air";
        blueprintWest[1][2] = "minecraft:oak_wall_sign[facing=east,waterlogged=false]";
        blueprintWest[1][3] = "minecraft:cobblestone_wall[east=low,north=none,south=none,up=false,waterlogged=false,west=low]";
        blueprintWest[2][0] = "minecraft:red_terracotta";
        blueprintWest[2][1] = "minecraft:smooth_stone_slab[type=top,waterlogged=false]";
        blueprintWest[2][2] = "minecraft:lever[face=floor,facing=south,powered=true]";
        blueprintWest[2][3] = "minecraft:light_gray_wool";
        blueprintWest[3][0] = "minecraft:smooth_stone_slab[type=bottom,waterlogged=false]";
        blueprintWest[3][1] = "minecraft:air";
        blueprintWest[3][2] = "minecraft:air";
        blueprintWest[3][3] = "minecraft:cobblestone_wall[east=none,north=low,south=low,up=false,waterlogged=false,west=none]";
        blueprintWest[4][0] = "minecraft:light_gray_wool";
        blueprintWest[4][1] = "minecraft:stone_bricks";
        blueprintWest[4][2] = "minecraft:orange_wool";
        blueprintWest[4][3] = "minecraft:stone_bricks";
        blueprintWest[5][0] = "minecraft:light_gray_wool";
        blueprintWest[5][1] = "minecraft:orange_wool";
        blueprintWest[5][2] = "minecraft:stone_bricks";
        blueprintWest[5][3] = "minecraft:orange_wool";
        blueprintWest[6][0] = "minecraft:light_gray_wool";
        blueprintWest[6][1] = "minecraft:air";
        blueprintWest[6][2] = "minecraft:air";
        blueprintWest[6][3] = "minecraft:air";
        blueprintWest[7][0] = "minecraft:light_gray_wool";
        blueprintWest[7][1] = "minecraft:stone_bricks";
        blueprintWest[7][2] = "minecraft:orange_wool";
        blueprintWest[7][3] = "minecraft:stone_bricks";
        blueprintWest[8][0] = "minecraft:light_gray_wool";
        blueprintWest[8][1] = "minecraft:air";
        blueprintWest[8][2] = "minecraft:air";
        blueprintWest[8][3] = "minecraft:air";
        blueprintWest[9][0] = "minecraft:air";
        blueprintWest[9][1] = "minecraft:air";
        blueprintWest[9][2] = "minecraft:air";
        blueprintWest[9][3] = "minecraft:air";
        // set stained WEST data strings
        stainedWest[0][0] = "minecraft:light_gray_stained_glass";
        stainedWest[0][1] = "minecraft:orange_stained_glass";
        stainedWest[0][2] = "minecraft:light_gray_stained_glass";
        stainedWest[0][3] = "minecraft:orange_stained_glass";
        stainedWest[1][0] = "minecraft:light_gray_stained_glass";
        stainedWest[1][1] = "minecraft:air";
        stainedWest[1][2] = "minecraft:oak_wall_sign[facing=east,waterlogged=false]";
        stainedWest[1][3] = "minecraft:light_gray_stained_glass";
        stainedWest[2][0] = "minecraft:red_stained_glass";
        stainedWest[2][1] = "minecraft:light_gray_stained_glass";
        stainedWest[2][2] = "minecraft:light_gray_stained_glass";
        stainedWest[2][3] = "minecraft:light_gray_stained_glass";
        stainedWest[3][0] = "minecraft:light_gray_stained_glass";
        stainedWest[3][1] = "minecraft:air";
        stainedWest[3][2] = "minecraft:air";
        stainedWest[3][3] = "minecraft:light_gray_stained_glass";
        stainedWest[4][0] = "minecraft:light_gray_stained_glass";
        stainedWest[4][1] = "minecraft:light_gray_stained_glass";
        stainedWest[4][2] = "minecraft:orange_stained_glass";
        stainedWest[4][3] = "minecraft:light_gray_stained_glass";
        stainedWest[5][0] = "minecraft:light_gray_stained_glass";
        stainedWest[5][1] = "minecraft:orange_stained_glass";
        stainedWest[5][2] = "minecraft:light_gray_stained_glass";
        stainedWest[5][3] = "minecraft:orange_stained_glass";
        stainedWest[6][0] = "minecraft:light_gray_stained_glass";
        stainedWest[6][1] = "minecraft:air";
        stainedWest[6][2] = "minecraft:air";
        stainedWest[6][3] = "minecraft:air";
        stainedWest[7][0] = "minecraft:light_gray_stained_glass";
        stainedWest[7][1] = "minecraft:light_gray_stained_glass";
        stainedWest[7][2] = "minecraft:orange_stained_glass";
        stainedWest[7][3] = "minecraft:light_gray_stained_glass";
        stainedWest[8][0] = "minecraft:light_gray_stained_glass";
        stainedWest[8][1] = "minecraft:air";
        stainedWest[8][2] = "minecraft:air";
        stainedWest[8][3] = "minecraft:air";
        stainedWest[9][0] = "minecraft:air";
        stainedWest[9][1] = "minecraft:air";
        stainedWest[9][2] = "minecraft:air";
        stainedWest[9][3] = "minecraft:air";
        // set glass WEST data strings
        glassWest[0][0] = "minecraft:glass";
        glassWest[0][1] = "minecraft:glass";
        glassWest[0][2] = "minecraft:glass";
        glassWest[0][3] = "minecraft:glass";
        glassWest[1][0] = "minecraft:glass";
        glassWest[1][1] = "minecraft:air";
        glassWest[1][2] = "minecraft:oak_wall_sign[facing=east,waterlogged=false]";
        glassWest[1][3] = "minecraft:glass";
        glassWest[2][0] = "minecraft:glass";
        glassWest[2][1] = "minecraft:glass";
        glassWest[2][2] = "minecraft:glass";
        glassWest[2][3] = "minecraft:glass";
        glassWest[3][0] = "minecraft:glass";
        glassWest[3][1] = "minecraft:air";
        glassWest[3][2] = "minecraft:air";
        glassWest[3][3] = "minecraft:glass";
        glassWest[4][0] = "minecraft:glass";
        glassWest[4][1] = "minecraft:glass";
        glassWest[4][2] = "minecraft:glass";
        glassWest[4][3] = "minecraft:glass";
        glassWest[5][0] = "minecraft:glass";
        glassWest[5][1] = "minecraft:glass";
        glassWest[5][2] = "minecraft:glass";
        glassWest[5][3] = "minecraft:glass";
        glassWest[6][0] = "minecraft:glass";
        glassWest[6][1] = "minecraft:air";
        glassWest[6][2] = "minecraft:air";
        glassWest[6][3] = "minecraft:air";
        glassWest[7][0] = "minecraft:glass";
        glassWest[7][1] = "minecraft:glass";
        glassWest[7][2] = "minecraft:glass";
        glassWest[7][3] = "minecraft:glass";
        glassWest[8][0] = "minecraft:glass";
        glassWest[8][1] = "minecraft:air";
        glassWest[8][2] = "minecraft:air";
        glassWest[8][3] = "minecraft:air";
        glassWest[9][0] = "minecraft:air";
        glassWest[9][1] = "minecraft:air";
        glassWest[9][2] = "minecraft:air";
        glassWest[9][3] = "minecraft:air";
    }

    @Override
    public void makePresets(boolean assymetric, boolean duck) {
        for (CardinalDirection d : CardinalDirection.values()) {
            if (d.equals(CardinalDirection.WEST)) {
                getBlueprint().put(d, TardisChameleonPreset.buildTardisChameleonColumn(CardinalDirection.EAST, blueprintWest, false, false));
                getStained().put(d, TardisChameleonPreset.buildTardisChameleonColumn(CardinalDirection.EAST, stainedWest, false, false));
                getGlass().put(d, TardisChameleonPreset.buildTardisChameleonColumn(CardinalDirection.EAST, glassWest, false, false));
            } else {
                getBlueprint().put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, blueprintData, false, false));
                getStained().put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, stainedData, false, false));
                getGlass().put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, glassData, false, false));
            }
        }
    }
}
