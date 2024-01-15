package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlyingAnimation implements Runnable {

    private final TARDIS plugin;
    private final ArmorStand stand;
    private final Player player;
    private final boolean pandorica;
    int i = 0;

    public FlyingAnimation(TARDIS plugin, ArmorStand stand, Player player, boolean pandorica) {
        this.plugin = plugin;
        this.stand = stand;
        this.player = player;
        this.pandorica = pandorica;
    }

    @Override
    public void run() {
        EntityEquipment ee = stand.getEquipment();
        ItemStack is = ee.getHelmet();
        ItemMeta im = is.getItemMeta();
        // switch the custom model - to simulate rotating the TARDIS while flying
        int open = plugin.getTrackerKeeper().getSonicDoorToggle().contains(player.getUniqueId()) ? 1000 : 0;
        im.setCustomModelData((pandorica ? 1008 : 1005) + i + open);
        is.setItemMeta(im);
        ee.setHelmet(is);
        if (!TARDISStaticUtils.isSonic(player.getInventory().getItemInMainHand())) {
            i++;
        }
        if (i > 15) {
            i = 0;
        }
    }
}
