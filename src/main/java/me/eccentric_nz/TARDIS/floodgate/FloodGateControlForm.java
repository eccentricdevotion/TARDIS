package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.control.TARDISRandomButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateControlForm {

    private final UUID uuid;

    public FloodgateControlForm(UUID uuid) {
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm form = SimpleForm.builder().title("TARDIS Control Menu")
                .button("Random", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/random_button.png")
                .button("Saves", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/saves_button.png")
                .button("Back", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/back_button.png")
                .button("Areas", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/areas_button.png")
                .button("Terminal", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/dest_terminal.png")
                .button("ARS", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/ars_button.png")
                .button("Desktop Theme", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/theme_button.png")
                .button("Power", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/power_on.png")
                .button("Light switch", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/on_switch.png")
                .button("Door toggle", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/toggle_open.png")
                .button("Map", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/map_button.png")
                .button("Chameleon Circuit", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/chameleon_button.png")
                .button("Siege Mode", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/siege_on.png")
                .button("Hide", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/hide_button.png")
                .button("Rebuild", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/rebuild_button.png")
                .button("Direction", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/direction_button.png")
                .button("Temporal Locator", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/temporal_button.png")
                .button("Artron Energy Levels", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/artron_button.png")
                .button("Scanner", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/scan_button.png")
                .button("TARDIS Information System", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/info_button.png")
                .button("Transmat", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/transmat_button.png")
                .button("Zero Room", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/zero_button.png")
                .button("Player Preferences", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/prefs_button.png")
                .button("Companions", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/companions_button.png")
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        int buttonId = response.clickedButtonId();
        Player player = Bukkit.getPlayer(uuid);
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(TARDIS.plugin, wheres, false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(TARDIS.plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                // check they initialised
                if (!tardis.isTardis_init()) {
                    TARDISMessage.send(player, "ENERGY_NO_INIT");
                    return;
                }
                if (TARDIS.plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on() && buttonId != 7 && buttonId != 12 && buttonId != 17) {
                    TARDISMessage.send(player, "POWER_DOWN");
                    return;
                }
                if (!tardis.isHandbrake_on()) {
                    switch (buttonId) {
                        case 5 -> {
                            TARDISMessage.send(player, "ARS_NO_TRAVEL");
                            return;
                        }
                        case 6, 7, 11, 12, 13, 14, 15, 18 -> {
                            if (!TARDIS.plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                return;
                            }
                        }
                        default -> {}
                    }
                }
                boolean lights = tardis.isLights_on();
                int level = tardis.getArtron_level();
                TARDISCircuitChecker tcc = null;
                if (!TARDIS.plugin.getDifficulty().equals(Difficulty.EASY)) {
                    tcc = new TARDISCircuitChecker(TARDIS.plugin, id);
                    tcc.getCircuits();
                }
                switch (buttonId) {
                    case 0 -> { // random button
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (tcc != null && !tcc.hasInput() && !TARDIS.plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "INPUT_MISSING");
                            return;
                        }
                        TARDIS.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> new TARDISRandomButton(TARDIS.plugin, player, id, level, 0, tardis.getCompanions(), tardis.getUuid()).clickButton(), 2L);
                    }
                    case 1 -> { // saves gui
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (tcc != null && !tcc.hasMemory()) {
                            TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                            return;
                        }
                        new FloodgateSavesForm(uuid, id).send();
                    }
                    case 2 -> { // back / fast return
                    }
                    case 3 -> { // areas gui
                    }
                    case 4 -> { // destination terminal
                    }
                    case 5 -> { // ars
                    }
                    case 6 -> { // desktop theme
                    }
                    case 7 -> { // power
                    }
                    case 8 -> { // light switch
                    }
                    case 9 -> { // door toggle
                    }
                    case 10 -> { // map
                    }
                    case 11 -> { // chameleon circuit
                    }
                    case 12 -> { // siege mode
                    }
                    case 13 -> { // hide
                    }
                    case 14 -> { // rebuild
                    }
                    case 15 -> { // direction
                    }
                    case 16 -> { // temporal
                    }
                    case 17 -> { // artron level
                    }
                    case 18 -> { // scanner
                    }
                    case 19 -> { // TIS
                    }
                    case 20 -> { // transmat
                    }
                    case 21 -> { // zero room
                    }
                    case 22 -> { // player prefs
                    }
                    case 23 -> { // companions
                    }
                    default -> { // do nothing
                    }
                }
            }
        }
    }
}
