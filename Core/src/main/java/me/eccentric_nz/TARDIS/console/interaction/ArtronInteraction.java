package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArtronInteraction {

    private final TARDIS plugin;

    public ArtronInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void show(int id, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        // set custom model data for artron button item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin, ConsoleInteraction.ARTRON);
        }
        new ArtronRightClick(plugin).process(id, player, interaction.getLocation());
    }
}
