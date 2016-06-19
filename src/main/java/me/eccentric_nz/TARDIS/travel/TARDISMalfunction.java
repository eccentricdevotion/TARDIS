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
package me.eccentric_nz.TARDIS.travel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private final Random rand;

    public TARDISMalfunction(TARDIS plugin) {
        this.plugin = plugin;
        this.rand = new Random();
    }

    public boolean isMalfunction() {
        boolean mal = false;
        if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
            int chance = 100 - plugin.getConfig().getInt("preferences.malfunction");
            if (rand.nextInt(100) > chance) {
                mal = true;
            }
        }
        return mal;
    }

    public Location getMalfunction(int id, Player p, COMPASS dir, Location handbrake_loc, String eps, String creeper) {
        Location l;
        // get cuurent TARDIS preset location
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
        if (rscl.resultSet()) {
            Location cl = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
            int end = 100 - plugin.getConfig().getInt("preferences.malfunction_end");
            int nether = end - plugin.getConfig().getInt("preferences.malfunction_nether");
            int r = rand.nextInt(100);
            TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
            byte x = (byte) rand.nextInt(15);
            byte z = (byte) rand.nextInt(15);
            byte y = (byte) rand.nextInt(15);
            if (r > end) {
                // get random the_end location
                l = tt.randomDestination(p, x, z, y, dir, "THE_END", null, true, cl);
            } else if (r > nether) {
                // get random nether location
                l = tt.randomDestination(p, x, z, y, dir, "NETHER", null, true, cl);
            } else {
                // get random normal location
                l = tt.randomDestination(p, x, z, y, dir, "NORMAL", null, false, cl);
            }
        } else {
            l = null;
        }
        if (l != null) {
            doMalfunction(l, id, p, eps, creeper, handbrake_loc);
        }
        return l;
    }

    public void doMalfunction(Location l, int id, Player p, String eps, String creeper, Location handbrake) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetLamps rsl = new ResultSetLamps(plugin, where, true);
        if (rsl.resultSet()) {
            // get player prefs
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("uuid", p.getUniqueId().toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
            if (rsp.resultSet()) {
                if (plugin.getPM().isPluginEnabled("Citizens") && plugin.getConfig().getBoolean("allow.emergency_npc") && rsp.isEpsOn()) {
                    // schedule the NPC to appear
                    String message = "This is Emergency Programme One. Now listen, this is important. If this message is activated, then it can only mean one thing: we must be in danger, and I mean fatal. You're about to die any second with no chance of escape.";
                    HashMap<String, Object> wherev = new HashMap<String, Object>();
                    wherev.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                    List<UUID> playerUUIDs;
                    if (rst.resultSet()) {
                        playerUUIDs = rst.getData();
                    } else {
                        playerUUIDs = new ArrayList<UUID>();
                        playerUUIDs.add(p.getUniqueId());
                    }
                    TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, message, p, playerUUIDs, id, eps, creeper);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 220L);
                }
                Material light = (rsp.isLanternsOn()) ? Material.SEA_LANTERN : Material.REDSTONE_LAMP_ON;
                // flicker lights
                final long start = System.currentTimeMillis() + 10000;
                TARDISLampsRunnable runnable = new TARDISLampsRunnable(plugin, rsl.getData(), start, light, rsp.isWoolLightsOn());
                runnable.setHandbrake(handbrake);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 10L);
                runnable.setTask(taskID);
            }
        }
    }
}
