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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.*;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LazarusUtils {

    public static final Set<UUID> pagers = new HashSet<>();
    public static final List<String> twaHelmets = List.of("Dalek Head", "Davros Head", "K9 Head", "Toclafane");

    public static void openDoor(Block b) {
        b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
        b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
    }

    public static void twaOff(Player player) {
        UUID uuid = player.getUniqueId();
        if (SkinUtils.SKINNED.containsKey(uuid)) {
            // remove skin
            TARDIS.plugin.getSkinChanger().remove(player);
            Skin skin = SkinUtils.SKINNED.get(uuid);
            SkinUtils.removeExtras(player, skin);
            SkinUtils.SKINNED.remove(uuid);
        } else {
            ItemStack helmet = player.getInventory().getHelmet();
            if (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasDisplayName()) {
                String metaName = ComponentUtils.stripColour(helmet.getItemMeta().displayName());
                if (twaHelmets.contains(metaName)) {
                    TARDIS.plugin.getServer().dispatchCommand(TARDIS.plugin.getConsole(), "twa disguise WEEPING_ANGEL off " + uuid);
                }
            }
        }
    }

    public static Skin skinForSlot(int slot) {
        Skin skin;
        if (slot >= 0 && slot < 16) {
            skin = DoctorSkins.DOCTORS.get(slot);
        } else if (slot >= 16 && slot < 35) {
            skin = CompanionSkins.COMPANIONS.get(slot - 16);
        } else {
            skin = CharacterSkins.LAZARUS_CHARACTERS.get(slot - 35);
        }
        return skin;
    }
}
