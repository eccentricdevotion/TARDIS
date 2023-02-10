package me.eccentric_nz.TARDIS.floodgate;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmatList;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class FloodgateTransmatForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateTransmatForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        // get transmat locations
        ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
        if (rslist.resultSet()) {
            SimpleForm.Builder builder = SimpleForm.builder();
            builder.title("TARDIS transmats");
            for (Transmat t : rslist.getData()) {
                
            }
            builder.validResultHandler(response -> handleResponse(response));
            SimpleForm form = builder.build();
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
            player.sendForm(form);
        } else {
            TARDISMessage.send(plugin.getServer().getPlayer(uuid), "TRANSMAT_NO_LIST");
        }
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
    }
}
