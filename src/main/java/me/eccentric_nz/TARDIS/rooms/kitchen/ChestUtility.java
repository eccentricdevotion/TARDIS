package me.eccentric_nz.TARDIS.rooms.kitchen;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetVault;
import me.eccentric_nz.TARDIS.enumeration.SmelterChest;
import me.eccentric_nz.TARDIS.rooms.surgery.HealingData;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class ChestUtility {

    public static ChestData getKitchenData(TARDIS plugin, int id) {
        ResultSetVault rs = new ResultSetVault(plugin);
        boolean has = rs.fromIdAndChestType(id, SmelterChest.KITCHEN);
        Location location = has ? TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation()) : null;
        return new ChestData(id, has, location);
    }

    public static ChestData getSurgeryData(TARDIS plugin, int id) {
        ResultSetVault rs = new ResultSetVault(plugin);
        boolean has = rs.fromIdAndChestType(id, SmelterChest.SURGERY);
        Location location = has ? TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation()) : null;
        return new ChestData(id, has, location);
    }

    public static Inventory getChestInventory(Location location) {
        Block block = location.getBlock();
        if (block.getState() instanceof Chest chest) {
            return chest.getInventory();
        }
        return null;
    }

    public static void removeItem(Material material, Location location) {
        Block block = location.getBlock();
        if (block.getState() instanceof Chest chest) {
            int slot = chest.getInventory().first(material);
            if (slot != -1) {
                ItemStack item = chest.getInventory().getItem(slot);
                int amount = item.getAmount() - 1;
                if (amount > 1) {
                    item.setAmount(amount);
                } else {
                    item = null;
                }
                chest.getInventory().setItem(slot, item);
                // return buckets
                if (item.getType() == Material.MILK_BUCKET) {
                    chest.getInventory().addItem(ItemStack.of(Material.BUCKET, 1));
                }
                // return bowls
                if (item.getType() == Material.MUSHROOM_STEW || item.getType() == Material.RABBIT_STEW || item.getType() == Material.BEETROOT_SOUP) {
                    chest.getInventory().addItem(ItemStack.of(Material.BOWL, 1));
                }
                // return bottles
                if (item.getType() == Material.HONEY_BOTTLE) {
                    chest.getInventory().addItem(ItemStack.of(Material.GLASS_BOTTLE, 1));
                }
                chest.update();
            }
        }
    }

    public static HealingData getFirstHealingPotion(Inventory inventory) {
        int slot = -1;
        // loop through all items in the inventory
        for (ItemStack item : inventory.getContents()) {
            slot++;
            // skip empty slots
            if (item == null || item.isEmpty()) {
                continue;
            }
            // is it a regular, splash, or lingering potion
            if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION) {
                // check if the item contains the POTION_CONTENTS component
                if (item.hasData(DataComponentTypes.POTION_CONTENTS)) {
                    PotionContents contents = item.getData(DataComponentTypes.POTION_CONTENTS);
                    if (contents != null && contents.potion() == PotionType.HEALING) {
                        for (PotionEffect effect : contents.allEffects()) {
                            if (effect.getType().equals(PotionEffectType.INSTANT_HEALTH)) {
                                // 0 = Level I, 1 = Level II, add 1 to make restoring health easier
                                return new HealingData(slot, item.getType(), effect.getAmplifier() + 1);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
