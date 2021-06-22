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
package me.eccentric_nz.tardis.blueprints;

public enum BlueprintConsole {

    ARS("tardis.ars"),
    BIGGER("tardis.bigger"),
    CORAL("tardis.coral"),
    CUSTOM("tardis.custom"),
    DELUXE("tardis.deluxe"),
    ELEVENTH("tardis.eleventh"),
    ENDER("tardis.ender"),
    LEGACY_BIGGER("tardis.legacy_bigger"),
    LEGACY_BUDGET("tardis.legacy_budget"),
    LEGACY_DELUXE("tardis.legacy_deluxe"),
    LEGACY_ELEVENTH("tardis.legacy_eleventh"),
    LEGACY_REDSTONE("tardis.legacy_redstone"),
    MASTER("tardis.master"),
    PLANK("tardis.plank"),
    PYRAMID("tardis.pyramid"),
    REDSTONE("tardis.redstone"),
    ROTOR("tardis.rotor"),
    STEAMPUNK("tardis.steampunk"),
    TOM("tardis.tom"),
    TWELFTH("tardis.twelfth"),
    WAR("tardis.war");

    private final String permission;

    BlueprintConsole(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
