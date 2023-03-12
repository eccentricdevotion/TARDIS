package me.eccentric_nz.tardisshop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.planets.TARDISAngelsAPI;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopItemGetter {

    public static ItemStack getTWAItem(ShopItem item) {
        if (TARDISShop.isTWAEnabled()) {
            // get TARDISWeepingAngels API
            TARDISWeepingAngelsAPI twa = TARDISAngelsAPI.getAPI(TARDIS.plugin);
            if (item.getMaterial() == Material.BONE) {
                // K9
                return twa.getK9();
            } else {
                // Monster head
                String m = item.toString().replace("_HEAD", "");
                Monster monster = Monster.valueOf(m);
                return twa.getHead(monster);
            }
        }
        return null;
    }

    public static ItemStack getSeedItem(ShopItem item) {
        String s = item.toString().replace("_SEED", "");
        return TARDISShop.getTardisAPI().getTARDISSeedItem(s);
    }

    public static ItemStack getShapeItem(ShopItem item, Player player) {
        return TARDISShop.getTardisAPI().getTARDISShapeItem(item.getDisplayName(), player);
    }

    public static ItemStack getBlueprintItem(ShopItem item, Player player) {
        return TARDISShop.getTardisAPI().getTARDISBlueprintItem(item.toString(), player);
    }
}
