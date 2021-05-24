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
package me.eccentric_nz.tardis.enumeration;

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public enum DiskCircuit {

	AREA("Area Storage Disk", Material.MUSIC_DISC_BLOCKS), ARS("tardis ars Circuit", Material.GLOWSTONE_DUST), BIOME("Biome Storage Disk", Material.MUSIC_DISC_CAT), BLANK("Blank Storage Disk", Material.MUSIC_DISC_STRAD), BLUEPRINT("Blueprint Disk", Material.MUSIC_DISC_MELLOHI), CHAMELEON("tardis Chameleon Circuit", Material.GLOWSTONE_DUST), CONTROL("Authorised Control Disk", Material.MUSIC_DISC_FAR), HANDLES("Handles Program Disk", Material.MUSIC_DISC_WARD), INPUT("tardis Input Circuit", Material.GLOWSTONE_DUST), INVISIBILITY("tardis Invisibility Circuit", Material.GLOWSTONE_DUST), KEY("tardis Key", Material.valueOf(TARDISPlugin.plugin.getRecipesConfig().getString("shaped.tardis Key.result"))), MATERIALISATION("tardis Materialisation Circuit", Material.GLOWSTONE_DUST), MEMORY("tardis Memory Circuit", Material.GLOWSTONE_DUST), PLAYER("Player Storage Disk", Material.MUSIC_DISC_WAIT), PRESET("Preset Storage Disk", Material.MUSIC_DISC_MALL), RANDOMISER("tardis Randomiser Circuit", Material.GLOWSTONE_DUST), SAVE("Save Storage Disk", Material.MUSIC_DISC_CHIRP), SCANNER("tardis Scanner Circuit", Material.GLOWSTONE_DUST), SONIC("Sonic Screwdriver", Material.valueOf(TARDISPlugin.plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result"))), TEMPORAL("tardis Temporal Circuit", Material.GLOWSTONE_DUST);

	static final List<String> circuitNames = new ArrayList<>();
	static final List<DiskCircuit> tardisCircuits = new ArrayList<>();

	static {
		for (DiskCircuit circuit : values()) {
			if (circuit.getName().endsWith("Circuit")) {
				circuitNames.add(circuit.getName());
			}
			if (circuit.getMaterial() == Material.GLOWSTONE_DUST) {
				tardisCircuits.add(circuit);
			}
		}
	}

	String name;
	Material material;

	DiskCircuit(String name, Material material) {
		this.name = name;
		this.material = material;
	}

	public static List<String> getCircuitNames() {
		return circuitNames;
	}

	public static List<DiskCircuit> getTardisCircuits() {
		return tardisCircuits;
	}

	public String getName() {
		return name;
	}

	public Material getMaterial() {
		return material;
	}

	public boolean isDisk() {
		return switch (this) {
			case AREA, BIOME, BLANK, HANDLES, PLAYER, PRESET, SAVE -> true;
			default -> false;
		};
	}
}
