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
package me.eccentric_nz.tardischunkgenerator.helpers;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;

public class TARDISPlanetData {

    private final GameMode gameMode;
    private final World.Environment environment;
    private final WorldType worldType;

    public TARDISPlanetData(GameMode gameMode, World.Environment environment, WorldType worldType) {
        this.gameMode = gameMode;
        this.environment = environment;
        this.worldType = worldType;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public WorldType getWorldType() {
        return worldType;
    }
}
