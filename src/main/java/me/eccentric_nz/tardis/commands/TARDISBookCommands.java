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
package me.eccentric_nz.tardis.commands;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advancement.TardisBook;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetAdvancements;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Command /tardisbook [book].
 * <p>
 * On Alfava Metraxis in the 51st century, River had obtained a book about the Weeping Angels. She gave it to the
 * Doctor, who quickly skimmed through it. The Doctor commented that it was slow in the middle and asked River if she
 * also hated the writer's girlfriend. He asked why there were no images, to which River replied that the "image of an
 * Angel is itself an Angel".
 *
 * @author eccentric_nz
 */
public class TardisBookCommands extends TardisCompleter implements CommandExecutor, TabCompleter {

    private final TardisPlugin plugin;
    private final LinkedHashMap<String, String> books;
    private final List<String> ROOT_SUBS;
    private final List<String> DO_SUBS = ImmutableList.of("get", "start");

    public TardisBookCommands(TardisPlugin plugin) {
        this.plugin = plugin;
        books = getAchievements();
        ROOT_SUBS = ImmutableList.copyOf(books.keySet());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisbook then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisbook")) {
            if (args.length < 1) {
                new TardisCommandHelper(plugin).getCommand("tardisbook", sender);
                return true;
            }
            if (TardisPermission.hasPermission(sender, "tardis.book")) {
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (first.equals("list")) {
                    int b = 1;
                    TardisMessage.send(sender, "BOOK_RASS");
                    if (books.size() > 0) {
                        for (Map.Entry<String, String> entry : books.entrySet()) {
                            sender.sendMessage(b + ". [" + entry.getKey() + "] - " + entry.getValue());
                            b++;
                        }
                    } else {
                        sender.sendMessage(Objects.requireNonNull(plugin.getLanguage().getString("BOOK_NONE")));
                    }
                    return true;
                }
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player == null) {
                    TardisMessage.send(sender, "CMD_PLAYER");
                    return true;
                }
                if (args.length < 2) {
                    TardisMessage.send(player, "BOOK_NEED");
                    return false;
                }
                if (!books.containsKey(first)) {
                    TardisMessage.send(player, "BOOK_NOT_FOUND");
                    return true;
                }
                String second = args[1].toLowerCase(Locale.ENGLISH);
                if (second.equals("get")) {
                    // need to check whether they already have been given the book
                    TardisBook book = new TardisBook(plugin);
                    // title, author, filename, player
                    book.writeBook(books.get(first), "Rassilon", first, player);
                    return true;
                }
                if (second.equals("start")) {
                    if (plugin.getAdvancementConfig().getBoolean(first + ".auto")) {
                        TardisMessage.send(player, "ACHIEVE_AUTO");
                        return true;
                    }
                    // check they have not already started the achievement
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", player.getUniqueId().toString());
                    where.put("name", first);
                    ResultSetAdvancements rsa = new ResultSetAdvancements(plugin, where, false);
                    if (rsa.resultSet()) {
                        if (rsa.isCompleted()) {
                            if (!plugin.getAdvancementConfig().getBoolean(first + ".repeatable")) {
                                TardisMessage.send(player, "ACHIEVE_ONCE");
                                return true;
                            }
                        } else {
                            TardisMessage.send(player, "ACHIEVE_ALREADY_STARTED");
                            return true;
                        }
                    }
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", player.getUniqueId().toString());
                    set.put("name", first);
                    plugin.getQueryFactory().doInsert("achievements", set);
                    TardisMessage.send(player, "ACHIEVE_STARTED", first);
                    return true;
                }
            }
        }
        return false;
    }

    private LinkedHashMap<String, String> getAchievements() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Set<String> aset = Objects.requireNonNull(plugin.getAdvancementConfig().getRoot()).getKeys(false);
        aset.forEach((a) -> {
            if (plugin.getAdvancementConfig().getBoolean(a + ".enabled")) {
                String title_reward = plugin.getAdvancementConfig().getString(a + ".name") + " - " + plugin.getAdvancementConfig().getString(a + ".reward_type") + ":" + plugin.getAdvancementConfig().getString(a + ".reward_amount");
                map.put(a, title_reward);
            }
        });
        return map;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];

        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            return partial(lastArg, DO_SUBS);
        }
        return ImmutableList.of();
    }
}
