/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISAbandonEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAbandonLister;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.converters.TARDISAbandonUpdate;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisAbandoned;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISAbandonCommand {

    private final TARDIS plugin;

    TARDISAbandonCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static Sign getSign(Location l, COMPASS d, ChameleonPreset p) {
        Sign sign = null;
        World w = l.getWorld();
        int signx, signz, signy;
        switch (p) {
            case JUNK_MODE -> {
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
            }
            case GRAVESTONE -> {
                signx = 0;
                signz = 0;
            }
            case TORCH -> {
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
            }
            case TOILET -> {
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
            }
            case APPERTURE -> {
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
            }
            default -> {
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
            }
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
        if (TARDISPermission.hasPermission(sender, "tardis.abandon") && plugin.getConfig().getBoolean("abandon.enabled")) {
            if (list) {
                // list abandoned TARDISes
                if (sender.hasPermission("tardis.admin")) {
                    new TARDISAbandonLister(plugin).list(sender);
                    return true;
                } else {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                }
            } else {
                // must be a Player
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_NO_CONSOLE");
                    return true;
                }
                if (!plugin.getConfig().getBoolean("allow.power_down")) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDON_POWER_DOWN");
                    return true;
                }
                // abandon TARDIS
                ResultSetTardisAbandoned rs = new ResultSetTardisAbandoned(plugin);
                if (!rs.fromUUID(player.getUniqueId().toString())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                    return true;
                } else {
                    ChameleonPreset preset = rs.getPreset();
                    // need to be in tardis
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                    if (!rst.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
                        return true;
                    }
                    if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_NOT_JUNK");
                        return true;
                    }
                    int id = rs.getTardis_id();
                    if (rst.getTardis_id() != id) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_OWN");
                        return true;
                    }
                    if (!rs.isTardis_init()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                        return true;
                    }
                    if (!rs.isHandbrake_on()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDBRAKE_ENGAGE");
                        return true;
                    }
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_VORTEX");
                        return true;
                    }
                    if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                        return true;
                    }
                    new TARDISAbandonUpdate(plugin, id, player.getUniqueId().toString()).run();
                    if (rs.isPowered_on()) {
                        // power down TARDIS
                        new TARDISPowerButton(plugin, id, player, rs.getPreset(), rs.isPowered_on(), rs.isHidden(), rs.isLights_on(), player.getLocation(), rs.getArtron_level(), rs.getSchematic().getLights()).clickButton();
                    }
                    // close the door
                    new TARDISDoorCloser(plugin, player.getUniqueId(), id).closeDoors();
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_SUCCESS");
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                    if (rsc.resultSet()) {
                        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        plugin.getPM().callEvent(new TARDISAbandonEvent(player, id, current));
                        // always clear sign
                        if (preset.usesArmourStand()) {
                            World world = rsc.getWorld();
                            // remove name from the item frame item
                            for (Entity e : world.getNearbyEntities(current, 1.0d, 1.0d, 1.0d)) {
                                if (e instanceof ItemFrame frame) {
                                    Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, id, true);
                                    ItemStack is = new ItemStack(dye, 1);
                                    ItemMeta im = is.getItemMeta();
                                    im.setCustomModelData(1001);
                                    im.setDisplayName("");
                                    is.setItemMeta(im);
                                    frame.setItem(is, false);
                                    break;
                                }
                            }
                        } else {
                            Sign sign = getSign(current, rsc.getDirection(), preset);
                            if (sign != null) {
                                SignSide front = sign.getSide(Side.FRONT);
                                switch (preset) {
                                    case GRAVESTONE -> front.setLine(3, "");
                                    case ANGEL, JAIL -> front.setLine(2, "");
                                    default -> front.setLine(0, "");
                                }
                                sign.update();
                            }
                        }
                    }
                }
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS_ABANDON");
        }
        return true;
    }
}
