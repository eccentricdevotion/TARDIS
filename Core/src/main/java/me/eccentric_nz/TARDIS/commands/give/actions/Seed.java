package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Seed {

    private final TARDIS plugin;

    public Seed(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, String[] args) {
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
            String type = args[2].toUpperCase(Locale.ENGLISH);
            String wall = "ORANGE_WOOL";
            String floor = "LIGHT_GRAY_WOOL";
            if (args.length > 4) {
                try {
                    wall = Material.valueOf(args[3].toUpperCase()).toString();
                    floor = Material.valueOf(args[4].toUpperCase()).toString();
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "SEED_MAT_NOT_VALID");
                    return;
                }
            }
            if (Consoles.getBY_NAMES().containsKey(type)) {
                Schematic schm = Consoles.getBY_NAMES().get(type);
                ItemStack is;
                int model = 10001;
                if (schm.isCustom()) {
                    is = new ItemStack(schm.getSeedMaterial(), 1);
                } else {
                    try {
                        TARDISDisplayItem tdi = TARDISDisplayItem.valueOf(type);
                        is = new ItemStack(tdi.getMaterial(), 1);
                        model = tdi.getCustomModelData();
                    } catch (IllegalArgumentException e) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SEED_NOT_VALID");
                        return;
                    }
                }
                ItemMeta im = is.getItemMeta();
                im.setCustomModelData(model);
                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
                // set display name
                im.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
                List<String> lore = new ArrayList<>();
                lore.add(type);
                lore.add("Walls: " + wall);
                lore.add("Floors: " + floor);
                lore.add("Chameleon: FACTORY");
                im.setLore(lore);
                is.setItemMeta(im);
                player.getInventory().addItem(is);
                player.updateInventory();
                plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_ITEM", sender.getName(), "a " + type + " seed block");
            }
        }
    }
}
