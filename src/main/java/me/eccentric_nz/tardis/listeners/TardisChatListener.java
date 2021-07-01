/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisTravelEvent;
import me.eccentric_nz.tardis.enumeration.TravelType;
import me.eccentric_nz.tardis.flight.TardisLand;
import me.eccentric_nz.tardis.handles.TardisHandlesPattern;
import me.eccentric_nz.tardis.handles.TardisHandlesRequest;
import me.eccentric_nz.tardis.howto.TardisSeedsInventory;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisRescue;
import me.eccentric_nz.tardis.travel.TardisRescue.RescueData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * In 21st century London, Rory has his father, Brian Williams, over to help fix a light bulb. After saying the fixture
 * may be the problem, the sound of the TARDIS materialisation is heard. The TARDIS materialises around them, shocking
 * Brian in place.
 *
 * @author eccentric_nz
 */
public class TardisChatListener implements Listener {

    private final TardisPlugin plugin;
    private final Pattern handlesPattern = TardisHandlesPattern.getPattern("prefix");
    private Pattern howToPattern = null;

    public TardisChatListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player typing "tardis rescue accept". If the player types it within 60 seconds of a Time Lord sending
     * a rescue request, a player rescue attempt is made.
     * <p>
     * Also processes questions pertaining to "How to make a TARDIS?" and variations thereof.
     *
     * @param event a player typing in chat
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        UUID saved = event.getPlayer().getUniqueId();
        String chat = event.getMessage().toLowerCase(Locale.ENGLISH);
        if (chat != null) {
            if (chat.equals("tardis rescue accept") || chat.equals("tardis request accept")) {
                event.setCancelled(true);
                boolean request = (chat.equals("tardis request accept"));
                if (plugin.getTrackerKeeper().getChatRescue().containsKey(saved)) {
                    Player rescuer = plugin.getServer().getPlayer(plugin.getTrackerKeeper().getChatRescue().get(saved));
                    TardisRescue res = new TardisRescue(plugin);
                    plugin.getTrackerKeeper().getChatRescue().remove(saved);
                    // delay it so the chat appears before the message
                    String player = event.getPlayer().getName();
                    String message = (request) ? "REQUEST_RELEASE" : "RESCUE_RELEASE";
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        RescueData rd = res.tryRescue(rescuer, saved, request);
                        if (rd.success()) {
                            if (plugin.getTrackerKeeper().getTelepathicRescue().containsKey(saved)) {
                                Player who = plugin.getServer().getPlayer(plugin.getTrackerKeeper().getTelepathicRescue().get(saved));
                                if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardisId())) {
                                    TardisMessage.send(who, message, player);
                                }
                                plugin.getTrackerKeeper().getTelepathicRescue().remove(saved);
                            } else if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardisId())) {
                                TardisMessage.send(rescuer, message, player);
                            }
                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardisId())) {
                                new TardisLand(plugin, rd.getTardisId(), rescuer).exitVortex();
                                plugin.getPluginManager().callEvent(new TardisTravelEvent(rescuer, null, TravelType.RANDOM, rd.getTardisId()));
                            }
                        }
                    }, 2L);
                } else {
                    String message = (request) ? "REQUEST_TIMEOUT" : "RESCUE_TIMEOUT";
                    TardisMessage.send(event.getPlayer(), message);
                }
            } else if (handlesPattern.matcher(chat).lookingAt()) {
                event.setCancelled(true);
                // process handles request
                new TardisHandlesRequest(plugin).process(saved, event.getMessage());
            } else {
                handleChat(event.getPlayer(), event.getMessage());
            }
        }
    }

    private void handleChat(Player p, String message) {
        if (plugin.getTrackerKeeper().getHowTo().contains(p.getUniqueId())) {
            return;
        }
        if (howToPattern == null) {
            howToPattern = Pattern.compile("(^|.*\\W)how\\W.*\\W(create|make|build|get)\\W.*tardis(\\W.*|$)", Pattern.CASE_INSENSITIVE);
        }
        if (howToPattern.matcher(message).matches()) {
            plugin.getTrackerKeeper().getHowTo().add(p.getUniqueId());
            // open how to GUI
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack[] seeds = new TardisSeedsInventory(plugin, p).getMenu();
                Inventory wall = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "TARDIS Seeds Menu");
                wall.setContents(seeds);
                p.openInventory(wall);
            }, 1L);
        }
    }
}