package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetScreenText;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConsoleTextCommand {

    private final TARDIS plugin;
    private final List<String> SCREEN_SUBS = List.of("forward", "backward", "left", "right");

    public ConsoleTextCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean move(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        if (!SCREEN_SUBS.contains(args[1].toLowerCase())) {
            return false;
        }
        // get tardis player is in
        String uuid = player.getUniqueId().toString();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            return true;
        }
        int id = rst.getTardis_id();
        // get text display
        ResultSetScreenText rsst = new ResultSetScreenText(plugin, id);
        rsst.resultSetAsync(resultSetOccupied -> {
            UUID t = rsst.getUuid();
            Entity entity = plugin.getServer().getEntity(t);
            if (!(entity instanceof TextDisplay textDisplay)) {
                plugin.debug("Entity not a text display");
                return;
            }
            Location location = textDisplay.getLocation();
            double x;
            double z;
            switch (args[1].toLowerCase()) {
                case "forward" -> {
                    x = 0.025d;
                    z = 0.025d;
                }
                case "backward" -> {
                    x = -0.025d;
                    z = -0.025d;
                }
                case "left" -> {
                    x = 0.025d;
                    z = 0;
                }
                case "right" -> {
                    x = 0;
                    z = 0.025d;
                }
                default -> {
                    x = 0;
                    z = 0;
                }
            }
            Location cloned = location.clone().add(x, 0, z);
            textDisplay.teleport(cloned);
        });
        return true;
    }
}
