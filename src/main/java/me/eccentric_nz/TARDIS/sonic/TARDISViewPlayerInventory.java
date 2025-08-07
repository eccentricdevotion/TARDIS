package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TARDISViewPlayerInventory implements InventoryHolder {

    private final Inventory inventory;

    public TARDISViewPlayerInventory(TARDIS plugin, Player scanned) {
        ItemStack[] items = scanned.getInventory().getStorageContents();
        this.inventory = plugin.getServer().createInventory(this, items.length, Component.text(scanned.getName() + "'s Inventory", NamedTextColor.DARK_RED));
        this.inventory.setContents(items);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
