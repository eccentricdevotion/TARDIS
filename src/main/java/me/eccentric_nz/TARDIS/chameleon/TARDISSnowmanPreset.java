/*
 * Copyright (C) 2020 eccentric_nz
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
 * Snowmen are sculptures fashioned in the shape of men. They are built of snow, and items are usually added as
 * accessories. In 1892 London, the Great Intelligence brought snowmen to life to take over the world. The snowmen could
 * be summoned if someone was thinking about them, thanks to being made of telepathic snow, but if said person thought
 * about melting the snowmen, they would melt.
 *
 * @author eccentric_nz
 */
class TARDISSnowmanPreset extends TARDISPreset {

	TARDISSnowmanPreset() {
		// set blueprint data strings
		String[][] blueprintData = new String[10][4];
		blueprintData[0][0] = "minecraft:air";
		blueprintData[0][1] = "minecraft:air";
		blueprintData[0][2] = "minecraft:snow_block";
		blueprintData[0][3] = "minecraft:air";
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
		blueprintData[6][2] = "minecraft:snow_block";
		blueprintData[6][3] = "minecraft:air";
		blueprintData[7][0] = "minecraft:snow_block";
		blueprintData[7][1] = "minecraft:snow_block";
		blueprintData[7][2] = "minecraft:white_wool";
		blueprintData[7][3] = "minecraft:jack_o_lantern[facing=west]";
		blueprintData[8][0] = "minecraft:air";
		blueprintData[8][1] = "minecraft:air";
		blueprintData[8][2] = "minecraft:oak_trapdoor[half=top,facing=east,open=true]";
		blueprintData[8][3] = "minecraft:air";
		blueprintData[9][0] = "minecraft:air";
		blueprintData[9][1] = "minecraft:air";
		blueprintData[9][2] = "minecraft:oak_wall_sign[facing=west]";
		blueprintData[9][3] = "minecraft:air";
		setBlueprintData(blueprintData);
		// set stained data strings
		String[][] stainedData = new String[10][4];
		stainedData[0][0] = "minecraft:air";
		stainedData[0][1] = "minecraft:air";
		stainedData[0][2] = "minecraft:white_stained_glass";
		stainedData[0][3] = "minecraft:air";
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
		stainedData[6][2] = "minecraft:white_stained_glass";
		stainedData[6][3] = "minecraft:air";
		stainedData[7][0] = "minecraft:white_stained_glass";
		stainedData[7][1] = "minecraft:white_stained_glass";
		stainedData[7][2] = "minecraft:white_stained_glass";
		stainedData[7][3] = "minecraft:orange_stained_glass";
		stainedData[8][0] = "minecraft:air";
		stainedData[8][1] = "minecraft:air";
		stainedData[8][2] = "minecraft:oak_trapdoor[half=top,facing=east,open=true]";
		stainedData[8][3] = "minecraft:air";
		stainedData[9][0] = "minecraft:air";
		stainedData[9][1] = "minecraft:air";
		stainedData[9][2] = "minecraft:oak_wall_sign[facing=west]";
		stainedData[9][3] = "minecraft:air";
		setStainedData(stainedData);
		// set glass data strings
		String[][] glassData = new String[10][4];
		glassData[0][0] = "minecraft:air";
		glassData[0][1] = "minecraft:air";
		glassData[0][2] = "minecraft:glass";
		glassData[0][3] = "minecraft:air";
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
		glassData[6][2] = "minecraft:glass";
		glassData[6][3] = "minecraft:air";
		glassData[7][0] = "minecraft:glass";
		glassData[7][1] = "minecraft:glass";
		glassData[7][2] = "minecraft:glass";
		glassData[7][3] = "minecraft:glass";
		glassData[8][0] = "minecraft:air";
		glassData[8][1] = "minecraft:air";
		glassData[8][2] = "minecraft:oak_trapdoor[half=top,facing=east,open=true]";
		glassData[8][3] = "minecraft:air";
		glassData[9][0] = "minecraft:air";
		glassData[9][1] = "minecraft:air";
		glassData[9][2] = "minecraft:oak_wall_sign[facing=west]";
		glassData[9][3] = "minecraft:air";
		setGlassData(glassData);
	}
}
