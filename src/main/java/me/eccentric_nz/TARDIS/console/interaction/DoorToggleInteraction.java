package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import org.bukkit.entity.Player;

public class DoorToggleInteraction {

    private final TARDIS plugin;

    public DoorToggleInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggle(int id, Player player) {
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
        new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
    }
}
