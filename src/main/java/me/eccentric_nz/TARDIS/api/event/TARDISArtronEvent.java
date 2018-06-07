package me.eccentric_nz.TARDIS.api.event;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardisArtron;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TARDISArtronEvent extends Event {

    private final Player player;
    private final int amount;
    private final int tardis_id;
    private int level = 0;
    private static final HandlerList HANDLERS = new HandlerList();

    public TARDISArtronEvent(Player player, int amount, int tardis_id) {
        this.player = player;
        this.amount = amount;
        this.tardis_id = tardis_id;
        ResultSetTardisArtron rs = new ResultSetTardisArtron(TARDIS.plugin);
        if (rs.fromID(this.tardis_id)) {
            level = rs.getArtron_level();
        }
    }

    /**
     * Returns the player involved in this event.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the amount of energy involved in this event. This could be a positive or negative amount.
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the TARDIS id involved in this event.
     *
     * @return the TARDIS id
     */
    public int getTardis_id() {
        return tardis_id;
    }

    /**
     * Returns the Artron Level after the amount has been added / subtracted.
     *
     * @return the player
     */
    public int getLevel() {
        return level;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
