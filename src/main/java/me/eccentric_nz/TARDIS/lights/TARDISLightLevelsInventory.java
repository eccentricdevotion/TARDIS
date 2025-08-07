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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.utility.LightLevel;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodels.GUILights;
import me.eccentric_nz.TARDIS.custommodels.GUIParticle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAllLightLevels;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISLightLevelsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private String interior_level = "15";
    private String exterior_level = "15";
    private String console_level = "15";
    private final Inventory inventory;

    public TARDISLightLevelsInventory(TARDIS plugin, int id) {
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
        ItemMeta iiim = i_info.getItemMeta();
        iiim.displayName(Component.text("Interior Lights"));
        iiim.lore(List.of(
                Component.text("Set the light level"),
                Component.text("of the interior lights")
        ));
        i_info.setItemMeta(iiim);
        // interior
        ItemStack interior = ItemStack.of(GUILights.INTERIOR.material(), 1);
        ItemMeta inim = interior.getItemMeta();
        inim.displayName(Component.text("Interior Lights"));
        inim.lore(List.of(Component.text(interior_level)));
        interior.setItemMeta(inim);
        // exterior info
        ItemStack e_info = ItemStack.of(GUILights.EXTERIOR_INFO.material(), 1);
        ItemMeta eiim = e_info.getItemMeta();
        eiim.displayName(Component.text("Exterior Lamp"));
        eiim.lore(List.of(
                Component.text("Set the light level"),
                Component.text("of the exterior lamp")
        ));
        e_info.setItemMeta(eiim);
        // exterior
        ItemStack exterior = ItemStack.of(GUILights.EXTERIOR.material(), 1);
        ItemMeta exim = exterior.getItemMeta();
        exim.displayName(Component.text("Exterior Lamp"));
        exim.lore(List.of(Component.text(exterior_level)));
        exterior.setItemMeta(exim);
        // console info
        ItemStack c_info = ItemStack.of(GUILights.CONSOLE_INFO.material(), 1);
        ItemMeta ciim = c_info.getItemMeta();
        ciim.displayName(Component.text("Console Lamp"));
        ciim.lore(List.of(
                Component.text("Set the light level"),
                Component.text("of the console lamp")
        ));
        c_info.setItemMeta(ciim);
        // console
        ItemStack console = ItemStack.of(GUILights.CONSOLE.material(), 1);
        ItemMeta lamp = console.getItemMeta();
        lamp.displayName(Component.text("Console Lamp"));
        lamp.lore(List.of(Component.text(console_level)));
        console.setItemMeta(lamp);
        // minus
        ItemStack minus = ItemStack.of(GUIParticle.MINUS.material(), 1);
        ItemMeta mim = minus.getItemMeta();
        mim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LESS")));
        minus.setItemMeta(mim);
        // plus
        ItemStack plus = ItemStack.of(GUIParticle.PLUS.material(), 1);
        ItemMeta pim = plus.getItemMeta();
        pim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MORE")));
        plus.setItemMeta(pim);
        // back button
        ItemStack back = ItemStack.of(GUIChameleonTemplate.BACK_HELP.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.displayName(Component.text(plugin.getChameleonGuis().getString("BACK_HELP")));
        back.setItemMeta(bk);
        // close
        ItemStack close = ItemStack.of(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(clim);
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
