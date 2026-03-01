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

/**
 * @author eccentric_nz
 */
public enum TardisCommand {

    abandon,
    abort,
    add,
    arch_time,
    archive,
    ars_remove,
    bell,
    check_loc,
    colorize,
    colourise,
    come_here,
    construct,
    cube,
    decommission,
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
    inside,
    item,
    jettison,
    lamps,
    list,
    make_her_blue,
    monsters,
    name_key,
    occupy,
    rebuild,
    remove,
    remove_save,
    rename_save,
    reorder_save,
    rescue,
    room,
    save,
    save_player,
    save_icon,
    secondary,
    section,
    set_destination,
    set_home,
    stop_sound,
    tag_the_ood,
    theme,
    transmat,
    update,
    upgrade,
    version;

    public boolean noSiege() {
        return switch (this) {
            case abandon, archive, colourise, colorize, come_here, desktop, direction, door, eject, excite, exterminate, hide, set_home, lamps, make_her_blue, rebuild, rescue, room, set_destination, theme, upgrade -> true;
            default -> false;
        };
    }
}
