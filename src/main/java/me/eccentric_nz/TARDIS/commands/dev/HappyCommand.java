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
package me.eccentric_nz.TARDIS.commands.dev;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.mobfarming.HappyGhastUtils;
import me.eccentric_nz.TARDIS.mobfarming.TARDISHappyGhast;
import me.eccentric_nz.TARDIS.rooms.happy.HappyLocations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HappyCommand {

    public void leash(Player player) {
        TARDISHappyGhast happy = new TARDISHappyGhast();
        happy.setHealth(20.0d);
        happy.setHarness(ItemStack.of(Material.CYAN_HARNESS, 1));
        happy.setName("Cuthbert");
        happy.setAge(7);
        happy.setBaby(false);
        Location location = player.getLocation().getBlock().getLocation();
        for (Pair<Vector, BlockFace> p : HappyLocations.VECTORS) {
            Location possible = location.clone().add(p.getFirst());
            HappyGhastUtils.setLeashed(possible, happy, p.getSecond());
        }
    }
}