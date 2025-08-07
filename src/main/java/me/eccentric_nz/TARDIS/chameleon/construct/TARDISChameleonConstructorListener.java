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
package me.eccentric_nz.TARDIS.chameleon.construct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.gui.TARDISChameleonHelpGUI;
import me.eccentric_nz.TARDIS.chameleon.gui.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISStainedGlassLookup;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChameleon;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISChameleonConstructorListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<Material> doors = new ArrayList<>();
    private final List<Material> lamps = new ArrayList<>();
    private final HashMap<UUID, Integer> currentDoor = new HashMap<>();
    private final HashMap<UUID, Integer> currentLamp = new HashMap<>();
    private final int dn;
    private final int ln;

    public TARDISChameleonConstructorListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        doors.addAll(Tag.DOORS.getValues());
        dn = doors.size();
        plugin.getBlocksConfig().getStringList("lamp_blocks").forEach((s) -> {
            try {
                lamps.add(Material.valueOf(s));
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid Material in lamp_blocks section.");
            }
        });
        ln = lamps.size();
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)

    public void onChameleonConstructorClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISChameleonConstructorGUI)) {
            return;
        }
        InventoryView view = event.getView();
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot >= 0 && (slot < 18 || slot == 26 || slot == 43 || slot == 52)) {
            event.setCancelled(true);
            ItemStack is = view.getItem(slot);
            if (is == null) {
                return;
            }
            // get the TARDIS the player is in
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
            if (!rst.resultSet()) {
                return;
            }
            int id = rst.getTardis_id();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                return;
            }
            UUID uuid = player.getUniqueId();
            Tardis tardis = rs.getTardis();
            ChameleonPreset preset = tardis.getPreset();
            Adaption adapt = tardis.getAdaption();
            switch (slot) {
                // back
                case 0 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        player.openInventory(new TARDISChameleonInventory(plugin, adapt, preset, tardis.getItemPreset()).getInventory()), 2L);
                // help
                case 2 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        player.openInventory(new TARDISChameleonHelpGUI(plugin).getInventory()), 2L);
                case 5 -> close(player); // abort
                case 7 -> {
                    // build
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", id);
                    ResultSetChameleon rscl = new ResultSetChameleon(plugin, wherecl);
                    if (!rscl.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_NO_SAVE");
                        return;
                    }
                    new ConstructBuilder(plugin).build(tardis.getPreset().toString(), id, player);
                }
                case 8 -> {
                    String air = TARDISConstants.AIR.getAsString();
                    // process
                    String[][] blue = new String[10][4];
                    String[][] stain = new String[10][4];
                    String[][] glass = new String[10][4];
                    int first = 0;
                    int second;
                    int nullcount = 0;
                    for (int s = 18; s < 27; s++) {
                        second = 0;
                        for (int c = 27; c >= 0; c -= 9) {
                            ItemStack d = view.getItem(s + c);
                            if (d != null) {
                                Material type = d.getType();
                                if ((!plugin.getConfig().getBoolean("allow.all_blocks") && TARDISMaterials.precious.contains(type)) || Tag.WOOL_CARPETS.isTagged(type)) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_NOT_CUSTOM");
                                    // return items
                                    player.getWorld().dropItemNaturally(player.getLocation(), d);
                                    event.getClickedInventory().clear(s + c);
                                    return;
                                }
                                Material mat = d.getType();
                                BlockData blockData = mat.createBlockData();
                                blue[first][second] = blockData.getAsString();
                                if (doors.contains(d.getType())) {
                                    // doors
                                    BlockData did = d.getType().createBlockData();
                                    Door door = (Door) did;
                                    // set facing / hinge
                                    door.setFacing(BlockFace.EAST);
                                    door.setHinge(Door.Hinge.RIGHT);
                                    if ((s + c) == 52) {
                                        door.setHalf(Bisected.Half.BOTTOM);
                                    } else {
                                        door.setHalf(Bisected.Half.TOP);
                                    }
                                    String dataStr = door.getAsString();
                                    blue[first][second] = dataStr;
                                    stain[first][second] = dataStr;
                                    glass[first][second] = dataStr;
                                } else if (mat.equals(Material.TORCH) || mat.equals(Material.REDSTONE_TORCH)) {
                                    // check block under torch
                                    if (view.getItem(35) == null) {
                                        blue[first][second] = air;
                                        glass[first][second] = air;
                                        stain[first][second] = air;
                                    } else {
                                        // torches
                                        blue[first][second] = blockData.getAsString();
                                        glass[first][second] = TARDISConstants.GLASS.getAsString();
                                        stain[first][second] = (mat.equals(Material.TORCH)) ? Material.YELLOW_STAINED_GLASS.createBlockData().getAsString() : Material.RED_STAINED_GLASS.createBlockData().getAsString();
                                    }
                                } else if (blockData instanceof Directional directional) {
                                    directional.setFacing(BlockFace.WEST);
                                    blue[first][second] = directional.getAsString();
                                    glass[first][second] = TARDISConstants.GLASS.getAsString();
                                    stain[first][second] = TARDISStainedGlassLookup.stainedGlassFromMaterial(null, mat).createBlockData().getAsString();
                                } else {
                                    blue[first][second] = blockData.getAsString();
                                    glass[first][second] = TARDISConstants.GLASS.getAsString();
                                    stain[first][second] = TARDISStainedGlassLookup.stainedGlassFromMaterial(null, mat).createBlockData().getAsString();
                                }
                            } else {
                                blue[first][second] = air;
                                glass[first][second] = air;
                                stain[first][second] = air;
                                nullcount++;
                            }
                            second++;
                        }
                        first++;
                    }
                    if (nullcount == 33) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_NOT_EMPTY");
                        return;
                    }
                    // add sign
                    BlockData sign = Material.OAK_WALL_SIGN.createBlockData();
                    Directional directional = (Directional) sign;
                    directional.setFacing(BlockFace.WEST);
                    String[] signData = new String[]{air, air, directional.getAsString(), air};
                    blue[9] = signData;
                    stain[9] = signData;
                    glass[9] = signData;
                    // json
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    String jsonBlue = gson.toJson(blue);
                    String jsonStain = gson.toJson(stain);
                    String jsonGlass = gson.toJson(glass);
                    // set other shells as inactive
                    HashMap<String, Object> seti = new HashMap<>();
                    seti.put("active", 0);
                    HashMap<String, Object> wherei = new HashMap<>();
                    wherei.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("chameleon", seti, wherei);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // save chameleon construct
                        HashMap<String, Object> setc = new HashMap<>();
                        setc.put("tardis_id", id);
                        setc.put("blueprintData", jsonBlue);
                        setc.put("stainData", jsonStain);
                        setc.put("glassData", jsonGlass);
                        setc.put("active", 1);
                        // always insert as you can now have multiple shell constructs
                        plugin.getQueryFactory().doSyncInsert("chameleon", setc);
                        new ConstructBuilder(plugin).build(tardis.getPreset().toString(), id, player);
                    }, 5L);
                }
                case 26 -> {
                    // set lamp
                    if (!currentLamp.containsKey(uuid)) {
                        currentLamp.put(uuid, 0);
                    }
                    nextLamp(uuid, view);
                }
                case 43, 52 -> {
                    // switch door
                    if (!currentDoor.containsKey(uuid)) {
                        currentDoor.put(uuid, 0);
                    }
                    nextDoor(uuid, view);
                }
                default -> {
                }
            }
        } else {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TARDISChameleonConstructorListener");
                event.setCancelled(true);
            }
        }
    }

    private void nextLamp(UUID uuid, InventoryView view) {
        int l = currentLamp.get(uuid);
        if (l < ln - 1) {
            l++;
        } else {
            l = 0;
        }
        view.setItem(26, ItemStack.of(lamps.get(l)));
        currentLamp.put(uuid, l);
    }

    private void nextDoor(UUID uuid, InventoryView view) {
        int d = currentDoor.get(uuid);
        if (d < dn - 1) {
            d++;
        } else {
            d = 0;
        }
        view.setItem(43, ItemStack.of(doors.get(d)));
        view.setItem(52, ItemStack.of(doors.get(d)));
        currentDoor.put(uuid, d);
    }
}
