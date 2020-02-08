package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.BIND;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

public class BindRemove {

    private final TARDIS plugin;

    public BindRemove(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setClick(BIND bind, Player player) {
        plugin.getTrackerKeeper().getBindRemoval().put(player.getUniqueId(), bind);
        TARDISMessage.send(player, "BIND_REMOVE_CLICK_BLOCK", bind.toString());
        return true;
    }
}
