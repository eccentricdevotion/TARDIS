/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.utils.TARDISAcceptor;
import me.eccentric_nz.TARDIS.desktop.PreviewData;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesPattern;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesRequest;
import me.eccentric_nz.TARDIS.howto.TARDISSeedsInventory;
import me.eccentric_nz.TARDIS.travel.ComehereAction;
import me.eccentric_nz.TARDIS.travel.ComehereRequest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
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
public class TARDISChatListener implements Listener {

    private final TARDIS plugin;
    private final Pattern handlesPattern = TARDISHandlesPattern.getPattern("prefix");
    private Pattern howToPattern = null;

    public TARDISChatListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player typing "tardis [rescue|request|call] accept". If the player types it within 60 seconds of a
     * Time Lord sending a rescue|request|call, a player rescue|request|call attempt is made.
     * <p>
     * Also processes questions pertaining to "How to make a TARDIS?" and variations thereof.
     *
     * @param event a player typing in chat
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID chatter = player.getUniqueId();
        String chat = event.getMessage().toLowerCase(Locale.ROOT);
        if (chat.equals("tardis rescue accept") || chat.equals("tardis request accept")) {
            event.setCancelled(true);
            boolean request = (chat.equals("tardis request accept"));
            if (plugin.getTrackerKeeper().getChatRescue().containsKey(chatter)) {
                new TARDISAcceptor(plugin).doRequest(player, request);
            } else {
                String message = (request) ? "REQUEST_TIMEOUT" : "RESCUE_TIMEOUT";
                plugin.getMessenger().send(player, TardisModule.TARDIS, message);
            }
        } else if (chat.equals("tardis call accept")) {
            // process comehere request
            event.setCancelled(true);
            if (plugin.getTrackerKeeper().getComehereRequests().containsKey(chatter)) {
                ComehereRequest request = plugin.getTrackerKeeper().getComehereRequests().get(chatter);
                new ComehereAction(plugin).doTravel(request);
                plugin.getTrackerKeeper().getComehereRequests().remove(chatter);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "REQUEST_TIMEOUT");
            }
        } else if (handlesPattern.matcher(chat).lookingAt()) {
            event.setCancelled(true);
            // process handles request
            new TARDISHandlesRequest(plugin).process(chatter, event.getMessage());
        } else if (chat.equals("done") && plugin.getTrackerKeeper().getPreviewers().containsKey(chatter)) {
            event.setCancelled(true);
            // transmat back to TARDIS
            PreviewData pd = plugin.getTrackerKeeper().getPreviewers().get(chatter);
            Location transmat = pd.location();
            if (transmat != null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
                plugin.getTrackerKeeper().getPreviewers().remove(chatter);
                // transmat to preview desktop
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // set gamemode
                    player.playSound(transmat, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    player.teleport(transmat);
                    player.setGameMode(pd.gamemode());
                    // set TARDIS occupied
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("tardis_id", pd.id());
                        set.put("uuid", chatter.toString());
                        plugin.getQueryFactory().doSyncInsert("travellers", set);
                    }, 10L);
                }, 10L);
            }
        } else {
            handleChat(player, event.getMessage());
        }
    }

    private void handleChat(Player p, String message) {
        if (plugin.getTrackerKeeper().getHowTo().contains(p.getUniqueId())) {
            return;
        }
        if (howToPattern == null) {
            howToPattern = Pattern.compile("(^|.*\\W)how\\W.*\\W(craft|create|make|build|get)\\W.*tardis(\\W.*|$)", Pattern.CASE_INSENSITIVE);
        }
        if (howToPattern.matcher(message).matches()) {
            plugin.getTrackerKeeper().getHowTo().add(p.getUniqueId());
            // open how to GUI
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack[] seeds = new TARDISSeedsInventory(plugin, p).getMenu();
                Inventory wall = plugin.getServer().createInventory(p, 45, ChatColor.DARK_RED + "TARDIS Seeds Menu");
                wall.setContents(seeds);
                p.openInventory(wall);
            }, 1L);
        }
    }
}
