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
        if (TARDISPermission.hasPermission(player, "tardis.architectural.blueprint")) {
            player.openInventory(new ArchitecturalBlueprintsInventory(plugin, player).getInventory());
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
        }
    }
}
