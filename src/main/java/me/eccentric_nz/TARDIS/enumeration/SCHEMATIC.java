/*
 * Copyright (C) 2014 eccentric_nz
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

/**
 *
 * @author eccentric_nz
 */
public enum SCHEMATIC {

    ANTIGRAVITY("antigravity.tschm"),
    ARBORETUM("arboretum.tschm"),
    ARS("ars.tschm"),
    BAKER("baker.tschm"),
    BEDROOM("bedroom.tschm"),
    BIGGER("bigger.tschm"),
    BUDGET("budget.tschm"),
    CUSTOM("custom.tschm"),
    DELUXE("deluxe.tschm"),
    ELEVENTH("eleventh.tschm"),
    EMPTY("empty.tschm"),
    FARM("farm.tschm"),
    GRAVITY("gravity.tschm"),
    GREENHOUSE("greenhouse.tschm"),
    HARMONY("harmony.tschm"),
    KITCHEN("kitchen.tschm"),
    LAZARUS("lazarus.tschm"),
    LIBRARY("library.tschm"),
    MUSHROOM("mushroom.tschm"),
    PASSAGE("passage.tschm"),
    PLANK("plank.tschm"),
    POOL("pool.tschm"),
    RAIL("rail.tschm"),
    REDSTONE("redstone.tschm"),
    RENDERER("renderer.tschm"),
    STABLE("stable.tschm"),
    STEAMPUNK("steampunk.tschm"),
    TOM("tom.tschm"),
    TRENZALORE("trenzalore.tschm"),
    VAULT("vault.tschm"),
    VILLAGE("village.tschm"),
    WAR("war.tschm"),
    WOOD("wood.tschm"),
    WORKSHOP("workshop.tschm"),
    ZERO("zero.tschm");

    String file;

    public String getFile() {
        return file;
    }

    private SCHEMATIC(String file) {
        this.file = file;
    }

    /**
     * Checks if this SCHEMATIC is 1 chunk wide.
     *
     * @return true if this SCHEMATIC is 1 chunk wide.
     */
    public boolean isSmall() {
        switch (this) {
            case ARS:
            case BUDGET:
            case PLANK:
            case STEAMPUNK:
            case TOM:
            case WAR:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if this SCHEMATIC is 2 chunks high.
     *
     * @return true if this SCHEMATIC is 2 chunks high.
     */
    public boolean isTall() {
        switch (this) {
            case DELUXE:
            case ELEVENTH:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if this SCHEMATIC has a beacon.
     *
     * @return true if this SCHEMATIC has a beacon.
     */
    public boolean hasBeacon() {
        switch (this) {
            case ARS:
            case BUDGET:
            case BIGGER:
            case DELUXE:
            case ELEVENTH:
            case REDSTONE:
            case STEAMPUNK:
            case WAR:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if this SCHEMATIC has a beacon.
     *
     * @return true if this SCHEMATIC has a beacon.
     */
    public boolean mustUseSonic() {
        switch (this) {
            case BUDGET:
                return true;
            default:
                return false;
        }
    }
}
