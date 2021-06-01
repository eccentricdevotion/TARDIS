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
package me.eccentric_nz.tardis.custommodeldata;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.utility.TARDISNumberParsers;
import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIDestinationTerminal {

	// Destination Terminal
	STEP_10(1, 1, Material.WHITE_WOOL),
	STEP_25(1, 3, Material.LIGHT_GRAY_WOOL),
	STEP_50(1, 5, Material.GRAY_WOOL),
	STEP_100(1, 7, Material.BLACK_WOOL),
	NEGATIVE_X(1, 9, Material.RED_WOOL),
	X(1, 13, Material.LIGHT_BLUE_WOOL),
	POSITIVE_X(1, 17, Material.LIME_WOOL),
	NEGATIVE_Z(1, 18, Material.RED_WOOL),
	Z(1, 22, Material.YELLOW_WOOL),
	POSITIVE_Z(1, 26, Material.LIME_WOOL),
	NEGATIVE_MULTIPLIER(1, 27, Material.RED_WOOL),
	BUTTON_MULTI(1, 31, Material.PURPLE_WOOL),
	POSITIVE_MULTIPLIER(1, 35, Material.LIME_WOOL),
	BUTTON_CURRENT(1, 36, Material.OAK_LEAVES),
	BUTTON_NORM(1, 38, Material.DIRT),
	NETHER(1, 40, Material.NETHERRACK),
	THE_END(1, 42, Material.END_STONE),
	BUTTON_SUB(1, 44, Material.WATER_BUCKET),
	BUTTON_CHECK(1, 46, Material.PISTON),
	BUTTON_DEST(1, 49, Material.BOOKSHELF),
	BUTTON_CANCEL(1, 52, Material.TNT);

	private final int customModelData;
	private final int slot;
	private final Material material;

	GUIDestinationTerminal(int customModelData, int slot, Material material) {
		this.customModelData = customModelData;
		this.slot = slot;
		this.material = material;
	}

	public int getCustomModelData() {
		return customModelData;
	}

	public int getSlot() {
		return slot;
	}

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		String s = toString();
		if (s.startsWith("STEP")) {
			String[] split = s.split("_");
			return TARDISPlugin.plugin.getLanguage().getString("BUTTON_STEP") + ": " +
				   (TARDISNumberParsers.parseInt(split[1]) *
					TARDISPlugin.plugin.getConfig().getInt("travel.terminal_step"));
		} else if (s.startsWith("POSITIVE")) {
			return TARDISPlugin.plugin.getLanguage().getString("BUTTON_POS");
		} else if (s.startsWith("NEGATIVE")) {
			return TARDISPlugin.plugin.getLanguage().getString("BUTTON_NEG");
		} else if (this == X || this == Z) {
			return s;
		} else if (this == NETHER || this == THE_END) {
			return TARDISStringUtils.capitalise(s);
		} else {
			return TARDISPlugin.plugin.getLanguage().getString(s);
		}
	}
}
