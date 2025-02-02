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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateTelepathicForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public FloodgateTelepathicForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void send() {
        // get biomes
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Telepathic Location Finder");
        builder.button("Toggle telepathic circuit", FormImage.Type.PATH, "textures/block/daylight_detector_top.png");
        Player player = Bukkit.getPlayer(uuid);
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.cave")) {
            builder.button("Cave finder", FormImage.Type.PATH, "textures/block/dripstone_block.png");
        }
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
            builder.button("Structure finder", FormImage.Type.PATH, "textures/block/hay_block_side.png");
        }
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
            builder.button("Biome finder", FormImage.Type.PATH, "textures/block/bamboo_mosaic.png");
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
        fplayer.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        // check for telepathic circuit
        TARDISCircuitChecker tcc = null;
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasTelepathic()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TELEPATHIC_CIRCUIT");
            return;
        }
        String label = response.clickedButton().text();
        if (!label.equals("Toggle telepathic circuit") && plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.TELEPATHIC_CIRCUIT)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Telepathic Circuit");
            return;
        }
        switch (label) {
            case "Cave finder" -> player.performCommand("tardistravel cave");
            case "Structure finder" -> new FloodgateStructuresForm(plugin, player.getUniqueId(), id).send();
            case "Biome finder" -> new FloodgateBiomesForm(plugin, player.getUniqueId(), id).send();
            default -> {
                // get current telepathic setting
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                int b = (rsp.resultSet() && rsp.isTelepathyOn()) ? 0 : 1;
                // update database
                HashMap<String, Object> set = new HashMap<>();
                HashMap<String, Object> whereu = new HashMap<>();
                whereu.put("uuid", player.getUniqueId().toString());
                set.put("telepathy_on", b);
                plugin.getQueryFactory().doUpdate("player_prefs", set, whereu);
                plugin.getMessenger().announceRepeater(player, "Telepathic Circuit " + (b == 1 ? "ON" : "OFF"));
            }
        }
    }
}
