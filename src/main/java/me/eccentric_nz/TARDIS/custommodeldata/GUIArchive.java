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

import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIArchive {

	// tardis Archive
	BACK(8, 18, Material.BOWL), SET_SIZE(77, 19, Material.BOWL), SCAN_CONSOLE(75, 20, Material.BOWL), ARCHIVE_CURRENT_CONSOLE(5, 0, Material.BOWL), SMALL(79, 22, Material.BOWL), MEDIUM(62, 23, Material.BOWL), TALL(81, 24, Material.BOWL), CLOSE(1, 26, Material.BOWL);

	private final int customModelData;
	private final int slot;
	private final Material material;

	GUIArchive(int customModelData, int slot, Material material) {
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
		return TARDISStringUtils.sentenceCase(s);
	}
}
