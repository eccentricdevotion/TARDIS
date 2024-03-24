package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class DoorLockAction {

    private final TARDIS plugin;

    public DoorLockAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void lockUnlock(Player player, Material m, int id, boolean isLocked, boolean checkId) {
        UUID playerUUID = player.getUniqueId();
        ItemStack stack = player.getInventory().getItemInMainHand();
        Material material = stack.getType();
        if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().endsWith("TARDIS Remote Key")) {
            return;
        }
        // must be the owner
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(playerUUID.toString())) {
            // must use key to lock / unlock door
            if (material.equals(m) || plugin.getConfig().getBoolean("preferences.any_key")) {
                if (rs.getTardis_id() != id) {
                    plugin.getMessenger().sendStatus(player, "DOOR_LOCK_UNLOCK");
                    return;
                }
                int locked = (isLocked) ? 0 : 1;
                String message = (isLocked) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                HashMap<String, Object> setl = new HashMap<>();
                setl.put("locked", locked);
                HashMap<String, Object> wherel = new HashMap<>();
                wherel.put("tardis_id", id);
                // always lock / unlock both doors
                plugin.getQueryFactory().doUpdate("doors", setl, wherel);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_LOCK", message);
            } else if (material.isAir()) {
                if (checkId && rs.getTardis_id() == id) {
                    return;
                }
                // knock with hand if it's not their TARDIS
                // only outside the TARDIS
                // only if companion
                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                if (rsc.getCompanions().contains(playerUUID)) {
                    // get Time Lord
                    HashMap<String, Object> wherett = new HashMap<>();
                    wherett.put("tardis_id", id);
                    ResultSetTardis rstt = new ResultSetTardis(plugin, wherett, "", false, 2);
                    if (rstt.resultSet()) {
                        UUID tluuid = rstt.getTardis().getUuid();
                        // only if Time Lord is inside
                        HashMap<String, Object> wherev = new HashMap<>();
                        wherev.put("uuid", tluuid.toString());
                        ResultSetTravellers rsv = new ResultSetTravellers(plugin, wherev, false);
                        if (rsv.resultSet()) {
                            Player tl = plugin.getServer().getPlayer(tluuid);
                            tl.getWorld().playSound(tl.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 3.0F, 3.0F);
                        }
                    }
                }
            }
        }
    }
}
