package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISSonicBlockConverter {

    static HashMap<Material, Material> convertable = new HashMap<>();

    static {
        convertable.put(Material.WHITE_CONCRETE_POWDER, Material.WHITE_CONCRETE);
        convertable.put(Material.ORANGE_CONCRETE_POWDER, Material.ORANGE_CONCRETE);
        convertable.put(Material.MAGENTA_CONCRETE_POWDER, Material.MAGENTA_CONCRETE);
        convertable.put(Material.YELLOW_CONCRETE_POWDER, Material.YELLOW_CONCRETE);
        convertable.put(Material.LIGHT_BLUE_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE);
        convertable.put(Material.CYAN_CONCRETE_POWDER, Material.CYAN_CONCRETE);
        convertable.put(Material.LIME_CONCRETE_POWDER, Material.LIME_CONCRETE);
        convertable.put(Material.PINK_CONCRETE_POWDER, Material.PINK_CONCRETE);
        convertable.put(Material.GRAY_CONCRETE_POWDER, Material.GRAY_CONCRETE);
        convertable.put(Material.LIGHT_GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE);
        convertable.put(Material.BLUE_CONCRETE_POWDER, Material.BLUE_CONCRETE);
        convertable.put(Material.BROWN_CONCRETE_POWDER, Material.BROWN_CONCRETE);
        convertable.put(Material.GREEN_CONCRETE_POWDER, Material.GREEN_CONCRETE);
        convertable.put(Material.PURPLE_CONCRETE_POWDER, Material.PURPLE_CONCRETE);
        convertable.put(Material.RED_CONCRETE_POWDER, Material.RED_CONCRETE);
        convertable.put(Material.BLACK_CONCRETE_POWDER, Material.BLACK_CONCRETE);
        convertable.put(Material.MUD, Material.CLAY);
        convertable.put(Material.DIRT, Material.MUD);
        convertable.put(Material.COARSE_DIRT, Material.MUD);
        convertable.put(Material.ROOTED_DIRT, Material.MUD);
    }

    public static void transform(TARDIS plugin, Block block, Player player) {
        // loop through a radius of blocks
        int r = plugin.getConfig().getInt("sonic.conversion_radius");
        for (int x = block.getX() - r; x <= block.getX() + r; x++) {
            for (int z = block.getZ() - r; z <= block.getZ() + r; z++) {
                Block b = block.getWorld().getBlockAt(x, block.getY(), z);
                if (TARDISSonicRespect.checkBlockRespect(plugin, player, b)) {
                    Material from = b.getType();
                    if (convertable.containsKey(from)) {
                        Material to = convertable.get(from);
                        SonicConverterRunnable scr = new SonicConverterRunnable(b, from, to);
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, scr, 1L, 5L);
                        scr.setTaskId(task);
                    }
                }
            }
        }
    }
}
