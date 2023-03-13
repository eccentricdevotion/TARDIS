package me.eccentric_nz.tardisshop;

import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemBreak;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemDespawn;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemExplode;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TARDISShop {

    public void enable(TARDIS plugin) {
        if (plugin.getPM().isPluginEnabled("Vault")) {
            plugin.getPM().registerEvents(new TARDISShopItemInteract(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISShopItemDespawn(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISShopItemBreak(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISShopItemExplode(plugin), plugin);
            ShopSettings settings = new ShopSettings();
            settings.setItemKey(new NamespacedKey(plugin, "tardis_shop_item"));
            settings.setBlockMaterial(Material.valueOf(plugin.getShopConfig().getString("block")));
            setupEconomy(settings, plugin);
            plugin.setShopSettings(settings);
            TARDISShopCommand command = new TARDISShopCommand(plugin);
            plugin.getCommand("tardisshop").setExecutor(command);
            plugin.getCommand("tardisshop").setTabCompleter(command);
        } else {
            plugin.getLogger().log(Level.WARNING, "This feature requires the Vault plugin to function, disabling...");
        }
    }

    // load economy API from Vault
    private void setupEconomy(ShopSettings settings, TARDIS plugin) {
        if (plugin.getPM().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        settings.setEconomy(rsp.getProvider());
    }
}
