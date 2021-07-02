/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonHelp;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * @author eccentric_nz
 */
class TARDISChameleonHelpGUI {

    private final TARDIS plugin;
    private final ItemStack[] help;

    TARDISChameleonHelpGUI(TARDIS plugin) {
        this.plugin = plugin;
        help = getItemStack();
    }

    private ItemStack[] getItemStack() {

        // back
        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta bk = back.getItemMeta();
        assert bk != null;
        bk.setDisplayName(plugin.getChameleonGuis().getString("BACK_CONSTRUCT"));
        back.setItemMeta(bk);
        // help
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta io = info.getItemMeta();
        assert io != null;
        io.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        io.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_1"));
        io.setCustomModelData(GUIChameleonHelp.INFO_HELP_1.getCustomModelData());
        info.setItemMeta(io);
        // help
        ItemStack info2 = new ItemStack(Material.BOWL, 1);
        ItemMeta io2 = info2.getItemMeta();
        assert io2 != null;
        io2.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        io2.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_2"));
        io2.setCustomModelData(GUIChameleonHelp.INFO_HELP_2.getCustomModelData());
        info2.setItemMeta(io2);
        // one
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta oe = one.getItemMeta();
        assert oe != null;
        oe.setDisplayName("1");
        oe.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_L_FRONT")));
        oe.setCustomModelData(GUIChameleonHelp.COL_L_FRONT.getCustomModelData());
        one.setItemMeta(oe);
        // two
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta to = two.getItemMeta();
        assert to != null;
        to.setDisplayName("2");
        to.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_L_MIDDLE")));
        to.setCustomModelData(GUIChameleonHelp.COL_L_MIDDLE.getCustomModelData());
        two.setItemMeta(to);
        // three
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta te = three.getItemMeta();
        assert te != null;
        te.setDisplayName("3");
        te.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_L_BACK")));
        te.setCustomModelData(GUIChameleonHelp.COL_L_BACK.getCustomModelData());
        three.setItemMeta(te);
        // four
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta fr = four.getItemMeta();
        assert fr != null;
        fr.setDisplayName("4");
        fr.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_B_MIDDLE")));
        fr.setCustomModelData(GUIChameleonHelp.COL_B_MIDDLE.getCustomModelData());
        four.setItemMeta(fr);
        // five
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta fe = five.getItemMeta();
        assert fe != null;
        fe.setDisplayName("5");
        fe.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_R_BACK")));
        fe.setCustomModelData(GUIChameleonHelp.COL_R_BACK.getCustomModelData());
        five.setItemMeta(fe);
        // six
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta sx = six.getItemMeta();
        assert sx != null;
        sx.setDisplayName("6");
        sx.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_R_MIDDLE")));
        sx.setCustomModelData(GUIChameleonHelp.COL_R_MIDDLE.getCustomModelData());
        six.setItemMeta(sx);
        // seven
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta sn = seven.getItemMeta();
        assert sn != null;
        sn.setDisplayName("7");
        sn.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_R_FRONT")));
        sn.setCustomModelData(GUIChameleonHelp.COL_R_FRONT.getCustomModelData());
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta et = eight.getItemMeta();
        assert et != null;
        et.setDisplayName("8");
        et.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_F_MIDDLE")));
        et.setCustomModelData(GUIChameleonHelp.COL_F_MIDDLE.getCustomModelData());
        eight.setItemMeta(et);
        // nine
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta ne = nine.getItemMeta();
        assert ne != null;
        ne.setDisplayName("9");
        ne.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_C_LAMP")));
        ne.setCustomModelData(GUIChameleonHelp.COL_C_LAMP.getCustomModelData());
        nine.setItemMeta(ne);
        // grid
        ItemStack grid = new ItemStack(Material.BOWL, 1);
        ItemMeta gd = grid.getItemMeta();
        assert gd != null;
        gd.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        gd.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_3"));
        gd.setCustomModelData(GUIChameleonHelp.INFO_HELP_3.getCustomModelData());
        grid.setItemMeta(gd);
        // grid
        ItemStack column = new ItemStack(Material.BOWL, 1);
        ItemMeta cn = column.getItemMeta();
        assert cn != null;
        cn.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        cn.setLore(plugin.getChameleonGuis().getStringList("INFO_HELP_4"));
        cn.setCustomModelData(GUIChameleonHelp.INFO_HELP_4.getCustomModelData());
        column.setItemMeta(cn);
        // example
        ItemStack example = new ItemStack(Material.BOWL, 1);
        ItemMeta ee = example.getItemMeta();
        assert ee != null;
        ee.setDisplayName(plugin.getChameleonGuis().getString("VIEW_TEMP"));
        ee.setCustomModelData(GUIChameleonHelp.VIEW_TEMP.getCustomModelData());
        example.setItemMeta(ee);
        // one
        ItemStack o = new ItemStack(Material.BOWL, 1);
        ItemMeta en = o.getItemMeta();
        assert en != null;
        en.setDisplayName("1");
        en.setCustomModelData(GUIChameleonHelp.ROW_1.getCustomModelData());
        o.setItemMeta(en);
        // two
        ItemStack w = new ItemStack(Material.BOWL, 1);
        ItemMeta wo = w.getItemMeta();
        assert wo != null;
        wo.setDisplayName("2");
        wo.setCustomModelData(GUIChameleonHelp.ROW_2.getCustomModelData());
        w.setItemMeta(wo);
        // three
        ItemStack t = new ItemStack(Material.BOWL, 1);
        ItemMeta hr = t.getItemMeta();
        assert hr != null;
        hr.setDisplayName("3");
        hr.setCustomModelData(GUIChameleonHelp.ROW_3.getCustomModelData());
        t.setItemMeta(hr);
        // four
        ItemStack f = new ItemStack(Material.BOWL, 1);
        ItemMeta ou = f.getItemMeta();
        assert ou != null;
        ou.setDisplayName("4");
        ou.setCustomModelData(GUIChameleonHelp.ROW_4.getCustomModelData());
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

    public ItemStack[] getHelp() {
        return help;
    }
}
