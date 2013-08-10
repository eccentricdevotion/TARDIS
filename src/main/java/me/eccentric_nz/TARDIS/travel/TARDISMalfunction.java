/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * The Dalek Asylum was a snowy and mountainous planet used by the Daleks as a
 * prison and "dumping ground" for those among them who had malfunctioned, gone
 * insane and/or become mentally scarred by battles. The sane Daleks left their
 * insane fellows in the Asylum rather than kill them because they epitomised
 * the Dalek concept of beauty: pure hatred.
 *
 * @author eccentric_nz
 */
public class TARDISMalfunction {

    private final TARDIS plugin;
    private int id;
    private Player p;
    private TARDISConstants.COMPASS dir;
    private Location handbrake_loc;
    private String eps;
    private String creeper;
    private Random rand;

    public TARDISMalfunction(TARDIS plugin, int id, Player p, TARDISConstants.COMPASS dir, Location handbrake_loc, String eps, String creeper) {
        this.plugin = plugin;
        this.id = id;
        this.p = p;
        this.dir = dir;
        this.handbrake_loc = handbrake_loc;
        this.eps = eps;
        this.creeper = creeper;
        this.rand = new Random();
    }

    public boolean isMalfunction() {
        boolean mal = false;
        if (plugin.getConfig().getInt("malfunction") > 0) {
            int chance = 100 - plugin.getConfig().getInt("malfunction");
            if (rand.nextInt(100) > chance) {
                mal = true;
                if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                    plugin.trackRescue.remove(Integer.valueOf(id));
                }
            }
        }
        return mal;
    }

    public Location getMalfunction() {
        Location l;
        int end = 100 - plugin.getConfig().getInt("malfunction_end");
        int nether = end - plugin.getConfig().getInt("malfunction_nether");
        int r = rand.nextInt(100);
        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
        byte x = (byte) 2;
        if (r > end) {
            // get random the_end location
            l = tt.randomDestination(p, x, x, x, dir, "THE_END", null, true);
        } else if (r > nether) {
            // get random nether location
            l = tt.randomDestination(p, x, x, x, dir, "NETHER", null, true);
        } else {
            // get random normal location
            l = tt.randomDestination(p, x, x, x, dir, "NORMAL", null, false);
        }
        if (l != null) {
            doMalfunction(l);
        }
        return l;
    }

    public void doMalfunction(Location l) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetLamps rsl = new ResultSetLamps(plugin, where, true);
        List<Block> lamps = new ArrayList<Block>();
        if (rsl.resultSet()) {
            // flicker lights
            ArrayList<HashMap<String, String>> data = rsl.getData();
            for (HashMap<String, String> map : data) {
                Location loc = plugin.utils.getLocationFromDB(map.get("location"), 0.0F, 0.0F);
                lamps.add(loc.getBlock());
            }
            if (plugin.pm.isPluginEnabled("Citizens") && plugin.getConfig().getBoolean("emergency_npc")) {
                // get player prefs
                HashMap<String, Object> wherep = new HashMap<String, Object>();
                wherep.put("player", p.getName());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                if (rsp.resultSet() && rsp.isEPS_on()) {
                    // schedule the NPC to appear
                    String message = "This is Emergency Programme One. Now listen, this is important. If this message is activated, then it can only mean one thing: we must be in danger, and I mean fatal. You're about to die any second with no chance of escape.";
                    HashMap<String, Object> wherev = new HashMap<String, Object>();
                    wherev.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                    List<String> players;
                    if (rst.resultSet()) {
                        players = rst.getData();
                    } else {
                        players = new ArrayList<String>();
                        players.add(p.getName());
                    }
                    TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, message, p, players, id, eps, creeper);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 220L);
                }
            }
            final long start = System.currentTimeMillis() + 10000;
            TARDISLampsRunnable runnable = new TARDISLampsRunnable(plugin, lamps, start);
            runnable.setHandbrake(handbrake_loc);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 10L);
            runnable.setTask(taskID);
        }
    }
}
