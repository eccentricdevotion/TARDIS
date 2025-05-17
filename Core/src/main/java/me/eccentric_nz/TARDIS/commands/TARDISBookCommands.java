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
package me.eccentric_nz.TARDIS.commands;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISBook;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAchievements;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Command /tardisbook [book].
 * <p>
 * On Alfava Metraxis in the 51st century, River had obtained a book about the
 * Weeping Angels. She gave it to the Doctor, who quickly skimmed through it.
 * The Doctor commented that it was slow in the middle and asked River if she
 * also hated the writer's girlfriend. He asked why there were no images, to
 * which River replied that the "image of an Angel is itself an Angel".
 *
 * @author eccentric_nz
 */
public class TARDISBookCommands extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final LinkedHashMap<String, String> books;
    private final List<String> ROOT_SUBS;
    private final List<String> DO_SUBS = ImmutableList.of("get", "start");

    public TARDISBookCommands(TARDIS plugin) {
        this.plugin = plugin;
        books = getAchievements();
        ROOT_SUBS = ImmutableList.copyOf(books.keySet());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisbook then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisbook")) {
            if (args.length < 1) {
                new TARDISCommandHelper(plugin).getCommand("tardisbook", sender);
                return true;
            }
            if (TARDISPermission.hasPermission(sender, "tardis.book")) {
                String first = args[0].toLowerCase(Locale.ROOT);
                if (first.equals("list")) {
                    int b = 1;
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "BOOK_RASS");
                    if (!books.isEmpty()) {
                        for (Map.Entry<String, String> entry : books.entrySet()) {
                            sender.sendMessage(b + ". [" + entry.getKey() + "] - " + entry.getValue());
                            b++;
                        }
                    } else {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "BOOK_NONE");
                    }
                    return true;
                }
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                    return true;
                }
                if (args.length < 2) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BOOK_NEED");
                    return false;
                }
                if (!books.containsKey(first)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BOOK_NOT_FOUND");
                    return true;
                }
                String second = args[1].toLowerCase(Locale.ROOT);
                if (second.equals("get")) {
                    // need to check whether they already have been given the book
                    TARDISBook book = new TARDISBook(plugin);
                    // title, author, filename, player
                    book.writeBook(books.get(first), "Rassilon", first, player);
                    return true;
                }
                if (second.equals("start")) {
                    if (plugin.getAchievementConfig().getBoolean(first + ".auto")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ACHIEVE_AUTO");
                        return true;
                    }
                    // check they have not already started the achievement
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", player.getUniqueId().toString());
                    where.put("name", first);
                    ResultSetAchievements rsa = new ResultSetAchievements(plugin, where);
                    if (rsa.resultSet()) {
                        if (rsa.isCompleted()) {
                            if (!plugin.getAchievementConfig().getBoolean(first + ".repeatable")) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "ACHIEVE_ONCE");
                                return true;
                            }
                        } else {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ACHIEVE_ALREADY_STARTED");
                            return true;
                        }
                    }
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", player.getUniqueId().toString());
                    set.put("name", first);
                    plugin.getQueryFactory().doInsert("achievements", set);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ACHIEVE_STARTED", first);
                    return true;
                }
            }
        }
        return false;
    }

    private LinkedHashMap<String, String> getAchievements() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Set<String> aset = plugin.getAchievementConfig().getRoot().getKeys(false);
        aset.forEach((a) -> {
            if (plugin.getAchievementConfig().getBoolean(a + ".enabled")) {
                String title_reward = plugin.getAchievementConfig().getString(a + ".name") + " - " + plugin.getAchievementConfig().getString(a + ".reward_type") + ":" + plugin.getAchievementConfig().getString(a + ".reward_amount");
                map.put(a, title_reward);
            }
        });
        return map;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];

        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            return partial(lastArg, DO_SUBS);
        }
        return ImmutableList.of();
    }
}
