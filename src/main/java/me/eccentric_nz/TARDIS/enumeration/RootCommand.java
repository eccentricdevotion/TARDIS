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

public enum RootCommand {

    tardis("https://tardis.pages.dev/commands/tardis"),
    tardisadmin("https://tardis.pages.dev/commands/admin"),
    tardisarea("https://tardis.pages.dev/commands/area"),
    tardisartron("http://tardis.pages.dev/artron-cells"),
    tardisbind("https://tardis.pages.dev/commands/bind"),
    tardisbook("https://tardis.pages.dev/books"),
    tardiscall("https://tardis.pages.dev/commands/misc#tardiscall"),
    tardischemistry("https://tardis.pages.dev/commands/chemistry"),
    tardisconfig("https://tardis.pages.dev/commands/config"),
    tardisdev("https://tardis.pages.dev/commands/dev"),
    tardisdisplay("http://tardis.pages.dev/display"),
    tardisgamemode("https://tardis.pages.dev/commands/misc#tardisgamemode"),
    tardisgive("https://tardis.pages.dev/commands/give"),
    tardisgravity("https://tardis.pages.dev/gravity-wells"),
    tardisinfo("Not applicable"),
    tardisjunk("https://tardis.pages.dev/junk-tardis"),
    tardismushroom("https://tardis.pages.dev/commands/misc#tardismushroom"),
    tardisnetherportal("https://tardis.pages.dev/commands/netherportal"),
    tardisprefs("https://tardis.pages.dev/commands/player-preferences"),
    tardisrecipe("https://tardis.pages.dev/commands/recipe"),
    tardisremote("https://tardis.pages.dev/commands/remote"),
    tardisroom("https://tardis.pages.dev/rooms/custom"),
    tardissay("https://tardis.pages.dev/translator"),
    tardisschematic("https://tardis.pages.dev/commands/schematic"),
    tardissudo("https://tardis.pages.dev/commands/sudo"),
    tardisteleport("https://tardis.pages.dev/world-management"),
    tardistime("https://tardis.pages.dev/commands/misc#tardistime"),
    tardistravel("https://tardis.pages.dev/commands/travel"),
    tardisweather("https://tardis.pages.dev/commands/misc#tardisweather"),
    tardisworld("https://tardis.pages.dev/world-management"),
    handles("https://tardis.pages.dev/handles"),
    twa("https://tardis.pages.dev/modules/weeping-angels"),
    vm("https://tardis.pages.dev/modules/vortex-manipulator"),
    tardisshop("https://tardis.pages.dev/modules/tardis-shop");

    public final String URL;

    RootCommand(String URL) {
        this.URL = URL;
    }
}
