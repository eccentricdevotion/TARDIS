package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import org.bukkit.scheduler.BukkitRunnable;

public class EndGameTask extends BukkitRunnable {
    private final Match match;

    public EndGameTask(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        match.end();
    }
}