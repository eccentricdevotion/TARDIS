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
package me.eccentric_nz.tardis.enumeration;

/**
 * @author eccentric_nz
 */
public enum TardisCommand {

    ABANDON,
    ABORT,
    ADD,
    ARCH_TIME,
    ARCHIVE,
    ARS_REMOVE,
    BELL,
    CHECK_LOC,
    COLOURISE,
    COLORIZE,
    COME_HERE,
    CONSTRUCT,
    CUBE,
    DESKTOP,
    DIRECTION,
    DOOR,
    EGG,
    EJECT,
    EP1,
    ERASE,
    EXCITE,
    EXTERMINATE,
    FIND,
    HANDBRAKE,
    HELP,
    HIDE,
    HOME,
    INSIDE,
    ITEM,
    JETTISON,
    LAMPS,
    LIST,
    MAKE_HER_BLUE,
    NAMEKEY,
    OCCUPY,
    REBUILD,
    REMOVE,
    REMOVESAVE,
    RENAMESAVE,
    REORDERSAVE,
    RESCUE,
    ROOM,
    SAVE,
    SAVEICON,
    SAVE_PLAYER,
    SECONDARY,
    SECTION,
    SETDEST,
    SETHOME,
    TAGTHEOOD,
    THEME,
    TRANSMAT,
    UPDATE,
    UPGRADE,
    VERSION;

    public boolean noSiege() {
        return switch (this) {
            case ABANDON, ARCHIVE, COLOURISE, COLORIZE, COME_HERE, DESKTOP, DIRECTION, DOOR, EJECT, EXCITE, EXTERMINATE, HIDE, HOME, SETHOME, LAMPS, MAKE_HER_BLUE, REBUILD, RESCUE, ROOM, SETDEST, THEME, UPGRADE -> true;
            default -> false;
        };
    }
}
