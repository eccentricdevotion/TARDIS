/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.SCHEMATIC;
import me.eccentric_nz.TARDIS.builders.TARDISBuildData;
import me.eccentric_nz.TARDIS.builders.TARDISSeedBlockProcessor;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.rooms.TARDISWallsLookup;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSeedBlockListener implements Listener {

    private final TARDIS plugin;
    private final TARDISWallsLookup twl;
    private final HashMap<Location, TARDISBuildData> trackTARDISSeed = new HashMap<Location, TARDISBuildData>();
    private final List<Integer> hasColour = new ArrayList<Integer>();

    public TARDISSeedBlockListener(TARDIS plugin) {
        this.plugin = plugin;
        twl = new TARDISWallsLookup(plugin);
        hasColour.addAll(Arrays.asList(new Integer[]{5, 17, 35, 95, 159, 162}));
    }

    /**
     * Store the TARDIS Seed block's values for use when clicked with the TARDIS
     * key to activate growing, or to return the block if broken.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSeedBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        ItemStack is = player.getItemInHand();
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasLore()) {
            return;
        }
        if (im.getDisplayName().equals("§6TARDIS Seed Block")) {
            List<String> lore = im.getLore();
            SCHEMATIC schm = SCHEMATIC.valueOf(lore.get(0));
            TwoValues wall_data = getValuesFromWallString(lore.get(1));
            TwoValues floor_data = getValuesFromWallString(lore.get(2));
            TwoValues cham_data = getValuesFromString(lore.get(3));
            TwoValues lamp_data = getValuesFromString(lore.get(4));
            TARDISBuildData seed = new TARDISBuildData();
            seed.setSchematic(schm);
            seed.setWall_id(wall_data.getId());
            seed.setWall_data(wall_data.getData());
            seed.setFloor_id(floor_data.getId());
            seed.setFloor_data(floor_data.getData());
            seed.setBox_id(cham_data.getId());
            seed.setBox_data(cham_data.getData());
            seed.setLamp(lamp_data.getId());
            Location l = event.getBlockPlaced().getLocation();
            trackTARDISSeed.put(l, seed);
            player.sendMessage(plugin.pluginName + "You placed a TARDIS seed block!");
            // now the player has to click the block with the TARDIS key
        }
    }

    /**
     * Return the TARDIS seed block to the player after it is broken.
     *
     * @param event a block break event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("deprecation")
    public void onSeedBlockBreak(BlockBreakEvent event) {
        Location l = event.getBlock().getLocation();
        Player p = event.getPlayer();
        if (trackTARDISSeed.containsKey(l)) {
            if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                // get the Seed block data
                TARDISBuildData data = trackTARDISSeed.get(l);
                // drop a TARDIS Seed Block
                World w = l.getWorld();
                ItemStack is = new ItemStack(event.getBlock().getType(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName("§6TARDIS Seed Block");
                List<String> lore = new ArrayList<String>();
                lore.add(data.getSchematic().toString());
                lore.add("Walls: " + twl.wall_lookup.get(data.getWall_id() + ":" + data.getWall_data()));
                lore.add("Floors: " + twl.wall_lookup.get(data.getFloor_id() + ":" + data.getFloor_data()));
// do some funky stuff to get data values for wool/stained glass & clay/wood/log/log_2
                if (hasColour.contains(data.getBox_id())) {
                    switch (data.getBox_id()) {
                        case 35:
                        case 95:
                        case 159:
                            lore.add("Chameleon block: " + DyeColor.getByWoolData(data.getBox_data()) + " " + Material.getMaterial(data.getBox_id()).toString());
                            break;
                        default:
                            lore.add("Chameleon block: " + plugin.utils.getWoodType(Material.getMaterial(data.getBox_id()), data.getBox_data()) + " " + Material.getMaterial(data.getBox_id()).toString());
                    }
                } else {
                    lore.add("Chameleon block: " + Material.getMaterial(data.getBox_id()).toString());
                }
//                lore.add("Chameleon block: " + ((data.getBox_id() == 35 || data.getBox_id() == 159) ? DyeColor.getByWoolData(data.getBox_data()) + " " : "") + Material.getMaterial(data.getBox_id()).toString());
                lore.add("Lamp: " + Material.getMaterial(data.getLamp()).toString());
                im.setLore(lore);
                is.setItemMeta(im);
                // set the block to AIR
                event.getBlock().setType(Material.AIR);
                w.dropItemNaturally(l, is);
            }
            trackTARDISSeed.remove(l);
        }
    }

    /**
     * Process the TARDIS seed block and turn it into a TARDIS!
     *
     * @param event a block interact event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSeedInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Location l = event.getClickedBlock().getLocation();
            if (trackTARDISSeed.containsKey(l)) {
                Player player = event.getPlayer();
                String key;
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("player", player.getName());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
                if (rsp.resultSet()) {
                    key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("key");
                } else {
                    key = plugin.getConfig().getString("key");
                }
                if (player.getItemInHand().getType().equals(Material.getMaterial(key))) {
                    // grow a TARDIS
                    TARDISBuildData seed = trackTARDISSeed.get(l);
                    // process seed data
                    if (new TARDISSeedBlockProcessor(plugin).processBlock(seed, l, player)) {
                        // remove seed data
                        trackTARDISSeed.remove(l);
                        // remove seed block
                        event.getClickedBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }

    /**
     * Determines the id and data values of the block. Values are calculated by
     * converting the string values stored in a TARDIS Seed block.
     *
     * @param str the lore stored in the TARDIS Seed block's Item Meta
     * @return an int and a byte stored in a simple data class
     */
    private TwoValues getValuesFromString(String str) {
        TwoValues data = new TwoValues();
        String[] split1 = str.split(": ");
        String[] split2 = split1[1].split(" ");
        if (split2.length > 1) {
            Material m = Material.getMaterial(split2[1]);
            data.setId(m.getId());
            if (m.equals(Material.WOOL) || m.equals(Material.STAINED_CLAY) || m.equals(Material.STAINED_GLASS)) {
                data.setData(DyeColor.valueOf(split2[0]).getWoolData());
            } else {
                data.setData(getWoodDataType(m, split2[0]));
            }
        } else {
            data.setId(Material.getMaterial(split1[1]).getId());
            data.setData((byte) 0);
        }
        return data;
    }

    /**
     * Determines the id and data values of the block. Values are calculated by
     * converting the string values stored in a TARDIS Seed block.
     *
     * @param str the lore stored in the TARDIS Seed block's Item Meta
     * @return an int and a byte stored in a simple data class
     */
    private TwoValues getValuesFromWallString(String str) {
        TwoValues data = new TwoValues();
        String[] split = str.split(": ");
        int[] values = plugin.tw.blocks.get(split[1]);
        data.setId(values[0]);
        data.setData((byte) values[1]);
        return data;
    }

    private byte getWoodDataType(Material m, String w) {
        byte b = (byte) 0;
        switch (m) {
            case LOG:
                if (w.equals("SPRUCE")) {
                    b = (byte) 1;
                }
                if (w.equals("BIRCH")) {
                    b = (byte) 2;
                }
                if (w.equals("JUNGLE")) {
                    b = (byte) 3;
                }
                break;
            case LOG_2:
                if (w.equals("DARK_OAK")) {
                    b = (byte) 1;
                }
                break;
            default:
                if (w.equals("SPRUCE")) {
                    b = (byte) 1;
                }
                if (w.equals("BIRCH")) {
                    b = (byte) 2;
                }
                if (w.equals("JUNGLE")) {
                    b = (byte) 3;
                }
                if (w.equals("ACACIA")) {
                    b = (byte) 4;
                }
                if (w.equals("DARK_OAK")) {
                    b = (byte) 5;
                }
                break;
        }
        return b;
    }

    /**
     * Simple inner class to store two variables returned by the
     * getValuesFromString() method.
     */
    public class TwoValues {

        public TwoValues() {
        }
        private int id;
        private byte data;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public byte getData() {
            return data;
        }

        public void setData(byte data) {
            this.data = data;
        }
    }
}
