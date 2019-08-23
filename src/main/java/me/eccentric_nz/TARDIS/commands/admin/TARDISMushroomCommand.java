package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class TARDISMushroomCommand {

    private final TARDIS plugin;
    private final List<String> blockNames = Arrays.asList("", "The Moment", "Siege Cube", "Police Box");
    private final NamespacedKey nsk;

    public TARDISMushroomCommand(TARDIS plugin) {
        this.plugin = plugin;
        nsk = new NamespacedKey(this.plugin, "customBlock");
    }

    public boolean give(CommandSender sender, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
            int which = TARDISNumberParsers.parseInt(args[1]);
            if (which != 0) {
                ItemStack is = new ItemStack(Material.BROWN_MUSHROOM_BLOCK, 16);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(blockNames.get(which));
                im.getPersistentDataContainer().set(nsk, PersistentDataType.INTEGER, which);
                im.setCustomModelData(10000000 + which);
                is.setItemMeta(im);
                player.getInventory().addItem(is);
                player.updateInventory();
            }
        } else {
            TARDISMessage.send(sender, "CMD_PLAYER");
        }
        return true;
    }
}
