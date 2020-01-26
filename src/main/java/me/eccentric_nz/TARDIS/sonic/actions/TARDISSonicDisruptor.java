package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TARDISSonicDisruptor {

    public static void breakBlock(TARDIS plugin, Player player, Block block) {
        // not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
        if (TARDISSonicRespect.checkBlockRespect(plugin, player, block)) {
            TARDISMessage.send(player, "SONIC_PROTECT");
            return;
        }
        TARDISSonicSound.playSonicSound(plugin, player, System.currentTimeMillis(), 600L, "sonic_short");
        // drop appropriate material
        Material mat = block.getType();
        if (player.hasPermission("tardis.sonic.silktouch")) {
            Location l = block.getLocation();
            if (mat.equals(Material.SNOW)) {
                Snow snow = (Snow) block.getBlockData();
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL, 1 + snow.getLayers()));
            } else {
                l.getWorld().dropItemNaturally(l, new ItemStack(block.getType(), 1));
            }
            l.getWorld().playSound(l, Sound.ENTITY_SHEEP_SHEAR, 1.0F, 1.5F);
            // set the block to AIR
            block.setBlockData(TARDISConstants.AIR);
        } else if (mat.equals(Material.SNOW) || mat.equals(Material.SNOW_BLOCK)) {
            // how many?
            int balls;
            if (mat.equals(Material.SNOW_BLOCK)) {
                balls = 4;
            } else {
                Snow snow = (Snow) block.getBlockData();
                balls = 1 + snow.getLayers();
            }
            block.setBlockData(TARDISConstants.AIR);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL, balls));
        } else {
            block.breakNaturally();
            block.getLocation().getWorld().playSound(block.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1.0F, 1.5F);
        }
    }
}
