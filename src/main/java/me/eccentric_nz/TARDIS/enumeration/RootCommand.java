/*
 * Copyright (C) 2023 eccentric_nz
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

    tardis("https://eccentricdevotion.github.io/TARDIS/tardis-commands.html"),
    tardisadmin("https://eccentricdevotion.github.io/TARDIS/admin-commands.html"),
    tardisarea("https://eccentricdevotion.github.io/TARDIS/area-commands.html"),
    tardisartron("http://eccentricdevotion.github.io/TARDIS/artron-cells.html"),
    tardisbind("https://eccentricdevotion.github.io/TARDIS/bind-commands.html"),
    tardisbiome("Not yet available"),
    tardisbook("https://eccentricdevotion.github.io/TARDIS/books.html"),
    tardiscall("Not yet available"),
    tardischemistry("https://eccentricdevotion.github.io/TARDIS/chemistry-lab"),
    tardisconfig("https://eccentricdevotion.github.io/TARDIS/config-commands.html"),
    tardisdev("Not yet available"),
    tardisdisplay("http://eccentricdevotion.github.io/TARDIS/display.html"),
    tardisgamemode("Not yet available"),
    tardisgive("https://eccentricdevotion.github.io/TARDIS/give-commands.html"),
    tardisgravity("https://eccentricdevotion.github.io/TARDIS/gravity-wells.html"),
    tardisinfo("Not yet available"),
    tardisjunk("https://eccentricdevotion.github.io/TARDIS/junk-tardis.html"),
    tardismushroom("Not yet available"),
    tardisnetherportal("https://eccentricdevotion.github.io/TARDIS/netherportal-command.html"),
    tardisprefs("https://eccentricdevotion.github.io/TARDIS/player-preferences.html"),
    tardisrecipe("https://eccentricdevotion.github.io/TARDIS/recipe-commands.html"),
    tardisremote("https://eccentricdevotion.github.io/TARDIS/remote-commands.html"),
    tardisroom("https://eccentricdevotion.github.io/TARDIS/custom-rooms.html"),
    tardissay("https://eccentricdevotion.github.io/TARDIS/translator.html"),
    tardisschematic("https://eccentricdevotion.github.io/TARDIS/schematic-commands.html"),
    tardissudo("https://eccentricdevotion.github.io/TARDIS/sudo-commands.html"),
    tardisteleport("https://eccentricdevotion.github.io/TARDIS/world-management.html"),
    tardistexture("https://eccentricdevotion.github.io/TARDIS/texture-commands.htmla"),
    tardistime("Not yet available"),
    tardistravel("https://eccentricdevotion.github.io/TARDIS/travel-commands.html"),
    tardisweather("Not yet available"),
    tardisworld("https://eccentricdevotion.github.io/TARDIS/world-management.html"),
    handles("https://eccentricdevotion.github.io/TARDIS/handles.html");

    public final String URL;

    RootCommand(String URL) {
        this.URL = URL;
    }
}
