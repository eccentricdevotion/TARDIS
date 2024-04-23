package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.console.models.WXYZModel;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MultiplierXZInteraction {

    private final TARDIS plugin;

    public MultiplierXZInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setRange(ConsoleInteraction ci, int state, Interaction interaction, int id, Player player) {
        int next = state + 1;
        if (next > 4) {
            next = 1;
        }
        // set custom model data for lamp level switch item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            int which = switch (ci) {
                case X -> 3;
                case Z -> 4;
                default -> 2;
            };
            new WXYZModel().setState(display, plugin, which);
        }
        new InteractionStateSaver(plugin).write(ci.toString(), next, id);
        plugin.getMessenger().announceRepeater(player, ci + ": " + next);
    }
}
