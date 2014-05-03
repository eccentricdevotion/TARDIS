package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISFarmBlockListener implements Listener {

    public final TARDIS plugin;
    // seeds
    private final Material air = Material.AIR;
    private final Material ci = Material.CARROT_ITEM;
    private final Material is = Material.INK_SACK;
    private final Material ms = Material.MELON_SEEDS;
    private final Material nw = Material.NETHER_WARTS;
    private final Material pi = Material.POTATO_ITEM;
    private final Material ps = Material.PUMPKIN_SEEDS;
    private final Material sc = Material.SUGAR_CANE;
    private final Material ss = Material.SEEDS;
    private final Material sonic;

    public TARDISFarmBlockListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        this.sonic = Material.valueOf(split[0]);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlantHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("tardis.sonic.plant")) {
            return;
        }
        ItemStack stack = player.getItemInHand();
        if (stack.getType().equals(sonic) && stack.hasItemMeta()) {
            ItemMeta im = player.getItemInHand().getItemMeta();
            if (im.hasDisplayName() && ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver") && im.hasLore() && im.getLore().contains("Emerald Upgrade")) {
                Block block = event.getBlock();
                Material type = block.getType();
                Byte data = block.getData();
                switch (block.getType()) {
                    case CARROT:
                        if (data == 7 && player.getInventory().contains(ci)) {
                            processHarvest(player, ci, block);
                        }
                        break;
                    case COCOA:
                        if (data == 2 && player.getInventory().contains(is)) {
                            processHarvest(player, is, block);
                        }
                        break;
                    case CROPS:
                        if (data == 7 && player.getInventory().contains(ss)) {
                            processHarvest(player, ss, block);
                        }
                        break;
                    case MELON_STEM:
                        if (data == 7 && player.getInventory().contains(ms)) {
                            processHarvest(player, ms, block);
                        }
                        break;
                    case NETHER_STALK:
                        if (data == 2 && player.getInventory().contains(nw)) {
                            processHarvest(player, nw, block);
                        }
                        break;
                    case POTATO:
                        if (data == 7 && player.getInventory().contains(pi)) {
                            processHarvest(player, pi, block);
                        }
                        break;
                    case PUMPKIN_STEM:
                        if (data == 7 && player.getInventory().contains(ps)) {
                            processHarvest(player, ps, block);
                        }
                        break;
                    case SUGAR_CANE_BLOCK:
                        if (player.getInventory().contains(sc)) {
                            processHarvest(player, sc, block);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void processHarvest(Player p, Material m, Block b) {
        int slot = p.getInventory().first(m);
        if (slot >= 0) {
            ItemStack next = p.getInventory().getItem(slot);
            if (next.getAmount() > 1) {
                next.setAmount(next.getAmount() - 1);
                p.getInventory().setItem(slot, next);
            } else {
                p.getInventory().setItem(slot, new ItemStack(air));
            }
            Runnable tsr = new TARDISSonicReplant(plugin, b, m);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, tsr, 20);
        }
    }
}
