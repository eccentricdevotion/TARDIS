/*
 * Copyright (C) 2019 eccentric_nz
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public enum FLAG {

    MESSAGE_PLAYER,
    PERMS_AREA,
    PERMS_NETHER,
    PERMS_THEEND,
    PERMS_WORLD,
    RESPECT_FACTIONS,
    RESPECT_GRIEFPREVENTION,
    RESPECT_TOWNY,
    RESPECT_WORLDBORDER,
    RESPECT_WORLDGUARD,
    SPACE_TARDIS;

    public static List<FLAG> getNoMessageFlags() {
        List<FLAG> flags = new ArrayList<>(Arrays.asList(FLAG.values()));
        flags.remove(FLAG.MESSAGE_PLAYER);
        return flags;
    }

    public static List<FLAG> getAPIFlags() {
        List<FLAG> flags = new ArrayList<>(Arrays.asList(FLAG.values()));
        flags.remove(FLAG.MESSAGE_PLAYER);
        flags.remove(FLAG.SPACE_TARDIS);
        return flags;
    }

    public static List<FLAG> getDefaultFlags() {
        return Arrays.asList(FLAG.values());
    }
}
