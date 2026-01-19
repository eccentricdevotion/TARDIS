package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class TicTacToeInventory implements InventoryHolder {

    private final Inventory inventory;

    public TicTacToeInventory() {
        this.inventory = TARDIS.plugin.getServer().createInventory(this, 9, Component.text("Tic Tac Toe", NamedTextColor.DARK_RED));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
