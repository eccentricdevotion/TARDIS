package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsoleLabel;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class LabelAction {

    private final TARDIS plugin;
    private final Transformation transformation = new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, new Vector3f(0.125f, 0.125f, 0.125f), TARDISConstants.AXIS_ANGLE_ZERO);

    public LabelAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggle(int id, boolean on) {
        // get the centre block
        ResultSetConsoleLabel rs = new ResultSetConsoleLabel(plugin, id);
        if (!rs.resultSet()) {
            return;
        }
        Location centre = rs.getLocation();
        if (centre == null) {
            return;
        }
        if (on) {
            for (ConsoleInteraction ci : ConsoleInteraction.values()) {
                if (ci == ConsoleInteraction.SCREEN_LEFT || ci == ConsoleInteraction.SCREEN_RIGHT) {
                    continue;
                }
                // spawn a text display
                spawnTextDisplay(centre, ci);
            }
        } else {
            // off
            removeTextDisplay(centre);
        }
    }

    private void spawnTextDisplay(Location centre, ConsoleInteraction ci) {
        double half = ci.getWidth() / 2.0d;
        Location spawn = centre.clone().add(ci.getRelativePosition().getX() - half, ci.getRelativePosition().getY() + ci.getHeight(), ci.getRelativePosition().getZ() - half);
        TextDisplay display = (TextDisplay) centre.getWorld().spawnEntity(spawn, EntityType.TEXT_DISPLAY);
        display.setText(ci.getAlternateName());
        display.setBackgroundColor(Color.BLACK);
        display.setRotation(Location.normalizeYaw(ci.getYaw()), 0.0f);
        display.setTransformation(transformation);
        display.setBillboard(Display.Billboard.FIXED);
        display.setSeeThrough(true);
    }

    private void removeTextDisplay(Location centre) {
        Location spawn = centre.clone().add(0.5f, 0, 0.5f);
        for (Entity e : spawn.getWorld().getNearbyEntities(spawn, 3.5, 2, 3.5, (t) -> t.getType() == EntityType.TEXT_DISPLAY)) {
            if (e instanceof TextDisplay && !e.getPersistentDataContainer().has(plugin.getInteractionUuidKey(), PersistentDataType.BOOLEAN)) {
                e.remove();
            }
        }
    }
}
