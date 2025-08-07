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
package me.eccentric_nz.TARDIS.console.telepathic;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.travel.TARDISStructureTravel;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

public class TARDISTelepathicStructure implements InventoryHolder {

    private final TARDIS plugin;
    private final Registry<Structure> structures = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE);
    private final Inventory inventory;

    public TARDISTelepathicStructure(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Telepathic Structure Finder", NamedTextColor.DARK_RED));
        this.inventory.setContents(getButtons());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getButtons() {
        // structure finder
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        for (Structure structure : TARDISStructureTravel.overworldStructures) {
            ItemStack is = make(structure, Material.GRASS_BLOCK);
            stack[i] = is;
            i++;
        }
        for (Structure structure : TARDISStructureTravel.netherStructures) {
            ItemStack is = make(structure, Material.CRIMSON_NYLIUM);
            stack[i] = is;
            i++;
        }
        ItemStack end = make(Structure.END_CITY, Material.PURPUR_BLOCK);
        stack[i] = end;
        // close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        gui.setItemModel(GUIMap.BUTTON_CLOSE.key());
        close.setItemMeta(gui);
        stack[53] = close;
        return stack;
    }

    private ItemStack make(Structure structure, Material material) {
        ItemStack is = ItemStack.of(material, 1);
        ItemMeta im = is.getItemMeta();
        if (material == Material.GRASS_BLOCK) {
            CustomModelDataComponent component = im.getCustomModelDataComponent();
            component.setColors(List.of(Color.GREEN));
            im.setCustomModelDataComponent(component);
        }
        im.displayName(Component.text(TARDISStringUtils.capitalise(structures.getKey(structure).getKey())));
        is.setItemMeta(im);
        return is;
    }
}
