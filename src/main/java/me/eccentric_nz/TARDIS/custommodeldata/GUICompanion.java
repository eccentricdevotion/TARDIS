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
import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUICompanion {

	// Add Companion
	INFO(1, 45, Material.BOOK), LIST_COMPANIONS(1, 47, Material.WRITABLE_BOOK), BUTTON_CLOSE(1, 53, Material.BOWL);

	private final int customModelData;
	private final int slot;
	private final Material material;

	GUICompanion(int customModelData, int slot, Material material) {
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
		if (s.startsWith("BUTTON")) {
			TARDISPlugin.plugin.getLanguage().getString("s");
		}
		return TARDISStringUtils.sentenceCase(s);
	}
}
