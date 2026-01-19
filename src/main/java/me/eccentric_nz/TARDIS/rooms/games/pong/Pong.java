package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeData;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeTracker;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Pong {

    private final TARDIS plugin;

    public Pong(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void startGame(Player player) {
        // teleport to ARCADE room
        Location arcade = getRoom(player);
        ArcadeTracker.PLAYERS.put(player.getUniqueId(), new ArcadeData(player.getLocation(), player.getAllowFlight(), null));
        player.setAllowFlight(true);
        player.teleport(arcade);
    }

    private Location getRoom(Player player) {
        // get the id of the TARDIS the player is in
        int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
        if (id != -1) {
            // get game record
            ResultSetGames rsg = new ResultSetGames(plugin);
            if (rsg.fromId(id)) {
                // get the player location
                String playerLocation = rsg.getPlayerLocation();
                Location first = TARDISStaticLocationGetters.getLocationFromBukkitString(playerLocation);
                first.setY(180d);
                return first;
            }
        }
        return null;
    }
}
