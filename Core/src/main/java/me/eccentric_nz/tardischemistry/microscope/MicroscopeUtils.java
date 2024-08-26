package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

class MicroscopeUtils {

    static boolean hasItemInHand(ItemStack is, Material type, TARDIS plugin) {
        if (is == null) {
            return false;
        }
        if (!is.getType().equals(type)) {
            return false;
        }
        // does it have item meta
        ItemMeta im = is.getItemMeta();
        if (im == null) {
            return false;
        }
        if (!im.hasDisplayName()) {
            return false;
        }
        if (!im.hasCustomModelData()) {
            return false;
        }
        if (im.getCustomModelData() != 9999) {
            return false;
        }
        return im.getPersistentDataContainer().has(plugin.getMicroscopeKey(), PersistentDataType.INTEGER);
    }

    static void reduceInHand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        int count = itemStack.getAmount();
        if (count - 1 > 0) {
            itemStack.setAmount(count - 1);
        } else {
            itemStack = null;
        }
        player.getInventory().setItemInMainHand(itemStack);
    }

    static final HashMap<UUID, ItemStack> STORED_STACKS = new HashMap<>();
}
