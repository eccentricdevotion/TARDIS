/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.commands.TardisRecipeTabComplete;
import me.eccentric_nz.tardis.commands.handles.TardisHandlesTeleportCommand;
import me.eccentric_nz.tardis.commands.handles.TardisHandlesTransmatCommand;
import me.eccentric_nz.tardis.control.TardisLightSwitch;
import me.eccentric_nz.tardis.control.TardisPowerButton;
import me.eccentric_nz.tardis.control.TardisRandomButton;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TardisHandlesRequest {

    private final TardisPlugin plugin;
    private final ItemStack handles;
    private final Pattern handlesPattern;

    public TardisHandlesRequest(TardisPlugin plugin) {
        this.plugin = plugin;
        handles = getHandles();
        handlesPattern = TardisHandlesPattern.getPattern("prefix", false);
    }

    public void process(UUID uuid, String chat) {
        // check if player is inside / outside TARDIS
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return;
        }
        if (!TardisPermission.hasPermission(player, "tardis.handles.use")) {
            TardisMessage.send(player, "NO_PERMS");
            return;
        }
        // must have a TARDIS
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(uuid.toString())) {
            int id = rs.getTardisId();
            // check for placed Handles
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("type", 26);
            ResultSetControls rsc = new ResultSetControls(plugin, where, false);
            if (rsc.resultSet()) {
                // if placed player must be in TARDIS or be wearing a communicator
                if (!plugin.getUtils().inTARDISWorld(player)) {
                    // player must have communicator
                    if (!TardisPermission.hasPermission(player, "tardis.handles.communicator")) {
                        TardisMessage.send(player, "NO_PERM_COMMUNICATOR");
                        return;
                    }
                    PlayerInventory pi = player.getInventory();
                    ItemStack communicator = pi.getHelmet();
                    if (communicator == null || !communicator.hasItemMeta() || !communicator.getType().equals(Material.BIRCH_BUTTON) || !Objects.requireNonNull(communicator.getItemMeta()).getDisplayName().equals("TARDIS Communicator")) {
                        TardisMessage.send(player, "HANDLES_COMMUNICATOR");
                        return;
                    }
                }
            } else {
                // Handles must be in inventory
                if (!player.getInventory().contains(handles)) {
                    TardisMessage.send(player, "HANDLES_INVENTORY");
                    return;
                }
            }
            // remove the prefix
            String removed = chat.replaceAll("(?i)" + handlesPattern, "").trim();
            // loop through handles config
            boolean matched = false;
            String key = "";
            List<String> groups = null;
            for (String k : Objects.requireNonNull(plugin.getHandlesConfig().getConfigurationSection("core-commands")).getKeys(true)) {
                if (!k.equals("travel") && !k.equals("door")) {
                    Pattern pattern = TardisHandlesPattern.getPattern(k, false);
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
                    case "craft":
                        if (groups != null) {
                            String tardis = groups.get(1);
                            if (tardis == null || tardis.isEmpty()) {
                                // tardis recipes
                                for (String item : TardisRecipeTabComplete.ROOT_SUBS) {
                                    if (groups.get(2).equalsIgnoreCase(item)) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardisrecipe " + item), 1L);
                                        return;
                                    }
                                }
                            } else {
                                // tardis seed block
                                for (String seed : TardisRecipeTabComplete.TARDIS_TYPES) {
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
                            TardisMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                        }
                        break;
                    case "remind":
                        if (!plugin.getHandlesConfig().getBoolean("reminders.enabled")) {
                            TardisMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                            return;
                        }
                        if (groups != null) {
                            String reminder = groups.get(0);
                            String time = groups.get(1);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles remind " + uuid + " " + reminder + " " + time), 1L);
                            return;
                        }
                        break;
                    case "say":
                        if (groups != null) {
                            String g = groups.get(0);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles say " + uuid + " " + StringUtils.normalizeSpace(g)), 1L);
                        }
                        break;
                    case "name":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles name " + uuid), 1L);
                        break;
                    case "time":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles time " + uuid), 1L);
                        break;
                    case "call":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles call " + uuid + " " + id), 1L);
                        break;
                    case "takeoff":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles takeoff " + uuid + " " + id), 1L);
                        break;
                    case "land":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles land " + uuid + " " + id), 1L);
                        break;
                    case "hide":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis hide"), 1L);
                        break;
                    case "rebuild":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis rebuild"), 1L);
                        break;
                    case "direction":
                        if (groups != null) {
                            CardinalDirection direction = null;
                            if (groups.get(0).equalsIgnoreCase("east")) {
                                direction = CardinalDirection.EAST;
                            } else if (groups.get(0).equalsIgnoreCase("north")) {
                                direction = CardinalDirection.NORTH;
                            } else if (groups.get(0).equalsIgnoreCase("west")) {
                                direction = CardinalDirection.WEST;
                            } else if (groups.get(0).equalsIgnoreCase("south")) {
                                direction = CardinalDirection.SOUTH;
                            }
                            if (direction != null) {
                                String d = direction.toString();
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis direction " + d), 1L);
                            }
                        }
                        break;
                    case "lights":
                        if (groups != null) {
                            boolean onoff = groups.get(0).equalsIgnoreCase("on");
                            // get tardis
                            HashMap<String, Object> wherel = new HashMap<>();
                            wherel.put("tardis_id", id);
                            ResultSetTardis rst = new ResultSetTardis(plugin, wherel, "", false, 2);
                            if (rst.resultSet()) {
                                Tardis tardis = rst.getTardis();
                                if ((onoff && !tardis.isLightsOn()) || (!onoff && tardis.isLightsOn())) {
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisLightSwitch(plugin, id, tardis.isLightsOn(), player, tardis.getSchematic().hasLanterns()).flickSwitch(), 1L);
                                }
                            }
                        }
                        break;
                    case "power":
                        if (groups != null) {
                            boolean onoff = groups.get(0).equalsIgnoreCase("off");
                            // get tardis
                            HashMap<String, Object> wherel = new HashMap<>();
                            wherel.put("tardis_id", id);
                            ResultSetTardis rst = new ResultSetTardis(plugin, wherel, "", false, 2);
                            if (rst.resultSet()) {
                                Tardis tardis = rst.getTardis();
                                if ((onoff && tardis.isPowered()) || (!onoff && !tardis.isPowered())) {
                                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPowered(), tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().hasLanterns()).clickButton(), 1L);
                                    }
                                }
                            }
                        }
                        break;
                    case "travel.save":
                        if (groups != null) {
                            HashMap<String, Object> wheres = new HashMap<>();
                            wheres.put("tardis_id", id);
                            ResultSetDestinations rsd = new ResultSetDestinations(plugin, wheres, true);
                            if (rsd.resultSet()) {
                                for (HashMap<String, String> map : rsd.getData()) {
                                    String dest = map.get("dest_name");
                                    if (groups.get(0).equalsIgnoreCase(dest) && TardisPermission.hasPermission(player, "tardis.timetravel")) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel dest " + dest), 1L);
                                        return;
                                    }
                                }
                            }
                        }
                        break;
                    case "travel.home":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel home"), 1L);
                        break;
                    case "travel.random":
                        // get tardis
                        HashMap<String, Object> wherel = new HashMap<>();
                        wherel.put("tardis_id", id);
                        ResultSetTardis rsr = new ResultSetTardis(plugin, wherel, "", false, 2);
                        if (rsr.resultSet()) {
                            Tardis tardis = rsr.getTardis();
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisRandomButton(plugin, player, id, tardis.getArtronLevel(), 0, tardis.getCompanions(), tardis.getUuid()).clickButton(), 1L);
                            return;
                        }
                        break;
                    case "travel.player":
                        if (groups != null) {
                            if (!TardisPermission.hasPermission(player, "tardis.timetravel.player")) {
                                TardisMessage.handlesSend(player, "NO_PERM_PLAYER");
                                return;
                            }
                            for (Player p : plugin.getServer().getOnlinePlayers()) {
                                String name = p.getName();
                                if (groups.get(0).equalsIgnoreCase(name)) {
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel " + name), 1L);
                                    return;
                                }
                                // don't understand
                                TardisMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                            }
                        }
                        break;
                    case "travel.area":
                        if (groups != null) {
                            String area = (groups.get(0) == null || groups.get(0).isEmpty()) ? groups.get(1) : groups.get(0);
                            ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
                            if (rsa.resultSet()) {
                                // cycle through areas
                                for (String name : rsa.getNames()) {
                                    if (area.equalsIgnoreCase(name) && (TardisPermission.hasPermission(player, "tardis.area." + name) || TardisPermission.hasPermission(player, "tardis.area.*"))) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel area " + name), 1L);
                                        return;
                                    }
                                }
                                // don't understand
                                TardisMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                            }
                        }
                        break;
                    case "travel.biome":
                        if (groups != null) {
                            if (!TardisPermission.hasPermission(player, "tardis.timetravel.biome")) {
                                TardisMessage.handlesSend(player, "TRAVEL_NO_PERM_BIOME");
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
                            TardisMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                        }
                        break;
                    case "travel.cave":
                        if (!TardisPermission.hasPermission(player, "tardis.timetravel.cave")) {
                            TardisMessage.handlesSend(player, "TRAVEL_NO_PERM_CAVE");
                            return;
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel cave"), 1L);
                        break;
                    case "travel.village":
                        if (!TardisPermission.hasPermission(player, "tardis.timetravel.village")) {
                            TardisMessage.handlesSend(player, "TRAVEL_NO_PERM_VILLAGE");
                            return;
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardistravel village"), 1L);
                        break;
                    case "door.open":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis door OPEN"), 1L);
                        break;
                    case "door.close":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardis door CLOSE"), 1L);
                        break;
                    case "door.lock":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles lock " + uuid + " " + id + " true"), 1L);
                        break;
                    case "door.unlock":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles unlock " + uuid + " " + id + " false"), 1L);
                        break;
                    case "scan":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().dispatchCommand(plugin.getConsole(), "handles scan " + uuid + " " + id), 1L);
                        break;
                    case "teleport":
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisHandlesTeleportCommand(plugin).beamMeUp(player), 1L);
                        break;
                    case "transmat":
                        if (groups != null) {
                            if (!TardisPermission.hasPermission(player, "tardis.transmat")) {
                                TardisMessage.handlesSend(player, "NO_PERMS");
                                return;
                            }
                            Location location = player.getLocation();
                            // must be in their TARDIS
                            if (!plugin.getUtils().inTARDISWorld(location)) {
                                TardisMessage.handlesSend(player, "HANDLES_NO_TRANSMAT_WORLD");
                                return;
                            }
                            ResultSetHandlesTransmat rst = new ResultSetHandlesTransmat(plugin, id);
                            if (rst.findSite(groups.get(0))) {
                                new TardisHandlesTransmatCommand(plugin).siteToSiteTransport(player, rst.getLocation());
                            } else {
                                TardisMessage.handlesSend(player, "HANDLES_NO_TRANSMAT");
                            }
                        }
                        break;
                    default:
                        break;
                }
                if (key.equals("remind")) {
                    TardisSounds.playTARDISSound(player, "handles_confirmed", 5L);
                }
            } else {
                // try custom-commands
                for (String k : Objects.requireNonNull(plugin.getHandlesConfig().getConfigurationSection("custom-commands")).getKeys(false)) {
                    Pattern pattern = TardisHandlesPattern.getPattern(k, true);
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
                    if (perm != null && TardisPermission.hasPermission(player, perm)) {
                        for (String cmd : plugin.getHandlesConfig().getStringList("custom-commands." + key + ".commands")) {
                            if (cmd.contains("%") && plugin.getPM().isPluginEnabled("PlaceholderAPI")) {
                                cmd = TardisHandlesPlaceholder.getSubstituted(cmd, player);
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
                    TardisSounds.playTARDISSound(player, "handles_confirmed", 5L);
                } else {
                    // don't understand
                    TardisMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                }
            }
        }
    }

    private ItemStack getHandles() {
        ItemStack is = new ItemStack(Material.BIRCH_BUTTON);
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setDisplayName("Handles");
        im.setLore(Arrays.asList("Cyberhead from the", "Maldovar Market"));
        is.setItemMeta(im);
        return is;
    }
}
