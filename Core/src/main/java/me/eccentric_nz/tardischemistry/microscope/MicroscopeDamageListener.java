package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MicroscopeDamageListener implements Listener {

    private final TARDIS plugin;

    MicroscopeDamageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ItemFrame frame) {
            if (!frame.getPersistentDataContainer().has(plugin.getMicroscopeKey(), PersistentDataType.INTEGER)) {
                return;
            }
            event.setCancelled(true);
            // get the item in the frame
            ItemStack dye = frame.getItem();
            if (dye.getType().isAir()) {
                return;
            }
            if (!LabEquipment.getByMaterial().containsKey(dye.getType())) {
                return;
            }
            if (event.getDamager() instanceof Player player) {
                LabEquipment equipment = LabEquipment.getByMaterial().get(dye.getType());
                ItemStack drop = new ItemStack(equipment.material, 1);
                ItemMeta dropMeta = drop.getItemMeta();
                dropMeta.setDisplayName(equipment.getName());
                dropMeta.setCustomModelData(10000);
                drop.setItemMeta(dropMeta);
                player.getWorld().dropItem(entity.getLocation(), drop);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, frame::remove, 1L);
            }
        }
    }
}
