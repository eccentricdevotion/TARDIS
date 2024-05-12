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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.TARDISRecipeTabComplete;
import me.eccentric_nz.TARDIS.commands.handles.TARDISHandlesTeleportCommand;
import me.eccentric_nz.TARDIS.commands.handles.TARDISHandlesTransmatCommand;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.control.TARDISRandomButton;
import me.eccentric_nz.TARDIS.control.actions.LightSwitchAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TARDISHandlesRequest {

    private final TARDIS plugin;
    private final Pattern handlesPattern;

    public TARDISHandlesRequest(TARDIS plugin) {
        this.plugin = plugin;
        handlesPattern = TARDISHandlesPattern.getPattern("prefix", false);
    }

    public void process(UUID uuid, String chat) {
        // check if player is inside / outside TARDIS
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return;
        }
        if (!TARDISPermission.hasPermission(player, "tardis.handles.use")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return;
        }
        // must have a TARDIS
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(uuid.toString())) {
            int id = rs.getTardis_id();
            // check for placed Handles
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("type", 26);
            ResultSetControls rsc = new ResultSetControls(plugin, where, false);
            if (rsc.resultSet()) {
                // if placed player must be in TARDIS or be wearing a communicator
                if (!plugin.getUtils().inTARDISWorld(player)) {
                    // player must have communicator
                    if (!TARDISPermission.hasPermission(player, "tardis.handles.communicator")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_COMMUNICATOR");
                        return;
                    }
                    PlayerInventory pi = player.getInventory();
                    ItemStack communicator = pi.getHelmet();
                    if (communicator == null || !communicator.hasItemMeta() || !communicator.getType().equals(Material.BIRCH_BUTTON) || !communicator.getItemMeta().getDisplayName().equals("TARDIS Communicator")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_COMMUNICATOR");
                        return;
                    }
                }
            } else {
                // Handles must be in inventory
                boolean found = false;
                for (ItemStack is : player.getInventory().getContents()) {
                    if (is != null && is.getType().equals(Material.BIRCH_BUTTON) && is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName() && im.getDisplayName().equals("Handles")) {
                            found = true;
                        }
                    }
                }
                if (!found) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_INVENTORY");
                    return;
                }
            }
            // remove the prefix
            String removed = chat.replaceAll("(?i)" + handlesPattern, "").trim();
            // loop through handles config
            boolean matched = false;
            String key = "";
            List<String> groups = null;
            for (String k : plugin.getHandlesConfig().getConfigurationSection("core-commands").getKeys(true)) {
                if (!k.equals("travel") && !k.equals("door")) {
                    Pattern pattern = TARDISHandlesPattern.getPattern(k, false);
                    Matcher m = pattern.matcher(removed);
                    if (m.find()) {
                        matched = true;
                        key = k;
                        if (m.groupCount() > 0) {
                            groups = new ArrayList<>();
                            for (int i = 0; i < m.groupCount(); i++) {
                                groups.add(m.group(i + 1));
                            }
                        }
                        break;
                    }
                }
            }
            if (matched) {
                switch (key) {
                    case "craft" -> {
                        if (groups != null) {
                            String tardis = groups.get(1);
                            if (tardis == null || tardis.isEmpty()) {
                                // tardis recipes
                                for (String item : TARDISRecipeTabComplete.ROOT_SUBS) {
                                    if (groups.get(2).equalsIgnoreCase(item)) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardisrecipe " + item), 1L);
                                        return;
                                    }
                                }
                            } else {
                                // tardis seed block
                                for (String seed : TARDISRecipeTabComplete.TARDIS_TYPES) {
                                    if (groups.get(0).equalsIgnoreCase(seed)) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardisrecipe tardis " + seed), 1L);
                                        return;
                                    }
                                }
                                // default to budget
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardisrecipe tardis budget"), 1L);
                                return;
                            }
                            // don't understand
                            plugin.getMessenger().handlesSend(player, "HANDLES_NO_COMMAND");
                        }
                    }
                    case "remind" -> {
                        if (!plugin.getHandlesConfig().getBoolean("reminders.enabled")) {
                            plugin.getMessenger().handlesSend(player, "HANDLES_NO_COMMAND");
                            return;
                        }
                        if (groups != null) {
                            String reminder = groups.get(0);
                            String time = groups.get(1);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles remind " + uuid + " " + reminder + " " + time), 1L);
                            return;
                        }
                    }
                    case "say" -> {
                        if (groups != null) {
                            String g = groups.get(0);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles say " + uuid + " " + TARDISStringUtils.normalizeSpace(g)), 1L);
                        }
                    }
                    case "name" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles name " + uuid), 1L);
                    case "time" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles time " + uuid), 1L);
                    case "call" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles call " + uuid + " " + id), 1L);
                    case "takeoff" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles takeoff " + uuid + " " + id), 1L);
                    case "land" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles land " + uuid + " " + id), 1L);
                    case "hide" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis hide"), 1L);
                    case "rebuild" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis rebuild"), 1L);
                    case "brake" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles brake on " + uuid + " " + id), 1L);
                    case "direction" -> {
                        if (groups != null) {
                            COMPASS direction = null;
                            if (groups.get(0).equalsIgnoreCase("east")) {
                                direction = COMPASS.EAST;
                            } else if (groups.get(0).equalsIgnoreCase("north")) {
                                direction = COMPASS.NORTH;
                            } else if (groups.get(0).equalsIgnoreCase("west")) {
                                direction = COMPASS.WEST;
                            } else if (groups.get(0).equalsIgnoreCase("south")) {
                                direction = COMPASS.SOUTH;
                            }
                            if (direction != null) {
                                String d = direction.toString();
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis direction " + d), 1L);
                            }
                        }
                    }
                    case "lights" -> {
                        if (groups != null) {
                            boolean onoff = groups.get(0).equalsIgnoreCase("on");
                            // get tardis
                            HashMap<String, Object> wherel = new HashMap<>();
                            wherel.put("tardis_id", id);
                            ResultSetTardis rst = new ResultSetTardis(plugin, wherel, "", false, 2);
                            if (rst.resultSet()) {
                                Tardis tardis = rst.getTardis();
                                if ((onoff && !tardis.isLightsOn()) || (!onoff && tardis.isLightsOn())) {
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new LightSwitchAction(plugin, id, tardis.isLightsOn(), player, tardis.getSchematic().getLights()).flickSwitch(), 1L);
                                }
                            }
                        }
                    }
                    case "power" -> {
                        if (groups != null) {
                            boolean onoff = groups.get(0).equalsIgnoreCase("off");
                            // get tardis
                            HashMap<String, Object> wherel = new HashMap<>();
                            wherel.put("tardis_id", id);
                            ResultSetTardis rst = new ResultSetTardis(plugin, wherel, "", false, 2);
                            if (rst.resultSet()) {
                                Tardis tardis = rst.getTardis();
                                if ((onoff && tardis.isPoweredOn()) || (!onoff && !tardis.isPoweredOn())) {
                                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPoweredOn(), tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().getLights()).clickButton(), 1L);
                                    }
                                }
                            }
                        }
                    }
                    case "travel.save" -> {
                        if (groups != null) {
                            HashMap<String, Object> wheres = new HashMap<>();
                            wheres.put("tardis_id", id);
                            ResultSetDestinations rsd = new ResultSetDestinations(plugin, wheres, true);
                            if (rsd.resultSet()) {
                                for (HashMap<String, String> map : rsd.getData()) {
                                    String dest = map.get("dest_name");
                                    if (groups.get(0).equalsIgnoreCase(dest) && TARDISPermission.hasPermission(player, "tardis.timetravel")) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel dest " + dest), 1L);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    case "travel.home" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel home"), 1L);
                    case "travel.random" -> {
                        // get tardis
                        HashMap<String, Object> wherel = new HashMap<>();
                        wherel.put("tardis_id", id);
                        ResultSetTardis rsr = new ResultSetTardis(plugin, wherel, "", false, 2);
                        if (rsr.resultSet()) {
                            Tardis tardis = rsr.getTardis();
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISRandomButton(plugin, player, id, tardis.getArtronLevel(), 0, tardis.getCompanions(), tardis.getUuid()).clickButton(), 1L);
                            return;
                        }
                    }
                    case "travel.player" -> {
                        if (groups != null) {
                            if (!TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
                                plugin.getMessenger().handlesSend(player, "NO_PERM_PLAYER");
                                return;
                            }
                            for (Player p : plugin.getServer().getOnlinePlayers()) {
                                String name = p.getName();
                                if (groups.get(0).equalsIgnoreCase(name)) {
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel " + name), 1L);
                                    return;
                                }
                                // don't understand
                                plugin.getMessenger().handlesSend(player, "HANDLES_NO_COMMAND");
                            }
                        }
                    }
                    case "travel.area" -> {
                        if (groups != null) {
                            String area = (groups.get(0) == null || groups.get(0).isEmpty()) ? groups.get(1) : groups.get(0);
                            ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
                            if (rsa.resultSet()) {
                                // cycle through areas
                                for (String name : rsa.getNames()) {
                                    if (area.equalsIgnoreCase(name) && (TARDISPermission.hasPermission(player, "tardis.area." + name) || TARDISPermission.hasPermission(player, "tardis.area.*"))) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel area " + name), 1L);
                                        return;
                                    }
                                }
                                // don't understand
                                plugin.getMessenger().handlesSend(player, "HANDLES_NO_COMMAND");
                            }
                        }
                    }
                    case "travel.biome" -> {
                        if (groups != null) {
                            if (!TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
                                plugin.getMessenger().handlesSend(player, "TRAVEL_NO_PERM_BIOME");
                                return;
                            }
                            String gb = (groups.get(0) == null || groups.get(0).isEmpty()) ? groups.get(1) : groups.get(0);
                            // cycle through biomes
                            for (Biome biome : Biome.values()) {
                                String b = biome.toString();
                                if (gb.equalsIgnoreCase(b)) {
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel biome " + b), 1L);
                                    return;
                                }
                            }
                            // don't understand
                            plugin.getMessenger().handlesSend(player, "HANDLES_NO_COMMAND");
                        }
                    }
                    case "travel.cave" -> {
                        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.cave")) {
                            plugin.getMessenger().handlesSend(player, "TRAVEL_NO_PERM_CAVE");
                            return;
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel cave"), 1L);
                    }
                    case "travel.village" -> {
                        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
                            plugin.getMessenger().handlesSend(player, "TRAVEL_NO_PERM_VILLAGE");
                            return;
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel village"), 1L);
                    }
                    case "door.open" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis door OPEN"), 1L);
                    case "door.close" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis door CLOSE"), 1L);
                    case "door.lock" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles lock " + uuid + " " + id + " true"), 1L);
                    case "door.unlock" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles unlock " + uuid + " " + id + " false"), 1L);
                    case "scan" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles scan " + uuid + " " + id), 1L);
                    case "teleport" -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISHandlesTeleportCommand(plugin).beamMeUp(player), 1L);
                    case "transmat" -> {
                        if (groups != null) {
                            if (!TARDISPermission.hasPermission(player, "tardis.transmat")) {
                                plugin.getMessenger().handlesSend(player, "NO_PERMS");
                                return;
                            }
                            Location location = player.getLocation();
                            // must be in their TARDIS
                            if (!plugin.getUtils().inTARDISWorld(location)) {
                                plugin.getMessenger().handlesSend(player, "HANDLES_NO_TRANSMAT_WORLD");
                                return;
                            }
                            ResultSetHandlesTransmat rst = new ResultSetHandlesTransmat(plugin, id);
                            if (rst.findSite(groups.get(0))) {
                                new TARDISHandlesTransmatCommand(plugin).siteToSiteTransport(player, rst.getLocation());
                            } else {
                                plugin.getMessenger().handlesSend(player, "HANDLES_NO_TRANSMAT");
                            }
                        }
                    }
                    default -> {
                    }
                }
                if (key.equals("remind")) {
                    TARDISSounds.playTARDISSound(player, "handles_confirmed", 5L);
                }
            } else {
                // try custom-commands
                for (String k : plugin.getHandlesConfig().getConfigurationSection("custom-commands").getKeys(false)) {
                    Pattern pattern = TARDISHandlesPattern.getPattern(k, true);
                    Matcher m = pattern.matcher(removed);
                    if (m.find()) {
                        matched = true;
                        key = k;
                        if (m.groupCount() > 0) {
                            groups = new ArrayList<>();
                            for (int i = 0; i < m.groupCount(); i++) {
                                groups.add(m.group(i + 1));
                            }
                        }
                    }
                }
                //
                if (matched) {
                    String perm = plugin.getHandlesConfig().getString("custom-commands." + key + ".permission");
                    if (perm != null && TARDISPermission.hasPermission(player, perm)) {
                        for (String cmd : plugin.getHandlesConfig().getStringList("custom-commands." + key + ".commands")) {
                            if (cmd.contains("%") && plugin.getPM().isPluginEnabled("PlaceholderAPI")) {
                                cmd = TARDISHandlesPlaceholder.getSubstituted(cmd, player);
                            }
                            // process capture groups (backwards so double digits $13 are replaced before single digits $1)
                            if (groups != null) {
                                for (int g = groups.size() - 1; g >= 0; g--) {
                                    String find = "$" + (g + 1);
                                    cmd = cmd.replace(find, groups.get(g));
                                }
                            }
                            boolean isConsoleCommand = cmd.startsWith("^");
                            // strip console character if necessary
                            String command = (isConsoleCommand) ? cmd.substring(1) : cmd;
                            if (isConsoleCommand) {
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), command), 1L);
                            } else {
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand(command), 1L);
                            }
                        }
                    }
                    TARDISSounds.playTARDISSound(player, "handles_confirmed", 5L);
                } else {
                    // don't understand
                    plugin.getMessenger().handlesSend(player, "HANDLES_NO_COMMAND");
                }
            }
        }
    }
}
