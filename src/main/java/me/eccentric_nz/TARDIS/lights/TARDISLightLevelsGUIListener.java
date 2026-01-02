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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.utility.LightLevel;
import me.eccentric_nz.TARDIS.control.actions.ConsoleLampAction;
import me.eccentric_nz.TARDIS.control.actions.LightLevelAction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class TARDISLightLevelsGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISLightLevelsGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onLightLevelsMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISLightLevelsInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (slot >= 0 && slot < 54) {
            // get selection
            InventoryView view = event.getView();
            ItemStack is = view.getItem(slot);
            if (is != null) {
                // get TARDIS id
                int id = -1;
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (rst.fromUUID(uuid.toString())) {
                    id = rst.getTardisId();
                }
                switch (slot) {
                    case 9 -> {
                        // interior minus
                        StateResult setLevel = getNewState(view, 10, false);
                        if (setLevel.success) {
                            // update indicator
                            setState(view, 10, setLevel.strength);
                            // set light level
                            setLightLevel(setLevel.level, 50, id);
                            // TODO update control?
                        }
                    }
                    case 11 -> {
                        // interior plus
                        StateResult setLevel = getNewState(view, 10, true);
                        if (setLevel.success) {
                            // update indicator
                            setState(view, 10, setLevel.strength);
                            // set light level
                            setLightLevel(setLevel.level, 50, id);
                            // TODO update control?
                        }
                    }
                    case 15 -> {
                        // exterior minus
                        StateResult setLevel = getNewState(view, 16, false);
                        if (setLevel.success) {
                            // update indicator
                            setState(view, 16, setLevel.strength);
                            // set light level
                            setLightLevel(setLevel.level, 49, id);
                            // TODO update control?
                        }
                    }
                    case 17 -> {
                        // exterior plus
                        StateResult setLevel = getNewState(view, 16, true);
                        if (setLevel.success) {
                            // update indicator
                            setState(view, 16, setLevel.strength);
                            // set light level
                            setLightLevel(setLevel.level, 49, id);
                            // TODO update control?
                        }
                    }
                    case 30 -> {
                        // console minus
                        StateResult setLevel = getNewState(view, 31, false);
                        if (setLevel.success) {
                            // update indicator
                            setState(view, 31, setLevel.strength);
                            // set light level
                            setLightLevel(setLevel.level, 56, id);
                            // TODO update control?
                        }
                    }
                    case 32 -> {
                        // console plus
                        StateResult setLevel = getNewState(view, 31, true);
                        if (setLevel.success) {
                            // update indicator
                            setState(view, 31, setLevel.strength);
                            // set light level
                            setLightLevel(setLevel.level, 56, id);
                            // TODO update control?
                        }
                    }
                    case 45 -> player.openInventory(new TARDISLightsInventory(plugin, id, uuid).getInventory()); // back
                    case 53 -> close(player); // close
                }
            }
        }
    }

    private StateResult getNewState(InventoryView view, int slot, boolean next) {
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        String lore = ComponentUtils.stripColour(im.lore().getFirst());
        int currentStrength = TARDISNumberParsers.parseInt(lore);
        int index;
        if (slot == 16) {
            index = ArrayUtils.indexOf(LightLevel.exterior_level, currentStrength);
            if (next && index - 1 >= 0) {
                return new StateResult(true, LightLevel.exterior_level[index - 1], index - 1);
            } else if (!next && index + 1 < LightLevel.exterior_level.length) {
                return new StateResult(true, LightLevel.exterior_level[index + 1], index + 1);
            }
        } else {
            index = ArrayUtils.indexOf(LightLevel.interior_level, currentStrength);
            if (next && index - 1 >= 0) {
                return new StateResult(true, LightLevel.interior_level[index - 1], index - 1);
            } else if (!next && index + 1 < LightLevel.interior_level.length) {
                return new StateResult(true, LightLevel.interior_level[index + 1], index + 1);
            }
        }
        return new StateResult(false, currentStrength, 0);
    }

    private void setState(InventoryView view, int slot, int strength) {
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        List<Component> lore = im.lore();
        lore.set(0, Component.text(strength));
        im.lore(lore);
        is.setItemMeta(im);
    }

    private void setLightLevel(int setLevel, int which, int id) {
        // get control
        ResultSetLightLevel rs = new ResultSetLightLevel(plugin);
        if (rs.fromTypeAndID(which, id)) {
            if (which > 50) {
                new ConsoleLampAction(plugin).illuminate(id, setLevel, rs.getControlId());
            } else {
                new LightLevelAction(plugin).illuminate(setLevel - 1, rs.getControlId(), rs.isPowered(), which, rs.isPoliceBox(), id, rs.isLightsOn());
            }
        }
    }

    private static class StateResult {
        Boolean success;
        Integer strength;
        Integer level;

        public StateResult(Boolean success, Integer strength, Integer level) {
            this.success = success;
            this.strength = strength;
            this.level = level;
        }
    }
}
