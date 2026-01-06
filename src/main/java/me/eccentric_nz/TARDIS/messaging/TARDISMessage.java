/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.artron.ArtronIndicator;
import me.eccentric_nz.TARDIS.artron.ArtronIndicatorData;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.wiki.WikiLink;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISMessage {

    public void message(CommandSender cs, TardisModule module, String message) {
        if (cs != null) {
            TextComponent textComponent = AdventureComponents.getModule(module).append(Component.text(message, NamedTextColor.WHITE));
            cs.sendMessage(textComponent);
        }
    }

    public void message(CommandSender cs, String message) {
            cs.sendMessage(message);
    }

    public void message(CommandSender cs, TardisModule module, Component component) {
        cs.sendMessage(AdventureComponents.getModule(module).append(component));
    }

    public void handlesMessage(Player p, String message) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, message), 2L);
    }

    public void handlesSend(Player p, String key) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, local), 2L);
    }

    public void handlesSend(Player p, String key, Object... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, String.format(local, subs)), 2L);
    }

    public void send(CommandSender cs, TardisModule module, String key) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        message(cs, module, local);
    }

    public void send(CommandSender cs, TardisModule module, String key, Object... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        message(cs, module, String.format(local, subs));
    }

    public void send(CommandSender cs, TardisModule module, String key, Component sub) {
        String local = TARDIS.plugin.getLanguage().getString(key).replace("%s", "");
        TextComponent textComponent = Component.text(local).append(sub);
        message(cs, module, textComponent);
    }

    public void send(Player player, String key, boolean handbrake) {
        if (handbrake) {
            sendJoinedStatus(player, key, "HANDBRAKE_RELEASE");
        } else {
            sendJoinedStatus(player, key, "LEAVING_VORTEX");
        }
    }

    public void sendJoined(Player player, String key, String sub, boolean handbrake) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        String other = (handbrake) ? TARDIS.plugin.getLanguage().getString("HANDBRAKE_RELEASE") : TARDIS.plugin.getLanguage().getString("LEAVING_VORTEX");
        TextComponent actionBar = AdventureComponents.getModule(TardisModule.TARDIS).append(Component.text(String.format(local, sub) + " " + other, NamedTextColor.WHITE));
        player.sendActionBar(actionBar);
    }

    public void broadcast(TardisModule module, String message) {
        message(TARDIS.plugin.getServer(), module, message);
    }

    public void sendWithColour(CommandSender cs, TardisModule module, String message, String hex) {
        TextColor colour = TextColor.fromHexString(hex);
        TextComponent textComponent = AdventureComponents.getModule(module).append(Component.text(message, colour));
        cs.sendMessage(textComponent);
    }

    public void messageWithColour(CommandSender cs, String message, String hex) {
        TextColor colour = TextColor.fromHexString(hex);
        TextComponent textComponent = Component.text(message, colour);
        cs.sendMessage(textComponent);
    }

    public void sendRequestComehereAccept(CommandSender cs, String key, String command) {
        cs.sendMessage(AdventureComponents.getRequestComehereAccept(key, command));
    }

    public void sendAbandoned(CommandSender cs, int i, String owner, String location, int id) {
        cs.sendMessage(AdventureComponents.getAbandoned(i, owner, location, id));
    }

    public void sendTransmat(CommandSender cs, Transmat t) {
        cs.sendMessage(AdventureComponents.getTransmat(t));
    }

    public void sendRoomTransmat(CommandSender cs, Transmat t) {
        cs.sendMessage(AdventureComponents.getRoomTransmat(t));
    }

    public void sendTARDISForList(CommandSender cs, Tardis t, String world, int x, int y, int z) {
        cs.sendMessage(AdventureComponents.getTARDISForList(t, world, x, y, z));
    }

    public void sendExterminate(CommandSender cs, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getExterminate(plugin));
    }

    public void sendRescue(CommandSender cs, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getRescue(plugin));
    }

    public void sendSuggestCommand(CommandSender cs, String item, String hover, String colour) {
        cs.sendMessage(AdventureComponents.getSuggestCommand(item, hover, colour));
    }

    public void sendRunCommand(CommandSender cs, String item, String hover, String colour) {
        cs.sendMessage(AdventureComponents.getRunCommand(item, hover, colour));
    }

    public void sendShowMore(CommandSender cs, String command) {
        cs.sendMessage(AdventureComponents.getShowMore(command));
    }

    public void sendRecharger(CommandSender cs, String recharger, String world, String x, String y, String z, boolean hasPerm) {
        cs.sendMessage(AdventureComponents.getRecharger(recharger, world, x, y, z, hasPerm));
    }

    public void sendHome(CommandSender cs, TARDIS plugin, String world, int x, int y, int z) {
        cs.sendMessage(AdventureComponents.getHome(plugin, world, x, y, z));
    }

    public void sendSave(CommandSender cs, HashMap<String, String> map, String world) {
        cs.sendMessage(AdventureComponents.getSave(map, world));
    }

    public void sendArea(CommandSender cs, Area a, int n, boolean hasPerm) {
        cs.sendMessage(AdventureComponents.getArea(a, n, hasPerm));
    }

    public void sendRoom(CommandSender cs, String room, boolean hasPerm) {
        cs.sendMessage(AdventureComponents.getRoom(room, hasPerm));
    }

    public void sendRoomGallery(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getRoomGallery());
    }

    public void sendEyebrows(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getEyebrows());
    }

    public void sendSign(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getSign());
    }

    public void sendInfo(CommandSender cs, String first, String value, String split) {
        cs.sendMessage(AdventureComponents.getUpdate(first, value, split));
    }

    public void sendHADS(CommandSender cs, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getHADS(plugin));
    }

    public void sendColouredCommand(CommandSender cs, String which, String command, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getColouredCommand(which, command, plugin));
    }

    public void sendInsertedColour(CommandSender cs, String local, String which, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getInsertColour(local, which, plugin));
    }

    public void sendWithColours(CommandSender cs, String first, String colour, String last, String hue) {
        cs.sendMessage(AdventureComponents.getWithColours(first, colour, last, hue));
    }

    public void sendWithColours(CommandSender cs, TardisModule module, String first, String colour, String last, String hue) {
        cs.sendMessage(AdventureComponents.getWithColours(module, first, colour, last, hue));
    }

    public void sendCommand(CommandSender cs, String root, String command) {
        cs.sendMessage(AdventureComponents.getCommand(root, command));
    }

    public void sendHeadsUpDisplay(Player player, TARDIS plugin) {
        TextComponent actionBar = LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getUtils().actionBarFormat(player));
        player.sendActionBar(actionBar);
    }

    public void sendWikiLink(Player player, WikiLink wikiLink) {
        player.sendMessage(AdventureComponents.getWikiLink(wikiLink));
    }

    public void sendStartBanner(CommandSender cs) {
        for (Component c : AdventureComponents.getStartupBanner()) {
            cs.sendMessage(c);
        }
    }

    public void sendProtected(CommandSender cs, String xyz, String location, int id) {
        cs.sendMessage(AdventureComponents.getRemoveProtected(xyz, location, id));
    }

    public void announceRepeater(Player player, String value) {
        Title.Times times = Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(1000), Duration.ofMillis(250));
        Title title = Title.title(Component.empty(), Component.text(value), times);
        player.showTitle(title);
    }

    public void sendStatus(Player player, String key) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        TextComponent actionBar = AdventureComponents.getModule(TardisModule.TARDIS).append(Component.text(local, NamedTextColor.WHITE));
        player.sendActionBar(actionBar);
    }

    public void sendStatus(Player player, String key, Object... subs) {
        String local = String.format(TARDIS.plugin.getLanguage().getString(key), subs);
        TextComponent actionBar = AdventureComponents.getModule(TardisModule.TARDIS).append(Component.text(local, NamedTextColor.WHITE));
        player.sendActionBar(actionBar);
    }

    public void sendArtron(Player player, int id, int used) {
        ArtronIndicatorData data = new ArtronIndicator(TARDIS.plugin).getLevels(player, id, used);
        TextComponent actionBar = AdventureComponents.getArtronIndicator(data);
        player.sendActionBar(actionBar);
    }

    public void sendFind(Player player, String world, int x, int y, int z) {
        String local = TARDIS.plugin.getLanguage().getString("TARDIS_FIND");
        player.sendMessage(AdventureComponents.getFind(local.substring(0, local.length() - 2), world, x, y, z));
    }

    public void sendJoinedStatus(Player player, String key, String otherKey) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        String other = TARDIS.plugin.getLanguage().getString(otherKey);
        TextComponent actionBar = AdventureComponents.getModule(TardisModule.TARDIS).append(Component.text(local + " " + other, NamedTextColor.WHITE));
        player.sendActionBar(actionBar);
    }

    public void message(Audience audience, TardisModule module, String message) {
        if (audience != null) {
            TextComponent textComponent = AdventureComponents.getModule(module).append(Component.text(message, NamedTextColor.WHITE));
            audience.sendMessage(textComponent);
        }
    }
}
