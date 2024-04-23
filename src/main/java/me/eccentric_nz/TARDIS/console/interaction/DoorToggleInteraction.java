package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DoorToggleInteraction {

    private final TARDIS plugin;

    public DoorToggleInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggle(int id, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
        // set custom model data for toggle wool button item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin);
        }
        new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
    }
}
