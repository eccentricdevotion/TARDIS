package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.loader.ChunkLoaderInventory;
import org.bukkit.entity.Player;

public class ChunkLoaderAction {

    private final TARDIS plugin;

    public ChunkLoaderAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        if (plugin.getConfig().getBoolean("allow.chunk_tickets")) {
            player.openInventory(new ChunkLoaderInventory(plugin).getInventory());
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LOADER_DISABLED");
        }
    }
}
