package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.List;
import java.util.UUID;

public class FloodgateDestinationTerminalForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateDestinationTerminalForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        // get list of worlds
        List<String> worlds = plugin.getTardisAPI().getWorlds();
        CustomForm form = CustomForm.builder()
                .title("Destination Terminal")
                .slider("X", -300, 300, 25, 0)
                .slider("Z", -300, 300, 25, 0)
                .slider("Multiplier", 1, 4, 1)
                .dropdown("World", worlds)
                .toggle("Submarine", false)
                .toggle("Just check calculated destination", false)
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(CustomFormResponse response) {
        // check player hs permission for world
        // if nether or the end check if travel is enabled there
        // check location
        // if not just checking and location is valid
        // set destination
    }
}
