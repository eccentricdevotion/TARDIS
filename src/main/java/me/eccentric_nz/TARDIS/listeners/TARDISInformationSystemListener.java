/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * In 21st century London, Rory has his father, Brian Williams, over to help fix
 * a light bulb. After saying the fixture may be the problem, the sound of the
 * TARDIS materialisation is heard. The TARDIS materialises around them,
 * shocking Brian in place.
 *
 * @author eccentric_nz
 */
public class TARDISInformationSystemListener implements Listener {

    private final TARDIS plugin;

    public TARDISInformationSystemListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player typing "tardis rescue accept". If the player types it
     * within 60 seconds of a Timelord sending a rescue request, a player rescue
     * attempt is made.
     *
     * @param event a player typing in chat
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onTISChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String name = p.getName();
        if (plugin.trackInfoMenu.containsKey(name)) {
            event.setCancelled(true);
            String chat = event.getMessage();
            // always exit if 'e' is pressed
            if (chat.equalsIgnoreCase("E")) {
                p.sendMessage("---");
                p.sendMessage("§4You have been logged out of the TARDIS Information System");
                plugin.trackChat.remove(name);
            }
            switch (plugin.trackInfoMenu.get(name)) {
                // TOP level menu
                case TIS:
                    if (chat.equalsIgnoreCase("M")) {
                        //processKey(p, TARDISInfoMenu.MANUAL);
                        p.sendMessage("---");
                        p.sendMessage("§6[TIS/TARDIS Manual] is not yet available in your current timestream");
                        p.sendMessage("§6---");
                        p.sendMessage("§4You have been logged out of the TARDIS Information System");
                        plugin.trackChat.remove(name);
                    }
                    if (chat.equalsIgnoreCase("I")) {
                        processKey(p, TARDISInfoMenu.ITEMS);
                    }
                    if (chat.equalsIgnoreCase("C")) {
                        processKey(p, TARDISInfoMenu.COMPONENTS);
                    }
                    if (chat.equalsIgnoreCase("O")) {
                        processKey(p, TARDISInfoMenu.COMMANDS);
                    }
                    if (chat.equalsIgnoreCase("R")) {
                        processKey(p, TARDISInfoMenu.ROOMS);
                    }
                    if (chat.equalsIgnoreCase("T")) {
                        processKey(p, TARDISInfoMenu.TYPES);
                    }
                    break;
                // SECOND level menu
                case ITEMS:
                    if (chat.equalsIgnoreCase("K")) {
                        processKey(p, TARDISInfoMenu.KEY);
                    }
                    if (chat.equalsIgnoreCase("S")) {
                        processKey(p, TARDISInfoMenu.SONIC);
                    }
                    if (chat.equalsIgnoreCase("L")) {
                        processKey(p, TARDISInfoMenu.LOCATOR);
                    }
                    if (chat.equalsIgnoreCase("R")) {
                        processKey(p, TARDISInfoMenu.REMOTE);
                    }
                    break;
                case COMPONENTS:
                    if (chat.equalsIgnoreCase("L")) {
                        processKey(p, TARDISInfoMenu.L_CIRCUIT);
                    }
                    if (chat.equalsIgnoreCase("M")) {
                        processKey(p, TARDISInfoMenu.M_CIRCUIT);
                    }
                    if (chat.equalsIgnoreCase("S")) {
                        processKey(p, TARDISInfoMenu.S_CIRCUIT);
                    }
                    break;
                case MANUAL:
                    break;
                case COMMANDS:
                    if (chat.equalsIgnoreCase("T")) {
                        processKey(p, TARDISInfoMenu.TARDIS);
                    }
                    if (chat.equalsIgnoreCase("A")) {
                        processKey(p, TARDISInfoMenu.T_ADMIN);
                    }
                    if (chat.equalsIgnoreCase("C")) {
                        processKey(p, TARDISInfoMenu.T_AREA);
                    }
                    if (chat.equalsIgnoreCase("B")) {
                        processKey(p, TARDISInfoMenu.T_BIND);
                    }
                    if (chat.equalsIgnoreCase("k")) {
                        processKey(p, TARDISInfoMenu.T_BOOK);
                    }
                    if (chat.equalsIgnoreCase("G")) {
                        processKey(p, TARDISInfoMenu.T_GRAVITY);
                    }
                    if (chat.equalsIgnoreCase("P")) {
                        processKey(p, TARDISInfoMenu.T_PREFS);
                    }
                    if (chat.equalsIgnoreCase("R")) {
                        processKey(p, TARDISInfoMenu.T_RECIPE);
                    }
                    if (chat.equalsIgnoreCase("o")) {
                        processKey(p, TARDISInfoMenu.T_ROOM);
                    }
                    if (chat.equalsIgnoreCase("T")) {
                        processKey(p, TARDISInfoMenu.T_TEXTURE);
                    }
                    if (chat.equalsIgnoreCase("v")) {
                        processKey(p, TARDISInfoMenu.T_TRAVEL);
                    }
                    break;
                case ROOMS:

                    break;
            }

        }
    }

    private void processKey(Player p, TARDISInfoMenu item) {
        plugin.trackInfoMenu.put(p.getName(), item);
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        for (Map.Entry<String, String> m : TARDISInfoMenu.getChildren(item.toString()).entrySet()) {
            String menu = m.getKey().replaceFirst(m.getValue(), "§f" + m.getValue() + "§6");
            p.sendMessage("§6> " + menu);
        }
        p.sendMessage("§6> §fE§6xit");
    }
}
