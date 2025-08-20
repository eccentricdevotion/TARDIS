/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

import java.util.HashMap;

public class PlotArea implements Runnable {

    private final TARDIS plugin;
    private final World world;

    public PlotArea(TARDIS plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    @Override
    public void run() {
        // check if an area exists
        HashMap<String, Object> where = new HashMap<>();
        where.put("area_name", "Plots");
        where.put("world", world.getName());
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false, true);
        if (rsa.resultSet()) {
            return;
        }
        int size = plugin.getGeneratorConfig().getInt("plot.size");
        int max = (16 * size) - 2;
        int y = world.getMinHeight() + 64;
        HashMap<String, Object> set = new HashMap<>();
        set.put("area_name", "Plots");
        set.put("world", world.getName());
        set.put("minx", 6);
        set.put("minz", 6);
        set.put("maxx", max);
        set.put("maxz", max);
        set.put("y", y + 1);
        plugin.getQueryFactory().doInsert("areas", set);
        // style the plot
        for (int x = 6; x <= max; x++) {
            for (int z = 6; z <= max; z++) {
                if ((x - 2) % 5 == 0 && (z - 2) % 5 == 0) {
                    world.getBlockAt(x, y, z).setType(Material.RESIN_BRICKS);
                } else {
                    world.getBlockAt(x, y, z).setType(Material.STRIPPED_BIRCH_WOOD);
                }
            }
        }
        // claim the plot
        HashMap<String, Object> setp = new HashMap<>();
        setp.put("uuid", TARDISConstants.UUID_ZERO.toString());
        setp.put("world", world.getName());
        setp.put("chunk_x", 0);
        setp.put("chunk_z", 0);
        setp.put("size", size);
        setp.put("name", "TARDIS Landing");
        plugin.getQueryFactory().doInsert("plots", setp);
        // update the sign
        Block block = world.getBlockAt(5, y + 2, 5);
        if (Tag.SIGNS.isTagged(block.getType())) {
            Sign sign = (Sign) block.getState();
            SignSide front = sign.getSide(Side.FRONT);
            // server name
            front.line(0, Component.text("TARDIS"));
            front.line(1, Component.text("Landing Area"));
            front.line(3, Component.text("X:0, Z:0"));
            SignSide back = sign.getSide(Side.BACK);
            back.line(0, Component.text("TARDIS"));
            back.line(1, Component.text("Landing Area"));
            back.line(3, Component.text("X:0, Z:0"));
            sign.setWaxed(true);
            sign.update(true);
        }
    }
}
