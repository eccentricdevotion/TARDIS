/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TVMCommandBeacon {

    private final TARDIS plugin;

    public TVMCommandBeacon(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(Player player) {
        if (!TARDISPermission.hasPermission(player, "vm.beacon")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        int required = plugin.getVortexConfig().getInt("tachyon_use.lifesigns");
        if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_BEACON_TACHYON");
            return true;
        }
        UUID uuid = player.getUniqueId();
        String ustr = uuid.toString();
        Location l = player.getLocation();
        // potential griefing, we need to check the location first!
        List<Flag> flags = new ArrayList<>();
        if (plugin.getConfig().getBoolean("preferences.respect_griefprevention")) {
            flags.add(Flag.RESPECT_GRIEFPREVENTION);
        }
        if (plugin.getConfig().getBoolean("preferences.respect_towny")) {
            flags.add(Flag.RESPECT_TOWNY);
        }
        if (plugin.getConfig().getBoolean("preferences.respect_worldborder")) {
            flags.add(Flag.RESPECT_WORLDBORDER);
        }
        if (plugin.getConfig().getBoolean("preferences.respect_worldguard")) {
            flags.add(Flag.RESPECT_WORLDGUARD);
        }
        Parameters params = new Parameters(player, flags);
        if (!plugin.getTardisAPI().getRespect().getRespect(l, params)) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_BEACON_PERMIT");
            return true;
        }
        Block b = l.getBlock().getRelative(BlockFace.DOWN);
        TVMQueryFactory qf = new TVMQueryFactory(plugin);
        qf.saveBeaconBlock(ustr, b);
        b.setBlockData(Material.BEACON.createBlockData());
        Block down = b.getRelative(BlockFace.DOWN);
        qf.saveBeaconBlock(ustr, down);
        BlockData iron = Material.IRON_BLOCK.createBlockData();
        down.setBlockData(iron);
        plugin.getGeneralKeeper().getSurrounding().forEach((f) -> {
            qf.saveBeaconBlock(ustr, down.getRelative(f));
            down.getRelative(f).setBlockData(iron);
        });
        plugin.getTvmSettings().getBeaconSetters().add(uuid);
        // remove tachyons
        qf.alterTachyons(ustr, -required);
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_BEACON_MOVE");
        return true;
    }
}
