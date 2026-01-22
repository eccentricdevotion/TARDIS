package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import org.bukkit.Material;

public enum MatchState {
    NOT_STARTED,
    IN_PROGRESS,
    DRAW(Material.PINK_GLAZED_TERRACOTTA),
    PLAYER_TURN(Material.CYAN_GLAZED_TERRACOTTA),
    PLAYER_WON(Material.GOLD_BLOCK),
    TARDIS_TURN(Material.RED_GLAZED_TERRACOTTA),
    TARDIS_WON(Material.LAPIS_BLOCK);

    private final Material symbol;

    MatchState(Material symbol) {
        this.symbol = symbol;
    }

    MatchState() {
        this.symbol = Material.LIGHT_GRAY_WOOL;
    }

    public Material getSymbol() {
        return symbol;
    }

    public boolean isNotPlayerTurn() {
        switch (this) {
            case DRAW, PLAYER_WON, TARDIS_TURN, TARDIS_WON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
