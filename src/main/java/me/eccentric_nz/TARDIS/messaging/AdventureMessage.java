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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class AdventureMessage implements TARDISMessage {

    @Override
    public void sendJenkinsUpdateReady(CommandSender cs, int current, int latest) {
        cs.sendMessage(AdventureComponents.getJenkinsUpdateReady(current, latest));
    }

    @Override
    public void sendUpdateCommand(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getUpdateCommand());
    }

    @Override
    public void sendBuildsBehind(CommandSender cs, int behind) {
        cs.sendMessage(AdventureComponents.getBuildsBehind(behind));
    }

    public void message(Audience audience, TardisModule module, String message) {
        if (audience != null) {
            TextComponent textComponent = AdventureComponents.getModule(module)
                    .append(Component.text(message, NamedTextColor.WHITE));
            audience.sendMessage(textComponent);
        }
    }

    @Override
    public void message(CommandSender cs, TardisModule module, String message) {
        if (cs != null) {
            TextComponent textComponent = AdventureComponents.getModule(module)
                    .append(Component.text(message, NamedTextColor.WHITE));
            cs.sendMessage(textComponent);
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
    public void send(CommandSender cs, TardisModule module, String key, Object... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        message(cs, module, String.format(local, subs));
    }

    @Override
    public void broadcast(TardisModule module, String message) {
        message(TARDIS.plugin.getServer(), module, message);
    }

    @Override
    public void sendWithColour(CommandSender cs, TardisModule module, String message, String hex) {
        TextColor colour = TextColor.fromHexString(hex);
        TextComponent textComponent = AdventureComponents.getModule(module)
                .append(Component.text(message, colour));
        cs.sendMessage(textComponent);
    }

    @Override
    public void messageWithColour(CommandSender cs, String message, String hex) {
        TextColor colour = TextColor.fromHexString(hex);
        TextComponent textComponent = Component.text(message, colour);
        cs.sendMessage(textComponent);
    }

    @Override
    public void sendRequestComehereAccept(CommandSender cs, String key, String command) {
        cs.sendMessage(AdventureComponents.getRequestComehereAccept(key, command));
    }

    @Override
    public void sendAbandoned(CommandSender cs, int i, String owner, String location, int id) {
        cs.sendMessage(AdventureComponents.getAbandoned(i, owner, location, id));
    }

    @Override
    public void sendTransmat(CommandSender cs, Transmat t) {
        cs.sendMessage(AdventureComponents.getTransmat(t));
    }

    @Override
    public void sendTARDISForList(CommandSender cs, Tardis t, String world, int x, int y, int z) {
        cs.sendMessage(AdventureComponents.getTARDISForList(t, world, x, y, z));
    }

    @Override
    public void sendExterminate(CommandSender cs, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getExterminate(plugin));
    }

    @Override
    public void sendRescue(CommandSender cs, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getRescue(plugin));
    }

    @Override
    public void sendSuggestCommand(CommandSender cs, String item, String hover, String colour) {
        cs.sendMessage(AdventureComponents.getSuggestCommand(item, hover, colour));
    }

    @Override
    public void sendRunCommand(CommandSender cs, String item, String hover, String colour) {
        cs.sendMessage(AdventureComponents.getRunCommand(item, hover, colour));
    }

    @Override
    public void sendShowMore(CommandSender cs, String command) {
        cs.sendMessage(AdventureComponents.getShowMore(command));
    }

    @Override
    public void sendRecharger(CommandSender cs, String recharger, String world, String x, String y, String z, boolean hasPerm) {
        cs.sendMessage(AdventureComponents.getRecharger(recharger, world, x, y, z, hasPerm));
    }

    @Override
    public void sendHome(CommandSender cs, TARDIS plugin, String world, int x, int y, int z) {
        cs.sendMessage(AdventureComponents.getHome(plugin, world, x, y, z));
    }

    @Override
    public void sendSave(CommandSender cs, HashMap<String, String> map, String world) {
        cs.sendMessage(AdventureComponents.getSave(map, world));
    }

    @Override
    public void sendArea(CommandSender cs, Area a, int n, boolean hasPerm) {
        cs.sendMessage(AdventureComponents.getArea(a, n, hasPerm));
    }

    @Override
    public void sendRoom(CommandSender cs, String room, boolean hasPerm) {
        cs.sendMessage(AdventureComponents.getRoom(room, hasPerm));
    }

    @Override
    public void sendRoomGallery(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getRoomGallery());
    }

    @Override
    public void sendEyebrows(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getEyebrows());
    }

    @Override
    public void sendSign(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getSign());
    }

    @Override
    public void sendInfo(CommandSender cs, String first, String value, String split) {
        cs.sendMessage(AdventureComponents.getUpdate(first, value, split));
    }

    @Override
    public void sendHADS(CommandSender cs, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getHADS(plugin));
    }

    @Override
    public void sendColouredCommand(CommandSender cs, String which, String command, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getColouredCommand(which, command, plugin));
    }

    @Override
    public void sendInsertedColour(CommandSender cs, String local, String which, TARDIS plugin) {
        cs.sendMessage(AdventureComponents.getInsertColour(local, which, plugin));
    }

    @Override
    public void sendWithColours(CommandSender cs, String first, String colour, String last, String hue) {
        cs.sendMessage(AdventureComponents.getWithColours(first, colour, last, hue));
    }

    @Override
    public void sendWithColours(CommandSender cs, TardisModule module, String first, String colour, String last, String hue) {
        cs.sendMessage(AdventureComponents.getWithColours(module, first, colour, last, hue));
    }

    @Override
    public void sendCommand(CommandSender cs, String root, String command) {
        cs.sendMessage(AdventureComponents.getCommand(root, command));
    }
}
