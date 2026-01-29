package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import org.bukkit.Material;

public class MatchData {

    private final boolean[] used = {false, false, false, false, false, false, false, false, false};
    private final Material[] board = {MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol(), MatchState.NOT_STARTED.getSymbol()};
    private final int[] compUsed = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    private int c = 0;
    private int turn = 0;
    private MatchState matchState = MatchState.NOT_STARTED;
    private int count = 0;

    public boolean[] getUsed() {
        return used;
    }

    public Material[] getBoard() {
        return board;
    }

    public int[] getCompUsed() {
        return compUsed;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
