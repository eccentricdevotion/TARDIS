package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetVault;
import me.eccentric_nz.TARDIS.enumeration.SmelterChest;
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
                        inv.remove(is);
                        continue;
                    }
                    plugin.debug("Golem condense " + is.getType());
                }
                // clear chest contents
                chest.getInventory().clear();
            }
        } else {
            // is it a drop chest?
            ResultSetVault rs = new ResultSetVault(plugin);
            if (!rs.fromLocation(loc.toString())) {
                return;
            }
            SmelterChest type = rs.getChestType();
            switch (type) {
                case DROP -> {
                    // process vault items
                }
                case FUEL -> {
                    // add fuel to furnaces
                }
                case SMELT -> {
                    // add items to furnaces
                }
                case LIBRARY -> {
                    // sort books into shelves
                }
                default -> {
                    // do nothing
                }
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
