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
package me.eccentric_nz.TARDIS.blueprints;

public enum BlueprintConsole {

    ANCIENT("tardis.ancient"),
    ARS("tardis.ars"),
    BIGGER("tardis.bigger"),
    BONE("tardis.bone"),
    CAVE("tardis.cave"),
    COPPER("tardis.copper"),
    CORAL("tardis.coral"),
    CURSED("tardis.cursed"),
    DELTA("tardis.delta"),
    DELUXE("tardis.deluxe"),
    DIVISION("tardis.division"),
    EIGHTH("tardis.eighth"),
    ELEVENTH("tardis.eleventh"),
    ENDER("tardis.ender"),
    FACTORY("tardis.factory"),
    FIFTEENTH("tardis.fifteenth"),
    FUGITIVE("tardis.fugitive"),
    HOSPITAL("tardis.hospital"),
    LEGACY_BIGGER("tardis.legacy_bigger"),
    LEGACY_DELUXE("tardis.legacy_deluxe"),
    LEGACY_ELEVENTH("tardis.legacy_eleventh"),
    LEGACY_REDSTONE("tardis.legacy_redstone"),
    MASTER("tardis.master"),
    MECHANICAL("tardis.mechanical"),
    ORIGINAL("tardis.original"),
    PLANK("tardis.plank"),
    PYRAMID("tardis.pyramid"),
    REDSTONE("tardis.redstone"),
    ROTOR("tardis.rotor"),
    RUSTIC("tardis.rustic"),
    SIDRAT("tardis.sidrat"),
    STEAMPUNK("tardis.steampunk"),
    THIRTEENTH("tardis.twelfth"),
    TOM("tardis.tom"),
    TWELFTH("tardis.twelfth"),
    WAR("tardis.war"),
    WEATHERED("tardis.weathered"),
    CUSTOM("tardis.custom");

    private final String permission;

    BlueprintConsole(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
