package me.eccentric_nz.TARDIS.chemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GlowStickListener implements Listener {

    private final TARDIS plugin;
    private final NamespacedKey namespacedKey;

    public GlowStickListener(TARDIS plugin) {
        this.plugin = plugin;
        namespacedKey = new NamespacedKey(this.plugin, "glow_stick_time");
    }

    @EventHandler
    public void onGlowStickUse(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Player player = event.getPlayer();
            ItemStack is = event.getItem();
            if (is != null && GlowStickMaterial.isCorrectMaterial(is.getType()) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName() && im.getDisplayName().endsWith("Glow Stick") && im.hasCustomModelData() && !im.hasEnchant(Enchantment.LOYALTY)) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0f, 1.0f);
                    // switch custom data models e.g. 10000008 -> 12000008
                    int cmd = im.getCustomModelData() + 2000000;
                    im.setCustomModelData(cmd);
                    im.addEnchant(Enchantment.LOYALTY, 1, true);
                    im.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, 100);
                    is.setItemMeta(im);
                }
            }
        }
    }
}
