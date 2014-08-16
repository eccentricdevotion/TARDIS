package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetVault;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicSorterListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TARDISVaultListener implements Listener {

    private final TARDIS plugin;
    private String dn;
    private final boolean dw;

    public TARDISVaultListener(TARDIS plugin) {
        this.plugin = plugin;
        this.dn = "TARDIS_TimeVortex";
        this.dw = this.plugin.getConfig().getBoolean("creation.default_world");
        if (this.dw) {
            dn = this.plugin.getConfig().getString("creation.default_world_name");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDropChestClose(InventoryCloseEvent event) {
        final Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            Player p = (Player) event.getPlayer();
            String loc = chest.getLocation().toString();
            // check is drop chest
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("location", loc);
            ResultSetVault rs = new ResultSetVault(plugin, where);
            if (!rs.resultSet()) {
                return;
            }
            // sort contents
            TARDISSonicSorterListener.sortInventory(inv, 0, inv.getSize());
            World w = chest.getWorld();
            // get vault dimensions
            int sx = rs.getX();
            int sy = rs.getY();
            int sz = rs.getZ();
            // loop through vault blocks
            for (int y = sy; y < (sy + 16); y++) {
                for (int x = sx; x < (sx + 16); x++) {
                    for (int z = sz; z < (sz + 16); z++) {
                        // get the block
                        Block b = w.getBlockAt(x, y, z);
                        // check if it is a chest (but not the drop chest)
                        if ((b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) && !loc.equals(b.getLocation().toString())) {
                            Chest c = (Chest) b.getState();
                            // get chest contents
                            Inventory cinv = c.getBlockInventory();
                            // make sure there is a free slot
                            if (cinv.firstEmpty() != -1) {
                                ItemStack[] cc = cinv.getContents();
                                List<Material> mats = new ArrayList<Material>();
                                // find unique item stack materials
                                for (ItemStack is : cc) {
                                    if (is != null && !mats.contains(is.getType())) {
                                        mats.add(is.getType());
                                    }
                                }
                                // for each material found, see if there are any stacks of it in the drop chest
                                for (Material m : mats) {
                                    int slot = inv.first(m);
                                    while (slot != -1 && cinv.firstEmpty() != -1) {
                                        // get the item stack
                                        ItemStack get = inv.getItem(slot);
                                        // remove the stack from the drop chest
                                        inv.setItem(slot, null);
                                        // put it in the chest
                                        cinv.setItem(cinv.firstEmpty(), get);
                                        // sort the chest
                                        TARDISSonicSorterListener.sortInventory(cinv, 0, cinv.getSize());
                                        // get any other stacks
                                        slot = inv.first(m);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
