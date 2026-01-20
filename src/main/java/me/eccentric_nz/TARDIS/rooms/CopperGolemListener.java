package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.SmelterChest;
import me.eccentric_nz.TARDIS.rooms.library.LibrarySorter;
import me.eccentric_nz.TARDIS.rooms.smelter.SmelterDrop;
import me.eccentric_nz.TARDIS.rooms.smelter.SmelterFuel;
import me.eccentric_nz.TARDIS.rooms.smelter.SmelterOre;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.CopperGolem;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CopperGolemListener implements Listener {

    private final TARDIS plugin;
    private final List<String> zero;

    public CopperGolemListener(TARDIS plugin) {
        this.plugin = plugin;
        zero = this.plugin.getBlocksConfig().getStringList("no_artron_value");
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        if (!block.getWorld().getName().contains("TARDIS")) {
            return;
        }
        if (!(block.getState() instanceof Chest chest)) {
            return;
        }
        Inventory inv = chest.getInventory();
        if (!inv.getViewers().isEmpty()) {
            return;
        }
        ItemStack[] contents = inv.getContents();
        // is it a condenser chest?
        Location loc = block.getLocation();
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", 34);
        where.put("location", loc.toString());
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet()) {
            if (plugin.getConfig().getBoolean("preferences.no_creative_condense")) {
                switch (plugin.getWorldManager()) {
                    case MULTIVERSE -> {
                        if (!plugin.getMVHelper().isWorldSurvival(block.getWorld())) {
                            return;
                        }
                    }
                    case NONE -> {
                        if (plugin.getPlanetsConfig().getString("planets." + block.getWorld().getName() + ".gamemode", "SURVIVAL").equalsIgnoreCase("CREATIVE")) {
                            return;
                        }
                    }
                }
            }
            // check for copper golems
            if (golemIsNear(block)) {
                ArrayList<ItemStack> returnedItems = new ArrayList<>();
                HashMap<String, Integer> item_counts = new HashMap<>();
                int amount = 0;
                // process chest contents
                for (ItemStack is : contents) {
                    // skip empty slots
                    if (is == null) {
                        continue;
                    }
                    String item = is.getType().toString();
                    if (plugin.getCondensables().containsKey(item) && !zero.contains(item)) {
                        int stack_size = is.getAmount();
                        // add item artron value
                        amount += stack_size * plugin.getCondensables().get(item);
                        // count blocks towards room growth and repair if enabled
                        String block_data = is.getType().toString();
                        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks") || plugin.getConfig().getBoolean("allow.repair")) {
                            if (item_counts.containsKey(block_data)) {
                                Integer add_this = (item_counts.get(block_data) + stack_size);
                                item_counts.put(block_data, add_this);
                            } else {
                                item_counts.put(block_data, stack_size);
                            }
                        }
                        plugin.debug("Golem condense " + is.getType());
                        continue;
                    }
                    // not condensable
                    returnedItems.add(is);
                }
                int id = rsc.getTardis_id();
                // halve it cause 1:1 is too much...
                amount = Math.round(amount / 2.0F);
                // check the artron capacitor is not at maximum
                ResultSetArtronStorageAndLevel rsas = new ResultSetArtronStorageAndLevel(plugin);
                int full = plugin.getArtronConfig().getInt("full_charge", 5000);
                if (rsas.fromID(id)) {
                    int damage = (full / 2) * rsas.getDamageCount();
                    int max = (full * rsas.getCapacitorCount()) - damage;
                    int current = rsas.getCurrentLevel();
                    if (current + amount > max) {
                        return;
                    } else {
                        // clear chest contents
                        chest.getInventory().clear();
                        // process item_counts
                        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks") || plugin.getConfig().getBoolean("allow.repair")) {
                            item_counts.forEach((key, value) -> {
                                // check if the tardis has condensed this material before
                                HashMap<String, Object> wherec = new HashMap<>();
                                wherec.put("tardis_id", id);
                                wherec.put("block_data", key);
                                ResultSetCondenser rscon = new ResultSetCondenser(plugin, wherec);
                                HashMap<String, Object> setc = new HashMap<>();
                                if (rscon.resultSet()) {
                                    int new_stack_size = value + rscon.getBlock_count();
                                    plugin.getQueryFactory().updateCondensedBlockCount(new_stack_size, id, key);
                                } else {
                                    setc.put("tardis_id", id);
                                    setc.put("block_data", key);
                                    setc.put("block_count", value);
                                    plugin.getQueryFactory().doInsert("condenser", setc);
                                }
                            });
                        }
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wheret, null);
                        // add back non-condensables
                        for (ItemStack ret : returnedItems) {
                            chest.getInventory().addItem(ret);
                        }
                    }
                }
            }
        } else {
            // is it a drop chest?
            ResultSetVault rsv = new ResultSetVault(plugin);
            if (!rsv.fromLocation(loc.toString())) {
                return;
            }
            SmelterChest type = rsv.getChestType();
            List<Chest> fuelChests = null;
            List<Chest> oreChests = null;
            if (type == SmelterChest.FUEL || type == SmelterChest.SMELT) {
                ResultSetSmelter rss = new ResultSetSmelter(plugin, loc.toString());
                if (!rss.resultSet()) {
                    return;
                }
                fuelChests = rss.getFuelChests();
                oreChests = rss.getOreChests();
            }
            switch (type) {
                case DROP -> {
                    // process vault items
                    new VaultDrop(plugin).processItems(inv, rsv);
                }
                case FUEL -> {
                    // add fuel to furnaces
                    if (fuelChests != null) {
                        new SmelterFuel().processItems(inv, fuelChests);
                    }
                }
                case SMELT -> {
                    // add items to furnaces
                    if (oreChests != null) {
                        new SmelterOre().processItems(inv, oreChests);
                    }
                }
                case LIBRARY -> new LibrarySorter(plugin).distribute(inv, loc.add(-8,-4,-8)); // sort books into shelves
                default -> { } // do nothing
            }
        }
    }

    private boolean golemIsNear(Block block) {
        boolean golemIsNear = false;
        for (Entity entity : block.getWorld().getNearbyEntities(block.getBoundingBox().expand(1))) {
            if (entity instanceof CopperGolem) {
                golemIsNear = true;
                break;
            }
        }
        return golemIsNear;
    }
}
