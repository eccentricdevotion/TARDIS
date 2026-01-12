package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rooms.laundry.WashingMachineInventory;
import org.bukkit.entity.Player;

public class LaundryAction {

    private final TARDIS plugin;

    public LaundryAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        player.openInventory(new WashingMachineInventory(plugin, player).getInventory());
    }
}
