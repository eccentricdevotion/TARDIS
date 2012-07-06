/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.eccentric_nz.plugins.TARDIS;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author rob
 */
public class TARDISTimetravel {

    public static void TARDISTimetravel(Player sender) {
        Random rand = new Random();
        int wherex;
        int wherez;
        wherex = rand.nextInt(500);
        wherez = rand.nextInt(500);
        wherex = wherex * 2;
        wherez = wherez * 2;
        wherex = wherex - 500;
        wherez = wherez - 500;
        int y1 = Bukkit.getServer().getWorld("cityworld").getHighestBlockYAt(wherex, wherez) - 1;
        int blockid = Bukkit.getServer().getWorld("cityworld").getBlockTypeIdAt(wherex, y1, wherez);
        int y2 = Bukkit.getServer().getWorld("cityworld").getHighestBlockYAt(wherex, wherez);
        int blockid2 = Bukkit.getServer().getWorld("cityworld").getBlockTypeIdAt(wherex, y2, wherez);
        int y3 = Bukkit.getServer().getWorld("cityworld").getHighestBlockYAt(wherex, wherez) + 1;
        int blockid3 = Bukkit.getServer().getWorld("cityworld").getBlockTypeIdAt(wherex, y3, wherez);
        Bukkit.getServer().getWorld("cityworld").getHighestBlockYAt(wherex, wherez);
        int wherey2;
        wherey2 = Bukkit.getServer().getWorld("cityworld").getHighestBlockYAt(wherex, wherez);
        Location tpmethere2 = new Location(Bukkit.getServer().getWorld("cityworld"), wherex, wherey2, wherez);
        Bukkit.getServer().getWorld("cityworld").getChunkAt(tpmethere2).load();
        Bukkit.getServer().getWorld("cityworld").getChunkAt(tpmethere2).load(true);
        while (!Bukkit.getServer().getWorld("cityworld").getChunkAt(tpmethere2).isLoaded()) {
            Bukkit.getServer().getWorld("cityworld").getChunkAt(tpmethere2).load();
        }
        int Danger;
        Danger = 0;
        if (blockid == 8) {
            sender.sendMessage(ChatColor.RED + "You would have spawned in " + ChatColor.BLUE + "water" + ChatColor.RED + "! Please try again");
            Danger = 1;
        }
        if (blockid == 9) {
            sender.sendMessage(ChatColor.RED + "You would have spawned in " + ChatColor.BLUE + "water" + ChatColor.RED + "! Please try again");
            Danger = 1;
        }
        if (blockid == 10) {
            sender.sendMessage(ChatColor.RED + "You would have spawned in " + ChatColor.GOLD + "lava" + ChatColor.RED + "! Please try again");
            Danger = 1;
        }
        if (blockid == 11) {
            sender.sendMessage(ChatColor.RED + "You would have spawned in " + ChatColor.GOLD + "lava" + ChatColor.RED + "! Please try again");
            Danger = 1;
        }
        if (blockid == 51) {
            sender.sendMessage(ChatColor.RED + "You would have spawned on " + ChatColor.GOLD + "fire" + ChatColor.RED + "! Please try again");
            Danger = 1;
        }
        if (blockid == 81) {
            sender.sendMessage(ChatColor.RED + "You would have spawned on " + ChatColor.GREEN + "cati" + ChatColor.RED + "! Please try again");
            Danger = 1;
        }
        if (Danger == 0) {
            Bukkit.getServer().getWorld("cityworld").getHighestBlockYAt(wherex, wherez);
            int wherey;
            wherey = Bukkit.getServer().getWorld("cityworld").getHighestBlockYAt(wherex, wherez);
            Location tpmethere = new Location(Bukkit.getServer().getWorld("cityworld"), wherex, wherey, wherez);
            Bukkit.getServer().getWorld("cityworld").getChunkAt(tpmethere).load();
            sender.teleport(tpmethere);
        }
    }
}