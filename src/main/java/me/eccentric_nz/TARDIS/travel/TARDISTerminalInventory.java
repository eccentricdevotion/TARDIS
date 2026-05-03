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
package me.eccentric_nz.TARDIS.travel;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * John Lumic was a business tycoon, owner of Cybus Industries and the creator of the Cybermen. Though he publicly
 * denied rumours of ill health, Lumic suffered from a terminal illness and used a motorized wheelchair as transport.
 *
 * @author eccentric_nz
 */
public class TARDISTerminalInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISTerminalInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Destination Terminal", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Destination Terminal GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // steps
        int step = plugin.getConfig().getInt("travel.terminal_step");
        // 10
        ItemStack ten = ItemStack.of(Material.WHITE_WOOL, 1);
        ten.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (10 * step)));
        // 25
        ItemStack twentyfive = ItemStack.of(Material.LIGHT_GRAY_WOOL, 1);
        twentyfive.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (25 * step)));
        // 50
        ItemStack fifty = ItemStack.of(Material.GRAY_WOOL, 1);
        fifty.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (50 * step)));
        // 100
        ItemStack onehundred = ItemStack.of(Material.BLACK_WOOL, 1);
        onehundred.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (100 * step)));
        // -ve
        ItemStack neg = ItemStack.of(Material.RED_WOOL, 1);
        neg.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_NEG")));
        // +ve
        ItemStack pos = ItemStack.of(Material.LIME_WOOL, 1);
        pos.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_POS")));
        // x
        ItemStack x = ItemStack.of(Material.LIGHT_BLUE_WOOL, 1);
        x.setData(DataComponentTypes.CUSTOM_NAME, Component.text("X"));
        x.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("0")).build());
        // z
        ItemStack z = ItemStack.of(Material.YELLOW_WOOL, 1);
        z.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Z"));
        z.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("0")).build());
        // multiplier
        ItemStack m = ItemStack.of(Material.PURPLE_WOOL, 1);
        m.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MULTI")));
        m.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("x1")).build());
        // environments
        // current
        ItemStack u = ItemStack.of(Material.OAK_LEAVES, 1);
        u.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CURRENT")));
        // normal
        ItemStack w = ItemStack.of(Material.DIRT, 1);
        w.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_NORM")));
        // nether
        ItemStack r;
        String ndn;
        if (plugin.getConfig().getBoolean("travel.nether") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
            r = ItemStack.of(Material.NETHERRACK, 1);
            ndn = "Nether";
        } else {
            r = ItemStack.of(Material.PODZOL, 1);
            ndn = plugin.getConfig().getString("travel.terminal.nether");
        }
        r.setData(DataComponentTypes.CUSTOM_NAME, Component.text(ndn));
        // the end
        ItemStack e;
        String edn;
        if (plugin.getConfig().getBoolean("travel.the_end") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
            e = ItemStack.of(Material.END_STONE, 1);
            edn = "The End";
        } else {
            e = ItemStack.of(Material.COARSE_DIRT, 1);
            edn = plugin.getConfig().getString("travel.terminal.the_end");
        }
        e.setData(DataComponentTypes.CUSTOM_NAME, Component.text(edn));
        // submarine
        ItemStack sub = ItemStack.of(Material.WATER_BUCKET, 1);
        sub.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SUB")));
        // test
        ItemStack t = ItemStack.of(Material.PISTON, 1);
        t.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CHECK")));
        // set
        ItemStack s = ItemStack.of(Material.BOOKSHELF, 1);
        s.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DEST")));
        // cancel
        ItemStack c = ItemStack.of(Material.TNT, 1);
        c.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));

        return new ItemStack[]{
                null, ten, null, twentyfive, null, fifty, null, onehundred, null,
                neg, null, null, null, x, null, null, null, pos,
                neg, null, null, null, z, null, null, null, pos,
                neg, m, null, null, null, null, null, null, pos,
                u, null, w, null, r, null, e, null, sub,
                null, t, null, null, s, null, null, c, null
        };
    }
}
