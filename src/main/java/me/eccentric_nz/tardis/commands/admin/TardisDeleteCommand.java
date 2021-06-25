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
package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisDestructionEvent;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.files.TardisBlockLoader;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

import static me.eccentric_nz.tardis.destroyers.TardisExterminator.deleteFolder;

/**
 * @author eccentric_nz
 */
public class TardisDeleteCommand {

    private final TardisPlugin plugin;

    TardisDeleteCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public static void cleanDatabase(int id) {
        TardisBlockLoader bl = new TardisBlockLoader(TardisPlugin.plugin);
        bl.unloadProtectedBlocks(id);
        List<String> tables = Arrays.asList("ars", "back", "blocks", "chunks", "controls", "current", "destinations", "doors", "gravity_well", "homes", "junk", "lamps", "next", "tardis", "thevoid", "travellers", "vaults");
        // remove record from database tables
        tables.forEach((table) -> {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            TardisPlugin.plugin.getQueryFactory().doDelete(table, where);
        });
    }

    boolean deleteTardis(CommandSender sender, String[] args) {
        boolean junk = (args[1].toLowerCase(Locale.ENGLISH).equals("junk"));
        int tmp = -1;
        int abandoned = (args.length > 2 && args[2].equals("abandoned")) ? 1 : 0;
        try {
            tmp = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            // do nothing
        }
        HashMap<String, Object> where = new HashMap<>();
        Player player = null;
        if (tmp == -1) {
            // this should be run from the console if the player running it is the player to be deleted
            if (sender instanceof Player) {
                player = (Player) sender;
                if (player.getName().equals(args[1])) {
                    HashMap<String, Object> wherep = new HashMap<>();
                    wherep.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
                    if (rst.resultSet()) {
                        TardisMessage.send(sender, "TARDIS_DELETE_NO");
                        return true;
                    }
                }
            }
            // Look up this player's UUID
            UUID uuid;
            if (junk) {
                uuid = UUID.fromString("00000000-aaaa-bbbb-cccc-000000000000");
            } else {
                uuid = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
            }
            where.put("uuid", uuid.toString());
        } else {
            where.put("tardis_id", tmp);
        }
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, abandoned);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            int tips = tardis.getTips();
            Schematic schm = tardis.getSchematic();
            String chunkLoc = tardis.getChunk();
            boolean hidden = tardis.isHidden();
            String[] cdata = chunkLoc.split(":");
            String wname;
            if (junk) {
                wname = plugin.getConfig().getString("creation.default_world_name");
            } else {
                wname = cdata[0];
            }
            World cw = TardisAliasResolver.getWorldFromAlias(wname);
            if (cw == null) {
                TardisMessage.send(sender, "WORLD_DELETED");
                return true;
            }
            // get the current location
            Location bb_loc = null;
            CardinalDirection d = CardinalDirection.EAST;
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (rsc.resultSet()) {
                bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                d = rsc.getDirection();
            }
            if (bb_loc == null) {
                TardisMessage.send(sender, "CURRENT_NOT_FOUND");
                return true;
            }
            plugin.getPluginManager().callEvent(new TardisDestructionEvent(player, bb_loc, tardis.getOwner()));
            // destroy outer TARDIS
            if (!hidden) {
                UUID u = rs.getTardis().getUuid();
                DestroyData dd = new DestroyData();
                dd.setDirection(d);
                dd.setLocation(bb_loc);
                dd.setPlayer(plugin.getServer().getOfflinePlayer(u));
                dd.setHide(true);
                dd.setOutside(false);
                dd.setSubmarine(rsc.isSubmarine());
                dd.setTardisId(id);
                dd.setThrottle(SpaceTimeThrottle.REBUILD);
                plugin.getPresetDestroyer().destroyPreset(dd);
            }
            // destroy the inner TARDIS
            // give the TARDIS time to remove itself as it's not hidden
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if ((plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) || Objects.requireNonNull(wname).contains("TARDIS_WORLD_")) {
                    // delete TARDIS world
                    List<Player> players = cw.getPlayers();
                    players.forEach((p) -> p.kickPlayer("World scheduled for deletion!"));
                    if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "mv remove " + wname);
                    }
                    if (plugin.getWorldManager().equals(WorldManager.MULTIWORLD)) {
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "mw unload " + wname);
                    }
                    if (plugin.getWorldManager().equals(WorldManager.MYWORLDS)) {
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds unload " + wname);
                    }
                    if (plugin.getPluginManager().isPluginEnabled("WorldBorder")) {
                        // wb <world> clear
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "wb " + wname + " clear");
                    }
                    plugin.getServer().unloadWorld(cw, true);
                    File world_folder = new File(plugin.getServer().getWorldContainer() + File.separator + wname + File.separator);
                    if (!deleteFolder(world_folder)) {
                        plugin.debug("Could not delete world <" + wname + ">");
                    }
                } else {
                    plugin.getInteriorDestroyer().destroyInner(schm, id, cw, tips);
                }
                cleanDatabase(id);
                TardisMessage.send(sender, "TARDIS_EXTERMINATED");
            }, 40L);
        } else {
            TardisMessage.send(sender, "PLAYER_NOT_FOUND_DB", args[1]);
            return true;
        }
        return true;
    }
}
