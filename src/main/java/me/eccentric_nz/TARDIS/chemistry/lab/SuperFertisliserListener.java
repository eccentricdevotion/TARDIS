package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SuperFertisliserListener implements Listener {

    private final TARDIS plugin;

    public SuperFertisliserListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    private final HashMap<Material, TreeType> TREE_LOOKUP = new HashMap<Material, TreeType>() {
        {
            put(Material.OAK_SAPLING, TreeType.TREE);
            put(Material.DARK_OAK_SAPLING, TreeType.DARK_OAK);
            put(Material.ACACIA_SAPLING, TreeType.ACACIA);
            put(Material.JUNGLE_SAPLING, TreeType.SMALL_JUNGLE);
            put(Material.SPRUCE_SAPLING, TreeType.REDWOOD);
            put(Material.BIRCH_SAPLING, TreeType.BIRCH);
            put(Material.CHORUS_FLOWER, TreeType.CHORUS_PLANT);
            put(Material.RED_MUSHROOM, TreeType.RED_MUSHROOM);
            put(Material.BROWN_MUSHROOM, TreeType.BROWN_MUSHROOM);
        }
    };
    private final List<Material> TREES = Arrays.asList(Material.OAK_SAPLING, Material.DARK_OAK_SAPLING, Material.ACACIA_SAPLING, Material.JUNGLE_SAPLING, Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING, Material.CHORUS_FLOWER, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM);

    @EventHandler(ignoreCancelled = true)
    public void onSuperFertilise(BlockFertilizeEvent event) {
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is != null && is.getType() == Material.BONE_MEAL && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Super Fertiliser") && is.getItemMeta().hasCustomModelData()) {
            event.setCancelled(true);
            Block block = event.getBlock();
            boolean removeItem = false;
            if (plugin.getPluginRespect().getRespect(block.getLocation(), new Parameters(player, FLAG.getNoMessageFlags()))) {
                switch (block.getType()) {
                    case PUMPKIN_STEM:
                    case MELON_STEM:
                    case CARROTS:
                    case WHEAT:
                    case POTATOES:
                    case BEETROOTS:
                    case SWEET_BERRY_BUSH:
                        Ageable ageable = (Ageable) block.getBlockData();
                        ageable.setAge(ageable.getMaximumAge());
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            block.setBlockData(ageable);
                        }, 3L);
                        removeItem = true;
                        break;
                    case BAMBOO_SAPLING:
                        Bamboo bamboo = (Bamboo) Material.BAMBOO.createBlockData();
                        bamboo.setAge(1);
                        bamboo.setStage(1);
                        block.setBlockData(bamboo);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            Block last = block;
                            for (int i = 1; i < 8; i++) {
                                Block b = block.getRelative(BlockFace.UP, i);
                                last = b;
                                if (b.getType().isAir()) {
                                    b.setBlockData(bamboo);
                                } else {
                                    break;
                                }
                            }
                            bamboo.setLeaves(Bamboo.Leaves.LARGE);
                            last.setBlockData(bamboo);
                        }, 3L);
                        removeItem = true;
                        break;
                    default:
                        break;
                }
            }
            if (removeItem) {
                int amount = is.getAmount() - 1;
                if (amount > 0) {
                    player.getInventory().getItemInMainHand().setAmount(amount);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
                player.updateInventory();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSuperFertiliseInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null && TREES.contains(block.getType())) {
            Player player = event.getPlayer();
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is != null && is.getType() == Material.BONE_MEAL && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Super Fertiliser") && is.getItemMeta().hasCustomModelData()) {
                event.setCancelled(true);
                TreeType treeType = TREE_LOOKUP.get(block.getType());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    block.setBlockData(TARDISConstants.AIR);
                    block.getWorld().generateTree(block.getLocation(), treeType);
                }, 3L);
                int amount = is.getAmount() - 1;
                if (amount > 0) {
                    player.getInventory().getItemInMainHand().setAmount(amount);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
                player.updateInventory();
            }
        }
    }
}
