package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.DirectionModel;
import me.eccentric_nz.TARDIS.control.actions.DirectionAction;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DirectionInteraction {

    private final TARDIS plugin;

    public DirectionInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void rotate(int id, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
            return;
        }
        String direction = new DirectionAction(plugin).rotate(id, player);
        plugin.getMessenger().announceRepeater(player, direction);
        int state = getState(direction);
        new InteractionStateSaver(plugin).write("DIRECTION", state, id);
        // set custom model data for direction item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new DirectionModel().setState(display, state);
        }
    }

    private int getState(String direction) {
        int state;
        switch (direction) {
            case "NORTH_EAST" -> state = 1;
            case "EAST" -> state = 2;
            case "SOUTH_EAST" -> state = 3;
            case "SOUTH" -> state = 4;
            case "SOUTH_WEST" -> state = 5;
            case "WEST" -> state = 6;
            case "NORTH_WEST" -> state = 7;
            default -> state = 0;
        }
        return state;
    }
}
