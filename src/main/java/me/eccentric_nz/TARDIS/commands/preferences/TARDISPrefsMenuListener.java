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
import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminMenuInventory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
        lookup.put("Autonomous Siege", "auto_siege_on");
        lookup.put("Beacon", "beacon_on");
        lookup.put("Do Not Disturb", "dnd_on");
        lookup.put("Emergency Programme One", "eps_on");
        lookup.put("Hostile Action Displacement System", "hads_on");
        lookup.put("Minecart Sounds", "minecart_on");
        lookup.put("Who Quotes", "quotes_on");
        lookup.put("Exterior Rendering Room", "renderer_on");
        lookup.put("Interior SFX", "sfx_on");
        lookup.put("Submarine Mode", "submarine_on");
        lookup.put("Resource Pack Switching", "texture_on");
        lookup.put("Companion Build", "build_on");
        lookup.put("Wool For Lights Off", "wool_lights_on");
        lookup.put("Connected Textures", "ctm_on");
        lookup.put("Preset Sign", "sign_on");
        lookup.put("Travel Bar", "travelbar_on");
        lookup.put("Mob Farming", "farm_on");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(final InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Player Prefs Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    final Player p = (Player) event.getWhoClicked();
                    UUID uuid = p.getUniqueId();
                    ItemMeta im = is.getItemMeta();
                    if (slot == 22 && im.getDisplayName().equals("TARDIS Map")) {
                        // must be in the TARDIS
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", uuid.toString());
                        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
                        if (rs.resultSet()) {
                            // close this gui and load the TARDIS map
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    Inventory new_inv = plugin.getServer().createInventory(p, 54, "§4TARDIS Map");
                                    // close inventory
                                    p.closeInventory();
                                    // open new inventory
                                    new_inv.setContents(new TARDISARSMap(plugin).getMap());
                                    p.openInventory(new_inv);
                                }
                            }, 1L);
                        } else {
                            TARDISMessage.send(p, "NOT_IN_TARDIS");
                        }
                        return;
                    }
                    if (slot == 26 && im.getDisplayName().equals("Admin Menu")) {
                        // close this gui and load the Admin Menu
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Inventory menu = plugin.getServer().createInventory(p, 54, "§4Admin Menu");
                                menu.setContents(new TARDISAdminMenuInventory(plugin).getMenu());
                                p.openInventory(menu);
                            }
                        }, 1L);
                        return;
                    }
                    List<String> lore = im.getLore();
                    boolean bool = (lore.get(0).equals(plugin.getLanguage().getString("SET_ON")));
                    String value = (bool) ? plugin.getLanguage().getString("SET_OFF") : plugin.getLanguage().getString("SET_ON");
                    int b = (bool) ? 0 : 1;
                    if (im.getDisplayName().equals("Companion Build")) {
                        String[] args = new String[2];
                        args[0] = "";
                        args[1] = value;
                        new TARDISBuildCommand(plugin).toggleCompanionBuilding(((Player) event.getWhoClicked()), args);
                    } else {
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put(lookup.get(im.getDisplayName()), b);
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", uuid.toString());
                        new QueryFactory(plugin).doUpdate("player_prefs", set, where);
                    }
                    lore.set(0, value);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    if (im.getDisplayName().equals("Beacon")) {
                        new TARDISBeaconToggler(plugin).flickSwitch(uuid, !bool);
                    }
                }
            }
        }
    }
}
