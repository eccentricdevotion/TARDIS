package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetVault;
import me.eccentric_nz.TARDIS.enumeration.SmelterChest;
import me.eccentric_nz.TARDIS.sonic.SonicSorterListener;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VaultDrop {

    private final TARDIS plugin;
    private final Set<Material> containers = new HashSet<>();
    private Block leftover = null;

    public VaultDrop(TARDIS plugin) {
        this.plugin = plugin;
        containers.add(Material.BARREL);
        containers.add(Material.CHEST);
        containers.add(Material.TRAPPED_CHEST);
        containers.addAll(Tag.SHULKER_BOXES.getValues());
        containers.addAll(Tag.COPPER_CHESTS.getValues());
    }

    public void processItems(Inventory inv, ResultSetVault rs) {
        InventoryHolder holder = inv.getHolder(false);
        if (holder instanceof Container container) {
            // make a list of container locations
            Set<String> containerLocations = new HashSet<>();
            containerLocations.add(container.getLocation().toString());
            // get the leftover chest if there is one
            ResultSetVault rsl = new ResultSetVault(plugin);
            if (rsl.fromIdAndChestType(rs.getTardis_id(), SmelterChest.UNSORTED)) {
                containerLocations.add(rsl.getLocation());
                Location left = TARDISStaticLocationGetters.getLocationFromBukkitString(rsl.getLocation());
                if (left != null) {
                    leftover = left.getBlock();
                }
            }
            // sort contents
            SonicSorterListener.sortInventory(inv);
            World w = container.getWorld();
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
                        String blocation = b.getLocation().toString();
                        // check if it is a container (but not the drop chest)
                        if (!containerLocations.contains(blocation)) {
                            if (containers.contains(b.getType())) {
                                Container c = (Container) b.getState();
                                if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                                    Chest chestdata = (Chest) b.getBlockData();
                                    Chest.Type chestType = chestdata.getType();
                                    // is it a double chest
                                    if (chestType.equals(Chest.Type.LEFT)) {
                                        switch (chestdata.getFacing()) {
                                            case WEST ->
                                                    containerLocations.add(b.getRelative(BlockFace.NORTH).getLocation().toString());
                                            case SOUTH ->
                                                    containerLocations.add(b.getRelative(BlockFace.WEST).getLocation().toString());
                                            case EAST ->
                                                    containerLocations.add(b.getRelative(BlockFace.SOUTH).getLocation().toString());
                                            // NORTH
                                            default ->
                                                    containerLocations.add(b.getRelative(BlockFace.EAST).getLocation().toString());
                                        }
                                    } else if (chestType.equals(Chest.Type.RIGHT)) {
                                        switch (chestdata.getFacing()) {
                                            case WEST ->
                                                    containerLocations.add(b.getRelative(BlockFace.SOUTH).getLocation().toString());
                                            case SOUTH ->
                                                    containerLocations.add(b.getRelative(BlockFace.EAST).getLocation().toString());
                                            case EAST ->
                                                    containerLocations.add(b.getRelative(BlockFace.NORTH).getLocation().toString());
                                            // NORTH
                                            default ->
                                                    containerLocations.add(b.getRelative(BlockFace.WEST).getLocation().toString());
                                        }
                                    }
                                }
                                containerLocations.add(blocation);
                                // get container contents
                                Inventory cinv = c.getInventory();
                                // make sure there is a free slot
                                if (cinv.firstEmpty() != -1) {
                                    ItemStack[] cc = cinv.getContents();
                                    List<Material> mats = new ArrayList<>();
                                    // find unique item stack materials
                                    for (ItemStack is : cc) {
                                        if (is != null) {
                                            Material m = is.getType();
                                            if (!mats.contains(m)) {
                                                mats.add(m);
                                            }
                                        }
                                    }
                                    // for each material found, see if there are any stacks of it in the drop chest
                                    mats.forEach((m) -> {
                                        int slot = inv.first(m);
                                        while (slot != -1 && cinv.firstEmpty() != -1) {
                                            // get the item stack
                                            ItemStack get = inv.getItem(slot);
                                            // remove the stack from the drop chest
                                            inv.setItem(slot, null);
                                            // put it in the container
                                            cinv.setItem(cinv.firstEmpty(), get);
                                            // sort the container
                                            SonicSorterListener.sortInventory(cinv);
                                            // get any other stacks
                                            slot = inv.first(m);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
            // put remaining item stacks into the leftover chest
            if (leftover != null) {
                ItemStack[] stacks = Arrays.stream(holder.getInventory().getContents())
                        .filter(Objects::nonNull)
                        .toArray(ItemStack[]::new);
                Container c = (Container) leftover.getState();
                HashMap<Integer, ItemStack> cannotFit = c.getInventory().addItem(stacks);
                holder.getInventory().clear();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    ItemStack[] remainingItems = cannotFit.values().toArray(new ItemStack[0]);
                    if (remainingItems.length > 0) {
                        holder.getInventory().addItem(remainingItems);
                    }
                }, 2L);
            }
        }
    }
}
