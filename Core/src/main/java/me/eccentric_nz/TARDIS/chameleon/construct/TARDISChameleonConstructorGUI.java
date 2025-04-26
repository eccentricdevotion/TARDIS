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
package me.eccentric_nz.TARDIS.chameleon.construct;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISChameleonConstructorGUI {

    private final TARDIS plugin;
    private final ItemStack[] construct;

    public TARDISChameleonConstructorGUI(TARDIS plugin) {
        this.plugin = plugin;
        construct = getItemStack();
    }

    private ItemStack[] getItemStack() {

        ItemStack[] is = new ItemStack[54];

        // back
        ItemStack back = new ItemStack(GUIChameleonConstructor.BACK_TO_CHAMELEON_CIRCUIT.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName(plugin.getChameleonGuis().getString("BACK_CHAM_OPTS"));
        back.setItemMeta(bk);
        is[GUIChameleonConstructor.BACK_TO_CHAMELEON_CIRCUIT.slot()] = back;
        // help
        ItemStack help = new ItemStack(GUIChameleonConstructor.HELP.material(), 1);
        ItemMeta hp = help.getItemMeta();
        hp.setDisplayName(plugin.getChameleonGuis().getString("HELP"));
        help.setItemMeta(hp);
        is[GUIChameleonConstructor.HELP.slot()] = help;
        // info
        ItemStack info = new ItemStack(GUIChameleonConstructor.INFO.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        io.setLore(plugin.getChameleonGuis().getStringList("INFO_CONSTRUCT"));
        info.setItemMeta(io);
        is[GUIChameleonConstructor.INFO.slot()] = info;
        // abort
        ItemStack abort = new ItemStack(GUIChameleonConstructor.ABORT.material(), 1);
        ItemMeta at = abort.getItemMeta();
        at.setDisplayName(plugin.getChameleonGuis().getString("ABORT"));
        abort.setItemMeta(at);
        is[GUIChameleonConstructor.ABORT.slot()] = abort;
        // load button
        ItemStack load = new ItemStack(GUIChameleonConstructor.USE_LAST_SAVED_CONSTRUCT.material(), 1);
        ItemMeta ld = load.getItemMeta();
        ld.setDisplayName(plugin.getChameleonGuis().getString("USE_PREV"));
        load.setItemMeta(ld);
        is[GUIChameleonConstructor.USE_LAST_SAVED_CONSTRUCT.slot()] = load;
        // save button
        ItemStack save = new ItemStack(GUIChameleonConstructor.SAVE_CONSTRUCT.material(), 1);
        ItemMeta se = save.getItemMeta();
        se.setDisplayName(plugin.getChameleonGuis().getString("SAVE"));
        save.setItemMeta(se);
        is[GUIChameleonConstructor.SAVE_CONSTRUCT.slot()] = save;
        // lamp button
        ItemStack lamp = new ItemStack(Material.TORCH, 1);
        is[26] = lamp;
        // save button
        ItemStack door = new ItemStack(Material.IRON_DOOR, 1);
        is[43] = door;
        is[52] = door;

        return is;
    }

    public ItemStack[] getConstruct() {
        return construct;
    }
}
