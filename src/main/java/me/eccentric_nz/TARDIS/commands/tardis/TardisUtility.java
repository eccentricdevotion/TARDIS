package me.eccentric_nz.TARDIS.commands.tardis;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.DiskWriterCommand;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.monitor.MonitorUtils;
import me.eccentric_nz.TARDIS.rotors.TimeRotor;
import me.eccentric_nz.TARDIS.sonic.SonicDock;
import me.eccentric_nz.TARDIS.update.TARDISUpdateableChecker;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TardisUtility {

    public static Pair<Player, Integer> check(TARDIS plugin, CommandSender sender, TardisCommand tc) {
        if (sender instanceof Player player) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return null;
            }
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardisId()) && tc.noSiege()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CMD");
                return null;
            }
            return new Pair<>(player, rs.getTardisId());
        }
        return null;
    }

    public static Pair<Player, Tardis> updateCheck(TARDIS plugin, CommandSender sender, TardisCommand tc) {
        if (sender instanceof Player player) {
            HashMap<String, Object> where = new HashMap<>();
            UUID uuid = TARDISSudoTracker.SUDOERS.getOrDefault(player.getUniqueId(), player.getUniqueId());
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return null;
            }
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardis().getTardisId()) && tc.noSiege()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CMD");
                return null;
            }
            return new Pair<>(player, rs.getTardis());
        }
        return null;
    }

    public static void doSave(TARDIS plugin, Player player, String name, boolean preset) {
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.SAVES)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().equals(Material.MUSIC_DISC_FAR)) {
            new DiskWriterCommand(plugin).writeSaveToControlDisk(player, name);
        } else {
            if (plugin.getConfig().getBoolean("difficulty.disks") && !plugin.getUtils().inGracePeriod(player, true)) {
                if (plugin.getConfig().getBoolean("difficulty.disk_in_hand_for_write") && heldDiskIsWrong(itemStack, "Save Storage Disk")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_HAND_SAVE");
                    return;
                }
                new DiskWriterCommand(plugin).writeSave(player, name);
            } else {
                new SaveLocationCommand(plugin).doSave(player, name, preset);
            }
        }
    }

    public static boolean heldDiskIsWrong(ItemStack is, String dn) {
        boolean complexBool = false;
        if (is == null) {
            complexBool = true;
        } else if (!is.hasItemMeta()) {
            complexBool = true;
        } else if (!is.getItemMeta().hasDisplayName()) {
            complexBool = true;
        } else if (!ComponentUtils.endsWith(is.getItemMeta().displayName(), dn)) {
            complexBool = true;
        }
        return complexBool;
    }

    public static void update(TARDIS plugin, Player player, Updateable updateable, Tardis tardis) {
        if (updateable.equals(Updateable.STORAGE)) {
            // update note block if it's not BARRIER
            Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
            if (block.getType().equals(Material.NOTE_BLOCK) || block.getType().equals(Material.MUSHROOM_STEM)) {
                block.setBlockData(TARDISConstants.BARRIER, true);
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, block, tardis.getTardisId());
            }
        }
        String tardis_block = updateable.toString();
        if (new TARDISUpdateableChecker(plugin, updateable, player, tardis, tardis_block).canUpdate()) {
            plugin.getTrackerKeeper().getUpdatePlayers().put(player.getUniqueId(), tardis_block);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_CLICK", tardis_block);
            if (updateable.equals(Updateable.DIRECTION)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HOOK_REMIND");
            }
        }
    }

    public static void unlock(TARDIS plugin, Player player, Updateable updateable, Tardis tardis) {
        // get frame location
        ItemFrame itemFrame = null;
        switch (updateable) {
            case ROTOR -> itemFrame = TimeRotor.getItemFrame(tardis.getRotor());
            case MONITOR -> itemFrame = MonitorUtils.getItemFrameFromLocation(tardis.getTardisId(), true);
            case MONITOR_FRAME -> {
                itemFrame = MonitorUtils.getItemFrameFromLocation(tardis.getTardisId(), false);
                // reinstate display name
                ItemStack glass = itemFrame.getItem();
                ItemMeta im = glass.getItemMeta();
                im.displayName(ComponentUtils.toWhite("Monitor Frame"));
                glass.setItemMeta(im);
            }
            case SONIC_DOCK -> itemFrame = SonicDock.getItemFrame(tardis.getTardisId());
        }
        if (itemFrame != null) {
            TimeRotor.unlockItemFrame(itemFrame);
            // also need to remove the item frame protection
            plugin.getGeneralKeeper().getTimeRotors().remove(itemFrame.getUniqueId());
            // and block protection
            Block block = itemFrame.getLocation().getBlock();
            String location = block.getLocation().toString();
            plugin.getGeneralKeeper().getProtectBlockMap().remove(location);
            String under = block.getRelative(BlockFace.DOWN).getLocation().toString();
            plugin.getGeneralKeeper().getProtectBlockMap().remove(under);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROTOR_UNFIXED");
        }
    }
}
