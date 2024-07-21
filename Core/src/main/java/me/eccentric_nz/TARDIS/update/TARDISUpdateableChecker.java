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
package me.eccentric_nz.TARDIS.update;

import com.google.common.collect.Sets;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Farm;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarming;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class TARDISUpdateableChecker {

    private final TARDIS plugin;
    private final Updateable updateable;
    private final Player player;
    private final Tardis tardis;
    private final String tardis_block;

    private final Set<Updateable> mustGrowRoom = Sets.newHashSet(
            Updateable.ALLAY,
            Updateable.BAMBOO,
            Updateable.BIRDCAGE,
            Updateable.FARM,
            Updateable.FUEL,
            Updateable.HUTCH,
            Updateable.IGLOO,
            Updateable.IISTUBIL,
            Updateable.LAVA,
            Updateable.PEN,
            Updateable.SMELT,
            Updateable.STABLE,
            Updateable.STALL,
            Updateable.VAULT,
            Updateable.VILLAGE
    );

    public TARDISUpdateableChecker(TARDIS plugin, Updateable updateable, Player player, Tardis tardis, String tardis_block) {
        this.plugin = plugin;
        this.updateable = updateable;
        this.player = player;
        this.tardis = tardis;
        this.tardis_block = tardis_block;
    }

    public boolean canUpdate() {
        if (updateable.equals(Updateable.SIEGE) && !plugin.getConfig().getBoolean("siege.enabled")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_DISABLED");
            return false;
        }
        if (updateable.equals(Updateable.BEACON) && !tardis.isPoweredOn()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_BEACON");
            return false;
        }
        if (updateable.equals(Updateable.ADVANCED) && !TARDISPermission.hasPermission(player, "tardis.advanced")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_ADV");
            return false;
        }
        if (updateable.equals(Updateable.FORCEFIELD) && !TARDISPermission.hasPermission(player, "tardis.forcefield")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_FF");
            return false;
        }
        if (updateable.equals(Updateable.STORAGE) && !TARDISPermission.hasPermission(player, "tardis.storage")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_DISK");
            return false;
        }
        if (updateable.equals(Updateable.BACKDOOR) && !TARDISPermission.hasPermission(player, "tardis.backdoor")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_BACKDOOR");
            return false;
        }
        if (updateable.equals(Updateable.TEMPORAL) && !TARDISPermission.hasPermission(player, "tardis.temporal")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TEMPORAL");
            return false;
        }
        boolean hasAllay = false;
        boolean hasBamboo = false;
        boolean hasBirdcage = false;
        boolean hasFarm = false;
        boolean hasHutch = false;
        boolean hasIgloo = false;
        boolean hasIistubil = false;
        boolean hasLava = false;
        boolean hasPen = false;
        boolean hasSmelt = false;
        boolean hasStable = false;
        boolean hasStall = false;
        boolean hasVault = false;
        boolean hasVillage = false;
        // check ARS for room type
        if (mustGrowRoom.contains(updateable)) {
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("tardis_id", tardis.getTardisId());
            ResultSetARS rsa = new ResultSetARS(plugin, wherea);
            if (rsa.resultSet()) {
                // check for rooms
                String[][][] json = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                for (String[][] level : json) {
                    for (String[] row : level) {
                        for (String col : row) {
                            switch (col) {
                                case "LIGHT_BLUE_CONCRETE" -> hasAllay = true;
                                case "BAMBOO" -> hasBamboo = true;
                                case "YELLOW_GLAZED_TERRACOTTA" -> hasBirdcage = true;
                                case "DIRT" -> hasFarm = true;
                                case "ACACIA_LOG" -> hasHutch = true;
                                case "PACKED_ICE" -> hasIgloo = true;
                                case "MOSS_BLOCK" -> hasPen = true;
                                case "WHITE_GLAZED_TERRACOTTA" -> hasIistubil = true;
                                case "MAGMA_BLOCK" -> hasLava = true;
                                case "CHEST" -> hasSmelt = true;
                                case "HAY_BLOCK" -> hasStable = true;
                                case "NETHER_WART_BLOCK" -> hasStall = true;
                                case "DISPENSER" -> hasVault = true;
                                case "OAK_LOG" -> hasVillage = true;
                                default -> {
                                }
                            }
                        }
                    }
                }
            }
        }
        if (updateable.equals(Updateable.VAULT)) {
            if (!TARDISPermission.hasPermission(player, "tardis.vault")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_NO_PERM", "Vault room drop chest");
                return false;
            }
            // must grow room first
            if (!hasVault) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                return false;
            }
        }
        if (updateable.equals(Updateable.FUEL) || updateable.equals(Updateable.SMELT)) {
            if (!TARDISPermission.hasPermission(player, "tardis.room.smelter")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_NO_PERM", "Smelter room drop chest");
                return false;
            }
            // must grow room first
            if (!hasSmelt) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                return false;
            }
        }
        if (updateable.equals(Updateable.FARM) || updateable.equals(Updateable.IGLOO) || updateable.equals(Updateable.STABLE)
                || updateable.equals(Updateable.STALL) || updateable.equals(Updateable.VILLAGE) || updateable.equals(Updateable.IISTUBIL)
                || updateable.equals(Updateable.HUTCH) || updateable.equals(Updateable.LAVA) || updateable.equals(Updateable.PEN)
                || updateable.equals(Updateable.BAMBOO) || updateable.equals(Updateable.BIRDCAGE) || updateable.equals(Updateable.ALLAY)) {
            if (!TARDISPermission.hasPermission(player, "tardis.farm")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_NO_PERM", tardis_block);
                return false;
            }
            // must grow a room first
            ResultSetFarming rsf = new ResultSetFarming(plugin, tardis.getTardisId());
            if (rsf.resultSet()) {
                Farm farming = rsf.getFarming();
                if (updateable.equals(Updateable.FARM) && farming.getFarm().isEmpty() && !hasFarm) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.ALLAY) && farming.getAllay().isEmpty() && !hasAllay) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.IGLOO) && farming.getIgloo().isEmpty() && !hasIgloo) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.IISTUBIL) && farming.getIistubil().isEmpty() && !hasIistubil) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.LAVA) && farming.getLava().isEmpty() && !hasLava) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.PEN) && farming.getPen().isEmpty() && !hasPen) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.HUTCH) && farming.getHutch().isEmpty() && !hasHutch) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.BAMBOO) && farming.getBamboo().isEmpty() && !hasBamboo) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.BIRDCAGE) && farming.getBirdcage().isEmpty() && !hasBirdcage) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.STABLE) && farming.getStable().isEmpty() && !hasStable) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.STALL) && farming.getStall().isEmpty() && !hasStall) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.VILLAGE) && farming.getVillage().isEmpty() && !hasVillage) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
            }
        }
        if (updateable.equals(Updateable.RAIL) && tardis.getRail().isEmpty()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
            return false;
        }
        if (updateable.equals(Updateable.ZERO) && tardis.getZero().isEmpty()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ZERO");
            return false;
        }
        if (updateable.equals(Updateable.ARS)) {
            if (!TARDISPermission.hasPermission(player, "tardis.architectural")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_ARS");
                return false;
            }
            if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_OWN_WORLD");
                return false;
            }
        }
        if (updateable.equals(Updateable.WEATHER)) {
            if (!TARDISPermission.hasPermission(player, "tardis.weather.clear") && !TARDISPermission.hasPermission(player, "tardis.weather.rain") && !TARDISPermission.hasPermission(player, "tardis.weather.thunder")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                return false;
            }
        }
        if (!updateable.equals(Updateable.BACKDOOR)) {
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
                return false;
            }
            int thisid = rst.getTardis_id();
            if (thisid != tardis.getTardisId()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_ONLY_TL");
                return false;
            }
        }
        return true;
    }
}
