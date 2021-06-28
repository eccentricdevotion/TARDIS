/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.artron;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TardisArtronStorageCommand implements CommandExecutor {

    private final TardisPlugin plugin;
    private final List<String> firstArgs = new ArrayList<>();

    public TardisArtronStorageCommand(TardisPlugin plugin) {
        this.plugin = plugin;
        firstArgs.add("tardis");
        firstArgs.add("timelord");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisartron then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisartron")) {
            if (!TardisPermission.hasPermission(sender, "tardis.store")) {
                TardisMessage.send(sender, "NO_PERMS");
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TardisMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (!is.hasItemMeta()) {
                TardisMessage.send(player, "CELL_IN_HAND");
                return true;
            }
            if (is.getAmount() > 1) {
                TardisMessage.send(player, "CELL_ONE");
                return true;
            }
            ItemMeta im = is.getItemMeta();
            assert im != null;
            String name = im.getDisplayName();
            if (!name.equals("Artron Storage Cell")) {
                TardisMessage.send(player, "CELL_IN_HAND");
                return true;
            }
            String which = args[0].toLowerCase(Locale.ENGLISH);
            if (!firstArgs.contains(which)) {
                TardisMessage.send(player, "CELL_WHICH");
                return false;
            }
            // must be a timelord
            String playerUuid = player.getUniqueId().toString();
            int current_level;
            if (which.equals("tardis")) {
                ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                if (!rs.fromUuid(playerUuid)) {
                    TardisMessage.send(player, "NO_TARDIS");
                    return true;
                }
                current_level = rs.getArtronLevel();
            } else {
                ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, playerUuid);
                if (!rs.resultSet()) {
                    TardisMessage.send(player, "NO_TARDIS");
                    return true;
                }
                current_level = rs.getArtronLevel();
            }
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
                if (amount < 0) {
                    TardisMessage.send(player, "ENERGY_NOT_NEG");
                    return true;
                }
            } catch (NumberFormatException numberFormatException) {
                TardisMessage.send(player, "ARG_SEC_NUMBER");
                return false;
            }
            // must have sufficient energy
            if (which.equals("tardis")) {
                if (current_level - amount < plugin.getArtronConfig().getInt("comehere")) {
                    TardisMessage.send(player, "CELL_NO_TRANSFER");
                    return true;
                }
            } else if (current_level - amount < 0) {
                TardisMessage.send(player, "CELL_NOT_ENOUGH");
                return true;
            }
            List<String> lore = im.getLore();
            assert lore != null;
            int level = TardisNumberParsers.parseInt(lore.get(1));
            int new_amount = amount + level;
            int max = plugin.getArtronConfig().getInt("full_charge");
            if (new_amount > max) {
                TardisMessage.send(player, "CELL_NO_CHARGE", String.format("%d", (max - level)));
                return false;
            }
            lore.set(1, "" + new_amount);
            im.setLore(lore);
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.values());
            is.setItemMeta(im);
            // remove the energy from the tardis/timelord
            HashMap<String, Object> where = new HashMap<>();
            String table;
            if (which.equals("tardis")) {
                where.put("uuid", playerUuid);
                table = "tardis";
            } else {
                where.put("uuid", playerUuid);
                table = "player_prefs";
            }
            plugin.getQueryFactory().alterEnergyLevel(table, -amount, where, player);
            TardisMessage.send(player, "CELL_CHARGED", String.format("%d", new_amount));
            return true;
        }
        return false;
    }
}
