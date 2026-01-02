/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.howto;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the TARDIS revealed that she had archived 30 versions. Once a control room was reconfigured, the TARDIS archived the
 * old design "for neatness". The TARDIS effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
class TARDISSeedRecipeInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Material block;
    private final Inventory inventory;

    TARDISSeedRecipeInventory(TARDIS plugin, Material block) {
        this.plugin = plugin;
        this.block = block;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Seed Recipe", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // get torch item
        Material torch = Material.REDSTONE_TORCH;
        if (!plugin.getConfig().getBoolean("creation.seed_block.legacy")) {
            String difficulty;
            World world = plugin.getServer().getWorlds().getFirst();
            switch (world.getDifficulty()) {
                case HARD -> difficulty = "hard";
                case NORMAL -> difficulty = "normal";
                default -> difficulty = "easy";
            }
            try {
                torch = Material.valueOf(plugin.getConfig().getString("creation.seed_block." + difficulty));
            } catch (IllegalArgumentException ignored) {
            }
        }
        ItemStack red = ItemStack.of(torch, 1);
        // lapis block
        ItemStack lapis = ItemStack.of(Material.LAPIS_BLOCK, 1);
        // interior wall
        ItemStack in_wall = ItemStack.of(Material.ORANGE_WOOL, 1);
        ItemMeta in_meta = in_wall.getItemMeta();
        in_meta.displayName(Component.text("Interior walls"));
        in_meta.lore(List.of(
                Component.text("Any valid Wall/Floor block"),
                Component.text("Click to see blocks...")
        ));
        in_wall.setItemMeta(in_meta);
        // interior floor
        ItemStack in_floor = ItemStack.of(Material.LIGHT_GRAY_WOOL, 1);
        ItemMeta fl_meta = in_floor.getItemMeta();
        fl_meta.displayName(Component.text("Interior floors"));
        fl_meta.lore(List.of(
                Component.text("Any valid Wall/Floor block"),
                Component.text("Click to see blocks...")
        ));
        in_floor.setItemMeta(fl_meta);
        // tardis type
        ItemStack tardis = ItemStack.of(block, 1);
        stack[0] = red;
        stack[9] = lapis;
        stack[11] = in_wall;
        stack[18] = tardis;
        stack[20] = in_floor;
        // close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);
        stack[26] = close;
        // back
        ItemStack back = ItemStack.of(Material.BOWL, 1);
        ItemMeta back_im = back.getItemMeta();
        back_im.displayName(Component.text("Back"));
        back.setItemMeta(back_im);
        stack[8] = back;

        return stack;
    }
}
