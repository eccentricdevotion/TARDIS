/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
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
 * @author macgeek
 */
public class TVMCommandCoords {

    private final TARDIS plugin;

    public TVMCommandCoords(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        Parameters params = new Parameters(player, Flag.getAPIFlags());
        int required;
        List<String> worlds = new ArrayList<>();
        Location l;
        switch (args.length) {
            case 1, 2, 3 -> {
                // check world is an actual world
                if (plugin.getServer().getWorld(args[0]) == null) {
                    TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_WORLD");
                    return true;
                }
                // check world is enabled for travel
                if (!containsIgnoreCase(args[0], plugin.getTardisAPI().getWorlds())) {
                    TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
                    return true;
                }
                required = plugin.getConfig().getInt("tachyon_use.travel.world");
                // only world specified (or incomplete setting)
                worlds.add(args[0]);
                l = plugin.getTardisAPI().getRandomLocation(worlds, null, params);
            }
            case 4 -> {
                required = plugin.getConfig().getInt("tachyon_use.travel.coords");
                // world, x, y, z specified
                World w;
                if (args[0].contains("~")) {
                    // relative location
                    w = player.getLocation().getWorld();
                } else {
                    w = plugin.getServer().getWorld(args[0]);
                    if (w == null) {
                        TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_WORLD");
                        return true;
                    }
                    // check world is enabled for travel
                    if (!containsIgnoreCase(args[0], plugin.getTardisAPI().getWorlds())) {
                        TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
                        return true;
                    }
                }
                double x;
                double y;
                double z;
                try {
                    if (args[1].startsWith("~")) {
                        // get players current location
                        Location tl = player.getLocation();
                        double tx = tl.getX();
                        double ty = tl.getY();
                        double tz = tl.getZ();
                        // strip off the initial "~" and add to current position
                        x = tx + Double.parseDouble(args[1].substring(1));
                        y = ty + Double.parseDouble(args[2].substring(1));
                        z = tz + Double.parseDouble(args[3].substring(1));
                    } else {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                        z = Double.parseDouble(args[3]);
                    }
                } catch (NumberFormatException e) {
                    TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_COORDS");
                    return true;
                }
                l = new Location(w, x, y, z);
                // check block has space for player
                if (!l.getBlock().getType().equals(Material.AIR)) {
                    TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_ADJUST");
                    // get highest block at these coords
                    int highest = l.getWorld().getHighestBlockYAt(l);
                    l.setY(highest);
                }
            }
            default -> {
                required = plugin.getConfig().getInt("tachyon_use.travel.random");
                // random
                l = plugin.getTardisAPI().getRandomLocation(plugin.getTardisAPI().getWorlds(), null, params);
            }
        }
        List<Player> players = new ArrayList<>();
        players.add(player);
        if (plugin.getConfig().getBoolean("allow.multiple")) {
            for (Entity e : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                if (e instanceof Player && !e.getUniqueId().equals(player.getUniqueId())) {
                    players.add((Player) e);
                }
            }
        }
        String uuid = player.getUniqueId().toString();
        int actual = required * players.size();
        if (!TVMUtils.checkTachyonLevel(uuid, actual)) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", actual);
            return true;
        }
        if (l != null) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_STANDY");
            while (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
            // remove tachyons
            new TVMQueryFactory(plugin).alterTachyons(uuid, -actual);
        } else {
            //close(player);
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PARAMETERS");
        }
        return true;
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
