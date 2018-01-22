/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.desktop;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDelavafier {

    private final TARDIS plugin;
    private final UUID uuid;
    int startx, starty = 64, startz;

    public TARDISDelavafier(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void swap() {
        // calculate startx, starty, startz
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                startx = gsl[0];
                startz = gsl[2];
            }
            String[] split = tardis.getChunk().split(":");
            World world = plugin.getServer().getWorld(split[0]);
            for (int level = 2; level < 6; level++) {
                for (int row = 0; row < 32; row++) {
                    for (int col = 0; col < 32; col++) {
                        int x = startx + row;
                        int y = starty + level;
                        int z = startz + col;
                        Block b = world.getBlockAt(x, y, z);
                        Material type = b.getType();
                        if (type.equals(Material.FLOWING_LAVA) || type.equals(Material.LAVA)) {
                            b.setType(Material.ORANGE_TERRACOTTA);
                        }
                        if (type.equals(Material.FLOWING_WATER) || type.equals(Material.WATER)) {
                            b.setType(Material.LIGHT_BLUE_STAINED_GLASS);
                        }
                    }
                }
            }
        }
    }
}
