package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.rooms.games.tictactoe.TicTacToeInventory;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class StoneMagmaIceListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public StoneMagmaIceListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof StoneMagmaIceInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 36) {
            return;
        }
        InventoryView view = event.getView();
        if (view.getItem(slot) == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        switch (slot) {
            case 11, 13, 15 -> {
                if (view.getItem(3) != null || view.getItem(5) != null) {
                    return;
                }
                view.setItem(3, view.getItem(slot).clone());
                // get TARDIS choice
                ShowHand hand = new ShowHand();
                view.setItem(5, hand.getTARDISChoice());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Sound sound = hand.revealResults(view);
                    player.playSound(player.getLocation(), sound, 0.8f, 0.8f);
                }, 10L);
            }
            // reset
            case 27 -> view.getTopInventory().setContents(new StoneMagmaIceInventory(plugin).getInventory().getContents());
            case 35 -> close(player);
            default -> {
            }
        }
    }
}
