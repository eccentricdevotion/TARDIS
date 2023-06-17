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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.utils.TARDISAcceptor;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPowered;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISRescueCommand {

    private final TARDIS plugin;

    TARDISRescueCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean startRescue(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        if (args[1].equalsIgnoreCase("accept")) {
            new TARDISAcceptor(plugin).doRequest(player, false);
            return true;
        }
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.rescue")) {
            ResultSetTardisPowered rs = new ResultSetTardisPowered(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                return true;
            }
            String saved = args[1];
            if (!saved.equalsIgnoreCase("accept")) {
                Player destPlayer = plugin.getServer().getPlayer(saved);
                if (destPlayer == null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ONLINE");
                    return true;
                }
                UUID savedUUID = destPlayer.getUniqueId();
                String who = (plugin.getTrackerKeeper().getTelepathicRescue().containsKey(savedUUID)) ? plugin.getServer().getPlayer(plugin.getTrackerKeeper().getTelepathicRescue().get(savedUUID)).getName() : player.getName();
                // get auto_rescue_on preference
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, destPlayer.getUniqueId().toString());
                if (rsp.resultSet() && rsp.isAutoRescueOn()) {
                    // go straight to rescue
                    TARDISRescue res = new TARDISRescue(plugin);
                    plugin.getTrackerKeeper().getChatRescue().remove(savedUUID);
                    // delay it so the chat appears before the message
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        TARDISRescue.RescueData rd = res.tryRescue(player, destPlayer.getUniqueId(), false);
                        if (rd.success()) {
                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardis_id())) {
                                new TARDISLand(plugin, rd.getTardis_id(), player).exitVortex();
                                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RESCUE, rd.getTardis_id()));
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "REQUEST_RELEASE", destPlayer.getName());
                            }
                        }
                    }, 2L);
                } else {
                    plugin.getMessenger().send(destPlayer, TardisModule.TARDIS, "RESCUE_REQUEST", who);
                    // TODO
                    TextComponent textComponent = new TextComponent(plugin.getLanguage().getString("REQUEST_COMEHERE_ACCEPT"));
                    textComponent.setColor(ChatColor.AQUA);
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "tardis rescue accept"));
                    destPlayer.spigot().sendMessage(textComponent);
                    plugin.getTrackerKeeper().getChatRescue().put(savedUUID, player.getUniqueId());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (plugin.getTrackerKeeper().getChatRescue().containsKey(savedUUID)) {
                            plugin.getTrackerKeeper().getChatRescue().remove(savedUUID);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "RESCUE_NO_RESPONSE", saved);
                        }
                    }, 1200L);
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_PLAYER");
            return true;
        }
        return false;
    }
}
