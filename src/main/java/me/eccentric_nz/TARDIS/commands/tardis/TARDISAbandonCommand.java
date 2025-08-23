/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISAbandonEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAbandonLister;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.converters.TARDISAbandonUpdate;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisAbandoned;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.doors.inner.Inner;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDoor;
import me.eccentric_nz.TARDIS.doors.inner.InnerMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

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
                        plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
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
                    ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
                    if (rsp.fromID(id)) {
                        boolean outerDisplayDoor = rsp.getPreset().usesArmourStand();
                        Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                        UUID playerUUID = player.getUniqueId();
                        // close inner
                        if (innerDisplayDoor.display()) {
                            new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID, true);
                        } else {
                            new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID);
                        }
                        // close outer
                        if (outerDisplayDoor) {
                            new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                        } else if (rsp.getPreset().hasDoor()) {
                            new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
                        }
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_SUCCESS");
                    ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                    if (rsc.resultSet()) {
                        Current current = rsc.getCurrent();
                        plugin.getPM().callEvent(new TARDISAbandonEvent(player, id, current.location()));
                        // always clear sign
                        if (preset.usesArmourStand()) {
                            World world = current.location().getWorld();
                            // remove custom name from the armour stand / helmet item
                            for (Entity e : world.getNearbyEntities(current.location(), 1.1d, 1.1d, 1.1d)) {
                                if (e instanceof ArmorStand stand) {
                                    ItemStack is = stand.getItem(EquipmentSlot.HEAD);
                                    ItemMeta im = is.getItemMeta();
                                    String pb = "";
                                    switch (preset) {
                                        case WEEPING_ANGEL -> pb = "Weeping Angel";
                                        case PANDORICA -> pb = "Pandorica";
                                        case ITEM -> {
                                            for (String k : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
                                                if (is.getType().toString().equals(plugin.getCustomModelConfig().getString("models." + k + ".item"))) {
                                                    pb = k;
                                                    break;
                                                }
                                            }
                                        }
                                        default -> pb = "Police Box";
                                    }
                                    Component custom = Component.text("Abandoned " + pb);
                                    im.displayName(custom);
                                    is.setItemMeta(im);
                                    EntityEquipment ee = stand.getEquipment();
                                    ee.setHelmet(is, true);
                                    stand.customName(custom);
                                    stand.setCustomNameVisible(true);
                                    break;
                                }
                            }
                        } else {
                            Sign sign = getSign(current.location(), current.direction(), preset);
                            if (sign != null) {
                                SignSide front = sign.getSide(Side.FRONT);
                                switch (preset) {
                                    case GRAVESTONE -> front.line(3, Component.empty());
                                    case ANGEL, JAIL -> front.line(2, Component.empty());
                                    default -> front.line(0, Component.empty());
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
