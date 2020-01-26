package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;

public class TARDISSonicIgnite {

    public static void ignite(TARDIS plugin, Block b, Player p) {
        if (!TARDISSonicRespect.checkBlockRespect(plugin, p, b)) {
            Block above = b.getRelative(BlockFace.UP);
            if (b.getType().equals(Material.TNT)) {
                b.setBlockData(TARDISConstants.AIR);
                b.getWorld().spawnEntity(b.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.PRIMED_TNT);
                plugin.getPM().callEvent(new BlockIgniteEvent(b, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, p));
                return;
            }
            if (above.getType().equals(Material.AIR)) {
                above.setBlockData(TARDISConstants.FIRE);
                // call a block ignite event
                plugin.getPM().callEvent(new BlockIgniteEvent(b, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, p));
            }
        }
    }
}
