package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISDirectionCommand;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DirectionAction {

    private final TARDIS plugin;

    public DirectionAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public String rotate(int id, Player player) {
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return "";
        }
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            String direction = rsc.getDirection().toString();
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false, 0);
            if (rst.resultSet()) {
                if (!rst.getTardis().getPreset().usesArmourStand()) {
                    // skip the angled rotations
                    switch (rsc.getDirection()) {
                        case SOUTH -> direction = "SOUTH_WEST";
                        case EAST -> direction = "SOUTH_EAST";
                        case NORTH -> direction = "NORTH_EAST";
                        case WEST -> direction = "NORTH_WEST";
                        default -> {
                        }
                    }
                }
                int ordinal = COMPASS.valueOf(direction).ordinal() + 1;
                if (ordinal == 8) {
                    ordinal = 0;
                }
                direction = COMPASS.values()[ordinal].toString();
            }
            String[] args = new String[]{"direction", direction};
            new TARDISDirectionCommand(plugin).changeDirection(player, args);
            return direction;
        }
        return "";
    }
}
