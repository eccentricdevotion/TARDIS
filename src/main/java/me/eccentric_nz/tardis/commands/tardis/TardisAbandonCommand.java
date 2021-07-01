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
import me.eccentric_nz.tardis.api.event.TardisAbandonEvent;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.TardisBuilderUtility;
import me.eccentric_nz.tardis.commands.admin.TardisAbandonLister;
import me.eccentric_nz.tardis.control.TardisPowerButton;
import me.eccentric_nz.tardis.database.converters.TardisAbandonUpdater;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisAbandoned;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.move.TardisDoorCloser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisAbandonCommand {

    private final TardisPlugin plugin;

    TardisAbandonCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public static Sign getSign(Location l, CardinalDirection d, Preset p) {
        Sign sign = null;
        World w = l.getWorld();
        int signx, signz, signy;
        switch (p) {
            case JUNK_MODE:
                switch (d) {
                    case EAST -> {
                        signx = 0;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = 0;
                        signz = -1;
                    }
                    default -> {
                        signx = 1;
                        signz = 0;
                    }
                }
                break;
            case GRAVESTONE:
                signx = 0;
                signz = 0;
                break;
            case TORCH:
                switch (d) {
                    case EAST -> {
                        signx = -1;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = -1;
                    }
                    case WEST -> {
                        signx = 1;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = 1;
                    }
                }
                break;
            case TOILET:
                switch (d) {
                    case EAST -> {
                        signx = 1;
                        signz = -1;
                    }
                    case SOUTH -> {
                        signx = 1;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = -1;
                        signz = 1;
                    }
                    default -> {
                        signx = -1;
                        signz = -1;
                    }
                }
                break;
            case APPERTURE:
                switch (d) {
                    case EAST -> {
                        signx = 1;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = -1;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = -1;
                    }
                }
                break;
            default:
                switch (d) {
                    case EAST -> {
                        signx = -2;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = -2;
                    }
                    case WEST -> {
                        signx = 2;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = 2;
                    }
                }
                break;
        }
        signy = switch (p) {
            case GAZEBO, JAIL, SHROOM, SWAMP -> 3;
            case TOPSYTURVEY, TOILET, TORCH -> 1;
            case ANGEL, APPERTURE, LAMP -> 0;
            default -> 2;
        };
        Block b = new Location(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz).getBlock();
        if (Tag.WALL_SIGNS.isTagged(b.getType())) {
            sign = (Sign) b.getState();
        }
        return sign;
    }

    boolean doAbandon(CommandSender sender, boolean list) {
        if (TardisPermission.hasPermission(sender, "tardis.abandon") && plugin.getConfig().getBoolean("abandon.enabled")) {
            if (list) {
                // list abandoned TARDISes
                if (sender.hasPermission("tardis.admin")) {
                    new TardisAbandonLister(plugin).list(sender);
                    return true;
                } else {
                    TardisMessage.send(sender, "NO_PERMS");
                }
            } else {
                // must be a Player
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player == null) {
                    TardisMessage.send(sender, "CMD_NO_CONSOLE");
                    return true;
                }
                if (!plugin.getConfig().getBoolean("allow.power_down")) {
                    TardisMessage.send(sender, "ABANDON_POWER_DOWN");
                    return true;
                }
                // abandon TARDIS
                ResultSetTardisAbandoned rs = new ResultSetTardisAbandoned(plugin);
                if (!rs.fromUuid(player.getUniqueId().toString())) {
                    TardisMessage.send(player, "NO_TARDIS");
                    return true;
                } else {
                    Preset preset = rs.getPreset();
                    // need to be in TARDIS
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                    if (!rst.resultSet()) {
                        TardisMessage.send(player, "NOT_IN_TARDIS");
                        return true;
                    }
                    if (preset.equals(Preset.JUNK_MODE)) {
                        TardisMessage.send(player, "ABANDONED_NOT_JUNK");
                        return true;
                    }
                    int id = rs.getTardisId();
                    if (rst.getTardisId() != id) {
                        TardisMessage.send(player, "ABANDONED_OWN");
                        return true;
                    }
                    if (!rs.isTardisInit()) {
                        TardisMessage.send(player, "ENERGY_NO_INIT");
                        return true;
                    }
                    if (!rs.isHandbrakeOn()) {
                        TardisMessage.send(player, "HANDBRAKE_ENGAGE");
                        return true;
                    }
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        TardisMessage.send(player, "NOT_IN_VORTEX");
                        return true;
                    }
                    if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                        TardisMessage.send(player, "NOT_WHILE_MAT");
                        return true;
                    }
                    new TardisAbandonUpdater(plugin, id, player.getUniqueId().toString()).run();
                    if (rs.isPowered()) {
                        // power down TARDIS
                        new TardisPowerButton(plugin, id, player, rs.getPreset(), rs.isPowered(), rs.isHidden(), rs.isLightsOn(), player.getLocation(), rs.getArtronLevel(), rs.getSchematic().hasLanterns()).clickButton();
                    }
                    // close the door
                    new TardisDoorCloser(plugin, player.getUniqueId(), id).closeDoors();
                    TardisMessage.send(player, "ABANDONED_SUCCESS");
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                    if (rsc.resultSet()) {
                        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        plugin.getPluginManager().callEvent(new TardisAbandonEvent(player, id, current));
                        // always clear sign
                        if (preset.usesItemFrame()) {
                            World world = rsc.getWorld();
                            // remove name from the item frame item
                            ItemFrame frame = null;
                            boolean found = false;
                            for (Entity e : world.getNearbyEntities(current, 1.0d, 1.0d, 1.0d)) {
                                if (e instanceof ItemFrame) {
                                    frame = (ItemFrame) e;
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                Material dye = TardisBuilderUtility.getMaterialForItemFrame(preset);
                                ItemStack is = new ItemStack(dye, 1);
                                ItemMeta im = is.getItemMeta();
                                assert im != null;
                                im.setCustomModelData(1001);
                                im.setDisplayName("");
                                is.setItemMeta(im);
                                frame.setItem(is, false);
                            }
                        } else {
                            Sign sign = getSign(current, rsc.getDirection(), preset);
                            if (sign != null) {
                                switch (preset) {
                                    case GRAVESTONE -> sign.setLine(3, "");
                                    case ANGEL, JAIL -> sign.setLine(2, "");
                                    default -> sign.setLine(0, "");
                                }
                                sign.update();
                            }
                        }
                    }
                }
            }
        } else {
            TardisMessage.send(sender, "NO_PERMS_ABANDON");
        }
        return true;
    }
}
