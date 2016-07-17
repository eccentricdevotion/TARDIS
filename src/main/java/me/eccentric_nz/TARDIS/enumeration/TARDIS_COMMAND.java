/*
 * Copyright (C) 2016 eccentric_nz
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
public enum TARDIS_COMMAND {

    abandon,
    abort,
    add,
    arch_time,
    archive,
    arsremove,
    colourise,
    colorize,
    comehere,
    cube,
    desktop,
    direction,
    eject,
    ep1,
    erase,
    exterminate,
    find,
    help,
    hide,
    home,
    inside,
    jettison,
    lamps,
    list,
    make_her_blue,
    namekey,
    occupy,
    rebuild,
    remove,
    removesave,
    rescue,
    room,
    save,
    save_player,
    secondary,
    section,
    setdest,
    tagtheood,
    theme,
    update,
    upgrade,
    version;

    public boolean noSiege() {
        switch (this) {
            case archive:
            case colourise:
            case colorize:
            case comehere:
            case desktop:
            case direction:
            case eject:
            case exterminate:
            case hide:
            case home:
            case lamps:
            case make_her_blue:
            case rebuild:
            case rescue:
            case room:
            case setdest:
            case theme:
            case upgrade:
                return true;
            default:
                return false;
        }
    }
}
