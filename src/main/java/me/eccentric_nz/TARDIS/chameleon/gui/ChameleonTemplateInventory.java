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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
class ChameleonTemplateInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    ChameleonTemplateInventory(TARDIS plugin) {
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
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("BACK_HELP")));
        // info
        ItemStack info = ItemStack.of(GUIChameleonTemplate.INFO_TEMPLATE.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> ioLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_TEMPLATE")) {
            ioLore.add(Component.text(s));
        }
        info.setData(DataComponentTypes.LORE, ItemLore.lore(ioLore));
        // next button
        ItemStack next = ItemStack.of(GUIChameleonTemplate.GO_CONSTRUCT.material(), 1);
        next.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("GO_CONSTRUCT")));
        // one
        ItemStack one = ItemStack.of(GUIChameleonTemplate.COL_L_FRONT.material(), 1);
        one.setData(DataComponentTypes.CUSTOM_NAME, Component.text("1"));
        one.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_L_FRONT"))).build());
        // two
        ItemStack two = ItemStack.of(GUIChameleonTemplate.COL_L_MIDDLE.material(), 1);
        two.setData(DataComponentTypes.CUSTOM_NAME, Component.text("2"));
        two.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_L_MIDDLE"))).build());
        // three
        ItemStack three = ItemStack.of(GUIChameleonTemplate.COL_L_BACK.material(), 1);
        three.setData(DataComponentTypes.CUSTOM_NAME, Component.text("3"));
        three.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_L_BACK"))).build());
        // four
        ItemStack four = ItemStack.of(GUIChameleonTemplate.COL_B_MIDDLE.material(), 1);
        four.setData(DataComponentTypes.CUSTOM_NAME, Component.text("4"));
        four.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_B_MIDDLE"))).build());
        // five
        ItemStack five = ItemStack.of(GUIChameleonTemplate.COL_R_BACK.material(), 1);
        five.setData(DataComponentTypes.CUSTOM_NAME, Component.text("5"));
        five.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_R_BACK"))).build());
        // six
        ItemStack six = ItemStack.of(GUIChameleonTemplate.COL_R_MIDDLE.material(), 1);
        six.setData(DataComponentTypes.CUSTOM_NAME, Component.text("6"));
        six.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_R_MIDDLE"))).build());
        // seven
        ItemStack seven = ItemStack.of(GUIChameleonTemplate.COL_R_FRONT.material(), 1);
        seven.setData(DataComponentTypes.CUSTOM_NAME, Component.text("7"));
        seven.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_R_FRONT"))).build());
        // eight
        ItemStack eight = ItemStack.of(GUIChameleonTemplate.COL_F_MIDDLE.material(), 1);
        eight.setData(DataComponentTypes.CUSTOM_NAME, Component.text("8"));
        eight.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_F_MIDDLE"))).build());
        // nine
        ItemStack nine = ItemStack.of(GUIChameleonTemplate.COL_C_LAMP.material(), 1);
        nine.setData(DataComponentTypes.CUSTOM_NAME, Component.text("9"));
        nine.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_C_LAMP"))).build());
        // redstone lamp
        ItemStack lamp = ItemStack.of(Material.REDSTONE_LAMP, 1);
        List<String> lampList = plugin.getChameleonGuis().getStringList("PB_LAMP");
        lamp.setData(DataComponentTypes.CUSTOM_NAME, Component.text(lampList.getFirst()));
        lamp.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(lampList.get(1))).addLine(Component.text(lampList.get(2))).build());
        // redstone block
        ItemStack power = ItemStack.of(Material.REDSTONE_BLOCK, 1);
        power.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("POWER")));
        // stone slab
        ItemStack slab = ItemStack.of(Material.STONE_SLAB, 1);
        slab.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("PB_SIGN")));
        // blue wool
        ItemStack blue = ItemStack.of(Material.BLUE_WOOL, 1);
        blue.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("PB_WALL")));
        // iron door
        ItemStack door = ItemStack.of(Material.IRON_DOOR, 1);
        List<String> doorList = plugin.getChameleonGuis().getStringList("PB_DOOR");
        door.setData(DataComponentTypes.CUSTOM_NAME, Component.text(doorList.getFirst()));
        door.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(doorList.get(1))).addLine(Component.text(doorList.get(2))).build());

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
