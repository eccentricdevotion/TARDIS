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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

@Deprecated
public class TARDISBiomeFinderOld implements Runnable {

    private final Integer[] directions = new Integer[]{0, 1, 2, 3};
    private final Player p;
    private final int startx;
    private final int startz;
    private final Biome b;
    private final World w;
    private int taskid, limite, limits, limitw, limitn, i = 0, plus = 0;
    private Location location = null;

    public TARDISBiomeFinderOld(TARDIS plugin, Player p, int startx, int startz, Biome b, World w) {
        this.p = p;
        this.startx = startx;
        this.startz = startz;
        this.b = b;
        this.w = w;
        Collections.shuffle(Arrays.asList(directions));
        limite = startx + 30000;
        limits = startz + 30000;
        limitw = startx - 30000;
        limitn = startz - 30000;
        if (plugin.getPM().isPluginEnabled("WorldBorder")) {
            // get the border limit for this world
            TARDISWorldBorderChecker wb = new TARDISWorldBorderChecker(plugin);
            int[] data = wb.getBorderDistance(w.getName());
            int minEW = Math.min(data[0], 30000);
            int minNS = Math.min(data[1], 30000);
            limite = minEW;
            limits = minNS;
            limitw = -minEW;
            limitn = -minNS;
        }
    }

    @Override
    public void run() {
        if (location == null) {
            switch (directions[i]) {
                case 0 -> {
                    // east
                    int east = startx + plus;
                    Biome chkbe = w.getBiome(east, w.getHighestBlockYAt(east, startz), startz);
                    if (chkbe.equals(b)) {
                        TARDISMessage.send(p, "BIOME_E", b.toString());
                        location = new Location(w, east, TARDISStaticLocationGetters.getHighestYIn3x3(w, east, startz), startz);
                    }
                    plus += 64;
                    if (east >= limite) {
                        i++;
                        plus = 0;
                    }
                }
                case 1 -> {
                    // south
                    int south = startz + plus;
                    Biome chkbs = w.getBiome(startx, w.getHighestBlockYAt(startx, south), south);
                    if (chkbs.equals(b)) {
                        TARDISMessage.send(p, "BIOME_S", b.toString());
                        location = new Location(w, startx, TARDISStaticLocationGetters.getHighestYIn3x3(w, startx, south), south);
                    }
                    plus += 64;
                    if (south >= limits) {
                        i++;
                        plus = 0;
                    }
                }
                case 2 -> {
                    // west
                    int west = startx - plus;
                    Biome chkbw = w.getBiome(west, w.getHighestBlockYAt(west, startz), startz);
                    if (chkbw.equals(b)) {
                        TARDISMessage.send(p, "BIOME_W", b.toString());
                        location = new Location(w, west, TARDISStaticLocationGetters.getHighestYIn3x3(w, west, startz), startz);
                    }
                    plus -= 64;
                    if (west <= limitw) {
                        i++;
                        plus = 0;
                    }
                }
                case 3 -> {
                    // north
                    int north = startz - plus;
                    Biome chkbn = w.getBiome(startx, w.getHighestBlockYAt(startx, north), north);
                    if (chkbn.equals(b)) {
                        TARDISMessage.send(p, "BIOME_N", b.toString());
                        location = new Location(w, startx, TARDISStaticLocationGetters.getHighestYIn3x3(w, startx, north), north);
                    }
                    plus -= 64;
                    if (north <= limitn) {
                        i++;
                        plus = 0;
                    }
                }
            }
        }
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public boolean poll() {
        return location != null;
    }

    public Location getLocation() {
        return location;
    }
}
