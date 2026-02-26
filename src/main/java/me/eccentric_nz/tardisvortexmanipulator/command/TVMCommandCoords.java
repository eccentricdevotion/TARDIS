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
package me.eccentric_nz.tardisvortexmanipulator.command;

import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eccentric_nz
 */
public class TVMCommandCoords {

    private final TARDIS plugin;

    public TVMCommandCoords(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player, World world, BlockPosition pos) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return;
        }
        Parameters params = new Parameters(player, Flag.getAPIFlags());
        int required;
        List<String> worlds = new ArrayList<>();
        Location l;
        if (world == null) {
            required = plugin.getVortexConfig().getInt("tachyon_use.travel.random");
            // random
            l = plugin.getTardisAPI().getRandomLocation(plugin.getTardisAPI().getWorlds(), null, params);
        } else if (pos == null) {
            // check world is enabled for travel
            if (!containsIgnoreCase(world.getName(), plugin.getTardisAPI().getWorlds())) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
                return;
            }
            required = plugin.getVortexConfig().getInt("tachyon_use.travel.world");
            // only world specified (or incomplete setting)
            worlds.add(world.getName());
            l = plugin.getTardisAPI().getRandomLocation(worlds, null, params);
        } else {
            required = plugin.getVortexConfig().getInt("tachyon_use.travel.coords");
            // world, x, y, z specified
            // check world is enabled for travel
            if (!containsIgnoreCase(world.getName(), plugin.getTardisAPI().getWorlds())) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
                return;
            }
            l = new Location(world, pos.blockX(), pos.blockY(), pos.blockZ());
            // check block has space for player
            if (!l.getBlock().getType().equals(Material.AIR)) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_ADJUST");
                // get highest block at these coords
                int highest = l.getWorld().getHighestBlockYAt(l);
                l.setY(highest);
            }
        }
        List<Player> players = new ArrayList<>();
        players.add(player);
        if (plugin.getVortexConfig().getBoolean("allow.multiple")) {
            for (Entity e : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                if (e instanceof Player && !e.getUniqueId().equals(player.getUniqueId())) {
                    players.add((Player) e);
                }
            }
        }
        String uuid = player.getUniqueId().toString();
        int actual = required * players.size();
        if (!TVMUtils.checkTachyonLevel(uuid, actual)) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", actual);
            return;
        }
        if (l != null) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_STANDBY");
            while (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
            // remove tachyons
            new TVMQueryFactory(plugin).alterTachyons(uuid, -actual);
        } else {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PARAMETERS");
        }
    }

    private boolean containsIgnoreCase(String str, List<String> list) {
        for (String s : list) {
            if (s.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
