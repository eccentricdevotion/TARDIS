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
package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIFarming;
import me.eccentric_nz.TARDIS.database.data.FarmPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarmingPrefs;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISFarmingInventory {

    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] gui;
    private final ItemStack on;
    private final ItemStack off;
    public TARDISFarmingInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        off = new ItemStack(GUIFarming.OFF.getMaterial(), 1);
        ItemMeta offMeta = off.getItemMeta();
        offMeta.setDisplayName(ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
        off.setItemMeta(offMeta);
        on = new ItemStack(GUIFarming.ON.getMaterial(), 1);
        ItemMeta onMeta = on.getItemMeta();
        onMeta.setDisplayName(ChatColor.RED + plugin.getLanguage().getString("SET_ON"));
        on.setItemMeta(offMeta);
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Farming GUI
     *
     * @return an array of ItemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[36];
        // allay, apiary, aquarium, bamboo, birdcage, farm, geode, hutch, igloo, iistubil, lava, mangrove, pen, stable, stall, village
        ResultSetFarmingPrefs rs = new ResultSetFarmingPrefs(plugin, uuid.toString());
        // set farming status
        if (rs.resultSet()) {
            FarmPrefs farmPrefs = rs.getData();
            stack[9] = farmPrefs.shouldFarmAllay() ? on : off;
            stack[10] = farmPrefs.shouldFarmBees() ? on : off;
            stack[11] = farmPrefs.shouldFarmFish() ? on : off;
            stack[12] = farmPrefs.shouldFarmPandas() ? on : off;
            stack[13] = farmPrefs.shouldFarmParrots() ? on : off;
            stack[14] = farmPrefs.shouldFarmLivestock() ? on : off;
            stack[15] = farmPrefs.shouldFarmAxolotls() ? on : off;
            stack[16] = farmPrefs.shouldFarmRabbits() ? on : off;
            stack[17] = farmPrefs.shouldFarmPolarBears() ? on : off;
            stack[27] = farmPrefs.shouldFarmCamels() ? on : off;
            stack[28] = farmPrefs.shouldFarmStriders() ? on : off;
            stack[29] = farmPrefs.shouldFarmFrogs() ? on : off;
            stack[30] = farmPrefs.shouldFarmSniffers() ? on : off;
            stack[31] = farmPrefs.shouldFarmHorses() ? on : off;
            stack[32] = farmPrefs.shouldFarmLlamas() ? on : off;
            stack[33] = farmPrefs.shouldFarmVillagers() ? on : off;
        } else {
            // insert a new record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("farming_prefs", set);
            // set all to on (the default)
            stack[9] = on;
            stack[10] = on;
            stack[11] = on;
            stack[12] = on;
            stack[13] = on;
            stack[14] = on;
            stack[15] = on;
            stack[16] = on;
            stack[17] = on;
            stack[27] = on;
            stack[28] = on;
            stack[29] = on;
            stack[30] = on;
            stack[31] = on;
            stack[32] = on;
            stack[33] = on;
        }
        // set GUI buttons
        for (GUIFarming f : GUIFarming.values()) {
            if (f != GUIFarming.ON && f != GUIFarming.OFF) {
                ItemStack is = new ItemStack(f.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(f.getMob());
                if (f.getModel() != null) {
                    im.setItemModel(f.getModel());
                }
                if (f != GUIFarming.CLOSE) {
                    im.setLore(List.of(f.getRoomName()));
                }
                is.setItemMeta(im);
                stack[f.getSlot()] = is;
            }
        }
        return stack;
    }

    public ItemStack[] getGui() {
        return gui;
    }
}
