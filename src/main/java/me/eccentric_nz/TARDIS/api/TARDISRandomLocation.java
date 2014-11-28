/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.api;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRandomLocation {

    private final TARDIS plugin;
    private final Parameters param;
    private final Random random = new Random();

    public TARDISRandomLocation(TARDIS plugin, List<String> list, Parameters param) {
        this.plugin = plugin;
        this.param = param;
    }

    public Location getlocation() {
        return null;
    }

    public final List<World> getWorlds(List<String> list) {
        List<World> worlds = new ArrayList<World>();
        for (String s : list) {
            World o = Bukkit.getServer().getWorld(s);
            if (o != null) {
                worlds.add(o);
            }
        }
        return worlds;
    }

    public WorldAndRange getWorldandRange(List<World> worlds) {
        int listlen = worlds.size();
        World w;
        int minX;
        int maxX;
        int minZ;
        int maxZ;
        int rangeX;
        int rangeZ;
        // random world
        w = worlds.get(random.nextInt(listlen));
        World.Environment env = w.getEnvironment();
        if (param.limit() && param.getCentre() != null) {
            int cx = param.getRange();
            int cz = param.getRange();
            int[] centre = param.getCentre();
            minX = centre[0] - cx;
            maxX = centre[0] + cx;
            minZ = centre[1] - cz;
            maxZ = centre[1] - cz;
        } else {
            // get the limits of the world
            // is WorldBorder enabled?
            if (plugin.getPM().isPluginEnabled("WorldBorder")) {
                BorderData border = Config.Border(w.getName());
                minX = (int) border.getX() - border.getRadiusX();
                maxX = (int) border.getX() + border.getRadiusX();
                minZ = (int) border.getZ() - border.getRadiusZ();
                maxZ = (int) border.getZ() + border.getRadiusZ();
            } else {
                // use config
                int cx = plugin.getConfig().getInt("travel.random_circuit.x");
                int cz = plugin.getConfig().getInt("travel.random_circuit.z");
                minX = 0 - cx;
                maxX = cx;
                minZ = 0 - cz;
                maxZ = cz;
            }
            // compensate for nether 1:8 ratio if necessary
            if (env.equals(World.Environment.NETHER)) {
                minX /= 8;
                maxX /= 8;
                minZ /= 8;
                maxZ /= 8;
            }
        }
        // just set the end values
        if (env.equals(World.Environment.THE_END)) {
            minX = -120;
            maxX = 120;
            minZ = -120;
            maxZ = 120;
        }
        // get ranges
        rangeX = Math.abs(minX) + maxX;
        rangeZ = Math.abs(minZ) + maxZ;
        // WorldAndRange(World w, int minX, int minZ,  int rangeX, int rangeZ)
        return new WorldAndRange(w, minX, minZ, rangeX, rangeZ);
    }

    public boolean checkLocation(Location l) {
        return true;
    }
}
