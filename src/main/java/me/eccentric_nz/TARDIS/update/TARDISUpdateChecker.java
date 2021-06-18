package me.eccentric_nz.tardis.update;

import com.google.common.collect.Sets;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.ars.TardisArsMethods;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.data.Farm;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetArs;
import me.eccentric_nz.tardis.database.resultset.ResultSetFarming;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class TardisUpdateChecker {

    private final TardisPlugin plugin;
    private final Updateable updateable;
    private final Player player;
    private final Tardis tardis;
    private final String tardis_block;

    private final Set<Updateable> mustGrowRoom = Sets.newHashSet(Updateable.FARM, Updateable.FUEL, Updateable.IGLOO, Updateable.SMELT, Updateable.STABLE, Updateable.STALL, Updateable.VAULT, Updateable.VILLAGE);

    public TardisUpdateChecker(TardisPlugin plugin, Updateable updateable, Player player, Tardis tardis, String tardis_block) {
        this.plugin = plugin;
        this.updateable = updateable;
        this.player = player;
        this.tardis = tardis;
        this.tardis_block = tardis_block;
    }

    public boolean canUpdate() {
        if (updateable.equals(Updateable.SIEGE) && !plugin.getConfig().getBoolean("siege.enabled")) {
            TardisMessage.send(player, "SIEGE_DISABLED");
            return false;
        }
        if (updateable.equals(Updateable.BEACON) && !tardis.isPowered()) {
            TardisMessage.send(player, "UPDATE_BEACON");
            return false;
        }
        if (updateable.equals(Updateable.ADVANCED) && !TardisPermission.hasPermission(player, "tardis.advanced")) {
            TardisMessage.send(player, "NO_PERM_ADV");
            return false;
        }
        if (updateable.equals(Updateable.FORCEFIELD) && !TardisPermission.hasPermission(player, "tardis.forcefield")) {
            TardisMessage.send(player, "NO_PERM_FF");
            return false;
        }
        if (updateable.equals(Updateable.STORAGE) && !TardisPermission.hasPermission(player, "tardis.storage")) {
            TardisMessage.send(player, "NO_PERM_DISK");
            return false;
        }
        if (updateable.equals(Updateable.BACKDOOR) && !TardisPermission.hasPermission(player, "tardis.backdoor")) {
            TardisMessage.send(player, "NO_PERM_BACKDOOR");
            return false;
        }
        if (updateable.equals(Updateable.TEMPORAL) && !TardisPermission.hasPermission(player, "tardis.temporal")) {
            TardisMessage.send(player, "NO_PERM_TEMPORAL");
            return false;
        }
        boolean hasFarm = false;
        boolean hasIgloo = false;
        boolean hasSmelt = false;
        boolean hasStable = false;
        boolean hasStall = false;
        boolean hasVault = false;
        boolean hasVillage = false;
        // check ARS for room type
        if (mustGrowRoom.contains(updateable)) {
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("tardis_id", tardis.getTardisId());
            ResultSetArs rsa = new ResultSetArs(plugin, wherea);
            if (rsa.resultSet()) {
                // check for rooms
                String[][][] json = TardisArsMethods.getGridFromJSON(rsa.getJson());
                for (String[][] level : json) {
                    for (String[] row : level) {
                        for (String col : row) {
                            if (col.equals("DIRT")) {
                                hasFarm = true;
                            }
                            if (col.equals("PACKED_ICE")) {
                                hasIgloo = true;
                            }
                            if (col.equals("CHEST")) {
                                hasSmelt = true;
                            }
                            if (col.equals("HAY_BLOCK")) {
                                hasStable = true;
                            }
                            if (col.equals("NETHER_WART_BLOCK")) {
                                hasStall = true;
                            }
                            if (col.equals("DISPENSER")) {
                                hasVault = true;
                            }
                            if (col.equals("OAK_LOG")) {
                                hasVillage = true;
                            }
                        }
                    }
                }
            }
        }
        if (updateable.equals(Updateable.VAULT)) {
            if (!TardisPermission.hasPermission(player, "tardis.vault")) {
                TardisMessage.send(player, "UPDATE_NO_PERM", "Vault room drop chest");
                return false;
            }
            // must grow room first
            if (!hasVault) {
                TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
                return false;
            }
        }
        if (updateable.equals(Updateable.FUEL) || updateable.equals(Updateable.SMELT)) {
            if (!TardisPermission.hasPermission(player, "tardis.room.smelter")) {
                TardisMessage.send(player, "UPDATE_NO_PERM", "Smelter room drop chest");
                return false;
            }
            // must grow room first
            if (!hasSmelt) {
                TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
                return false;
            }
        }
        if (updateable.equals(Updateable.FARM) || updateable.equals(Updateable.IGLOO) || updateable.equals(Updateable.STABLE) || updateable.equals(Updateable.STALL) || updateable.equals(Updateable.VILLAGE)) {
            if (!TardisPermission.hasPermission(player, "tardis.farm")) {
                TardisMessage.send(player, "UPDATE_NO_PERM", tardis_block);
                return false;
            }
            // must grow a room first
            ResultSetFarming rsf = new ResultSetFarming(plugin, tardis.getTardisId());
            if (rsf.resultSet()) {
                Farm farming = rsf.getFarming();
                if (updateable.equals(Updateable.FARM) && farming.getFarm().isEmpty() && !hasFarm) {
                    TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.IGLOO) && farming.getIgloo().isEmpty() && !hasIgloo) {
                    TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.STABLE) && farming.getStable().isEmpty() && !hasStable) {
                    TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.STALL) && farming.getStall().isEmpty() && !hasStall) {
                    TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
                    return false;
                }
                if (updateable.equals(Updateable.VILLAGE) && farming.getVillage().isEmpty() && !hasVillage) {
                    TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
                    return false;
                }
            }
        }
        if (updateable.equals(Updateable.RAIL) && tardis.getRail().isEmpty()) {
            TardisMessage.send(player, "UPDATE_ROOM", tardis_block);
            return false;
        }
        if (updateable.equals(Updateable.ZERO) && tardis.getZero().isEmpty()) {
            TardisMessage.send(player, "UPDATE_ZERO");
            return false;
        }
        if (updateable.equals(Updateable.ARS)) {
            if (!TardisPermission.hasPermission(player, "tardis.architectural")) {
                TardisMessage.send(player, "NO_PERM_ARS");
                return false;
            }
            if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                TardisMessage.send(player, "ARS_OWN_WORLD");
                return false;
            }
        }
        if (updateable.equals(Updateable.WEATHER)) {
            if (!TardisPermission.hasPermission(player, "tardis.weather.clear") && !TardisPermission.hasPermission(player, "tardis.weather.rain") && !TardisPermission.hasPermission(player, "tardis.weather.thunder")) {
                TardisMessage.send(player, "NO_PERMS");
                return false;
            }
        }
        if (!updateable.equals(Updateable.BACKDOOR)) {
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TardisMessage.send(player, "NOT_IN_TARDIS");
                return false;
            }
            int thisid = rst.getTardisId();
            if (thisid != tardis.getTardisId()) {
                TardisMessage.send(player, "CMD_ONLY_TL");
                return false;
            }
        }
        return true;
    }
}
