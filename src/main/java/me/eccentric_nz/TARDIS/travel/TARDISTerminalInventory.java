/*
 * Copyright (C) 2019 eccentric_nz
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
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * John Lumic was a business tycoon, owner of Cybus Industries and the creator of the Cybermen. Though he publicly
 * denied rumours of ill health, Lumic suffered from a terminal illness and used a motorized wheelchair as transport.
 *
 * @author eccentric_nz
 */
public class TARDISTerminalInventory {

    private final ItemStack[] terminal;
    private final TARDIS plugin;

    public TARDISTerminalInventory(TARDIS plugin) {
        this.plugin = plugin;
        terminal = getItemStack();
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
        ItemStack ten = new ItemStack(Material.WHITE_WOOL, 1);
        ItemMeta im10 = ten.getItemMeta();
        im10.setDisplayName(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (10 * step));
        ten.setItemMeta(im10);
        // 25
        ItemStack twentyfive = new ItemStack(Material.LIGHT_GRAY_WOOL, 1);
        ItemMeta im25 = twentyfive.getItemMeta();
        im25.setDisplayName(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (25 * step));
        twentyfive.setItemMeta(im25);
        // 50
        ItemStack fifty = new ItemStack(Material.GRAY_WOOL, 1);
        ItemMeta im50 = fifty.getItemMeta();
        im50.setDisplayName(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (50 * step));
        fifty.setItemMeta(im50);
        // 100
        ItemStack onehundred = new ItemStack(Material.BLACK_WOOL, 1);
        ItemMeta im100 = onehundred.getItemMeta();
        im100.setDisplayName(plugin.getLanguage().getString("BUTTON_STEP") + ": " + (100 * step));
        onehundred.setItemMeta(im100);
        // -ve
        ItemStack neg = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta nim = neg.getItemMeta();
        nim.setDisplayName(plugin.getLanguage().getString("BUTTON_NEG"));
        neg.setItemMeta(nim);
        // +ve
        ItemStack pos = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta pim = pos.getItemMeta();
        pim.setDisplayName(plugin.getLanguage().getString("BUTTON_POS"));
        pos.setItemMeta(pim);
        // x
        ItemStack x = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
        ItemMeta xim = x.getItemMeta();
        xim.setDisplayName("X");
        xim.setLore(Collections.singletonList("0"));
        x.setItemMeta(xim);
        // z
        ItemStack z = new ItemStack(Material.YELLOW_WOOL, 1);
        ItemMeta zim = z.getItemMeta();
        zim.setDisplayName("Z");
        zim.setLore(Collections.singletonList("0"));
        z.setItemMeta(zim);
        // multiplier
        ItemStack m = new ItemStack(Material.PURPLE_WOOL, 1);
        ItemMeta mim = m.getItemMeta();
        mim.setDisplayName(plugin.getLanguage().getString("BUTTON_MULTI"));
        mim.setLore(Collections.singletonList("x1"));
        m.setItemMeta(mim);
        // environments
        // current
        ItemStack u = new ItemStack(Material.OAK_LEAVES, 1);
        ItemMeta uim = u.getItemMeta();
        uim.setDisplayName(plugin.getLanguage().getString("BUTTON_CURRENT"));
        u.setItemMeta(uim);
        // normal
        ItemStack w = new ItemStack(Material.DIRT, 1);
        ItemMeta wim = w.getItemMeta();
        wim.setDisplayName(plugin.getLanguage().getString("BUTTON_NORM"));
        w.setItemMeta(wim);
        // nether
        ItemStack r;
        String ndn;
        if (plugin.getConfig().getBoolean("travel.nether") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
            r = new ItemStack(Material.NETHERRACK, 1);
            ndn = "Nether";
        } else {
            r = new ItemStack(Material.PODZOL, 1);
            ndn = plugin.getConfig().getString("travel.terminal.nether");
        }
        ItemMeta rim = r.getItemMeta();
        rim.setDisplayName(ndn);
        r.setItemMeta(rim);
        // the end
        ItemStack e;
        String edn;
        if (plugin.getConfig().getBoolean("travel.nether") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
            e = new ItemStack(Material.END_STONE, 1);
            edn = "The End";
        } else {
            e = new ItemStack(Material.COARSE_DIRT, 1);
            edn = plugin.getConfig().getString("travel.terminal.the_end");
        }
        ItemMeta eim = e.getItemMeta();
        eim.setDisplayName(edn);
        e.setItemMeta(eim);
        // submarine
        ItemStack sub = new ItemStack(Material.WATER_BUCKET, 1);
        ItemMeta subim = sub.getItemMeta();
        subim.setDisplayName(plugin.getLanguage().getString("BUTTON_SUB"));
        sub.setItemMeta(subim);
        // test
        ItemStack t = new ItemStack(Material.PISTON, 1);
        ItemMeta tim = t.getItemMeta();
        tim.setDisplayName(plugin.getLanguage().getString("BUTTON_CHECK"));
        t.setItemMeta(tim);
        // set
        ItemStack s = new ItemStack(Material.BOOKSHELF, 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DEST"));
        s.setItemMeta(sim);
        // cancel
        ItemStack c = new ItemStack(Material.TNT, 1);
        ItemMeta cim = c.getItemMeta();
        cim.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
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

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
