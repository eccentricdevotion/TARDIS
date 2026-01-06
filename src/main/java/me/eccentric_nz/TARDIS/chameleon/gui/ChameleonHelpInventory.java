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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonHelp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class ChameleonHelpInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ChameleonHelpInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Chameleon Help", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {

        // back
        ItemStack back = ItemStack.of(GUIChameleonHelp.BACK_CHAM_OPTS.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.displayName(Component.text(plugin.getChameleonGuis().getString("BACK_CONSTRUCT")));
        back.setItemMeta(bk);
        // help
        ItemStack info = ItemStack.of(GUIChameleonHelp.INFO_HELP_1.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> ioLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_1")) {
            ioLore.add(Component.text(s));
        }
        io.lore(ioLore);
        info.setItemMeta(io);
        // help
        ItemStack info2 = ItemStack.of(GUIChameleonHelp.INFO_HELP_2.material(), 1);
        ItemMeta io2 = info2.getItemMeta();
        io2.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> io2Lore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_2")) {
            io2Lore.add(Component.text(s));
        }
        io2.lore(io2Lore);
        info2.setItemMeta(io2);
        // one
        ItemStack one = ItemStack.of(GUIChameleonHelp.COL_L_FRONT.material(), 1);
        ItemMeta oe = one.getItemMeta();
        oe.displayName(Component.text("1"));
        oe.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_L_FRONT"))));
        one.setItemMeta(oe);
        // two
        ItemStack two = ItemStack.of(GUIChameleonHelp.COL_L_MIDDLE.material(), 1);
        ItemMeta to = two.getItemMeta();
        to.displayName(Component.text("2"));
        to.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_L_MIDDLE"))));
        two.setItemMeta(to);
        // three
        ItemStack three = ItemStack.of(GUIChameleonHelp.COL_L_BACK.material(), 1);
        ItemMeta te = three.getItemMeta();
        te.displayName(Component.text("3"));
        te.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_L_BACK"))));
        three.setItemMeta(te);
        // four
        ItemStack four = ItemStack.of(GUIChameleonHelp.COL_B_MIDDLE.material(), 1);
        ItemMeta fr = four.getItemMeta();
        fr.displayName(Component.text("4"));
        fr.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_B_MIDDLE"))));
        four.setItemMeta(fr);
        // five
        ItemStack five = ItemStack.of(GUIChameleonHelp.COL_R_BACK.material(), 1);
        ItemMeta fe = five.getItemMeta();
        fe.displayName(Component.text("5"));
        fe.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_R_BACK"))));
        five.setItemMeta(fe);
        // six
        ItemStack six = ItemStack.of(GUIChameleonHelp.COL_R_MIDDLE.material(), 1);
        ItemMeta sx = six.getItemMeta();
        sx.displayName(Component.text("6"));
        sx.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_R_MIDDLE"))));
        six.setItemMeta(sx);
        // seven
        ItemStack seven = ItemStack.of(GUIChameleonHelp.COL_R_FRONT.material(), 1);
        ItemMeta sn = seven.getItemMeta();
        sn.displayName(Component.text("7"));
        sn.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_R_FRONT"))));
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = ItemStack.of(GUIChameleonHelp.COL_F_MIDDLE.material(), 1);
        ItemMeta et = eight.getItemMeta();
        et.displayName(Component.text("8"));
        et.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_F_MIDDLE"))));
        eight.setItemMeta(et);
        // nine
        ItemStack nine = ItemStack.of(GUIChameleonHelp.COL_C_LAMP.material(), 1);
        ItemMeta ne = nine.getItemMeta();
        ne.displayName(Component.text("9"));
        ne.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_C_LAMP"))));
        nine.setItemMeta(ne);
        // grid
        ItemStack grid = ItemStack.of(GUIChameleonHelp.INFO_HELP_3.material(), 1);
        ItemMeta gd = grid.getItemMeta();
        gd.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> gdLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_3")) {
            gdLore.add(Component.text(s));
        }
        gd.lore(gdLore);
        grid.setItemMeta(gd);
        // grid
        ItemStack column = ItemStack.of(GUIChameleonHelp.INFO_HELP_4.material(), 1);
        ItemMeta cn = column.getItemMeta();
        cn.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> cnLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_4")) {
            cnLore.add(Component.text(s));
        }
        cn.lore(cnLore);
        column.setItemMeta(cn);
        // example
        ItemStack example = ItemStack.of(GUIChameleonHelp.VIEW_TEMP.material(), 1);
        ItemMeta ee = example.getItemMeta();
        ee.displayName(Component.text(plugin.getChameleonGuis().getString("VIEW_TEMP")));
        example.setItemMeta(ee);
        // one
        ItemStack o = ItemStack.of(GUIChameleonHelp.ROW_1.material(), 1);
        ItemMeta en = o.getItemMeta();
        en.displayName(Component.text("1"));
        o.setItemMeta(en);
        // two
        ItemStack w = ItemStack.of(GUIChameleonHelp.ROW_2.material(), 1);
        ItemMeta wo = w.getItemMeta();
        wo.displayName(Component.text("2"));
        w.setItemMeta(wo);
        // three
        ItemStack t = ItemStack.of(GUIChameleonHelp.ROW_3.material(), 1);
        ItemMeta hr = t.getItemMeta();
        hr.displayName(Component.text("3"));
        t.setItemMeta(hr);
        // four
        ItemStack f = ItemStack.of(GUIChameleonHelp.ROW_4.material(), 1);
        ItemMeta ou = f.getItemMeta();
        ou.displayName(Component.text("4"));
        f.setItemMeta(ou);

        return new ItemStack[]{
                back, null, null, info, info2, null, null, null, null,
                null, null, null, null, null, null, null, column, null,
                null, grid, null, null, null, null, null, f, null,
                three, four, five, null, null, null, null, t, null,
                two, nine, six, null, example, null, null, w, null,
                one, eight, seven, null, null, null, null, o, null
        };
    }
}
