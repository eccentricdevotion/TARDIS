package me.eccentric_nz.TARDIS.floodgate;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.utils.TARDISWeather;
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Weather;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

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
                .button("excite", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/excite.png")
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = plugin.getServer().getPlayer(uuid);
        String label = response.clickedButton().text();
        if (!plugin.getConfig().getBoolean("allow.weather_set")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_DISABLED");
            return;
        }
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                // check they initialised
                if (!tardis.isTardisInit()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                    return;
                }
                if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                    return;
                }
                if (!tardis.isHandbrakeOn()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
                    return;
                }
                // get current location
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", tardis.getTardisId());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                if (!rsc.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                }
                if (label.equals("excite")) {
                    // atmospheric excitation
                    if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_EXCITE");
                        return;
                    }
                    new TARDISAtmosphericExcitation(plugin).excite(tardis.getTardisId(), player);
                    plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
                } else {
                    // change weather
                    Weather weather = Weather.fromString(label);
                    String perm = weather.toString().toLowerCase();
                    if (!TARDISPermission.hasPermission(player, "tardis.weather." + perm)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                    }
                    TARDISWeather.setWeather(rsc.getWorld(), weather);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_SET", perm);
                }
            }
        }
    }
}
