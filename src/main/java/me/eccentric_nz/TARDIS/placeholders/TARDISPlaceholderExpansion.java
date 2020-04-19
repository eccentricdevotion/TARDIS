package me.eccentric_nz.TARDIS.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISPlaceholderExpansion extends PlaceholderExpansion {

    private final TARDIS plugin;

    public TARDISPlaceholderExpansion(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier() {
        return "tardis";
    }

    @Override
    public String getAuthor() {
        return "eccentric_nz";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value. We specify the value
     * identifier in this method.
     *
     * @param player     An org.bukkit.Player
     * @param identifier A String containing the identifier
     * @return a possibly-null String of the requested identifier
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        String result = null;
        if (player == null) {
            result = "";
        } else {
            String uuid = player.getUniqueId().toString();
            ResultSetArtronLevel rsl;
            ResultSetTardis rst;
            HashMap<String, Object> where = new HashMap<>();
            ResultSetTardisID rsti;
            ResultSetCurrentLocation rscl;
            switch (identifier) {
                case "ars_status":
                    Integer percent = plugin.getBuildKeeper().getRoomProgress().get(player.getUniqueId());
                    if (percent != null) {
                        result = Integer.toString(percent);
                    }
                    break;
                case "artron_amount":
                    rsl = new ResultSetArtronLevel(plugin, uuid);
                    if (rsl.resultset()) {
                        result = Integer.toString(rsl.getArtronLevel());
                    }
                    break;
                case "artron_percent":
                    rsl = new ResultSetArtronLevel(plugin, uuid);
                    if (rsl.resultset()) {
                        result = String.format("%s%%", Math.round(rsl.getArtronLevel() * 100.0d / plugin.getArtronConfig().getDouble("full_charge")));
                    }
                    break;
                case "console":
                    where.put("uuid", uuid);
                    rst = new ResultSetTardis(plugin, where, "", false, 2);
                    if (rst.resultSet()) {
                        result = TARDISStringUtils.uppercaseFirst(rst.getTardis().getSchematic().getPermission());
                    }
                    break;
                case "in":
                    where.put("uuid", uuid);
                    ResultSetTravellers rsv = new ResultSetTravellers(plugin, where, false);
                    result = rsv.resultSet() ? "true" : "false";
                    break;
                case "preset":
                    where.put("uuid", uuid);
                    rst = new ResultSetTardis(plugin, where, "", false, 2);
                    if (rst.resultSet()) {
                        result = rst.getTardis().getPreset().toString();
                    }
                    break;
                case "current_location":
                    rsti = new ResultSetTardisID(plugin);
                    if (rsti.fromUUID(uuid)) {
                        where.put("tardis_id", rsti.getTardis_id());
                        rscl = new ResultSetCurrentLocation(plugin, where);
                        if (rscl.resultSet()) {
                            result = "TARDIS was left at " + rscl.getWorld().getName() + " at " + "x: " + rscl.getX() + " y: " + rscl.getY() + " z: " + rscl.getZ();
                        }
                    }
                    break;
                case "current_location_x":
                    rsti = new ResultSetTardisID(plugin);
                    if (rsti.fromUUID(uuid)) {
                        where.put("tardis_id", rsti.getTardis_id());
                        rscl = new ResultSetCurrentLocation(plugin, where);
                        if (rscl.resultSet()) {
                            result = Integer.toString(rscl.getX());
                        }
                    }
                    break;
                case "current_location_y":
                    rsti = new ResultSetTardisID(plugin);
                    if (rsti.fromUUID(uuid)) {
                        where.put("tardis_id", rsti.getTardis_id());
                        rscl = new ResultSetCurrentLocation(plugin, where);
                        if (rscl.resultSet()) {
                            result = Integer.toString(rscl.getY());
                        }
                    }
                    break;
                case "current_location_z":
                    rsti = new ResultSetTardisID(plugin);
                    if (rsti.fromUUID(uuid)) {
                        where.put("tardis_id", rsti.getTardis_id());
                        rscl = new ResultSetCurrentLocation(plugin, where);
                        if (rscl.resultSet()) {
                            result = Integer.toString(rscl.getZ());
                        }
                    }
                    break;
                case "current_location_world":
                    rsti = new ResultSetTardisID(plugin);
                    if (rsti.fromUUID(uuid)) {
                        where.put("tardis_id", rsti.getTardis_id());
                        rscl = new ResultSetCurrentLocation(plugin, where);
                        if (rscl.resultSet()) {
                            result = rscl.getWorld().getName();
                        }
                    }
                    break;
                case "current_location_direction":
                    rsti = new ResultSetTardisID(plugin);
                    if (rsti.fromUUID(uuid)) {
                        where.put("tardis_id", rsti.getTardis_id());
                        rscl = new ResultSetCurrentLocation(plugin, where);
                        if (rscl.resultSet()) {
                            result = rscl.getDirection().toString();
                        }
                    }
                    break;
                case "current_location_biome":
                    rsti = new ResultSetTardisID(plugin);
                    if (rsti.fromUUID(uuid)) {
                        where.put("tardis_id", rsti.getTardis_id());
                        rscl = new ResultSetCurrentLocation(plugin, where);
                        if (rscl.resultSet()) {
                            result = rscl.getBiome().toString();
                        }
                    }
                    break;
                case "timelord_artron_amount":
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                    if (rsp.resultSet()) {
                        result = Integer.toString(rsp.getArtronLevel());
                    }
                    break;
                default:
                    break;
            }
        }
        // return null if an invalid placeholder (e.g. %tardis_unknownplaceholder%) was provided
        return result;
    }
}
