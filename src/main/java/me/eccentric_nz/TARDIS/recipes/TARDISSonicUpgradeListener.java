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
package me.eccentric_nz.TARDIS.recipes;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicUpgradeListener implements Listener {

    //private final TARDIS plugin;
    private final Material sonicMaterial;
    private final List<String> upgrades = new ArrayList<String>();

    public TARDISSonicUpgradeListener(TARDIS plugin) {
        //this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        this.sonicMaterial = Material.valueOf(split[0]);
        this.upgrades.add("Admin Upgrade");
        this.upgrades.add("Bio-scanner Upgrade");
        this.upgrades.add("Redstone Upgrade");
        this.upgrades.add("Diamond Upgrade");
        this.upgrades.add("Emerald Upgrade");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSonicUpgrade(PrepareItemCraftEvent event) {
        CraftingInventory ci = event.getInventory();
        Recipe recipe = ci.getRecipe();
        if (recipe instanceof ShapelessRecipe) {
            ItemStack is = ci.getResult();
            if (is.getType().equals(sonicMaterial) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                String upgrade = im.getDisplayName();
                if (!upgrades.contains(upgrade)) {
                    ci.setResult(null);
                }
                ItemStack sonic = null;
                for (int i = 1; i <= ci.getSize(); i++) {
                    ItemStack item = ci.getItem(i);
                    if (item != null && item.getType().equals(sonicMaterial) && item.hasItemMeta()) {
                        sonic = item;
                        break;
                    }
                }
                if (sonic == null) {
                    ci.setResult(null);
                } else {
                    ItemMeta sim = sonic.getItemMeta();
                    List<String> lore;
                    if (sim.hasLore()) {
                        lore = sim.getLore();
                    } else {
                        lore = new ArrayList<String>();
                        lore.add("Upgrades:");
                    }
                    if (!lore.contains(upgrade)) {
                        im.setDisplayName("Sonic Screwdriver");
                        lore.add(upgrade);
                        im.setLore(lore);
                        is.setItemMeta(im);
                        ci.setResult(is);
                    } else {
                        ci.setResult(null);
                    }
                }
            }
        }
    }

//    public enum UPGRADE {
//
//        ADMIN("Admin Upgrade"),
//        BIO("Bio-scanner Upgrade"),
//        REDSTONE("Redstone Upgrade"),
//        DIAMOND("Diamond Upgrade"),
//        EMERALD("Emerald Upgrade");
//        private final String name;
//
//        private UPGRADE(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        /**
//         * Attempts to get the UPGRADE with the given name.
//         *
//         * @param name Name of the material to get
//         * @return UPGRADE if found, or null
//         */
//        public static UPGRADE getUPGRADE(final String name) {
//            return BY_NAME.get(name);
//        }
//
//        static {
//            for (UPGRADE u : values()) {
//                BY_NAME.put(u.getName(), u);
//            }
//        }
//    }
}
