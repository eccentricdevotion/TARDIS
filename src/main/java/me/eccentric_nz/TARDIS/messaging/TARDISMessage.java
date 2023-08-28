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
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.wiki.WikiLink;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Send messages.
 *
 * @author eccentric_nz
 */
public interface TARDISMessage {

    void sendJenkinsUpdateReady(CommandSender cs, int current, int latest);

    void sendUpdateCommand(CommandSender cs);

    void sendBuildsBehind(CommandSender cs, int behind);

    void message(CommandSender cs, TardisModule module, String message);

    void message(CommandSender cs, String message);

    void handlesMessage(Player p, String message);

    void handlesSend(Player p, String key);

    void handlesSend(Player p, String key, Object... subs);

    void send(CommandSender cs, TardisModule module, String key);

    void send(CommandSender cs, TardisModule module, String key, Object... subs);

    void send(Player player, String key, boolean handbrake);

    void broadcast(TardisModule module, String message);

    void sendWithColour(CommandSender cs, TardisModule module, String message, String hex);

    void messageWithColour(CommandSender cs, String message, String hex);

    void sendRequestComehereAccept(CommandSender cs, String key, String command);

    void sendAbandoned(CommandSender cs, int i, String owner, String location, int id);

    void sendTransmat(CommandSender cs, Transmat t);

    void sendTARDISForList(CommandSender cs, Tardis t, String world, int x, int y, int z);

    void sendExterminate(CommandSender cs, TARDIS plugin);

    void sendRescue(CommandSender cs, TARDIS plugin);

    void sendSuggestCommand(CommandSender cs, String item, String hover, String colour);

    void sendRunCommand(CommandSender cs, String item, String hover, String colour);

    void sendShowMore(CommandSender cs, String command);

    void sendRecharger(CommandSender cs, String recharger, String world, String x, String y, String z, boolean hasPerm);

    void sendHome(CommandSender cs, TARDIS plugin, String world, int x, int y, int z);

    void sendSave(CommandSender cs, HashMap<String, String> map, String world);

    void sendArea(CommandSender cs, Area a, int n, boolean hasPerm);

    void sendRoom(CommandSender cs, String room, boolean hasPerm);

    void sendRoomGallery(CommandSender cs);

    void sendEyebrows(CommandSender cs);

    void sendSign(CommandSender cs);

    void sendInfo(CommandSender cs, String first, String value, String split);

    void sendHADS(CommandSender cs, TARDIS plugin);

    void sendColouredCommand(CommandSender cs, String which, String command, TARDIS plugin);

    void sendInsertedColour(CommandSender cs, String local, String which, TARDIS plugin);

    void sendWithColours(CommandSender cs, String first, String colour, String last, String hue);

    void sendWithColours(CommandSender cs, TardisModule module, String first, String colour, String last, String hue);

    void sendCommand(CommandSender cs, String root, String command);

    void sendHeadsUpDisplay(Player player, TARDIS plugin);

    void sendWikiLink(Player player, WikiLink wikiLink);

    void sendStartBanner(CommandSender cs);

    void sendProtected(CommandSender cs, String xyz, String location, int id);

    void announceRepeater(Player player, String value);

    void sendStatus(Player player, String key);

    void sendArtron(Player player, int id, int used);
}
