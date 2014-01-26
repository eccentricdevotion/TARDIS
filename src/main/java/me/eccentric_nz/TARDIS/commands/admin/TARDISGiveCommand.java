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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/**
 *
 * @author eccentric_nz
 */
public class TARDISGiveCommand implements CommandExecutor {

    private final TARDIS plugin;
    private final int full;
    private final HashMap<String, String> items = new HashMap<String, String>();

    public TARDISGiveCommand(TARDIS plugin) {
        this.plugin = plugin;
        this.full = this.plugin.getArtronConfig().getInt("full_charge");
        items.put("artron", "");
        items.put("a-circuit", "Server Admin Circuit");
        items.put("bio-circuit", "Bio-scanner Circuit");
        items.put("biome-disk", "Biome Storage Disk");
        items.put("blank", "Blank Storage Disk");
        items.put("c-circuit", "TARDIS Chameleon Circuit");
        items.put("cell", "Artron Storage Cell");
        items.put("d-circuit", "Diamond Disruptor Circuit");
        items.put("e-circuit", "Emerald Environment Circuit");
        items.put("filter", "Perception Filter");
        items.put("key", "TARDIS Key");
        items.put("kit", "TARDIS Item Kit");
        items.put("l-circuit", "TARDIS Locator Circuit");
        items.put("locator", "TARDIS Locator");
        items.put("m-circuit", "TARDIS Materialisation Circuit");
        items.put("memory-circuit", "TARDIS Memory Circuit");
        items.put("oscillator", "Sonic Oscillator");
        items.put("p-circuit", "Perception Circuit");
        items.put("player-disk", "Player Storage Disk");
        items.put("preset-disk", "Preset Storage Disk");
        items.put("r-circuit", "Redstone Activator Circuit");
        items.put("remote", "Stattenheim Remote");
        items.put("s-circuit", "TARDIS Stattenheim Circuit");
        items.put("scanner-circuit", "TARDIS Scanner Circuit");
        items.put("save-disk", "Save Storage Disk");
        items.put("sonic", "Sonic Screwdriver");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisgive then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisgive")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length < 3) {
                    sender.sendMessage(plugin.pluginName + "Too few command arguments! /tardisgive [player] [item] [amount]");
                    return true;
                }
                String item = args[1].toLowerCase();
                if (!items.containsKey(item)) {
                    sender.sendMessage(plugin.pluginName + "Unknown item! Try one of: artron|kit|a-circuit|bio-circuit|biome-disk|blank|c-circuit|d-circuit|e-circuit|filter|key|l-circuit|locator|m-circuit|memory-circuit|oscillator|p-circuit|player-disk|preset-disk|r-circuit|remote|s-circuit|scanner-circuit|save-disk|sonic");
                    return true;
                }
                if (item.equals("kit")) {
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if (p == null) { // player must be online
                        sender.sendMessage(plugin.pluginName + "Could not find a player with that name!");
                        return true;
                    }
                    if (!plugin.getKitsConfig().contains("kits." + args[2])) {
                        sender.sendMessage(plugin.pluginName + "Could not find a kit with that name!");
                        return true;
                    }
                    for (String k : plugin.getKitsConfig().getStringList("kits." + args[2])) {
                        this.giveItem(k, p);
                    }
                    p.sendMessage(plugin.pluginName + sender.getName() + " just gave you the TARDIS Item Kit " + args[2]);
                    return true;
                }
                int amount;
                if (args[2].equals("full")) {
                    amount = full;
                } else if (args[2].equals("empty")) {
                    amount = 0;
                } else {
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(plugin.pluginName + "Amount must be a number, 'full' or 'empty'! /tardisgive [player] [item] [amount]");
                        return true;
                    }
                }
                if (item.equals("artron")) {
                    if (plugin.getServer().getOfflinePlayer(args[0]) == null) {
                        sender.sendMessage(plugin.pluginName + "Could not find a player with that name!");
                        return true;
                    }
                    return this.giveArtron(sender, args[0], amount);
                } else {
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if (p == null) { // player must be online
                        sender.sendMessage(plugin.pluginName + "Could not find a player with that name!");
                        return true;
                    }
                    return this.giveItem(sender, item, amount, p);
                }
            } else {
                sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
                return true;
            }
        }
        return false;
    }

    private boolean giveItem(CommandSender sender, String item, int amount, Player player) {
        if (amount > 64) {
            sender.sendMessage(plugin.pluginName + "You can only give a maximum of 64 items at once!");
            return true;
        }
        String item_to_give = items.get(item);
        ItemStack result;
        if (item.equals("save-disk") || item.equals("preset-disk") || item.equals("biome-disk") || item.equals("player-disk")) {
            ShapelessRecipe recipe = plugin.incomposita.getShapelessRecipes().get(item_to_give);
            result = recipe.getResult();
        } else {
            ShapedRecipe recipe = plugin.figura.getShapedRecipes().get(item_to_give);
            result = recipe.getResult();
        }
        result.setAmount(amount);
        player.getInventory().addItem(result);
        player.updateInventory();
        player.sendMessage(plugin.pluginName + sender.getName() + " just gave you " + amount + " " + item_to_give);
        return true;
    }

    private boolean giveItem(String item, Player player) {
        ItemStack result;
        if (plugin.incomposita.getShapelessRecipes().containsKey(item)) {
            ShapelessRecipe recipe = plugin.incomposita.getShapelessRecipes().get(item);
            result = recipe.getResult();
        } else {
            ShapedRecipe recipe = plugin.figura.getShapedRecipes().get(item);
            result = recipe.getResult();
        }
        result.setAmount(1);
        player.getInventory().addItem(result);
        player.updateInventory();
        return true;
    }

    private boolean giveArtron(CommandSender sender, String player, int amount) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", player);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int id = rs.getTardis_id();
            int level = rs.getArtron_level();
            int set_level;
            if (amount == 0) {
                set_level = 0;
            } else {
                // always fill to full and no more
                if (level >= full && amount > 0) {
                    sender.sendMessage(plugin.pluginName + player + " already has a full Artron Energy Capacitor!");
                    return true;
                }
                if ((full - level) < amount) {
                    set_level = full;
                } else {
                    set_level = level + amount;
                }
            }
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("artron", set_level);
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", id);
            qf.doUpdate("tardis", set, wheret);
            sender.sendMessage(plugin.pluginName + player + "'s Artron Energy Level was set to " + set_level);
        }
        return true;
    }
}
