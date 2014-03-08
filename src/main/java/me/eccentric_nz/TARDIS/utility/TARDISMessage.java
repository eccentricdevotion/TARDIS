/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

/**
 * Send tidy messages.
 *
 * @author eccentric_nz
 */
public class TARDISMessage {

    private static final int lineLength = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH;

    /**
     * Splits a message into multiple lines if it is longer than the guaranteed
     * chat page width.
     *
     * @param p the player to send the message
     * @param message the message to send
     */
    public static void send(Player p, String message) {
        if (message.length() > lineLength) {
            String[] multiline = ChatPaginator.wordWrap(message, lineLength);
            p.sendMessage(multiline);
        } else {
            p.sendMessage(message);
        }
    }
}
