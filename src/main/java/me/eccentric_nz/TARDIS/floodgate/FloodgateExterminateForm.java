package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateExterminateForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateExterminateForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Delete TARDIS");
        builder.content(plugin.getLanguage().getString("EXTERMINATE_CONFIRM"));
        builder.button("Exterminate");
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = plugin.getServer().getPlayer(uuid);
        String label = response.clickedButton().text();
        if (label.equals("Exterminate")) {
            player.performCommand("tardis exterminate 6z@3=V!Q7*/O_OB^");
        }
    }
}
