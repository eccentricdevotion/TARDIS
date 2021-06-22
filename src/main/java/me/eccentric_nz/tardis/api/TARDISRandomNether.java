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
package me.eccentric_nz.tardis.api;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.travel.TardisTimeTravel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TardisRandomNether extends TardisRandomLocation {

    private final TardisPlugin plugin;
    private final Parameters param;
    private final List<World> worlds;

    TardisRandomNether(TardisPlugin plugin, List<String> list, Parameters param) {
        super(plugin);
        worlds = getWorlds(list);
        this.plugin = plugin;
        this.param = param;
    }

    @Override
    public Location getLocation() {
        WorldAndRange war = getWorldAndRange(worlds);
        // loop till random attempts limit reached
        for (int n = 0; n < plugin.getConfig().getInt("travel.random_attempts"); n++) {
            // get random values in range
            int randX = TardisConstants.RANDOM.nextInt(war.getRangeX());
            int randZ = TardisConstants.RANDOM.nextInt(war.getRangeZ());
            // get the x coord
            int x = war.getMinX() + randX;
            // get the z coord
            int z = war.getMinZ() + randZ;
            int startx, starty, startz, resetx, resetz, count;
            int wherey = 100;
            Block startBlock = war.getW().getBlockAt(x, wherey, z);
            while (!startBlock.getChunk().isLoaded()) {
                startBlock.getChunk().load();
            }
            while (!startBlock.getType().isAir()) {
                startBlock = startBlock.getRelative(BlockFace.DOWN);
            }
            int air = 0;
            while (startBlock.getType().isAir() && startBlock.getLocation().getBlockY() > 30) {
                startBlock = startBlock.getRelative(BlockFace.DOWN);
                air++;
            }
            Material mat = startBlock.getType();
            if (plugin.getGeneralKeeper().getGoodNether().contains(mat) && air >= 4) {
                Location dest = startBlock.getLocation();
                int netherLocY = dest.getBlockY();
                dest.setY(netherLocY + 1);
                if (param.spaceTardis()) {
                    if (plugin.getPluginRespect().getRespect(dest, param)) {
                        // get start location for checking there is enough space
                        int[] gsl = TardisTimeTravel.getStartLocation(dest, param.getCompass());
                        startx = gsl[0];
                        resetx = gsl[1];
                        starty = dest.getBlockY();
                        startz = gsl[2];
                        resetz = gsl[3];
                        count = TardisTimeTravel.safeLocation(startx, starty, startz, resetx, resetz, war.getW(), param.getCompass());
                    } else {
                        count = 1;
                    }
                    if (count == 0) {
                        return dest;
                    }
                } else {
                    // space for a player / check plugin respect
                    if (plugin.getPluginRespect().getRespect(dest, param)) {
                        return dest;
                    }
                }
            }
        }
        return null;
    }
}
