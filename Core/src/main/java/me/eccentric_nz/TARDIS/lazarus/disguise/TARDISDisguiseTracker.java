/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.lazarus.disguise;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TARDISDisguiseTracker {

    public static final Set<UUID> DISGUISED_AS_PLAYER = new HashSet<>();
    public static final HashMap<Integer, UUID> DISGUISED_NPCS = new HashMap<>();
    public static final HashMap<UUID, TARDISDisguise> DISGUISED_ARMOR_STANDS = new HashMap<>();
    public static final HashMap<UUID, TARDISDisguise> DISGUISED_AS_MOB = new HashMap<>();
    public static final HashMap<UUID, String> ARCHED = new HashMap<>();
}
