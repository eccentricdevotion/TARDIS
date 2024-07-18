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
package me.eccentric_nz.TARDIS.lazarus;

import io.papermc.lib.PaperLib;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.skins.ArchSkins;
import me.eccentric_nz.TARDIS.regeneration.SkinChangerPaper;
import me.eccentric_nz.TARDIS.regeneration.SkinChangerSpigot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TARDISLazarusDisguise {

    private final TARDIS plugin;
    private final Player player;
    private final EntityType entityType;
    private final Object[] options;

    public TARDISLazarusDisguise(TARDIS plugin, Player player, EntityType entityType, Object[] options) {
        this.plugin = plugin;
        this.player = player;
        this.entityType = entityType;
        this.options = options;
    }

    public static void removeDisguise(Player player) {
        TARDIS.plugin.getTardisHelper().undisguise(player);
    }

    public static void runImmortalityGate(Player player) {
        TARDIS.plugin.getServer().getOnlinePlayers().forEach((p) -> {
            if (!p.equals(player)) {
                TARDIS.plugin.getTardisHelper().disguise(p, player.getUniqueId());
            }
        });
    }

    public void createDisguise() {
        plugin.debug(entityType.toString());
        if (entityType.equals(EntityType.PLAYER)) {
            if (PaperLib.isPaper()) {
                SkinChangerPaper.set(player, ArchSkins.HEROBRINE);
            } else {
                plugin.debug("SkinChangerSpigot.set");
                SkinChangerSpigot.set(player, ArchSkins.HEROBRINE);
            }
        } else {
            plugin.getTardisHelper().disguise(entityType, player, options);
        }
    }
}
