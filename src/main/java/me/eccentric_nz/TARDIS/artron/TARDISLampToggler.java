/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisTimeLord;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISLampToggler {

    private final TARDIS plugin;
    private final BlockData lamp;
    private final BlockData sea;

    public TARDISLampToggler(TARDIS plugin) {
        this.plugin = plugin;
        lamp = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(52));
        sea = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(53));
    }

    public void flickSwitch(int id, UUID uuid, boolean on, boolean lantern) {
        // get lamp locations
        HashMap<String, Object> wherel = new HashMap<>();
        wherel.put("tardis_id", id);
        ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, true);
        if (rsl.resultSet()) {
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            boolean use_wool = false;
            if (rsp.resultSet()) {
                // only use player preference if the tardis id of the timelord/companion is the same as the tardis id they are in
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (rs.fromUUID(uuid.toString()) && rs.getTardis_id() == id) {
                    lantern = rsp.isLanternsOn();
                    use_wool = rsp.isWoolLightsOn();
                } else {
                    // also force the use of lanterns if that is the tardis owner's preference
                    ResultSetTardisTimeLord rstl = new ResultSetTardisTimeLord(plugin);
                    if (rstl.fromID(id) && rstl.getUuid() != uuid) {
                        // get tardis owner's preference
                        ResultSetPlayerPrefs rsptl = new ResultSetPlayerPrefs(plugin, rstl.getUuid().toString());
                        if (rsptl.resultSet()) {
                            lantern = rsptl.isLanternsOn();
                            use_wool = rsptl.isWoolLightsOn();
                        }
                    }
                }
            }
            BlockData onlamp = (lantern) ? TARDISConstants.LANTERN : TARDISConstants.LAMP;
            for (Block b : rsl.getData()) {
                while (!b.getChunk().isLoaded()) {
                    b.getChunk().load();
                }
                if (on) {
                    if (b.getType().equals(Material.SEA_LANTERN) || (b.getType().equals(Material.REDSTONE_LAMP))) {
                        BlockData multipleFacing;
                        if (use_wool) {
                            multipleFacing = TARDISConstants.BLACK;
                        } else if (lantern) {
                            multipleFacing = sea;
                        } else {
                            multipleFacing = lamp;
                        }
                        b.setBlockData(multipleFacing);
                    }
                } else if (b.getType().equals(Material.MUSHROOM_STEM) || b.getType().equals(Material.SPONGE) || b.getType().equals(Material.INFESTED_STONE) || b.getType().equals(Material.BLACK_WOOL)) {
                    b.setBlockData(onlamp);
                }
            }
        }
    }
}
