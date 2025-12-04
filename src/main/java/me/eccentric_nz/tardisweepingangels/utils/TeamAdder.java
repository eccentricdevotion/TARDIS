package me.eccentric_nz.tardisweepingangels.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamAdder {

    public static void joinTeam(Entity entity) {
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        Team twaGolems = board.getTeam("TWA_Golems") == null ? board.registerNewTeam("TWA_Golems") : board.getTeam("TWA_Golems");
        twaGolems.addEntity(entity);
    }
}
