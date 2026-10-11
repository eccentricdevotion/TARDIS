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
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import java.util.Locale;
import java.util.UUID;

public class FloodgateKeyForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private Material material;

    public FloodgateKeyForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        try {
            material = Material.valueOf(plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            material = Material.GOLD_NUGGET;
        }
    }

    public void send() {
        // get biomes
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Key Prefs");
        builder.content("Hold your key in your main hand, then choose a key style.");
        for (KeyVariant variant : KeyVariant.values()) {
            if (variant.ordinal() < 13) {
                String icon = variant.name().toLowerCase(Locale.ROOT);
                if (variant == KeyVariant.BROMLEY_GOLD) {
                    icon += "_icon";
                }
                builder.button(TARDISStringUtils.capitalise(variant.toString()), FormImage.Type.URL, String.format("https://github.com/eccentricdevotion/TARDISBedrockResourcePack/blob/master/textures/item/key/%s.png?raw=true", icon));
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
        // get floats and strings from clicked button
        String enumStr = TARDISStringUtils.toEnumUppercase(response.clickedButton().text());
        try {
            KeyVariant variant = KeyVariant.valueOf(enumStr);
            key.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addFloats(variant.getFloats())
                    .addString(variant.toString().toLowerCase(Locale.ROOT))
                    .build());
            // personalise
            key.editPersistentDataContainer(pdc -> pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), uuid));
            // set lore
            List<Component> lore;
            if (ComponentUtils.hasLore(key)) {
                lore = new ArrayList<>(key.getData(DataComponentTypes.LORE).lines());
            } else {
                lore = new ArrayList<>();
            }
            Component belongs = Component.text("This key belongs to", NamedTextColor.AQUA).decorate(TextDecoration.ITALIC);
            if (!lore.contains(belongs)) {
                lore.add(belongs);
                lore.add(Component.text(player.getName(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                key.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
            }
        } catch (IllegalArgumentException ignored) {
        }
    }
}
