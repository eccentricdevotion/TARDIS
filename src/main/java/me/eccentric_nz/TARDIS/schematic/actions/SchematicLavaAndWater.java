package me.eccentric_nz.TARDIS.schematic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SchematicLavaAndWater {

    public boolean act(TARDIS plugin, Player player, boolean lava) {
        UUID uuid = player.getUniqueId();
        // check they have selected start and end blocks
        if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NO_START");
            return true;
        }
        if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NO_END");
            return true;
        }
        // get the world
        World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
        String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getName();
        if (!w.getName().equals(chk_w)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_WORLD");
            return true;
        }
        // get the raw coords
        int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
        int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
        int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
        int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
        int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
        int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
        // get the min & max coords
        int minx = Math.min(sx, ex);
        int maxx = Math.max(sx, ex);
        int miny = Math.min(sy, ey);
        int maxy = Math.max(sy, ey);
        int minz = Math.min(sz, ez);
        int maxz = Math.max(sz, ez);
        Material which = lava ? Material.LAVA : Material.WATER;
        // loop through the blocks inside this cube
        for (int l = miny; l <= maxy; l++) {
            for (int r = minx; r <= maxx; r++) {
                for (int c = minz; c <= maxz; c++) {
                    Block b = w.getBlockAt(r, l, c);
                    if (b.getType().equals(which)) {
                        Levelled to = (Levelled) which.createBlockData();
//                        to.setLevel(0);
                        b.setBlockData(to);
                    }
                }
            }
        }
        return true;
    }
}
