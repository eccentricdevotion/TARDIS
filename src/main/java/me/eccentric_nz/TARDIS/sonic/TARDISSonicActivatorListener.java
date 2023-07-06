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
package me.eccentric_nz.TARDIS.sonic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSonic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISSonicActivatorListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<ItemStack> stacks;

    public TARDISSonicActivatorListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        stacks = getStacks();
    }

    private List<ItemStack> getStacks() {
        // get the Sonic Generator recipe
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get("Sonic Generator");
        List<ItemStack> mats = new ArrayList<>(recipe.getIngredientMap().values());
        mats.removeAll(Collections.singleton(null));
        return mats;
    }

    @EventHandler(ignoreCancelled = true)
    public void onActivatorMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Sonic Activator")) {
            Player p = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 9) {
                switch (slot) {
                    case 7 -> event.setCancelled(true);
                    case 8 -> {
                        event.setCancelled(true);
                        // close
                        save(p, view);
                    }
                    default -> {
                    }
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void save(Player p, InventoryView view) {
        Material m = Material.AIR;
        int count = 0;
        for (int i = 0; i < 7; i++) {
            ItemStack is = view.getItem(i);
            if (is != null) {
                if (!is.getType().equals(m) && stacks.contains(is)) {
                    m = is.getType();
                    count++;
                }
            }
        }
        close(p);
        if (count == stacks.size()) {
            // actvate Sonic Generator
            String uuid = p.getUniqueId().toString();
            // do they have a sonic record?
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid);
            ResultSetSonic rss = new ResultSetSonic(plugin, wheres);
            HashMap<String, Object> set = new HashMap<>();
            set.put("activated", 1);
            if (rss.resultSet() && !rss.getSonic().isActivated()) {
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("uuid", uuid);
                plugin.getQueryFactory().doUpdate("sonic", set, wherea);
            } else {
                set.put("uuid", uuid);
                plugin.getQueryFactory().doInsert("sonic", set);
            }
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SONIC_ACTIVATED");
        } else {
            // return item stacks
            Location l = p.getLocation();
            World w = l.getWorld();
            for (int i = 0; i < 7; i++) {
                ItemStack is = view.getItem(i);
                if (is != null) {
                    w.dropItemNaturally(l, is);
                }
            }
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SONIC_NOT_ACTIVATED");
        }
    }
}
