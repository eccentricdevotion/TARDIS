/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders.interior;

import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.Material;

/**
 * @author eccentric_nz
 */
public class TARDISBuildData {

    private Schematic schematic;
    private Material wallType;
    private Material floorType;
    private ChameleonPreset preset;
    private StructureVariant variant;

    public Schematic getSchematic() {
        return schematic;
    }

    public void setSchematic(Schematic schematic) {
        this.schematic = schematic;
    }

    public Material getWallType() {
        return wallType;
    }

    public void setWallType(Material wallType) {
        this.wallType = wallType;
    }

    public Material getFloorType() {
        return floorType;
    }

    public void setFloorType(Material floorType) {
        this.floorType = floorType;
    }

    public ChameleonPreset getPreset() {
        return preset;
    }

    public void setPreset(ChameleonPreset preset) {
        this.preset = preset;
    }

    /**
     * Sets the StructureVariant for the Fade schematic.
     *
     * @param variant the player's chosen Fade variant.
     */
    public void setVariant(StructureVariant variant) {
        this.variant = variant;
    }

    /**
     * Gets the StructureVariant associated with the Fade schematic.
     *
     * @return the Fade variant.
     */
    public StructureVariant getVariant() {
        return variant;
    }
}
