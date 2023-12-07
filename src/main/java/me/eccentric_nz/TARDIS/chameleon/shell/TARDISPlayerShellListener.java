/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon.shell;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISShellBuilder;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.chameleon.construct.ConstructBuilder;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISPlayerShellListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> selected = new HashMap<>();

    public TARDISPlayerShellListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onShellLoaderClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Shells")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot > 53) {
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        // get the TARDIS the player is in
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            return;
        }
        TARDISChameleonColumn chameleonColumn = null;
        ChameleonPreset preset;
        // get the Shell room button
        Location button = getButton(id);
        if (button == null) {
            return;
        }
        switch (slot) {
            case 53 -> close(player); // close
            case 50 -> {
                // save
                new TARDISShellRoomConstructor(plugin).createShell(player, id, button.getBlock(), -1);
                // close
                close(player);
            }
            case 49 -> {
                // new
                clear(button, false, id);
                // close
                close(player);
            }
            case 47 -> {
                // update selected shell
                if (selected.containsKey(uuid)) {
                    int cid = getChameleonId(view, selected.get(uuid));
                    // scan
                    new TARDISShellRoomConstructor(plugin).createShell(player, id, button.getBlock(), cid);
                    // close
                    close(player);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SHELL_SELECT");
                }
            }
            case 46 -> {
                // delete selected shell
                if (selected.containsKey(uuid)) {
                    // if active set chameleon circuit to FACTORY
                    if (isActive(view, selected.get(uuid))) {
                        // set preset
                        HashMap<String, Object> setf = new HashMap<>();
                        setf.put("chameleon_preset", "FACTORY");
                        setf.put("adapti_on", 0);
                        HashMap<String, Object> wheref = new HashMap<>();
                        wheref.put("tardis_id", id);
                        plugin.getQueryFactory().doSyncUpdate("tardis", setf, wheref);
                        // rebuild
                        new TARDISRebuildCommand(plugin).rebuildPreset(player);
                    }
                    int cid = getChameleonId(view, selected.get(uuid));
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("chameleon_id", cid);
                    plugin.getQueryFactory().doDelete("chameleon", whered);
                    // close
                    close(player);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SHELL_SELECT");
                }
            }
            case 45 -> {
                // set as the active shell
                if (selected.containsKey(uuid)) {
                    // set other shells as inactive
                    HashMap<String, Object> seti = new HashMap<>();
                    seti.put("active", 0);
                    HashMap<String, Object> wherei = new HashMap<>();
                    wherei.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("chameleon", seti, wherei);
                    // set selected as active
                    int cid = getChameleonId(view, selected.get(uuid));
                    HashMap<String, Object> wheresc = new HashMap<>();
                    wheresc.put("chameleon_id", cid);
                    HashMap<String, Object> seta = new HashMap<>();
                    seta.put("active", 1);
                    plugin.getQueryFactory().doUpdate("chameleon", seta, wheresc);
                    // set chameleon circuit to selected shell
                    new ConstructBuilder(plugin).build(rs.getTardis().getPreset().toString(), id, player);
                    // close
                    close(player);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SHELL_SELECT");
                }
            }
            default -> {
                selected.put(uuid, slot);
                // get the chameleon_id
                int cid = getChameleonId(view, slot);
                if (cid == -1) {
                    return;
                }
                // load selected shell
                preset = ChameleonPreset.CONSTRUCT;
                // load saved construct
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("chameleon_id", cid);
                ResultSetShells rsc = new ResultSetShells(plugin, wherec);
                if (rsc.resultSet()) {
                    // convert to String[][] array
                    String data = rsc.getData().get(0).get("blueprintData");
                    if (data != null) {
                        JsonArray json = JsonParser.parseString(data).getAsJsonArray();
                        String[][] strings = new String[10][4];
                        for (int i = 0; i < 10; i++) {
                            JsonArray inner = json.get(i).getAsJsonArray();
                            for (int j = 0; j < 4; j++) {
                                String block = inner.get(j).getAsString();
                                strings[i][j] = block;
                            }
                        }
                        chameleonColumn = TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, strings);
                    }
                }
                if (chameleonColumn == null) {
                    return;
                }
                clear(button, true, id);
                // get the start location
                Location centre = button.clone().add(3, 1, 0);
                // build shell in the shell room
                new TARDISShellBuilder(plugin, preset, chameleonColumn, centre, cid).buildPreset();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onShellBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            // prevent TARDIS block breakage
            Block b = event.getBlock();
            String l = b.getLocation().toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l);
            where.put("police_box", 3);
            ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, false);
            if (rsb.resultSet()) {
                plugin.getMessenger().sendStatus(player, "SHELL_CLEAR");
                event.setCancelled(true);
            }
        }
    }

    private int getChameleonId(InventoryView view, int slot) {
        // get the chameleon_id
        ItemStack shell = view.getItem(slot);
        ItemMeta im = shell.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        if (!pdc.has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
            return -1;
        }
        return pdc.get(plugin.getCustomBlockKey(), PersistentDataType.INTEGER);
    }

    private boolean isActive(InventoryView view, int slot) {
        // get if active
        ItemStack shell = view.getItem(slot);
        ItemMeta im = shell.getItemMeta();
        return im.hasLore() && im.getLore().size() > 4;
    }

    private Location getButton(int id) {
        HashMap<String, Object> whereb = new HashMap<>();
        whereb.put("tardis_id", id);
        whereb.put("type", 25);
        ResultSetControls resultSetControls = new ResultSetControls(plugin, whereb, false);
        if (!resultSetControls.resultSet()) {
            return null;
        }
        return TARDISStaticLocationGetters.getLocationFromBukkitString(resultSetControls.getLocation());
    }

    private void clear(Location button, boolean protect, int id) {
        World w = button.getWorld();
        int fx = button.getBlockX() + 2;
        int fy = button.getBlockY() + 1;
        int fz = button.getBlockZ() - 1;
        // always clear the platform first
        // do problem blocks first
        for (int c = 0; c < 10; c++) {
            for (int y = fy; y < fy + 4; y++) {
                Block block = w.getBlockAt(fx + TARDISShellRoomConstructor.orderx[c], y, fz + TARDISShellRoomConstructor.orderz[c]);
                if (ShellLoaderProblemBlocks.DO_FIRST.contains(block.getType())) {
                    block.setBlockData(TARDISConstants.AIR);
                }
            }
        }
        // set to AIR
        for (int c = 0; c < 10; c++) {
            for (int y = fy; y < fy + 4; y++) {
                Block b = w.getBlockAt(fx + TARDISShellRoomConstructor.orderx[c], y, fz + TARDISShellRoomConstructor.orderz[c]);
                b.setBlockData(TARDISConstants.AIR);
                String loc = b.getLocation().toString();
                if (protect) {
                    // set block protection
                    HashMap<String, Object> setpb = new HashMap<>();
                    setpb.put("tardis_id", id);
                    setpb.put("location", loc);
                    setpb.put("data", "minecraft:air");
                    setpb.put("police_box", 3);
                    plugin.getQueryFactory().doInsert("blocks", setpb);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(loc, id);
                } else {
                    // remove protection
                    HashMap<String, Object> wherep = new HashMap<>();
                    wherep.put("tardis_id", id);
                    wherep.put("police_box", 3);
                    plugin.getQueryFactory().doDelete("blocks", wherep);
                    plugin.getGeneralKeeper().getProtectBlockMap().remove(loc);
                }
            }
        }
    }
}

