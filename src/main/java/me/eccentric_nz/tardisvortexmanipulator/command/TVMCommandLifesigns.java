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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TVMCommandLifesigns {

    private final TARDIS plugin;

    public TVMCommandLifesigns(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean scan(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "vm.lifesigns")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        int required = plugin.getVortexConfig().getInt("tachyon_use.lifesigns");
        if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_LIFESIGNS_TACHYON");
            return true;
        }
        // remove tachyons
        new TVMQueryFactory(plugin).alterTachyons(player.getUniqueId().toString(), -required);
        if (args.length == 1) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "SCAN_ENTS");
            // scan nearby entities
            double d = plugin.getVortexConfig().getDouble("lifesign_scan_distance");
            List<Entity> ents = player.getNearbyEntities(d, d, d);
            if (!ents.isEmpty()) {
                // record nearby entities
                HashMap<EntityType, Integer> scannedEntities = new HashMap<>();
                List<String> playernames = new ArrayList<>();
                for (Entity k : ents) {
                    EntityType et = k.getType();
                    if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                        int entity_count = scannedEntities.getOrDefault(et, 0);
                        boolean visible = true;
                        if (et.equals(EntityType.PLAYER)) {
                            Player entPlayer = (Player) k;
                            if (player.canSee(entPlayer)) {
                                playernames.add(entPlayer.getName());
                            } else {
                                visible = false;
                            }
                        }
                        if (visible) {
                            scannedEntities.put(et, entity_count + 1);
                        }
                    }
                }
                for (Map.Entry<EntityType, Integer> entry : scannedEntities.entrySet()) {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (entry.getKey().equals(EntityType.PLAYER) && !playernames.isEmpty()) {
                        playernames.forEach((pn) -> buf.append(", ").append(pn));
                        message = " (" + buf.substring(2) + ")";
                    }
                    player.sendMessage("    " + entry.getKey() + ": " + entry.getValue() + message);
                }
                scannedEntities.clear();
            } else {
                player.sendMessage("SCAN_NONE");
            }
            return true;
        }
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PLAYER");
            return true;
        }
        Player scanned = plugin.getServer().getPlayer(args[1]);
        if (scanned == null) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "COULD_NOT_FIND_NAME");
            return true;
        }
        if (!scanned.isOnline()) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NOT_ONLINE", args[1]);
            return true;
        }
        // getHealth() / getMaxHealth() * getHealthScale()
        double health = scanned.getHealth() / scanned.getAttribute(Attribute.MAX_HEALTH).getValue() * scanned.getHealthScale();
        float hunger = (scanned.getFoodLevel() / 20F) * 100;
        int air = scanned.getRemainingAir();
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_LIFESIGNS", args[1]);
        player.sendMessage("Has been alive for: " + TVMUtils.convertTicksToTime(scanned.getTicksLived()));
        player.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
        player.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
        player.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
        return true;
    }
}
