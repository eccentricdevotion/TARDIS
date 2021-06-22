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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.ReplacedBlock;
import me.eccentric_nz.tardis.database.resultset.ResultSetBlocks;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisBlockSetters;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TardisMaterialisePoliceBox implements Runnable {

    private final TardisPlugin plugin;
    private final BuildData bd;
    private final int loops;
    private final Preset preset;
    private int task;
    private int i;
    private ItemFrame frame;
    private ItemStack is;

    TardisMaterialisePoliceBox(TardisPlugin plugin, BuildData bd, Preset preset) {
        this.plugin = plugin;
        this.bd = bd;
        loops = this.bd.getThrottle().getLoops();
        this.preset = preset;
        i = 0;
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisId())) {
            World world = bd.getLocation().getWorld();
            if (i < loops) {
                i++;
                int cmd = switch (i % 3) {
                    case 2 -> // stained
                            1003;
                    case 1 -> // glass
                            1004;
                    default -> // preset
                            1001;
                };
                // first run
                if (i == 1) {
                    TardisBuilderUtility.saveDoorLocation(bd);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisId());
                    boolean found = false;
                    assert world != null;
                    for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                        if (e instanceof ItemFrame) {
                            frame = (ItemFrame) e;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        Block block = bd.getLocation().getBlock();
                        Block under = block.getRelative(BlockFace.DOWN);
                        block.setBlockData(TardisConstants.AIR);
                        TardisBlockSetters.setUnderDoorBlock(world, under.getX(), under.getY(), under.getZ(), bd.getTardisId(), false);
                        // spawn item frame
                        frame = (ItemFrame) world.spawnEntity(bd.getLocation(), EntityType.ITEM_FRAME);
                    }
                    frame.setFacingDirection(BlockFace.UP);
                    frame.setRotation(bd.getDirection().getRotation());
                    Material dye = TardisBuilderUtility.getMaterialForItemFrame(preset);
                    is = new ItemStack(dye, 1);
                    if (bd.isOutside()) {
                        if (!bd.useMinecartSounds()) {
                            String sound;
                            if (preset.equals(Preset.JUNK_MODE)) {
                                sound = "junk_land";
                            } else {
                                sound = switch (bd.getThrottle()) {
                                    case WARP, RAPID, FASTER -> "tardis_land_" + bd.getThrottle().toString().toLowerCase();
                                    default -> // NORMAL
                                            "tardis_land";
                                };
                            }
                            TardisSounds.playTARDISSound(bd.getLocation(), sound);
                        } else {
                            world.playSound(bd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                        }
                    }
                }
                ItemMeta im = is.getItemMeta();
                assert im != null;
                im.setCustomModelData(cmd);
                if (bd.shouldAddSign()) {
                    String pb = (preset.equals(Preset.WEEPING_ANGEL)) ? "Weeping Angel" : "Police Box";
                    im.setDisplayName(bd.getPlayer().getName() + "'s " + pb);
                }
                is.setItemMeta(im);
                frame.setItem(is, false);
                frame.setFixed(true);
                frame.setVisible(false);
            } else {
                // remove trackers
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisId()));
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisId()));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                plugin.getTrackerKeeper().getMalfunction().remove(bd.getTardisId());
                if (plugin.getTrackerKeeper().getDidDematToVortex().contains(bd.getTardisId())) {
                    plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(bd.getTardisId()));
                }
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisId())) {
                    int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(bd.getTardisId());
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisId());
                }
                if (!bd.isRebuild()) {
                    plugin.getTrackerKeeper().getActiveForceFields().remove(Objects.requireNonNull(bd.getPlayer().getPlayer()).getUniqueId());
                }
                // message travellers in tardis
                if (loops > 3) {
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", bd.getTardisId());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
                    if (rst.resultSet()) {
                        List<UUID> travellers = rst.getData();
                        travellers.forEach((s) -> {
                            Player p = plugin.getServer().getPlayer(s);
                            if (p != null) {
                                String message = (bd.isMalfunction()) ? "MALFUNCTION" : "HANDBRAKE_LEFT_CLICK";
                                TardisMessage.send(p, message);
                                // TARDIS has travelled so add players to list so they can receive Artron on exit
                                plugin.getTrackerKeeper().getHasTravelled().add(s);
                            }
                        });
                    } else if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(bd.getPlayer().getUniqueId())) {
                        TardisMessage.send(bd.getPlayer().getPlayer(), "JUNK_HANDBRAKE_LEFT_CLICK");
                    }
                    // restore beacon up block if present
                    HashMap<String, Object> whereb = new HashMap<>();
                    whereb.put("tardis_id", bd.getTardisId());
                    whereb.put("police_box", 2);
                    ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, false);
                    if (rs.resultSet()) {
                        ReplacedBlock rb = rs.getReplacedBlock();
                        TardisBlockSetters.setBlock(rb.getLocation(), rb.getBlockData());
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", bd.getTardisId());
                        whered.put("police_box", 2);
                        plugin.getQueryFactory().doDelete("blocks", whered);
                    }
                    // tardis has moved so remove HADS damage count
                    plugin.getTrackerKeeper().getDamage().remove(bd.getTardisId());
                    // update demat field in database
                    TardisBuilderUtility.updateChameleonDemat(preset.toString(), bd.getTardisId());
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
