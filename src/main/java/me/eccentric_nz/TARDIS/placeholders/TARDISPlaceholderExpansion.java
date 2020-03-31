package me.eccentric_nz.TARDIS.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetArtronLevel;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
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
            switch (identifier) {
                case "artron_amount":
                    rsl = new ResultSetArtronLevel(plugin, uuid);
                    if (rsl.resultset()) {
                        result = "" + rsl.getArtronLevel();
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
                case "preset":
                    where.put("uuid", uuid);
                    rst = new ResultSetTardis(plugin, where, "", false, 2);
                    if (rst.resultSet()) {
                        result = rst.getTardis().getPreset().toString();
                    }
                    break;
                case "timelord_artron_amount":
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                    if (rsp.resultSet()) {
                        result = "" + rsp.getArtronLevel();
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
