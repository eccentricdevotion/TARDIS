/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.artron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISArtronRunnable implements Runnable {

    private final TARDIS plugin;
    private int id;
    private Player p;
    private int task;
    List<Location> rechargers;
    QueryFactory qf;

    public TARDISArtronRunnable(TARDIS plugin, int id, Player p) {
        this.plugin = plugin;
        this.id = id;
        this.p = p;
        this.rechargers = getRechargers();
        this.qf = new QueryFactory(plugin);
    }

    /**
     * A runnable task that recharges the TARDIS.
     */
    @Override
    public void run() {
        int level = isFull(id);
        if (!isNearCharger(id) || level > 999) {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            plugin.trackRecharge.remove(Integer.valueOf(id));
        }
        // update TARDIS artron_level
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        qf.alterEnergyLevel("tardis", 10, where, p);
    }

    /**
     * Checks whether the TARDIS is near a recharge location.
     */
    private boolean isNearCharger(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return false;
        }
        // get Police Box location
        String save = rs.getSave();
        String[] data = save.split(":");
        World w = plugin.getServer().getWorld(data[0]);
        int x = plugin.utils.parseNum(data[1]);
        int y = plugin.utils.parseNum(data[2]);
        int z = plugin.utils.parseNum(data[3]);
        Location pb_loc = new Location(w, x, y, z);
        // check location is within 10 blocks of a recharger
        for (Location l : rechargers) {
            if (plugin.utils.compareLocations(pb_loc, l)) {
                // strike lightning to the Police Box torch location
                pb_loc.setY(pb_loc.getY() + 3);
                w.strikeLightningEffect(pb_loc);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a list of recharge locations from the config.
     */
    private List<Location> getRechargers() {
        List<Location> list = new ArrayList<Location>();
        if (plugin.getConfig().isConfigurationSection("rechargers")) {
            Set<String> therechargers = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
            for (String s : therechargers) {
                World w = plugin.getServer().getWorld(plugin.getConfig().getString("rechargers." + s + ".world"));
                int x = plugin.getConfig().getInt("rechargers." + s + ".x");
                int y = plugin.getConfig().getInt("rechargers." + s + ".y");
                int z = plugin.getConfig().getInt("rechargers." + s + ".z");
                Location rc_loc = new Location(w, x, y, z);
                list.add(rc_loc);
            }
        }
        return list;
    }

    /**
     * Gets the current Artron Energy Level for the specified TARDIS.
     */
    private int isFull(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        rs.resultSet();
        return rs.getArtron_level();
    }

    public void setTask(int task) {
        this.task = task;
    }
}