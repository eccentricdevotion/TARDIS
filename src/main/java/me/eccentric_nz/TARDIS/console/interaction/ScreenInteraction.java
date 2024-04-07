package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ControlMonitor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;

public class ScreenInteraction {

    private final TARDIS plugin;

    public ScreenInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void display(int id, Location location, boolean coords) {
        // TODO if shift-click change display else open Control Menu GUI
        // get the text display
        TextDisplay display = getTextDisplay(location, coords);
        if (display != null) {
            display.setRotation(Location.normalizeYaw(300), -10f);
            new ControlMonitor(plugin).update(id, display.getUniqueId(), coords);
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
