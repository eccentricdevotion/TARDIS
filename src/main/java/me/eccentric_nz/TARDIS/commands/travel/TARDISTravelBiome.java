/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISBiomeReaderListener;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TARDISBiomeFinder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelBiome {

    private final TARDIS plugin;

    public TARDISTravelBiome(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args, int id) {
        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_BIOME");
            return true;
        }
        String upper = args[1].toUpperCase(Locale.ENGLISH);
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false) && !upper.equals("LIST")) {
            if (plugin.getDifficulty().equals(Difficulty.MEDIUM)) {
                // check they have a biome disk in storage
                boolean hasBiomeDisk = false;
                UUID uuid = player.getUniqueId();
                HashMap<String, Object> whereb = new HashMap<>();
                whereb.put("uuid", uuid.toString());
                ResultSetDiskStorage rsb = new ResultSetDiskStorage(plugin, whereb);
                if (rsb.resultSet()) {
                    try {
                        ItemStack[] disks1 = TARDISSerializeInventory.itemStacksFromString(rsb.getBiomesOne());
                        if (TARDISBiomeReaderListener.hasBiomeDisk(disks1, upper)) {
                            hasBiomeDisk = true;
                        } else {
                            ItemStack[] disks2 = TARDISSerializeInventory.itemStacksFromString(rsb.getBiomesTwo());
                            if (TARDISBiomeReaderListener.hasBiomeDisk(disks2, upper)) {
                                hasBiomeDisk = true;
                            }
                        }
                    } catch (IOException ex) {
                        plugin.debug("Could not serialize inventory!");
                    }
                }
                if (!hasBiomeDisk) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_DISK_NOT_FOUND");
                    return true;
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ADV_BIOME");
                return true;
            }
        }
        if (upper.equals("LIST")) {
            StringBuilder buf = new StringBuilder();
            for (Biome bi : Biome.values()) {
                if (!bi.equals(Biome.THE_VOID)) {
                    buf.append(bi.toString()).append(", ");
                }
            }
            String b = buf.substring(0, buf.length() - 2);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOMES", b);
        } else {
            try {
                Biome biome = Biome.valueOf(upper);
                if (biome.equals(Biome.THE_VOID)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_TRAVEL_NOT_VALID");
                    return true;
                }
                plugin.getMessenger().sendStatus(player, "BIOME_SEARCH");
                World w;
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                    return true;
                }
                // have they specified a world argument?
                if (args.length > 2) {
                    // must be in the vortex
                    if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_FROM_VORTEX");
                        return true;
                    }
                    String planet = args[2].toLowerCase(Locale.ROOT);
                    if (TARDISConstants.isTARDISPlanet(planet)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_PLANET", args[2]);
                        return true;
                    }
                    // get the world
                    w = TARDISAliasResolver.getWorldFromAlias(args[2]);
                    if (w == null) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "WORLD_DELETED", args[2]);
                        return true;
                    }
                } else {
                    String planet = rsc.getWorld().getName();
                    if (TARDISConstants.isTARDISPlanet(planet)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_PLANET", rsc.getWorld().getName());
                        return true;
                    }
                    w = rsc.getWorld();
                }
                Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                new TARDISBiomeFinder(plugin).run(w, biome, player, id, rsc.getDirection(), current);
            } catch (IllegalArgumentException iae) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_VALID");
                return true;
            }
        }
        return true;
    }
}
