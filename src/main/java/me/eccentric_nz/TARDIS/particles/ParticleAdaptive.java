package me.eccentric_nz.TARDIS.particles;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAdaptive;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import org.bukkit.Location;
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
            Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
            Material material = tcc.getChameleonBlock(block, null);
            return material.createBlockData();
        }
        return null;
    }
}
