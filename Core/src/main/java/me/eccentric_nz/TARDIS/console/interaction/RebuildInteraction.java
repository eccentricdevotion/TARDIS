package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RebuildInteraction {

    private final TARDIS plugin;

    public RebuildInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
        // set custom model data for rebuild button item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin, ConsoleInteraction.REBUILD);
        }
        new TARDISRebuildCommand(plugin).rebuildPreset(player);
    }
}
