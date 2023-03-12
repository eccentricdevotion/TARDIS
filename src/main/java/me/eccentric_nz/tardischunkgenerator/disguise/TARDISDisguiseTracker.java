/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardischunkgenerator.disguise;

import com.mojang.authlib.properties.PropertyMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISDisguiseTracker {

    public static final List<UUID> DISGUISED_AS_PLAYER = new ArrayList<>();
    public static final HashMap<Integer, UUID> DISGUISED_NPCS = new HashMap<>();
    public static final HashMap<UUID, TARDISDisguise> DISGUISED_ARMOR_STANDS = new HashMap<>();
    public static final HashMap<UUID, TARDISDisguise> DISGUISED_AS_MOB = new HashMap<>();
    public static final HashMap<UUID, ProfileData> ARCHED = new HashMap<>();

    public static class ProfileData {

        PropertyMap properties;
        String name;

        public ProfileData(PropertyMap properties, String name) {
            this.properties = properties;
            this.name = name;
        }

        public PropertyMap getProperties() {
            return properties;
        }

        public String getName() {
            return name;
        }
    }
}
