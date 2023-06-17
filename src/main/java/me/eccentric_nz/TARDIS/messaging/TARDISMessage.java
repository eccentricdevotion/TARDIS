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

import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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

    public void send(CommandSender cs, TardisModule module, String key, Object... subs);

    public void send(CommandSender cs, TardisModule module, String key, boolean handbrake);

    public void broadcast(TardisModule module, String message);

    public void sendWithColour(CommandSender cs, TardisModule module, String message, String hex);

    public void messageWithColour(CommandSender cs, String message, String hex);
}
