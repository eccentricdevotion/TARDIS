/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISDestructionEvent;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.files.TARDISBlockLoader;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static me.eccentric_nz.TARDIS.destroyers.TARDISExterminator.deleteFolder;

/**
 * @author eccentric_nz
 */
public class TARDISDeleteCommand {

    private final TARDIS plugin;

    TARDISDeleteCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static void cleanDatabase(int id) {
        TARDISBlockLoader bl = new TARDISBlockLoader(TARDIS.plugin);
        bl.unloadProtectedBlocks(id);
        List<String> tables = List.of("ars", "back", "chunks", "controls", "current", "destinations", "doors", "gravity_well", "homes", "junk", "lamps", "next", "tardis", "thevoid", "travellers", "vaults");
        // remove record from database tables
        tables.forEach((table) -> {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            TARDIS.plugin.getQueryFactory().doDelete(table, where);
        });
    }

    boolean deleteTARDIS(CommandSender sender, String[] args) {
        boolean junk = (args[1].toLowerCase(Locale.ROOT).equals("junk"));
        int tmp = -1;
        int abandoned = (args.length > 2 && args[2].equals("abandoned")) ? 1 : 0;
        try {
            tmp = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            // do nothing
        }
        HashMap<String, Object> where = new HashMap<>();
        OfflinePlayer player = null;
        if (tmp == -1) {
            // Look up this player's UUID
            UUID uuid;
            if (junk) {
                uuid = UUID.fromString("00000000-aaaa-bbbb-cccc-000000000000");
            } else {
                player = plugin.getServer().getOfflinePlayer(args[1]);
                uuid = player.getUniqueId();
            }
            where.put("uuid", uuid.toString());
        } else {
            where.put("tardis_id", tmp);
        }
        where.put("abandoned", abandoned);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            int tips = tardis.getTIPS();
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
            World cw = TARDISAliasResolver.getWorldFromAlias(wname);
            if (cw == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_DELETED");
                return true;
            }
            // get the current location
            Location bb_loc = null;
            COMPASS d = COMPASS.EAST;
            boolean sub = false;
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (rsc.resultSet()) {
                Current current = rsc.getCurrent();
                bb_loc = current.location();
                d = current.direction();
                sub = current.submarine();
            }
            if (bb_loc == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return true;
            }
            plugin.getPM().callEvent(new TARDISDestructionEvent(player.getPlayer(), bb_loc, tardis.getOwner()));
            // destroy outer TARDIS
            if (!hidden) {
                UUID u = rs.getTardis().getUuid();
                DestroyData dd = new DestroyData();
                dd.setDirection(d);
                dd.setLocation(bb_loc);
                dd.setPlayer(plugin.getServer().getOfflinePlayer(u));
                dd.setHide(true);
                dd.setOutside(false);
                dd.setSubmarine(sub);
                dd.setTardisID(id);
                dd.setThrottle(SpaceTimeThrottle.REBUILD);
                plugin.getPresetDestroyer().destroyPreset(dd);
            }
            // destroy the inner TARDIS
            // give the TARDIS time to remove itself as it's not hidden
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if ((plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) || wname.contains("TARDIS_WORLD_")) {
                    // delete TARDIS world
                    List<Player> players = cw.getPlayers();
                    players.forEach((p) -> p.kick(Component.text("World scheduled for deletion!")));
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + cw + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "mv remove " + wname);
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
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TARDIS_EXTERMINATED");
            }, 40L);
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND_DB", args[1]);
            return true;
        }
        return true;
    }
}
