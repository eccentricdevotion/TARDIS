package me.eccentric_nz.TARDIS.commands.dev;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public class MapCoords {

    public void getHeldMapBounds(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() != Material.FILLED_MAP) {
            player.sendMessage("You are not holding a filled map!");
            return;
        }
        if (heldItem.getItemMeta() instanceof MapMeta mapMeta) {
            MapView mapView = mapMeta.getMapView();
            if (mapView == null) {
                return;
            }
            int centerX = mapView.getCenterX();
            int centerZ = mapView.getCenterZ();
            // get scale and calculate the map's total width in blocks
            MapView.Scale scale = mapView.getScale();
            int blocksPerPixel = switch (scale) {
                case CLOSEST -> 1;   // scale 0
                case CLOSE -> 2;     // scale 1
                case NORMAL -> 4;    // scale 2
                case FAR -> 8;       // scale 3
                case FARTHEST -> 16; // scale 4
            };
            int mapSizeInBlocks = 128 * blocksPerPixel;
            // calculate the exact boundaries of the map grid
            // because maps snap to a global grid, we can safely compute corners from the center
            int minBlockX = centerX - (mapSizeInBlocks / 2);
            int minBlockZ = centerZ - (mapSizeInBlocks / 2);
            int maxBlockX = minBlockX + mapSizeInBlocks - 1;
            int maxBlockZ = minBlockZ + mapSizeInBlocks - 1;
            // convert block boundaries to chunk boundaries
            int minChunkX = minBlockX >> 4;
            int minChunkZ = minBlockZ >> 4;
            int maxChunkX = maxBlockX >> 4;
            int maxChunkZ = maxBlockZ >> 4;
            // output results
            player.sendMessage(String.format("Map Scale: %s (Size: %dx%d blocks)", scale.name(), mapSizeInBlocks, mapSizeInBlocks));
            player.sendMessage(String.format("Block Bounds: X(%d to %d), Z(%d to %d)", minBlockX, maxBlockX, minBlockZ, maxBlockZ));
            player.sendMessage(String.format("Chunk Bounds: X(%d to %d), Z(%d to %d)", minChunkX, maxChunkX, minChunkZ, maxChunkZ));
        }
    }
}
