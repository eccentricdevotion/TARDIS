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
import me.eccentric_nz.TARDIS.handles.wiki.WikiLink;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Send messages.
 *
 * @author eccentric_nz
 */
public interface TARDISMessage {

    public void sendJenkinsUpdateReady(CommandSender cs, int current, int latest);

    public void sendUpdateCommand(CommandSender cs);

    public void sendBuildsBehind(CommandSender cs, int behind);

    public void message(CommandSender cs, TardisModule module, String message);

    public void message(CommandSender cs, String message);

    public void handlesMessage(Player p, String message);

    public void handlesSend(Player p, String key);

    public void handlesSend(Player p, String key, Object... subs);

    public void send(CommandSender cs, TardisModule module, String key);

    public void send(CommandSender cs, TardisModule module, String key, Object... subs);

    public void send(CommandSender cs, TardisModule module, String key, boolean handbrake);

    public void broadcast(TardisModule module, String message);

    public void sendWithColour(CommandSender cs, TardisModule module, String message, String hex);

    public void messageWithColour(CommandSender cs, String message, String hex);

    public void sendRequestComehereAccept(CommandSender cs, String key, String command);

    public void sendAbandoned(CommandSender cs, int i, String owner, String location, int id);

    public void sendTransmat(CommandSender cs, Transmat t);

    public void sendTARDISForList(CommandSender cs, Tardis t, String world, int x, int y, int z);

    public void sendExterminate(CommandSender cs, TARDIS plugin);

    public void sendRescue(CommandSender cs, TARDIS plugin);

    public void sendSuggestCommand(CommandSender cs, String item, String hover, String colour);

    public void sendRunCommand(CommandSender cs, String item, String hover, String colour);

    public void sendShowMore(CommandSender cs, String command);

    public void sendRecharger(CommandSender cs, String recharger, String world, String x, String y, String z, boolean hasPerm);

    public void sendHome(CommandSender cs, TARDIS plugin, String world, int x, int y, int z);

    public void sendSave(CommandSender cs, HashMap<String, String> map, String world);

    public void sendArea(CommandSender cs, Area a, int n, boolean hasPerm);

    public void sendRoom(CommandSender cs, String room, boolean hasPerm);

    public void sendRoomGallery(CommandSender cs);

    public void sendEyebrows(CommandSender cs);

    public void sendSign(CommandSender cs);

    public void sendInfo(CommandSender cs, String first, String value, String split);

    public void sendHADS(CommandSender cs, TARDIS plugin);

    public void sendColouredCommand(CommandSender cs, String which, String command, TARDIS plugin);

    public void sendInsertedColour(CommandSender cs, String local, String which, TARDIS plugin);

    public void sendWithColours(CommandSender cs, String first, String colour, String last, String hue);

    public void sendWithColours(CommandSender cs, TardisModule module, String first, String colour, String last, String hue);

    public void sendCommand(CommandSender cs, String root, String command);

    public void sendHeadsUpDisplay(Player player, TARDIS plugin);

    public void sendWikiLink(Player player, WikiLink wikiLink);

    public void sendStartBanner(CommandSender cs);

    public void sendProtected(CommandSender cs, String xyz, String location, int id);

    public void announceRepeater(Player player, String value);
}
