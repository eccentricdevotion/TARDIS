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
package me.eccentric_nz.TARDIS.chameleon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChameleon;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISChameleonConstructorListener extends TARDISMenuListener implements Listener {

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
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Chameleon Construction")) {
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && (slot < 18 || slot == 26 || slot == 43 || slot == 52)) {
                event.setCancelled(true);
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            UUID uuid = player.getUniqueId();
                            PRESET preset = tardis.getPreset();
                            Adaption adapt = tardis.getAdaption();
                            switch (slot) {
                                case 0:
                                    // back
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonInventory tci = new TARDISChameleonInventory(plugin, adapt, preset);
                                        ItemStack[] items = tci.getMenu();
                                        Inventory chaminv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                                        chaminv.setContents(items);
                                        player.openInventory(chaminv);
                                    }, 2L);
                                    break;
                                case 2:
                                    // help
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonHelpGUI tci = new TARDISChameleonHelpGUI(plugin);
                                        ItemStack[] items = tci.getHelp();
                                        Inventory chaminv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Chameleon Help");
                                        chaminv.setContents(items);
                                        player.openInventory(chaminv);
                                    }, 2L);
                                    break;
                                case 5:
                                    // abort
                                    close(player);
                                    break;
                                case 7:
                                    HashMap<String, Object> wherecl = new HashMap<>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetChameleon rscl = new ResultSetChameleon(plugin, wherecl);
                                    if (!rscl.resultSet()) {
                                        TARDISMessage.send(player, "CHAM_NO_SAVE");
                                        return;
                                    }
                                    buildConstruct(tardis.getPreset().toString(), id, player);
                                    break;
                                case 8:
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
                                                if ((!plugin.getConfig().getBoolean("allow.all_blocks") && TARDISMaterials.precious.contains(type)) || Tag.CARPETS.isTagged(type)) {
                                                    TARDISMessage.send(player, "CHAM_NOT_CUSTOM");
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
                                                } else if (blockData instanceof Directional) {
                                                    Directional directional = (Directional) blockData;
                                                    directional.setFacing(BlockFace.WEST);
                                                    blue[first][second] = directional.getAsString();
                                                    glass[first][second] = TARDISConstants.GLASS.getAsString();
                                                    stain[first][second] = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(mat).createBlockData().getAsString();
                                                } else {
                                                    blue[first][second] = blockData.getAsString();
                                                    glass[first][second] = TARDISConstants.GLASS.getAsString();
                                                    stain[first][second] = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(mat).createBlockData().getAsString();
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
                                        TARDISMessage.send(player, "CHAM_NOT_EMPTY");
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
                                    // save chameleon construct
                                    HashMap<String, Object> wherec = new HashMap<>();
                                    wherec.put("tardis_id", id);
                                    ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("blueprintData", jsonBlue);
                                    set.put("stainData", jsonStain);
                                    set.put("glassData", jsonGlass);
                                    if (rsc.resultSet()) {
                                        // update
                                        HashMap<String, Object> whereu = new HashMap<>();
                                        whereu.put("tardis_id", id);
                                        plugin.getQueryFactory().doUpdate("chameleon", set, whereu);
                                    } else {
                                        // insert
                                        set.put("tardis_id", id);
                                        plugin.getQueryFactory().doInsert("chameleon", set);
                                    }
                                    buildConstruct(tardis.getPreset().toString(), id, player);
                                    break;
                                case 26:
                                    // set lamp
                                    if (!currentLamp.containsKey(uuid)) {
                                        currentLamp.put(uuid, 0);
                                    }
                                    nextLamp(uuid, view);
                                    break;
                                case 43:
                                case 52:
                                    // switch door
                                    if (!currentDoor.containsKey(uuid)) {
                                        currentDoor.put(uuid, 0);
                                    }
                                    nextDoor(uuid, view);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
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
        view.setItem(26, new ItemStack(lamps.get(l)));
        currentLamp.put(uuid, l);
    }

    private void nextDoor(UUID uuid, InventoryView view) {
        int d = currentDoor.get(uuid);
        if (d < dn - 1) {
            d++;
        } else {
            d = 0;
        }
        view.setItem(43, new ItemStack(doors.get(d)));
        view.setItem(52, new ItemStack(doors.get(d)));
        currentDoor.put(uuid, d);
    }

    private void buildConstruct(String preset, int id, Player player) {
        // update tardis table
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("chameleon_preset", "CONSTRUCT");
        sett.put("chameleon_demat", preset);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", sett, wheret);
        // update the Chameleon Circuit sign(s)
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        wherec.put("type", 31);
        ResultSetControls rsc = new ResultSetControls(plugin, wherec, true);
        if (rsc.resultSet()) {
            for (HashMap<String, String> map : rsc.getData()) {
                TARDISStaticUtils.setSign(map.get("location"), 3, "CONSTRUCT", player);
            }
        }
        new TARDISChameleonFrame(plugin).updateChameleonFrame(id, PRESET.CONSTRUCT);
        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Construct");
        // rebuild
        player.performCommand("tardis rebuild");
        plugin.getTrackerKeeper().getConstructors().remove(player.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> close(player), 2L);
        // damage the circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            // decrement uses
            int uses_left = tcc.getChameleonUses();
            new TARDISCircuitDamager(plugin, DiskCircuit.CHAMELEON, uses_left, id, player).damage();
        }
    }
}
