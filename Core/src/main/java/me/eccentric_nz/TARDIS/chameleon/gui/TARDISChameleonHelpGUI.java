/*
 * Copyright (C) 2025 eccentric_nz
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISChameleonHelpGUI {

    private final TARDIS plugin;
    private final ItemStack[] help;

    public TARDISChameleonHelpGUI(TARDIS plugin) {
        this.plugin = plugin;
        help = getItemStack();
    }

    private ItemStack[] getItemStack() {

        // back
        ItemStack back = new ItemStack(GUIChameleonHelp.BACK_CHAM_OPTS.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName(plugin.getChameleonGuis().getString("BACK_CONSTRUCT"));
        back.setItemMeta(bk);
        // help
        ItemStack info = new ItemStack(GUIChameleonHelp.INFO_HELP_1.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        io.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_1"));
        info.setItemMeta(io);
        // help
        ItemStack info2 = new ItemStack(GUIChameleonHelp.INFO_HELP_2.material(), 1);
        ItemMeta io2 = info2.getItemMeta();
        io2.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        io2.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_2"));
        info2.setItemMeta(io2);
        // one
        ItemStack one = new ItemStack(GUIChameleonHelp.COL_L_FRONT.material(), 1);
        ItemMeta oe = one.getItemMeta();
        oe.setDisplayName("1");
        oe.setLore(List.of(plugin.getChameleonGuis().getString("COL_L_FRONT")));
        one.setItemMeta(oe);
        // two
        ItemStack two = new ItemStack(GUIChameleonHelp.COL_L_MIDDLE.material(), 1);
        ItemMeta to = two.getItemMeta();
        to.setDisplayName("2");
        to.setLore(List.of(plugin.getChameleonGuis().getString("COL_L_MIDDLE")));
        two.setItemMeta(to);
        // three
        ItemStack three = new ItemStack(GUIChameleonHelp.COL_L_BACK.material(), 1);
        ItemMeta te = three.getItemMeta();
        te.setDisplayName("3");
        te.setLore(List.of(plugin.getChameleonGuis().getString("COL_L_BACK")));
        three.setItemMeta(te);
        // four
        ItemStack four = new ItemStack(GUIChameleonHelp.COL_B_MIDDLE.material(), 1);
        ItemMeta fr = four.getItemMeta();
        fr.setDisplayName("4");
        fr.setLore(List.of(plugin.getChameleonGuis().getString("COL_B_MIDDLE")));
        four.setItemMeta(fr);
        // five
        ItemStack five = new ItemStack(GUIChameleonHelp.COL_R_BACK.material(), 1);
        ItemMeta fe = five.getItemMeta();
        fe.setDisplayName("5");
        fe.setLore(List.of(plugin.getChameleonGuis().getString("COL_R_BACK")));
        five.setItemMeta(fe);
        // six
        ItemStack six = new ItemStack(GUIChameleonHelp.COL_R_MIDDLE.material(), 1);
        ItemMeta sx = six.getItemMeta();
        sx.setDisplayName("6");
        sx.setLore(List.of(plugin.getChameleonGuis().getString("COL_R_MIDDLE")));
        six.setItemMeta(sx);
        // seven
        ItemStack seven = new ItemStack(GUIChameleonHelp.COL_R_FRONT.material(), 1);
        ItemMeta sn = seven.getItemMeta();
        sn.setDisplayName("7");
        sn.setLore(List.of(plugin.getChameleonGuis().getString("COL_R_FRONT")));
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = new ItemStack(GUIChameleonHelp.COL_F_MIDDLE.material(), 1);
        ItemMeta et = eight.getItemMeta();
        et.setDisplayName("8");
        et.setLore(List.of(plugin.getChameleonGuis().getString("COL_F_MIDDLE")));
        eight.setItemMeta(et);
        // nine
        ItemStack nine = new ItemStack(GUIChameleonHelp.COL_C_LAMP.material(), 1);
        ItemMeta ne = nine.getItemMeta();
        ne.setDisplayName("9");
        ne.setLore(List.of(plugin.getChameleonGuis().getString("COL_C_LAMP")));
        nine.setItemMeta(ne);
        // grid
        ItemStack grid = new ItemStack(GUIChameleonHelp.INFO_HELP_3.material(), 1);
        ItemMeta gd = grid.getItemMeta();
        gd.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        gd.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_3"));
        grid.setItemMeta(gd);
        // grid
        ItemStack column = new ItemStack(GUIChameleonHelp.INFO_HELP_4.material(), 1);
        ItemMeta cn = column.getItemMeta();
        cn.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        cn.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_4"));
        column.setItemMeta(cn);
        // example
        ItemStack example = new ItemStack(GUIChameleonHelp.VIEW_TEMP.material(), 1);
        ItemMeta ee = example.getItemMeta();
        ee.setDisplayName(plugin.getChameleonGuis().getString("VIEW_TEMP"));
        example.setItemMeta(ee);
        // one
        ItemStack o = new ItemStack(GUIChameleonHelp.ROW_1.material(), 1);
        ItemMeta en = o.getItemMeta();
        en.setDisplayName("1");
        o.setItemMeta(en);
        // two
        ItemStack w = new ItemStack(GUIChameleonHelp.ROW_2.material(), 1);
        ItemMeta wo = w.getItemMeta();
        wo.setDisplayName("2");
        w.setItemMeta(wo);
        // three
        ItemStack t = new ItemStack(GUIChameleonHelp.ROW_3.material(), 1);
        ItemMeta hr = t.getItemMeta();
        hr.setDisplayName("3");
        t.setItemMeta(hr);
        // four
        ItemStack f = new ItemStack(GUIChameleonHelp.ROW_4.material(), 1);
        ItemMeta ou = f.getItemMeta();
        ou.setDisplayName("4");
        f.setItemMeta(ou);

        return new ItemStack[]{back, null, null, info, info2, null, null, null, null, null, null, null, null, null, null, null, column, null, null, grid, null, null, null, null, null, f, null, three, four, five, null, null, null, null, t, null, two, nine, six, null, example, null, null, w, null, one, eight, seven, null, null, null, null, o, null};
    }

    public ItemStack[] getHelp() {
        return help;
    }
}
