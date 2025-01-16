package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.desktop.TARDISThemeProcessor;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
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
        blocks.add("Default");
        for (Material mat : TARDISWalls.BLOCKS) {
            blocks.add(mat.toString());
        }
    }

    public void send() {
        CustomForm form = CustomForm.builder()
                .title("TARDIS " + which + " Menu")
                .dropdown("Material", blocks)
                .validResultHandler(this::handleResponse)
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(CustomFormResponse response) {
        // save block
        TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
        String m = blocks.get(response.asDropdown(0));
        if (m.equals("Default")) {
            m = (which.equals("Wall")) ? "ORANGE_WOOL" : "LIGHT_GRAY_WOOL";
        }
        if (which.equals("Wall")) {
            tud.setWall(m);
            // open floor form
            new FloodgateWallFloorForm(plugin, uuid, "Floor").send();
        } else {
            tud.setFloor(m);
            // run desktop change
            new TARDISThemeProcessor(plugin, uuid).changeDesktop();
        }
    }
}
