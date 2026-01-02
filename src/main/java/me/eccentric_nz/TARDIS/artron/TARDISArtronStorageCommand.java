/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.artron;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISArtronStorageCommand implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<>();

    public TARDISArtronStorageCommand(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("tardis");
        firstArgs.add("timelord");
        firstArgs.add("combine");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisartron then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisartron")) {
            if (!TARDISPermission.hasPermission(sender, "tardis.store")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                return true;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                return true;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (!is.hasItemMeta()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                return true;
            }
            if (is.getAmount() > 1) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_ONE");
                return true;
            }
            ItemMeta im = is.getItemMeta();
            if (!im.hasDisplayName() || !ComponentUtils.endsWith(im.displayName(), "Artron Storage Cell")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                return true;
            }
            String which = args[0].toLowerCase(Locale.ROOT);
            if (!firstArgs.contains(which)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_WHICH");
                return false;
            }
            int max = plugin.getArtronConfig().getInt("full_charge");
            if (args.length == 1 && args[0].equalsIgnoreCase("combine")) {
                ItemStack offhand = player.getInventory().getItemInOffHand();
                if (!offhand.hasItemMeta()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                    return true;
                }
                if (is.getAmount() > 1) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_ONE");
                    return true;
                }
                ItemMeta offMeta = offhand.getItemMeta();
                if (!offMeta.hasDisplayName() || !ComponentUtils.endsWith(offMeta.displayName(), "Artron Storage Cell")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                    return true;
                }
                // get the artron levels of each storage cell
                int mainLevel = getLevel(im);
                int offLevel = getLevel(offMeta);
                if (mainLevel <= 0 || offLevel <= 0) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NOT_CHARGED");
                    return true;
                }
                int combined = mainLevel + offLevel;
                int remainder = 0;
                if (combined > max) {
                    remainder = combined - max;
                    combined = max;
                }
                setLevel(is, im, combined, player, true);
                setLevel(offhand, offMeta, remainder, player, false);
            } else {
                if (args.length < 2) {
                    return false;
                }
                String playerUUID = player.getUniqueId().toString();
                int current_level;
                if (which.equals("tardis")) {
                    ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                    if (!rs.fromUUID(playerUUID)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                        return true;
                    }
                    current_level = rs.getArtronLevel();
                } else {
                    ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, playerUUID);
                    if (!rs.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                        return true;
                    }
                    current_level = rs.getArtronLevel();
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                    if (amount < 0) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NOT_NEG");
                        return true;
                    }
                } catch (NumberFormatException n) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_SEC_NUMBER");
                    return false;
                }
                // must have sufficient energy
                if (which.equals("tardis")) {
                    if (current_level - amount < plugin.getArtronConfig().getInt("comehere")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NO_TRANSFER");
                        return true;
                    }
                } else if (current_level - amount < 0) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NOT_ENOUGH");
                    return true;
                }
                List<Component> lore = im.lore();
                int level = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
                if (level < 0) {
                    level = 0;
                }
                int new_amount = amount + level;
                if (new_amount > max) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NO_CHARGE", String.format("%d", (max - level)));
                    return false;
                }
                lore.set(1, Component.text(new_amount));
                im.lore(lore);
                im.setEnchantmentGlintOverride(true);
                im.addItemFlags(ItemFlag.values());
                im.setAttributeModifiers(Multimaps.forMap(Map.of()));
                is.setItemMeta(im);
                // remove the energy from the tardis/timelord
                HashMap<String, Object> where = new HashMap<>();
                String table;
                if (which.equals("tardis")) {
                    where.put("uuid", playerUUID);
                    table = "tardis";
                } else {
                    where.put("uuid", playerUUID);
                    table = "player_prefs";
                }
                plugin.getQueryFactory().alterEnergyLevel(table, -amount, where, player);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_CHARGED", String.format("%d", new_amount));
            }
            return true;
        }
        return false;
    }

    private int getLevel(ItemMeta im) {
        String lore = ComponentUtils.stripColour(im.lore().get(1));
        return TARDISNumberParsers.parseInt(lore);
    }

    private void setLevel(ItemStack is, ItemMeta im, int level, Player player, boolean main) {
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
}
