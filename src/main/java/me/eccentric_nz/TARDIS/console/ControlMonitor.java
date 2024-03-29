package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.UUID;

public class ControlMonitor {
    private final TARDIS plugin;
    private final Transformation transformation = new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, new Vector3f(0.5f, 0.5f, 0.5f), TARDISConstants.AXIS_ANGLE_ZERO);

    public ControlMonitor(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void update(int id, UUID uuid, boolean coords) {
        Entity entity = plugin.getServer().getEntity(uuid);
        if (!(entity instanceof TextDisplay textDisplay)) {
            return;
        }
        // line_width or just \n?
        // billboard - vertical
        textDisplay.setText(makeText(id));
    }

    private String makeText(int id) {

        return "";
    }
}
