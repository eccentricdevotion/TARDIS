package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FloodgateWallFloorForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final String which;
    private final List<String> blocks = new ArrayList<>();

    public FloodgateWallFloorForm(TARDIS plugin, UUID uuid, String which) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.which = which;
        for (Material entry : TARDISWalls.BLOCKS) {
            blocks.add(entry.toString());
        }
    }

    public void send() {
        CustomForm form = CustomForm.builder()
                .title("TARDIS "+which+" Menu")
                .dropdown("Material", blocks)
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(CustomFormResponse response) {
        Material material;
        try {
            material = Material.matchMaterial(response.asInput(0));
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(Bukkit.getPlayer(uuid), "ARG_MATERIAL");
        }
        // save block
        if (which.equals("Wall")) {
            // open floor form
            new FloodgateWallFloorForm(plugin, uuid, "Floor").send();
        } else {
            // run desktop change
        }
    }
}
