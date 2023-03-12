package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TVMCommandBeacon implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TVMCommandBeacon(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmb")) {
            Player p = null;
            if (sender instanceof Player) {
                p = (Player) sender;
            }
            if (p == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!p.hasPermission("vm.beacon")) {
                p.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack is = p.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                int required = plugin.getConfig().getInt("tachyon_use.lifesigns");
                if (!TVMUtils.checkTachyonLevel(p.getUniqueId().toString(), required)) {
                    p.sendMessage(plugin.getPluginName() + "You don't have enough tachyons to set a beacon signal!");
                    return true;
                }
                UUID uuid = p.getUniqueId();
                String ustr = uuid.toString();
                Location l = p.getLocation();
                // potential griefing, we need to check the location first!
                List<Flag> flags = new ArrayList<>();
                if (plugin.getConfig().getBoolean("respect.factions")) {
                    flags.add(Flag.RESPECT_FACTIONS);
                }
                if (plugin.getConfig().getBoolean("respect.griefprevention")) {
                    flags.add(Flag.RESPECT_GRIEFPREVENTION);
                }
                if (plugin.getConfig().getBoolean("respect.towny")) {
                    flags.add(Flag.RESPECT_TOWNY);
                }
                if (plugin.getConfig().getBoolean("respect.worldborder")) {
                    flags.add(Flag.RESPECT_WORLDBORDER);
                }
                if (plugin.getConfig().getBoolean("respect.worldguard")) {
                    flags.add(Flag.RESPECT_WORLDGUARD);
                }
                Parameters params = new Parameters(p, flags);
                if (!plugin.getTardisAPI().getRespect().getRespect(l, params)) {
                    p.sendMessage(plugin.getPluginName() + "You are not permitted to set a beacon signal here!");
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
                plugin.getBeaconSetters().add(uuid);
                // remove tachyons
                qf.alterTachyons(ustr, -required);
                p.sendMessage(plugin.getPluginName() + "Beacon signal set, don't move!");
                return true;
            } else {
                p.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
