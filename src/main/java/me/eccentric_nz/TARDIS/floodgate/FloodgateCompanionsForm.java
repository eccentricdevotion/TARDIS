/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionGUIListener;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

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
                    builder.button(op.getName(), FormImage.Type.URL, String.format(url, heads[i % 2]));
                    i++;
                }
            }
        }
        builder.button("Add");
        builder.validResultHandler(this::handleResponse);
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
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardisId();
                String comps = tardis.getCompanions();
                // get UUID for offline player
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(label);
                if (op != null) {
                    UUID u = op.getUniqueId();
                    TARDISCompanionGUIListener.removeCompanion(id, comps, u.toString(), player);
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        if (!comps.equalsIgnoreCase("everyone")) {
                            String[] data = tardis.getChunk().split(":");
                            TARDISCompanionGUIListener.removeFromRegion(data[0], tardis.getOwner(), u);
                        }
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "COMPANIONS_REMOVE_ONE", label);
                }
            }
        }
    }
}
