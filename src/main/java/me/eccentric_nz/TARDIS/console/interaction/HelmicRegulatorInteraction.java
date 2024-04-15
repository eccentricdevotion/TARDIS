package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.HelmicRegulatorModel;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HelmicRegulatorInteraction {

    private final TARDIS plugin;

    public HelmicRegulatorInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void selectWorld(int state, int id, Player player, Interaction interaction) {
        int next = state + 1;
        if (next > 8 || player.isSneaking()) {
            next = 0;
        }
        String which = "OFF";
        if (next > 0) {
            // get world name
            which = getWorldFromState(next);
        }
        // show title
        plugin.getMessenger().announceRepeater(player, which);
        if (which.equals("OFF")) {
            next = 0;
        }
        // save state
        new InteractionStateSaver(plugin).write("HELMIC_REGULATOR", next, id);
        // set custom model data for helmic regulator item display
        UUID model = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (model != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(model);
            new HelmicRegulatorModel().setState(display, next);
        }
    }

    private String getWorldFromState(int state) {
        for (String w : plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
            if (plugin.getPlanetsConfig().getInt("planets." + w + ".helmic_regulator_order") == state) {
                return w;
            }
        }
        return "OFF";
    }
}
