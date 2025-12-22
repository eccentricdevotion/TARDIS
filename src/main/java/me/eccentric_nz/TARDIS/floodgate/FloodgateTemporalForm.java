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
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.custommodels.GUITemporalLocator;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateTemporalForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateTemporalForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Temporal Locator");
        for (GUITemporalLocator clock : GUITemporalLocator.values()) {
            builder.button(clock.getName()+" ~"+clock.getLore(), FormImage.Type.PATH, "textures/items/clock_item.png");
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        String label = response.clickedButton().text();
        long ticks = getTime(label);
        plugin.getTrackerKeeper().getSetTime().put(player.getUniqueId(), ticks);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TEMPORAL_SET", String.format("%d", ticks));
        // damage the circuit if configured
        DamageUtility.run(plugin, DiskCircuit.TEMPORAL, plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId()), player);
    }

    private long getTime(String label) {
        String[] data = label.split("~");
        return TARDISNumberParsers.parseLong(data[1]);
    }
}
