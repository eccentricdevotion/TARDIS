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
package me.eccentric_nz.TARDIS.advanced;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class SerializeInventory {

    public static String itemStacksToString(ItemStack[] stack) {
        byte[] bytes = ItemStack.serializeItemsAsBytes(stack);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @SuppressWarnings("deprecation")
    public static ItemStack[] itemStacksFromString(String data) throws IOException {
        try {
            byte[] decoded = Base64.getDecoder().decode(data);
            return ItemStack.deserializeItemsFromBytes(decoded);
        } catch (IllegalArgumentException tryLegacy) {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                ItemStack[] stack;
                try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                    stack = new ItemStack[dataInput.readInt()];
                    // Read the serialized ItemStacks
                    for (int i = 0; i < stack.length; i++) {
                        ItemStack is = (ItemStack) dataInput.readObject();
                        if (is != null && is.getType() == Material.GLOWSTONE_DUST) {
                            if (is.hasData(DataComponentTypes.CUSTOM_NAME) && ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME)).equals("Circuits")) {
                                CustomModelData component = is.getData(DataComponentTypes.CUSTOM_MODEL_DATA);
                                if (component != null && !component.floats().isEmpty() && component.floats().getFirst() != 130.0f) {
                                    is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                                            .addFloats(List.of(130.0f))
                                            .build());
                                }
                            }
                        }
                        stack[i] = is;
                    }
                }
                return stack;
            } catch (ClassNotFoundException e) {
                throw new IOException("Unable to decode class type.", e);
            }
        }
    }
}
