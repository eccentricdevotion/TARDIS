package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.InteractionResponse;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import org.bukkit.entity.Player;

public class WorldInteraction {

    private final TARDIS plugin;

    public WorldInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void selectWorld(int state, Player player, int id) {
        /*
        THIS => 1,
        NORMAL => 2,
        NETHER => 3,
        THE_END => 4
         */
        int next = state + 1;
        if (next > 4) {
            next = 1;
        }
        new InteractionStateSaver(plugin).write("WORLD", next, id);
        plugin.getMessenger().announceRepeater(player, InteractionResponse.environment.get(next));
    }
}
