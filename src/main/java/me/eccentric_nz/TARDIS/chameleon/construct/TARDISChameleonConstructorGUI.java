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
package me.eccentric_nz.TARDIS.chameleon.construct;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
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
public class TARDISChameleonConstructorGUI implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISChameleonConstructorGUI(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Chameleon Construction", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {

        ItemStack[] is = new ItemStack[54];

        // back
        ItemStack back = ItemStack.of(GUIChameleonConstructor.BACK_TO_CHAMELEON_CIRCUIT.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.displayName(Component.text(plugin.getChameleonGuis().getString("BACK_CHAM_OPTS", "Back to Chameleon Circuit")));
        back.setItemMeta(bk);
        is[GUIChameleonConstructor.BACK_TO_CHAMELEON_CIRCUIT.slot()] = back;
        // help
        ItemStack help = ItemStack.of(GUIChameleonConstructor.HELP.material(), 1);
        ItemMeta hp = help.getItemMeta();
        hp.displayName(Component.text(plugin.getChameleonGuis().getString("HELP", "Help")));
        help.setItemMeta(hp);
        is[GUIChameleonConstructor.HELP.slot()] = help;
        // info
        ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        List<Component> ioLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("INFO_CONSTRUCT")) {
            ioLore.add(Component.text(s));
        }
        io.lore(ioLore);
        info.setItemMeta(io);
        is[GUIChameleonConstructor.INFO.slot()] = info;
        // abort
        ItemStack abort = ItemStack.of(GUIChameleonConstructor.ABORT.material(), 1);
        ItemMeta at = abort.getItemMeta();
        at.displayName(Component.text(plugin.getChameleonGuis().getString("ABORT", "Abort")));
        abort.setItemMeta(at);
        is[GUIChameleonConstructor.ABORT.slot()] = abort;
        // load button
        ItemStack load = ItemStack.of(GUIChameleonConstructor.USE_LAST_SAVED_CONSTRUCT.material(), 1);
        ItemMeta ld = load.getItemMeta();
        ld.displayName(Component.text(plugin.getChameleonGuis().getString("USE_PREV", "Use last saved construct")));
        load.setItemMeta(ld);
        is[GUIChameleonConstructor.USE_LAST_SAVED_CONSTRUCT.slot()] = load;
        // save button
        ItemStack save = ItemStack.of(GUIChameleonConstructor.SAVE_CONSTRUCT.material(), 1);
        ItemMeta se = save.getItemMeta();
        se.displayName(Component.text(plugin.getChameleonGuis().getString("SAVE", "Save construct")));
        save.setItemMeta(se);
        is[GUIChameleonConstructor.SAVE_CONSTRUCT.slot()] = save;
        // lamp button
        ItemStack lamp = ItemStack.of(Material.TORCH, 1);
        is[26] = lamp;
        // save button
        ItemStack door = ItemStack.of(Material.IRON_DOOR, 1);
        is[43] = door;
        is[52] = door;

        return is;
    }
}
