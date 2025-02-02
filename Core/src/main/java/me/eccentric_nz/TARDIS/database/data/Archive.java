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
package me.eccentric_nz.TARDIS.database.data;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class Archive {

    private final UUID uuid;
    private final String name;
    private final ConsoleSize consoleSize;
    private final boolean beacon;
    private final TardisLight light;
    private final int use;
    private final int y;
    private final JsonObject JSON;
    private final String description;

    public Archive(UUID uuid, String name, String consoleSize, boolean beacon, TardisLight light, int use, int y, JsonObject JSON, String description) {
        this.uuid = uuid;
        this.name = name;
        this.consoleSize = ConsoleSize.valueOf(consoleSize);
        this.beacon = beacon;
        this.light = light;
        this.use = use;
        this.y = y;
        this.JSON = JSON;
        this.description = description;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public ConsoleSize getConsoleSize() {
        return consoleSize;
    }

    public boolean isBeacon() {
        return beacon;
    }

    public TardisLight getLight() {
        return light;
    }

    public int getUse() {
        return use;
    }

    public int getY() {
        return y;
    }

    public JsonObject getJSON() {
        return JSON;
    }

    public String getDescription() {
        return description;
    }
}
