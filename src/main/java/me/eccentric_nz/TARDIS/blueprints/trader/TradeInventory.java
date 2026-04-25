package me.eccentric_nz.TARDIS.blueprints.trader;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TradeInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TradeInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Timelord Trades", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack(player));
    }

    /**
     * Constructs an inventory for the Time Lord Trader GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack(Player player) {

        ItemStack[] is = new ItemStack[54];

        return is;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
