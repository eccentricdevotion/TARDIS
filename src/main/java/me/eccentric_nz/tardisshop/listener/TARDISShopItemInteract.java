package me.eccentric_nz.tardisshop.listener;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisshop.*;
import me.eccentric_nz.tardisshop.database.DeleteShopItem;
import me.eccentric_nz.tardisshop.database.ResultSetShopItem;
import me.eccentric_nz.tardisshop.database.UpdateShopItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class TARDISShopItemInteract implements Listener {

    private final TARDIS plugin;

    public TARDISShopItemInteract(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == plugin.getShopSettings().getBlockMaterial()) {
            Location location = block.getLocation();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                if (plugin.getShopSettings().getSettingItem().containsKey(uuid)) {
                    TARDISShopItem item = plugin.getShopSettings().getSettingItem().get(uuid);
                    Location drop = location.clone().add(0.5d, 1.05d, 0.5d);
                    new TARDISShopItemSpawner(plugin).setItem(drop, item);
                    // update location in database
                    new UpdateShopItem(plugin).addLocation(location.toString(), item.getId());
                    player.sendMessage(plugin.getPluginName() + "Item location added to database!");
                    plugin.getShopSettings().getSettingItem().remove(uuid);
                } else if (plugin.getShopSettings().getRemovingItem().contains(uuid)) {
                    for (Entity e : block.getWorld().getNearbyEntities(location, 1.0d, 2.0d, 1.0d)) {
                        if (e instanceof Item && e.getPersistentDataContainer().has(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER) || e instanceof ArmorStand) {
                            e.remove();
                        }
                    }
                    // remove database record
                    if (new DeleteShopItem(plugin).removeByLocation(location.toString()) > 0) {
                        player.sendMessage(plugin.getPluginName() + "Item removed from database");
                    } else {
                        player.sendMessage(plugin.getPluginName() + "Item location not found in database!");
                    }
                    plugin.getShopSettings().getRemovingItem().remove(uuid);
                } else {
                    // is it a shop block?
                    ResultSetShopItem rs = new ResultSetShopItem(plugin);
                    if (rs.itemFromBlock(location.toString())) {
                        TARDISShopItem item = rs.getShopItem();
                        String message;
                        // do they have sufficient credit?
                        if (player.hasPermission("tardis.admin") && plugin.getConfig().getBoolean("tardis_admin_free")) {
                            // give item
                            giveItem(item.getItem(), player);
                            message = "Freebies for admins :)";
                        } else if (plugin.getShopSettings().getEconomy().getBalance(player) > item.getCost()) {
                            // give item
                            giveItem(item.getItem(), player);
                            plugin.getShopSettings().getEconomy().withdrawPlayer(player, item.getCost());
                            message = "Item purchased :)";
                        } else {
                            // no credit
                            message = "You have insufficient credit available to purchase this item!";
                        }
                        player.sendMessage(plugin.getPluginName() + message);
                    }
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                ResultSetShopItem rs = new ResultSetShopItem(plugin);
                event.setCancelled(rs.itemFromBlock(location.toString()));
            }
        }
    }

    private void giveItem(String item, Player player) {
        try {
            ShopItem recipe = ShopItem.valueOf(TARDISStringUtils.toEnumUppercase(item));
            ItemStack is;
            switch (recipe.getRecipeType()) {
                case BLUEPRINT:
                    is = ShopItemGetter.getBlueprintItem(recipe, player);
                    break;
                case TWA:
                    is = ShopItemGetter.getTWAItem(recipe);
                    break;
                case SEED:
                    is = ShopItemGetter.getSeedItem(recipe);
                    break;
                case SHAPELESS:
                default:
                    is = ShopItemGetter.getShapeItem(recipe, player);
                    break;
            }
            if (is != null) {
                HashMap<Integer, ItemStack> res = player.getInventory().addItem(is);
                if (!res.isEmpty()) {
                    for (ItemStack stack : res.values()) {
                        player.getWorld().dropItem(player.getLocation(), stack);
                    }
                }
                player.updateInventory();
            }
        } catch (IllegalArgumentException e) {
            plugin.debug("Could not get ShopItemRecipe from item string. " + e.getMessage());
        }
    }
}
