package me.eccentric_nz.TARDIS.chemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GlowStickRunnable implements Runnable {

    private final TARDIS plugin;
    private final NamespacedKey namespacedKey;

    public GlowStickRunnable(TARDIS plugin) {
        this.plugin = plugin;
        namespacedKey = new NamespacedKey(this.plugin, "glow_stick_time");
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerInventory inventory = player.getInventory();
            // item in hands
            ItemStack mainHand = inventory.getItemInMainHand();
            if (mainHand != null && isGlowStick(mainHand)) {
                damage(mainHand, player, inventory, true);
            }
            ItemStack offHand = inventory.getItemInOffHand();
            if (offHand != null && isGlowStick(offHand)) {
                damage(offHand, player, inventory, false);
            }
        }
    }

    private void damage(ItemStack glowStick, Player player, PlayerInventory inventory, boolean main) {
        ItemMeta im = glowStick.getItemMeta();
        PersistentDataContainer pdk = im.getPersistentDataContainer();
        if (pdk.has(namespacedKey, PersistentDataType.INTEGER)) {
            int damage = pdk.get(namespacedKey, PersistentDataType.INTEGER) - 5;
//            plugin.debug("damage: " + damage);
            if (damage <= 0) {
                if (main) {
                    inventory.setItemInMainHand(null);
                } else {
                    inventory.setItemInOffHand(null);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            } else {
                pdk.set(namespacedKey, PersistentDataType.INTEGER, damage);
                glowStick.setItemMeta(im);
            }
            player.updateInventory();
        }
    }

    private boolean isGlowStick(ItemStack glowStick) {
        return glowStick != null && GlowStickMaterial.isCorrectMaterial(glowStick.getType()) && glowStick.hasItemMeta() && glowStick.getItemMeta().hasCustomModelData() && glowStick.containsEnchantment(Enchantment.LOYALTY);
    }
}
