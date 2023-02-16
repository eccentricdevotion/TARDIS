package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.utils.TARDISWeather;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.Weather;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateWeatherForm {

    private final TARDIS plugin;
    private final UUID uuid;
    public FloodgateWeatherForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm form = SimpleForm.builder()
                .title("TARDIS Weather Menu")
                .button("clear", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/clear.png")
                .button("rain", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/rain.png")
                .button("thunder", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/thunder.png")
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = plugin.getServer().getPlayer(uuid);
        String label = response.clickedButton().text();
        if (!plugin.getConfig().getBoolean("allow.weather_set")) {
            TARDISMessage.send(player, "WEATHER_DISABLED");
            return;
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
                return;
            }
        }
        Weather weather = Weather.fromString(label);
        String perm = weather.toString().toLowerCase();
        if (!TARDISPermission.hasPermission(player, "tardis.weather." + perm)) {
            TARDISMessage.send(player, "NO_PERMS");
        }
        TARDISWeather.setWeather(world, weather);
        TARDISMessage.send(player, "WEATHER_SET", perm);
    }
}
