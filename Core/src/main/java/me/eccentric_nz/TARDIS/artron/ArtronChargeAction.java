package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class ArtronChargeAction {

    private final TARDIS plugin;

    public ArtronChargeAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void add(Player player, Material item, Material full, Location location, int current_level, int id) {
        int amount = 0;
        int fc = plugin.getArtronConfig().getInt("full_charge");
        if (item.equals(full)) {
            if (plugin.getConfig().getBoolean("preferences.no_creative_condense")) {
                switch (plugin.getWorldManager()) {
                    case MULTIVERSE -> {
                        if (!plugin.getMVHelper().isWorldSurvival(location.getWorld())) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARTRON_FULL_CREATIVE");
                            return;
                        }
                    }
                    case NONE -> {
                        if (plugin.getPlanetsConfig().getString("planets." + location.getWorld().getName() + ".gamemode").equalsIgnoreCase("CREATIVE")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARTRON_FULL_CREATIVE");
                            return;
                        }
                    }
                }
            }
            // remove the NETHER_STAR! (if appropriate)
            int a = player.getInventory().getItemInMainHand().getAmount();
            int a2 = a - 1;
            if (current_level < fc) {
                // There's room in the tank!
                amount = fc;
                if (a2 > 0) {
                    player.getInventory().getItemInMainHand().setAmount(a2);
                } else {
                    player.getInventory().removeItem(new ItemStack(full, 1));
                }
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_AT_MAX");
            } else {
                // We're either full or exceeding maximum, so don't do anything!
                amount = current_level;
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_MAX");
            }
        } else {
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                String name = im.getDisplayName();
                if (!name.endsWith("Artron Storage Cell")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NOT_VALID");
                    return;
                }
                List<String> lore = im.getLore();
                int charge = TARDISNumberParsers.parseInt(lore.get(1));
                if (charge <= 0) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NOT_CHARGED");
                    return;
                }
                int max;
                ResultSetArtronStorage rsas = new ResultSetArtronStorage(plugin);
                if (rsas.fromID(id)) {
                    int damage = (fc / 2) * rsas.getDamageCount();
                    max = (fc * rsas.getCapacitorCount()) - damage;
                } else {
                    return;
                }
                charge *= is.getAmount();
                amount = current_level + charge;
                // if only one cell
                if (is.getAmount() == 1 && amount > max) {
                    if (current_level >= max) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_TRANSFER", max);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_ADD");
                        return;
                    }
                    // just take as much as we can
                    amount = max;
                    int remove = max - current_level;
                    int set = charge - remove;
                    lore.set(1, "" + set);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    if (set < 1) {
                        is.getEnchantments().keySet().forEach(is::removeEnchantment);
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_TRANSFER");
                } else {
                    // only add energy up to capacitors * max level - damage
                    if (amount <= max) {
                        lore.set(1, "0");
                        im.setLore(lore);
                        is.setItemMeta(im);
                        is.getEnchantments().keySet().forEach(is::removeEnchantment);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_TRANSFER");
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_TRANSFER", max);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_ADD");
                        // give items back
                        return;
                    }
                }
            }
        }
        // update charge
        HashMap<String, Object> set = new HashMap<>();
        set.put("artron_level", amount);
        HashMap<String, Object> whereid = new HashMap<>();
        whereid.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", set, whereid);
    }
}
