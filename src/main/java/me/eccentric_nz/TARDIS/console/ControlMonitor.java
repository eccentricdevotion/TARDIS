package me.eccentric_nz.TARDIS.console;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsole;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOccupiedScreen;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.UUID;

public class ControlMonitor implements Runnable {

    private final TARDIS plugin;
    private final Transformation transformation = new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, new Vector3f(0.5f, 0.5f, 0.5f), TARDISConstants.AXIS_ANGLE_ZERO);
    private int modulo = 0;

    public ControlMonitor(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ResultSetOccupiedScreen rsoc = new ResultSetOccupiedScreen(plugin);
        rsoc.resultSetAsync(resultSetOccupied -> {
            for (Pair<Integer, UUID> pair : rsoc.getData()) {
                update(pair.getFirst(), pair.getSecond(), modulo % 2 == 0);
            }
        });
        modulo++;
        if (modulo == 2) {
            modulo = 0;
        }
    }

    public void update(int id, UUID uuid, boolean coords) {
        Entity entity = plugin.getServer().getEntity(uuid);
        if (!(entity instanceof TextDisplay textDisplay)) {
            return;
        }
        textDisplay.setTransformation(transformation);
        textDisplay.setBillboard(Display.Billboard.FIXED);
        String text = makeText(id, coords);
        plugin.debug(text);
        textDisplay.setText(text);
    }

    private String makeText(int id, boolean coords) {
        ResultSetConsole rsc = new ResultSetConsole(plugin, id);
        StringBuilder builder = new StringBuilder();
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            builder.append(ChatColor.DARK_PURPLE)
                    .append("Drifting\n")
                    .append("in the\n")
                    .append("time\n")
                    .append("vortex...");
        } else if (coords) {
            rsc.locationAsync((hasResult, resultSetConsole) -> {
                if (hasResult) {
                    String worldName = (resultSetConsole.getWorld() != null) ? TARDISAliasResolver.getWorldAlias(resultSetConsole.getWorld()) : "";
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + resultSetConsole.getWorld() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE) && !worldName.isEmpty()) {
                        worldName = plugin.getMVHelper().getAlias(worldName);
                    }
                    builder.append(ChatColor.DARK_PURPLE)
                            .append(worldName)
                            .append("\n")
                            .append(ChatColor.WHITE)
                            .append(resultSetConsole.getX())
                            .append("\n")
                            .append(resultSetConsole.getY())
                            .append("\n")
                            .append(resultSetConsole.getZ());
                }
            });
        } else {
            // get the artron data
            rsc.artronAsync((hasResult, resultSetConsole) -> {
                if (hasResult) {
                    builder.append(ChatColor.WHITE)
                            .append(plugin.getLanguage().getString("ARTRON_DISPLAY"))
                            .append("\n")
                            .append(ChatColor.AQUA)
                            .append(resultSetConsole.getArtronLevel())
                            .append("\n")
                            .append(ChatColor.WHITE)
                            .append(plugin.getLanguage().getString("CHAM_DISPLAY"))
                            .append("\n");
                    String preset;
                    if (resultSetConsole.getPreset().startsWith("POLICE_BOX_")) {
                        ChatColor colour = TARDISStaticUtils.policeBoxToChatColor(resultSetConsole.getPreset());
                        preset = colour + "POLICE_BOX";
                    } else {
                        preset = ChatColor.BLUE + resultSetConsole.getPreset().replace("ITEM:", "");
                    }
                    builder.append(preset);
                }
            });
        }
        return builder.toString();
    }
}
