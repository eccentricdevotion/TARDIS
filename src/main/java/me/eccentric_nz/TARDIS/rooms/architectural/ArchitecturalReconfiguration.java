package me.eccentric_nz.TARDIS.rooms.architectural;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

public class ArchitecturalReconfiguration {

    private final TARDIS plugin;

    public ArchitecturalReconfiguration(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(int id, Player player) {
        player.openInventory(new ArchitecturalBlueprintsInventory(plugin, player).getInventory());
    }
}
