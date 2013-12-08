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
package me.eccentric_nz.TARDIS.commands.admin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import static me.eccentric_nz.TARDIS.destroyers.TARDISExterminator.deleteFolder;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDeleteCommand {

    private final TARDIS plugin;

    public TARDISDeleteCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean deleteTARDIS(CommandSender sender, String[] args) {
        // this should be run from the console if the player running it is the player to be deleted
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getName().equals(args[1])) {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("player", player.getName());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                if (rst.resultSet()) {
                    sender.sendMessage(plugin.pluginName + "You cannot be in your TARDIS when you delete it!");
                    return true;
                }
            }
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", args[1]);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int id = rs.getTardis_id();
            int tips = rs.getTIPS();
            TARDISConstants.SCHEMATIC schm = rs.getSchematic();
            String chunkLoc = rs.getChunk();
            String[] cdata = chunkLoc.split(":");
            String name = cdata[0];
            World cw = plugin.getServer().getWorld(name);
            int restore = getRestore(cw);
            // check if player is in the TARDIS
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("tardis_id", id);
            if (rst.resultSet()) {
                qf.doDelete("travellers", whered);
            }
            // get the current location
            Location bb_loc = null;
            TARDISConstants.COMPASS d = TARDISConstants.COMPASS.EAST;
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (rsc.resultSet()) {
                bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                d = rsc.getDirection();
            }
            if (bb_loc == null) {
                sender.sendMessage(plugin.pluginName + "Could not get the location of the TARDIS!");
                return true;
            }
            // destroy the TARDIS
            if ((plugin.getConfig().getBoolean("create_worlds") && !plugin.getConfig().getBoolean("default_world")) || name.contains("TARDIS_WORLD_")) {
                // delete TARDIS world
                List<Player> players = cw.getPlayers();
                for (Player p : players) {
                    p.kickPlayer("World scheduled for deletion!");
                }
                if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
                    plugin.getServer().dispatchCommand(plugin.console, "mv remove " + name);
                }
                if (plugin.pm.isPluginEnabled("MultiWorld")) {
                    plugin.getServer().dispatchCommand(plugin.console, "mw unload " + name);
                }
                if (plugin.pm.isPluginEnabled("WorldBorder")) {
                    // wb <world> clear
                    plugin.getServer().dispatchCommand(plugin.console, "wb " + name + " clear");
                }
                plugin.getServer().unloadWorld(cw, true);
                File world_folder = new File(plugin.getServer().getWorldContainer() + File.separator + name + File.separator);
                if (!deleteFolder(world_folder)) {
                    plugin.debug("Could not delete world <" + name + ">");
                }
            } else {
                plugin.destroyerI.destroyInner(schm, id, cw, restore, args[1], tips);
            }
            if (!rs.isHidden()) {
                plugin.destroyerP.destroyPreset(bb_loc, d, id, false, false, false, null);
            }
            // delete the TARDIS from the db
            HashMap<String, Object> wherec = new HashMap<String, Object>();
            wherec.put("tardis_id", id);
            qf.doDelete("chunks", wherec);
            HashMap<String, Object> wherea = new HashMap<String, Object>();
            wherea.put("tardis_id", id);
            qf.doDelete("tardis", wherea);
            HashMap<String, Object> whereo = new HashMap<String, Object>();
            whereo.put("tardis_id", id);
            qf.doDelete("doors", whereo);
            HashMap<String, Object> whereb = new HashMap<String, Object>();
            whereb.put("tardis_id", id);
            qf.doDelete("blocks", whereb);
            HashMap<String, Object> wherev = new HashMap<String, Object>();
            wherev.put("tardis_id", id);
            qf.doDelete("travellers", wherev);
            HashMap<String, Object> whereg = new HashMap<String, Object>();
            whereg.put("tardis_id", id);
            qf.doDelete("gravity_well", whereg);
            HashMap<String, Object> wheres = new HashMap<String, Object>();
            wheres.put("tardis_id", id);
            qf.doDelete("destinations", wheres);
            sender.sendMessage(plugin.pluginName + "The TARDIS was removed from the world and database successfully.");
        } else {
            sender.sendMessage(plugin.pluginName + "Could not find player [" + args[1] + "] in the database!");
            return true;
        }
        return true;
    }

    private int getRestore(World w) {
        World.Environment env = w.getEnvironment();
        if (w.getWorldType() == WorldType.FLAT || w.getName().equals("TARDIS_TimeVortex") || w.getGenerator() instanceof TARDISChunkGenerator) {
            return 0;
        }
        switch (env) {
            case NETHER:
                return 87;
            case THE_END:
                return 121;
            default:
                return 1;
        }
    }
}
