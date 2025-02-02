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
package me.eccentric_nz.TARDIS.spigot;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import me.eccentric_nz.TARDIS.skins.ProfileChanger;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinChanger;
import me.eccentric_nz.TARDIS.skins.SkinFetcher;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkinChangerSpigot implements SkinChanger {

    public void set(Player player, Skin skin) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", skin.value(), skin.signature()));
        // set the game profile
        ProfileChanger.setPlayerProfile((CraftPlayer) player, profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public void set(Player player, JsonObject properties) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        // get value and signature
        String value = properties.get("value").getAsString();
        String signature = properties.get("signature").getAsString();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", value, signature));
        // set the game profile
        ProfileChanger.setPlayerProfile((CraftPlayer) player, profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public void remove(Player player) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        UUID uuid = player.getUniqueId();
        SkinFetcher getter = new SkinFetcher(TARDIS.plugin, uuid);
        getter.fetchAsync((hasResult, fetched) -> {
            if (hasResult) {
                JsonObject properties = fetched.getSkin();
                if (properties != null) {
                    String value = properties.get("value").getAsString();
                    String signature = properties.get("signature").getAsString();
                    profile.getProperties().removeAll("textures");
                    profile.getProperties().put("textures", new Property("textures", value, signature));
                    // set the game profile
                    ProfileChanger.setPlayerProfile((CraftPlayer) player, profile);
                    TARDISDisguiseTracker.DISGUISED_AS_PLAYER.remove(uuid);
                }
            }
        });
    }
}
