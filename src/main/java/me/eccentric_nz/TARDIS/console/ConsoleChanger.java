package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConsoleChanger {

    private final TARDIS plugin;

    public ConsoleChanger(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setType(Block block, int type) {
        // get item displays around the block
        for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 2,2,2)) {
            if (entity instanceof ItemDisplay display) {
                // get the item stack
                ItemStack is = display.getItemStack();
                if (is != null && is.getType() == Material.AMETHYST_SHARD) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasCustomModelData()) {
                        // cmd % 10 + (1000 * type)
                        int cmd = (im.getCustomModelData() % 10) + (1000 * type);
                        im.setCustomModelData(cmd);
                        is.setItemMeta(im);
                        display.setItemStack(is);
                    }
                }
            }
        }
    }
}
