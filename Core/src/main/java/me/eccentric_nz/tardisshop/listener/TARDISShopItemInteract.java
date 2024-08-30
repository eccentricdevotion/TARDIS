package me.eccentric_nz.tardisshop.listener;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisshop.ShopItem;
import me.eccentric_nz.tardisshop.ShopItemGetter;
import me.eccentric_nz.tardisshop.TARDISShopItem;
import me.eccentric_nz.tardisshop.TARDISShopItemSpawner;
import me.eccentric_nz.tardisshop.database.DeleteShopItem;
import me.eccentric_nz.tardisshop.database.ResultSetShopItem;
import me.eccentric_nz.tardisshop.database.UpdateShopItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.UUID;

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
                    new TARDISShopItemSpawner(plugin).setItem(location, item);
                    // update location in database
                    new UpdateShopItem(plugin).addLocation(location.toString(), item.getId());
                    plugin.getMessenger().send(player, TardisModule.SHOP, "SHOP_LOCATION_ADDED");
                    plugin.getShopSettings().getSettingItem().remove(uuid);
                } else if (plugin.getShopSettings().getRemovingItem().contains(uuid)) {
                    BoundingBox box = new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 2.5, block.getZ() + 1);
                    for (Entity e : block.getWorld().getNearbyEntities(box)) {
                        if (e instanceof ItemDisplay
                                || e instanceof TextDisplay
                                || e instanceof Item && e.getPersistentDataContainer().has(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER)
                                || e instanceof ArmorStand) {
                            e.remove();
                        }
                    }
                    // remove database record
                    if (new DeleteShopItem(plugin).removeByLocation(location.toString()) > 0) {
                        plugin.getMessenger().send(player, TardisModule.SHOP, "SHOP_ITEM_REMOVED");
                    } else {
                        plugin.getMessenger().send(player, TardisModule.SHOP, "SHOP_NOT_FOUND");
                    }
                    plugin.getShopSettings().getRemovingItem().remove(uuid);
                } else {
                    // is it a shop block?
                    ResultSetShopItem rs = new ResultSetShopItem(plugin);
                    if (rs.itemFromBlock(location.toString())) {
                        TARDISShopItem item = rs.getShopItem();
                        String message;
                        // do they have sufficient credit?
                        if (TARDISPermission.hasPermission(player, "tardis.admin") && plugin.getShopConfig().getBoolean("tardis_admin_free")) {
                            // give item
                            giveItem(item.getItem(), player);
                            message = "SHOP_FREE";
                        } else if (plugin.getShopSettings().getEconomy().getBalance(player) > item.getCost()) {
                            // give item
                            giveItem(item.getItem(), player);
                            plugin.getShopSettings().getEconomy().withdrawPlayer(player, item.getCost());
                            message = "SHOP_PURCHASED";
                        } else {
                            // no credit
                            message = "SHOP_NO_FUNDS";
                        }
                        plugin.getMessenger().send(player, TardisModule.SHOP, message);
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
            ItemStack is = switch (recipe.getRecipeType()) {
                case BLUEPRINT -> ShopItemGetter.getBlueprintItem(recipe, player);
                case TWA -> ShopItemGetter.getTWAItem(recipe);
                case SEED -> ShopItemGetter.getSeedItem(recipe);
                // SHAPED & SHAPELESS
                default -> ShopItemGetter.getShapeItem(recipe, player);
            };
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
