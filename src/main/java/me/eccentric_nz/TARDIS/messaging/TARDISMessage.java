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
package me.eccentric_nz.tardis.messaging;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Send tidy messages.
 *
 * @author eccentric_nz
 */
public class TardisMessage {

    public static final String JENKINS_UPDATE_READY = ChatColor.RED + "There is a new tardis build! You are using " + ChatColor.GOLD + "#%s" + ChatColor.RED + ", the latest build is " + ChatColor.GOLD + "#%s" + ChatColor.RED + "!";
    public static final String UPDATE_COMMAND = ChatColor.GOLD + "Visit http://tardisjenkins.duckdns.org:8080/job/TARDIS/ or run the '/tardisadmin update_plugins' command";
    private static final String HANDLES = ChatColor.BLUE + "[Handles] " + ChatColor.RESET;

    /**
     * Splits a message into multiple lines if it is longer than the guaranteed chat page width.
     *
     * @param p       the player to send the message
     * @param message the message to send
     */
    public static void message(Player p, String message) {
        if (p != null) {
            if (message.length() > TardisChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
                String[] multiline = TardisChatPaginator.wordWrap(message);
                p.sendMessage(multiline);
            } else {
                p.sendMessage(message);
            }
        }
    }

    public static void message(CommandSender cs, String message) {
        if (message.length() > TardisChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
            String[] multiline = TardisChatPaginator.wordWrap(message);
            cs.sendMessage(multiline);
        } else {
            cs.sendMessage(message);
        }
    }

    public static void send(Player p, String key) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        message(p, TardisPlugin.plugin.getPluginName() + local);
    }

    public static void handlesMessage(Player p, String message) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(TardisPlugin.plugin, () -> message(p, HANDLES + message), 2L);
    }

    public static void handlesSend(Player p, String key) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TardisPlugin.plugin, () -> message(p, HANDLES + local), 2L);
    }

    public static void send(Player p, String key, String sub) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        message(p, TardisPlugin.plugin.getPluginName() + String.format(local, sub));
    }

    public static void handlesSend(Player p, String key, String sub) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TardisPlugin.plugin, () -> {
            assert local != null;
            message(p, HANDLES + String.format(local, sub));
        }, 2L);
    }

    public static void send(CommandSender cs, String key) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        message(cs, TardisPlugin.plugin.getPluginName() + local);
    }

    public static void send(CommandSender cs, String key, String sub) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        message(cs, TardisPlugin.plugin.getPluginName() + String.format(local, sub));
    }

    public static void send(Player p, String key, boolean handbrake) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        if (handbrake) {
            message(p, TardisPlugin.plugin.getPluginName() + local + " " + TardisPlugin.plugin.getLanguage().getString("HANDBRAKE_RELEASE"));
        } else {
            message(p, TardisPlugin.plugin.getPluginName() + local + " " + TardisPlugin.plugin.getLanguage().getString("LEAVING_VORTEX"));
        }
    }

    public static void send(Player p, String key, String sub, boolean handbrake) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        if (handbrake) {
            message(p, TardisPlugin.plugin.getPluginName() + String.format(local, sub) + " " + TardisPlugin.plugin.getLanguage().getString("HANDBRAKE_RELEASE"));
        } else {
            message(p, TardisPlugin.plugin.getPluginName() + String.format(local, sub) + " " + TardisPlugin.plugin.getLanguage().getString("LEAVING_VORTEX"));
        }
    }

    public static void send(Player p, String key, String one, String two) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        message(p, TardisPlugin.plugin.getPluginName() + String.format(local, one, two));
    }

    public static void send(CommandSender cs, String key, String one, String two) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        message(cs, TardisPlugin.plugin.getPluginName() + String.format(local, one, two));
    }

    public static void send(Player p, String key, String one, String two, String three) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        message(p, TardisPlugin.plugin.getPluginName() + String.format(local, one, two, three));
    }

    public static void handlesSend(Player p, String key, long one, String two, String three) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        message(p, HANDLES + String.format(local, one, two, three));
    }

    public static void send(CommandSender cs, String key, String one, int two, int three) {
        String local = TardisPlugin.plugin.getLanguage().getString(key);
        assert local != null;
        message(cs, TardisPlugin.plugin.getPluginName() + String.format(local, one, two, three));
    }
}
