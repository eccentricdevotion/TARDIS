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
package me.eccentric_nz.TARDIS.commands.admin;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import static me.eccentric_nz.TARDIS.destroyers.TARDISExterminator.deleteFolder;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
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

    @SuppressWarnings("deprecation")
    public boolean deleteTARDIS(final CommandSender sender, final String[] args) {
        // this should be run from the console if the player running it is the player to be deleted
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getName().equals(args[1])) {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                if (rst.resultSet()) {
                    TARDISMessage.send(sender, "TARDIS_DELETE_NO");
                    return true;
                }
            }
        }
        // Look up this player's UUID
        UUID uuid = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
        if (uuid == null) {
            uuid = plugin.getGeneralKeeper().getUUIDCache().getIdOptimistic(args[1]);
            plugin.getGeneralKeeper().getUUIDCache().getId(args[1]);
        }
        if (uuid != null) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                final int id = rs.getTardis_id();
                final int tips = rs.getTIPS();
                final SCHEMATIC schm = rs.getSchematic();
                String chunkLoc = rs.getChunk();
                boolean hidden = rs.isHidden();
                String[] cdata = chunkLoc.split(":");
                final String name = cdata[0];
                final World cw = plugin.getServer().getWorld(name);
                if (cw == null) {
                    TARDISMessage.send(sender, "WORLD_DELETED");
                    return true;
                }
                final int restore = getRestore(cw);
                // get the current location
                Location bb_loc = null;
                COMPASS d = COMPASS.EAST;
                Biome biome = null;
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                    d = rsc.getDirection();
                    biome = rsc.getBiome();
                }
                if (bb_loc == null) {
                    TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
                    return true;
                }
                // destroy outer TARDIS
                if (!hidden) {
                    final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                    pdd.setChameleon(false);
                    pdd.setDirection(d);
                    pdd.setLocation(bb_loc);
                    pdd.setDematerialise(false);
                    pdd.setPlayer(plugin.getServer().getOfflinePlayer(uuid));
                    pdd.setHide(false);
                    pdd.setOutside(false);
                    pdd.setSubmarine(rsc.isSubmarine());
                    pdd.setTardisID(id);
                    pdd.setBiome(biome);
                    plugin.getPresetDestroyer().destroyPreset(pdd);
                }
                // destroy the inner TARDIS
                // give the TARDIS time to remove itself as it's not hidden
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if ((plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) || name.contains("TARDIS_WORLD_")) {
                            // delete TARDIS world
                            List<Player> players = cw.getPlayers();
                            for (Player p : players) {
                                p.kickPlayer("World scheduled for deletion!");
                            }
                            if (plugin.isMVOnServer()) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv remove " + name);
                            }
                            if (plugin.getPM().isPluginEnabled("MultiWorld")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw unload " + name);
                            }
                            if (plugin.getPM().isPluginEnabled("WorldBorder")) {
                                // wb <world> clear
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "wb " + name + " clear");
                            }
                            plugin.getServer().unloadWorld(cw, true);
                            File world_folder = new File(plugin.getServer().getWorldContainer() + File.separator + name + File.separator);
                            if (!deleteFolder(world_folder)) {
                                plugin.debug("Could not delete world <" + name + ">");
                            }
                        } else {
                            plugin.getInteriorDestroyer().destroyInner(schm, id, cw, restore, args[1], tips);
                        }
                        cleanDatabase(id);
                        TARDISMessage.send(sender, "TARDIS_EXTERMINATED");
                    }
                }, 40L);
            } else {
                TARDISMessage.send(sender, "PLAYER_NOT_FOUND_DB", args[1]);
                return true;
            }
        } else {
            TARDISMessage.send(sender, "UUID_NOT_FOUND", args[1]);
            return true;
        }
        return true;
    }

    private int getRestore(World w) {
        if (w == null || w.getWorldType() == WorldType.FLAT || w.getName().equals("TARDIS_TimeVortex") || w.getGenerator() instanceof TARDISChunkGenerator) {
            return 0;
        }
        Environment env = w.getEnvironment();
        switch (env) {
            case NETHER:
                return 87;
            case THE_END:
                return 121;
            default:
                return 1;
        }
    }

    public static void cleanDatabase(int id) {
        QueryFactory qf = new QueryFactory(TARDIS.plugin);
        List<String> tables = Arrays.asList("ars", "back", "blocks", "chunks", "controls", "current", "destinations", "doors", "gravity_well", "homes", "lamps", "next", "tardis", "travellers");
        // remove record from database tables
        for (String table : tables) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            qf.doDelete(table, where);
        }
    }
}
