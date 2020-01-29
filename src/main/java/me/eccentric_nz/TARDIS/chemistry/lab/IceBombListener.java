package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

public class IceBombListener implements Listener {

    private final TARDIS plugin;

    public IceBombListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIceBombThrow(ProjectileLaunchEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Snowball) {
            Snowball snowball = (Snowball) entity;
            ProjectileSource shooter = snowball.getShooter();
            if (shooter instanceof Player) {
                Player player = (Player) shooter;
                ItemStack is = player.getInventory().getItemInMainHand();
                if (is != null && is.getType() == Material.SNOWBALL && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Ice Bomb")) {
                    snowball.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, "Ice_Bomb");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onIceBombHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Snowball && entity.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.STRING)) {
            Block block = event.getHitBlock();
            if (block != null) {
                Block up = block.getRelative(BlockFace.UP);
                if (up.getType().equals(Material.WATER)) {
                    // should really do some fancy vector math to get the first water block that the snowball entered
                    while (up.getType().equals(Material.WATER)) {
                        up = up.getRelative(BlockFace.UP);
                    }
                    up = up.getRelative(BlockFace.DOWN);
                    // check plugin respect
                    if (plugin.getPluginRespect().getRespect(block.getLocation(), new Parameters((Player) event.getEntity().getShooter(), FLAG.getNoMessageFlags()))) {
                        // freeze water
                        up.setBlockData(TARDISConstants.AIR);
                        for (BlockFace face : plugin.getGeneralKeeper().getSurrounding()) {
                            Block water = up.getRelative(face);
                            if (water.getType().equals(Material.WATER)) {
                                water.setBlockData(TARDISConstants.AIR);
                            }
                        }
                    }
                }
            }
        }
    }
}
