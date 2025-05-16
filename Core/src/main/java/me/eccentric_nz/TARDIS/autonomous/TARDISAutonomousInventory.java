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
package me.eccentric_nz.TARDIS.autonomous;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIAutonomous;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAutonomousSave;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISAutonomousInventory {

    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] gui;
    private final ItemStack off;

    public TARDISAutonomousInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        off = new ItemStack(Material.LIGHT_GRAY_CARPET, 1);
        ItemMeta offMeta = off.getItemMeta();
        offMeta.setDisplayName(ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
        off.setItemMeta(offMeta);
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Autonomous GUI
     *
     * @return an array of ItemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[36];
        // set unselected
        stack[12] = off;
        stack[13] = off;
        stack[14] = off;
        stack[15] = off;
        stack[16] = off;
        stack[30] = off;
        stack[31] = off;
        // set GUI buttons
        for (GUIAutonomous a : GUIAutonomous.values()) {
            ItemStack is = new ItemStack(a.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(a.getName().contains("Selected") ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : a.getName());
            if (a == GUIAutonomous.AUTONOMOUS_TYPE) {
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                component.setFloats(SwitchVariant.AUTO_TYPE.getFloats());
                im.setCustomModelDataComponent(component);
            }
            if (a == GUIAutonomous.FALLBACK) {
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                component.setFloats(SwitchVariant.AUTO_DEFAULT.getFloats());
                im.setCustomModelDataComponent(component);
            }
            if (a == GUIAutonomous.SAVE_SELECTOR) {
                List<String> lore = new ArrayList<>(a.getLore());
                // get tardis id
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (rst.fromUUID(uuid.toString())) {
                    // get autonomous save
                    ResultSetAutonomousSave rsd = new ResultSetAutonomousSave(plugin);
                    if (rsd.fromID(rst.getTardisId())) {
                        lore.add(ChatColor.GREEN + rsd.getAutonomous());
                    }
                }
                im.setLore(lore);
            } else if (a.getLore() != null) {
                im.setLore(a.getLore());
            }
            is.setItemMeta(im);
            int slot = (a.getSlot() == -1) ? findSlot(a) : a.getSlot();
            stack[slot] = is;
        }
        return stack;
    }

    private int findSlot(GUIAutonomous a) {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        if (rsp.resultSet()) {
            return (a.equals(GUIAutonomous.SELECTED_TYPE)) ? rsp.getAutoType().getSlot() : rsp.getAutoDefault().getSlot();
        }
        // couldn't get preference so return close slot which will be overwritten
        return 35;
    }

    public ItemStack[] getGui() {
        return gui;
    }
}
