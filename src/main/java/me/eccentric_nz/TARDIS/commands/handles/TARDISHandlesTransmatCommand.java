package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TARDISHandlesTransmatCommand {

    private final TARDIS plugin;

    public TARDISHandlesTransmatCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * A site-to-site transport is a special type of transport in which an object or person is transported from one site
     * directly to another, with neither site being a transporter platform.
     *
     * @param player The player to transmat
     */
    public void siteToSiteTransport(Player player, Location transmat) {
        Location location = player.getLocation();
        transmat.setPitch(location.getPitch());
        TARDISMessage.handlesSend(player, "TRANSMAT");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.playSound(transmat, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
            player.teleport(transmat);
        }, 10L);
    }
}
