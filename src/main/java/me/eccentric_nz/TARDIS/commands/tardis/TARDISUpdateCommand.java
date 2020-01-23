/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.database.ResultSetFarming;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Farm;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISUpdateCommand {

    private final TARDIS plugin;
    private final List<String> validBlockNames = Arrays.asList("advanced", "ars", "artron", "back", "backdoor", "beacon", "button", "chameleon", "condenser", "control", "creeper", "direction", "dispenser", "door", "eps", "farm", "frame", "forcefield", "generator", "handbrake", "hinge", "info", "keyboard", "light", "rail", "save-sign", "scanner", "siege", "stable", "storage", "telepathic", "temporal", "terminal", "toggle_wool", "vault", "village", "world-repeater", "x-repeater", "y-repeater", "z-repeater", "zero");

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
            String tardis_block = args[1].toLowerCase(Locale.ENGLISH);
            if (!validBlockNames.contains(tardis_block)) {
                new TARDISUpdateLister(player).list();
                return true;
            }
            if (tardis_block.equals("siege") && !plugin.getConfig().getBoolean("siege.enabled")) {
                TARDISMessage.send(player, "SIEGE_DISABLED");
                return true;
            }
            if (tardis_block.equals("beacon") && !tardis.isPowered_on()) {
                TARDISMessage.send(player, "UPDATE_BEACON");
                return true;
            }
            if (tardis_block.equals("hinge")) {
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
            if (tardis_block.equals("vault")) {
                return new TARDISVaultCommand(plugin).addDropChest(player);
            }
            if (tardis_block.equals("advanced") && !player.hasPermission("tardis.advanced")) {
                TARDISMessage.send(player, "NO_PERM_ADV");
                return true;
            }
            if (tardis_block.equals("forcefield") && !player.hasPermission("tardis.forcefield")) {
                TARDISMessage.send(player, "NO_PERM_FF");
                return true;
            }
            if (tardis_block.equals("storage") && !player.hasPermission("tardis.storage")) {
                TARDISMessage.send(player, "NO_PERM_DISK");
                return true;
            }
            if (tardis_block.equals("backdoor") && !player.hasPermission("tardis.backdoor")) {
                TARDISMessage.send(player, "NO_PERM_BACKDOOR");
                return true;
            }
            if (tardis_block.equals("temporal") && !player.hasPermission("tardis.temporal")) {
                TARDISMessage.send(player, "NO_PERM_TEMPORAL");
                return true;
            }
            if ((tardis_block.equals("farm") || tardis_block.equals("stable") || tardis_block.equals("village") || tardis_block.equals("stall")) && !player.hasPermission("tardis.farm")) {
                TARDISMessage.send(player, "UPDATE_NO_PERM", tardis_block);
                return true;
            }
            // must grow a room first
            if (tardis_block.equals("farm") || tardis_block.equals("stable") || tardis_block.equals("stall") || tardis_block.equals("village")) {
                ResultSetFarming rsf = new ResultSetFarming(plugin, ownerid);
                if (rsf.resultSet()) {
                    Farm farming = rsf.getFarming();
                    if (tardis_block.equals("farm") && farming.getFarm().isEmpty()) {
                        TARDISMessage.send(player, "UPDATE_FARM");
                        return true;
                    }
                    if (tardis_block.equals("stable") && farming.getStable().isEmpty()) {
                        TARDISMessage.send(player, "UPDATE_STABLE");
                        return true;
                    }
                    if (tardis_block.equals("stall") && farming.getStall().isEmpty()) {
                        TARDISMessage.send(player, "UPDATE_STALL");
                        return true;
                    }
                    if (tardis_block.equals("village") && farming.getVillage().isEmpty()) {
                        TARDISMessage.send(player, "UPDATE_VILLAGE");
                        return true;
                    }
                }
            }
            if (tardis_block.equals("rail") || tardis_block.equals("zero")) {
                if (tardis_block.equals("rail") && tardis.getRail().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_RAIL");
                    return true;
                }
                if (tardis_block.equals("zero") && tardis.getZero().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_ZERO");
                    return true;
                }
            }
            if (tardis_block.equals("ars")) {
                if (!player.hasPermission("tardis.architectural")) {
                    TARDISMessage.send(player, "NO_PERM_ARS");
                    return true;
                }
                if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                    TARDISMessage.send(player, "ARS_OWN_WORLD");
                    return true;
                }
            }
            if (!tardis_block.equals("backdoor")) {
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
            if (tardis_block.equals("direction")) {
                TARDISMessage.send(player, "HOOK_REMIND");
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
