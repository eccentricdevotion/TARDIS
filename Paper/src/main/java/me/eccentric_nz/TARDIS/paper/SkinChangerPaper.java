/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinChanger;
import me.eccentric_nz.TARDIS.skins.SkinFetcher;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkinChangerPaper implements SkinChanger {

    public void set(Player player, Skin skin) {
        PlayerProfile profile = player.getPlayerProfile();
        // load skin
        profile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
        profile.getProperties().add(new ProfileProperty("textures", skin.value(), skin.signature()));
        // set the game profile
        player.setPlayerProfile(profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public void set(Player player, JsonObject properties) {
        PlayerProfile profile = player.getPlayerProfile();
        // get value and signature
        String value = properties.get("value").getAsString();
        String signature = properties.get("signature").getAsString();
        profile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
        profile.getProperties().add(new ProfileProperty("textures", value, signature));
        // set the game profile
        player.setPlayerProfile(profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public void remove(Player player) {
        PlayerProfile profile = player.getPlayerProfile();
        UUID uuid = player.getUniqueId();
        SkinFetcher getter = new SkinFetcher(TARDIS.plugin, uuid);
        getter.fetchAsync((hasResult, fetched) -> {
            if (hasResult) {
                JsonObject properties = fetched.getSkin();
                if (properties != null) {
                    String value = properties.get("value").getAsString();
                    String signature = properties.get("signature").getAsString();
                    profile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
                    profile.getProperties().add(new ProfileProperty("textures", value, signature));
                    // reset the game profile
                    player.setPlayerProfile(profile);
                    TARDISDisguiseTracker.DISGUISED_AS_PLAYER.remove(uuid);
                }
            }
        });
    }
}
