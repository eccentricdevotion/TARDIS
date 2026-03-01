package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AreaShow {

    public void display(TARDIS plugin, String name, Player player) {
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", name);
        ResultSetAreas rsaShow = new ResultSetAreas(plugin, wherea, false, false);
        if (!rsaShow.resultSet()) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return;
        }
        if (!rsaShow.getArea().grid()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_SHOW_NONGRID");
            return;
        }
        Area a = rsaShow.getArea();
        int mix = a.minX();
        int miz = a.minZ();
        int max = a.maxX();
        int maz = a.maxZ();
        World w = TARDISAliasResolver.getWorldFromAlias(a.world());
        Set<Block> markers = new HashSet<>();
        markers.add(w.getHighestBlockAt(mix, miz).getRelative(BlockFace.UP));
        markers.add(w.getHighestBlockAt(mix, maz).getRelative(BlockFace.UP));
        markers.add(w.getHighestBlockAt(max, miz).getRelative(BlockFace.UP));
        markers.add(w.getHighestBlockAt(max, maz).getRelative(BlockFace.UP));
        for (Block block : markers) {
            player.sendBlockChange(block.getLocation(), TARDISConstants.SNOW_BLOCK);
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (Block block : markers) {
                block.getState().update();
            }
        }, 300L);
    }
}
