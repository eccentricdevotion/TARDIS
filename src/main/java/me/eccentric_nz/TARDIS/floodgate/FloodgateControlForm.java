package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISDirectionCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.control.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (tcc != null && !tcc.hasInput() && !TARDIS.plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "INPUT_MISSING");
                            return;
                        }
                        new TARDISFastReturnButton(TARDIS.plugin, player, id, level).clickButton();
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
                        if (TARDIS.plugin.getConfig().getBoolean("allow.power_down")) {
                            new TARDISPowerButton(TARDIS.plugin, id, player, tardis.getPreset(), tardis.isPowered_on(), tardis.isHidden(), lights, player.getLocation(), level, tardis.getSchematic().hasLanterns()).clickButton();
                        } else {
                            TARDISMessage.send(player, "POWER_DOWN_DISABLED");
                        }
                    }
                    case 8 -> { // light switch
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (!lights && TARDIS.plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        new TARDISLightSwitch(TARDIS.plugin, id, lights, player, tardis.getSchematic().hasLanterns()).flickSwitch();
                    }
                    case 9 -> { // door toggle
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        new TARDISBlackWoolToggler(TARDIS.plugin).toggleBlocks(id, player);
                    }
                    case 10 -> { // map
                    }
                    case 11 -> { // chameleon circuit
                    }
                    case 12 -> { // siege mode
                        if (tcc != null && !tcc.hasMaterialisation()) {
                            TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                            return;
                        }
                        new TARDISSiegeButton(TARDIS.plugin, player, tardis.isPowered_on(), id).clickButton();
                    }
                    case 13 -> { // hide
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        new TARDISHideCommand(TARDIS.plugin).hide(player);
                    }
                    case 14 -> { // rebuild
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        new TARDISRebuildCommand(TARDIS.plugin).rebuildPreset(player);
                    }
                    case 15 -> { // direction
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(TARDIS.plugin, whered);
                        String direction = "EAST";
                        if (rsc.resultSet()) {
                            direction = rsc.getDirection().toString();
                            int ordinal = COMPASS.valueOf(direction).ordinal() + 1;
                            if (ordinal == 4) {
                                ordinal = 0;
                            }
                            direction = COMPASS.values()[ordinal].toString();
                        }
                        String[] args = new String[]{"direction", direction};
                        new TARDISDirectionCommand(TARDIS.plugin).changeDirection(player, args);
                    }
                    case 16 -> { // temporal
                    }
                    case 17 -> { // artron level
                        new TARDISArtronIndicator(TARDIS.plugin).showArtronLevel(player, id, 0);
                    }
                    case 18 -> { // scanner
                        TARDISScanner.scan(player, id, TARDIS.plugin.getServer().getScheduler());
                    }
                    case 19 -> { // TIS
                        new TARDISInfoMenuButton(TARDIS.plugin, player).clickButton();
                    }
                    case 20 -> { // transmat
                    }
                    case 21 -> { // zero room
                        if (TARDIS.plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        int zero_amount = TARDIS.plugin.getArtronConfig().getInt("zero");
                        if (level < zero_amount) {
                            TARDISMessage.send(player, "NOT_ENOUGH_ZERO_ENERGY");
                            return;
                        }
                        Location zero = TARDISStaticLocationGetters.getLocationFromDB(tardis.getZero());
                        if (zero != null) {
                            TARDISMessage.send(player, "ZERO_READY");
                            TARDIS.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> new TARDISExteriorRenderer(TARDIS.plugin).transmat(player, COMPASS.SOUTH, zero), 20L);
                            TARDIS.plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
                            HashMap<String, Object> wherez = new HashMap<>();
                            wherez.put("tardis_id", id);
                            TARDIS.plugin.getQueryFactory().alterEnergyLevel("tardis", -zero_amount, wherez, player);
                        } else {
                            TARDISMessage.send(player, "NO_ZERO");
                        }
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
