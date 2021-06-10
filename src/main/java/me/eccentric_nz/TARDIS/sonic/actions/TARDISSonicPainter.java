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
package me.eccentric_nz.tardis.sonic.actions;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISMaterials;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TARDISSonicPainter {

    private static final List<Material> PAINTABLE = new ArrayList<>();

    static {
        PAINTABLE.add(Material.BLACK_CARPET);
        PAINTABLE.add(Material.BLACK_CONCRETE);
        PAINTABLE.add(Material.BLACK_CONCRETE_POWDER);
        PAINTABLE.add(Material.BLACK_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.BLACK_STAINED_GLASS);
        PAINTABLE.add(Material.BLACK_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.BLACK_TERRACOTTA);
        PAINTABLE.add(Material.BLACK_WOOL);
        PAINTABLE.add(Material.BLUE_CARPET);
        PAINTABLE.add(Material.BLUE_CONCRETE);
        PAINTABLE.add(Material.BLUE_CONCRETE_POWDER);
        PAINTABLE.add(Material.BLUE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.BLUE_STAINED_GLASS);
        PAINTABLE.add(Material.BLUE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.BLUE_TERRACOTTA);
        PAINTABLE.add(Material.BLUE_WOOL);
        PAINTABLE.add(Material.BROWN_CARPET);
        PAINTABLE.add(Material.BROWN_CONCRETE);
        PAINTABLE.add(Material.BROWN_CONCRETE_POWDER);
        PAINTABLE.add(Material.BROWN_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.BROWN_STAINED_GLASS);
        PAINTABLE.add(Material.BROWN_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.BROWN_TERRACOTTA);
        PAINTABLE.add(Material.BROWN_WOOL);
        PAINTABLE.add(Material.CYAN_CARPET);
        PAINTABLE.add(Material.CYAN_CONCRETE);
        PAINTABLE.add(Material.CYAN_CONCRETE_POWDER);
        PAINTABLE.add(Material.CYAN_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.CYAN_STAINED_GLASS);
        PAINTABLE.add(Material.CYAN_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.CYAN_TERRACOTTA);
        PAINTABLE.add(Material.CYAN_WOOL);
        PAINTABLE.add(Material.GRAY_CARPET);
        PAINTABLE.add(Material.GRAY_CONCRETE);
        PAINTABLE.add(Material.GRAY_CONCRETE_POWDER);
        PAINTABLE.add(Material.GRAY_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.GRAY_STAINED_GLASS);
        PAINTABLE.add(Material.GRAY_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.GRAY_TERRACOTTA);
        PAINTABLE.add(Material.GRAY_WOOL);
        PAINTABLE.add(Material.GREEN_CARPET);
        PAINTABLE.add(Material.GREEN_CONCRETE);
        PAINTABLE.add(Material.GREEN_CONCRETE_POWDER);
        PAINTABLE.add(Material.GREEN_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.GREEN_STAINED_GLASS);
        PAINTABLE.add(Material.GREEN_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.GREEN_TERRACOTTA);
        PAINTABLE.add(Material.GREEN_WOOL);
        PAINTABLE.add(Material.LIGHT_BLUE_CARPET);
        PAINTABLE.add(Material.LIGHT_BLUE_CONCRETE);
        PAINTABLE.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
        PAINTABLE.add(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_BLUE_STAINED_GLASS);
        PAINTABLE.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.LIGHT_BLUE_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_BLUE_WOOL);
        PAINTABLE.add(Material.LIGHT_GRAY_CARPET);
        PAINTABLE.add(Material.LIGHT_GRAY_CONCRETE);
        PAINTABLE.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
        PAINTABLE.add(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_GRAY_STAINED_GLASS);
        PAINTABLE.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.LIGHT_GRAY_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_GRAY_WOOL);
        PAINTABLE.add(Material.LIME_CARPET);
        PAINTABLE.add(Material.LIME_CONCRETE);
        PAINTABLE.add(Material.LIME_CONCRETE_POWDER);
        PAINTABLE.add(Material.LIME_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.LIME_STAINED_GLASS);
        PAINTABLE.add(Material.LIME_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.LIME_TERRACOTTA);
        PAINTABLE.add(Material.LIME_WOOL);
        PAINTABLE.add(Material.MAGENTA_CARPET);
        PAINTABLE.add(Material.MAGENTA_CONCRETE);
        PAINTABLE.add(Material.MAGENTA_CONCRETE_POWDER);
        PAINTABLE.add(Material.MAGENTA_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.MAGENTA_STAINED_GLASS);
        PAINTABLE.add(Material.MAGENTA_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.MAGENTA_TERRACOTTA);
        PAINTABLE.add(Material.MAGENTA_WOOL);
        PAINTABLE.add(Material.ORANGE_CARPET);
        PAINTABLE.add(Material.ORANGE_CONCRETE);
        PAINTABLE.add(Material.ORANGE_CONCRETE_POWDER);
        PAINTABLE.add(Material.ORANGE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.ORANGE_STAINED_GLASS);
        PAINTABLE.add(Material.ORANGE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.ORANGE_TERRACOTTA);
        PAINTABLE.add(Material.ORANGE_WOOL);
        PAINTABLE.add(Material.PINK_CARPET);
        PAINTABLE.add(Material.PINK_CONCRETE);
        PAINTABLE.add(Material.PINK_CONCRETE_POWDER);
        PAINTABLE.add(Material.PINK_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.PINK_STAINED_GLASS);
        PAINTABLE.add(Material.PINK_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.PINK_TERRACOTTA);
        PAINTABLE.add(Material.PINK_WOOL);
        PAINTABLE.add(Material.PURPLE_CARPET);
        PAINTABLE.add(Material.PURPLE_CONCRETE);
        PAINTABLE.add(Material.PURPLE_CONCRETE_POWDER);
        PAINTABLE.add(Material.PURPLE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.PURPLE_STAINED_GLASS);
        PAINTABLE.add(Material.PURPLE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.PURPLE_TERRACOTTA);
        PAINTABLE.add(Material.PURPLE_WOOL);
        PAINTABLE.add(Material.RED_CARPET);
        PAINTABLE.add(Material.RED_CONCRETE);
        PAINTABLE.add(Material.RED_CONCRETE_POWDER);
        PAINTABLE.add(Material.RED_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.RED_STAINED_GLASS);
        PAINTABLE.add(Material.RED_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.RED_TERRACOTTA);
        PAINTABLE.add(Material.RED_WOOL);
        PAINTABLE.add(Material.WHITE_CARPET);
        PAINTABLE.add(Material.WHITE_CONCRETE);
        PAINTABLE.add(Material.WHITE_CONCRETE_POWDER);
        PAINTABLE.add(Material.WHITE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.WHITE_STAINED_GLASS);
        PAINTABLE.add(Material.WHITE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.WHITE_TERRACOTTA);
        PAINTABLE.add(Material.WHITE_WOOL);
        PAINTABLE.add(Material.YELLOW_CARPET);
        PAINTABLE.add(Material.YELLOW_CONCRETE);
        PAINTABLE.add(Material.YELLOW_CONCRETE_POWDER);
        PAINTABLE.add(Material.YELLOW_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.YELLOW_STAINED_GLASS);
        PAINTABLE.add(Material.YELLOW_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.YELLOW_TERRACOTTA);
        PAINTABLE.add(Material.YELLOW_WOOL);
    }

    public static List<Material> getPaintable() {
        return PAINTABLE;
    }

    public static void paint(TARDISPlugin plugin, Player player, Block b) {
        // must be in tardis world
        if (!plugin.getUtils().inTARDISWorld(player)) {
            TARDISMessage.send(player, "UPDATE_IN_WORLD");
            return;
        }
        // not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
        if (TARDISSonicRespect.checkBlockRespect(plugin, player, b)) {
            long now = System.currentTimeMillis();
            TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
            // check for dye in slot
            PlayerInventory inv = player.getInventory();
            ItemStack dye = inv.getItem(8);
            if (dye == null || !TARDISMaterials.dyes.contains(dye.getType())) {
                TARDISMessage.send(player, "SONIC_DYE");
                return;
            }
            // don't do anything if it is the same colour
            switch (b.getType()) {
                case BLACK_CARPET:
                case BLACK_CONCRETE:
                case BLACK_CONCRETE_POWDER:
                case BLACK_GLAZED_TERRACOTTA:
                case BLACK_STAINED_GLASS:
                case BLACK_STAINED_GLASS_PANE:
                case BLACK_TERRACOTTA:
                case BLACK_WOOL:
                    if (!dye.getType().equals(Material.BLACK_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case BLUE_CARPET:
                case BLUE_CONCRETE:
                case BLUE_CONCRETE_POWDER:
                case BLUE_GLAZED_TERRACOTTA:
                case BLUE_STAINED_GLASS:
                case BLUE_STAINED_GLASS_PANE:
                case BLUE_TERRACOTTA:
                case BLUE_WOOL:
                    if (!dye.getType().equals(Material.BLUE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case BROWN_CARPET:
                case BROWN_CONCRETE:
                case BROWN_CONCRETE_POWDER:
                case BROWN_GLAZED_TERRACOTTA:
                case BROWN_STAINED_GLASS:
                case BROWN_STAINED_GLASS_PANE:
                case BROWN_TERRACOTTA:
                case BROWN_WOOL:
                    if (!dye.getType().equals(Material.BROWN_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case CYAN_CARPET:
                case CYAN_CONCRETE:
                case CYAN_CONCRETE_POWDER:
                case CYAN_GLAZED_TERRACOTTA:
                case CYAN_STAINED_GLASS:
                case CYAN_STAINED_GLASS_PANE:
                case CYAN_TERRACOTTA:
                case CYAN_WOOL:
                    if (!dye.getType().equals(Material.CYAN_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case GRAY_CARPET:
                case GRAY_CONCRETE:
                case GRAY_CONCRETE_POWDER:
                case GRAY_GLAZED_TERRACOTTA:
                case GRAY_STAINED_GLASS:
                case GRAY_STAINED_GLASS_PANE:
                case GRAY_TERRACOTTA:
                case GRAY_WOOL:
                    if (!dye.getType().equals(Material.GRAY_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case GREEN_CARPET:
                case GREEN_CONCRETE:
                case GREEN_CONCRETE_POWDER:
                case GREEN_GLAZED_TERRACOTTA:
                case GREEN_STAINED_GLASS:
                case GREEN_STAINED_GLASS_PANE:
                case GREEN_TERRACOTTA:
                case GREEN_WOOL:
                    if (!dye.getType().equals(Material.GREEN_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case LIGHT_BLUE_CARPET:
                case LIGHT_BLUE_CONCRETE:
                case LIGHT_BLUE_CONCRETE_POWDER:
                case LIGHT_BLUE_GLAZED_TERRACOTTA:
                case LIGHT_BLUE_STAINED_GLASS:
                case LIGHT_BLUE_STAINED_GLASS_PANE:
                case LIGHT_BLUE_TERRACOTTA:
                case LIGHT_BLUE_WOOL:
                    if (!dye.getType().equals(Material.LIGHT_BLUE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case LIGHT_GRAY_CARPET:
                case LIGHT_GRAY_CONCRETE:
                case LIGHT_GRAY_CONCRETE_POWDER:
                case LIGHT_GRAY_GLAZED_TERRACOTTA:
                case LIGHT_GRAY_STAINED_GLASS:
                case LIGHT_GRAY_STAINED_GLASS_PANE:
                case LIGHT_GRAY_TERRACOTTA:
                case LIGHT_GRAY_WOOL:
                    if (!dye.getType().equals(Material.LIGHT_GRAY_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case LIME_CARPET:
                case LIME_CONCRETE:
                case LIME_CONCRETE_POWDER:
                case LIME_GLAZED_TERRACOTTA:
                case LIME_STAINED_GLASS:
                case LIME_STAINED_GLASS_PANE:
                case LIME_TERRACOTTA:
                case LIME_WOOL:
                    if (!dye.getType().equals(Material.LIME_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case MAGENTA_CARPET:
                case MAGENTA_CONCRETE:
                case MAGENTA_CONCRETE_POWDER:
                case MAGENTA_GLAZED_TERRACOTTA:
                case MAGENTA_STAINED_GLASS:
                case MAGENTA_STAINED_GLASS_PANE:
                case MAGENTA_TERRACOTTA:
                case MAGENTA_WOOL:
                    if (!dye.getType().equals(Material.MAGENTA_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case ORANGE_CARPET:
                case ORANGE_CONCRETE:
                case ORANGE_CONCRETE_POWDER:
                case ORANGE_GLAZED_TERRACOTTA:
                case ORANGE_STAINED_GLASS:
                case ORANGE_STAINED_GLASS_PANE:
                case ORANGE_TERRACOTTA:
                case ORANGE_WOOL:
                    if (!dye.getType().equals(Material.ORANGE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case PINK_CARPET:
                case PINK_CONCRETE:
                case PINK_CONCRETE_POWDER:
                case PINK_GLAZED_TERRACOTTA:
                case PINK_STAINED_GLASS:
                case PINK_STAINED_GLASS_PANE:
                case PINK_TERRACOTTA:
                case PINK_WOOL:
                    if (!dye.getType().equals(Material.PINK_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case PURPLE_CARPET:
                case PURPLE_CONCRETE:
                case PURPLE_CONCRETE_POWDER:
                case PURPLE_GLAZED_TERRACOTTA:
                case PURPLE_STAINED_GLASS:
                case PURPLE_STAINED_GLASS_PANE:
                case PURPLE_TERRACOTTA:
                case PURPLE_WOOL:
                    if (!dye.getType().equals(Material.PURPLE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case RED_CARPET:
                case RED_CONCRETE:
                case RED_CONCRETE_POWDER:
                case RED_GLAZED_TERRACOTTA:
                case RED_STAINED_GLASS:
                case RED_STAINED_GLASS_PANE:
                case RED_TERRACOTTA:
                case RED_WOOL:
                    if (!dye.getType().equals(Material.RED_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case WHITE_CARPET:
                case WHITE_CONCRETE:
                case WHITE_CONCRETE_POWDER:
                case WHITE_GLAZED_TERRACOTTA:
                case WHITE_STAINED_GLASS:
                case WHITE_STAINED_GLASS_PANE:
                case WHITE_TERRACOTTA:
                case WHITE_WOOL:
                    if (!dye.getType().equals(Material.WHITE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                case YELLOW_CARPET:
                case YELLOW_CONCRETE:
                case YELLOW_CONCRETE_POWDER:
                case YELLOW_GLAZED_TERRACOTTA:
                case YELLOW_STAINED_GLASS:
                case YELLOW_STAINED_GLASS_PANE:
                case YELLOW_TERRACOTTA:
                case YELLOW_WOOL:
                    if (!dye.getType().equals(Material.YELLOW_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                    break;
                default:
                    break;
            }
        } else {
            TARDISMessage.send(player, "SONIC_PROTECT");
        }
    }

    private static void changeColour(Block block, ItemStack dye, Inventory inv, Player player) {
        // remove one dye
        int a = dye.getAmount();
        int a2 = a - 1;
        if (a2 > 0) {
            Objects.requireNonNull(inv.getItem(8)).setAmount(a2);
        } else {
            inv.setItem(8, null);
        }
        player.updateInventory();
        // determine colour
        String[] b = block.getType().toString().split("_");
        String[] d = dye.getType().toString().split("_");
        b[0] = d[0];
        String joined = String.join("_", b);
        BlockData data = Material.valueOf(joined).createBlockData();
        block.setBlockData(data, true);
    }
}
