/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.lazarus;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TardisLazarusDisguise {

    private final TardisPlugin plugin;
    private final Player player;
    private final EntityType entityType;
    private final Object[] options;

    public TardisLazarusDisguise(TardisPlugin plugin, Player player, EntityType entityType, Object[] options) {
        this.plugin = plugin;
        this.player = player;
        this.entityType = entityType;
        this.options = options;
    }

    public static void removeDisguise(Player player) {
        TardisPlugin.plugin.getTardisHelper().undisguise(player);
    }

    public static void runImmortalityGate(Player player) {
        TardisPlugin.plugin.getServer().getOnlinePlayers().forEach((p) -> {
            if (!p.equals(player)) {
                TardisPlugin.plugin.getTardisHelper().disguise(p, player.getUniqueId());
            }
        });
    }

    public void createDisguise() {
        if (entityType.equals(EntityType.PLAYER)) {
            plugin.getTardisHelper().disguise(player, UUID.fromString("f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2")); // or 91f25eb5-2b0e-46bc-8437-401c6017f369
        } else {
            plugin.getTardisHelper().disguise(entityType, player, options);
        }
    }
}
