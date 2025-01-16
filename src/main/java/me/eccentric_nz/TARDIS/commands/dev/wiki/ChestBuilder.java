package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.Damageable;

public class ChestBuilder {

    private final TARDIS plugin;

    public ChestBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean place(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        // fill chests with every TARDIS item
        Location location = player.getLocation().add(0, 2, 0);
        int shapedChests = (plugin.getFigura().getShapedRecipes().size() / 27) + 1;
        int shlessChests = (plugin.getIncomposita().getShapelessRecipes().size() / 27) + 1;
        // place some chests
        for (int i = 0; i < shapedChests; i++) {
            location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
        }
        for (int i = 1; i <= shlessChests; i++) {
            location.getBlock().getRelative(BlockFace.WEST, i).setType(Material.CHEST);
        }
        int count = 0;
        int chestNum = 0;
        Chest chest = (Chest) location.getBlock().getState();
        ItemStack is;
        for (ShapedRecipe s : plugin.getFigura().getShapedRecipes().values()) {
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            is = s.getResult();
            is.setAmount(1);
            if (is.getItemMeta() instanceof Damageable damageable) {
                damageable.setDamage(0);
                damageable.setUnbreakable(true);
                damageable.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
                is.setItemMeta(damageable);
            }
            chest.getBlockInventory().addItem(is);
            count++;
        }
        count = 0;
        chestNum = 0;
        chest = (Chest) location.getBlock().getRelative(BlockFace.WEST).getState();
        for (ShapelessRecipe s : plugin.getIncomposita().getShapelessRecipes().values()) {
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.WEST, chestNum).getState();
            }
            is = s.getResult();
            is.setAmount(1);
            if (is.getItemMeta() instanceof Damageable damageable) {
                damageable.setDamage(0);
                damageable.setUnbreakable(true);
                damageable.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
                is.setItemMeta(damageable);
            }
            chest.getBlockInventory().addItem(is);
            count++;
        }
        return true;
    }
}
