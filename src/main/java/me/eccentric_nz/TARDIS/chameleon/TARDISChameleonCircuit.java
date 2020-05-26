/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * The Chameleon Circuit controls the exterior shell's Cloaking Device. The Cloaking Device allows the shape, color,
 * mass and texture of the shell to be altered to blend in with its surroundings. This feature is designed to help
 * prevent changes to history.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonCircuit {

    private final TARDIS plugin;

    public TARDISChameleonCircuit(TARDIS plugin) {
        this.plugin = plugin;
    }

    public Material getChameleonBlock(Block b, OfflinePlayer p) {
        Material chameleonType = b.getType();
        Material wall_block = Material.LIGHT_GRAY_TERRACOTTA;
        // determine wall_block
        if (plugin.getBlocksConfig().getStringList("chameleon_blocks").contains(chameleonType.toString())) {
            wall_block = chameleonType;
        }
        if (TARDISConstants.CHAMELEON_BLOCKS_BAD.contains(chameleonType)) {
            TARDISMessage.send(p.getPlayer(), "CHAM_NOT_ENGAGE");
        }
        if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains(chameleonType)) {
            wall_block = swapMaterial(chameleonType);
        }
        if (TARDISConstants.CHAMELEON_BLOCKS_NEXT.contains(chameleonType)) {
            // try the surrounding blocks
            for (BlockFace bf : plugin.getGeneralKeeper().getSurrounding()) {
                Block surroundblock = b.getRelative(bf);
                Material emat = surroundblock.getType();
                if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains(emat)) {
                    wall_block = emat;
                    break;
                }
                if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains(emat)) {
                    wall_block = swapMaterial(emat);
                    break;
                }
            }
        }
        // if it's a precious block or TNT and all_blocks is false, then switch it to wool of similar colour
        if (TARDISConstants.CHAMELEON_BLOCKS_PRECIOUS.contains(chameleonType) && !plugin.getConfig().getBoolean("allow.all_blocks")) {
            switch (chameleonType) {
                case IRON_BLOCK:
                    wall_block = Material.YELLOW_WOOL;
                    break;
                case DIAMOND_BLOCK:
                    wall_block = Material.LIGHT_BLUE_WOOL;
                    break;
                case EMERALD_BLOCK:
                    wall_block = Material.LIME_WOOL;
                    break;
                case REDSTONE_BLOCK:
                case TNT:
                    wall_block = Material.RED_WOOL;
                    break;
                case COAL_BLOCK:
                    wall_block = Material.BLACK_WOOL;
                    break;
                default: // GOLD_BLOCK and any others
                    wall_block = Material.BLUE_WOOL;
                    break;
            }
        }
        return wall_block;
    }

    private Material swapMaterial(Material material) {
        return TARDISConstants.CHAMELEON_BLOCKS_CHANGE_HASH.get(material);
    }
}
