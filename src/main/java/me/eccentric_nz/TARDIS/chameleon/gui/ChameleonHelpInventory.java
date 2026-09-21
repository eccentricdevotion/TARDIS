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
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonHelp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("BACK_CONSTRUCT")));
        // help
        ItemStack info = ItemStack.of(GUIChameleonHelp.INFO_HELP_1.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> ioLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_1")) {
            ioLore.add(Component.text(s));
        }
        info.setData(DataComponentTypes.LORE, ItemLore.lore(ioLore));
        // help
        ItemStack info2 = ItemStack.of(GUIChameleonHelp.INFO_HELP_2.material(), 1);
        info2.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> io2Lore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_2")) {
            io2Lore.add(Component.text(s));
        }
        info2.setData(DataComponentTypes.LORE, ItemLore.lore(io2Lore));
        // one
        ItemStack one = ItemStack.of(GUIChameleonHelp.COL_L_FRONT.material(), 1);
        one.setData(DataComponentTypes.CUSTOM_NAME, Component.text("1"));
        one.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_L_FRONT"))).build());
        // two
        ItemStack two = ItemStack.of(GUIChameleonHelp.COL_L_MIDDLE.material(), 1);
        two.setData(DataComponentTypes.CUSTOM_NAME, Component.text("2"));
        two.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_L_MIDDLE"))).build());
        // three
        ItemStack three = ItemStack.of(GUIChameleonHelp.COL_L_BACK.material(), 1);
        three.setData(DataComponentTypes.CUSTOM_NAME, Component.text("3"));
        three.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_L_BACK"))).build());
        // four
        ItemStack four = ItemStack.of(GUIChameleonHelp.COL_B_MIDDLE.material(), 1);
        four.setData(DataComponentTypes.CUSTOM_NAME, Component.text("4"));
        four.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_B_MIDDLE"))).build());
        // five
        ItemStack five = ItemStack.of(GUIChameleonHelp.COL_R_BACK.material(), 1);
        five.setData(DataComponentTypes.CUSTOM_NAME, Component.text("5"));
        five.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_R_BACK"))).build());
        // six
        ItemStack six = ItemStack.of(GUIChameleonHelp.COL_R_MIDDLE.material(), 1);
        six.setData(DataComponentTypes.CUSTOM_NAME, Component.text("6"));
        six.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_R_MIDDLE"))).build());
        // seven
        ItemStack seven = ItemStack.of(GUIChameleonHelp.COL_R_FRONT.material(), 1);
        seven.setData(DataComponentTypes.CUSTOM_NAME, Component.text("7"));
        seven.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_R_FRONT"))).build());
        // eight
        ItemStack eight = ItemStack.of(GUIChameleonHelp.COL_F_MIDDLE.material(), 1);
        eight.setData(DataComponentTypes.CUSTOM_NAME, Component.text("8"));
        eight.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_F_MIDDLE"))).build());
        // nine
        ItemStack nine = ItemStack.of(GUIChameleonHelp.COL_C_LAMP.material(), 1);
        nine.setData(DataComponentTypes.CUSTOM_NAME, Component.text("9"));
        nine.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(plugin.getChameleonGuis().getString("COL_C_LAMP"))).build());
        // grid
        ItemStack grid = ItemStack.of(GUIChameleonHelp.INFO_HELP_3.material(), 1);
        grid.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> gdLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_3")) {
            gdLore.add(Component.text(s));
        }
        grid.setData(DataComponentTypes.LORE, ItemLore.lore(gdLore));
        // grid
        ItemStack column = ItemStack.of(GUIChameleonHelp.INFO_HELP_4.material(), 1);
        column.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> cnLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_HELP_4")) {
            cnLore.add(Component.text(s));
        }
        column.setData(DataComponentTypes.LORE, ItemLore.lore(cnLore));
        // example
        ItemStack example = ItemStack.of(GUIChameleonHelp.VIEW_TEMP.material(), 1);
        example.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("VIEW_TEMP")));
        // one
        ItemStack o = ItemStack.of(GUIChameleonHelp.ROW_1.material(), 1);
        o.setData(DataComponentTypes.CUSTOM_NAME, Component.text("1"));
        // two
        ItemStack w = ItemStack.of(GUIChameleonHelp.ROW_2.material(), 1);
        w.setData(DataComponentTypes.CUSTOM_NAME, Component.text("2"));
        // three
        ItemStack t = ItemStack.of(GUIChameleonHelp.ROW_3.material(), 1);
        t.setData(DataComponentTypes.CUSTOM_NAME, Component.text("3"));
        // four
        ItemStack f = ItemStack.of(GUIChameleonHelp.ROW_4.material(), 1);
        f.setData(DataComponentTypes.CUSTOM_NAME, Component.text("4"));

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
