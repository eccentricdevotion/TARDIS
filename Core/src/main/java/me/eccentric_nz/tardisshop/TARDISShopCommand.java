/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.tardisshop;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisshop.database.InsertShopItem;
import me.eccentric_nz.tardisshop.database.ResultSetUpdateShop;
import me.eccentric_nz.tardisshop.database.UpdateShopItem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TARDISShopCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("add", "remove", "update");
    private final List<String> ITEM_SUBS;

    public TARDISShopCommand(TARDIS plugin) {
        this.plugin = plugin;
        ITEM_SUBS = new ArrayList<>(this.plugin.getItemsConfig().getKeys(false));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisshop")) {
            // must be a player
            if (sender instanceof Player player) {
                // return if no arguments
                if (args.length < 1) {
                    plugin.getMessenger().send(player, TardisModule.SHOP, "TOO_FEW_ARGS");
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    plugin.getShopSettings().getRemovingItem().add(player.getUniqueId());
                    plugin.getMessenger().send(player, TardisModule.SHOP, "SHOP_REMOVE", plugin.getShopSettings().getBlockMaterial().toString());
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    // reload items.yml
                    File file = new File(plugin.getDataFolder(), "items.yml");
                    try {
                        plugin.getItemsConfig().load(file);
                    } catch (InvalidConfigurationException | IOException e) {
                        plugin.debug("Failed to reload items.yml" + e.getMessage());
                    }
                    // get shop items
                    ResultSetUpdateShop rs = new ResultSetUpdateShop(plugin);
                    if (rs.getAll()) {
                        for (TARDISShopItem item : rs.getShopItems()) {
                            String lookup = item.getItem().replace(" ", "_").toLowerCase(Locale.ROOT);
                            double cost = plugin.getItemsConfig().getDouble(lookup);
                            if (cost != item.getCost()) {
                                // update database
                                new UpdateShopItem(plugin).updateCost(cost, item.getId());
                                // find text display and update the etxt
                                for (Entity e : item.getLocation().getWorld().getNearbyEntities(item.getLocation().add(0.5d, 1.0d, 0.5d), 0.5d, 1.0d, 0.5d)) {
                                    if (e instanceof TextDisplay text) {
                                        text.setText(item.getItem() + "\n" + ChatColor.RED + "Cost:" + ChatColor.RESET + String.format(" %.2f", cost));
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
                // need at least 2 arguments from here on
                if (args.length < 2) {
                    plugin.getMessenger().send(player, TardisModule.SHOP, "TOO_FEW_ARGS");
                    return true;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    String name = args[1].toLowerCase(Locale.ROOT);
                    if (!plugin.getItemsConfig().contains(name)) {
                        plugin.getMessenger().send(player, TardisModule.SHOP, "TOO_FEW_ARGS");
                        return true;
                    }
                    double cost = plugin.getItemsConfig().getDouble(name);
                    TARDISShopItem item = new InsertShopItem(plugin).addNamedItem(TARDISStringUtils.capitalise(args[1]), cost);
                    plugin.getShopSettings().getSettingItem().put(player.getUniqueId(), item);
                    plugin.getMessenger().send(player, TardisModule.SHOP, "SHOP_ADD", plugin.getShopSettings().getBlockMaterial().toString());
                    return true;
                }
                return true;
            } else {
                plugin.getMessenger().send(sender, TardisModule.SHOP, "CMD_NO_CONSOLE");
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equalsIgnoreCase("add")) {
                return partial(lastArg, ITEM_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
