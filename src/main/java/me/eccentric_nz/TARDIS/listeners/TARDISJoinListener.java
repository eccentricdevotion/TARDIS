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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISBook;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISResourcePackChanger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Tylos was a member of Varsh's group of Outlers on Alzarius. When Adric asked
 * to join them, Tylos challenged him to prove his worth by stealing some
 * riverfruit.
 *
 * @author eccentric_nz
 */
public class TARDISJoinListener implements Listener {

    private final TARDIS plugin;

    public TARDISJoinListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player joining the server. If the player has TARDIS
     * permissions (ie not a guest), then check whether they have achieved the
     * building of a TARDIS. If not then insert an achievement record and give
     * them the tardis book.
     *
     * @param event a player joining the server
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        String playerNameStr = player.getName();
        QueryFactory qf = new QueryFactory(plugin);
        if (plugin.getKitsConfig().getBoolean("give.join.enabled")) {
            if (player.hasPermission("tardis.kit.join")) {
                // check if they have the tardis kit
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("player", playerNameStr);
                where.put("name", "joinkit");
                ResultSetAchievements rsa = new ResultSetAchievements(plugin, where, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("player", playerNameStr);
                    set.put("name", "joinkit");
                    qf.doInsert("achievements", set);
                    // give the join kit
                    String kit = plugin.getKitsConfig().getString("give.join.kit");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisgive " + playerNameStr + " kit " + kit);
                }
            }
        }
        if (plugin.getConfig().getBoolean("allow.achievements")) {
            if (player.hasPermission("tardis.book")) {
                // check if they have started building a TARDIS yet
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("player", playerNameStr);
                where.put("name", "tardis");
                ResultSetAchievements rsa = new ResultSetAchievements(plugin, where, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("player", playerNameStr);
                    set.put("name", "tardis");
                    qf.doInsert("achievements", set);
                    TARDISBook book = new TARDISBook(plugin);
                    // title, author, filename, player
                    book.writeBook("Get transport", "Rassilon", "tardis", player);
                }
            }
        }
        if (plugin.getConfig().getBoolean("allow.tp_switch") && player.hasPermission("tardis.texture")) {
            // are they in the TARDIS?
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player", playerNameStr);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                // is texture switching on?
                HashMap<String, Object> wherep = new HashMap<String, Object>();
                wherep.put("player", playerNameStr);
                final ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                if (rsp.resultSet()) {
                    if (rsp.isTextureOn()) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureIn());
                            }
                        }, 50L);
                    }
                }
            }
        }
        // load and remember the players Police Box chunk
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("owner", playerNameStr);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherep, "", false);
        if (rs.resultSet()) {
            int id = rs.getTardis_id();
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (rsc.resultSet()) {
                World w = rsc.getWorld();
                if (w != null) {
                    Chunk chunk = w.getChunkAt(new Location(w, rsc.getX(), rsc.getY(), rsc.getZ()));
                    while (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    plugin.getGeneralKeeper().getTardisChunkList().add(chunk);
                }
            }
            long now;
            if (player.hasPermission("tardis.prune.bypass")) {
                now = Long.MAX_VALUE;
            } else {
                now = System.currentTimeMillis();
            }
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("lastuse", now);
            HashMap<String, Object> wherel = new HashMap<String, Object>();
            wherel.put("tardis_id", id);
            qf.doUpdate("tardis", set, wherel);
        }
        plugin.getFilter().addPlayer(player);
    }
}
