/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISHumListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Integer, Long> sounds = new HashMap<>();
    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final HashMap<UUID, Integer> last = new HashMap<>();

    public TARDISHumListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        sounds.put(0, Long.valueOf(27402));
        sounds.put(1, Long.valueOf(40594));
        sounds.put(2, Long.valueOf(68519));
        sounds.put(3, Long.valueOf(46026));
        sounds.put(4, Long.valueOf(51632));
        sounds.put(5, Long.valueOf(64313));
        sounds.put(6, Long.valueOf(75000));
        sounds.put(7, Long.valueOf(80379));
        sounds.put(8, Long.valueOf(70656));
        sounds.put(9, Long.valueOf(52950));
        sounds.put(10, Long.valueOf(77296));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS Interior Sounds")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 18) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    Player p = (Player) event.getWhoClicked();
                    UUID uuid = p.getUniqueId();
                    ItemMeta im = is.getItemMeta();
                    switch (slot) {
                        case 11:
                            HashMap<String, Object> setr = new HashMap<>();
                            HashMap<String, Object> wherer = new HashMap<>();
                            wherer.put("uuid", uuid.toString());
                            setr.put("hum", "");
                            new QueryFactory(plugin).doUpdate("player_prefs", setr, wherer);
                            close(p);
                            TARDISMessage.send(p, "HUM_SAVED");
                            break;
                        case 15:
                            // toggle play save
                            if (isPlay(inv)) {
                                setPlay(inv, "SAVE");
                            } else {
                                setPlay(inv, "PLAY");
                            }
                            break;
                        case 17:
                            // close
                            close(p);
                            break;
                        default:
                            if (isPlay(inv)) {
                                long now = System.currentTimeMillis();
                                if (cooldown.containsKey(uuid) && now < cooldown.get(uuid) + sounds.get(last.get(uuid))) {
                                    close(p);
                                    TARDISMessage.send(p, "HUM_WAIT");
                                } else {
                                    TARDISSounds.playTARDISSound(p, "tardis_hum_" + im.getDisplayName().toLowerCase(Locale.ENGLISH));
                                    last.put(uuid, slot);
                                    cooldown.put(uuid, System.currentTimeMillis());
                                }
                            } else {
                                HashMap<String, Object> set = new HashMap<>();
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("uuid", uuid.toString());
                                set.put("hum", im.getDisplayName().toLowerCase(Locale.ENGLISH));
                                new QueryFactory(plugin).doUpdate("player_prefs", set, where);
                                close(p);
                                TARDISMessage.send(p, "HUM_SAVED");
                            }
                            break;
                    }
                }
            }
        }
    }

    private void setPlay(Inventory inv, String str) {
        ItemStack play = inv.getItem(15);
        ItemMeta save = play.getItemMeta();
        save.setLore(Arrays.asList(str));
        play.setItemMeta(save);
    }

    private boolean isPlay(Inventory inv) {
        ItemStack play = inv.getItem(15);
        ItemMeta save = play.getItemMeta();
        return (save.getLore().get(0).endsWith("PLAY"));
    }
}
