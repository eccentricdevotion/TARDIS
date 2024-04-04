package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISScanner;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ScannerIntraction {

    private final TARDIS plugin;

    public ScannerIntraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, Player player) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            new TARDISScanner(plugin).scan(id, player, rs.getTardis().getRenderer(), rs.getTardis().getArtronLevel());
        }
    }
}
