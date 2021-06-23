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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.custommodeldata.GuiChameleonTemplate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TardisChameleonTemplateGui {

    private final TardisPlugin plugin;
    private final ItemStack[] template;

    TardisChameleonTemplateGui(TardisPlugin plugin) {
        this.plugin = plugin;
        template = getItemStack();
    }

    private ItemStack[] getItemStack() {

        // back button
        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta bk = back.getItemMeta();
        assert bk != null;
        bk.setDisplayName(plugin.getChameleonGuis().getString("BACK_HELP"));
        bk.setCustomModelData(GuiChameleonTemplate.BACK_HELP.getCustomModelData());
        back.setItemMeta(bk);
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta io = info.getItemMeta();
        assert io != null;
        io.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        io.setLore(plugin.getChameleonGuis().getStringList("INFO_TEMPLATE"));
        io.setCustomModelData(GuiChameleonTemplate.INFO_TEMPLATE.getCustomModelData());
        info.setItemMeta(io);
        // next button
        ItemStack next = new ItemStack(Material.ARROW, 1);
        ItemMeta nt = next.getItemMeta();
        assert nt != null;
        nt.setDisplayName(plugin.getChameleonGuis().getString("GO_CONSTRUCT"));
        next.setItemMeta(nt);
        // one
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta oe = one.getItemMeta();
        assert oe != null;
        oe.setDisplayName("1");
        oe.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_L_FRONT")));
        oe.setCustomModelData(GuiChameleonTemplate.COL_L_FRONT.getCustomModelData());
        one.setItemMeta(oe);
        // two
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta to = two.getItemMeta();
        assert to != null;
        to.setDisplayName("2");
        to.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_L_MIDDLE")));
        to.setCustomModelData(GuiChameleonTemplate.COL_L_MIDDLE.getCustomModelData());
        two.setItemMeta(to);
        // three
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta te = three.getItemMeta();
        assert te != null;
        te.setDisplayName("3");
        te.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_L_BACK")));
        te.setCustomModelData(GuiChameleonTemplate.COL_L_BACK.getCustomModelData());
        three.setItemMeta(te);
        // four
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta fr = four.getItemMeta();
        assert fr != null;
        fr.setDisplayName("4");
        fr.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_B_MIDDLE")));
        fr.setCustomModelData(GuiChameleonTemplate.COL_B_MIDDLE.getCustomModelData());
        four.setItemMeta(fr);
        // five
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta fe = five.getItemMeta();
        assert fe != null;
        fe.setDisplayName("5");
        fe.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_R_BACK")));
        fe.setCustomModelData(GuiChameleonTemplate.COL_R_BACK.getCustomModelData());
        five.setItemMeta(fe);
        // six
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta sx = six.getItemMeta();
        assert sx != null;
        sx.setDisplayName("6");
        sx.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_R_MIDDLE")));
        sx.setCustomModelData(GuiChameleonTemplate.COL_R_MIDDLE.getCustomModelData());
        six.setItemMeta(sx);
        // seven
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta sn = seven.getItemMeta();
        assert sn != null;
        sn.setDisplayName("7");
        sn.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_R_FRONT")));
        sn.setCustomModelData(GuiChameleonTemplate.COL_R_FRONT.getCustomModelData());
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta et = eight.getItemMeta();
        assert et != null;
        et.setDisplayName("8");
        et.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_F_MIDDLE")));
        et.setCustomModelData(GuiChameleonTemplate.COL_F_MIDDLE.getCustomModelData());
        eight.setItemMeta(et);
        // nine
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta ne = nine.getItemMeta();
        assert ne != null;
        ne.setDisplayName("9");
        ne.setLore(Collections.singletonList(plugin.getChameleonGuis().getString("COL_C_LAMP")));
        ne.setCustomModelData(GuiChameleonTemplate.COL_C_LAMP.getCustomModelData());
        nine.setItemMeta(ne);
        // redstone lamp
        ItemStack lamp = new ItemStack(Material.REDSTONE_LAMP, 1);
        ItemMeta lp = lamp.getItemMeta();
        List<String> lampList = plugin.getChameleonGuis().getStringList("PB_LAMP");
        assert lp != null;
        lp.setDisplayName(lampList.get(0));
        lp.setLore(Arrays.asList(lampList.get(1), lampList.get(2)));
        lamp.setItemMeta(lp);
        // redstone block
        ItemStack power = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta pr = power.getItemMeta();
        assert pr != null;
        pr.setDisplayName(plugin.getChameleonGuis().getString("POWER"));
        power.setItemMeta(pr);
        // stone slab
        ItemStack slab = new ItemStack(Material.STONE_SLAB, 1);
        ItemMeta sb = slab.getItemMeta();
        assert sb != null;
        sb.setDisplayName(plugin.getChameleonGuis().getString("PB_SIGN"));
        slab.setItemMeta(sb);
        // blue wool
        ItemStack blue = new ItemStack(Material.BLUE_WOOL, 1);
        ItemMeta be = blue.getItemMeta();
        assert be != null;
        be.setDisplayName(plugin.getChameleonGuis().getString("PB_WALL"));
        blue.setItemMeta(be);
        // iron door
        ItemStack door = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta dr = door.getItemMeta();
        List<String> doorList = plugin.getChameleonGuis().getStringList("PB_DOOR");
        assert dr != null;
        dr.setDisplayName(doorList.get(0));
        dr.setLore(Arrays.asList(doorList.get(1), doorList.get(2)));
        door.setItemMeta(dr);

        return new ItemStack[]{back, null, null, null, info, null, null, null, next, one, two, three, four, five, six, seven, eight, nine, slab, slab, slab, slab, slab, slab, slab, slab, lamp, blue, blue, blue, blue, blue, blue, blue, blue, power, blue, blue, blue, blue, blue, blue, blue, door, null, blue, blue, blue, blue, blue, blue, blue, door, null};
    }

    public ItemStack[] getTemplate() {
        return template;
    }
}
