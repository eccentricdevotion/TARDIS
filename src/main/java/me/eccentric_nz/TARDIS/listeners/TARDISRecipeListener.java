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

//import java.util.HashMap;
//import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRecipeListener implements Listener {

    private final TARDIS plugin;

    public TARDISRecipeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRecipeClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        InventoryType type = top.getType();
        if (type == InventoryType.WORKBENCH) {
            final Player player = (Player) event.getWhoClicked();
            if (plugin.trackRecipeView.contains(player.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRecipeClose(InventoryCloseEvent event) {
        Inventory top = event.getView().getTopInventory();
        InventoryType type = top.getType();
        if (type == InventoryType.WORKBENCH) {
            Player p = (Player) event.getPlayer();
            String name = p.getName();
            if (plugin.trackRecipeView.contains(name)) {
                plugin.trackRecipeView.remove(name);
                event.getView().getTopInventory().clear();
                p.updateInventory();
            }
        }
    }
    /**
     * Changes the default crafted Sonic Screwdriver (TARDIS Key) to the
     * Material type specified in the player's TARDIS preferences (if NOT a
     * stick). Has huge potential for being a DIAMOND generator, as there is
     * nothing stopping players from changing their key preference to a DIAMOND
     * and crafting as many as they want! Could still be valid if a key material
     * list is used and the items are of the same or lesser value than 2 sticks
     * and a redstone, OR the recipe is changed to be really expensive.
     */
//    @EventHandler(priority = EventPriority.NORMAL)
//    public void onRecipeResult(CraftItemEvent event) {
//        ItemStack is = event.getRecipe().getResult();
//        if (is.hasItemMeta()) {
//            ItemMeta im = is.getItemMeta();
//            if (im.hasLore()) {
//                List<String> lore = im.getLore();
//                if (im.getDisplayName().equals("Sonic Screwdriver") && lore.get(0).equals("Enter and exit your TARDIS")) {
//                    String p = ((Player) event.getWhoClicked()).getName();
//                    HashMap<String, Object> where = new HashMap<String, Object>();
//                    where.put("player", p);
//                    ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, where);
//                    if (rs.resultSet()) {
//                        Material m = Material.valueOf(rs.getKey());
//                        if (!m.equals(Material.STICK)) {
//                            ItemStack newis = new ItemStack(m, 1);
//                            TARDISItemRenamer ir = new TARDISItemRenamer(newis);
//                            ir.setName("Sonic Screwdriver", true);
//                            event.setCurrentItem(newis);
//                        }
//                    }
//                }
//            }
//        }
//    }
}
