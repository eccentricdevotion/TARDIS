/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.lazarus.disguise;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.SkinFetcher;
import org.bukkit.entity.Player;

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
        // get skin
        SkinFetcher getter = new SkinFetcher(TARDIS.plugin, uuid);
        getter.fetchAsync((hasResult, fetched) -> {
            if (hasResult) {
                JsonObject properties = fetched.getSkin();
                if (properties != null) {
                    // set skin
                    TARDIS.plugin.getSkinChanger().set(player, properties);
                }
            } else {
                TARDIS.plugin.debug("Player disguiser failed to fetch skin!");
            }
        });
    }
}
