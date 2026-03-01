package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class AreaYard {

    public void create(TARDIS plugin, String name, String fill, String dock, Player player) {
        BlockData fillData;
        BlockData dockData;
        try {
            fillData = Material.valueOf(fill.toUpperCase(Locale.ROOT)).createBlockData();
            dockData = Material.valueOf(dock.toUpperCase(Locale.ROOT)).createBlockData();
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_MATERIAL");
            return;
        }
        if (!fillData.getMaterial().isBlock() || !dockData.getMaterial().isBlock() || !fillData.getMaterial().isSolid() || !dockData.getMaterial().isSolid()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_NOT_BLOCK");
            return;
        }
        HashMap<String, Object> yardWhere = new HashMap<>();
        yardWhere.put("area_name", name);
        ResultSetAreas rsaYard = new ResultSetAreas(plugin, yardWhere, false, false);
        if (!rsaYard.resultSet()) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return;
        }
        Area yardArea = rsaYard.getArea();
        int yardMinX = yardArea.minX();
        int yardMinZ = yardArea.minZ();
        int yardMaxX = yardArea.maxX();
        int yardMaxZ = yardArea.maxZ();
        World yardWorld = TARDISAliasResolver.getWorldFromAlias(yardArea.world());
        for (int x = yardMinX; x <= yardMaxX; x++) {
            for (int z = yardMinZ; z <= yardMaxZ; z++) {
                int y = yardWorld.getHighestBlockYAt(x, z);
                if ((x - 2) % 5 == 0 && (z - 2) % 5 == 0) {
                    yardWorld.getBlockAt(x, y, z).setBlockData(dockData);
                } else {
                    yardWorld.getBlockAt(x, y, z).setBlockData(fillData);
                }
            }
        }
    }
}
