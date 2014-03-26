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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPrefsMenuListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<String, String> lookup = new HashMap<String, String>();

    public TARDISPrefsMenuListener(TARDIS plugin) {
        this.plugin = plugin;
        lookup.put("Autonomous", "auto_on");
        lookup.put("Beacon", "beacon_on");
        lookup.put("Do Not Disturb", "dnd_on");
        lookup.put("Emergency Program One", "eps_on");
        lookup.put("Hostile Action Displacement System", "hads_on");
        lookup.put("Minecart Sounds", "minecart_on");
        lookup.put("Safety Platform", "platform_on");
        lookup.put("Who Quotes", "quotes_on");
        lookup.put("Exterior Rendering Room", "renderer_on");
        lookup.put("Interior SFX", "sfx_on");
        lookup.put("Submarine Mode", "submarine_on");
        lookup.put("Resource Pack Switching", "texture_on");
        lookup.put("Companion Build", "build_on");
        lookup.put("Wool For Lights Off", "wool_lights_on");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Player Prefs Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 18) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    ItemMeta im = is.getItemMeta();
                    List<String> lore = im.getLore();
                    boolean bool = (lore.get(0).equals("ON"));
                    String value = (bool) ? "OFF" : "ON";
                    int b = (bool) ? 0 : 1;
                    String p = ((Player) event.getWhoClicked()).getName();
                    if (im.getDisplayName().equals("Companion Build")) {
                        String[] args = new String[2];
                        args[0] = "";
                        args[1] = value;
                        new TARDISBuildCommand(plugin).toggleCompanionBuilding(((Player) event.getWhoClicked()), args);
                    } else {
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put(lookup.get(im.getDisplayName()), b);
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("player", p);
                        new QueryFactory(plugin).doUpdate("player_prefs", set, where);
                    }
                    lore.set(0, value);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    if (im.getDisplayName().equals("Beacon")) {
                        new TARDISToggleOnOffCommand(plugin).toggleBeacon(p, !bool);
                    }
                }
            }
        }
    }
}
