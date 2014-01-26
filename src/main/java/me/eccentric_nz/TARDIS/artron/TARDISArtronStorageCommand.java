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
package me.eccentric_nz.TARDIS.artron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArtronStorageCommand implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<String>();

    public TARDISArtronStorageCommand(TARDIS plugin) {
        this.plugin = plugin;
        this.firstArgs.add("tardis");
        this.firstArgs.add("timelord");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisartron then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisartron")) {
            if (!sender.hasPermission("tardis.store")) {
                sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + "This command can only be run by a player");
                return true;
            }
            ItemStack is = player.getItemInHand();
            if (is == null || !is.hasItemMeta()) {
                sender.sendMessage(plugin.pluginName + "You must be holding an Artron Storage Cell in your hand!");
                return true;
            }
            if (is.getAmount() > 1) {
                sender.sendMessage(plugin.pluginName + "You can only charge 1 Artron Storage Cell at a time!");
                return true;
            }
            ItemMeta im = is.getItemMeta();
            String name = im.getDisplayName();
            if (!name.equals("Artron Storage Cell")) {
                sender.sendMessage(plugin.pluginName + "You must be holding an Artron Storage Cell in your hand!");
                return true;
            }
            String which = args[0].toLowerCase(Locale.ENGLISH);
            if (!firstArgs.contains(which)) {
                sender.sendMessage(plugin.pluginName + "You must specify 'tardis' or 'timelord' energy to transfer!");
                return false;
            }
            // must be a timelord
            String playerNameStr = player.getName();
            int current_level;
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            if (which.equals("tardis")) {
                wheret.put("owner", playerNameStr);
                ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                if (!rs.resultSet()) {
                    sender.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS.getText());
                    return true;
                }
                current_level = rs.getArtron_level();
            } else {
                wheret.put("player", playerNameStr);
                ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, wheret);
                if (!rs.resultSet()) {
                    sender.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS.getText());
                    return true;
                }
                current_level = rs.getArtronLevel();
            }
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException n) {
                sender.sendMessage(plugin.pluginName + "The second command argument must be a number!");
                return false;
            }
            // must have sufficient energy
            if (which.equals("tardis")) {
                if (current_level - amount < plugin.getArtronConfig().getInt("comehere")) {
                    sender.sendMessage(plugin.pluginName + "You cannot transfer that much energy, you must have enough left to call your TARDIS!");
                    return true;
                }
            } else {
                if (current_level - amount < 0) {
                    sender.sendMessage(plugin.pluginName + "You do not have that much Time Lord energy to transfer!");
                    return true;
                }
            }
            List<String> lore = im.getLore();
            int level = plugin.utils.parseInt(lore.get(1));
            int new_amount = amount + level;
            int max = plugin.getArtronConfig().getInt("full_charge");
            if (new_amount > max) {
                sender.sendMessage(plugin.pluginName + "You cannot overcharge this cell, transfer " + (max - level) + ", or use an empty cell!");
                return false;
            }
            lore.set(1, "" + new_amount);
            im.setLore(lore);
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            is.setItemMeta(im);
            // remove the energy from the tardis/timelord
            HashMap<String, Object> where = new HashMap<String, Object>();
            String table;
            if (which.equals("tardis")) {
                where.put("owner", playerNameStr);
                table = "tardis";
            } else {
                where.put("player", playerNameStr);
                table = "player_prefs";
            }
            new QueryFactory(plugin).alterEnergyLevel(table, -amount, where, player);
            sender.sendMessage(plugin.pluginName + "Artron Storage Cell charged to " + new_amount);
            return true;
        }
        return false;
    }
}
