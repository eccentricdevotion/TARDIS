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
package me.eccentric_nz.TARDIS.update;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.UUID;

/**
 * The TARDIS interior goes through occasional metamorphoses, sometimes by choice, sometimes for other reasons, such as
 * the Doctor's own regeneration. Some of these changes were physical in nature (involving secondary control rooms,
 * etc.), but it was also possible to re-arrange the interior design of the TARDIS with ease, using the Architectural
 * Configuration system.
 *
 * @author eccentric_nz
 */
public class TARDISUpdateListener implements Listener {

    private final TARDIS plugin;

    public TARDISUpdateListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with the TARDIS console and other specific items. If the block is clicked and
     * players name is contained in the appropriate HashMap, then the block's position is recorded in the database.
     *
     * @param event a player clicking on a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUpdateInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Updateable updateable;
        boolean secondary = false;
        if (plugin.getTrackerKeeper().getPlayers().containsKey(uuid)) {
            updateable = Updateable.valueOf(TARDISStringUtils.toScoredUppercase(plugin.getTrackerKeeper().getPlayers().get(uuid)));
        } else if (plugin.getTrackerKeeper().getSecondary().containsKey(uuid)) {
            updateable = plugin.getTrackerKeeper().getSecondary().get(uuid);
            secondary = true;
        } else if (player.isSneaking() && plugin.getTrackerKeeper().getSecondaryRemovers().containsKey(uuid)) {
            Block block = event.getClickedBlock();
            // attempt to remove secondary control record
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", plugin.getTrackerKeeper().getSecondaryRemovers().get(uuid));
            String location = (block.getType().equals(Material.REPEATER)) ? TARDISStaticLocationGetters.makeLocationStr(block.getLocation()) : block.getLocation().toString();
            wherec.put("location", location);
            wherec.put("secondary", 1);
            ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
            if (rsc.resultSet()) {
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("c_id", rsc.getC_id());
                new QueryFactory(plugin).doDelete("controls", whered);
                TARDISMessage.send(player, "SEC_REMOVED");
            } else {
                TARDISMessage.send(player, "SEC_REMOVE_NO_MATCH");
            }
            plugin.getTrackerKeeper().getSecondaryRemovers().remove(uuid);
            return;
        } else {
            return;
        }
        // check they are still in the TARDIS world
        if (!updateable.equals(Updateable.BACKDOOR) && !plugin.getUtils().inTARDISWorld(player)) {
            TARDISMessage.send(player, "UPDATE_IN_WORLD");
            return;
        }
        String playerUUID = uuid.toString();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Location block_loc = block.getLocation();
            World bw = block_loc.getWorld();
            int bx = block_loc.getBlockX();
            int by = block_loc.getBlockY();
            int bz = block_loc.getBlockZ();
            BlockData data = block.getBlockData();
            if (blockType.equals(Material.IRON_DOOR)) {
                Bisected bisected = (Bisected) data;
                if (bisected.getHalf().equals(Half.TOP)) {
                    by = (by - 1);
                    block = block.getRelative(BlockFace.DOWN);
                }
            }
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", playerUUID);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return;
            }
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardis_id();
            HashMap<String, Object> tid = new HashMap<>();
            HashMap<String, Object> set = new HashMap<>();
            tid.put("tardis_id", id);
            String blockLocStr = (updateable.isControl()) ? block_loc.toString() : bw.getName() + ":" + bx + ":" + by + ":" + bz;
            if (secondary) {
                plugin.getTrackerKeeper().getSecondary().remove(uuid);
            } else {
                plugin.getTrackerKeeper().getPlayers().remove(uuid);
            }
            if (!updateable.isAnyBlock() && !updateable.getMaterialChoice().getChoices().contains(blockType)) {
                TARDISMessage.send(player, "UPDATE_BAD_CLICK", updateable.getName());
                return;
            }
            switch (updateable) {
                case BACKDOOR:
                case DOOR:
                    new UpdateDoor(plugin).process(updateable, block, secondary, id, player);
                    break;
                case GENERATOR:
                    plugin.getQueryFactory().insertControl(id, 24, blockLocStr, secondary ? 1 : 0);
                    break;
                case DISPENSER:
                    plugin.getQueryFactory().insertControl(id, 28, blockLocStr, secondary ? 1 : 0);
                    break;
                case TELEPATHIC:
                    plugin.getTrackerKeeper().getTelepathicPlacements().remove(uuid);
                    plugin.getQueryFactory().insertControl(id, 23, blockLocStr, secondary ? 1 : 0);
                    Block detector = block;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> detector.setBlockData(TARDISConstants.DAYLIGHT), 3L);
                    break;
                case HANDBRAKE:
                    plugin.getQueryFactory().insertControl(id, 0, blockLocStr, secondary ? 1 : 0);
                    break;
                case BEACON:
                    set.put("beacon", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                    break;
                case FARM:
                case STABLE:
                case STALL:
                case VILLAGE:
                    set.put(updateable.getName(), blockLocStr);
                    plugin.getQueryFactory().doUpdate("farming", set, tid);
                    break;
                case CREEPER:
                    blockLocStr = bw.getName() + ":" + bx + ".5:" + by + ":" + bz + ".5";
                    set.put("creeper", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                    break;
                case EPS:
                    blockLocStr = bw.getName() + ":" + bx + ".5:" + (by + 1) + ":" + bz + ".5";
                    set.put("eps", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                    break;
                case RAIL:
                    set.put("rail", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                    break;
                case CHAMELEON:
                    plugin.getQueryFactory().insertControl(id, 31, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign cs = (Sign) block.getState();
                    cs.setLine(0, plugin.getSigns().getStringList("chameleon").get(0));
                    cs.setLine(1, plugin.getSigns().getStringList("chameleon").get(1));
                    cs.setLine(2, "");
                    cs.setLine(3, tardis.getPreset().toString());
                    cs.update();
                    break;
                case KEYBOARD:
                    plugin.getQueryFactory().insertControl(id, 7, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign ks = (Sign) block.getState();
                    ks.setLine(0, plugin.getSigns().getStringList("keyboard").get(0));
                    for (int i = 1; i < 4; i++) {
                        ks.setLine(i, "");
                    }
                    ks.update();
                    break;
                case SAVE_SIGN:
                    plugin.getQueryFactory().insertControl(id, 32, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign ss = (Sign) block.getState();
                    ss.setLine(0, "TARDIS");
                    ss.setLine(1, plugin.getSigns().getStringList("saves").get(0));
                    ss.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                    ss.setLine(3, "");
                    ss.update();
                    break;
                case TERMINAL:
                    plugin.getQueryFactory().insertControl(id, 9, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign ts = (Sign) block.getState();
                    ts.setLine(0, "");
                    ts.setLine(1, plugin.getSigns().getStringList("terminal").get(0));
                    ts.setLine(2, plugin.getSigns().getStringList("terminal").get(1));
                    ts.setLine(3, "");
                    ts.update();
                    break;
                case CONTROL:
                    plugin.getQueryFactory().insertControl(id, 22, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign os = (Sign) block.getState();
                    os.setLine(0, "");
                    os.setLine(1, plugin.getSigns().getStringList("control").get(0));
                    os.setLine(2, plugin.getSigns().getStringList("control").get(1));
                    os.setLine(3, "");
                    os.update();
                    break;
                case ARS:
                    new UpdateARS(plugin).process(block, tardis.getSchematic(), id, playerUUID);
                    break;
                case BACK:
                    plugin.getQueryFactory().insertControl(id, 8, blockLocStr, secondary ? 1 : 0);
                    // insert current into back
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", id);
                    ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
                    if (rscl.resultSet()) {
                        HashMap<String, Object> setb = new HashMap<>();
                        setb.put("world", rscl.getWorld().getName());
                        setb.put("x", rscl.getX());
                        setb.put("y", rscl.getY());
                        setb.put("z", rscl.getZ());
                        setb.put("direction", rscl.getDirection().toString());
                        setb.put("submarine", (rscl.isSubmarine()) ? 1 : 0);
                        HashMap<String, Object> whereb = new HashMap<>();
                        whereb.put("tardis_id", id);
                        plugin.getQueryFactory().doUpdate("back", setb, whereb);
                    }
                    break;
                case TEMPORAL:
                    plugin.getQueryFactory().insertControl(id, 11, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign es = (Sign) block.getState();
                    es.setLine(0, "");
                    es.setLine(1, plugin.getSigns().getStringList("temporal").get(0));
                    es.setLine(2, plugin.getSigns().getStringList("temporal").get(1));
                    es.setLine(3, "");
                    es.update();
                    break;
                case ADVANCED:
                case STORAGE:
                    plugin.getQueryFactory().insertControl(id, Control.getUPDATE_CONTROLS().get(updateable.getName()), blockLocStr, secondary ? 1 : 0);
                    // check if player has storage record, and update the tardis_id field
                    plugin.getUtils().updateStorageId(playerUUID, id);
                    // check the block type
                    if (!blockType.equals(Material.MUSHROOM_STEM)) {
                        int bd = (updateable.equals(Updateable.ADVANCED)) ? 50 : 51;
                        BlockData mushroom = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(bd));
                        block.setBlockData(mushroom, true);
                    }
                    break;
                case INFO:
                    plugin.getQueryFactory().insertControl(id, 13, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "-----");
                    s.setLine(1, "TARDIS");
                    s.setLine(2, plugin.getSigns().getStringList("info").get(0));
                    s.setLine(3, plugin.getSigns().getStringList("info").get(1));
                    s.update();
                    break;
                case ZERO:
                    plugin.getQueryFactory().insertControl(id, 16, blockLocStr, 0);
                    break;
                case THROTTLE:
                    plugin.getQueryFactory().insertControl(id, 39, blockLocStr, secondary ? 1 : 0);
                    Block rblock = block;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Repeater repeater = (Repeater) rblock.getBlockData();
                        repeater.setLocked(true);
                        repeater.setDelay(4);
                        rblock.setBlockData(repeater);
                    }, 2L);
                    break;
                default:
                    plugin.getQueryFactory().insertControl(id, Control.getUPDATE_CONTROLS().get(updateable.getName()), blockLocStr, secondary ? 1 : 0);
                    break;
            }
            TARDISMessage.send(player, "UPDATE_SET", updateable.getName());
        }
    }
}
