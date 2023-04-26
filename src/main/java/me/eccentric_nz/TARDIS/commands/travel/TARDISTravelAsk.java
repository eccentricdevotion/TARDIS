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
package me.eccentric_nz.TARDIS.commands.travel;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISTravelRequest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelAsk {

    private final TARDIS plugin;

    public TARDISTravelAsk(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args, int id) {
        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
            TARDISMessage.send(player, "NO_PERM_PLAYER");
            return true;
        }
        Player requested = plugin.getServer().getPlayer(args[0]);
        if (requested == null) {
            TARDISMessage.send(player, "NOT_ONLINE");
            return true;
        }
        // check the to player's DND status
        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, requested.getUniqueId().toString());
        if (rspp.resultSet() && rspp.isDND()) {
            TARDISMessage.send(player, "DND", args[0]);
            return true;
        }
        // check the location
        TARDISTravelRequest ttr = new TARDISTravelRequest(plugin);
        if (!ttr.getRequest(player, requested, requested.getLocation())) {
            return true;
        }
        // ask if we can travel to this player
        UUID requestedUUID = requested.getUniqueId();
        TARDISMessage.send(requested, "REQUEST_TRAVEL", player.getName());
        TextComponent textComponent = new TextComponent(plugin.getLanguage().getString("REQUEST_COMEHERE_ACCEPT"));
        textComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis request accept"));
        requested.spigot().sendMessage(textComponent);
        // message asking player too
        TARDISMessage.send(player, "REQUEST_SENT", requested.getName());
        plugin.getTrackerKeeper().getChatRescue().put(requestedUUID, player.getUniqueId());
        Player p = player;
        String to = args[0];
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (plugin.getTrackerKeeper().getChatRescue().containsKey(requestedUUID)) {
                plugin.getTrackerKeeper().getChatRescue().remove(requestedUUID);
                TARDISMessage.send(p, "REQUEST_NO_RESPONSE", to);
            }
        }, 1200L);
        return true;
    }
}
