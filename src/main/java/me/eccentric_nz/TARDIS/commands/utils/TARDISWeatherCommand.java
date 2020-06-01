package me.eccentric_nz.TARDIS.commands.utils;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.WEATHER;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class TARDISWeatherCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("clear", "c", "rain", "r", "thunder", "t", "sun", "s");

    public TARDISWeatherCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisweather")) {
            if (args.length < 1) {
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                return true;
            }
            Player player;
            if (sender instanceof Player) {
                player = (Player) sender;
                if (player == null) {
                    TARDISMessage.send(sender, "CMD_PLAYER");
                    return true;
                }
                Location location = player.getLocation();
                World world = location.getWorld();
                if (plugin.getUtils().inTARDISWorld(player)) {
                    // get TARDIS player is in
                    int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player);
                    // get current TARDIS location
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
                    if (rsc.resultSet()) {
                        world = rsc.getWorld();
                    } else {
                        // can't change weather in TARDIS world
                        TARDISMessage.send(player, "WEATHER_TARDIS");
                        return true;
                    }
                }
                WEATHER weather = WEATHER.fromString(args[0]);
                String perm = weather.toString().toLowerCase();
                if (!player.hasPermission("tardis.weather." + perm)) {
                    TARDISMessage.send(sender, "NO_PERMS");
                    return true;
                }
                TARDISWeather.setWeather(world, weather);
                TARDISMessage.send(player, "WEATHER_SET", perm);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        }
        return ImmutableList.of();
    }
}
