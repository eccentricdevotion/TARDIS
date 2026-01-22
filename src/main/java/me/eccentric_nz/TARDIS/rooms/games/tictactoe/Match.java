package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Random;

public class Match {

    private final Material firstPlayerItem = Material.CYAN_GLAZED_TERRACOTTA;
    private final Material tardisPlayerItem = Material.RED_GLAZED_TERRACOTTA;
    private final int[] field;
    private final Player firstPlayer;
    private final Player tardisPlayer;
    private final boolean broadcastWinEnabled;
    private final boolean broadcastDrawEnabled;
    public TARDIS plugin;
    private boolean matchFinished;
    private Inventory gui;
    private TicTacToePlayer whoseTurn;

    public Match(Player firstPlayer, Player tardisPlayer, TARDIS plugin) {
        this.firstPlayer = firstPlayer;
        this.tardisPlayer = tardisPlayer;
        this.plugin = plugin;
        // Getting additional information from the config
        this.broadcastWinEnabled = plugin.getConfig().getBoolean("misc.broadcastWinEnabled");
        this.broadcastDrawEnabled = plugin.getConfig().getBoolean("misc.broadcastDrawEnabled");
        // Initializing the field
        field = new int[9];
        Arrays.fill(field, -1);
        // Randomly choosing who will have the first turn (except TtcPlayer.NONE)
        whoseTurn = TicTacToePlayer.values()[new Random().nextInt(TicTacToePlayer.values().length - 1) + 1];
        updateGui();
    }

    public void move(TicTacToePlayer player, int slotIndex) {
        // Return if the slot is not in bounds of the field (3 x 3)
        if (slotIndex < 0 || slotIndex > 8) {
            return;
        }
        // Return if player is not allowed to make a turn
        if (whoseTurn != player) {
            return;
        }
        // Return if provided slot was already taken
        if (field[slotIndex] != -1) {
            return;
        }
        // Setting the value of a field cell with corresponding player index
        field[slotIndex] = player.ordinal();
        MatchState state = getFieldState();
        if (state == MatchState.PLAYER_WON) {
            whoseTurn = TicTacToePlayer.NONE;
            win(TicTacToePlayer.FIRST);
            return;
        }
        if (state == MatchState.TARDIS_WON) {
            whoseTurn = TicTacToePlayer.NONE;
            win(TicTacToePlayer.SECOND);
            return;
        }
        if (state == MatchState.DRAW) {
            whoseTurn = TicTacToePlayer.NONE;
            draw();
            return;
        }
        // Choosing who will have the next move
        if (whoseTurn == TicTacToePlayer.FIRST) {
            whoseTurn = TicTacToePlayer.SECOND;
        } else {
            if (whoseTurn == TicTacToePlayer.SECOND) whoseTurn = TicTacToePlayer.FIRST;
        }
        updateGui();
    }

    // Simplified overload of updateGui()
    // Updates the gui with current state of a game
    public void updateGui() {
        updateGui(null);
    }

    // Updates the gui with current state of a game
    public void updateGui(String customTitle) {
        // Setting default window title, if customTitle is null
        if (customTitle == null) {
            Player playerWhoseMove;
            if (whoseTurn == TicTacToePlayer.FIRST) {
                playerWhoseMove = firstPlayer;
            } else {
                playerWhoseMove = tardisPlayer;
            }
            customTitle = String.format("%s's turn!", playerWhoseMove.getDisplayName());
        }
        // Creating new inventory with updated state
//        gui = Bukkit.createInventory(new TicTacToeInventory(), InventoryType.DROPPER, customTitle);
        gui = Bukkit.createInventory(new NoughtsAndCrossesInventory(plugin), InventoryType.DROPPER, Component.text("Tic Tac Toe", NamedTextColor.DARK_RED));
        // Filling up the inventory with current items
        for (int i = 0; i < field.length; i++) {
            if (field[i] == -1) {
                continue;
            }
            if (TicTacToePlayer.values()[field[i]] == TicTacToePlayer.FIRST) {
                gui.setItem(i, new ItemStack(firstPlayerItem));
            } else if (TicTacToePlayer.values()[field[i]] == TicTacToePlayer.SECOND) {
                gui.setItem(i, new ItemStack(tardisPlayerItem));
            }
        }
        // Opening the updated inventory to players
        firstPlayer.openInventory(gui);
        tardisPlayer.openInventory(gui);
    }

    public Inventory getGui() {
        return gui;
    }

    public void win(TicTacToePlayer player) {
        this.matchFinished = true;
        Player winner;
        Player looser;
        if (player == TicTacToePlayer.FIRST) {
            winner = firstPlayer;
            looser = tardisPlayer;
        } else {
            winner = tardisPlayer;
            looser = firstPlayer;
        }
        if (this.broadcastWinEnabled) {
            String winBroadcastMessage = String.format("&#318CE7%s won against %s!", winner.getDisplayName(), looser.getDisplayName());
            Bukkit.broadcastMessage(winBroadcastMessage);
        }
        String winMessage = String.format("&#8EE53FYou won the game against &n%s&#8EE53F!", looser.getDisplayName());
        String loseMessage = String.format("&#CF1020You lost against &n%s&#CF1020!", winner.getDisplayName());
        winner.sendMessage(winMessage);
        looser.sendMessage(loseMessage);
        updateGui(String.format("%s won!", winner.getDisplayName()));
        new EndGameTask(this).runTaskLater(this.plugin, 30);
    }

    public void draw() {
        String drawMessage = "&#FECB00Game ended draw!";
        firstPlayer.sendMessage(drawMessage);
        tardisPlayer.sendMessage(drawMessage);
        updateGui("Draw!");
        if (this.broadcastDrawEnabled) {
            String winBroadcastMessage = String.format("&#318CE7%s and %s have a draw!", firstPlayer.getDisplayName(), tardisPlayer.getDisplayName());
            Bukkit.broadcastMessage(winBroadcastMessage);
        }
        this.end();
    }

    public void end() {
        Matches.matches.remove(this);
        firstPlayer.closeInventory();
        tardisPlayer.closeInventory();
    }

    public void gameAbortedByPlayer(TicTacToePlayer player) {
        // Don't do anything when match is already finished
        if (this.matchFinished) {
            return;
        }
        String winMessage = "&#318CE7Your opponent aborted the game! &#8EE53F&nYou are the winner!";
        String loseMessage = "&#CF1020You've aborted the game! You lost.";
        if (player == TicTacToePlayer.FIRST) {
            firstPlayer.sendMessage(loseMessage);
            tardisPlayer.sendMessage(winMessage);
        } else {
            firstPlayer.sendMessage(winMessage);
            tardisPlayer.sendMessage(loseMessage);
        }
        this.end();
    }

    public boolean containsPlayer(Player player) {
        return firstPlayer == player || tardisPlayer == player;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return tardisPlayer;
    }

    private MatchState getFieldState() {
        int[][] twoDField = new int[3][3];
        // Converting field to 2D-array
        int index = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                twoDField[x][y] = field[index++];
            }
        }
        // Checking verticals
        for (int y = 0; y < 3; y++) {
            if (twoDField[0][y] == -1) {
                continue;
            }
            if (twoDField[0][y] == twoDField[1][y] && twoDField[1][y] == twoDField[2][y]) {
                TicTacToePlayer winner = TicTacToePlayer.values()[twoDField[0][y]];
                if (winner == TicTacToePlayer.FIRST) return MatchState.PLAYER_WON;
                if (winner == TicTacToePlayer.SECOND) return MatchState.TARDIS_WON;
            }
        }
        // Checking horizontals
        for (int x = 0; x < 3; x++) {
            if (twoDField[x][0] == -1) {
                continue;
            }
            if (twoDField[x][0] == twoDField[x][1] && twoDField[x][1] == twoDField[x][2]) {
                TicTacToePlayer winner = TicTacToePlayer.values()[twoDField[x][0]];
                if (winner == TicTacToePlayer.FIRST) {
                    return MatchState.PLAYER_WON;
                }
                if (winner == TicTacToePlayer.SECOND) {
                    return MatchState.TARDIS_WON;
                }
            }
        }
        // Checking diagonals
        if (twoDField[0][0] == twoDField[1][1] && twoDField[1][1] == twoDField[2][2] && twoDField[0][0] != -1) {
            TicTacToePlayer winner = TicTacToePlayer.values()[twoDField[0][0]];
            if (winner == TicTacToePlayer.FIRST) {
                return MatchState.PLAYER_WON;
            }
            if (winner == TicTacToePlayer.SECOND) {
                return MatchState.TARDIS_WON;
            }
        }
        if (twoDField[0][2] == twoDField[1][1] && twoDField[1][1] == twoDField[2][0] && twoDField[0][2] != -1) {
            TicTacToePlayer winner = TicTacToePlayer.values()[twoDField[0][2]];
            if (winner == TicTacToePlayer.FIRST) {
                return MatchState.PLAYER_WON;
            }
            if (winner == TicTacToePlayer.SECOND) {
                return MatchState.TARDIS_WON;
            }
        }
        // Checking if the field is full
        int emptyFields = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (twoDField[x][y] == -1) {
                    emptyFields++;
                }
            }
        }
        return emptyFields == 0 ? MatchState.DRAW : MatchState.IN_PROGRESS;
    }
}
