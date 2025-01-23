/*
 * Copyright (C) 2024 eccentric_nz
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
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
        bk.displayName(Component.text(plugin.getChameleonGuis().getString("BACK_CONSTRUCT")));
        bk.setItemModel(GUIChameleonHelp.BACK_CHAM_OPTS.key());
        back.setItemMeta(bk);
        // help
        ItemStack info = new ItemStack(GUIChameleonHelp.INFO_HELP_1.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.displayName(Component.text(plugin.getChameleonGuis().getString("INFO")));
        List<TextComponent> hlore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_1")) {
            hlore.add(Component.text(s));
        }
        io.lore(hlore);
        io.setItemModel(GUIChameleonHelp.INFO_HELP_1.key());
        info.setItemMeta(io);
        // help
        ItemStack info2 = new ItemStack(GUIChameleonHelp.INFO_HELP_2.material(), 1);
        ItemMeta io2 = info2.getItemMeta();
        io2.displayName(Component.text(plugin.getChameleonGuis().getString("INFO")));
        List<TextComponent> lore2 = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_2")) {
            lore2.add(Component.text(s));
        }
        io2.lore(lore2);
        io2.setItemModel(GUIChameleonHelp.INFO_HELP_2.key());
        info2.setItemMeta(io2);
        // one
        ItemStack one = new ItemStack(GUIChameleonHelp.COL_L_FRONT.material(), 1);
        ItemMeta oe = one.getItemMeta();
        oe.displayName(Component.text("1"));
        List<TextComponent> lore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_L_FRONT")) {
            lore.add(Component.text(s));
        }
        oe.lore(lore);
        oe.setItemModel(GUIChameleonHelp.COL_L_FRONT.key());
        one.setItemMeta(oe);
        // two
        ItemStack two = new ItemStack(GUIChameleonHelp.COL_L_MIDDLE.material(), 1);
        ItemMeta to = two.getItemMeta();
        to.displayName(Component.text("2"));
        List<TextComponent> wlore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_L_MIDDLE")) {
            wlore.add(Component.text(s));
        }
        to.lore(wlore);
        to.setItemModel(GUIChameleonHelp.COL_L_MIDDLE.key());
        two.setItemMeta(to);
        // three
        ItemStack three = new ItemStack(GUIChameleonHelp.COL_L_BACK.material(), 1);
        ItemMeta te = three.getItemMeta();
        te.displayName(Component.text("3"));
        List<TextComponent> tlore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_L_BACK")) {
            tlore.add(Component.text(s));
        }
        te.lore(tlore);
        te.setItemModel(GUIChameleonHelp.COL_L_BACK.key());
        three.setItemMeta(te);
        // four
        ItemStack four = new ItemStack(GUIChameleonHelp.COL_B_MIDDLE.material(), 1);
        ItemMeta fr = four.getItemMeta();
        fr.displayName(Component.text("4"));
        List<TextComponent> olore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_B_MIDDLE")) {
            olore.add(Component.text(s));
        }
        fr.lore(olore);
        fr.setItemModel(GUIChameleonHelp.COL_B_MIDDLE.key());
        four.setItemMeta(fr);
        // five
        ItemStack five = new ItemStack(GUIChameleonHelp.COL_R_BACK.material(), 1);
        ItemMeta fe = five.getItemMeta();
        fe.displayName(Component.text("5"));
        List<TextComponent> flore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_R_BACK")) {
            flore.add(Component.text(s));
        }
        fe.lore(flore);
        fe.setItemModel(GUIChameleonHelp.COL_R_BACK.key());
        five.setItemMeta(fe);
        // six
        ItemStack six = new ItemStack(GUIChameleonHelp.COL_R_MIDDLE.material(), 1);
        ItemMeta sx = six.getItemMeta();
        sx.displayName(Component.text("6"));
        List<TextComponent> xlore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_R_MIDDLE")) {
            xlore.add(Component.text(s));
        }
        sx.lore(xlore);
        sx.setItemModel(GUIChameleonHelp.COL_R_MIDDLE.key());
        six.setItemMeta(sx);
        // seven
        ItemStack seven = new ItemStack(GUIChameleonHelp.COL_R_FRONT.material(), 1);
        ItemMeta sn = seven.getItemMeta();
        sn.displayName(Component.text("7"));
        List<TextComponent> slore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_R_FRONT")) {
            slore.add(Component.text(s));
        }
        sn.lore(slore);
        sn.setItemModel(GUIChameleonHelp.COL_R_FRONT.key());
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = new ItemStack(GUIChameleonHelp.COL_F_MIDDLE.material(), 1);
        ItemMeta et = eight.getItemMeta();
        et.displayName(Component.text("8"));
        List<TextComponent> elore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_F_MIDDLE")) {
            elore.add(Component.text(s));
        }
        et.lore(elore);
        et.setItemModel(GUIChameleonHelp.COL_F_MIDDLE.key());
        eight.setItemMeta(et);
        // nine
        ItemStack nine = new ItemStack(GUIChameleonHelp.COL_C_LAMP.material(), 1);
        ItemMeta ne = nine.getItemMeta();
        ne.displayName(Component.text("9"));
        List<TextComponent> nlore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("COL_C_LAMP")) {
            nlore.add(Component.text(s));
        }
        ne.lore(nlore);
        ne.setItemModel(GUIChameleonHelp.COL_C_LAMP.key());
        nine.setItemMeta(ne);
        // grid
        ItemStack grid = new ItemStack(GUIChameleonHelp.INFO_HELP_3.material(), 1);
        ItemMeta gd = grid.getItemMeta();
        gd.displayName(Component.text(plugin.getChameleonGuis().getString("INFO")));
        List<TextComponent> gdlore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_3")) {
            gdlore.add(Component.text(s));
        }
        gd.lore(gdlore);
        gd.setItemModel(GUIChameleonHelp.INFO_HELP_3.key());
        grid.setItemMeta(gd);
        // grid
        ItemStack column = new ItemStack(GUIChameleonHelp.INFO_HELP_4.material(), 1);
        ItemMeta cn = column.getItemMeta();
        cn.displayName(Component.text(plugin.getChameleonGuis().getString("INFO")));
        List<TextComponent> clore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_4")) {
            clore.add(Component.text(s));
        }
        cn.lore(clore);
        cn.setItemModel(GUIChameleonHelp.INFO_HELP_4.key());
        column.setItemMeta(cn);
        // example
        ItemStack example = new ItemStack(GUIChameleonHelp.VIEW_TEMP.material(), 1);
        ItemMeta ee = example.getItemMeta();
        ee.displayName(Component.text(plugin.getChameleonGuis().getString("VIEW_TEMP")));
        ee.setItemModel(GUIChameleonHelp.VIEW_TEMP.key());
        example.setItemMeta(ee);
        // one
        ItemStack o = new ItemStack(GUIChameleonHelp.ROW_1.material(), 1);
        ItemMeta en = o.getItemMeta();
        en.displayName(Component.text("1"));
        en.setItemModel(GUIChameleonHelp.ROW_1.key());
        o.setItemMeta(en);
        // two
        ItemStack w = new ItemStack(GUIChameleonHelp.ROW_2.material(), 1);
        ItemMeta wo = w.getItemMeta();
        wo.displayName(Component.text("2"));
        wo.setItemModel(GUIChameleonHelp.ROW_2.key());
        w.setItemMeta(wo);
        // three
        ItemStack t = new ItemStack(GUIChameleonHelp.ROW_3.material(), 1);
        ItemMeta hr = t.getItemMeta();
        hr.displayName(Component.text("3"));
        hr.setItemModel(GUIChameleonHelp.ROW_3.key());
        t.setItemMeta(hr);
        // four
        ItemStack f = new ItemStack(GUIChameleonHelp.ROW_4.material(), 1);
        ItemMeta ou = f.getItemMeta();
        ou.displayName(Component.text("4"));
        ou.setItemModel(GUIChameleonHelp.ROW_4.key());
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
