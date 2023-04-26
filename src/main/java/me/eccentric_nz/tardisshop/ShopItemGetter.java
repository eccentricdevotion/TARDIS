package me.eccentric_nz.tardisshop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.utils.HeadBuilder;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopItemGetter {

    public static ItemStack getTWAItem(ShopItem item) {
            if (item.getMaterial() == Material.BONE) {
                // K9
                return HeadBuilder.getK9();
            } else {
                // Monster head
                String m = item.toString().replace("_HEAD", "");
                Monster monster = Monster.valueOf(m);
                return HeadBuilder.getItemStack(monster);
            }
    }

    public static ItemStack getSeedItem(ShopItem item) {
        String s = item.toString().replace("_SEED", "");
        return TARDIS.plugin.getTardisAPI().getTARDISSeedItem(s);
    }

    public static ItemStack getShapeItem(ShopItem item, Player player) {
        return TARDIS.plugin.getTardisAPI().getTARDISShapeItem(item.getDisplayName(), player);
    }

    public static ItemStack getBlueprintItem(ShopItem item, Player player) {
        return TARDIS.plugin.getTardisAPI().getTARDISBlueprintItem(item.toString(), player);
    }
}
