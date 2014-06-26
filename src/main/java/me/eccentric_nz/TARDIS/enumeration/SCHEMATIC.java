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

    ANTIGRAVITY("antigravity.schematic"),
    ARBORETUM("arboretum.schematic"),
    ARS("ars.schematic"),
    BAKER("baker.schematic"),
    BEDROOM("bedroom.schematic"),
    BIGGER("bigger.schematic"),
    BUDGET("budget.schematic"),
    CUSTOM("custom.schematic"),
    DELUXE("deluxe.schematic"),
    ELEVENTH("eleventh.schematic"),
    EMPTY("empty.schematic"),
    FARM("farm.schematic"),
    GRAVITY("gravity.schematic"),
    GREENHOUSE("greenhouse.schematic"),
    HARMONY("harmony.schematic"),
    KITCHEN("kitchen.schematic"),
    LAZARUS("lazarus.schematic"),
    LIBRARY("library.schematic"),
    MUSHROOM("mushroom.schematic"),
    PASSAGE("passage.schematic"),
    PLANK("plank.schematic"),
    POOL("pool.schematic"),
    RAIL("rail.schematic"),
    REDSTONE("redstone.schematic"),
    RENDERER("renderer.schematic"),
    STABLE("stable.schematic"),
    STEAMPUNK("steampunk.schematic"),
    TOM("tom.schematic"),
    TRENZALORE("trenzalore.schematic"),
    VAULT("vault.schematic"),
    VILLAGE("village.schematic"),
    WAR("war.schematic"),
    WOOD("wood.schematic"),
    WORKSHOP("workshop.schematic"),
    ZERO("zero.schematic");

    String file;

    public String getFile() {
        return file;
    }

    private SCHEMATIC(String file) {
        this.file = file;
    }
}
