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
package me.eccentric_nz.TARDIS.commands;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISBook;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardisbook [book].
 *
 * On Alfava Metraxis in the 51st century, River had obtained a book about the
 * Weeping Angels. She gave it to the Doctor, who quickly skimmed through it.
 * The Doctor commented that it was slow in the middle and asked River if she
 * also hated the writer's girlfriend. He asked why there were no images, to
 * which River replied that the "image of an Angel is itself an Angel".
 *
 * @author eccentric_nz
 */
public class TARDISBookCommands implements CommandExecutor {

    private TARDIS plugin;
    LinkedHashMap<String, String> books;

    public TARDISBookCommands(TARDIS plugin) {
        this.plugin = plugin;
        this.books = getAchievements();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisbook then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisbook")) {
            if (sender.hasPermission("tardis.book")) {
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (first.equals("list")) {
                    int b = 1;
                    sender.sendMessage(TARDIS.plugin.pluginName + "The books of Rassilon");
                    for (Map.Entry<String, String> entry : books.entrySet()) {
                        sender.sendMessage(b + ". [" + entry.getKey() + "] - " + entry.getValue());
                        b++;
                    }
                    return true;
                }
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player == null) {
                    sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(plugin.pluginName + "You need to specify a book name!");
                    return false;
                }
                String bookname = args[1].toLowerCase(Locale.ENGLISH);
                if (!books.containsKey(bookname)) {
                    sender.sendMessage(plugin.pluginName + "Could not find that book!");
                    return true;
                }
                if (first.equals("get")) {
                    // need to check whether they already have been given the book
                    TARDISBook book = new TARDISBook(plugin);
                    // title, author, filename, player
                    book.writeBook(books.get(bookname), "Rassilon", bookname, player);
                    return true;
                }
                if (first.equals("start")) {
                    if (plugin.ayml.getBoolean(bookname + ".auto")) {
                        sender.sendMessage(plugin.pluginName + "This achievement is awarded automatically!");
                        return true;
                    }
                    // check they have not already started the achievement
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", player.getName());
                    where.put("name", bookname);
                    ResultSetAchievements rsa = new ResultSetAchievements(plugin, where, false);
                    if (rsa.resultSet()) {
                        if (rsa.isCompleted()) {
                            if (!plugin.ayml.getBoolean(bookname + ".repeatable")) {
                                sender.sendMessage(plugin.pluginName + "This achievement can only be gained once!");
                                return true;
                            }
                        } else {
                            sender.sendMessage(plugin.pluginName + "You have already started this achievement!");
                            return true;
                        }
                    }
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("player", player.getName());
                    set.put("name", bookname);
                    QueryFactory qf = new QueryFactory(plugin);
                    qf.doInsert("achievements", set);
                    sender.sendMessage(plugin.pluginName + "Achievement '" + bookname + "' started!");
                    return true;
                }
            }
        }
        return false;
    }

    private LinkedHashMap<String, String> getAchievements() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Set<String> aset = plugin.ayml.getRoot().getKeys(false);
        for (String a : aset) {
            if (plugin.ayml.getBoolean(a + ".enabled")) {
                String title_reward = plugin.ayml.getString(a + ".name") + " - " + plugin.ayml.getString(a + ".reward_type") + ":" + plugin.ayml.getString(a + ".reward_amount");
                map.put(a, title_reward);
            }
        }
        return map;
    }
}
