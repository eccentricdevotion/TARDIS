package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.loader.ChunkLoaderInventory;
import org.bukkit.entity.Player;

public class ChunkLoaderAction {

    private final TARDIS plugin;

    public ChunkLoaderAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        if (!TARDISPermission.hasPermission(player, "tardis.chunk_tickets")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return;
        }
        if (plugin.getConfig().getBoolean("allow.chunk_tickets")) {
            player.openInventory(new ChunkLoaderInventory(plugin).getInventory());
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LOADER_DISABLED");
        }
    }
}
