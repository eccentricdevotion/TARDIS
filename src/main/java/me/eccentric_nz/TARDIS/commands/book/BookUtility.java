package me.eccentric_nz.TARDIS.commands.book;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAchievements;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BookUtility {

    public static void list(TARDIS plugin, CommandSender sender) {
        int b = 1;
        LinkedHashMap<String, String> books = getAchievements();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "BOOK_RASS");
        if (!books.isEmpty()) {
            for (Map.Entry<String, String> entry : books.entrySet()) {
                sender.sendMessage(b + ". [" + entry.getKey() + "] - " + entry.getValue());
                b++;
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "BOOK_NONE");
        }
    }

    public static void start(TARDIS plugin, String first, Player player) {
        if (plugin.getAchievementConfig().getBoolean(first + ".auto")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ACHIEVE_AUTO");
            return;
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
                    return;
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ACHIEVE_ALREADY_STARTED");
                return;
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", player.getUniqueId().toString());
        set.put("name", first);
        plugin.getQueryFactory().doInsert("achievements", set);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ACHIEVE_STARTED", first);
    }

    public static LinkedHashMap<String, String> getAchievements() {
        FileConfiguration books = TARDIS.plugin.getAchievementConfig();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Set<String> aset = books.getRoot().getKeys(false);
        aset.forEach((a) -> {
            if (books.getBoolean(a + ".enabled")) {
                String title_reward = books.getString(a + ".name") + " - " + books.getString(a + ".reward_type") + ":" + books.getString(a + ".reward_amount");
                map.put(a, title_reward);
            }
        });
        return map;
    }
}
