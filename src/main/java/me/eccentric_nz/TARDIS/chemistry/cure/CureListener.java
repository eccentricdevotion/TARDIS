package me.eccentric_nz.TARDIS.chemistry.cure;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CureListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrinkCure(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("tardis.chemistry.cure")) {
            return;
        }
        ItemStack is = event.getItem();
        if (!is.getType().equals(Material.MILK_BUCKET) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasCustomModelData()) {
            return;
        }
        switch (im.getDisplayName()) {
            case "Antidote":
                PotionEffect poison = event.getPlayer().getPotionEffect(PotionEffectType.POISON);
                if (poison != null) {

                }
                break;
            case "Elixir":
                PotionEffect weakness = event.getPlayer().getPotionEffect(PotionEffectType.WEAKNESS);
                if (weakness != null) {

                }
                break;
            case "Eye drops":
                PotionEffect blindness = event.getPlayer().getPotionEffect(PotionEffectType.BLINDNESS);
                if (blindness != null) {

                }
                break;
            case "Tonic":
                PotionEffect nausea = event.getPlayer().getPotionEffect(PotionEffectType.CONFUSION);
                if (nausea != null) {

                }
                break;
            default:
                break;
        }
    }
}
