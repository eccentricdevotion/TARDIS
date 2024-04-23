package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.ArtronAbandoned;
import me.eccentric_nz.TARDIS.artron.ArtronChargeAction;
import me.eccentric_nz.TARDIS.artron.ArtronInitAction;
import me.eccentric_nz.TARDIS.artron.ArtronTransferAction;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ArtronRightClick {

    private final TARDIS plugin;

    public ArtronRightClick(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, Player player, Location location) {
        // get tardis data
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.getPreset().equals(ChameleonPreset.JUNK)) {
                return;
            }
            boolean abandoned = tardis.isAbandoned();
            int current_level = tardis.getArtronLevel();
            boolean init = tardis.isTardisInit();
            boolean lights = tardis.isLightsOn();
            Material item = player.getInventory().getItemInMainHand().getType();
            Material full = Material.valueOf(plugin.getArtronConfig().getString("full_charge_item"));
            Material cell = plugin.getFigura().getShapedRecipes().get("Artron Storage Cell").getResult().getType();
            // determine key item
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            String key;
            boolean hasPrefs = false;
            if (rsp.resultSet()) {
                hasPrefs = true;
                key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
            } else {
                key = plugin.getConfig().getString("preferences.key");
            }
            if (item.equals(full) || item.equals(cell)) {
                if (!init) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                    return;
                }
                new ArtronChargeAction(plugin).add(player, item, full, location, current_level, id);
            } else if (item.equals(Material.getMaterial(key))) {
                // has the TARDIS been initialised?
                if (!init) {
                    new ArtronInitAction(plugin).powerUp(location, tardis, player, id);
                } else { // toggle power
                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                        boolean pu = true;
                        if (abandoned) {
                            // transfer ownership to the player who clicked
                            pu = new ArtronAbandoned(plugin).claim(player, id, location, tardis);
                        }
                        if (pu) {
                            new TARDISPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPoweredOn(), tardis.isHidden(), lights, player.getLocation(), current_level, tardis.getSchematic().getLights()).clickButton();
                        }
                    }
                }
            } else if (player.isSneaking()) {
                if (!init) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                    return;
                }
                new ArtronTransferAction(plugin).add(current_level, rsp.getArtronLevel(), player, id, hasPrefs);
            } else {
                if (!init) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                    return;
                }
                // just tell us how much energy we have
                plugin.getMessenger().sendArtron(player, id, 0);
            }
        }
    }
}
