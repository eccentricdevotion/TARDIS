package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
    private final List<String> brownBlockNames = Arrays.asList("", "The Moment", "Siege Cube", "Police Box Blue", "Police Box White", "Police Box Orange", "Police Box Magenta", "Police Box Light Blue", "Police Box Yellow", "Police Box Lime", "Police Box Pink", "Police Box Gray", "Police Box Light Gray", "Police Box Cyan", "Police Box Purple", "Police Box Brown", "Police Box Green", "Police Box Red", "Police Box Black", "Police Box Blue South", "Police Box White South", "Police Box Orange South", "Police Box Magenta South", "Police Box Light Blue South", "Police Box Yellow South", "Police Box Lime South", "Police Box Pink South", "Police Box Gray South", "Police Box Light Gray South", "Police Box Cyan South", "Police Box Purple South", "Police Box Brown South", "Police Box Green South", "Police Box Red South", "Police Box Black South", "Police Box Blue West", "Police Box White West", "Police Box Orange West", "Police Box Magenta West", "Police Box Light Blue West", "Police Box Yellow West", "Police Box Lime West", "Police Box Pink West", "Police Box Gray West", "Police Box Light Gray West", "Police Box Cyan West", "Police Box Purple West", "Police Box Brown West", "Police Box Green West", "Police Box Red West", "Police Box Black West", "Police Box Blue North", "Police Box White North", "Police Box Orange North");
    private final List<String> brownSubs = Arrays.asList("", "the_moment", "siege_cube", "tardis_blue", "tardis_white", "tardis_orange", "tardis_magenta", "tardis_light_blue", "tardis_yellow", "tardis_lime", "tardis_pink", "tardis_gray", "tardis_light_gray", "tardis_cyan", "tardis_purple", "tardis_brown", "tardis_green", "tardis_red", "tardis_black", "tardis_blue_south", "tardis_white_south", "tardis_orange_south", "tardis_magenta_south", "tardis_light_blue_south", "tardis_yellow_south", "tardis_lime_south", "tardis_pink_south", "tardis_gray_south", "tardis_light_gray_south", "tardis_cyan_south", "tardis_purple_south", "tardis_brown_south", "tardis_green_south", "tardis_red_south", "tardis_black_south", "tardis_blue_west", "tardis_white_west", "tardis_orange_west", "tardis_magenta_west", "tardis_light_blue_west", "tardis_yellow_west", "tardis_lime_west", "tardis_pink_west", "tardis_gray_west", "tardis_light_gray_west", "tardis_cyan_west", "tardis_purple_west", "tardis_brown_west", "tardis_green_west", "tardis_red_west", "tardis_black_west", "tardis_blue_north", "tardis_white_north", "tardis_orange_north");
    private final List<String> redBlockNames = Arrays.asList("", "Police Box Magenta North", "Police Box Light Blue North", "Police Box Yellow North", "Police Box Lime North", "Police Box Pink North", "Police Box Gray North", "Police Box Light Gray North", "Police Box Cyan North", "Police Box Purple North", "Police Box Brown North", "Police Box Green North", "Police Box Red North", "Police Box Black North", "Ars", "Bigger", "Budget", "Coral", "Deluxe", "Eleventh", "Ender", "Plank", "Pyramid", "Redstone", "Steampunk", "Thirteenth", "Factory", "Tom", "Twelfth", "War", "Small", "Medium", "Tall", "Legacy Bigger", "Legacy Budget", "Legacy Deluxe", "Legacy Eleventh", "Legacy Redstone", "Pandorica");
    private final List<String> redSubs = Arrays.asList("", "tardis_magenta_north", "tardis_light_blue_north", "tardis_yellow_north", "tardis_lime_north", "tardis_pink_north", "tardis_gray_north", "tardis_light_gray_north", "tardis_cyan_north", "tardis_purple_north", "tardis_brown_north", "tardis_green_north", "tardis_red_north", "tardis_black_north", "ars", "bigger", "budget", "coral", "deluxe", "eleventh", "ender", "plank", "pyramid", "redstone", "steampunk", "thirteenth", "factory", "tom", "twelfth", "war", "small", "medium", "tall", "legacy_bigger", "legacy_budget", "legacy_deluxe", "legacy_eleventh", "legacy_redstone", "pandorica");
    private final NamespacedKey nsk;

    public TARDISMushroomCommand(TARDIS plugin) {
        this.plugin = plugin;
        nsk = new NamespacedKey(this.plugin, "customBlock");
    }

    public boolean give(CommandSender sender, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
            int which;
            Material mushroom;
            String displayName;
            if (redSubs.contains(args[1])) {
                which = redSubs.indexOf(args[1]);
                mushroom = Material.RED_MUSHROOM_BLOCK;
                displayName = redBlockNames.get(which);
            } else if (brownSubs.contains(args[1])) {
                which = brownSubs.indexOf(args[1]);
                mushroom = Material.BROWN_MUSHROOM_BLOCK;
                displayName = brownBlockNames.get(which);
            } else {
                TARDISMessage.message(sender, "Invalid TARDIS mushroom block state!");
                return true;
            }
            if (which != 0) {
                ItemStack is = new ItemStack(mushroom, 16);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(displayName);
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
