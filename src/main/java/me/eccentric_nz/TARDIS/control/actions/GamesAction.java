package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rooms.games.GamesInventory;
import org.bukkit.entity.Player;

public class GamesAction {

    private final TARDIS plugin;

    public GamesAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        player.openInventory(new GamesInventory(plugin).getInventory());
    }
}
