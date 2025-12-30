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

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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
        ItemMeta im10 = ten.getItemMeta();
        im10.displayName(Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (10 * step)));
        ten.setItemMeta(im10);
        // 25
        ItemStack twentyfive = ItemStack.of(Material.LIGHT_GRAY_WOOL, 1);
        ItemMeta im25 = twentyfive.getItemMeta();
        im25.displayName(Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (25 * step)));
        twentyfive.setItemMeta(im25);
        // 50
        ItemStack fifty = ItemStack.of(Material.GRAY_WOOL, 1);
        ItemMeta im50 = fifty.getItemMeta();
        im50.displayName(Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (50 * step)));
        fifty.setItemMeta(im50);
        // 100
        ItemStack onehundred = ItemStack.of(Material.BLACK_WOOL, 1);
        ItemMeta im100 = onehundred.getItemMeta();
        im100.displayName(Component.text(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (100 * step)));
        onehundred.setItemMeta(im100);
        // -ve
        ItemStack neg = ItemStack.of(Material.RED_WOOL, 1);
        ItemMeta nim = neg.getItemMeta();
        nim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_NEG")));
        neg.setItemMeta(nim);
        // +ve
        ItemStack pos = ItemStack.of(Material.LIME_WOOL, 1);
        ItemMeta pim = pos.getItemMeta();
        pim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_POS")));
        pos.setItemMeta(pim);
        // x
        ItemStack x = ItemStack.of(Material.LIGHT_BLUE_WOOL, 1);
        ItemMeta xim = x.getItemMeta();
        xim.displayName(Component.text("X"));
        xim.lore(List.of(Component.text("0")));
        x.setItemMeta(xim);
        // z
        ItemStack z = ItemStack.of(Material.YELLOW_WOOL, 1);
        ItemMeta zim = z.getItemMeta();
        zim.displayName(Component.text("Z"));
        zim.lore(List.of(Component.text("0")));
        z.setItemMeta(zim);
        // multiplier
        ItemStack m = ItemStack.of(Material.PURPLE_WOOL, 1);
        ItemMeta mim = m.getItemMeta();
        mim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MULTI")));
        mim.lore(List.of(Component.text("x1")));
        m.setItemMeta(mim);
        // environments
        // current
        ItemStack u = ItemStack.of(Material.OAK_LEAVES, 1);
        ItemMeta uim = u.getItemMeta();
        uim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CURRENT")));
        u.setItemMeta(uim);
        // normal
        ItemStack w = ItemStack.of(Material.DIRT, 1);
        ItemMeta wim = w.getItemMeta();
        wim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_NORM")));
        w.setItemMeta(wim);
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
        ItemMeta rim = r.getItemMeta();
        rim.displayName(Component.text(ndn));
        r.setItemMeta(rim);
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
        ItemMeta eim = e.getItemMeta();
        eim.displayName(Component.text(edn));
        e.setItemMeta(eim);
        // submarine
        ItemStack sub = ItemStack.of(Material.WATER_BUCKET, 1);
        ItemMeta subim = sub.getItemMeta();
        subim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SUB")));
        sub.setItemMeta(subim);
        // test
        ItemStack t = ItemStack.of(Material.PISTON, 1);
        ItemMeta tim = t.getItemMeta();
        tim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CHECK")));
        t.setItemMeta(tim);
        // set
        ItemStack s = ItemStack.of(Material.BOOKSHELF, 1);
        ItemMeta sim = s.getItemMeta();
        sim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DEST")));
        s.setItemMeta(sim);
        // cancel
        ItemStack c = ItemStack.of(Material.TNT, 1);
        ItemMeta cim = c.getItemMeta();
        cim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));
        c.setItemMeta(cim);

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
