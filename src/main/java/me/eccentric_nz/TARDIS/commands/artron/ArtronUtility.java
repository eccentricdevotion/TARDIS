package me.eccentric_nz.TARDIS.commands.artron;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtronUtility {

    public static ItemStack hasCell(TARDIS plugin, Player player) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!is.hasItemMeta()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
            return null;
        }
        if (is.getAmount() > 1) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_ONE");
            return null;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !ComponentUtils.endsWith(im.displayName(), "Artron Storage Cell")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
            return null;
        }
        return is;
    }

    public static int getLevel(ItemMeta im) {
        String lore = ComponentUtils.stripColour(im.lore().get(1));
        return TARDISNumberParsers.parseInt(lore);
    }

    public static void setLevel(ItemStack is, ItemMeta im, int level, Player player, boolean main) {
        List<Component> lore = im.lore();
        lore.set(1, Component.text(level));
        im.lore(lore);
        // add glint if missing
        if (main && !im.hasEnchantmentGlintOverride()) {
            im.removeEnchant(Enchantment.UNBREAKING);
            im.setEnchantmentGlintOverride(true);
        }
        if (main) {
            is.setItemMeta(im);
            player.getInventory().setItemInMainHand(is);
        } else {
            // remove enchant if level <= 0
            if (level <= 0) {
                is.getEnchantments().keySet().forEach(is::removeEnchantment);
                im.setEnchantmentGlintOverride(null);
                is.setItemMeta(im);
            }
            player.getInventory().setItemInOffHand(is);
        }
    }

    public static void chargeCell(TARDIS plugin, ItemStack is, Player player, int amount, String table) {
        ItemMeta im = is.getItemMeta();
        List<Component> lore = im.lore();
        int level = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
        if (level < 0) {
            level = 0;
        }
        int max = plugin.getArtronConfig().getInt("full_charge");
        int new_amount = amount + level;
        if (new_amount > max) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NO_CHARGE", String.format("%d", (max - level)));
            return;
        }
        lore.set(1, Component.text(new_amount));
        im.lore(lore);
        im.setEnchantmentGlintOverride(true);
        im.addItemFlags(ItemFlag.values());
        im.setAttributeModifiers(Multimaps.forMap(Map.of()));
        is.setItemMeta(im);
        // remove the energy from the tardis/timelord
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().alterEnergyLevel(table, -amount, where, player);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_CHARGED", String.format("%d", new_amount));
    }
}
