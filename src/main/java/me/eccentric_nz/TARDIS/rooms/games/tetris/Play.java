package me.eccentric_nz.TARDIS.rooms.games.tetris;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Play {

    private final TARDIS plugin;

    public Play(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void startGame(Player p, int  startLevel) {
        Game game = new Game(plugin, p, startLevel);
        Bukkit.getPluginManager().registerEvents(game, plugin);
    }
}
