package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISZeroRoomEnterEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISZeroRoomExitEvent;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ZeroRoomAction {

    private final TARDIS plugin;

    public ZeroRoomAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void doEntry(int level, Player player, String z, int id) {
        // enter zero room
        int zero_amount = plugin.getArtronConfig().getInt("zero");
        if (level < zero_amount) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ZERO_ENERGY");
            return;
        }
        Location zero = TARDISStaticLocationGetters.getLocationFromDB(z);
        if (zero != null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ZERO_READY");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new TARDISExteriorRenderer(plugin).transmat(player, COMPASS.SOUTH, zero);
                plugin.getPM().callEvent(new TARDISZeroRoomEnterEvent(player, id));
            }, 20L);
            plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
            HashMap<String, Object> wherez = new HashMap<>();
            wherez.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", -zero_amount, wherez, player);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_ZERO");
        }
    }

    public void doExit(Player player, int id) {
        // exit zero room
        plugin.getTrackerKeeper().getZeroRoomOccupants().remove(player.getUniqueId());
        plugin.getGeneralKeeper().getRendererListener().transmat(player);
        plugin.getPM().callEvent(new TARDISZeroRoomExitEvent(player, id));
    }
}
