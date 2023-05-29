/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.Material;
import static org.bukkit.Material.BLACK_CANDLE;
import static org.bukkit.Material.BLACK_CANDLE_CAKE;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.block.data.type.Candle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TARDISSonicPainter {

    private static final List<Material> PAINTABLE = new ArrayList<>();

    static {
        PAINTABLE.add(Material.BLACK_BED);
        PAINTABLE.add(Material.BLACK_CANDLE);
        PAINTABLE.add(Material.BLACK_CANDLE_CAKE);
        PAINTABLE.add(Material.BLACK_CARPET);
        PAINTABLE.add(Material.BLACK_CONCRETE);
        PAINTABLE.add(Material.BLACK_CONCRETE_POWDER);
        PAINTABLE.add(Material.BLACK_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.BLACK_STAINED_GLASS);
        PAINTABLE.add(Material.BLACK_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.BLACK_TERRACOTTA);
        PAINTABLE.add(Material.BLACK_WOOL);
        PAINTABLE.add(Material.BLUE_BED);
        PAINTABLE.add(Material.BLUE_CANDLE);
        PAINTABLE.add(Material.BLUE_CANDLE_CAKE);
        PAINTABLE.add(Material.BLUE_CARPET);
        PAINTABLE.add(Material.BLUE_CONCRETE);
        PAINTABLE.add(Material.BLUE_CONCRETE_POWDER);
        PAINTABLE.add(Material.BLUE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.BLUE_STAINED_GLASS);
        PAINTABLE.add(Material.BLUE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.BLUE_TERRACOTTA);
        PAINTABLE.add(Material.BLUE_WOOL);
        PAINTABLE.add(Material.BROWN_BED);
        PAINTABLE.add(Material.BROWN_CANDLE);
        PAINTABLE.add(Material.BROWN_CANDLE_CAKE);
        PAINTABLE.add(Material.BROWN_CARPET);
        PAINTABLE.add(Material.BROWN_CONCRETE);
        PAINTABLE.add(Material.BROWN_CONCRETE_POWDER);
        PAINTABLE.add(Material.BROWN_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.BROWN_STAINED_GLASS);
        PAINTABLE.add(Material.BROWN_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.BROWN_TERRACOTTA);
        PAINTABLE.add(Material.BROWN_WOOL);
        PAINTABLE.add(Material.CYAN_BED);
        PAINTABLE.add(Material.CYAN_CANDLE);
        PAINTABLE.add(Material.CYAN_CANDLE_CAKE);
        PAINTABLE.add(Material.CYAN_CARPET);
        PAINTABLE.add(Material.CYAN_CONCRETE);
        PAINTABLE.add(Material.CYAN_CONCRETE_POWDER);
        PAINTABLE.add(Material.CYAN_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.CYAN_STAINED_GLASS);
        PAINTABLE.add(Material.CYAN_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.CYAN_TERRACOTTA);
        PAINTABLE.add(Material.CYAN_WOOL);
        PAINTABLE.add(Material.GRAY_BED);
        PAINTABLE.add(Material.GRAY_CANDLE);
        PAINTABLE.add(Material.GRAY_CANDLE_CAKE);
        PAINTABLE.add(Material.GRAY_CARPET);
        PAINTABLE.add(Material.GRAY_CONCRETE);
        PAINTABLE.add(Material.GRAY_CONCRETE_POWDER);
        PAINTABLE.add(Material.GRAY_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.GRAY_STAINED_GLASS);
        PAINTABLE.add(Material.GRAY_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.GRAY_TERRACOTTA);
        PAINTABLE.add(Material.GRAY_WOOL);
        PAINTABLE.add(Material.GREEN_BED);
        PAINTABLE.add(Material.GREEN_CANDLE);
        PAINTABLE.add(Material.GREEN_CANDLE_CAKE);
        PAINTABLE.add(Material.GREEN_CARPET);
        PAINTABLE.add(Material.GREEN_CONCRETE);
        PAINTABLE.add(Material.GREEN_CONCRETE_POWDER);
        PAINTABLE.add(Material.GREEN_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.GREEN_STAINED_GLASS);
        PAINTABLE.add(Material.GREEN_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.GREEN_TERRACOTTA);
        PAINTABLE.add(Material.GREEN_WOOL);
        PAINTABLE.add(Material.LIGHT_BLUE_BED);
        PAINTABLE.add(Material.LIGHT_BLUE_CANDLE);
        PAINTABLE.add(Material.LIGHT_BLUE_CANDLE_CAKE);
        PAINTABLE.add(Material.LIGHT_BLUE_CARPET);
        PAINTABLE.add(Material.LIGHT_BLUE_CONCRETE);
        PAINTABLE.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
        PAINTABLE.add(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_BLUE_STAINED_GLASS);
        PAINTABLE.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.LIGHT_BLUE_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_BLUE_WOOL);
        PAINTABLE.add(Material.LIGHT_GRAY_BED);
        PAINTABLE.add(Material.LIGHT_GRAY_CANDLE);
        PAINTABLE.add(Material.LIGHT_GRAY_CANDLE_CAKE);
        PAINTABLE.add(Material.LIGHT_GRAY_CARPET);
        PAINTABLE.add(Material.LIGHT_GRAY_CONCRETE);
        PAINTABLE.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
        PAINTABLE.add(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_GRAY_STAINED_GLASS);
        PAINTABLE.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.LIGHT_GRAY_TERRACOTTA);
        PAINTABLE.add(Material.LIGHT_GRAY_WOOL);
        PAINTABLE.add(Material.LIME_BED);
        PAINTABLE.add(Material.LIME_CANDLE);
        PAINTABLE.add(Material.LIME_CANDLE_CAKE);
        PAINTABLE.add(Material.LIME_CARPET);
        PAINTABLE.add(Material.LIME_CONCRETE);
        PAINTABLE.add(Material.LIME_CONCRETE_POWDER);
        PAINTABLE.add(Material.LIME_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.LIME_STAINED_GLASS);
        PAINTABLE.add(Material.LIME_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.LIME_TERRACOTTA);
        PAINTABLE.add(Material.LIME_WOOL);
        PAINTABLE.add(Material.MAGENTA_BED);
        PAINTABLE.add(Material.MAGENTA_CANDLE);
        PAINTABLE.add(Material.MAGENTA_CANDLE_CAKE);
        PAINTABLE.add(Material.MAGENTA_CARPET);
        PAINTABLE.add(Material.MAGENTA_CONCRETE);
        PAINTABLE.add(Material.MAGENTA_CONCRETE_POWDER);
        PAINTABLE.add(Material.MAGENTA_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.MAGENTA_STAINED_GLASS);
        PAINTABLE.add(Material.MAGENTA_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.MAGENTA_TERRACOTTA);
        PAINTABLE.add(Material.MAGENTA_WOOL);
        PAINTABLE.add(Material.ORANGE_BED);
        PAINTABLE.add(Material.ORANGE_CANDLE);
        PAINTABLE.add(Material.ORANGE_CANDLE_CAKE);
        PAINTABLE.add(Material.ORANGE_CARPET);
        PAINTABLE.add(Material.ORANGE_CONCRETE);
        PAINTABLE.add(Material.ORANGE_CONCRETE_POWDER);
        PAINTABLE.add(Material.ORANGE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.ORANGE_STAINED_GLASS);
        PAINTABLE.add(Material.ORANGE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.ORANGE_TERRACOTTA);
        PAINTABLE.add(Material.ORANGE_WOOL);
        PAINTABLE.add(Material.PINK_BED);
        PAINTABLE.add(Material.PINK_CANDLE);
        PAINTABLE.add(Material.PINK_CANDLE_CAKE);
        PAINTABLE.add(Material.PINK_CARPET);
        PAINTABLE.add(Material.PINK_CONCRETE);
        PAINTABLE.add(Material.PINK_CONCRETE_POWDER);
        PAINTABLE.add(Material.PINK_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.PINK_STAINED_GLASS);
        PAINTABLE.add(Material.PINK_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.PINK_TERRACOTTA);
        PAINTABLE.add(Material.PINK_WOOL);
        PAINTABLE.add(Material.PURPLE_BED);
        PAINTABLE.add(Material.PURPLE_CANDLE);
        PAINTABLE.add(Material.PURPLE_CANDLE_CAKE);
        PAINTABLE.add(Material.PURPLE_CARPET);
        PAINTABLE.add(Material.PURPLE_CONCRETE);
        PAINTABLE.add(Material.PURPLE_CONCRETE_POWDER);
        PAINTABLE.add(Material.PURPLE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.PURPLE_STAINED_GLASS);
        PAINTABLE.add(Material.PURPLE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.PURPLE_TERRACOTTA);
        PAINTABLE.add(Material.PURPLE_WOOL);
        PAINTABLE.add(Material.RED_BED);
        PAINTABLE.add(Material.RED_CANDLE);
        PAINTABLE.add(Material.RED_CANDLE_CAKE);
        PAINTABLE.add(Material.RED_CARPET);
        PAINTABLE.add(Material.RED_CONCRETE);
        PAINTABLE.add(Material.RED_CONCRETE_POWDER);
        PAINTABLE.add(Material.RED_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.RED_STAINED_GLASS);
        PAINTABLE.add(Material.RED_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.RED_TERRACOTTA);
        PAINTABLE.add(Material.RED_WOOL);
        PAINTABLE.add(Material.WHITE_BED);
        PAINTABLE.add(Material.WHITE_CANDLE);
        PAINTABLE.add(Material.WHITE_CANDLE_CAKE);
        PAINTABLE.add(Material.WHITE_CARPET);
        PAINTABLE.add(Material.WHITE_CONCRETE);
        PAINTABLE.add(Material.WHITE_CONCRETE_POWDER);
        PAINTABLE.add(Material.WHITE_GLAZED_TERRACOTTA);
        PAINTABLE.add(Material.WHITE_STAINED_GLASS);
        PAINTABLE.add(Material.WHITE_STAINED_GLASS_PANE);
        PAINTABLE.add(Material.WHITE_TERRACOTTA);
        PAINTABLE.add(Material.WHITE_WOOL);
        PAINTABLE.add(Material.YELLOW_BED);
        PAINTABLE.add(Material.YELLOW_CANDLE);
        PAINTABLE.add(Material.YELLOW_CANDLE_CAKE);
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

    public static void paint(TARDIS plugin, Player player, Block b) {
        // must be in TARDIS world
        if (!plugin.getUtils().inTARDISWorld(player)) {
            TARDISMessage.send(player, "UPDATE_IN_WORLD");
            return;
        }
        // not protected blocks - WorldGuard / GriefPrevention / Towny
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
                case BLACK_CARPET, BLACK_CONCRETE, BLACK_CONCRETE_POWDER, BLACK_GLAZED_TERRACOTTA, BLACK_STAINED_GLASS, BLACK_STAINED_GLASS_PANE, BLACK_TERRACOTTA, BLACK_WOOL, BLACK_CANDLE, BLACK_CANDLE_CAKE, BLACK_BED -> {
                    if (!dye.getType().equals(Material.BLACK_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case BLUE_CARPET, BLUE_CONCRETE, BLUE_CONCRETE_POWDER, BLUE_GLAZED_TERRACOTTA, BLUE_STAINED_GLASS, BLUE_STAINED_GLASS_PANE, BLUE_TERRACOTTA, BLUE_WOOL, BLUE_CANDLE, BLUE_CANDLE_CAKE, BLUE_BED -> {
                    if (!dye.getType().equals(Material.BLUE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case BROWN_CARPET, BROWN_CONCRETE, BROWN_CONCRETE_POWDER, BROWN_GLAZED_TERRACOTTA, BROWN_STAINED_GLASS, BROWN_STAINED_GLASS_PANE, BROWN_TERRACOTTA, BROWN_WOOL, BROWN_CANDLE, BROWN_CANDLE_CAKE, BROWN_BED -> {
                    if (!dye.getType().equals(Material.BROWN_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case CYAN_CARPET, CYAN_CONCRETE, CYAN_CONCRETE_POWDER, CYAN_GLAZED_TERRACOTTA, CYAN_STAINED_GLASS, CYAN_STAINED_GLASS_PANE, CYAN_TERRACOTTA, CYAN_WOOL, CYAN_CANDLE, CYAN_CANDLE_CAKE, CYAN_BED -> {
                    if (!dye.getType().equals(Material.CYAN_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case GRAY_CARPET, GRAY_CONCRETE, GRAY_CONCRETE_POWDER, GRAY_GLAZED_TERRACOTTA, GRAY_STAINED_GLASS, GRAY_STAINED_GLASS_PANE, GRAY_TERRACOTTA, GRAY_WOOL, GRAY_CANDLE, GRAY_CANDLE_CAKE, GRAY_BED -> {
                    if (!dye.getType().equals(Material.GRAY_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case GREEN_CARPET, GREEN_CONCRETE, GREEN_CONCRETE_POWDER, GREEN_GLAZED_TERRACOTTA, GREEN_STAINED_GLASS, GREEN_STAINED_GLASS_PANE, GREEN_TERRACOTTA, GREEN_WOOL, GREEN_CANDLE, GREEN_CANDLE_CAKE -> {
                    if (!dye.getType().equals(Material.GREEN_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case LIGHT_BLUE_CARPET, LIGHT_BLUE_CONCRETE, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_BLUE_GLAZED_TERRACOTTA, LIGHT_BLUE_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS_PANE, LIGHT_BLUE_TERRACOTTA, LIGHT_BLUE_WOOL, LIGHT_BLUE_CANDLE, LIGHT_BLUE_CANDLE_CAKE, LIGHT_BLUE_BED -> {
                    if (!dye.getType().equals(Material.LIGHT_BLUE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case LIGHT_GRAY_CARPET, LIGHT_GRAY_CONCRETE, LIGHT_GRAY_CONCRETE_POWDER, LIGHT_GRAY_GLAZED_TERRACOTTA, LIGHT_GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS_PANE, LIGHT_GRAY_TERRACOTTA, LIGHT_GRAY_WOOL, LIGHT_GRAY_CANDLE, LIGHT_GRAY_CANDLE_CAKE, LIGHT_GRAY_BED -> {
                    if (!dye.getType().equals(Material.LIGHT_GRAY_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case LIME_CARPET, LIME_CONCRETE, LIME_CONCRETE_POWDER, LIME_GLAZED_TERRACOTTA, LIME_STAINED_GLASS, LIME_STAINED_GLASS_PANE, LIME_TERRACOTTA, LIME_WOOL, LIME_CANDLE, LIME_CANDLE_CAKE, LIME_BED -> {
                    if (!dye.getType().equals(Material.LIME_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case MAGENTA_CARPET, MAGENTA_CONCRETE, MAGENTA_CONCRETE_POWDER, MAGENTA_GLAZED_TERRACOTTA, MAGENTA_STAINED_GLASS, MAGENTA_STAINED_GLASS_PANE, MAGENTA_TERRACOTTA, MAGENTA_WOOL, MAGENTA_CANDLE, MAGENTA_CANDLE_CAKE, MAGENTA_BED -> {
                    if (!dye.getType().equals(Material.MAGENTA_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case ORANGE_CARPET, ORANGE_CONCRETE, ORANGE_CONCRETE_POWDER, ORANGE_GLAZED_TERRACOTTA, ORANGE_STAINED_GLASS, ORANGE_STAINED_GLASS_PANE, ORANGE_TERRACOTTA, ORANGE_WOOL, ORANGE_CANDLE, ORANGE_CANDLE_CAKE, ORANGE_BED -> {
                    if (!dye.getType().equals(Material.ORANGE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case PINK_CARPET, PINK_CONCRETE, PINK_CONCRETE_POWDER, PINK_GLAZED_TERRACOTTA, PINK_STAINED_GLASS, PINK_STAINED_GLASS_PANE, PINK_TERRACOTTA, PINK_WOOL, PINK_CANDLE, PINK_CANDLE_CAKE, PINK_BED -> {
                    if (!dye.getType().equals(Material.PINK_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case PURPLE_CARPET, PURPLE_CONCRETE, PURPLE_CONCRETE_POWDER, PURPLE_GLAZED_TERRACOTTA, PURPLE_STAINED_GLASS, PURPLE_STAINED_GLASS_PANE, PURPLE_TERRACOTTA, PURPLE_WOOL, PURPLE_CANDLE, PURPLE_CANDLE_CAKE -> {
                    if (!dye.getType().equals(Material.PURPLE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case RED_CARPET, RED_CONCRETE, RED_CONCRETE_POWDER, RED_GLAZED_TERRACOTTA, RED_STAINED_GLASS, RED_STAINED_GLASS_PANE, RED_TERRACOTTA, RED_WOOL, RED_CANDLE, RED_CANDLE_CAKE, RED_BED -> {
                    if (!dye.getType().equals(Material.RED_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case WHITE_CARPET, WHITE_CONCRETE, WHITE_CONCRETE_POWDER, WHITE_GLAZED_TERRACOTTA, WHITE_STAINED_GLASS, WHITE_STAINED_GLASS_PANE, WHITE_TERRACOTTA, WHITE_WOOL, WHITE_CANDLE, WHITE_CANDLE_CAKE, WHITE_BED -> {
                    if (!dye.getType().equals(Material.WHITE_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                case YELLOW_CARPET, YELLOW_CONCRETE, YELLOW_CONCRETE_POWDER, YELLOW_GLAZED_TERRACOTTA, YELLOW_STAINED_GLASS, YELLOW_STAINED_GLASS_PANE, YELLOW_TERRACOTTA, YELLOW_WOOL, YELLOW_CANDLE, YELLOW_CANDLE_CAKE -> {
                    if (!dye.getType().equals(Material.YELLOW_DYE)) {
                        changeColour(b, dye, inv, player);
                    }
                }
                default -> {
                }
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
            inv.getItem(8).setAmount(a2);
        } else {
            inv.setItem(8, null);
        }
        player.updateInventory();
        // determine colour
        String[] b = block.getType().toString().split("_");
        String[] d = dye.getType().toString().split("_");
        String joined;
        if (b[0].equals("LIGHT") && d[0].equals("LIGHT")) {
            b[1] = d[1];
            joined = String.join("_", b);
        } else if (b[0].equals("LIGHT")) {
            b[1] = d[0];
            joined = String.join("_", Arrays.copyOfRange(b, 1, b.length));
        } else if (d[0].equals("LIGHT")) {
            b[0] = d[1];
            joined = "LIGHT_" + String.join("_", b);
        } else {
            b[0] = d[0];
            joined = String.join("_", b);
        }
        BlockData original = block.getBlockData();
        BlockData data = Material.valueOf(joined).createBlockData();
        if (original instanceof Candle candle) {
            ((Candle) data).setCandles(candle.getCandles());
        }
        if (original instanceof Lightable lightable) {
            ((Lightable) data).setLit(lightable.isLit());
        }
        if (original instanceof Bed bed) {
            ((Bed) data).setPart(bed.getPart());
            ((Bed) data).setFacing(bed.getFacing());
            block.setBlockData(data, false);
            // also change the other side of the bed
            Block side;
            switch (bed.getFacing()) {
                case WEST -> side = (bed.getPart() == Part.HEAD) ? block.getRelative(BlockFace.EAST) : block.getRelative(BlockFace.WEST);
                case NORTH -> side = (bed.getPart() == Part.HEAD) ? block.getRelative(BlockFace.SOUTH) : block.getRelative(BlockFace.NORTH);
                case EAST -> side = (bed.getPart() == Part.HEAD) ? block.getRelative(BlockFace.WEST) : block.getRelative(BlockFace.EAST);
                default -> side = (bed.getPart() == Part.HEAD) ? block.getRelative(BlockFace.NORTH) : block.getRelative(BlockFace.SOUTH); // SOUTH
            }
            ((Bed) data).setPart(getOpposite(bed.getPart()));
            side.setBlockData(data, false);
        } else {
            block.setBlockData(data, true); 
        }
    }

    private static Part getOpposite(Part part) {
        return part == Part.FOOT ? Part.HEAD : Part.FOOT;
    }
}
