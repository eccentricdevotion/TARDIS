package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetSmelter;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicSorterListener;
import me.eccentric_nz.TARDIS.utility.Smelter;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TARDISSmelterListener implements Listener {

    private final TARDIS plugin;

    public TARDISSmelterListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSmelterDropChestClose(InventoryCloseEvent event) {
        final Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            String loc = chest.getLocation().toString();
            // check is drop chest
            ResultSetSmelter rs = new ResultSetSmelter(plugin, loc);
            if (!rs.resultSet()) {
                return;
            }
            // sort contents
            TARDISSonicSorterListener.sortInventory(inv, 0, inv.getSize());
            // get fuel chests
            List<Chest> fuelChests = rs.getFuelChests();
            List<Chest> oreChests = rs.getOreChests();
            // process drop chest contents
            HashMap<Material, Integer> fuels = new HashMap<>();
            HashMap<Material, Integer> ores = new HashMap<>();
            HashMap<Material, Integer> remainders = new HashMap<>();
            for (ItemStack is : inv.getContents()) {
                if (is != null) {
                    Material m = is.getType();
                    if (Smelter.isFuel(m)) {
                        int amount = (fuels.containsKey(m)) ? fuels.get(m) + is.getAmount() : is.getAmount();
                        fuels.put(m, amount);
                        inv.remove(is);
                    }
                    if (Smelter.isSmeltable(m)) {
                        int amount = (ores.containsKey(m)) ? ores.get(m) + is.getAmount() : is.getAmount();
                        ores.put(m, amount);
                        inv.remove(is);
                    }
                }
            }
            // process fuels
            int fsize = fuelChests.size();
            fuels.entrySet().forEach((fmap) -> {
                int remainder = fmap.getValue() % fsize;
                if (remainder > 0) {
                    remainders.put(fmap.getKey(), remainder);
                }
                int distrib = fmap.getValue() / fsize;
                fuelChests.forEach((fc) -> {
                    fc.getInventory().addItem(new ItemStack(fmap.getKey(), distrib));
                });
            });
            // process ores
            int osize = oreChests.size();
            ores.entrySet().forEach((omap) -> {
                int remainder = omap.getValue() % osize;
                if (remainder > 0) {
                    remainders.put(omap.getKey(), remainder);
                }
                int distrib = omap.getValue() / osize;
                oreChests.forEach((fc) -> {
                    fc.getInventory().addItem(new ItemStack(omap.getKey(), distrib));
                });
            });
            // return remainder to drop chest
            remainders.entrySet().forEach((rmap) -> {
                inv.addItem(new ItemStack(rmap.getKey(), rmap.getValue()));
            });
        }
    }
}
