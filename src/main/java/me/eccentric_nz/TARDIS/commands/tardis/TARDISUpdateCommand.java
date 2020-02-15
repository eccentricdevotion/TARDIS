/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.ResultSetFarming;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Farm;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.UPDATEABLE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISUpdateCommand {

    private final TARDIS plugin;

    TARDISUpdateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean startUpdate(Player player, String[] args) {
        if (player.hasPermission("tardis.update")) {
            if (args.length == 1 && plugin.getPM().isPluginEnabled("ProtocolLib")) {
                return new TARDISUpdateChatGUI(plugin).showInterface(player, args);
            } else if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            Tardis tardis = rs.getTardis();
            int ownerid = tardis.getTardis_id();
            String tardis_block = TARDISStringUtils.toScoredUppercase(args[1]);
            UPDATEABLE updateable;
            try {
                updateable = UPDATEABLE.valueOf(tardis_block);
            } catch (IllegalArgumentException e) {
                new TARDISUpdateLister(player).list();
                return true;
            }
            if (updateable.equals(UPDATEABLE.SIEGE) && !plugin.getConfig().getBoolean("siege.enabled")) {
                TARDISMessage.send(player, "SIEGE_DISABLED");
                return true;
            }
            if (updateable.equals(UPDATEABLE.BEACON) && !tardis.isPowered_on()) {
                TARDISMessage.send(player, "UPDATE_BEACON");
                return true;
            }
            if (updateable.equals(UPDATEABLE.HINGE)) {
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.IRON_DOOR)) {
                    Door door = (Door) block.getBlockData();
                    if (args.length == 3) {
                        Hinge setHinge = Hinge.valueOf(args[2].toUpperCase(Locale.ENGLISH));
                        door.setHinge(setHinge);
                    } else {
                        Hinge hinge = door.getHinge();
                        if (hinge.equals(Hinge.LEFT)) {
                            door.setHinge(Hinge.RIGHT);
                        } else {
                            door.setHinge(Hinge.LEFT);
                        }
                    }
                    block.setBlockData(door);
                }
                return true;
            }
            if (updateable.equals(UPDATEABLE.VAULT)) {
                return new TARDISVaultCommand(plugin).addDropChest(player);
            }
            if (updateable.equals(UPDATEABLE.ADVANCED) && !player.hasPermission("tardis.advanced")) {
                TARDISMessage.send(player, "NO_PERM_ADV");
                return true;
            }
            if (updateable.equals(UPDATEABLE.STORAGE)) {
                // update note block if it's not MUSHROOM_STEM
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.NOTE_BLOCK)) {
                    BlockData mushroom = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
                    block.setBlockData(mushroom, true);
                }
            }
            if (updateable.equals(UPDATEABLE.FORCEFIELD) && !player.hasPermission("tardis.forcefield")) {
                TARDISMessage.send(player, "NO_PERM_FF");
                return true;
            }
            if (updateable.equals(UPDATEABLE.STORAGE) && !player.hasPermission("tardis.storage")) {
                TARDISMessage.send(player, "NO_PERM_DISK");
                return true;
            }
            if (updateable.equals(UPDATEABLE.BACKDOOR) && !player.hasPermission("tardis.backdoor")) {
                TARDISMessage.send(player, "NO_PERM_BACKDOOR");
                return true;
            }
            if (updateable.equals(UPDATEABLE.TEMPORAL) && !player.hasPermission("tardis.temporal")) {
                TARDISMessage.send(player, "NO_PERM_TEMPORAL");
                return true;
            }
            if ((updateable.equals(UPDATEABLE.FARM) || updateable.equals(UPDATEABLE.STABLE) || updateable.equals(UPDATEABLE.VILLAGE) || updateable.equals(UPDATEABLE.STALL)) && !player.hasPermission("tardis.farm")) {
                TARDISMessage.send(player, "UPDATE_NO_PERM", tardis_block);
                return true;
            }
            // must grow a room first
            if (updateable.equals(UPDATEABLE.FARM) || updateable.equals(UPDATEABLE.STABLE) || updateable.equals(UPDATEABLE.STALL) || updateable.equals(UPDATEABLE.VILLAGE)) {
                // check ARS for room type
                boolean hasFarm = false;
                boolean hasStable = false;
                boolean hasStall = false;
                boolean hasVillage = false;
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("tardis_id", ownerid);
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
                                if (col.equals("HAY_BLOCK")) {
                                    hasStable = true;
                                }
                                if (col.equals("NETHER_WART_BLOCK")) {
                                    hasStall = true;
                                }
                                if (col.equals("OAK_LOG")) {
                                    hasVillage = true;
                                }
                            }
                        }
                    }
                }
                ResultSetFarming rsf = new ResultSetFarming(plugin, ownerid);
                if (rsf.resultSet()) {
                    Farm farming = rsf.getFarming();
                    if (updateable.equals(UPDATEABLE.FARM) && farming.getFarm().isEmpty() && !hasFarm) {
                        TARDISMessage.send(player, "UPDATE_FARM");
                        return true;
                    }
                    if (updateable.equals(UPDATEABLE.STABLE) && farming.getStable().isEmpty() && !hasStable) {
                        TARDISMessage.send(player, "UPDATE_STABLE");
                        return true;
                    }
                    if (updateable.equals(UPDATEABLE.STALL) && farming.getStall().isEmpty() && !hasStall) {
                        TARDISMessage.send(player, "UPDATE_STALL");
                        return true;
                    }
                    if (updateable.equals(UPDATEABLE.VILLAGE) && farming.getVillage().isEmpty() && !hasVillage) {
                        TARDISMessage.send(player, "UPDATE_VILLAGE");
                        return true;
                    }
                }
            }
            if (updateable.equals(UPDATEABLE.RAIL) || updateable.equals(UPDATEABLE.ZERO)) {
                if (updateable.equals(UPDATEABLE.RAIL) && tardis.getRail().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_RAIL");
                    return true;
                }
                if (updateable.equals(UPDATEABLE.ZERO) && tardis.getZero().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_ZERO");
                    return true;
                }
            }
            if (updateable.equals(UPDATEABLE.ARS)) {
                if (!player.hasPermission("tardis.architectural")) {
                    TARDISMessage.send(player, "NO_PERM_ARS");
                    return true;
                }
                if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                    TARDISMessage.send(player, "ARS_OWN_WORLD");
                    return true;
                }
            }
            if (!updateable.equals(UPDATEABLE.BACKDOOR)) {
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (!rst.resultSet()) {
                    TARDISMessage.send(player, "NOT_IN_TARDIS");
                    return false;
                }
                int thisid = rst.getTardis_id();
                if (thisid != ownerid) {
                    TARDISMessage.send(player, "CMD_ONLY_TL");
                    return false;
                }
            }
            plugin.getTrackerKeeper().getPlayers().put(player.getUniqueId(), tardis_block);
            TARDISMessage.send(player, "UPDATE_CLICK", tardis_block);
            if (updateable.equals(UPDATEABLE.DIRECTION)) {
                TARDISMessage.send(player, "HOOK_REMIND");
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
