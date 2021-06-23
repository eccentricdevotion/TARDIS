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
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TardisWorldBorderChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

@Deprecated
public class TardisBiomeFinderOld implements Runnable {

    private final Integer[] directions = new Integer[]{0, 1, 2, 3};
    private final Player p;
    private final int startX;
    private final int startZ;
    private final Biome b;
    private final World w;
    private int taskId, limitE, limitS, limitW, limitN, i = 0, plus = 0;
    private Location location = null;

    public TardisBiomeFinderOld(TardisPlugin plugin, Player p, int startX, int startZ, Biome b, World w) {
        this.p = p;
        this.startX = startX;
        this.startZ = startZ;
        this.b = b;
        this.w = w;
        Collections.shuffle(Arrays.asList(directions));
        limitE = startX + 30000;
        limitS = startZ + 30000;
        limitW = startX - 30000;
        limitN = startZ - 30000;
        if (plugin.getPM().isPluginEnabled("WorldBorder")) {
            // get the border limit for this world
            TardisWorldBorderChecker wb = new TardisWorldBorderChecker(plugin);
            int[] data = wb.getBorderDistance(w.getName());
            int minEW = Math.min(data[0], 30000);
            int minNS = Math.min(data[1], 30000);
            limitE = minEW;
            limitS = minNS;
            limitW = -minEW;
            limitN = -minNS;
        }
    }

    @Override
    public void run() {
        if (location == null) {
            switch (directions[i]) {
                case 0 -> {
                    // east
                    int east = startX + plus;
                    Biome chkbe = w.getBiome(east, w.getHighestBlockYAt(east, startZ), startZ);
                    if (chkbe.equals(b)) {
                        TardisMessage.send(p, "BIOME_E", b.toString());
                        location = new Location(w, east, TardisStaticLocationGetters.getHighestYIn3x3(w, east, startZ), startZ);
                    }
                    plus += 64;
                    if (east >= limitE) {
                        i++;
                        plus = 0;
                    }
                }
                case 1 -> {
                    // south
                    int south = startZ + plus;
                    Biome chkbs = w.getBiome(startX, w.getHighestBlockYAt(startX, south), south);
                    if (chkbs.equals(b)) {
                        TardisMessage.send(p, "BIOME_S", b.toString());
                        location = new Location(w, startX, TardisStaticLocationGetters.getHighestYIn3x3(w, startX, south), south);
                    }
                    plus += 64;
                    if (south >= limitS) {
                        i++;
                        plus = 0;
                    }
                }
                case 2 -> {
                    // west
                    int west = startX - plus;
                    Biome chkbw = w.getBiome(west, w.getHighestBlockYAt(west, startZ), startZ);
                    if (chkbw.equals(b)) {
                        TardisMessage.send(p, "BIOME_W", b.toString());
                        location = new Location(w, west, TardisStaticLocationGetters.getHighestYIn3x3(w, west, startZ), startZ);
                    }
                    plus -= 64;
                    if (west <= limitW) {
                        i++;
                        plus = 0;
                    }
                }
                case 3 -> {
                    // north
                    int north = startZ - plus;
                    Biome chkbn = w.getBiome(startX, w.getHighestBlockYAt(startX, north), north);
                    if (chkbn.equals(b)) {
                        TardisMessage.send(p, "BIOME_N", b.toString());
                        location = new Location(w, startX, TardisStaticLocationGetters.getHighestYIn3x3(w, startX, north), north);
                    }
                    plus -= 64;
                    if (north <= limitN) {
                        i++;
                        plus = 0;
                    }
                }
            }
        }
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean poll() {
        return location != null;
    }

    public Location getLocation() {
        return location;
    }
}
