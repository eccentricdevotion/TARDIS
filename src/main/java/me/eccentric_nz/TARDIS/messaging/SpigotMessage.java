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
import me.eccentric_nz.TARDIS.artron.ArtronIndicatorData;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.wiki.WikiLink;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class SpigotMessage implements TARDISMessage {

    @Override
    public void sendJenkinsUpdateReady(CommandSender cs, int current, int latest) {
        cs.spigot().sendMessage(SpigotComponents.getJenkinsUpdateReady(current, latest));
    }

    @Override
    public void sendUpdateCommand(CommandSender cs) {
        cs.spigot().sendMessage(SpigotComponents.getUpdateCommand());
    }

    @Override
    public void sendBuildsBehind(CommandSender cs, int behind) {
        cs.spigot().sendMessage(SpigotComponents.getBuildsBehind(behind));
    }

    @Override
    public void message(CommandSender cs, TardisModule module, String message) {
        if (cs != null) {
            TextComponent textComponent = SpigotComponents.getModule(module);
            TextComponent m = new TextComponent(message);
            m.setColor(ChatColor.WHITE);
            textComponent.addExtra(m);
            cs.spigot().sendMessage(textComponent);
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
    public void send(CommandSender cs, TardisModule module, String key) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        message(cs, module, local);
    }

    @Override
    public void send(CommandSender cs, TardisModule module, String key, Object... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        message(cs, module, String.format(local, subs));
    }

    @Override
    public void send(Player player, String key, boolean handbrake) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        if (handbrake) {
            sendStatus(player, local + " " + TARDIS.plugin.getLanguage().getString("HANDBRAKE_RELEASE"));
        } else {
            sendStatus(player, local + " " + TARDIS.plugin.getLanguage().getString("LEAVING_VORTEX"));
        }
    }

    @Override
    public void broadcast(TardisModule module, String message) {
        TextComponent textComponent = SpigotComponents.getModule(module);
        TextComponent m = new TextComponent(message);
        textComponent.addExtra(m);
        TARDIS.plugin.getServer().broadcast(textComponent);
    }

    @Override
    public void sendWithColour(CommandSender cs, TardisModule module, String message, String hex) {
        if (cs != null) {
            TextComponent textComponent = SpigotComponents.getModule(module);
            TextComponent m = new TextComponent(message);
            ChatColor colour = ChatColor.of(hex);
            m.setColor(colour);
            textComponent.addExtra(m);
            cs.spigot().sendMessage(textComponent);
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

    @Override
    public void sendRequestComehereAccept(CommandSender cs, String key, String command) {
        cs.spigot().sendMessage(SpigotComponents.getRequestComehereAccept(key, command));
    }

    @Override
    public void sendAbandoned(CommandSender cs, int i, String owner, String location, int id) {
        cs.spigot().sendMessage(SpigotComponents.getAbandoned(i, owner, location, id));
    }

    @Override
    public void sendTransmat(CommandSender cs, Transmat t) {
        cs.spigot().sendMessage(SpigotComponents.getTransmat(t));
    }

    @Override
    public void sendTARDISForList(CommandSender cs, Tardis t, String world, int x, int y, int z) {
        cs.spigot().sendMessage(SpigotComponents.getTARDISForList(t, world, x, y, z));
    }

    @Override
    public void sendExterminate(CommandSender cs, TARDIS plugin) {
        cs.spigot().sendMessage(SpigotComponents.getExterminate(plugin));
    }

    @Override
    public void sendRescue(CommandSender cs, TARDIS plugin) {
        cs.spigot().sendMessage(SpigotComponents.getRescue(plugin));
    }

    @Override
    public void sendSuggestCommand(CommandSender cs, String item, String hover, String colour) {
        cs.spigot().sendMessage(SpigotComponents.getSuggestCommand(item, hover, colour));
    }

    @Override
    public void sendRunCommand(CommandSender cs, String item, String hover, String colour) {
        cs.spigot().sendMessage(SpigotComponents.getRunCommand(item, hover, colour));
    }

    @Override
    public void sendShowMore(CommandSender cs, String command) {
        cs.spigot().sendMessage(SpigotComponents.getShowMore(command));
    }

    @Override
    public void sendRecharger(CommandSender cs, String recharger, String world, String x, String y, String z, boolean hasPerm) {
        cs.spigot().sendMessage(SpigotComponents.getRecharger(recharger, world, x, y, z, hasPerm));
    }

    @Override
    public void sendHome(CommandSender cs, TARDIS plugin, String world, int x, int y, int z) {
        cs.spigot().sendMessage(SpigotComponents.getHome(plugin, world, x, y, z));
    }

    @Override
    public void sendSave(CommandSender cs, HashMap<String, String> map, String world) {
        cs.spigot().sendMessage(SpigotComponents.getSave(map, world));
    }

    @Override
    public void sendArea(CommandSender cs, Area a, int n, boolean hasPerm) {
        cs.spigot().sendMessage(SpigotComponents.getArea(a, n, hasPerm));
    }

    @Override
    public void sendRoom(CommandSender cs, String room, boolean hasPerm) {
        cs.spigot().sendMessage(SpigotComponents.getRoom(room, hasPerm));
    }

    @Override
    public void sendRoomGallery(CommandSender cs) {
        cs.spigot().sendMessage(SpigotComponents.getRoomGallery());
    }

    @Override
    public void sendEyebrows(CommandSender cs) {
        cs.spigot().sendMessage(SpigotComponents.getEyebrows());
    }

    @Override
    public void sendSign(CommandSender cs) {
        cs.spigot().sendMessage(SpigotComponents.getSign());
    }

    @Override
    public void sendInfo(CommandSender cs, String first, String value, String split) {
        cs.spigot().sendMessage(SpigotComponents.getUpdate(first, value, split));
    }

    @Override
    public void sendHADS(CommandSender cs, TARDIS plugin) {
        cs.spigot().sendMessage(SpigotComponents.getHADS(plugin));
    }

    @Override
    public void sendColouredCommand(CommandSender cs, String which, String command, TARDIS plugin) {
        cs.spigot().sendMessage(SpigotComponents.getColouredCommand(which, command, plugin));
    }

    @Override
    public void sendInsertedColour(CommandSender cs, String local, String which, TARDIS plugin) {
        cs.spigot().sendMessage(SpigotComponents.getInsertColour(local, which, plugin));
    }

    @Override
    public void sendWithColours(CommandSender cs, String first, String colour, String last, String hue) {
        cs.spigot().sendMessage(SpigotComponents.getWithColours(first, colour, last, hue));
    }

    @Override
    public void sendWithColours(CommandSender cs, TardisModule module, String first, String colour, String last, String hue) {
        cs.spigot().sendMessage(SpigotComponents.getWithColours(module, first, colour, last, hue));
    }

    @Override
    public void sendCommand(CommandSender cs, String root, String command) {
        cs.spigot().sendMessage(SpigotComponents.getCommand(root, command));
    }

    @Override
    public void sendHeadsUpDisplay(Player player, TARDIS plugin) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getUtils().actionBarFormat(player)));
    }

    @Override
    public void sendWikiLink(Player player, WikiLink wikiLink) {
        player.spigot().sendMessage(SpigotComponents.getWikiLink(wikiLink));
    }

    @Override
    public void sendStartBanner(CommandSender cs) {
        for (TextComponent c : SpigotComponents.getStartBanner()) {
            cs.spigot().sendMessage(c);
        }
    }

    @Override
    public void sendProtected(CommandSender cs, String xyz, String location, int id) {
        cs.spigot().sendMessage(SpigotComponents.getRemoveProtected(xyz, location, id));
    }

    @Override
    public void announceRepeater(Player player, String value) {
        player.sendTitle("", value, 5, 20, 5);
    }

    @Override
    public void sendStatus(Player player, String key) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        TextComponent actionBar = SpigotComponents.getModule(TardisModule.TARDIS);
        TextComponent m = new TextComponent(local);
        m.setColor(ChatColor.WHITE);
        actionBar.addExtra(m);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionBar);
    }

    @Override
    public void sendStatus(Player player, String key, Object... subs) {
        String local = String.format(TARDIS.plugin.getLanguage().getString(key), subs);
        TextComponent actionBar = SpigotComponents.getModule(TardisModule.TARDIS);
        TextComponent m = new TextComponent(local);
        m.setColor(ChatColor.WHITE);
        actionBar.addExtra(m);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionBar);
    }

    @Override
    public void sendArtron(Player player, int id, int used) {
        ArtronIndicatorData data = new TARDISArtronIndicator(TARDIS.plugin).getLevels(player, id, used);
        TextComponent actionBar = SpigotComponents.getArtronIndicator(data);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionBar);
    }
}
