/*
 * Copyright (C) 2023 eccentric_nz
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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;
import java.util.regex.Pattern;

public class TARDISFloodgate {

    private static final Pattern VALID_KEY = Pattern.compile("[a-z0-9/._-]+");

    public static boolean isBedrockPlayer(UUID uuid) {
        return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
    }

    public static boolean isBedrockPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        return player != null && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }

    public static boolean isFloodgateEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("floodgate");
    }

    public static String sanitisePlayerName(String name) {
        String replace = "^" + FloodgateApi.getInstance().getPlayerPrefix();
        return name.replaceFirst(replace, "");
    }

    public static String getPlayerWorldName(String name) {
        return "TARDIS_WORLD_" + name.replaceFirst(FloodgateApi.getInstance().getPlayerPrefix(), ".");
    }

    public static boolean shouldReplacePrefix(String name) {
        if (!isFloodgateEnabled()) {
            return false;
        }
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return false;
        }
        if (!isBedrockPlayer(player.getUniqueId())) {
            return false;
        }
        return (!VALID_KEY.matcher(player.getName()).matches());
    }

    public static boolean shouldReplacePrefix(UUID uuid) {
        if (!isFloodgateEnabled()) {
            return false;
        }
        if (!isBedrockPlayer(uuid)) {
            return false;
        }
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return false;
        }
        return (!VALID_KEY.matcher(player.getName()).matches());
    }

    public static void sendControlForm(UUID uuid) {
        new FloodgateControlForm(uuid).send();
    }
}
