package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class TicTacToeListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof NoughtsAndCrossesInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        Match match = null;
        for (int i = 0; i < Matches.matches.size(); i++) {
            Match ttcMatch = Matches.matches.get(i);
            if (!ttcMatch.getGui().equals(inventory)) {
                continue;
            }
            if (!ttcMatch.containsPlayer(player)) {
                continue;
            }
            match = ttcMatch;
        }
        // Preventing further execution if event was produced not in TicTacToe game inventory
        if (match == null) {
            return;
        }
        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();
        int slot = event.getSlot();
        // Sending player's move to TicTacToe match
        if (player == firstPlayer) {
            match.move(TicTacToePlayer.FIRST, slot);
        } else if (player == secondPlayer) {
            match.move(TicTacToePlayer.SECOND, slot);
        }
        // Preventing a player from taking items from the game inventory
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        for (int i = 0; i < Matches.matches.size(); i++) {
            Match match = Matches.matches.get(i);
            // Determining if the closed inventory was exactly TicTacToe game inventory
            if (!match.getGui().equals(inventory)) {
                continue;
            }
            if (!match.containsPlayer(player)) {
                continue;
            }
            Player firstPlayer = match.getFirstPlayer();
            Player secondPlayer = match.getSecondPlayer();
            // Aborting the game
            if (player == firstPlayer) {
                match.gameAbortedByPlayer(TicTacToePlayer.FIRST);
            } else if (player == secondPlayer) {
                match.gameAbortedByPlayer(TicTacToePlayer.SECOND);
            }
            break;
        }
    }
}
