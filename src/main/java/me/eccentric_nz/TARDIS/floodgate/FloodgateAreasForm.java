package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateAreasForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Player player;
    private final String path = "textures/blocks/%s.png";

    public FloodgateAreasForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.player = this.plugin.getServer().getPlayer(this.uuid);
    }

    public void send() {
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, true, false);
        if (rsa.resultSet()) {
            SimpleForm.Builder builder = SimpleForm.builder();
            builder.title("TARDIS Areas");
            int i = 0;
            for (Area a : rsa.getData()) {
                String name = a.getAreaName();
                if (TARDISPermission.hasPermission(player, "tardis.area." + name) || TARDISPermission.hasPermission(player, "tardis.area.*")) {
                    builder.button(name, FormImage.Type.URL, String.format(path, FloodgateColouredBlocks.IMAGES.get(i)));
                    i++;
                }
            }
            builder.validResultHandler(response -> handleResponse(response));
            SimpleForm form = builder.build();
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
            player.sendForm(form);
        } else {
            TARDISMessage.send(player, "AREA_NONE");
        }
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        Location l = plugin.getTardisArea().getNextSpot(label);
        if (l == null) {
            TARDISMessage.send(player, "NO_MORE_SPOTS");
            return;
        }
        // check the player is not already in the area!
        if (plugin.getTardisArea().areaCheckInExisting(l)) {
            TARDISMessage.send(player, "TRAVEL_NO_AREA");
            return;
        }
        player.performCommand("tardistravel area " + label);
    }
}
