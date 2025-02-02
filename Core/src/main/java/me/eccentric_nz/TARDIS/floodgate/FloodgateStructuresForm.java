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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISStructureTravel;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateStructuresForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public FloodgateStructuresForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void send() {
        // get structures
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Telepathic Structure Finder");
        for (Structure structure : TARDISStructureTravel.overworldStructures) {
            builder.button(TARDISStringUtils.capitalise(structure.getKey().getKey()), FormImage.Type.PATH, "textures/blocks/grass_side_carried.png");
        }
        for (Structure structure : TARDISStructureTravel.netherStructures) {
            builder.button(TARDISStringUtils.capitalise(structure.getKey().getKey()), FormImage.Type.PATH, "textures/blocks/crimson_nylium_side.png");
        }
        builder.button(TARDISStringUtils.capitalise(Structure.END_CITY.getKey().getKey()), FormImage.Type.PATH, "textures/blocks/purpur_block.png");
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        // get tardis artron level
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (!rs.fromID(id)) {
            return;
        }
        int level = rs.getArtronLevel();
        int travel = plugin.getArtronConfig().getInt("travel");
        if (level < travel) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
            return;
        }
        String enumStr = TARDISStringUtils.toEnumUppercase(response.clickedButton().text());
        player.performCommand("tardistravel structure " + enumStr);
    }
}
