package me.eccentric_nz.TARDIS.floodgate;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmatList;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;

public class FloodgateTransmatForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public FloodgateTransmatForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void send() {
        // get transmat locations
        ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
        if (rslist.resultSet()) {
            SimpleForm.Builder builder = SimpleForm.builder();
            builder.title("TARDIS transmats");
            for (Transmat t : rslist.getData()) {
                builder.button(t.getName());
            }
            builder.validResultHandler(response -> handleResponse(response));
            SimpleForm form = builder.build();
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
            player.sendForm(form);
        } else {
            TARDISMessage.send(plugin.getServer().getPlayer(uuid), "TRANSMAT_NO_LIST");
        }
    }

    private void handleResponse(@NotNull SimpleFormResponse response) {
        String label = response.clickedButton().text();
        Player player = plugin.getServer().getPlayer(uuid);
        ResultSetTransmat rst = new ResultSetTransmat(plugin, id, label);
        if (rst.resultSet()) {
            TARDISMessage.send(player, "TRANSMAT");
            Location tp_loc = rst.getLocation();
            tp_loc.setYaw(rst.getYaw());
            tp_loc.setPitch(player.getLocation().getPitch());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.teleport(tp_loc);
            }, 10L);
        }
    }
}
