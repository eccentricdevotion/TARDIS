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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBind;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Isomorphic controls could only be operated by one user. Such controls ostensibly worked only after identifying the
 * allowed user through genetics or other uniquely identifying properties, such as their biological morphic field, of
 * which the name "isomorphic" was derived from.
 *
 * @author eccentric_nz
 */
public class TARDISBindListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validBlocks = new ArrayList<>();

    public TARDISBindListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.addAll(Tag.SIGNS.getValues());
        validBlocks.addAll(Tag.BUTTONS.getValues());
        validBlocks.add(Material.LEVER);
    }

    /**
     * Listens for player interaction with blocks after running the /tardisbind [save|cmd|player|area|biome|chameleon]
     * command. If the player's name is contained in the trackBinder HashMap then the block location is recorded in the
     * bind field of the destinations table.
     * <p>
     * If the player is travelling in the TARDIS then a check is made of the destinations table for the location of the
     * clicked block. If found the destination for the next TARDIS time travel location is set.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b != null) {
            Material m = b.getType();
            if (validBlocks.contains(m)) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                String l = b.getLocation().toString();
                HashMap<String, Object> where = new HashMap<>();
                if (plugin.getTrackerKeeper().getBinder().containsKey(uuid)) {
                    where.put("bind_id", plugin.getTrackerKeeper().getBinder().get(uuid));
                    plugin.getTrackerKeeper().getBinder().remove(uuid);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("location", l);
                    plugin.getQueryFactory().doUpdate("bind", set, where);
                    TARDISMessage.send(player, "BIND_SAVE", m.toString());
                } else if (plugin.getTrackerKeeper().getBindRemoval().containsKey(uuid)) {
                    Bind bind = plugin.getTrackerKeeper().getBindRemoval().get(uuid);
                    // get the bind record from the location and type
                    HashMap<String, Object> whereb = new HashMap<>();
                    whereb.put("type", bind.getType());
                    whereb.put("location", l);
                    ResultSetBind rsb = new ResultSetBind(plugin, whereb);
                    if (rsb.resultSet()) {
                        where.put("bind_id", rsb.getBind_id());
                        plugin.getQueryFactory().doDelete("bind", where);
                        TARDISMessage.send(player, "BIND_REMOVED", bind.toString());
                    } else {
                        TARDISMessage.send(player, "BIND_REMOVE_NO_MATCH");
                    }
                    plugin.getTrackerKeeper().getBindRemoval().remove(uuid);
                } else {
                    // is player travelling in TARDIS
                    where.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            UUID ownerUUID = tardis.getUuid();
                            HashMap<String, Object> whereb = new HashMap<>();
                            whereb.put("tardis_id", id);
                            whereb.put("location", l);
                            ResultSetBind rsb = new ResultSetBind(plugin, whereb);
                            if (rsb.resultSet()) {
                                if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                                    TARDISMessage.send(player, "POWER_DOWN");
                                    return;
                                }
                                if ((tardis.isIso_on() && !player.getUniqueId().equals(ownerUUID) && !event.useInteractedBlock().equals(Event.Result.DENY)) || plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId())) {
                                    TARDISMessage.send(player, "ISO_HANDS_OFF");
                                    return;
                                }
                                int type = rsb.getType();
                                if (type != 6 && !tardis.isHandbrake_on() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                    return;
                                }
                                // make sure TARDIS is not dispersed
                                if (type != 6 && plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                                    TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
                                    return;
                                }
                                // what bind type is it?
                                String name = rsb.getName();
                                switch (type) {
                                    case 1: // command
                                        if (name.equals("rebuild")) {
                                            player.performCommand("tardis rebuild");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis rebuild");
                                        }
                                        if (name.equals("hide")) {
                                            player.performCommand("tardis hide");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis hide");
                                        }
                                        if (name.equals("home")) {
                                            player.performCommand("tardistravel home");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel home");
                                        }
                                        if (name.equals("cave")) {
                                            player.performCommand("tardistravel cave");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel cave");
                                        }
                                        if (name.equals("make_her_blue")) {
                                            player.performCommand("tardis make_her_blue");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis make_her_blue");
                                        }
                                        if (name.equals("occupy")) {
                                            player.performCommand("tardis occupy");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis occupy");
                                        }
                                        break;
                                    case 2: // player
                                        player.performCommand("tardistravel " + name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel " + name);
                                        break;
                                    case 3: // area
                                        player.performCommand("tardistravel area " + name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel area " + name);
                                        break;
                                    case 4: // biome
                                        player.performCommand("tardistravel biome " + name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel biome " + name);
                                        break;
                                    case 5: // chameleon
                                        HashMap<String, Object> wherec = new HashMap<>();
                                        HashMap<String, Object> set = new HashMap<>();
                                        switch (name) {
                                            case "OFF" -> {
                                                set.put("adapti_on", 0);
                                                set.put("chameleon_preset", "POLICE_BOX_BLUE");
                                            }
                                            case "ADAPT" -> {
                                                set.put("adapti_on", 1);
                                                set.put("chameleon_preset", "FACTORY");
                                            }
                                            default -> { // preset
                                                set.put("adapti_on", 0);
                                                set.put("chameleon_preset", name);
                                            }
                                        }
                                        wherec.put("tardis_id", id);
                                        plugin.getQueryFactory().doUpdate("tardis", set, wherec);
                                        player.performCommand("tardis rebuild");
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis rebuild" + name);
                                        break;
                                    case 6:
                                        // transmat player to internal destination
                                        if (rsb.getName().equals("console")) {
                                            // get internal door location
                                            plugin.getGeneralKeeper().getRendererListener().transmat(player);
                                        } else {
                                            // look up the transmat location
                                            ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, rsb.getName());
                                            if (rsm.resultSet()) {
                                                TARDISMessage.send(player, "TRANSMAT");
                                                Location tp_loc = rsm.getLocation();
                                                tp_loc.setYaw(rsm.getYaw());
                                                tp_loc.setPitch(player.getLocation().getPitch());
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                                    player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                                    player.teleport(tp_loc);
                                                }, 10L);
                                            }
                                        }
                                        break;
                                    default: // (0) save
                                        player.performCommand("tardistravel dest " + name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel dest " + name);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
