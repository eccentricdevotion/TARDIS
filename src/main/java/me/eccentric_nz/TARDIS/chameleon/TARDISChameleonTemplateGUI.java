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
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChameleonTemplateGUI {

    private final TARDIS plugin;
    private final ItemStack[] template;

    public TARDISChameleonTemplateGUI(TARDIS plugin) {
        this.plugin = plugin;
        this.template = getItemStack();
    }

    private ItemStack[] getItemStack() {

        // back button
        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName(plugin.getChameleonGuis().getString("BACK_HELP"));
        back.setItemMeta(bk);
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta io = info.getItemMeta();
        io.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        List<String> infoListTemp = plugin.getChameleonGuis().getStringList("INFO_TEMPLATE");
        io.setLore(Arrays.asList(infoListTemp.get(0), infoListTemp.get(1), infoListTemp.get(2), infoListTemp.get(3)));
        info.setItemMeta(io);
        // next button
        ItemStack next = new ItemStack(Material.ARROW, 1);
        ItemMeta nt = next.getItemMeta();
        nt.setDisplayName(plugin.getChameleonGuis().getString("GO_CONSTRUCT"));
        next.setItemMeta(nt);
        // one
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta oe = one.getItemMeta();
        oe.setDisplayName("1");
        oe.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_L_FRONT")));
        one.setItemMeta(oe);
        // two
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta to = two.getItemMeta();
        to.setDisplayName("2");
        to.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_L_MIDDLE")));
        two.setItemMeta(to);
        // three
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta te = three.getItemMeta();
        te.setDisplayName("3");
        te.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_L_BACK")));
        three.setItemMeta(te);
        // four
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta fr = four.getItemMeta();
        fr.setDisplayName("4");
        fr.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_B_MIDDLE")));
        four.setItemMeta(fr);
        // five
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta fe = five.getItemMeta();
        fe.setDisplayName("5");
        fe.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_R_BACK")));
        five.setItemMeta(fe);
        // six
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta sx = six.getItemMeta();
        sx.setDisplayName("6");
        sx.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_R_MIDDLE")));
        six.setItemMeta(sx);
        // seven
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta sn = seven.getItemMeta();
        sn.setDisplayName("7");
        sn.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_R_FRONT")));
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta et = eight.getItemMeta();
        et.setDisplayName("8");
        et.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_F_MIDDLE")));
        eight.setItemMeta(et);
        // nine
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta ne = nine.getItemMeta();
        ne.setDisplayName("9");
        ne.setLore(Arrays.asList(plugin.getChameleonGuis().getString("COL_C_LAMP")));
        nine.setItemMeta(ne);
        // redstone lamp
        ItemStack lamp = new ItemStack(Material.REDSTONE_LAMP_OFF, 1);
        ItemMeta lp = lamp.getItemMeta();
        List<String> lampList = plugin.getChameleonGuis().getStringList("PB_LAMP");
        lp.setDisplayName(lampList.get(0));
        lp.setLore(Arrays.asList(lampList.get(1), lampList.get(2)));
        lamp.setItemMeta(lp);
        // redstone block
        ItemStack power = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta pr = power.getItemMeta();
        pr.setDisplayName(plugin.getChameleonGuis().getString("POWER"));
        power.setItemMeta(pr);
        // stone slab
        ItemStack slab = new ItemStack(Material.STEP, 1);
        ItemMeta sb = slab.getItemMeta();
        sb.setDisplayName(plugin.getChameleonGuis().getString("PB_SIGN"));
        slab.setItemMeta(sb);
        // blue wool
        ItemStack blue = new ItemStack(Material.WOOL, 1, (byte) 11);
        ItemMeta be = blue.getItemMeta();
        be.setDisplayName(plugin.getChameleonGuis().getString("PB_WALL"));
        blue.setItemMeta(be);
        // iron door
        ItemStack door = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta dr = door.getItemMeta();
        List<String> doorList = plugin.getChameleonGuis().getStringList("PB_DOOR");
        dr.setDisplayName(doorList.get(0));
        dr.setLore(Arrays.asList(doorList.get(1), doorList.get(2)));
        door.setItemMeta(dr);

        ItemStack[] is = {
            back, null, null, null, info, null, null, null, next,
            one, two, three, four, five, six, seven, eight, nine,
            slab, slab, slab, slab, slab, slab, slab, slab, lamp,
            blue, blue, blue, blue, blue, blue, blue, blue, power,
            blue, blue, blue, blue, blue, blue, blue, door, null,
            blue, blue, blue, blue, blue, blue, blue, door, null
        };
        return is;
    }

    public ItemStack[] getTemplate() {
        return template;
    }
}
