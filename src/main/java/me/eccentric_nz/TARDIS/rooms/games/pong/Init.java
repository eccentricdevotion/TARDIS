package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Init {

    private final TARDIS plugin;

    public Init(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void startGame(Player player) {
        Pong pong = new Pong(plugin, player);
        Bukkit.getPluginManager().registerEvents(pong, plugin);
    }
}
