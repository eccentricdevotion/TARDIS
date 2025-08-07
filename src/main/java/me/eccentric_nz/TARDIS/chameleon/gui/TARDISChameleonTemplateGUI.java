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
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISChameleonTemplateGUI implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    TARDISChameleonTemplateGUI(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Chameleon Template", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {

        // back button
        ItemStack back = ItemStack.of(GUIChameleonTemplate.BACK_HELP.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.displayName(Component.text(plugin.getChameleonGuis().getString("BACK_HELP")));
        back.setItemMeta(bk);
        // info
        ItemStack info = ItemStack.of(GUIChameleonTemplate.INFO_TEMPLATE.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> ioLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_TEMPLATE")) {
            ioLore.add(Component.text(s));
        }
        io.lore(ioLore);
        info.setItemMeta(io);
        // next button
        ItemStack next = ItemStack.of(GUIChameleonTemplate.GO_CONSTRUCT.material(), 1);
        ItemMeta nt = next.getItemMeta();
        nt.displayName(Component.text(plugin.getChameleonGuis().getString("GO_CONSTRUCT")));
        next.setItemMeta(nt);
        // one
        ItemStack one = ItemStack.of(GUIChameleonTemplate.COL_L_FRONT.material(), 1);
        ItemMeta oe = one.getItemMeta();
        oe.displayName(Component.text("1"));
        oe.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_L_FRONT"))));
        one.setItemMeta(oe);
        // two
        ItemStack two = ItemStack.of(GUIChameleonTemplate.COL_L_MIDDLE.material(), 1);
        ItemMeta to = two.getItemMeta();
        to.displayName(Component.text("2"));
        to.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_L_MIDDLE"))));
        two.setItemMeta(to);
        // three
        ItemStack three = ItemStack.of(GUIChameleonTemplate.COL_L_BACK.material(), 1);
        ItemMeta te = three.getItemMeta();
        te.displayName(Component.text("3"));
        te.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_L_BACK"))));
        three.setItemMeta(te);
        // four
        ItemStack four = ItemStack.of(GUIChameleonTemplate.COL_B_MIDDLE.material(), 1);
        ItemMeta fr = four.getItemMeta();
        fr.displayName(Component.text("4"));
        fr.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_B_MIDDLE"))));
        four.setItemMeta(fr);
        // five
        ItemStack five = ItemStack.of(GUIChameleonTemplate.COL_R_BACK.material(), 1);
        ItemMeta fe = five.getItemMeta();
        fe.displayName(Component.text("5"));
        fe.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_R_BACK"))));
        five.setItemMeta(fe);
        // six
        ItemStack six = ItemStack.of(GUIChameleonTemplate.COL_R_MIDDLE.material(), 1);
        ItemMeta sx = six.getItemMeta();
        sx.displayName(Component.text("6"));
        sx.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_R_MIDDLE"))));
        six.setItemMeta(sx);
        // seven
        ItemStack seven = ItemStack.of(GUIChameleonTemplate.COL_R_FRONT.material(), 1);
        ItemMeta sn = seven.getItemMeta();
        sn.displayName(Component.text("7"));
        sn.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_R_FRONT"))));
        seven.setItemMeta(sn);
        // eight
        ItemStack eight = ItemStack.of(GUIChameleonTemplate.COL_F_MIDDLE.material(), 1);
        ItemMeta et = eight.getItemMeta();
        et.displayName(Component.text("8"));
        et.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_F_MIDDLE"))));
        eight.setItemMeta(et);
        // nine
        ItemStack nine = ItemStack.of(GUIChameleonTemplate.COL_C_LAMP.material(), 1);
        ItemMeta ne = nine.getItemMeta();
        ne.displayName(Component.text("9"));
        ne.lore(List.of(Component.text(plugin.getChameleonGuis().getString("COL_C_LAMP"))));
        nine.setItemMeta(ne);
        // redstone lamp
        ItemStack lamp = ItemStack.of(Material.REDSTONE_LAMP, 1);
        ItemMeta lp = lamp.getItemMeta();
        List<String> lampList = plugin.getChameleonGuis().getStringList("PB_LAMP");
        lp.displayName(Component.text(lampList.getFirst()));
        lp.lore(List.of(Component.text(lampList.get(1)), Component.text(lampList.get(2))));
        lamp.setItemMeta(lp);
        // redstone block
        ItemStack power = ItemStack.of(Material.REDSTONE_BLOCK, 1);
        ItemMeta pr = power.getItemMeta();
        pr.displayName(Component.text(plugin.getChameleonGuis().getString("POWER")));
        power.setItemMeta(pr);
        // stone slab
        ItemStack slab = ItemStack.of(Material.STONE_SLAB, 1);
        ItemMeta sb = slab.getItemMeta();
        sb.displayName(Component.text(plugin.getChameleonGuis().getString("PB_SIGN")));
        slab.setItemMeta(sb);
        // blue wool
        ItemStack blue = ItemStack.of(Material.BLUE_WOOL, 1);
        ItemMeta be = blue.getItemMeta();
        be.displayName(Component.text(plugin.getChameleonGuis().getString("PB_WALL")));
        blue.setItemMeta(be);
        // iron door
        ItemStack door = ItemStack.of(Material.IRON_DOOR, 1);
        ItemMeta dr = door.getItemMeta();
        List<String> doorList = plugin.getChameleonGuis().getStringList("PB_DOOR");
        dr.displayName(Component.text(doorList.getFirst()));
        dr.lore(List.of(Component.text(doorList.get(1)), Component.text(doorList.get(2))));
        door.setItemMeta(dr);

        return new ItemStack[]{
                back, null, null, null, info, null, null, null, next,
                one, two, three, four, five, six, seven, eight, nine,
                slab, slab, slab, slab, slab, slab, slab, slab, lamp,
                blue, blue, blue, blue, blue, blue, blue, blue, power,
                blue, blue, blue, blue, blue, blue, blue, door, null,
                blue, blue, blue, blue, blue, blue, blue, door, null
        };
    }
}
