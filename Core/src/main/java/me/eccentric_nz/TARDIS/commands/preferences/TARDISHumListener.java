/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISHumListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<Integer, Long> sounds = new HashMap<>();
    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final HashMap<UUID, Integer> last = new HashMap<>();

    public TARDISHumListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        sounds.put(0, 27402L);
        sounds.put(1, 40594L);
        sounds.put(2, 68519L);
        sounds.put(3, 46026L);
        sounds.put(4, 51632L);
        sounds.put(5, 64313L);
        sounds.put(6, 75000L);
        sounds.put(7, 80379L);
        sounds.put(8, 70656L);
        sounds.put(9, 52950L);
        sounds.put(10, 77296L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Interior Sounds")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 17) {
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        UUID uuid = p.getUniqueId();
        ItemMeta im = is.getItemMeta();
        switch (slot) {
            case 11 -> {
                HashMap<String, Object> setr = new HashMap<>();
                HashMap<String, Object> wherer = new HashMap<>();
                wherer.put("uuid", uuid.toString());
                setr.put("hum", "");
                plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
                close(p);
                plugin.getMessenger().send(p, TardisModule.TARDIS, "HUM_SAVED");
            }
            case 15 -> {
                // toggle play save
                if (isPlay(view)) {
                    setPlay(view, "SAVE");
                } else {
                    setPlay(view, "PLAY");
                }
            }
            case 17 ->
                // close
                    close(p);
            default -> {
                if (isPlay(view)) {
                    long now = System.currentTimeMillis();
                    if (cooldown.containsKey(uuid) && now < cooldown.get(uuid) + sounds.get(last.get(uuid))) {
                        close(p);
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "HUM_WAIT");
                    } else {
                        TARDISSounds.playTARDISSound(p, "tardis_hum_" + im.getDisplayName().toLowerCase(Locale.ENGLISH), 5L);
                        last.put(uuid, slot);
                        cooldown.put(uuid, System.currentTimeMillis());
                    }
                } else {
                    HashMap<String, Object> set = new HashMap<>();
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", uuid.toString());
                    set.put("hum", im.getDisplayName().toLowerCase(Locale.ENGLISH));
                    plugin.getQueryFactory().doUpdate("player_prefs", set, where);
                    close(p);
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "HUM_SAVED");
                }
            }
        }
    }

    private void setPlay(InventoryView view, String str) {
        ItemStack play = view.getItem(15);
        ItemMeta save = play.getItemMeta();
        save.setLore(List.of(str));
        play.setItemMeta(save);
    }

    private boolean isPlay(InventoryView view) {
        ItemStack play = view.getItem(15);
        ItemMeta save = play.getItemMeta();
        return (save.getLore().getFirst().endsWith("PLAY"));
    }
}
