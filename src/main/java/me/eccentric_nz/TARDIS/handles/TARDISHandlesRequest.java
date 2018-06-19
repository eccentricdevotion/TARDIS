/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.TARDISRecipeTabComplete;
import me.eccentric_nz.TARDIS.commands.handles.TARDISHandlesTeleportCommand;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class TARDISHandlesRequest {

    private final TARDIS plugin;
    private final ItemStack handles;

    public TARDISHandlesRequest(TARDIS plugin) {
        this.plugin = plugin;
        handles = getHandles();
    }

    public void process(UUID uuid, String chat) {
        // check if player is inside / outside TARDIS
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return;
        }
        if (!player.hasPermission("tardis.handles.use")) {
            TARDISMessage.send(player, "NO_PERMS");
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
                    if (!player.hasPermission("tardis.handles.communicator")) {
                        TARDISMessage.send(player, "NO_PERM_COMMUNICATOR");
                        return;
                    }
                    PlayerInventory pi = player.getInventory();
                    ItemStack communicator = pi.getHelmet();
                    if (communicator == null || !communicator.hasItemMeta() || !communicator.getType().equals(Material.LEATHER_HELMET) || !communicator.getItemMeta().getDisplayName().equals("TARDIS Communicator")) {
                        TARDISMessage.send(player, "HANDLES_COMMUNICATOR");
                        return;
                    }
                }
            } else {
                // Handles must be in inventory
                if (!player.getInventory().contains(handles)) {
                    TARDISMessage.send(player, "HANDLES_INVENTORY");
                    return;
                }
            }
            // remove the prefix
            String removed = chat.replaceAll("(?i)" + Pattern.quote(plugin.getConfig().getString("handles.prefix")), "").trim();
            List<String> split = Arrays.asList(removed.toLowerCase().split(" "));
            if (split.contains("craft")) {
                if (split.contains("tardis")) {
                    for (String seed : TARDISRecipeTabComplete.TARDIS_TYPES) {
                        if (split.contains(seed)) {
                            player.performCommand("tardisrecipe tardis " + seed);
                            return;
                        }
                    }
                    // default to budget
                    player.performCommand("tardisrecipe tardis budget");
                } else {
                    for (String item : TARDISRecipeTabComplete.ROOT_SUBS) {
                        if (split.contains(item)) {
                            player.performCommand("tardisrecipe " + item);
                            return;
                        }
                    }
                    // don't understand
                    TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                }
            } else if (split.contains("remind")) {
                if (!plugin.getConfig().getBoolean("handles.reminders.enabled")) {
                    TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                    return;
                }
                // remove 'remind me to '
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles remind " + uuid.toString() + " " + StringUtils.normalizeSpace(removed.replaceAll("(?i)" + Pattern.quote("remind"), "").replaceAll("(?i)" + Pattern.quote("me to"), "")));
            } else if (split.contains("say")) {
                // remove 'say '
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles say " + uuid.toString() + " " + StringUtils.normalizeSpace(removed.replaceAll("(?i)" + Pattern.quote("say"), "")));
            } else if (split.contains("name") || split.contains("name?")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles name " + uuid.toString());
            } else if (split.contains("time") || split.contains("time?")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles time " + uuid.toString());
            } else if (split.contains("call")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles call " + uuid.toString() + " " + id);
            } else if (split.contains("takeoff")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles takeoff " + uuid.toString() + " " + id);
            } else if (split.contains("land")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles land " + uuid.toString() + " " + id);
            } else if (split.contains("lock")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles lock " + uuid.toString() + " " + id + " true");
            } else if (split.contains("unlock")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles unlock " + uuid.toString() + " " + id + " false");
            } else if (split.contains("hide")) {
                player.performCommand("tardis hide");
            } else if (split.contains("rebuild")) {
                player.performCommand("tardis rebuild");
            } else if (split.contains("travel")) {
                if (split.contains("save")) {
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("tardis_id", id);
                    ResultSetDestinations rsd = new ResultSetDestinations(plugin, wheres, true);
                    if (rsd.resultSet()) {
                        for (HashMap<String, String> map : rsd.getData()) {
                            String dest = map.get("dest_name");
                            if (split.contains(dest) && player.hasPermission("tardis.timetravel")) {
                                player.performCommand("tardistravel dest " + dest);
                                return;
                            }
                        }
                    }
                } else if (split.contains("player")) {
                    if (!player.hasPermission("tardis.timetravel.player")) {
                        TARDISMessage.handlesSend(player, "NO_PERM_PLAYER");
                        return;
                    }
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        String name = p.getName();
                        if (split.contains(name)) {
                            player.performCommand("tardistravel " + name);
                            return;
                        }
                        // don't understand
                        TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                    }
                } else if (split.contains("area")) {
                    ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
                    if (rsa.resultSet()) {
                        // cycle through areas
                        rsa.getNames().forEach((name) -> {
                            if (split.contains(name) && (player.hasPermission("tardis.area." + name) || player.hasPermission("tardis.area.*"))) {
                                player.performCommand("tardistravel area " + name);
                                return;
                            }
                        });
                        // don't understand
                        TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                    }
                } else if (split.contains("biome")) {
                    if (!player.hasPermission("tardis.timetravel.biome")) {
                        TARDISMessage.handlesSend(player, "TRAVEL_NO_PERM_BIOME");
                        return;
                    }
                    // cycle through biomes
                    for (Biome biome : Biome.values()) {
                        String b = biome.toString();
                        if (split.contains(b)) {
                            player.performCommand("tardistravel biome " + b);
                            return;
                        }
                    }
                    // don't understand
                    TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                } else if (split.contains("home")) {
                    // travel home
                    player.performCommand("tardistravel home");
                    return;
                } else {
                    // don't understand
                    TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                }
            } else if (split.contains("scan")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles scan " + uuid.toString() + " " + id);
            } else if (split.contains("teleport")) {
                new TARDISHandlesTeleportCommand(plugin).beamMeUp(player);
            } else {
                // don't understand
                TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
            }
        }
    }

    private ItemStack getHandles() {
        ItemStack is = new ItemStack(Material.BIRCH_BUTTON);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Handles");
        im.setLore(Arrays.asList("Cyberhead from the", "Maldovar Market"));
        is.setItemMeta(im);
        return is;
    }
}
