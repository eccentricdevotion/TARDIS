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
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Locale;
import java.util.UUID;

public class FloodgateSonicForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Material material = Material.BLAZE_ROD;

    public FloodgateSonicForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        // get biomes
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Sonic Prefs");
        builder.content("Hold your sonic in your main hand, then choose a key style.");
        for (SonicVariant variant : SonicVariant.values()) {
            if (variant.ordinal() < 19) {
                builder.button(TARDISStringUtils.capitalise(variant.toString()), FormImage.Type.URL, String.format("https://github.com/eccentricdevotion/TARDISBedrockResourcePack/blob/master/textures/item/sonic/%s_icon.png?raw=true", variant.toString().toLowerCase(Locale.ROOT)));
            }
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        ItemStack key = player.getInventory().getItemInMainHand();
        if (key.isEmpty() || key.getType() != material || !key.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            return;
        }
        // get floats from clicked button
        String enumStr = TARDISStringUtils.toEnumUppercase(response.clickedButton().text());
        try {
            SonicVariant variant = SonicVariant.valueOf(enumStr);
            key.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addFloats(variant.getFloats())
                    .build());
        } catch (IllegalArgumentException ignored) {
        }
    }
}
