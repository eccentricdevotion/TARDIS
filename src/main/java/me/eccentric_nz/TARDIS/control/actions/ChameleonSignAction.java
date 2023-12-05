package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISChameleonControl;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.floodgate.FloodgateChameleonCircuitForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChameleonSignAction {

    private final TARDIS plugin;

    public ChameleonSignAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, Tardis tardis, int id) {
        UUID playerUUID = player.getUniqueId();
        if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
            new FloodgateChameleonCircuitForm(plugin, playerUUID, id, tardis.getPreset()).send();
        } else {
            new TARDISChameleonControl(plugin).openGUI(player, id, tardis.getAdaption(), tardis.getPreset(), tardis.getItemPreset());
        }
    }
}
