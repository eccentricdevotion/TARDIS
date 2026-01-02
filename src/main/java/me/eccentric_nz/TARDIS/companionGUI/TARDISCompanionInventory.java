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
package me.eccentric_nz.TARDIS.companionGUI;

import com.google.common.collect.Multimaps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUICompanion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final String[] companionData;
    private final Inventory inventory;

    public TARDISCompanionInventory(TARDIS plugin, String[] companionData) {
        this.plugin = plugin;
        this.companionData = companionData;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Companions", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        int i = 0;
        for (String c : companionData) {
            if (!c.isEmpty() && i < 45) {
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(UUID.fromString(c));
                try {
                    ItemStack head = ItemStack.of(Material.PLAYER_HEAD, 1);
                    SkullMeta skull = (SkullMeta) head.getItemMeta();
                    skull.setOwningPlayer(op);
                    String name = op.getName();
                    if (!op.hasPlayedBefore() || name == null) {
                        // lookup name
                        name = getPlayerNameFromMojang(c);
                    }
                    skull.displayName(Component.text(name));
                    skull.lore(List.of(Component.text(c)));
                    skull.addItemFlags(ItemFlag.values());
                    skull.setAttributeModifiers(Multimaps.forMap(Map.of()));
                    head.setItemMeta(skull);
                    heads[i] = head;
                    i++;
                } catch (Exception e) {
                    plugin.debug("Could not get offline companion: " + e.getMessage());
                }
            }
        }
        // add buttons
        ItemStack info = ItemStack.of(GUICompanion.INFO.material(), 1);
        ItemMeta ii = info.getItemMeta();
        ii.displayName(Component.text("Info"));
        ii.lore(List.of(
                Component.text("To REMOVE a companion"),
                Component.text("select a player head"),
                Component.text("then click the Remove"),
                Component.text("button (bucket)."),
                Component.text("To ADD a companion"),
                Component.text("click the Add button"),
                Component.text("(nether star).")
        ));
        info.setItemMeta(ii);
        heads[GUICompanion.INFO.slot()] = info;
        ItemStack add = ItemStack.of(GUICompanion.ADD_COMPANION.material(), 1);
        ItemMeta aa = add.getItemMeta();
        aa.displayName(Component.text("Add"));
        add.setItemMeta(aa);
        heads[GUICompanion.ADD_COMPANION.slot()] = add;
        ItemStack del = ItemStack.of(GUICompanion.DELETE_COMPANION.material(), 1);
        ItemMeta dd = del.getItemMeta();
        dd.displayName(Component.text("Remove"));
        del.setItemMeta(dd);
        heads[GUICompanion.DELETE_COMPANION.slot()] = del;
        // Cancel / close
        ItemStack close = ItemStack.of(GUICompanion.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        heads[GUICompanion.BUTTON_CLOSE.slot()] = close;

        return heads;
    }

    private String getPlayerNameFromMojang(String uuid) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                return json.get("name").getAsString();
            }
        } catch (Exception e) {
            plugin.debug("Could not get player name from Mojang!");
        }
        return "Unknown player";
    }
}
