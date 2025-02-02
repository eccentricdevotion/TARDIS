/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
        float h = (ci == ConsoleInteraction.WORLD) ? 0.15f : 0;
        Location spawn = centre.clone().add(ci.getRelativePosition().getX(), ci.getRelativePosition().getY() + ci.getHeight() + h, ci.getRelativePosition().getZ());
        TextDisplay display = (TextDisplay) centre.getWorld().spawnEntity(spawn, EntityType.TEXT_DISPLAY);
        display.setText(ci.getAlternateName());
        display.setBackgroundColor(Color.BLACK);
        display.setRotation(Location.normalizeYaw(ci.getYaw()), 0.0f);
        display.setTransformation(transformation);
        display.setBillboard(Display.Billboard.FIXED);
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
