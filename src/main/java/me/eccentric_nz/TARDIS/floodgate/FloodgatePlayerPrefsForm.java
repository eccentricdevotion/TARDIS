package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.*;

public class FloodgatePlayerPrefsForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private List<String> flightmodes = Arrays.asList("NORMAL", "REGULATOR", "MANUAL");
    private List<String> hums = Arrays.asList("ALIEN", "ATMOSPHERE", "COMPUTER", "COPPER", "CORAL", "GALAXY", "COMPUTER", "COPPER", "LEARNING", "MIND", "NEON", "SLEEPING", "VOID", "RANDOM");
    private List<String> automodes = Arrays.asList("OFF", "HOME", "AREAS", "CONFIGURED_AREAS", "CLOSEST");

    public FloodgatePlayerPrefsForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        // get player prefs
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        List<Boolean> values = new ArrayList<>();
        if (!rsp.resultSet()) {
            // make a new record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("player_prefs", set);
            // get the new record
            rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            rsp.resultSet();
        }
        CustomForm form = CustomForm.builder().title("Player Prefs Menu")
                .toggle("Autonomous Homing", rsp.isAutoOn())
                .toggle("Autonomous Siege Mode", rsp.isAutoSiegeOn())
                .toggle("Autonomous Rescue", rsp.isAutoRescueOn())
                .toggle("Beacon", rsp.isBeaconOn())
                .toggle("Close GUI", rsp.isCloseGUIOn())
                .toggle("Do Not Disturb", rsp.isDND())
                .toggle("Emergency Program One", rsp.isEpsOn())
                .toggle("HADS", rsp.isHadsOn())
                .toggle("HADS Type Dispersal", rsp.getHadsType().equals(HADS.DISPERSAL))
                .toggle("Who Quotes", rsp.isQuotesOn())
                .toggle("Renderer", rsp.isRendererOn())
                .toggle("Interior SFX", rsp.isSfxOn())
                .toggle("Submarine", rsp.isSubmarineOn())
                .toggle("Resource Pack Switching", rsp.isTextureOn())
                .toggle("Build", rsp.isBuildOn())
                .toggle("Wool Lights", rsp.isWoolLightsOn())
                .toggle("Exterior Sign", rsp.isSignOn())
                .toggle("Travel Bar", rsp.isTravelbarOn())
                .toggle("Farming", rsp.isFarmOn())
                .toggle("Telepathic Circuit", rsp.isTelepathyOn())
                .dropdown("Flight Mode", flightmodes)
                .dropdown("Interior Hum Sound", hums)
                .dropdown("Autonomus Mode", automodes)
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(CustomFormResponse response) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("auto_on", (response.asToggle(0)) ? 1 : 0);
        set.put("auto_siege_on", (response.asToggle(1)) ? 1 : 0);
        set.put("auto_rescue_on", (response.asToggle(2)) ? 1 : 0);
        set.put("beacon_on", (response.asToggle(3)) ? 1 : 0);
        set.put("close_gui_on", (response.asToggle(4)) ? 1 : 0);
        set.put("dnd_on", (response.asToggle(5)) ? 1 : 0);
        set.put("eps_on", (response.asToggle(6)) ? 1 : 0);
        set.put("hads_on", (response.asToggle(7)) ? 1 : 0);
        set.put("quotes_type", (response.asToggle(8)) ? "DISPERSION" : "DISPLACEMENT");
        set.put("renderer_on", (response.asToggle(9)) ? 1 : 0);
        set.put("sfx_on", (response.asToggle(10)) ? 1 : 0);
        set.put("submarine_on", (response.asToggle(11)) ? 1 : 0);
        set.put("texture_on", (response.asToggle(12)) ? 1 : 0);
        set.put("build_on", (response.asToggle(13)) ? 1 : 0);
        set.put("wool_lights_on", (response.asToggle(14)) ? 1 : 0);
        set.put("sign_on", (response.asToggle(15)) ? 1 : 0);
        set.put("travelbar_on", (response.asToggle(16)) ? 1 : 0);
        set.put("farm_on", (response.asToggle(17)) ? 1 : 0);
        set.put("telepathy_on", (response.asToggle(18)) ? 1 : 0);
        set.put("sign_on", (response.asToggle(19)) ? 1 : 0);
        set.put("flight_mode", response.asDropdown(20) + 1);
        set.put("hum", hums.get(response.asDropdown(21)));
        set.put("auto_default", automodes.get(response.asDropdown(22)));
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doUpdate("player_prefs", set, where);
    }
}
