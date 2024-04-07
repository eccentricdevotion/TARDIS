package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import org.bukkit.entity.Player;

public class HelmicRegulatorInteraction {

    private final TARDIS plugin;

    public HelmicRegulatorInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void selectWorld(int state, int id, Player player) {
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
        // TODO set custom model data for helmic regulator item display
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
