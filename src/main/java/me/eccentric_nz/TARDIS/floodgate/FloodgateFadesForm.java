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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FloodgateFadesForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateFadesForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        // get biomes
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Fade Interior Chooser");
        builder.content("You can view the fades on the wiki - https://tardis.pages.dev/fades");
        // show all the fades
        for (Map.Entry<Material, Material[]> gradient : plugin.getBuildKeeper().getFade().getGradients().entrySet()) {
            String path = gradient.getKey().getKey().value();
            builder.button(TARDISStringUtils.capitalise(path), FormImage.Type.PATH, String.format("textures/blocks/%s", path));
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        // get the seed block in the player's main hand
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType() != Material.ORANGE_POPLAR_LEAVES) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
            return;
        }
        if (!is.hasData(DataComponentTypes.CUSTOM_NAME)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
            return;
        }
        if (!"TARDIS Seed Block".equals(ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME)))) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
            return;
        }
        ItemLore lore = is.getData(DataComponentTypes.LORE);
        if (lore == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
            return;
        }
        List<Component> lines = new ArrayList<>(lore.lines());
        if (lines.size() > 4) {
            lines.set(4, Component.text("Gradient: " + TARDISStringUtils.toEnumUppercase(response.clickedButton().text())));
        } else {
            lines.add(Component.text("Gradient: " + TARDISStringUtils.toEnumUppercase(response.clickedButton().text())));
        }
        lines.add(Component.text("Gradient: " + TARDISStringUtils.toEnumUppercase(response.clickedButton().text())));
        is.setData(DataComponentTypes.LORE, ItemLore.lore().addLines(lines));
        plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SET");
    }
}
