package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class MicroscopePlaceListener implements Listener {

    private final TARDIS plugin;
    private final Set<Material> MATS = new HashSet<>();

    MicroscopePlaceListener(TARDIS plugin) {
        this.plugin = plugin;
        MATS.add(Material.GLASS);
        MATS.add(Material.GRAY_STAINED_GLASS);
        MATS.add(Material.LIGHT_BLUE_STAINED_GLASS);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        Material type = event.getBlock().getType();
        if (MATS.contains(type)) {
            Player player = event.getPlayer();
            ItemStack is = player.getInventory().getItemInMainHand();
            event.setCancelled(MicroscopeUtils.hasItemInHand(is, type, plugin));
        }
    }
}
