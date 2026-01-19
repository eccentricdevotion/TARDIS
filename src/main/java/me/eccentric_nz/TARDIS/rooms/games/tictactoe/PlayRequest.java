package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import org.bukkit.entity.Player;

public class PlayRequest {
    private Player sender;
    private Player receiver;

    public PlayRequest(Player sender, Player receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }
}
