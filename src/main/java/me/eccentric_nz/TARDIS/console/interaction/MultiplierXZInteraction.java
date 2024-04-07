package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import org.bukkit.entity.Player;

public class MultiplierXZInteraction {

    private final TARDIS plugin;

    public MultiplierXZInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setRange(ConsoleInteraction ci, int state, int id, Player player) {
        int next = state + 1;
        if (next > 4) {
            next = 1;
        }
        new InteractionStateSaver(plugin).write(ci.toString(), next, id);
        plugin.getMessenger().announceRepeater(player, ci + ": " + next);
    }
}
