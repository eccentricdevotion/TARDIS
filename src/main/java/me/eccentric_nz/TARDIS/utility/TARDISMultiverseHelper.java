/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMultiverseHelper {

    private final MultiverseCore mvc;

    public TARDISMultiverseHelper(Plugin mvplugin) {
        this.mvc = (MultiverseCore) mvplugin;
    }

    public String getAlias(World world) {
        MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(world);
        return mvw.getAlias();
    }

    public String getAlias(String world) {
        MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(world);
        return mvw.getAlias();
    }

    public void setSpawnLocation(World world, int x, int y, int z) {
        MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(world.getName());
        Location spawn = new Location(world, (x + 0.5), y, (z + 1.5), 0, 0);
        mvw.setSpawnLocation(spawn);
    }

    public boolean isWorldSurvival(World world) {
        MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(world);
        GameMode gm = mvw.getGameMode();
        return (gm.equals(GameMode.SURVIVAL));
    }
}
