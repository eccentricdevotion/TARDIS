/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetChameleon;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ADAPTION;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChameleonConstructorListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> doors = new ArrayList<>();
    private final List<Material> precious = new ArrayList<>();
    private final List<Material> lamps = new ArrayList<>();
    private final HashMap<UUID, Integer> currentDoor = new HashMap<>();
    private final HashMap<UUID, Integer> currentLamp = new HashMap<>();
    private final int dn;
    private final int ln;

    public TARDISChameleonConstructorListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        this.doors.add(Material.IRON_DOOR);
        this.doors.add(Material.OAK_DOOR);
        this.doors.add(Material.SPRUCE_DOOR);
        this.doors.add(Material.BIRCH_DOOR);
        this.doors.add(Material.JUNGLE_DOOR);
        this.doors.add(Material.ACACIA_DOOR);
        this.doors.add(Material.DARK_OAK_DOOR);
        this.dn = this.doors.size();
        this.precious.add(Material.BEDROCK);
        this.precious.add(Material.COAL_ORE);
        this.precious.add(Material.DIAMOND_BLOCK);
        this.precious.add(Material.DIAMOND_ORE);
        this.precious.add(Material.EMERALD_BLOCK);
        this.precious.add(Material.EMERALD_ORE);
        this.precious.add(Material.NETHER_QUARTZ_ORE);
        this.precious.add(Material.GOLD_BLOCK);
        this.precious.add(Material.GOLD_ORE);
        this.precious.add(Material.IRON_BLOCK);
        this.precious.add(Material.IRON_ORE);
        this.precious.add(Material.JACK_O_LANTERN);
        this.precious.add(Material.LAPIS_BLOCK);
        this.precious.add(Material.LAPIS_BLOCK);
        this.precious.add(Material.MELON);
        this.precious.add(Material.OBSIDIAN);
        this.precious.add(Material.PUMPKIN);
        this.precious.add(Material.REDSTONE_BLOCK);
        this.precious.add(Material.REDSTONE_ORE);
        this.precious.add(Material.SEA_LANTERN);
        plugin.getBlocksConfig().getStringList("lamp_blocks").forEach((s) -> {
            try {
                this.lamps.add(Material.valueOf(s));
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid Material in lamp_blocks section.");
            }
        });
        this.ln = this.lamps.size();
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)

    public void onChameleonConstructorClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Chameleon Construction")) {
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && (slot < 18 || slot == 26 || slot == 43 || slot == 52)) {
                event.setCancelled(true);
                ItemStack is = inv.getItem(slot);
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
                            final PRESET preset = tardis.getPreset();
                            final ADAPTION adapt = tardis.getAdaption();
                            switch (slot) {
                                case 0:
                                    // back
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonInventory tci = new TARDISChameleonInventory(plugin, adapt, preset);
                                        ItemStack[] items = tci.getMenu();
                                        Inventory chaminv = plugin.getServer().createInventory(player, 27, "§4Chameleon Circuit");
                                        chaminv.setContents(items);
                                        player.openInventory(chaminv);
                                    }, 2L);
                                    break;
                                case 2:
                                    // help
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonHelpGUI tci = new TARDISChameleonHelpGUI(plugin);
                                        ItemStack[] items = tci.getHelp();
                                        Inventory chaminv = plugin.getServer().createInventory(player, 54, "§4Chameleon Help");
                                        chaminv.setContents(items);
                                        player.openInventory(chaminv);
                                    }, 2L);
                                    break;
                                case 5:
                                    // abort
                                    // drop any user placed items in the inventory
                                    for (int s = 18; s < 54; s++) {
                                        if (s != 26 && s != 43 && s != 52) {
                                            ItemStack userStack = inv.getItem(s);
                                            if (userStack != null) {
                                                player.getWorld().dropItemNaturally(player.getLocation(), userStack);
                                            }
                                        }
                                    }
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
                                    buildConstruct(tardis.getPreset().toString(), id, new QueryFactory(plugin), tardis.getChameleon(), player);
                                    break;
                                case 8:
                                    String air = Material.AIR.createBlockData().getDataString();
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
                                            ItemStack d = inv.getItem(s + c);
                                            if (d != null) {
                                                Material type = d.getType();
                                                if ((!plugin.getConfig().getBoolean("allow.all_blocks") && precious.contains(type)) || Tag.CARPETS.isTagged(type)) {
                                                    TARDISMessage.send(player, "CHAM_NOT_CUSTOM");
                                                    return;
                                                }
                                                Material tid = d.getType();
                                                blue[first][second] = tid.createBlockData().getDataString();
                                                if (doors.contains(d.getType())) {
                                                    // doors
                                                    BlockData did = d.getType().createBlockData();
                                                    Bisected bisected = (Bisected) did;
                                                    if ((s + c) == 52) {
                                                        bisected.setHalf(Bisected.Half.BOTTOM);
                                                    } else {
                                                        bisected.setHalf(Bisected.Half.TOP);
                                                    }
                                                    String dataStr = bisected.getDataString();
                                                    blue[first][second] = dataStr;
                                                    stain[first][second] = dataStr;
                                                    glass[first][second] = dataStr;
                                                } else if (tid.equals(Material.TORCH) || tid.equals(Material.REDSTONE_TORCH)) {
                                                    // check block under torch
                                                    if (inv.getItem(35) == null) {
                                                        blue[first][second] = air;
                                                        glass[first][second] = air;
                                                        stain[first][second] = air;
                                                    } else {
                                                        // torches
                                                        blue[first][second] = tid.createBlockData().getDataString();
//                                                        blueData[first][second] = (byte) 5;
                                                        glass[first][second] = Material.GLASS.createBlockData().getDataString();
                                                        stain[first][second] = (tid.equals(Material.TORCH)) ? Material.YELLOW_STAINED_GLASS.createBlockData().getDataString() : Material.RED_STAINED_GLASS.createBlockData().getDataString();
                                                    }
                                                } else {
                                                    blue[first][second] = tid.createBlockData().getDataString();
                                                    glass[first][second] = Material.GLASS.createBlockData().getDataString();
                                                    stain[first][second] = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(tid).createBlockData().getDataString();
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
                                    BlockData sign = Material.WALL_SIGN.createBlockData();
                                    Directional directional = (Directional) sign;
                                    directional.setFacing(BlockFace.WEST);
                                    String[] signData = new String[]{air, air, directional.getDataString(), air};
                                    blue[9] = signData;
                                    stain[9] = signData;
                                    glass[9] = signData;
                                    // json
                                    String jsonBlue = new JSONArray(blue).toString();
                                    String jsonStain = new JSONArray(stain).toString();
                                    String jsonGlass = new JSONArray(glass).toString();
                                    // save chameleon construct
                                    HashMap<String, Object> wherec = new HashMap<>();
                                    wherec.put("tardis_id", id);
                                    ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
                                    QueryFactory qf = new QueryFactory(plugin);
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("blueprintID", jsonBlue);
                                    set.put("stainID", jsonStain);
                                    set.put("glassID", jsonGlass);
                                    if (rsc.resultSet()) {
                                        // update
                                        HashMap<String, Object> whereu = new HashMap<>();
                                        whereu.put("tardis_id", id);
                                        qf.doUpdate("chameleon", set, whereu);
                                    } else {
                                        // insert
                                        set.put("tardis_id", id);
                                        qf.doInsert("chameleon", set);
                                    }
                                    buildConstruct(tardis.getPreset().toString(), id, qf, tardis.getChameleon(), player);
                                    break;
                                case 26:
                                    // set lamp
                                    if (!currentLamp.containsKey(uuid)) {
                                        currentLamp.put(uuid, 0);
                                    }
                                    nextLamp(uuid, inv);
                                    break;
                                case 43:
                                case 52:
                                    // switch door
                                    if (!currentDoor.containsKey(uuid)) {
                                        currentDoor.put(uuid, 0);
                                    }
                                    nextDoor(uuid, inv);
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

    private void nextLamp(UUID uuid, Inventory inv) {
        int l = currentLamp.get(uuid);
        if (l < ln - 1) {
            l++;
        } else {
            l = 0;
        }
        inv.setItem(26, new ItemStack(lamps.get(l)));
        currentLamp.put(uuid, l);
    }

    private void nextDoor(UUID uuid, Inventory inv) {
        int d = currentDoor.get(uuid);
        if (d < dn - 1) {
            d++;
        } else {
            d = 0;
        }
        inv.setItem(43, new ItemStack(doors.get(d)));
        inv.setItem(52, new ItemStack(doors.get(d)));
        currentDoor.put(uuid, d);
    }

    private void buildConstruct(String preset, int id, QueryFactory qf, String location, Player player) {
        // update tardis table
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("chameleon_preset", "CONSTRUCT");
        sett.put("chameleon_demat", preset);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        qf.doUpdate("tardis", sett, wheret);
        // update chameleon sign
        TARDISStaticUtils.setSign(location, 3, "CONSTRUCT", player);
        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Construct");
        // rebuild
        player.performCommand("tardis rebuild");
        close(player);
        // damage the circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            // decrement uses
            int uses_left = tcc.getChameleonUses();
            new TARDISCircuitDamager(plugin, DISK_CIRCUIT.CHAMELEON, uses_left, id, player).damage();
        }
    }

    //TODO construct a list of valid chameleon Materials to default BlockData strings
}
