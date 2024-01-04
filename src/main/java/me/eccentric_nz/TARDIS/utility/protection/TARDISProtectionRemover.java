package me.eccentric_nz.TARDIS.utility.protection;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;

import java.util.HashMap;

public class TARDISProtectionRemover {

    private final TARDIS plugin;

    public TARDISProtectionRemover(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void cleanInteriorBlocks(int id) {
        // get and remove block protection
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("police_box", 0);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, true);
        if (rsb.resultSet()) {
            for (ReplacedBlock b : rsb.getData()) {
                plugin.getGeneralKeeper().getProtectBlockMap().remove(b.getStrLocation());
            }
        }
        // remove database entries
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("tardis_id", id);
        wherep.put("police_box", 0);
        plugin.getQueryFactory().doDelete("blocks", wherep);
    }
}
