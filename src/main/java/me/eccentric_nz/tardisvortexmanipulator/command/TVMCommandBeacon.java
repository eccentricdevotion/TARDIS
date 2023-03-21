package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TVMCommandBeacon {

    private final TARDIS plugin;

    public TVMCommandBeacon(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(Player player) {
        if (!TARDISPermission.hasPermission(player, "vm.beacon")) {
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        int required = plugin.getVortexConfig().getInt("tachyon_use.lifesigns");
        if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_BEACON_TACHYON");
            return true;
        }
        UUID uuid = player.getUniqueId();
        String ustr = uuid.toString();
        Location l = player.getLocation();
        // potential griefing, we need to check the location first!
        List<Flag> flags = new ArrayList<>();
        if (plugin.getConfig().getBoolean("preferences.respect_factions")) {
            flags.add(Flag.RESPECT_FACTIONS);
        }
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
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_BEACON_PERMIT");
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
        List<BlockFace> faces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST, BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST);
        faces.forEach((f) -> {
            qf.saveBeaconBlock(ustr, down.getRelative(f));
            down.getRelative(f).setBlockData(iron);
        });
        plugin.getTvmSettings().getBeaconSetters().add(uuid);
        // remove tachyons
        qf.alterTachyons(ustr, -required);
        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_BEACON_MOVE");
        return true;
    }
}
