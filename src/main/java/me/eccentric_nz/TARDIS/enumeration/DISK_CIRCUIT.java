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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public enum DISK_CIRCUIT {

    AREA("Area Storage Disk", Material.MUSIC_DISC_BLOCKS),
    ARS("TARDIS ARS Circuit", Material.MAP),
    BIOME("Biome Storage Disk", Material.MUSIC_DISC_CAT),
    CHAMELEON("TARDIS Chameleon Circuit", Material.MAP),
    INPUT("TARDIS Input Circuit", Material.MAP),
    INVISIBILITY("TARDIS Invisibility Circuit", Material.MAP),
    KEY("TARDIS Key", Material.valueOf(TARDIS.plugin.getRecipesConfig().getString("shaped.TARDIS Key.result"))),
    MATERIALISATION("TARDIS Materialisation Circuit", Material.MAP),
    MEMORY("TARDIS Memory Circuit", Material.MAP),
    PLAYER("Player Storage Disk", Material.MUSIC_DISC_WAIT),
    PRESET("Preset Storage Disk", Material.MUSIC_DISC_MALL),
    RANDOMISER("TARDIS Randomiser Circuit", Material.MAP),
    SAVE("Save Storage Disk", Material.MUSIC_DISC_CHIRP),
    SCANNER("TARDIS Scanner Circuit", Material.MAP),
    SONIC("Sonic Screwdriver", Material.valueOf(TARDIS.plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result"))),
    TEMPORAL("TARDIS Temporal Circuit", Material.MAP);

    String name;
    Material material;
    static final List<String> circuitNames = new ArrayList<>();

    DISK_CIRCUIT(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isDisk() {
        switch (this) {
            case AREA:
            case BIOME:
            case PLAYER:
            case PRESET:
            case SAVE:
                return true;
            default:
                return false;
        }
    }

    static {
        for (DISK_CIRCUIT circuit : values()) {
            if (circuit.getName().endsWith("Circuit")) {
                circuitNames.add(circuit.getName());
            }
        }
    }

    public static List<String> getCircuitNames() {
        return circuitNames;
    }
}
