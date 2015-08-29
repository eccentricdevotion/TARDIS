/*
 * Copyright (C) 2015 eccentric_nz
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

import java.util.Arrays;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChameleonHelpGUI {

    private final TARDIS plugin;
    private final ItemStack[] help;

    public TARDISChameleonHelpGUI(TARDIS plugin) {
        this.plugin = plugin;
        this.help = getItemStack();
    }

    private ItemStack[] getItemStack() {

        // back
        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName("Back to construction");
        back.setItemMeta(bk);
        // help
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta io = info.getItemMeta();
        io.setDisplayName("Info");
        io.setLore(Arrays.asList("A custom chameleon preset", "is made up of 9 columns of", "4 blocks.", "We need to roll the columns", "out flat just like a map of", "the (Earth's) globe."));
        info.setItemMeta(io);
        // help
        ItemStack info2 = new ItemStack(Material.BOWL, 1);
        ItemMeta io2 = info2.getItemMeta();
        io2.setDisplayName("Info");
        io2.setLore(Arrays.asList("The order of the columns before", "they are 'unrolled' is shown below left.", "Click the 'View example template'", "button to see what they look like", "unrolled, ready for construction."));
        info2.setItemMeta(io2);
        // one
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta oe = one.getItemMeta();
        oe.setDisplayName("1");
        oe.setLore(Arrays.asList("Left-side front column"));
        one.setItemMeta(oe);
        // two
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta to = two.getItemMeta();
        to.setDisplayName("2");
        to.setLore(Arrays.asList("Left-side middle column"));
        two.setItemMeta(to);
        // three
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta te = three.getItemMeta();
        te.setDisplayName("3");
        te.setLore(Arrays.asList("Left-side back column"));
        three.setItemMeta(te);
        // four
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta fr = four.getItemMeta();
        fr.setDisplayName("4");
        fr.setLore(Arrays.asList("Back middle column"));
        four.setItemMeta(fr);
        // five
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta fe = five.getItemMeta();
        fe.setDisplayName("5");
        fe.setLore(Arrays.asList("Right-side back column"));
        five.setItemMeta(fe);
        // six
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta sx = six.getItemMeta();
        sx.setDisplayName("6");
        sx.setLore(Arrays.asList("Right-side middle column"));
        six.setItemMeta(sx);
        // seven
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta sn = seven.getItemMeta();
        sn.setDisplayName("7");
        sn.setLore(Arrays.asList("Right-side front column"));
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta et = eight.getItemMeta();
        et.setDisplayName("8");
        et.setLore(Arrays.asList("Front middle (with door) column"));
        eight.setItemMeta(et);
        // nine
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta ne = nine.getItemMeta();
        ne.setDisplayName("9");
        ne.setLore(Arrays.asList("Centre (with lamp) column"));
        nine.setItemMeta(ne);
        // grid
        ItemStack grid = new ItemStack(Material.BOWL, 1);
        ItemMeta gd = grid.getItemMeta();
        gd.setDisplayName("Info");
        gd.setLore(Arrays.asList("Position of chameleon columns", "looking down from above."));
        grid.setItemMeta(gd);
        // grid
        ItemStack column = new ItemStack(Material.BOWL, 1);
        ItemMeta cn = column.getItemMeta();
        cn.setDisplayName("Info");
        cn.setLore(Arrays.asList("One chameleon column", "looking from the side."));
        column.setItemMeta(cn);
        // example
        ItemStack example = new ItemStack(Material.BOWL, 1);
        ItemMeta ee = example.getItemMeta();
        ee.setDisplayName("View example template");
        example.setItemMeta(ee);
        // one
        ItemStack o = new ItemStack(Material.BOWL, 1);
        ItemMeta en = o.getItemMeta();
        en.setDisplayName("1");
        o.setItemMeta(en);
        // two
        ItemStack w = new ItemStack(Material.BOWL, 1);
        ItemMeta wo = w.getItemMeta();
        wo.setDisplayName("2");
        w.setItemMeta(wo);
        // three
        ItemStack t = new ItemStack(Material.BOWL, 1);
        ItemMeta hr = t.getItemMeta();
        hr.setDisplayName("3");
        t.setItemMeta(hr);
        // four
        ItemStack f = new ItemStack(Material.BOWL, 1);
        ItemMeta ou = f.getItemMeta();
        ou.setDisplayName("4");
        f.setItemMeta(ou);

        ItemStack[] is = {
            back, null, null, info, info2, null, null, null, null,
            null, null, null, null, null, null, null, column, null,
            null, grid, null, null, null, null, null, f, null,
            three, four, five, null, null, null, null, t, null,
            two, nine, six, null, example, null, null, w, null,
            one, eight, seven, null, null, null, null, o, null
        };
        return is;
    }

    public ItemStack[] getHelp() {
        return help;
    }
}
