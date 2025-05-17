/*
 * Copyright (C) 2025 eccentric_nz
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

import org.bukkit.Material;

/**
 * @author eccentric_nz
 */
public class Schematic {

    private final String seed;
    private final String permission;
    private final String description;
    private final ConsoleSize consoleSize;
    private final boolean beacon;
    private final TardisLight light;
    private final int startY;
    private final boolean custom;
    private final int preview;

    public Schematic(String seed, String permission, String description, ConsoleSize consoleSize, boolean beacon, TardisLight light, int startY, boolean custom, int preview) {
        this.seed = seed;
        this.permission = permission;
        this.description = description;
        this.consoleSize = consoleSize;
        this.beacon = beacon;
        this.light = light;
        this.startY = startY;
        this.custom = custom;
        this.preview = preview;
    }

    public Schematic(String seed, String permission, String description, ConsoleSize consoleSize, boolean beacon, TardisLight light, boolean custom, int preview) {
        this.seed = seed;
        this.permission = permission;
        this.description = description;
        this.consoleSize = consoleSize;
        this.beacon = beacon;
        this.light = light;
        this.startY = 64;
        this.custom = custom;
        this.preview = preview;
    }

    /**
     * Gets the seed block Material.
     *
     * @return the Material.toString().
     */
    public String getSeed() {
        return seed;
    }

    /**
     * Gets the block type of this Schematic.
     *
     * @return a block type.
     */
    public Material getSeedMaterial() {
        return Material.valueOf(seed);
    }

    /**
     * Gets the Schematic permission node.
     *
     * @return the Material.toString().
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Gets the Schematic description.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the console size for this Schematic.
     *
     * @return the ConsoleSize.
     */
    public ConsoleSize getConsoleSize() {
        return consoleSize;
    }

    /**
     * Checks if this Schematic has a beacon.
     *
     * @return true if this Schematic has a beacon.
     */
    public boolean hasBeacon() {
        return beacon;
    }

    /**
     * Gets which type of light this Schematic uses.
     *
     * @return a TARDIS light type.
     */
    public TardisLight getLights() {
        return light;
    }

    /**
     * Gets the y coordinate this schematic uses.
     *
     * @return the y coordinate to start building the console.
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Checks if this is a custom Schematic.
     *
     * @return true if this Schematic is custom.
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * Gets the TIPS slot number for this console when built as a preview.
     *
     * @return the TIPS slot.
     */
    public int getPreview() {
        return preview;
    }

    /**
     * Checks if players must use the sonic to change the beacon glass colour.
     *
     * @return true or false.
     */
    public boolean mustUseSonic() {
        return permission.equals("budget");
    }
}
