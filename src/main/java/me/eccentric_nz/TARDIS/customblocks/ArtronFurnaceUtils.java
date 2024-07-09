package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronPowered;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ArtronFurnaceUtils {

    public static Block find(Block block, TARDIS plugin) {
        for (BlockFace face : TARDIS.plugin.getGeneralKeeper().getBlockFaces()) {
            Block other = block.getRelative(face);
            if (other.getType() == Material.FURNACE && plugin.getTardisHelper().isArtronFurnace(other)) {
                return other;
            }
        }
        return null;
    }

    public static boolean isTARDISPowered(String location, TARDIS plugin) {
        ResultSetArtronPowered rs = new ResultSetArtronPowered(plugin);
        return rs.fromLocation(location).getFirst();
    }

    public static void drain(int id, TARDIS plugin) {
        int drain = plugin.getArtronConfig().getInt("artron_furnace.power_drain");
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        plugin.getQueryFactory().alterEnergyLevel("tardis", -drain, where, null);
    }

    public static void register(String location, Player player, TARDIS plugin) {
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (!rst.resultSet()) {
            plugin.getMessenger().message(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
            return;
        }
        int id = rst.getTardis_id();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        // check they initialised
        if (!tardis.isTardisInit()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
            return;
        }
        // check powered limit
        if (tardis.getFurnaces() >= plugin.getArtronConfig().getInt("artron_furnace.power_limit")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_POWERED_LIMIT");
            return;
        }
        // check there is no capacitor associated with this furnace
        if (isTARDISPowered(location, plugin)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_ALREADY_POWERED", "furnace");
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        set.put("location", location);
        // insert record
        plugin.getQueryFactory().doInsert("artron_powered", set);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_POWERED", "furnace");
    }

    public static void remove(String location, Player player, TARDIS plugin) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("location", location);
        // delete record
        plugin.getQueryFactory().doDelete("artron_powered", where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_UNPOWERED", "furnace");
    }

    public static void removeFromCapacitor(Block block, Player player, TARDIS plugin) {
        ItemDisplay itemDisplay = TARDISDisplayItemUtils.get(block);
        if (itemDisplay != null) {
            TARDISDisplayItem display = TARDISDisplayItemUtils.get(itemDisplay);
            if (display == TARDISDisplayItem.EYE_STORAGE) {
                //  is there an Artron Furnace in the surrounding blocks?
                Block furnace = find(block, plugin);
                if (furnace != null) {
                    remove(furnace.getLocation().toString(), player, plugin);
                }
            }
        }
    }
}
