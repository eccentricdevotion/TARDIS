package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class TARDISInteractionDoorUpdater {

    private final TARDIS plugin;

    public TARDISInteractionDoorUpdater(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean addIds() {
        HashMap<String, Object> where = new HashMap<>();
        where.put("door_type", 1);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, true);
        if (rsd.resultSet()) {
            for (HashMap<String, String> map : rsd.getData()) {
                String l = map.get("door_location");
                String i = map.get("tardis_id");
                Location location = TARDISStaticLocationGetters.getLocationFromDB(l);
                if (location != null) {
                    while (!location.getChunk().isLoaded()) {
                        location.getChunk().load();
                    }
                    Interaction interaction = TARDISDisplayItemUtils.getInteraction(location);
                    if (interaction != null) {
                        int id = TARDISNumberParsers.parseInt(i);
                        if (id != -1) {
                            interaction.getPersistentDataContainer().set(plugin.getTardisIdKey(), PersistentDataType.INTEGER, id);
                        }
                    }
                }
            }
        }
        return true;
    }
}
