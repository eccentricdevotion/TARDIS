package me.eccentric_nz.TARDIS.floodgate;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionGUIListener;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class FloodgateCompanionsForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final String[] companionData;
    private final String url = "https://raw.githubusercontent.com/eccentricdevotion/TARDIS-Resource-Pack/master/assets/tardis/textures/item/gui/%s.png";
    private final String[] heads = new String[2];

    public FloodgateCompanionsForm(TARDIS plugin, UUID uuid, String[] companionData) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.companionData = companionData;
        this.heads[0] = "alex";
        this.heads[1] = "steve";
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Companions Menu");
        builder.content("To REMOVE a companion select a player button. To add a companion, select the Add button.");
        int i = 0;
        for (String c : companionData) {
            if (!c.isEmpty()) {
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(UUID.fromString(c));
                if (op != null) {
                    builder.button(op.getName(), FormImage.Type.URL, String.format(url, heads[i%2]));
                    i++;
                }
            }
        }
        builder.button("Add");
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        String label = response.clickedButton().text();
        if (label.equals("Add")) {
            // open Add Companion Gui
            new FloodgateAddCompanionsForm(plugin, uuid).send();
        } else {
            // remove player from companions
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardis_id();
                String comps = tardis.getCompanions();
                // get UUID for offline player
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(label);
                UUID u = op.getUniqueId();
                if (op != null) {
                    TARDISCompanionGUIListener.removeCompanion(id, comps, u.toString(), player);
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        if (!comps.equalsIgnoreCase("everyone")) {
                            String[] data = tardis.getChunk().split(":");
                            TARDISCompanionGUIListener.removeFromRegion(data[0], tardis.getOwner(), u);
                        }
                    }
                    TARDISMessage.send(player, "COMPANIONS_REMOVE_ONE", label);
                }
            }
        }
    }
}
