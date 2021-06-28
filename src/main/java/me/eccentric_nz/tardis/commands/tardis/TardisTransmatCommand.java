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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.TardisBoundTransmatRemoval;
import me.eccentric_nz.tardis.database.data.Transmat;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

class TardisTransmatCommand {

    private final TardisPlugin plugin;

    TardisTransmatCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean teleportOrProcess(Player player, String[] args) {
        if (args.length < 2) {
            TardisMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        if (!TardisPermission.hasPermission(player, "tardis.transmat")) {
            TardisMessage.send(player, "NO_PERMS");
            return true;
        }
        // must be a time lord
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (!rs.fromUuid(player.getUniqueId().toString())) {
            TardisMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        // player is in TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TardisMessage.send(player, "NOT_IN_TARDIS");
            return false;
        }
        int id = rs.getTardisId();
        int thisid = rst.getTardisId();
        if (thisid != id) {
            TardisMessage.send(player, "CMD_ONLY_TL");
            return false;
        }
        if (args[1].equalsIgnoreCase("list")) {
            ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
            if (rslist.resultSet()) {
                TardisMessage.send(player, "TRANSMAT_LIST");
                for (Transmat t : rslist.getData()) {
                    TextComponent tcg = new TextComponent(t.getName());
                    tcg.setColor(ChatColor.GREEN);
                    TextComponent tcl = new TextComponent(String.format("X: %.2f, Y: %.2f, Z: %.2f, Yaw %.2f", t.getX(), t.getY(), t.getZ(), t.getYaw()));
                    TextComponent tce = new TextComponent(" < Transmat > ");
                    tce.setColor(ChatColor.AQUA);
                    tce.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Transmat to this location")));
                    tce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis transmat tp " + t.getName()));
                    tcg.addExtra(tcl);
                    tcg.addExtra(tce);
                    player.spigot().sendMessage(tcg);
                }
            } else {
                TardisMessage.send(player, "TRANSMAT_NO_LIST");
            }
            return true;
        }
        if (args.length < 3) {
            TardisMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        if (args[1].equalsIgnoreCase("tp")) {
            // transmat to specified location
            if (args[2].equalsIgnoreCase("console")) {
                // get internal door location
                plugin.getGeneralKeeper().getRendererListener().transmat(player);
            } else {
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    TardisMessage.send(player, "TRANSMAT");
                    Location tp_loc = rsm.getLocation();
                    tp_loc.setYaw(rsm.getYaw());
                    tp_loc.setPitch(player.getLocation().getPitch());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.teleport(tp_loc);
                    }, 10L);
                } else {
                    TardisMessage.send(player, "TRANSMAT_NOT_FOUND");
                }
            }
            return true;
        } else {
            Location location = player.getLocation();
            if (args[1].equalsIgnoreCase("add")) {
                // must be in their TARDIS
                if (!plugin.getUtils().inTardisWorld(location)) {
                    TardisMessage.send(player, "CMD_IN_WORLD");
                    return true;
                }
                // get the transmat name
                if (!args[2].matches("[A-Za-z0-9_]{2,16}")) {
                    TardisMessage.send(player, "SAVE_NAME_NOT_VALID");
                    return true;
                }
                // check if transmat name exists
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    TardisMessage.send(player, "TRANSMAT_EXISTS");
                    return true;
                } else {
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("tardis_id", id);
                    set.put("name", args[2]);
                    set.put("world", Objects.requireNonNull(location.getWorld()).getName());
                    set.put("x", location.getX());
                    set.put("y", location.getY());
                    set.put("z", location.getZ());
                    set.put("yaw", location.getYaw());
                    plugin.getQueryFactory().doInsert("transmats", set);
                    TardisMessage.send(player, "TRANSMAT_SAVED");
                }
                return true;
            } else if (args[1].equalsIgnoreCase("update")) {
                // check if transmat name exists
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("world", Objects.requireNonNull(location.getWorld()).getName());
                    set.put("x", location.getX());
                    set.put("y", location.getY());
                    set.put("z", location.getZ());
                    set.put("yaw", location.getYaw());
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    where.put("name", args[2]);
                    plugin.getQueryFactory().doUpdate("transmats", set, where);
                    TardisMessage.send(player, "TRANSMAT_SAVED");
                } else {
                    TardisMessage.send(player, "TRANSMAT_NOT_FOUND");
                }
                return true;
            } else if (args[1].equalsIgnoreCase("remove")) {
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    HashMap<String, Object> wherer = new HashMap<>();
                    wherer.put("transmat_id", rsm.getTransmatId());
                    plugin.getQueryFactory().doDelete("transmats", wherer);
                    // check for bound transmat
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    whered.put("name", args[2]);
                    ResultSetBind rsd = new ResultSetBind(plugin, whered);
                    if (rsd.resultSet()) {
                        new TardisBoundTransmatRemoval(plugin, id, args[2]).unbind();
                    }
                    TardisMessage.send(player, "TRANSMAT_REMOVED");
                } else {
                    TardisMessage.send(player, "TRANSMAT_NOT_FOUND");
                }
            }
        }
        return false;
    }
}
