package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInteraction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class ConsoleInteractionListener implements Listener {

    private final TARDIS plugin;

    public ConsoleInteractionListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConsoleInteractionClick(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Interaction interaction) {
            if (interaction.getPersistentDataContainer().has(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID())) {
                UUID uuid = interaction.getPersistentDataContainer().get(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID());
                ResultSetInteraction rsi = new ResultSetInteraction(plugin, uuid);
                if (rsi.resultSet()) {
                    switch (rsi.getControl()) {
                        case SCREEN_LEFT, SCREEN_RIGHT -> {
                            plugin.getMessenger().announceRepeater(event.getPlayer(), rsi.getControl().getAlternateName());
                            boolean coords = rsi.getControl() == ConsoleInteraction.SCREEN_RIGHT;
                            // get the text display
                            TextDisplay display = getTextDisplay(interaction.getLocation(), coords);
                            if (display != null) {
                                display.setRotation(Location.normalizeYaw(300), -10f);
                                new ControlMonitor(plugin).update(3, display.getUniqueId(), coords);
                            }
                        }
                        default -> plugin.getMessenger().announceRepeater(event.getPlayer(), rsi.getControl().getAlternateName());
                    }
                }
            }
        }
    }

    private TextDisplay getTextDisplay(Location location, boolean coords) {
        TextDisplay textDisplay = null;
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1.0d, 1.0d, 1.0d, (e) -> e.getType() == EntityType.TEXT_DISPLAY)) {
            textDisplay = (TextDisplay) entity;
            break;
        }
        if (textDisplay == null) {
            Location adjusted = location.clone();
            Vector vector = coords ? new Vector(0.0d, 0.5d, 0.35d) : new Vector(0.32d, 0.5d, -0.225d);
            adjusted.add(vector);
            textDisplay = (TextDisplay) location.getWorld().spawnEntity(adjusted, EntityType.TEXT_DISPLAY);
        }
        return textDisplay;
    }
}
