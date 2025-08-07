package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ArtronCondenserInventory implements InventoryHolder {

    private final String title;
    private final Location location;
    private final Inventory inventory;

    public ArtronCondenserInventory(TARDIS plugin, InventoryHolder holder, String title, Location location) {
        this.title = title;
        this.location = location;
        ItemStack[] stacks = holder.getInventory().getContents();
        this.inventory = plugin.getServer().createInventory(this, stacks.length, Component.text(title, NamedTextColor.DARK_RED));
        this.inventory.setContents(stacks);
        holder.getInventory().clear();
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
