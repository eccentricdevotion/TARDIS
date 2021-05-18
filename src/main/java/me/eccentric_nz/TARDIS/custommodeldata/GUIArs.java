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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public enum GUIArs {

	// Architectural Reconfiguration

	BUTTON_UP(1, 1, Material.CYAN_WOOL),
	BUTTON_DOWN(2, 18, Material.CYAN_WOOL),
	BUTTON_LEFT(3, 9, Material.CYAN_WOOL),
	BUTTON_RIGHT(4, 11, Material.CYAN_WOOL),
	BUTTON_MAP(2, 10, Material.MAP),
	BUTTON_RECON(1, 12, Material.PINK_WOOL),
	BUTTON_LEVEL_B(1, 27, Material.WHITE_WOOL),
	BUTTON_LEVEL(2, 28, Material.YELLOW_WOOL),
	BUTTON_LEVEL_T(3, 29, Material.WHITE_WOOL),
	BUTTON_RESET(1, 30, Material.COBBLESTONE),
	BUTTON_SCROLL_L(1, 36, Material.RED_WOOL),
	BUTTON_SCROLL_R(1, 38, Material.LIME_WOOL),
	BUTTON_JETT(1, 39, Material.TNT),
	BUTTON_MAP_ON(1, -1, Material.BLACK_WOOL),
	EMPTY_SLOT(1, -1, Material.STONE);

	private final int customModelData;
	private final int slot;
	private final Material material;

	GUIArs(int customModelData, int slot, Material material) {
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
		return (this == EMPTY_SLOT) ? "Empty Slot" : TARDIS.plugin.getLanguage().getString(s);
	}
}
