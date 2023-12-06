/*
 * Copyright (C) 2023 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.disguise;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class TARDISPlayerDisguiser {

    private final Player player;
    private final UUID uuid;

    public TARDISPlayerDisguiser(Player player, UUID uuid) {
        this.player = player;
        this.uuid = uuid;
        disguisePlayer();
    }

    public static void disguiseToPlayer(Player disguised, Player to) {
        to.hidePlayer(disguised);
        to.showPlayer(disguised);
    }

    public void disguisePlayer() {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        // set skin
        if (setSkin(entityPlayer.getGameProfile(), uuid) && !TARDISDisguiseTracker.DISGUISED_AS_PLAYER.contains(player.getUniqueId())) {
            TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
        }
    }

    private boolean setSkin(GameProfile profile, UUID uuid) {
        try {
            URL url = new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", fromUUID(uuid)));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) connection.getContent())); //Convert the input stream to a json element
                JsonObject rootobj = root.getAsJsonObject();
                JsonArray jsonArray = rootobj.getAsJsonArray("properties");
                JsonObject properties = jsonArray.get(0).getAsJsonObject();
                String skin = properties.get("value").getAsString();
                String signature = properties.get("signature").getAsString();
                profile.getProperties().removeAll("textures");
                return profile.getProperties().put("textures", new Property("textures", skin, signature));
            } else {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HELPER_WARNING, "Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disguiseToAll() {
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player && player.getWorld() == p.getWorld()) {
                p.hidePlayer(player);
                p.showPlayer(player);
            }
        }
    }

    private String fromUUID(final UUID value) {
        return value.toString().replace("-", "");
    }
}
