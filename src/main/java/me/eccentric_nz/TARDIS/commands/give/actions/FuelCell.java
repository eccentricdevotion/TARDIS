package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FuelCell {

    private final TARDIS plugin;

    public FuelCell(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, int amount, Player player) {
        if (amount > 64) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_MAX");
            return;
        }
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get("Artron Storage Cell");
        ItemStack result = recipe.getResult();
        result.setAmount(amount);
        // add lore and enchantment
        ItemMeta im = result.getItemMeta();
        List<String> lore = im.getLore();
        int max = plugin.getArtronConfig().getInt("full_charge");
        lore.set(1, "" + max);
        im.setLore(lore);
        im.addEnchant(Enchantment.UNBREAKING, 1, true);
        im.addItemFlags(ItemFlag.values());
        result.setItemMeta(im);
        player.getInventory().addItem(result);
        player.updateInventory();
        plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_ITEM", sender.getName(), amount + " Full Artron Storage Cell");
    }
}
