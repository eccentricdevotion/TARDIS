package me.eccentric_nz.TARDIS.api.event;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISTravelEvent extends TARDISEvent {

    private final TravelType travelType;
    private final int id;

    /**
     * A TARDIS Time Travel event.
     *
     * @param player     the player growing the room
     * @param tardis     the Tardis data object, may be null
     * @param travelType the type of travel that occurred
     * @param id         the TARDIS id
     */
    public TARDISTravelEvent(Player player, Tardis tardis, TravelType travelType, int id) {
        super(player, tardis);
        this.travelType = travelType;
        this.id = id;
        recordTravel();
    }

    public TravelType getTravelType() {
        return travelType;
    }

    public int getId() {
        return id;
    }

    private void recordTravel() {
        HashMap<String, Object> set = new HashMap<>();
        set.put("travel_type", travelType.toString());
        set.put("tardis_id", id);
        set.put("uuid", getPlayer().getUniqueId().toString());
        TARDIS.plugin.getQueryFactory().doInsert("travel_stats", set);
    }
}
