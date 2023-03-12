package me.eccentric_nz.tardisshop;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class TARDISShopItemSpawner {

    private final TARDISShop plugin;

    public TARDISShopItemSpawner(TARDISShop plugin) {
        this.plugin = plugin;
    }

    public void setItem(Location location, TARDISShopItem what) {
        String toEnum = TARDISStringUtils.toEnumUppercase(what.getItem());
        try {
            ShopItem si = ShopItem.valueOf(toEnum);
            ItemStack is = new ItemStack(si.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(si.getCustomModelData());
            im.setDisplayName(what.getItem());
            im.getPersistentDataContainer().set(plugin.getItemKey(), PersistentDataType.INTEGER, 10001);
            is.setItemMeta(im);
            Item item = location.getWorld().dropItem(location, is);
            item.setVelocity(new Vector(0, 0, 0));
            item.getPersistentDataContainer().set(plugin.getItemKey(), PersistentDataType.INTEGER, 10001);
            item.setCustomName(what.getItem());
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setInvulnerable(true);
            item.setVelocity(new Vector(0, 0, 0));
            ArmorStand armourStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, -1.05d, 0), EntityType.ARMOR_STAND);
            armourStand.setSmall(true);
            armourStand.setVisible(false);
            armourStand.setCustomName(ChatColor.RED + "Cost:" + ChatColor.RESET + String.format(" %.2f", what.getCost()));
            armourStand.setCustomNameVisible(true);
            armourStand.setGravity(false);
            armourStand.setInvulnerable(true);
            armourStand.setVelocity(new Vector(0, 0, 0));
        } catch (IllegalArgumentException e) {
            plugin.debug("Illegal shop item [" + toEnum + "] :" + e.getMessage());
        }
    }
}
