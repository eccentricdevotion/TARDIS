/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.api.event.TARDISDestructionEvent;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
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
        boolean junk = (args[1].toLowerCase().equals("junk"));
        int tmp = -1;
        try {
            tmp = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            // do nothing
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        Player player = null;
        if (tmp == -1) {
            // this should be run from the console if the player running it is the player to be deleted
            if (sender instanceof Player) {
                player = (Player) sender;
                if (player.getName().equals(args[1])) {
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
                    if (rst.resultSet()) {
                        TARDISMessage.send(sender, "TARDIS_DELETE_NO");
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
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            final int id = tardis.getTardis_id();
            final int tips = tardis.getTIPS();
            final SCHEMATIC schm = tardis.getSchematic();
            String chunkLoc = tardis.getChunk();
            boolean hidden = tardis.isHidden();
            String[] cdata = chunkLoc.split(":");
            String wname;
            if (junk) {
                wname = plugin.getConfig().getString("creation.default_world_name");
            } else {
                wname = cdata[0];
            }
            final String name = wname;
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
            plugin.getPM().callEvent(new TARDISDestructionEvent(player, bb_loc, tardis.getOwner()));
            // destroy outer TARDIS
            if (!hidden) {
                UUID u = rs.getTardis().getUuid();
                final DestroyData dd = new DestroyData(plugin, u.toString());
                dd.setChameleon(false);
                dd.setDirection(d);
                dd.setLocation(bb_loc);
                dd.setPlayer(plugin.getServer().getOfflinePlayer(u));
                dd.setHide(true);
                dd.setOutside(false);
                dd.setSubmarine(rsc.isSubmarine());
                dd.setTardisID(id);
                dd.setBiome(biome);
                plugin.getPresetDestroyer().destroyPreset(dd);
            } else {
                // restore biome
                plugin.getUtils().restoreBiome(bb_loc, biome);
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
        List<String> tables = Arrays.asList("ars", "back", "blocks", "chunks", "controls", "current", "destinations", "doors", "gravity_well", "homes", "junk", "lamps", "next", "tardis", "thevoid", "travellers", "vaults");
        // remove record from database tables
        for (String table : tables) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            qf.doDelete(table, where);
        }
    }
}
