package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.desktop.TARDISRepair;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SudoRepair {

    private final TARDIS plugin;
    private final UUID uuid;
    private final boolean clean;

    public SudoRepair(TARDIS plugin, UUID uuid, boolean clean) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.clean = clean;
    }

    public boolean repair() {
        Player player = plugin.getServer().getPlayer(uuid);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, "NO_TARDIS");
            return false;
        }
        Tardis tardis = rs.getTardis();
        // get player's current console
        SCHEMATIC current_console = tardis.getSchematic();
        int level = tardis.getArtron_level();
        TARDISUpgradeData tud = new TARDISUpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
        TARDISRepair tr = new TARDISRepair(plugin, player);
        tr.restore(clean);
        return true;
    }
}
