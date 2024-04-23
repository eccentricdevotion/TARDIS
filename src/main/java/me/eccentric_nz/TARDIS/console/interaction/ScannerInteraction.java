package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import me.eccentric_nz.TARDIS.control.TARDISScanner;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ScannerInteraction {

    private final TARDIS plugin;

    public ScannerInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, Player player, Interaction interaction) {
        // set custom model data for scanner button item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin);
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            new TARDISScanner(plugin).scan(id, player, rs.getTardis().getRenderer(), rs.getTardis().getArtronLevel());
        }
    }
}
