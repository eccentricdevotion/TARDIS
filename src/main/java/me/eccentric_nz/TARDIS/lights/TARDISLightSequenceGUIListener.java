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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISLightSequenceGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<Material> colours = List.of(Material.BLUE_WOOL, Material.GREEN_WOOL, Material.ORANGE_WOOL, Material.PINK_WOOL, Material.PURPLE_WOOL, Material.YELLOW_WOOL, Material.RED_WOOL, Material.WHITE_WOOL, Material.BLACK_WOOL);
    private final List<ClickType> clicks = List.of(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT, ClickType.DOUBLE_CLICK);

    public TARDISLightSequenceGUIListener(TARDIS plugin) {
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
    public void onLightMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!(event.getInventory().getHolder(false) instanceof TARDISLightSequenceInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (slot >= 0 && slot < 45) {
            // get selection
            ItemStack is = view.getItem(slot);
            if (is != null) {
                switch (slot) {
                    // cycle through colours
                    case 9, 10, 11, 12, 13, 14, 15, 16, 17 -> setColour(view, slot);
                    // cycle through delays
                    case 18, 19, 20, 21, 22, 23, 24, 25, 26 -> setDelay(view, slot, clicks.contains(event.getClick()));
                    // cycle through levels
                    case 27, 28, 29, 30, 31, 32, 33, 34, 35 -> setLevel(view, slot, clicks.contains(event.getClick()));
                    // cycle through presets
                    case 36 -> cyclePresets(view);
                    // save
                    case 40 -> save(view, uuid.toString());
                    // back
                    case 42 -> {
                        ResultSetTardisID rst = new ResultSetTardisID(plugin);
                        if (rst.fromUUID(uuid.toString())) {
                            player.openInventory(new TARDISLightsInventory(plugin, rst.getTardisId(), uuid).getInventory());
                        }
                    }
                    // close
                    case 44 -> close(player);
                    // do nothing
                    default -> {
                    }
                }
            }
        }
    }

    private void setColour(InventoryView view, int slot) {
        ItemStack wool = view.getItem(slot);
        int index = colours.indexOf(wool.getType()) + 1;
        if (index == colours.size()) {
            index = 0;
        }
        Material next = colours.get(index);
        view.setItem(slot, ItemStack.of(next));
    }

    private void setDelay(InventoryView view, int slot, boolean shift) {
        ItemStack delay = view.getItem(slot);
        int amount = delay.getAmount() + 1;
        if (amount > 40) {
            amount = 5;
        }
        if (shift) {
            for (int i = 18; i < 27; i++) {
                ItemStack d = view.getItem(i);
                d.setAmount(amount);
            }
        } else {
            delay.setAmount(amount);
        }
    }

    private void setLevel(InventoryView view, int slot, boolean shift) {
        ItemStack level = view.getItem(slot);
        int amount = level.getAmount();
        int index = ArrayUtils.indexOf(LightLevel.interior_level, amount) + 1;
        if (index == 8) {
            index = 0;
        }
        if (shift) {
            for (int i = 27; i < 36; i++) {
                ItemStack l = view.getItem(i);
                l.setAmount(LightLevel.interior_level[index]);
            }
        } else {
            level.setAmount(LightLevel.interior_level[index]);
        }
    }

    private void cyclePresets(InventoryView view) {
        // which sequence?
        ItemStack preset = view.getItem(36);
        ItemMeta im = preset.getItemMeta();
        List<Component> lore = im.lore();
        String num = ComponentUtils.stripColour(lore.get(2));
        int next = TARDISNumberParsers.parseInt(num) + 1;
        if (next == Sequences.PRESETS.size()) {
            next = 0;
        }
        // set sequence
        List<Material> sequence = Sequences.PRESETS.get(next);
        for (int i = 9; i < 18; i++) {
            view.setItem(i, ItemStack.of(sequence.get(i - 9)));
        }
        // set delays
        List<Integer> delays = Sequences.DELAYS.get(next);
        for (int i = 18; i < 27; i++) {
            view.getItem(i).setAmount(delays.get(i - 18));
        }
        // set light levels
        List<Integer> levels = Sequences.LEVELS.get(next);
        for (int i = 27; i < 36; i++) {
            view.getItem(i).setAmount(levels.get(i - 27));
        }
        lore.set(2, Component.text(next));
        im.lore(lore);
        preset.setItemMeta(im);
    }

    private void save(InventoryView view, String uuid) {
        // get player's TARDIS
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(uuid)) {
            int id = rst.getTardisId();
            // get pattern
            StringBuilder patternBuilder = new StringBuilder();
            for (int i = 9; i < 18; i++) {
                ItemStack wool = view.getItem(i);
                String material = wool.getType().toString().replace("_WOOL", "");
                patternBuilder.append(material);
                if (i != 17) {
                    patternBuilder.append(":");
                }
            }
            StringBuilder delayBuilder = new StringBuilder();
            for (int i = 18; i < 27; i++) {
                ItemStack delay = view.getItem(i);
                int amount = delay.getAmount();
                delayBuilder.append(amount);
                if (i != 26) {
                    delayBuilder.append(":");
                }
            }
            StringBuilder levelBuilder = new StringBuilder();
            for (int i = 27; i < 36; i++) {
                ItemStack level = view.getItem(i);
                int amount = level.getAmount();
                levelBuilder.append(amount);
                if (i != 35) {
                    levelBuilder.append(":");
                }
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("pattern", patternBuilder.toString());
            set.put("delays", delayBuilder.toString());
            set.put("levels", levelBuilder.toString());
            ResultSetLightPrefs rslp = new ResultSetLightPrefs(plugin);
            if (rslp.fromID(id)) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("light_prefs", set, where);
            } else {
                set.put("tardis_id", id);
                plugin.getQueryFactory().doInsert("light_prefs", set);
            }
        }
    }
}
