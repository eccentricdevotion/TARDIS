package me.eccentric_nz.TARDIS.blueprints.trader;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.planets.GallifreyBlueprintTrade;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.view.MerchantView;
import org.bukkit.inventory.view.builder.MerchantInventoryViewBuilder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TimeLordTradeListener implements Listener {

    private final TARDIS plugin;

    public TimeLordTradeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Mannequin mannequin
                && mannequin.getPersistentDataContainer().has(plugin.getTimeLordUuidKey(), PersistentDataType.STRING)) {
            int count = mannequin.getPersistentDataContainer().getOrDefault(plugin.getTradesKey(), PersistentDataType.INTEGER, 0);
            if (count > 4) {
                // remove this time lord
                Dematerialise runnable = new Dematerialise(plugin, mannequin);
                int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 20L);
                runnable.setTask(task);
                TARDISSounds.playTARDISSound(mannequin.getLocation(), "tardis_takeoff_fast");
                return;
            }
            // get the recipes
            String t = mannequin.getPersistentDataContainer().get(plugin.getTimeLordUuidKey(), PersistentDataType.STRING);
            if (t == null) {
                return;
            }
            // get recipes
            List<MerchantRecipe> recipes = new ArrayList<>();
            for (String r : t.split(",")) {
                recipes.add(getRoom(r));
            }
            // create a merchant
            Merchant merchant = plugin.getServer().createMerchant();
            merchant.setRecipes(recipes);
            // open trade gui
            MerchantInventoryViewBuilder<MerchantView> menuBuilder = MenuType.MERCHANT.builder();
            menuBuilder.title(Component.text("Time Lord Trade", NamedTextColor.RED));
            menuBuilder.merchant(merchant);
            Player player = event.getPlayer();
            InventoryView inventoryView = menuBuilder.build(player);
            player.openInventory(inventoryView);
        }
    }

    private MerchantRecipe getRoom(String room) {
        BlueprintRoom bpr = BlueprintRoom.valueOf(room);
        // get the blueprint item stack
        ItemStack ris = GallifreyBlueprintTrade.buildResult(plugin, bpr.getPermission(), bpr.toString());
        return GallifreyBlueprintTrade.getRoomRecipe(plugin, room, ris, 1);
    }
}
