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
package me.eccentric_nz.TARDIS.lights;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.utility.LightLevel;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUILights;
import me.eccentric_nz.TARDIS.custommodels.GUIParticle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAllLightLevels;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LightLevelsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;
    private String interior_level = "15";
    private String exterior_level = "15";
    private String console_level = "15";

    public LightLevelsInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        ResultSetAllLightLevels rs = new ResultSetAllLightLevels(plugin, id);
        if (rs.resultSet()) {
            interior_level = "" + LightLevel.interior_level[rs.getInteriorLevel()];
            exterior_level = "" + LightLevel.exterior_level[rs.getExteriorLevel()];
            console_level = "" + LightLevel.interior_level[rs.getConsoleLevel()];
        }
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Light Levels", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the TARDIS Lights GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // interior info
        ItemStack i_info = ItemStack.of(GUILights.INTERIOR_INFO.material(), 1);
        i_info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Interior Lights"));
        i_info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Set the light level"),
                Component.text("of the interior lights")
        )));
        // interior
        ItemStack interior = ItemStack.of(GUILights.INTERIOR.material(), 1);
        interior.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Interior Lights"));
        interior.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(interior_level)).build());
        // exterior info
        ItemStack e_info = ItemStack.of(GUILights.EXTERIOR_INFO.material(), 1);
        e_info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Exterior Lamp"));
        e_info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Set the light level"),
                Component.text("of the exterior lamp")
        )));
        // exterior
        ItemStack exterior = ItemStack.of(GUILights.EXTERIOR.material(), 1);
        exterior.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Exterior Lamp"));
        exterior.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(exterior_level)).build());
        // console info
        ItemStack c_info = ItemStack.of(GUILights.CONSOLE_INFO.material(), 1);
        c_info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Console Lamp"));
        c_info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Set the light level"),
                Component.text("of the console lamp")
        )));
        // console
        ItemStack console = ItemStack.of(GUILights.CONSOLE.material(), 1);
        console.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Console Lamp"));
        console.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(console_level)).build());
        // minus
        ItemStack minus = ItemStack.of(GUIParticle.MINUS.material(), 1);
        minus.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LESS")));
        // plus
        ItemStack plus = ItemStack.of(GUIParticle.PLUS.material(), 1);
        plus.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MORE")));
        // back button
        ItemStack back = ItemStack.of(GUIChameleonTemplate.BACK_HELP.material(), 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back"));
        // close
        ItemStack close = GUIItemFactory.close();
        return new ItemStack[]{
                null, i_info, null, null, null, null, null, e_info, null,
                minus, interior, plus, null, null, null, minus, exterior, plus,
                null, null, null, null, c_info, null, null, null, null,
                null, null, null, minus, console, plus, null, null, null,
                null, null, null, null, null, null, null, null, null,
                back, null, null, null, null, null, null, null, close
        };
    }
}
