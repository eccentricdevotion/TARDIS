/*
 * Copyright (C) 2026 eccentric_nz
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
import java.util.UUID;

import static me.eccentric_nz.TARDIS.destroyers.Exterminator.deleteFolder;

/**
 * @author eccentric_nz
 */
public class DeleteTARDISCommand {

    private final TARDIS plugin;

    public DeleteTARDISCommand(TARDIS plugin) {
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

    public boolean deleteTARDIS(CommandSender sender, Player player, int abandoned) {
        HashMap<String, Object> where = new HashMap<>();
            // Look up this player's UUID
        UUID uuid = player.getUniqueId();
            where.put("uuid", uuid.toString());

        where.put("abandoned", abandoned);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            process(rs.getTardis(), sender, player);
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND_DB", player.getName());
            return true;
        }
        return true;
    }

    public boolean deleteTARDIS(CommandSender sender, int id, int abandoned) {
        HashMap<String, Object> where = new HashMap<>();
        OfflinePlayer player = null;
        where.put("tardis_id", id);
        where.put("abandoned", abandoned);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            player = plugin.getServer().getOfflinePlayer(tardis.getUuid());
            process(tardis, sender, player.getPlayer());
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND_DB", "tardis_id=" + id);
            return true;
        }
        return true;
    }

    public boolean deleteJunk(CommandSender sender) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            process(rs.getTardis(), sender, null);
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND_DB", "junk");
            return true;
        }
        return true;
    }

    private void process(Tardis tardis, CommandSender sender, Player player) {
        int id = tardis.getTardisId();
        int tips = tardis.getTIPS();
        Schematic schm = tardis.getSchematic();
        String chunkLoc = tardis.getChunk();
        boolean hidden = tardis.isHidden();
        String[] cdata = chunkLoc.split(":");
        String wname = cdata[0];
        World cw = TARDISAliasResolver.getWorldFromAlias(wname);
        if (cw == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_DELETED");
            return;
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
            return;
        }
        plugin.getPM().callEvent(new TARDISDestructionEvent(player, bb_loc, tardis.getOwner()));
        // destroy outer TARDIS
        if (!hidden) {
            UUID u = tardis.getUuid();
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
    }
}
