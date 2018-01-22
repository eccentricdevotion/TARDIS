package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetVault;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicSorterListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TARDISVaultListener implements Listener {

    private final TARDIS plugin;

    public TARDISVaultListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    
    public void onVaultDropChestClose(InventoryCloseEvent event) {
        final Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            String loc = chest.getLocation().toString();
            // check is drop chest
            ResultSetVault rs = new ResultSetVault(plugin, loc);
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
                                List<Material> mats = new ArrayList<>();
                                List<MatData> mds = new ArrayList<>();
                                // find unique item stack materials
                                for (ItemStack is : cc) {
                                    if (is != null) {
                                        Material m = is.getType();
                                        MatData md = new MatData();
                                        md.setMaterial(m);
                                        md.setData(is.getData().getData());
                                        if (mats.contains(m)) {
                                            boolean found = false;
                                            for (MatData dm : mds) {
                                                if (dm.equals(md)) {
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                mds.add(md);
                                            }
                                        } else {
                                            // just add the material data
                                            mds.add(md);
                                            mats.add(m);
                                        }
                                    }
                                }
                                // for each material found, see if there are any stacks of it in the drop chest
                                mds.forEach((m) -> {
                                    int slot = inv.first(m.getMaterial());
                                    while (slot != -1 && cinv.firstEmpty() != -1) {
                                        // get the item stack
                                        ItemStack get = inv.getItem(slot);
                                        if (get.getData().getData() == m.getData()) {
                                            // remove the stack from the drop chest
                                            inv.setItem(slot, null);
                                            // put it in the chest
                                            cinv.setItem(cinv.firstEmpty(), get);
                                            // sort the chest
                                            TARDISSonicSorterListener.sortInventory(cinv, 0, cinv.getSize());
                                            // get any other stacks
                                            slot = inv.first(m.getMaterial());
                                        } else {
                                            slot = -1;
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    public static class MatData {

        private Material material;
        private byte data;

        public Material getMaterial() {
            return material;
        }

        public void setMaterial(Material material) {
            this.material = material;
        }

        public byte getData() {
            return data;
        }

        public void setData(byte data) {
            this.data = data;
        }

        /**
         * Define equality of state.
         *
         * @param md the MatData to compare to
         * @return true or false
         */
        @Override
        public boolean equals(Object md) {
            if (this == md) {
                return true;
            }
            if (!(md instanceof MatData)) {
                return false;
            }
            MatData that = (MatData) md;
            return (this.material == that.material && this.data == that.data);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (this.material != null ? this.material.hashCode() : 0);
            hash = 67 * hash + this.data;
            return hash;
        }
    }
}
