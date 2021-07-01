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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advancement.TardisBook;
import me.eccentric_nz.tardis.arch.TardisArchPersister;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisResourcePackChanger;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Objects;

/**
 * Tylos was a member of Varsh's group of Outlers on Alzarius. When Adric asked to join them, Tylos challenged him to
 * prove his worth by stealing some riverfruit.
 *
 * @author eccentric_nz
 */
public class TardisJoinListener implements Listener {

    private final TardisPlugin plugin;

    public TardisJoinListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player joining the server. If the player has TARDIS permissions (ie not a guest), then check
     * whether they have achieved the building of a TARDIS. If not then insert an achievement record and give them the
     * tardis book.
     *
     * @param event a player joining the server
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (plugin.getKitsConfig().getBoolean("give.join.enabled")) {
            if (TardisPermission.hasPermission(player, "tardis.kit.join")) {
                // check if they have the tardis kit
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                where.put("name", "joinkit");
                ResultSetAdvancements rsa = new ResultSetAdvancements(plugin, where, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", uuid);
                    set.put("name", "joinkit");
                    plugin.getQueryFactory().doInsert("achievements", set);
                    // give the join kit
                    String kit = plugin.getKitsConfig().getString("give.join.kit");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisgive " + player.getName() + " kit " + kit);
                }
            }
        }
        if (plugin.getConfig().getBoolean("allow.achievements")) {
            if (TardisPermission.hasPermission(player, "tardis.book")) {
                // check if they have started building a TARDIS yet
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                where.put("name", "tardis");
                ResultSetAdvancements rsa = new ResultSetAdvancements(plugin, where, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", uuid);
                    set.put("name", "tardis");
                    plugin.getQueryFactory().doInsert("achievements", set);
                    TardisBook book = new TardisBook(plugin);
                    // title, author, filename, player
                    book.writeBook("Get transport", "Rassilon", "tardis", player);
                }
            }
        }
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && ((plugin.getConfig().getBoolean("allow.player_difficulty") && TardisPermission.hasPermission(player, "tardis.difficulty")) || (plugin.getConfig().getInt("travel.grace_period") > 0 && TardisPermission.hasPermission(player, "tardis.create")))) {
            // check if they have t_count record - create one if not
            ResultSetCount rsc = new ResultSetCount(plugin, uuid);
            if (!rsc.resultSet()) {
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("uuid", uuid);
                setc.put("player", player.getName());
                plugin.getQueryFactory().doInsert("t_count", setc);
            }
        }
        // are they in the TARDIS?
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (rst.resultSet()) {
            if (plugin.getConfig().getBoolean("allow.tp_switch") && TardisPermission.hasPermission(player, "tardis.texture")) {
                // is texture switching on?
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                if (rsp.resultSet()) {
                    if (rsp.isTextureOn()) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisResourcePackChanger(plugin).changeResourcePack(player, rsp.getTextureIn()), 50L);
                    }
                }
            }
            if (plugin.getConfig().getBoolean("creation.keep_night")) {
                player.setPlayerTime(18000, false);
            }
        }
        // load and remember the players Police Box chunk
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherep, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            String owner = tardis.getOwner();
            String last_known_name = tardis.getLastKnownName();
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (rsc.resultSet()) {
                World w = rsc.getWorld();
                if (w != null) {
                    Chunk chunk = w.getChunkAt(new Location(w, rsc.getX(), rsc.getY(), rsc.getZ()));
                    while (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    chunk.setForceLoaded(true);
                }
            }
            long now;
            if (TardisPermission.hasPermission(player, "tardis.prune.bypass")) {
                now = Long.MAX_VALUE;
            } else {
                now = System.currentTimeMillis();
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("lastuse", now);
            set.put("monsters", 0);
            if (!last_known_name.equals(player.getName())) {
                // update the player's name WG region as it may have changed
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    World cw = TardisStaticLocationGetters.getWorld(tardis.getChunk());
                    // tardis region
                    plugin.getWorldGuardUtils().updateRegionForNameChange(cw, owner, player.getUniqueId(), "tardis");
                }
                set.put("last_known_name", player.getName());
            }
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", set, wherel);
        }
        // re-arch the player
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
            new TardisArchPersister(plugin).reArch(player.getUniqueId());
        }
        // add to perception filter team
        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
            plugin.getFilter().addPlayer(player);
        }
        // add to zero room occupants
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            if (Objects.requireNonNull(player.getLocation().getWorld()).getName().equals("TARDIS_Zero_Room")) {
                plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
            }
        }
        // teleport players that rejoined after logging out while in Junk TARDIS
        if (plugin.getTrackerKeeper().getJunkRelog().containsKey(player.getUniqueId())) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.teleport(plugin.getTrackerKeeper().getJunkRelog().get(player.getUniqueId())), 2L);
        }
        // notify updates
        if (plugin.getConfig().getBoolean("preferences.notify_update") && plugin.isUpdateFound() && player.isOp()) {
            TardisMessage.message(player, String.format(TardisMessage.JENKINS_UPDATE_READY, plugin.getBuildNumber(), plugin.getUpdateNumber()));
        }
    }
}