package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonFrame;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class FloodgateChameleonPresetForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Player player;


    public FloodgateChameleonPresetForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.player = this.plugin.getServer().getPlayer(this.uuid);
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Chameleon Circuit");
        for (PRESET preset : PRESET.values()) {
            if (preset.isBlockPreset() && TARDISPermission.hasPermission(player, "tardis.preset." + preset.toString().toLowerCase())) {
                String path = String.format("textures/blocks/%s.png", preset.getGuiDisplay().toString().toLowerCase(Locale.ROOT));
                plugin.debug("texture path -> " + path);
                builder.button(preset.toString(), FormImage.Type.PATH, path);
            }
        }
        // TODO TARDIS-Bedrock-Resource-Pack.mcpack for ItemFrame police boxes and sounds
//        for (PRESET preset : PRESET.values()) {
//            if (preset.usesItemFrame()) {
//                builder.button(preset.toString(), FormImage.Type.PATH, String.format("textures/blocks/%s.png", preset.getGuiDisplay().toString().toLowerCase(Locale.ROOT)));
//            }
//        }
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                // set the Chameleon Circuit sign(s)
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", id);
                wherec.put("type", Control.CHAMELEON.getId());
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, true);
                boolean hasSign = rsc.resultSet();
                HashMap<String, Object> wheref = new HashMap<>();
                wheref.put("tardis_id", id);
                wheref.put("type", Control.FRAME.getId());
                ResultSetControls rsf = new ResultSetControls(plugin, wheref, true);
                boolean hasFrame = rsf.resultSet();
                // set the Chameleon Circuit sign(s)
                HashMap<String, Object> set = new HashMap<>();
                PRESET selected = PRESET.valueOf(label);
                set.put("chameleon_preset", selected.toString());
                if (hasSign) {
                    updateChameleonSign(rsf.getData(), selected.toString(), player);
                }
                if (hasFrame) {
                    new TARDISChameleonFrame().updateChameleonFrame(selected, rsf.getLocation());
                }
                TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + selected.getDisplayName());
                if (set.size() > 0) {
                    set.put("adapti_on", 0);
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                }
            }
        }
    }

    private void updateChameleonSign(ArrayList<HashMap<String, String>> map, String preset, Player player) {
        for (HashMap<String, String> entry : map) {
            TARDISStaticUtils.setSign(entry.get("location"), 3, preset, player);
        }
    }
}
