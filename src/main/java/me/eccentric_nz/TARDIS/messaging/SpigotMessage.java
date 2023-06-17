/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class SpigotMessage implements TARDISMessage {

    public final String JENKINS_UPDATE_READY = ChatColor.RED + "There is a new TARDIS build! You are using " + ChatColor.GOLD + "#%s" + ChatColor.RED + ", the latest build is " + ChatColor.GOLD + "#%s" + ChatColor.RED + "!";
    public final String UPDATE_COMMAND = ChatColor.GOLD + "Visit http://tardisjenkins.duckdns.org:8080/job/TARDIS/ or run the '/tardisadmin update_plugins' command";
    public final String BUILDS_BEHIND = "You are %s builds behind! Type " + ChatColor.AQUA + "/tadmin update_plugins" + ChatColor.RESET + " to update!";

    private final String getModule(TardisModule module) {
        ChatColor colour = ChatColor.of(module.getHex());
        return colour + "[" + module.getName() + "]" + ChatColor.RESET + " ";
    }

    @Override
    public void sendJenkinsUpdateReady(CommandSender cs, int current, int latest) {
        message(cs, TardisModule.TARDIS, String.format(JENKINS_UPDATE_READY, current, latest));
    }

    @Override
    public void sendUpdateCommand(CommandSender cs) {
        message(cs, TardisModule.TARDIS, UPDATE_COMMAND);
    }

    @Override
    public void sendBuildsBehind(CommandSender cs, int behind) {
        message(cs, TardisModule.TARDIS, String.format(BUILDS_BEHIND, behind));
    }

    @Override
    public void message(CommandSender cs, TardisModule module, String message) {
        if (cs != null) {
            String concat = getModule(module) + message;
            if (concat.length() > TARDISChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
                String[] multiline = TARDISChatPaginator.wordWrap(concat);
                cs.sendMessage(multiline);
            } else {
                cs.sendMessage(concat);
            }
        }
    }

    @Override
    public void message(CommandSender cs, String message) {
        if (message.length() > TARDISChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
            String[] multiline = TARDISChatPaginator.wordWrap(message);
            cs.sendMessage(multiline);
        } else {
            cs.sendMessage(message);
        }
    }

    @Override
    public void handlesMessage(Player p, String message) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, message), 2L);
    }

    @Override
    public void handlesSend(Player p, String key) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, local), 2L);
    }

    @Override
    public void handlesSend(Player p, String key, Object... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, String.format(local, subs)), 2L);
    }

    @Override
    public void send(CommandSender cs, TardisModule module, String key, boolean handbrake) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        if (handbrake) {
            message(cs, module, local + " " + TARDIS.plugin.getLanguage().getString("HANDBRAKE_RELEASE"));
        } else {
            message(cs, module, local + " " + TARDIS.plugin.getLanguage().getString("LEAVING_VORTEX"));
        }
    }

    @Override
    public void send(CommandSender p, TardisModule module, String key, Object... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        message(p, module, String.format(local, subs));
    }

    @Override
    public void broadcast(TardisModule module, String message) {
        TARDIS.plugin.getServer().broadcastMessage(getModule(module) + message);
    }

    @Override
    public void sendWithColour(CommandSender cs, TardisModule module, String message, String hex) {
        if (cs != null) {
            ChatColor colour = ChatColor.of(hex);
            String concat = getModule(module) + colour + message;
            if (concat.length() > TARDISChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
                String[] multiline = TARDISChatPaginator.wordWrap(concat);
                cs.sendMessage(multiline);
            } else {
                cs.sendMessage(concat);
            }
        }
    }

    @Override
    public void messageWithColour(CommandSender cs, String message, String hex) {
        message = ChatColor.of(hex) + message;
        if (message.length() > TARDISChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
            String[] multiline = TARDISChatPaginator.wordWrap(message);
            cs.sendMessage(multiline);
        } else {
            cs.sendMessage(message);
        }
    }
}
