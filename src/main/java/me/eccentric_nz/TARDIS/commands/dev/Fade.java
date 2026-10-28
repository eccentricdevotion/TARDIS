package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.discovery.Mutator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class Fade {

    private final TARDIS plugin;
    private final Material[][][] box = new Material[9][9][9];

    public Fade(TARDIS plugin) {
        this.plugin = plugin;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    box[i][j][k] = Material.TUFF;
                }
            }
        }
        box[0][0][0] = Material.MUDDY_MANGROVE_ROOTS;
    }

    public void build(Player player) {
        // async task to populate box with block fade
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Material[][][] array = new Mutator(player.getLocation().getWorld().getSeed(), 10).addBlends(box);
            // switch back to the main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                Location location = player.getLocation().add(0, 3, 0);
                place(location, array);
            });
        });
    }

    private void place(Location location, Material[][][] materials) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    BlockData data = materials[i][j][k].createBlockData();
                    world.setBlockData(x + i, y + j, z + k, data);
                }
            }
        }
    }
}
