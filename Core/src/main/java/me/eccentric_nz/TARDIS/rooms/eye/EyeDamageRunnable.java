package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class EyeDamageRunnable implements Runnable {

    private final TARDIS plugin;

    public EyeDamageRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (UUID uuid : plugin.getTrackerKeeper().getEyeDamage()) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline() && !hasSpaceHelmet(player)) {
                player.damage(plugin.getConfig().getDouble("eye_of_harmony.damage_amount"));
            }
        }
    }

    private boolean hasSpaceHelmet(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        if (helmet == null || helmet.getType() != Material.GLASS || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta im = helmet.getItemMeta();
        if (!im.hasDisplayName() || !im.hasCustomModelData() || !im.hasMaxStackSize()) {
            return false;
        }
        return im.getDisplayName().equals("TARDIS Space Helmet") && im.getCustomModelData() == 5 && im.getMaxStackSize() == 1;
    }
}
