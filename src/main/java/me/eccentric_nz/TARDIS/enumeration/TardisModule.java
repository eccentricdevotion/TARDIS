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
package me.eccentric_nz.TARDIS.enumeration;

public enum TardisModule {

    BLASTER("Sonic Blaster", "#00AA00"),
    BLUEPRINTS("TARDIS Blueprints", "#5555FF"),
    CHEMISTRY("TARDIS Chemistry", "#FF55FF"),
    DEBUG("TARDIS Debug", "#FF55FF"),
    MAPPING("TARDIS Mapping", "#55FF55"),
    EMERGENCY_PROGRAM("Emergency Program One", "#C70039"),
    HANDLES("Handles", "#00AAAA"),
    HELPER("TARDIS Helper", "#55FFFF"),
    MONSTERS("Weeping Angels", "#FF5555"),
    REGENERATION("Regeneration", "#FFAA00"),
    SHOP("TARDIS Shop", "#FFFF55"),
    TARDIS("TARDIS", "#FFAA00"),
    TRANSLATOR("Universal Translator", "#0000AA"),
    VORTEX_MANIPULATOR("Vortex Manipulator", "#AA00AA"),
    WARNING("TARDIS WARNING", "#FF5555"),
    SEVERE("TARDIS ERROR", "#AA0000"),
    HELPER_WARNING("TARDIS Helper WARNING", "#FF5555"),
    HELPER_SEVERE("TARDIS Helper ERROR", "#AA0000");

    private final String name;
    private final String hex;

    TardisModule(String name, String hex) {
        this.name = name;
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }
}
