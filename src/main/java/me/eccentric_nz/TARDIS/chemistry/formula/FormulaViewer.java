/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.formula;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.compound.Compound;
import me.eccentric_nz.TARDIS.chemistry.compound.CompoundBuilder;
import me.eccentric_nz.TARDIS.chemistry.element.Element;
import me.eccentric_nz.TARDIS.chemistry.element.ElementBuilder;
import me.eccentric_nz.TARDIS.chemistry.lab.Lab;
import me.eccentric_nz.TARDIS.chemistry.lab.LabBuilder;
import me.eccentric_nz.TARDIS.chemistry.product.Product;
import me.eccentric_nz.TARDIS.chemistry.product.ProductBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FormulaViewer {

    private final TARDIS plugin;
    private final Player player;
    private final ItemStack[] stack = new ItemStack[27];
    private String formula;

    public FormulaViewer(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void getCompoundFormula(Compound compound) {
        String[] shape = compound.getFormula().split("-");
        for (int i = 0; i < shape.length; i++) {
            ItemStack is = null;
            String[] data = shape[i].split(":");
            // is it an element?
            try {
                Element element = Element.valueOf(data[0]);
                is = ElementBuilder.getElement(element);
                is.setAmount(Integer.parseInt(data[1]));
            } catch (IllegalArgumentException ee) {
                // don't know what it is
            }
            stack[i + 18] = is;
        }
        ItemStack result = CompoundBuilder.getCompound(compound);
        stack[0] = result;
        formula = compound.getSymbol();
        showView();
    }

    public void getProductFormula(Product product) {
        String[] shape = product.getRecipe().split("\\|");
        String[][] data = new String[3][3];
        data[0] = shape[0].split(",");
        data[1] = shape[1].split(",");
        data[2] = shape[2].split(",");
        int row = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (data[i][j] != null && !data[i][j].equals("-")) {
                    ItemStack is = null;
                    try {
                        // is it a Spigot material?
                        Material material = Material.valueOf(data[i][j]);
                        is = new ItemStack(material, 1);
                    } catch (IllegalArgumentException me) {
                        // is it a compound?
                        try {
                            Compound compound = Compound.valueOf(data[i][j].replace(" ", "_"));
                            is = CompoundBuilder.getCompound(compound);
                        } catch (IllegalArgumentException ce) {
                            // is it an element?
                            try {
                                Element element = Element.valueOf(data[i][j]);
                                is = ElementBuilder.getElement(element);
                            } catch (IllegalArgumentException ee) {
                                // don't know what it is
                            }
                        }
                    }
                    int slot = i + j + row;
                    stack[slot] = is;
                }
            }
            row += 8;
        }
        ItemStack result = ProductBuilder.getProduct(product);
        stack[14] = result;
        formula = product.toString();
        showView();
    }

    public void getLabFormula(Lab lab) {
        String[] shape = lab.getRecipe().split(",");
        for (int i = 0; i < shape.length; i++) {
            ItemStack is = null;
            try {
                Compound compound = Compound.valueOf(shape[i].replace(" ", "_"));
                is = CompoundBuilder.getCompound(compound);
            } catch (IllegalArgumentException ce) {
                // is it an element?
                try {
                    Element element = Element.valueOf(shape[i]);
                    is = ElementBuilder.getElement(element);
                } catch (IllegalArgumentException ee) {
                    // don't know what it is
                }
            }
            stack[i + 18] = is;
        }
        ItemStack result = LabBuilder.getLabProduct(lab);
        stack[0] = result;
        formula = lab.toString();
        showView();
    }

    private void showView() {
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close_im.setCustomModelData(1);
        close.setItemMeta(close_im);
        stack[26] = close;
        Inventory viewer = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + formula.replace("_", " ") + " Formula");
        viewer.setContents(stack);
        player.openInventory(viewer);
    }
}
