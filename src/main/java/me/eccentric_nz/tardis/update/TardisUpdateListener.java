/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.update;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.commands.sudo.TardisSudoTracker;
import me.eccentric_nz.tardis.custommodeldata.TardisMushroomBlockData;
import me.eccentric_nz.tardis.database.QueryFactory;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Control;
import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TardisStringUtils;
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
 * The tardis interior goes through occasional metamorphoses, sometimes by choice, sometimes for other reasons, such as
 * the Doctor's own regeneration. Some of these changes were physical in nature (involving secondary control rooms,
 * etc.), but it was also possible to re-arrange the interior design of the tardis with ease, using the Architectural
 * Configuration system.
 *
 * @author eccentric_nz
 */
public class TardisUpdateListener implements Listener {

    private final TardisPlugin plugin;

    public TardisUpdateListener(TardisPlugin plugin) {
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
        UUID playerUuid = player.getUniqueId();
        Updateable updateable;
        boolean secondary = false;
        if (plugin.getTrackerKeeper().getPlayers().containsKey(playerUuid)) {
            updateable = Updateable.valueOf(TardisStringUtils.toScoredUppercase(plugin.getTrackerKeeper().getPlayers().get(playerUuid)));
        } else if (plugin.getTrackerKeeper().getSecondary().containsKey(playerUuid)) {
            updateable = plugin.getTrackerKeeper().getSecondary().get(playerUuid);
            secondary = true;
        } else if (player.isSneaking() && plugin.getTrackerKeeper().getSecondaryRemovers().containsKey(playerUuid)) {
            Block block = event.getClickedBlock();
            // attempt to remove secondary control record
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", plugin.getTrackerKeeper().getSecondaryRemovers().get(playerUuid));
            assert block != null;
            String location = (block.getType().equals(Material.REPEATER)) ? TardisStaticLocationGetters.makeLocationStr(block.getLocation()) : block.getLocation().toString();
            wherec.put("location", location);
            wherec.put("secondary", 1);
            ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
            if (rsc.resultSet()) {
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("c_id", rsc.getcId());
                new QueryFactory(plugin).doDelete("controls", whered);
                TardisMessage.send(player, "SEC_REMOVED");
            } else {
                TardisMessage.send(player, "SEC_REMOVE_NO_MATCH");
            }
            plugin.getTrackerKeeper().getSecondaryRemovers().remove(playerUuid);
            return;
        } else {
            return;
        }
        // check they are still in the TARDIS world
        if (!updateable.equals(Updateable.BACKDOOR) && !plugin.getUtils().inTardisWorld(player)) {
            TardisMessage.send(player, "UPDATE_IN_WORLD");
            return;
        }
        String uuid = (TardisSudoTracker.SUDOERS.containsKey(playerUuid)) ? TardisSudoTracker.SUDOERS.get(playerUuid).toString() : playerUuid.toString();
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
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TardisMessage.send(player, "NO_TARDIS");
                return;
            }
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            HashMap<String, Object> tid = new HashMap<>();
            HashMap<String, Object> set = new HashMap<>();
            tid.put("tardis_id", id);
            String blockLocStr;
            if ((updateable.isControl())) {
                blockLocStr = block_loc.toString();
            } else {
                assert bw != null;
                blockLocStr = bw.getName() + ":" + bx + ":" + by + ":" + bz;
            }
            if (secondary) {
                plugin.getTrackerKeeper().getSecondary().remove(playerUuid);
            } else {
                plugin.getTrackerKeeper().getPlayers().remove(playerUuid);
            }
            if (!updateable.isAnyBlock() && !updateable.getMaterialChoice().getChoices().contains(blockType)) {
                TardisMessage.send(player, "UPDATE_BAD_CLICK", updateable.getName());
                return;
            }
            switch (updateable) {
                case BACKDOOR, DOOR -> new UpdateDoor(plugin).process(updateable, block, secondary, id, player);
                case GENERATOR -> plugin.getQueryFactory().insertControl(id, 24, blockLocStr, secondary ? 1 : 0);
                case DISPENSER -> plugin.getQueryFactory().insertControl(id, 28, blockLocStr, secondary ? 1 : 0);
                case TELEPATHIC -> {
                    plugin.getTrackerKeeper().getTelepathicPlacements().remove(playerUuid);
                    plugin.getQueryFactory().insertControl(id, 23, blockLocStr, secondary ? 1 : 0);
                    Block detector = block;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> detector.setBlockData(TardisConstants.DAYLIGHT), 3L);
                }
                case HANDBRAKE -> plugin.getQueryFactory().insertControl(id, 0, blockLocStr, secondary ? 1 : 0);
                case BEACON -> {
                    set.put("beacon", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                }
                case FARM, STABLE, STALL, VILLAGE -> {
                    set.put(updateable.getName(), blockLocStr);
                    plugin.getQueryFactory().doUpdate("farming", set, tid);
                }
                case CREEPER -> {
                    assert bw != null;
                    blockLocStr = bw.getName() + ":" + bx + ".5:" + by + ":" + bz + ".5";
                    set.put("creeper", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                }
                case EPS -> {
                    assert bw != null;
                    blockLocStr = bw.getName() + ":" + bx + ".5:" + (by + 1) + ":" + bz + ".5";
                    set.put("eps", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                }
                case RAIL -> {
                    set.put("rail", blockLocStr);
                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                }
                case CHAMELEON -> {
                    plugin.getQueryFactory().insertControl(id, 31, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign cs = (Sign) block.getState();
                    cs.setLine(0, plugin.getSigns().getStringList("chameleon").get(0));
                    cs.setLine(1, plugin.getSigns().getStringList("chameleon").get(1));
                    cs.setLine(2, "");
                    cs.setLine(3, tardis.getPreset().toString());
                    cs.update();
                }
                case KEYBOARD -> {
                    plugin.getQueryFactory().insertControl(id, 7, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign ks = (Sign) block.getState();
                    ks.setLine(0, plugin.getSigns().getStringList("keyboard").get(0));
                    for (int i = 1; i < 4; i++) {
                        ks.setLine(i, "");
                    }
                    ks.update();
                }
                case SAVE_SIGN -> {
                    plugin.getQueryFactory().insertControl(id, 32, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign ss = (Sign) block.getState();
                    ss.setLine(0, "TARDIS");
                    ss.setLine(1, plugin.getSigns().getStringList("saves").get(0));
                    ss.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                    ss.setLine(3, "");
                    ss.update();
                }
                case TERMINAL -> {
                    plugin.getQueryFactory().insertControl(id, 9, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign ts = (Sign) block.getState();
                    ts.setLine(0, "");
                    ts.setLine(1, plugin.getSigns().getStringList("terminal").get(0));
                    ts.setLine(2, plugin.getSigns().getStringList("terminal").get(1));
                    ts.setLine(3, "");
                    ts.update();
                }
                case CONTROL -> {
                    plugin.getQueryFactory().insertControl(id, 22, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign os = (Sign) block.getState();
                    os.setLine(0, "");
                    os.setLine(1, plugin.getSigns().getStringList("control").get(0));
                    os.setLine(2, plugin.getSigns().getStringList("control").get(1));
                    os.setLine(3, "");
                    os.update();
                }
                case ARS -> new UpdateArs(plugin).process(block, tardis.getSchematic(), id, uuid);
                case BACK -> {
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
                }
                case TEMPORAL -> {
                    plugin.getQueryFactory().insertControl(id, 11, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign es = (Sign) block.getState();
                    es.setLine(0, "");
                    es.setLine(1, plugin.getSigns().getStringList("temporal").get(0));
                    es.setLine(2, plugin.getSigns().getStringList("temporal").get(1));
                    es.setLine(3, "");
                    es.update();
                }
                case ADVANCED, STORAGE -> {
                    plugin.getQueryFactory().insertControl(id, Control.getUPDATE_CONTROLS().get(updateable.getName()), blockLocStr, secondary ? 1 : 0);
                    // check if player has storage record, and update the tardis_id field
                    plugin.getUtils().updateStorageId(uuid, id);
                    // always set the block type
                    int bd = (updateable.equals(Updateable.ADVANCED)) ? 50 : 51;
                    BlockData mushroom = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(bd));
                    block.setBlockData(mushroom, true);
                }
                case INFO -> {
                    plugin.getQueryFactory().insertControl(id, 13, blockLocStr, secondary ? 1 : 0);
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "-----");
                    s.setLine(1, "TARDIS");
                    s.setLine(2, plugin.getSigns().getStringList("info").get(0));
                    s.setLine(3, plugin.getSigns().getStringList("info").get(1));
                    s.update();
                }
                case ZERO -> plugin.getQueryFactory().insertControl(id, 16, blockLocStr, 0);
                case THROTTLE -> {
                    plugin.getQueryFactory().insertControl(id, 39, blockLocStr, secondary ? 1 : 0);
                    Block rblock = block;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Repeater repeater = (Repeater) rblock.getBlockData();
                        repeater.setLocked(true);
                        repeater.setDelay(4);
                        rblock.setBlockData(repeater);
                    }, 2L);
                }
                case SMELT, FUEL -> new TardisSmelterCommand(plugin).addDropChest(player, updateable, id, block);
                case VAULT -> new TardisVaultCommand(plugin).addDropChest(player, id, block);
                default -> plugin.getQueryFactory().insertControl(id, Control.getUPDATE_CONTROLS().get(updateable.getName()), blockLocStr, secondary ? 1 : 0);
            }
            if (!updateable.equals(Updateable.FUEL) && !updateable.equals(Updateable.SMELT)) {
                TardisMessage.send(player, "UPDATE_SET", updateable.getName());
            }
            TardisSudoTracker.SUDOERS.remove(playerUuid);
        }
    }
}
