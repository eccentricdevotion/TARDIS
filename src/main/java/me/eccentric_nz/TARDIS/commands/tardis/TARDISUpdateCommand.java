/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISUpdateCommand {

    private final TARDIS plugin;

    public TARDISUpdateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startUpdate(Player player, String[] args) {
        if (player.hasPermission("tardis.update")) {
            String[] validBlockNames = {"advanced", "ars", "artron", "back", "backdoor", "button", "chameleon", "condenser", "creeper", "direction", "door", "eps", "farm", "handbrake", "info", "keyboard", "light", "rail", "save-sign", "scanner", "stable", "storage", "temporal", "terminal", "village", "world-repeater", "x-repeater", "y-repeater", "z-repeater", "zero"};
            if (args.length < 2) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            String tardis_block = args[1].toLowerCase(Locale.ENGLISH);
            if (!Arrays.asList(validBlockNames).contains(tardis_block)) {
                new TARDISUpdateLister(plugin, player).list();
                return true;
            }
            if (tardis_block.equals("advanced") && !player.hasPermission("tardis.advanced")) {
                player.sendMessage(plugin.getPluginName() + "You do not have permission to create an Advanced Console!");
                return true;
            }
            if (tardis_block.equals("storage") && !player.hasPermission("tardis.storage")) {
                player.sendMessage(plugin.getPluginName() + "You do not have permission to create Disk Storage!");
                return true;
            }
            if (tardis_block.equals("backdoor") && !player.hasPermission("tardis.backdoor")) {
                player.sendMessage(plugin.getPluginName() + "You do not have permission to create a back door!");
                return true;
            }
            if (tardis_block.equals("temporal") && !player.hasPermission("tardis.temporal")) {
                player.sendMessage(plugin.getPluginName() + "You do not have permission to create a Temporal Locator!");
                return true;
            }
            if ((tardis_block.equals("farm") || tardis_block.equals("stable") || tardis_block.equals("village")) && !player.hasPermission("tardis.farm")) {
                player.sendMessage(plugin.getPluginName() + "You do not have permission to update the " + tardis_block + "!");
                return true;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NOT_A_TIMELORD.getText());
                return false;
            }
            // must grow a room first
            if (tardis_block.equals("farm") || tardis_block.equals("stable") || tardis_block.equals("village") || tardis_block.equals("rail")) {
                if (tardis_block.equals("farm") && rs.getFarm().isEmpty()) {
                    player.sendMessage(plugin.getPluginName() + "You must grow a farm room before you can update its position!");
                    return true;
                }
                if (tardis_block.equals("stable") && rs.getStable().isEmpty()) {
                    player.sendMessage(plugin.getPluginName() + "You must grow a stable room before you can update its position!");
                    return true;
                }
                if (tardis_block.equals("village") && rs.getVillage().isEmpty()) {
                    player.sendMessage(plugin.getPluginName() + "You must grow a village room before you can update its position!");
                    return true;
                }
                if (tardis_block.equals("rail") && rs.getRail().isEmpty()) {
                    player.sendMessage(plugin.getPluginName() + "You need to grow a rail room before you can update its position.");
                    return true;
                }
                if (tardis_block.equals("zero") && rs.getZero().isEmpty()) {
                    player.sendMessage(plugin.getPluginName() + "You need to grow a zero room before you can update its entry button.");
                    return true;
                }
            }
            if (tardis_block.equals("ars")) {
                if (!player.hasPermission("tardis.ars")) {
                    player.sendMessage(plugin.getPluginName() + "You do not have permission to create an Architectural Reconfiguration System!");
                    return true;
                }
                if (!plugin.getUtils().canGrowRooms(rs.getChunk())) {
                    player.sendMessage(plugin.getPluginName() + "You cannot use the Architectural Reconfiguration System unless your TARDIS was created in its own world!");
                    return true;
                }
                SCHEMATIC schm = rs.getSchematic();
                if (schm.equals(SCHEMATIC.CUSTOM)) {
                    player.sendMessage(plugin.getPluginName() + "You cannot use the Architectural Reconfiguration System with a CUSTOM TARDIS!");
                    return true;
                }
            }
            if (!tardis_block.equals("backdoor")) {
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("player", player.getName());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (!rst.resultSet()) {
                    player.sendMessage(plugin.getPluginName() + MESSAGE.NOT_IN_TARDIS.getText());
                    return false;
                }
            }
            plugin.getTrackerKeeper().getTrackPlayers().put(player.getName(), tardis_block);
            player.sendMessage(plugin.getPluginName() + "Click the TARDIS " + tardis_block + " to update its position.");
            if (tardis_block.equals("direction")) {
                player.sendMessage(plugin.getPluginName() + "Don't forget to place a TRIPWIRE HOOK in the Direction Frame to enable it.");
            }
            return true;
        } else {
            player.sendMessage(plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
