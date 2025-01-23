package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Kit {

    private final TARDIS plugin;

    public Kit(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(String item, Player player) {
        ItemStack result;
        if (plugin.getIncomposita().getShapelessRecipes().containsKey(item)) {
            ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item);
            result = recipe.getResult();
        } else {
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item);
            result = recipe.getResult();
            if (result.hasItemMeta()) {
                ItemMeta im = result.getItemMeta();
                if (im.hasDisplayName() && (im.getDisplayName().contains("Key") || im.getDisplayName().contains("Authorised Control Disk"))) {
                    im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                    if (im.hasLore()) {
                        List<String> lore = im.getLore();
                        String format = NamedTextColor.AQUA + "" + TextDecoration.ITALIC;
                        String what = im.getDisplayName().contains("Key") ? "key" : "disk";
                        lore.add(format + "This " + what + " belongs to");
                        lore.add(format + player.getName());
                        im.lore(lore);
                    }
                    result.setItemMeta(im);
                }
            }
        }
        result.setAmount(1);
        player.getInventory().addItem(result);
        player.updateInventory();
    }
}
