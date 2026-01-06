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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.EmergencyRelocation;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateAreasForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Player player;
    private final String path = "textures/blocks/%s.png";

    public FloodgateAreasForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.player = this.plugin.getServer().getPlayer(this.uuid);
    }

    public void send() {
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, true, false);
        if (rsa.resultSet()) {
            SimpleForm.Builder builder = SimpleForm.builder();
            builder.title("TARDIS Areas");
            int i = 0;
            for (Area a : rsa.getData()) {
                String name = a.areaName();
                if (TARDISPermission.hasPermission(player, "tardis.area." + name) || TARDISPermission.hasPermission(player, "tardis.area.*")) {
                    builder.button(name, FormImage.Type.PATH, String.format(path, FloodgateColouredBlocks.IMAGES.get(i)));
                    i++;
                }
            }
            builder.validResultHandler(this::handleResponse);
            SimpleForm form = builder.build();
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
            player.sendForm(form);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NONE");
        }
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        Location l;
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", label);
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
        rsa.resultSet();
        if (rsa.getArea().grid()) {
            l = plugin.getTardisArea().getNextSpot(label);
        } else {
            l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
        }
        if (l == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MORE_SPOTS");
            return;
        }
        // get the TARDIS the player is in
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            // get current location
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                new EmergencyRelocation(plugin).relocate(id, player);
                return;
            }
            Current current = rsc.getCurrent();
            // check the player is not already in the area!
            if (plugin.getTardisArea().isInExistingArea(current.location(), rsa.getArea().areaId())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_AREA");
                return;
            }
            HashMap<String, Object> set_next = new HashMap<>();
            set_next.put("world", l.getWorld().getName());
            set_next.put("x", l.getBlockX());
            set_next.put("y", l.getBlockY());
            set_next.put("z", l.getBlockZ());
            set_next.put("submarine", 0);
            // should be setting direction of TARDIS
            if (!rsa.getArea().direction().isEmpty()) {
                set_next.put("direction", rsa.getArea().direction());
            } else {
                set_next.put("direction", current.direction().toString());
            }
            HashMap<String, Object> wheren = new HashMap<>();
            wheren.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("next", set_next, wheren);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_APPROVED", label);
            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.AREA));
        }
    }
}
