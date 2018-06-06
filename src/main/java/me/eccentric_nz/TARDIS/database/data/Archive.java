/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class Archive {

    private final int archive_id;
    private final UUID uuid;
    private final String name;
    private final ConsoleSize consoleSize;
    private final boolean beacon;
    private final boolean lanterns;
    private final int use;
    private final JSONObject JSON;
    private final String description;

    public Archive(int archive_id, UUID uuid, String name, String consoleSize, boolean beacon, boolean lanterns, int use, JSONObject JSON, String description) {
        this.archive_id = archive_id;
        this.uuid = uuid;
        this.name = name;
        this.consoleSize = ConsoleSize.valueOf(consoleSize);
        this.beacon = beacon;
        this.lanterns = lanterns;
        this.use = use;
        this.JSON = JSON;
        this.description = description;
    }

    public int getArchive_id() {
        return archive_id;
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

    public boolean isLanterns() {
        return lanterns;
    }

    public int getUse() {
        return use;
    }

    public JSONObject getJSON() {
        return JSON;
    }

    public String getDescription() {
        return description;
    }
}
