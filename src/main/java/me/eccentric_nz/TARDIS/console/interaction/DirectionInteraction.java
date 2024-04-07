package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.actions.DirectionAction;
import org.bukkit.entity.Player;

public class DirectionInteraction {

    private final TARDIS plugin;

    public DirectionInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void rotate(int id, Player player) {
        String direction = new DirectionAction(plugin).rotate(id, player);
        plugin.getMessenger().announceRepeater(player, direction);
        // TODO set custom model data for direction item display
    }
}
