package me.eccentric_nz.TARDIS.commands.dev;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BleachCommand {

    private final TARDIS plugin;

    public BleachCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setDisplay(Player player) {
        Block block = player.getTargetBlock(null, 4);
        Location location = block.getLocation().add(0.75d, 0.5d, 1.0d);
        ItemStack bleach = ItemStack.of(Material.WHITE_DYE);
        bleach.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Bleach"));
        bleach.editPersistentDataContainer(pdc -> pdc.set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1));
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
        display.setItemStack(bleach);
    }
}
