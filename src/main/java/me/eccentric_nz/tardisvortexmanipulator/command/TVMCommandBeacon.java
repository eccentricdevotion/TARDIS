package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TVMCommandBeacon implements CommandExecutor {

    private final TARDIS plugin;

    public TVMCommandBeacon(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmb")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "CMD_PLAYER!");
                return true;
            }
            if (!player.hasPermission("vm.beacon")) {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
                return true;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
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
            } else {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_HAND");
                return true;
            }
        }
        return false;
    }
}
