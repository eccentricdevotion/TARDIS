package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class VillagerListener implements Listener {

    private final TARDIS plugin;

    public VillagerListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerCareerChange(VillagerCareerChangeEvent event) {
        if (event.getReason() != VillagerCareerChangeEvent.ChangeReason.EMPLOYED) {
            return;
        }
        if (event.getProfession() != Villager.Profession.CARTOGRAPHER) {
            return;
        }
        Villager villager = event.getEntity();
        if (villager.getPersistentDataContainer().has(plugin.getTradesKey(), PersistentDataType.BOOLEAN)) {
            // already has a TARDIS map trade
            return;
        }
        List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());
        LocatorMap lm = new LocatorMap(plugin);
        Chunk chunk = villager.getLocation().getChunk();
        Location location = lm.getLocation(villager.getWorld(), chunk.getX(), chunk.getZ());
        if (location != null) {
            recipes.add(lm.fromMap(lm.create(location)));
        }
        villager.setRecipes(recipes);
        // prevent adding another map trade
        villager.getPersistentDataContainer().set(plugin.getTradesKey(), PersistentDataType.BOOLEAN, true);
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        AbstractVillager trader = event.getEntity();
        if (trader.getPersistentDataContainer().has(plugin.getTradesKey(), PersistentDataType.BOOLEAN)) {
            // already has a TARDIS map trade
            return;
        }
        if (trader instanceof Villager villager && villager.getProfession() != Villager.Profession.CARTOGRAPHER) {
            return;
        }
        LocatorMap lm = new LocatorMap(plugin);
        Chunk chunk = trader.getLocation().getChunk();
        Location location = lm.getLocation(trader.getWorld(), chunk.getX(), chunk.getZ());
        if (location != null) {
            event.setRecipe(lm.fromMap(lm.create(location)));
        }
    }

    @EventHandler
    public void onVillagerReplenish(VillagerReplenishTradeEvent event) {
        AbstractVillager trader = event.getEntity();
        if (trader.getPersistentDataContainer().has(plugin.getTradesKey(), PersistentDataType.BOOLEAN)) {
            // already has a TARDIS map trade
            return;
        }
        if (trader instanceof Villager villager && villager.getProfession() != Villager.Profession.CARTOGRAPHER) {
            return;
        }
        List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
        LocatorMap lm = new LocatorMap(plugin);
        Chunk chunk = trader.getLocation().getChunk();
        Location location = lm.getLocation(trader.getWorld(), chunk.getX(), chunk.getZ());
        if (location != null) {
            recipes.add(lm.fromMap(lm.create(location)));
        }
        trader.setRecipes(recipes);
    }
}
