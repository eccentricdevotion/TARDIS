package me.eccentric_nz.TARDIS.rooms.architectural;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

public class ArchitecturalReconfiguration {

    private final TARDIS plugin;

    public ArchitecturalReconfiguration(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        plugin.debug("attempt opening architectural reconfiguration");
        if (TARDISPermission.hasPermission(player, "tardis.architectural.blueprint")) {
            plugin.debug("has permission to open architectural reconfiguration");
            player.openInventory(new ArchitecturalBlueprintsInventory(plugin, player).getInventory());
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
        }
    }
}
