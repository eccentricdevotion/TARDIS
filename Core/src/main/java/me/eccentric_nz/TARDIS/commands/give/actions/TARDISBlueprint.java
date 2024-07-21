package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TARDISBlueprint {

    private final TARDIS plugin;

    public TARDISBlueprint(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, String[] args, String blueprint) {
        Player player = null;
        if (args[0].equals("@s") && sender instanceof Player) {
            player = (Player) sender;
        } else if (args[0].equals("@p")) {
            List<Entity> near = Bukkit.selectEntities(sender, "@p");
            if (!near.isEmpty() && near.get(0) instanceof Player) {
                player = (Player) near.get(0);
                if (player == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_NEARBY_PLAYER");
                    return;
                }
            }
        } else {
            player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                return;
            }
        }
        if (player != null) {
            ItemStack bp = plugin.getTardisAPI().getTARDISBlueprintItem(blueprint, player);
            player.getInventory().addItem(bp);
            player.updateInventory();
            plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_ITEM", sender.getName(), "a TARDIS Blueprint Disk");
        }
    }
}
