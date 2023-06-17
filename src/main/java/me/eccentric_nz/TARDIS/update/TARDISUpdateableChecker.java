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
package me.eccentric_nz.TARDIS.update;

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Set;
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

public class TARDISUpdateableChecker {

    private final TARDIS plugin;
    private final Updateable updateable;
    private final Player player;
    private final Tardis tardis;
    private final String tardis_block;

    private final Set<Updateable> mustGrowRoom = Sets.newHashSet(
            Updateable.FARM,
            Updateable.FUEL,
            Updateable.IGLOO,
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
        if (updateable.equals(Updateable.BEACON) && !tardis.isPowered_on()) {
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
        boolean hasFarm = false;
        boolean hasIgloo = false;
        boolean hasSmelt = false;
        boolean hasStable = false;
        boolean hasStall = false;
        boolean hasVault = false;
        boolean hasVillage = false;
        // check ARS for room type
        if (mustGrowRoom.contains(updateable)) {
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("tardis_id", tardis.getTardis_id());
            ResultSetARS rsa = new ResultSetARS(plugin, wherea);
            if (rsa.resultSet()) {
                // check for rooms
                String[][][] json = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                for (String[][] level : json) {
                    for (String[] row : level) {
                        for (String col : row) {
                            if (col.equals("DIRT")) {
                                hasFarm = true;
                            }
                            if (col.equals("PACKED_ICE")) {
                                hasIgloo = true;
                            }
                            if (col.equals("CHEST")) {
                                hasSmelt = true;
                            }
                            if (col.equals("HAY_BLOCK")) {
                                hasStable = true;
                            }
                            if (col.equals("NETHER_WART_BLOCK")) {
                                hasStall = true;
                            }
                            if (col.equals("DISPENSER")) {
                                hasVault = true;
                            }
                            if (col.equals("OAK_LOG")) {
                                hasVillage = true;
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
        if (updateable.equals(Updateable.FARM) || updateable.equals(Updateable.IGLOO) || updateable.equals(Updateable.STABLE) || updateable.equals(Updateable.STALL) || updateable.equals(Updateable.VILLAGE)) {
            if (!TARDISPermission.hasPermission(player, "tardis.farm")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_NO_PERM", tardis_block);
                return false;
            }
            // must grow a room first
            ResultSetFarming rsf = new ResultSetFarming(plugin, tardis.getTardis_id());
            if (rsf.resultSet()) {
                Farm farming = rsf.getFarming();
                if (updateable.equals(Updateable.FARM) && farming.getFarm().isEmpty() && !hasFarm) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.IGLOO) && farming.getIgloo().isEmpty() && !hasIgloo) {
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
            if (thisid != tardis.getTardis_id()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_ONLY_TL");
                return false;
            }
        }
        return true;
    }
}
