package me.eccentric_nz.tardisshop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisshop.database.InsertShopItem;
import me.eccentric_nz.tardisshop.database.ResultSetUpdateShop;
import me.eccentric_nz.tardisshop.database.UpdateShopItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class ShopUtility {

    public static void add(TARDIS plugin, Player player, String n) {
        String name = n.toLowerCase(Locale.ROOT);
        if (!plugin.getItemsConfig().contains(name)) {
            plugin.getMessenger().send(player, TardisModule.SHOP, "TOO_FEW_ARGS");
        }
        double cost = plugin.getItemsConfig().getDouble(name);
        TARDISShopItem item = new InsertShopItem(plugin).addNamedItem(TARDISStringUtils.capitalise(n), cost);
        plugin.getShopSettings().getSettingItem().put(player.getUniqueId(), item);
        plugin.getMessenger().send(player, TardisModule.SHOP, "SHOP_ADD", plugin.getShopSettings().getBlockMaterial().toString());
    }

    public static void remove(TARDIS plugin, Player player) {
        plugin.getShopSettings().getRemovingItem().add(player.getUniqueId());
        plugin.getMessenger().send(player, TardisModule.SHOP, "SHOP_REMOVE", plugin.getShopSettings().getBlockMaterial().toString());
    }

    public static void update(TARDIS plugin) {
        // reload items.yml
        File file = new File(plugin.getDataFolder(), "items.yml");
        try {
            plugin.getItemsConfig().load(file);
        } catch (InvalidConfigurationException | IOException e) {
            plugin.debug("Failed to reload items.yml" + e.getMessage());
        }
        // get shop items
        ResultSetUpdateShop rs = new ResultSetUpdateShop(plugin);
        if (rs.getAll()) {
            for (TARDISShopItem item : rs.getShopItems()) {
                String lookup = item.item().replace(" ", "_").toLowerCase(Locale.ROOT);
                double cost = plugin.getItemsConfig().getDouble(lookup);
                if (cost != item.cost()) {
                    // update database
                    new UpdateShopItem(plugin).updateCost(cost, item.id());
                    // find text display and update the text
                    for (Entity e : item.location().getWorld().getNearbyEntities(item.location().add(0.5d, 1.0d, 0.5d), 0.5d, 1.0d, 0.5d)) {
                        if (e instanceof TextDisplay text) {
                            text.text(Component.text(item.item() + "\n").append( Component.text("Cost:", NamedTextColor.RED)).append(Component.text(String.format(" %.2f", cost), NamedTextColor.WHITE)));
                        }
                    }
                }
            }
        }
    }
}
