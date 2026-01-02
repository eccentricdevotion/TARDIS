/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.particles;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAdaptive;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import java.util.UUID;

public class ParticleAdaptive {

    public static Pair<Boolean, Integer> isAdaptive(TARDIS plugin, UUID uuid) {
        // get player's TARDIS id
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(uuid.toString())) {
            int id = rst.getTardisId();
            ResultSetAdaptive rs = new ResultSetAdaptive(plugin);
            if (rs.fromID(id)) {
                return new Pair<>(rs.getAdaption() > 0, id);
            }
        }
        return new Pair<>(false, -1);
    }

    public static BlockData getAdaptiveData(TARDIS plugin, int id) {
        // get current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            Block block = rsc.getCurrent().location().getBlock().getRelative(BlockFace.DOWN);
            TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
            Material material = tcc.getChameleonBlock(block, null);
            return material.createBlockData();
        }
        return null;
    }
}
