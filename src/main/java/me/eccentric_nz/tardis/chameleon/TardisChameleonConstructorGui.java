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
import me.eccentric_nz.tardis.custommodeldata.GuiChameleonConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
class TardisChameleonConstructorGui {

    private final TardisPlugin plugin;
    private final ItemStack[] construct;

    TardisChameleonConstructorGui(TardisPlugin plugin) {
        this.plugin = plugin;
        construct = getItemStack();
    }

    private ItemStack[] getItemStack() {

        ItemStack[] is = new ItemStack[54];

        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta bk = back.getItemMeta();
        assert bk != null;
        bk.setDisplayName(plugin.getChameleonGuis().getString("BACK_CHAM_OPTS"));
        bk.setCustomModelData(GuiChameleonConstructor.BACK_TO_CHAMELEON_CIRCUIT.getCustomModelData());
        back.setItemMeta(bk);
        is[0] = back;
        // help
        ItemStack help = new ItemStack(Material.BOWL, 1);
        ItemMeta hp = help.getItemMeta();
        assert hp != null;
        hp.setDisplayName(plugin.getChameleonGuis().getString("HELP"));
        hp.setCustomModelData(GuiChameleonConstructor.HELP.getCustomModelData());
        help.setItemMeta(hp);
        is[2] = help;
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta io = info.getItemMeta();
        assert io != null;
        io.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
        io.setLore(plugin.getChameleonGuis().getStringList("INFO_CONSTRUCT"));
        io.setCustomModelData(GuiChameleonConstructor.INFO.getCustomModelData());
        info.setItemMeta(io);
        is[3] = info;
        // abort
        ItemStack abort = new ItemStack(Material.BOWL, 1);
        ItemMeta at = abort.getItemMeta();
        assert at != null;
        at.setDisplayName(plugin.getChameleonGuis().getString("ABORT"));
        at.setCustomModelData(GuiChameleonConstructor.ABORT.getCustomModelData());
        abort.setItemMeta(at);
        is[5] = abort;
        // load button
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta ld = load.getItemMeta();
        assert ld != null;
        ld.setDisplayName(plugin.getChameleonGuis().getString("USE_PREV"));
        ld.setCustomModelData(GuiChameleonConstructor.USE_LAST_SAVED_CONSTRUCT.getCustomModelData());
        load.setItemMeta(ld);
        is[7] = load;
        // save button
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta se = save.getItemMeta();
        assert se != null;
        se.setDisplayName(plugin.getChameleonGuis().getString("SAVE"));
        se.setCustomModelData(GuiChameleonConstructor.SAVE_CONSTRUCT.getCustomModelData());
        save.setItemMeta(se);
        is[8] = save;
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
