package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class NoughtsAndCrossesListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, MatchData> matches = new HashMap<>();
    private final int[][] winPos = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {2, 4, 6}, {0, 4, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}};
    private final HashMap<Integer, Integer> slotToIndex = new HashMap<>() {
        {
            put(3, 0);
            put(4, 1);
            put(5, 2);
            put(12, 3);
            put(13, 4);
            put(14, 5);
            put(21, 6);
            put(22, 7);
            put(23, 8);
        }
    };
    private final HashMap<Integer, Integer> indexToSlot = new HashMap<>() {
        {
            put(0, 3);
            put(1, 4);
            put(2, 5);
            put(3, 12);
            put(4, 13);
            put(5, 14);
            put(6, 21);
            put(7, 22);
            put(8, 23);
        }
    };
    long delay = 20;

    public NoughtsAndCrossesListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onTicTacToeClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof NoughtsAndCrossesInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 54) {
            return;
        }
        InventoryView view = event.getView();
        if (view.getItem(slot) == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        MatchData match = matches.getOrDefault(uuid, new MatchData());
        switch (slot) {
            case 3, 4, 5, 12, 13, 14, 21, 22, 23 -> {
                // check game state
                if (match.getMatchState().isNotPlayerTurn()) {
                    return;
                }
                // check it is MatchState.NOT_STARTED.symbol()
                ItemStack is = view.getItem(slot);
                if (is != null && is.getType() == MatchState.NOT_STARTED.getSymbol()) {
                    turn(view, slotToIndex.get(slot), match);
                }
            }
            case 45 -> {
                view.getTopInventory().setContents(new NoughtsAndCrossesInventory(plugin).getInventory().getContents());
                matches.put(uuid, new MatchData());
            }
            case 53 -> close(player);
            default -> {
            }
        }
    }

    public void switchTurn(InventoryView view, MatchData match) {
        if (match.getMatchState() == MatchState.NOT_STARTED || match.getMatchState() == MatchState.PLAYER_TURN) {
            match.setMatchState(MatchState.TARDIS_TURN);
            view.setItem(8, null);
            view.setItem(17, ItemStack.of(MatchState.TARDIS_TURN.getSymbol()));
        } else {
            match.setMatchState(MatchState.PLAYER_TURN);
            view.setItem(8, ItemStack.of(MatchState.PLAYER_TURN.getSymbol()));
            view.setItem(17, null);
        }
        checkWinner(match);
    }

    public void turn(InventoryView view, int n, MatchData match) {
        plugin.debug("index = " + n + ", slot = " + indexToSlot.get(n) + ", " + match.getMatchState());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            view.setItem(indexToSlot.get(n), ItemStack.of(match.getMatchState().getSymbol()));
            match.getBoard()[n] = match.getMatchState().getSymbol();
            debugBoard(match);
            match.getUsed()[n] = true;
            switchTurn(view, match);
            match.setCount(match.getCount() + 1);
            if (match.getTurn() % 2 == 0) {
                match.setTurn(match.getTurn() + 1);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> tardisTurn(view, match), delay);
            }
        }, 5L);
    }

    public void checkWinner(MatchData match) {
        int chk = 0;
        for (int i = 0; i < 8; i++) {
            if ((match.getBoard()[winPos[i][0]].equals(MatchState.PLAYER_TURN.getSymbol())) && (match.getBoard()[winPos[i][0]].equals(match.getBoard()[winPos[i][1]])) && (match.getBoard()[winPos[i][1]].equals(match.getBoard()[winPos[i][2]]))) {
                //x has won
                plugin.debug("YOU WON!");
                match.setMatchState(MatchState.PLAYER_WON);
                chk = 1;
                for (int j = 0; j < 9; j++) {
                    match.getUsed()[j] = true;
                }
            } else if ((match.getBoard()[winPos[i][0]].equals(MatchState.TARDIS_TURN.getSymbol())) && (match.getBoard()[winPos[i][0]].equals(match.getBoard()[winPos[i][1]])) && (match.getBoard()[winPos[i][1]].equals(match.getBoard()[winPos[i][2]]))) {
                //o has won
                plugin.debug("YOU LOST!");
                match.setMatchState(MatchState.TARDIS_WON);
                chk = 1;
                for (int j = 0; j < 9; j++) {
                    match.getUsed()[j] = true;
                }
            }
        }
        if (match.getCount() == 8 && chk == 0) {
            //draw
            plugin.debug("DRAW!");
            match.setMatchState(MatchState.DRAW);
            for (int j = 0; j < 9; j++) {
                match.getUsed()[j] = true;
            }
        }
    }

    public void tardisTurn(InventoryView view, MatchData match) {
        if (match.getMatchState().isGameOver()) {
            return;
        }
        
        int rand = (int) (Math.random() * 9);
        boolean test = true;
        if (match.getC() < 9) {
            if (!match.getUsed()[4]) {
                rand = 4;
            } else if ((match.getUsed()[0] && match.getUsed()[1]) && !match.getUsed()[2] && ((match.getBoard()[0] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[1] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[0] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[1] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 2;
            } else if ((match.getUsed()[1] && match.getUsed()[2]) && !match.getUsed()[0] && ((match.getBoard()[1] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[2] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[1] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[2] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 0;
            } else if ((match.getUsed()[0] && match.getUsed()[2]) && !match.getUsed()[1] && ((match.getBoard()[0] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[2] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[0] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[2] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 1;
            } else if ((match.getUsed()[3] && match.getUsed()[4]) && !match.getUsed()[5] && ((match.getBoard()[3] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[3] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 5;
            } else if ((match.getUsed()[4] && match.getUsed()[5]) && !match.getUsed()[3] && ((match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[5] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[5] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 3;
            } else if ((match.getUsed()[3] && match.getUsed()[5]) && !match.getUsed()[4] && ((match.getBoard()[3] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[5] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[3] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[5] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 4;
            } else if ((match.getUsed()[6] && match.getUsed()[7]) && !match.getUsed()[8] && ((match.getBoard()[6] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[7] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[6] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[7] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 8;
            } else if ((match.getUsed()[7] && match.getUsed()[8]) && !match.getUsed()[6] && ((match.getBoard()[7] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[8] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[7] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[8] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 6;
            } else if ((match.getUsed()[6] && match.getUsed()[8]) && !match.getUsed()[7] && ((match.getBoard()[6] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[8] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[6] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[8] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 7;
            } else if ((match.getUsed()[0] && match.getUsed()[3]) && !match.getUsed()[6] && ((match.getBoard()[0] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[3] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[0] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[3] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 6;
            } else if ((match.getUsed()[3] && match.getUsed()[6]) && !match.getUsed()[0] && ((match.getBoard()[3] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[6] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[3] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[6] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 0;
            } else if ((match.getUsed()[0] && match.getUsed()[6]) && !match.getUsed()[3] && ((match.getBoard()[0] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[6] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[0] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[6] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 3;
            } else if ((match.getUsed()[1] && match.getUsed()[4]) && !match.getUsed()[7] && ((match.getBoard()[1] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[1] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 7;
            } else if ((match.getUsed()[4] && match.getUsed()[7]) && !match.getUsed()[1] && ((match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[7] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[7] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 1;
            } else if ((match.getUsed()[1] && match.getUsed()[7]) && !match.getUsed()[4] && ((match.getBoard()[1] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[7] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[1] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[7] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 4;
            } else if ((match.getUsed()[2] && match.getUsed()[5]) && !match.getUsed()[8] && ((match.getBoard()[2] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[5] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[2] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[5] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 8;
            } else if ((match.getUsed()[5] && match.getUsed()[8]) && !match.getUsed()[2] && ((match.getBoard()[5] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[8] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[5] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[8] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 2;
            } else if ((match.getUsed()[2] && match.getUsed()[8]) && !match.getUsed()[5] && ((match.getBoard()[2] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[8] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[2] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[8] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 5;
            } else if ((match.getUsed()[0] && match.getUsed()[4]) && !match.getUsed()[8] && ((match.getBoard()[0] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[0] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 8;
            } else if ((match.getUsed()[4] && match.getUsed()[8]) && !match.getUsed()[0] && ((match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[8] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[8] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 0;
            } else if ((match.getUsed()[0] && match.getUsed()[8]) && !match.getUsed()[4] && ((match.getBoard()[0] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[8] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[0] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[8] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 4;
            } else if ((match.getUsed()[2] && match.getUsed()[4]) && !match.getUsed()[6] && ((match.getBoard()[2] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[2] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 6;
            } else if ((match.getUsed()[4] && match.getUsed()[6]) && !match.getUsed()[2] && ((match.getBoard()[4] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[6] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[4] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[6] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 2;
            } else if ((match.getUsed()[2] && match.getUsed()[6]) && !match.getUsed()[4] && ((match.getBoard()[2] == MatchState.PLAYER_TURN.getSymbol() && match.getBoard()[6] == MatchState.PLAYER_TURN.getSymbol()) || (match.getBoard()[2] == MatchState.TARDIS_TURN.getSymbol() && match.getBoard()[6] == MatchState.TARDIS_TURN.getSymbol()))) {
                rand = 4;
            } else {
                while ((match.getUsed()[rand] || test)) {
                    test = false;
                    rand = (int) (Math.random() * 9);
                    for (int i : match.getCompUsed()) {
                        if (rand == i) {
                            test = true;
                            break;
                        }
                    }
                    if (!test) {
                        match.getCompUsed()[match.getC()] = rand;
                    }
                }
            }
            match.getCompUsed()[match.getC()] = rand;
            match.setC(match.getC() + 1);
            match.getUsed()[rand] = true;
            checkWinner(match);
            turn(view, rand, match);
        }
    }

    private void debugBoard(MatchData match) {
        plugin.debug("[" + match.getBoard()[0] + "][" + match.getBoard()[1] + "][" + match.getBoard()[2] + "]");
        plugin.debug("[" + match.getBoard()[3] + "][" + match.getBoard()[4] + "][" + match.getBoard()[5] + "]");
        plugin.debug("[" + match.getBoard()[6] + "][" + match.getBoard()[7] + "][" + match.getBoard()[8] + "]");
    }

    @EventHandler
    public void onTicTacOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder(false) instanceof NoughtsAndCrossesInventory) {
            Player p = (Player) event.getPlayer();
            matches.put(p.getUniqueId(), new MatchData());
        }
    }

    @EventHandler
    public void onTicTacClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder(false) instanceof NoughtsAndCrossesInventory) {
            Player p = (Player) event.getPlayer();
            matches.remove(p.getUniqueId());
        }
    }
}
