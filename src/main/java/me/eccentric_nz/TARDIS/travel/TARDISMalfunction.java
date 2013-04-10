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
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMalfunction {

    private final TARDIS plugin;
    private int id;
    private Player p;
    private TARDISConstants.COMPASS dir;
    private Random rand;

    public TARDISMalfunction(TARDIS plugin, int id, Player p, TARDISConstants.COMPASS dir) {
        this.plugin = plugin;
        this.id = id;
        this.p = p;
        this.dir = dir;
        this.rand = new Random();
    }

    public boolean isMalfunction() {
        boolean mal = false;
        if (plugin.getConfig().getInt("malfunction") > 0) {
            int chance = 100 - plugin.getConfig().getInt("malfunction");
            if (rand.nextInt(100) > chance) {
                mal = true;
            }
        }
        return mal;
    }

    public Location getMalfunction() {
        Location l;
        int end = 100 - plugin.getConfig().getInt("malfunction_end");
        int nether = end - plugin.getConfig().getInt("malfunction_nether");
        int r = rand.nextInt(100);
        TARDISTimetravel tt = new TARDISTimetravel(plugin);
        byte x = (byte) 2;
        if (r > end) {
            // get random the_end location
            l = tt.randomDestination(p, x, x, x, dir, "THE_END");
        } else if (r > nether) {
            // get random nether location
            l = tt.randomDestination(p, x, x, x, dir, "NETHER");
        } else {
            // get random normal location
            l = tt.randomDestination(p, x, x, x, dir, "NORMAL");
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
            final long start = System.currentTimeMillis() + 5000;
            TARDISLampsRunnable runnable = new TARDISLampsRunnable(plugin, lamps, start);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 1L, 5L);
            runnable.setTask(taskID);
            // play tardis crash sound
            if (plugin.pm.getPlugin("Spout") != null && SpoutManager.getPlayer(p).isSpoutCraftEnabled()) {
                SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropbox.com/u/53758864/tardis_emergency_land.mp3", false, l, 20, 75);
            } else {
                try {
                    Class.forName("org.bukkit.Sound");
                    l.getWorld().playSound(l, Sound.MINECART_INSIDE, 1, 0);
                } catch (ClassNotFoundException e) {
                    l.getWorld().playEffect(l, Effect.BLAZE_SHOOT, 0);
                }
            }
        }
    }
}
