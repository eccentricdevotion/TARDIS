package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BleachCommand {

    private final TARDIS plugin;

    public BleachCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setDisplay(Player player) {
        Block block = player.getTargetBlock(null, 4);
        Location location = block.getLocation().add(0.75d, 0.5d, 1.0d);
        ItemStack bleach = ItemStack.of(Material.WHITE_DYE);
        ItemMeta im = bleach.getItemMeta();
        im.displayName(Component.text("Bleach"));
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
        bleach.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
        display.setItemStack(bleach);
        return true;
    }
}
