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
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmatList;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateTransmatForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public FloodgateTransmatForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void send() {
        // get transmat locations
        ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
        if (rslist.resultSet()) {
            SimpleForm.Builder builder = SimpleForm.builder();
            builder.title("TARDIS transmats");
            for (Transmat t : rslist.getData()) {
                builder.button(t.name());
            }
            builder.validResultHandler(this::handleResponse);
            SimpleForm form = builder.build();
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
            player.sendForm(form);
        } else {
            plugin.getMessenger().send(plugin.getServer().getPlayer(uuid), TardisModule.TARDIS, "TRANSMAT_NO_LIST");
        }
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        Player player = plugin.getServer().getPlayer(uuid);
        ResultSetTransmat rst = new ResultSetTransmat(plugin, id, label);
        if (rst.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
            Location tp_loc = rst.getLocation();
            tp_loc.setYaw(rst.getYaw());
            tp_loc.setPitch(player.getLocation().getPitch());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.teleport(tp_loc);
            }, 10L);
        }
    }
}
