package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISDevCircuitCommand {

    private final TARDIS plugin;
    private final List<String> circuits = new ArrayList<>();

    public TARDISDevCircuitCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (DiskCircuit dc : DiskCircuit.values()) {
            if (dc.getMaterial() == Material.GLOWSTONE_DUST) {
                circuits.add(dc.getName());
            }
        }
    }

    public boolean give(CommandSender sender) {
        if (sender instanceof Player player) {
            // get a random circuit
            String c = circuits.get(TARDISConstants.RANDOM.nextInt(circuits.size()));
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(c);
            ItemStack result = recipe.getResult();
            // set the second line of lore
            ItemMeta im = result.getItemMeta();
            List<String> lore = im.getLore();
            String uses = ChatColor.YELLOW + "" + (plugin.getConfig().getInt("circuits.uses.invisibility") - 3);
            lore.set(1, uses);
            im.setLore(lore);
            result.setItemMeta(im);
            player.getInventory().addItem(result);
        }
        return true;
    }
}
