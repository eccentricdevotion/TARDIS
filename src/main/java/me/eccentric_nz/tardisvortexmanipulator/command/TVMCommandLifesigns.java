package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
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
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        int required = plugin.getVortexConfig().getInt("tachyon_use.lifesigns");
        if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_LIFESIGNS_TACHYON");
            return true;
        }
        // remove tachyons
        new TVMQueryFactory(plugin).alterTachyons(player.getUniqueId().toString(), -required);
        if (args.length == 1) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "SCAN_ENTS");
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
                        Integer entity_count = (scannedEntities.containsKey(et)) ? scannedEntities.get(et) : 0;
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
                    if (entry.getKey().equals(EntityType.PLAYER) && playernames.size() > 0) {
                        playernames.forEach((pn) -> {
                            buf.append(", ").append(pn);
                        });
                        message = " (" + buf.toString().substring(2) + ")";
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
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PLAYER");
            return true;
        }
        Player scanned = plugin.getServer().getPlayer(args[1]);
        if (scanned == null) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "COULD_NOT_FIND_NAME");
            return true;
        }
        if (!scanned.isOnline()) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NOT_ONLINE", args[1]);
            return true;
        }
        // getHealth() / getMaxHealth() * getHealthScale()
        double health = scanned.getHealth() / scanned.getMaxHealth() * scanned.getHealthScale();
        float hunger = (scanned.getFoodLevel() / 20F) * 100;
        int air = scanned.getRemainingAir();
        TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_LIFESIGNS", args[1]);
        player.sendMessage("Has been alive for: " + TVMUtils.convertTicksToTime(scanned.getTicksLived()));
        player.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
        player.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
        player.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
        return true;
    }
}
