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
package me.eccentric_nz.tardischunkgenerator.helpers;

import io.papermc.lib.PaperLib;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

/**
 *
 * @author eccentric_nz
 */
public class WaxedHelper {

    public static void setWaxed(Sign sign) {
        if (PaperLib.isPaper()) {
            Location l = sign.getLocation();
            ServerLevel world = ((CraftWorld) sign.getWorld()).getHandle();
            SignBlockEntity sbe = (SignBlockEntity) world.getBlockEntity(new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
            sbe.setWaxed(true);
        } else {
            sign.setWaxed(true);
        }
    }
}
