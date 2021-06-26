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
package me.eccentric_nz.TARDIS.enumeration;

/**
 * @author eccentric_nz
 */
public enum TardisCommand {

    abandon,
    abort,
    add,
    arch_time,
    archive,
    arsremove,
    bell,
    check_loc,
    colourise,
    colorize,
    comehere,
    construct,
    cube,
    desktop,
    direction,
    door,
    egg,
    eject,
    ep1,
    erase,
    excite,
    exterminate,
    find,
    handbrake,
    help,
    hide,
    home,
    inside,
    item,
    jettison,
    lamps,
    list,
    make_her_blue,
    namekey,
    occupy,
    rebuild,
    remove,
    removesave,
    renamesave,
    reordersave,
    rescue,
    room,
    save,
    saveicon,
    save_player,
    secondary,
    section,
    setdest,
    sethome,
    tagtheood,
    theme,
    transmat,
    update,
    upgrade,
    version;

    public boolean noSiege() {
        return switch (this) {
            case abandon, archive, colourise, colorize, comehere, desktop, direction, door, eject, excite, exterminate, hide, home, sethome, lamps, make_her_blue, rebuild, rescue, room, setdest, theme, upgrade -> true;
            default -> false;
        };
    }
}
