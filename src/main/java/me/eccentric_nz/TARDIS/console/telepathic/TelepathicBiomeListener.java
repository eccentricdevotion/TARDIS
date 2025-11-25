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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TelepathicBiomeListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> scroll = new HashMap<>();
    private final List<UUID> scrolling = new ArrayList<>();
    private final ItemStack[][] biomes;
    private final int rows;

    public TelepathicBiomeListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        rows = EnvironmentBiomes.OVERWORLD.size() / 8 + 1;
        biomes = getBiomes();
    }

    @EventHandler
    public void onWallMenuOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder(false) instanceof TARDISTelepathicBiome) {
            Player p = (Player) event.getPlayer();
            scroll.put(p.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBiomeMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISTelepathicBiome)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TelepathicBiomeListener");
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        InventoryView view = event.getView();
        switch (slot) {
            // scroll up
            case 8 -> {
                if (view.getItem(8) != null) {
                    if (!scrolling.contains(uuid)) {
                        scrolling.add(uuid);
                        scroll(view, scroll.get(uuid) + 1, true, uuid);
                    }
                }
            }
            // scroll down
            case 17 -> {
                if (view.getItem(17) != null) {
                    if (!scrolling.contains(uuid)) {
                        scrolling.add(uuid);
                        scroll(view, scroll.get(uuid) - 1, false, uuid);
                    }
                }
            }
            // close
            case 53 -> close(player);
            // run a command
            default -> {
                ItemStack choice = view.getItem(slot);
                if (choice != null) {
                    // get the biome
                    ItemMeta im = choice.getItemMeta();
                    String enumStr = ComponentUtils.toEnumUppercase(im.displayName());
                    player.performCommand("tardistravel biome " + enumStr);
                    close(player);
                }
            }
        }
    }

    private void scroll(InventoryView view, int row, boolean up, UUID uuid) {
        if ((up && row < (rows - 5)) || (!up && row >= 0)) {
            scroll.put(uuid, row);
            setSlots(view, row, uuid);
        } else {
            scrolling.remove(uuid);
        }
    }

    private void setSlots(InventoryView view, int row, UUID uuid) {
        int slot = 0;
        for (int r = row; r < row + 6; r++) {
            for (int c = 0; c < 8; c++) {
                view.setItem(slot, biomes[r][c]);
                if (slot % 9 == 7) {
                    slot += 2;
                } else {
                    slot++;
                }
            }
        }
        scrolling.remove(uuid);
    }

    private ItemStack[][] getBiomes() {
        ItemStack[][] stacks = new ItemStack[rows][8];
        int r = 0;
        int c = 0;
        for (Biome biome : EnvironmentBiomes.OVERWORLD) {
            Material material = EnvironmentBiomes.BIOME_BLOCKS.get(biome.getKey().getKey());
            if (material != null) {
                ItemStack is = ItemStack.of(material, 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(TARDISStringUtils.capitalise(biome.getKey().getKey())));
                is.setItemMeta(im);
                stacks[r][c] = is;
                c++;
                if (c == 8) {
                    r++;
                    c = 0;
                }
            }
        }
        return stacks;
    }
}
