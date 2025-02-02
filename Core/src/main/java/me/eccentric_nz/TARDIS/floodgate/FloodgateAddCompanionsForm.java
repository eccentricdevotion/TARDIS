/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionAddGUIListener;
import me.eccentric_nz.TARDIS.companionGUI.VanishChecker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisCompanions;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FloodgateAddCompanionsForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Player player;

    public FloodgateAddCompanionsForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.player = this.plugin.getServer().getPlayer(this.uuid);
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Add Companion");
        builder.content("To ADD a companion select a player button.");
        List<String> comps;
        // get current companions
        ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
        if (rs.fromUUID(uuid.toString()) && rs.getCompanions() != null && !rs.getCompanions().isEmpty()) {
            comps = List.of(rs.getCompanions().split(":"));
        } else {
            comps = new ArrayList<>();
        }
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            UUID puid = p.getUniqueId();
            if (puid != uuid && !comps.contains(puid.toString()) && VanishChecker.canSee(player, p)) {
                builder.button(p.getName());
            }
        }
        builder.button("Everyone");
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        String label = response.clickedButton().text();
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rsa = new ResultSetTardis(plugin, wherea, "", false, 0);
        if (rsa.resultSet()) {
            Tardis tardis = rsa.getTardis();
            int id = tardis.getTardisId();
            String comps = tardis.getCompanions();
            if (label.equals("Everyone")) {
                TARDISCompanionAddGUIListener.addCompanion(id, comps, "everyone");
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    // remove all members
                    String[] data = tardis.getChunk().split(":");
                    plugin.getWorldGuardUtils().removeAllMembersFromRegion(TARDISAliasResolver.getWorldFromAlias(data[0]), player.getName(), player.getUniqueId());
                    // set entry and exit flags to allow
                    plugin.getWorldGuardUtils().setEntryExitFlags(data[0], player.getName(), true);
                }
                plugin.getMessenger().sendInsertedColour(player, "COMPANIONS_ADD", "everyone", plugin);
                plugin.getMessenger().sendColouredCommand(player, "COMPANIONS_EVERYONE", "/tardis remove all", plugin);
            } else {
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(label);
                if (op != null) {
                    String u = op.getUniqueId().toString();
                    TARDISCompanionAddGUIListener.addCompanion(id, comps, u);
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        String[] data = tardis.getChunk().split(":");
                        TARDISCompanionAddGUIListener.addToRegion(data[0], tardis.getOwner(), label);
                        // set entry and exit flags to deny
                        plugin.getWorldGuardUtils().setEntryExitFlags(data[0], label, false);
                    }
                    plugin.getMessenger().sendInsertedColour(player, "COMPANIONS_ADD", label, plugin);
                }
            }
        }
    }
}
