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
package me.eccentric_nz.TARDIS.utility.protection;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TARDISRedProtectChecker {

    /**
     * Checks to see whether the specified location is within a GriefPrevention claim.
     *
     * @param player   the player to check access for.
     * @param location the location to check.
     * @return true or false depending on whether the player has rights to build within the region
     */
    public static boolean canBuild(Player player, Location location) {
        boolean canbuild = true;
        Region region = RedProtect.get().getRegionManager().getTopRegion(location);
        if (!region.canBuild(player)) {
            canbuild = false;
        }
        return canbuild;
    }

    public static boolean canSonic(Player player, Block block) {
        boolean cansonic;
        Region region = RedProtect.get().getRegionManager().getTopRegion(block.getLocation());
        cansonic = switch (block.getType()) {
            case ENDER_CHEST -> region.canEnderChest(player);
            case CHEST, TRAPPED_CHEST -> region.canChest(player);
            case LEVER -> region.canLever(player);
            case DETECTOR_RAIL, MUSHROOM_STEM, PISTON, POLISHED_BLACKSTONE_BUTTON, POWERED_RAIL, REDSTONE_LAMP, REDSTONE_WIRE, STICKY_PISTON -> region.canRedstone(player);
            case ACACIA_BUTTON, BAMBOO_BUTTON, BIRCH_BUTTON, CHERRY_BUTTON, CRIMSON_BUTTON, DARK_OAK_BUTTON, JUNGLE_BUTTON, OAK_BUTTON, SPRUCE_BUTTON, STONE_BUTTON, WARPED_BUTTON -> region.canButton(player);
            case ACACIA_DOOR, ACACIA_TRAPDOOR, BAMBOO_DOOR, BAMBOO_TRAPDOOR, BIRCH_DOOR, BIRCH_TRAPDOOR, CHERRY_DOOR, CHERRY_TRAPDOOR, CRIMSON_DOOR, CRIMSON_TRAPDOOR, DARK_OAK_DOOR, DARK_OAK_TRAPDOOR, IRON_DOOR, IRON_TRAPDOOR, JUNGLE_DOOR, JUNGLE_TRAPDOOR, MANGROVE_DOOR, MANGROVE_TRAPDOOR, OAK_DOOR, OAK_TRAPDOOR, SPRUCE_DOOR, SPRUCE_TRAPDOOR, WARPED_DOOR, WARPED_TRAPDOOR -> region.canDoor(player);
            case CANDLE, BLACK_CANDLE, BLUE_CANDLE, BROWN_CANDLE, CYAN_CANDLE, GRAY_CANDLE, GREEN_CANDLE, LIGHT_BLUE_CANDLE, LIGHT_GRAY_CANDLE, LIME_CANDLE, MAGENTA_CANDLE, ORANGE_CANDLE, PINK_CANDLE, PURPLE_CANDLE, RED_CANDLE, WHITE_CANDLE, YELLOW_CANDLE, BLACK_CANDLE_CAKE, BLUE_CANDLE_CAKE, BROWN_CANDLE_CAKE, CYAN_CANDLE_CAKE, GRAY_CANDLE_CAKE, GREEN_CANDLE_CAKE, LIGHT_BLUE_CANDLE_CAKE, LIGHT_GRAY_CANDLE_CAKE, LIME_CANDLE_CAKE, MAGENTA_CANDLE_CAKE, ORANGE_CANDLE_CAKE, PINK_CANDLE_CAKE, PURPLE_CANDLE_CAKE, RED_CANDLE_CAKE, WHITE_CANDLE_CAKE, YELLOW_CANDLE_CAKE, NETHERRACK, OBSIDIAN, SOUL_CAMPFIRE, SOUL_SAND, SOUL_SOIL -> region.canFire();
            default -> region.canBreak(block.getType());
        };
        return cansonic;
    }
}
