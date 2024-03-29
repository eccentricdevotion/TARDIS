package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ConsoleBuilder {

    private final TARDIS plugin;

    public ConsoleBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void create(Block block, int type) {
        Block up = block.getRelative(BlockFace.UP);
        for (int i = 0; i < 6; i++) {
            ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta im = shard.getItemMeta();
            im.setCustomModelData((1000 * type) + (i + 1));
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, i);
            shard.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(shard);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            display.setPersistent(true);
            display.setInvulnerable(true);
            float yaw = i * 60.0f;
            yaw = Location.normalizeYaw(yaw);
            // set display rotation
            display.setRotation(yaw, 0);
        }
        for (int i = 30; i < 360; i += 60) {
            ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta im = shard.getItemMeta();
            im.setCustomModelData((1000 * type) + 7);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, i);
            shard.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(shard);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            display.setPersistent(true);
            display.setInvulnerable(true);
            float yaw = Location.normalizeYaw(i);
            // set display rotation
            display.setRotation(yaw, 0);
        }
    }
}
