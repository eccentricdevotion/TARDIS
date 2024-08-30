/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCount;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISThemeMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISThemeMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Upgrade Menu")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ClickType click = event.getClick();
        if (slot < 0 || slot > 53) {
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        ItemStack choice = view.getItem(slot);
        event.setCancelled(true);
        switch (slot) {
            case 45 -> {}
            case 46 -> {
                // archive
                if (choice != null) {
                    archive(player);
                }
            }
            case 47 -> {
                // repair
                if (choice != null && plugin.getConfig().getBoolean("allow.repair")) {
                    repair(player);
                }
            }
            case 48 -> {
                // clean
                if (choice != null && plugin.getConfig().getBoolean("allow.repair")) {
                    clean(player);
                }
            }
            case 51 -> {
                // get player upgrade data
                TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(player.getUniqueId());
                ItemStack[] consoles;
                // switch page
                if (choice.getItemMeta().getCustomModelData() == GUIChameleonPresets.GO_TO_PAGE_2.customModelData()) {
                    // page 2
                    consoles = new TARDISCustomThemeInventory(plugin, player, tud.getPrevious().getPermission(), tud.getLevel()).getMenu();
                } else {
                    // page 1
                    consoles = new TARDISPluginThemeInventory(plugin, player, tud.getPrevious().getPermission(), tud.getLevel()).getMenu();
                }
                Inventory upg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Upgrade Menu");
                upg.setContents(consoles);
                player.openInventory(upg);
            }
            case 53 -> close(player); // close
            default -> {
                if (choice == null) {
                    return;
                }
                // get material of selected console
                String perm = Consoles.schematicFor(choice.getType()).getPermission();
                if (!TARDISPermission.hasPermission(player, "tardis." + perm)) {
                    return;
                }
                // remember the upgrade choice
                Schematic schematic = Consoles.schematicFor(perm);
                UUID uuid = player.getUniqueId();
                if (click.equals(ClickType.SHIFT_LEFT) && plugin.getConfig().getBoolean("creation.previews")) {
                    // get the transmat location
                    ResultSetTransmat rst = new ResultSetTransmat(plugin, schematic.getPreview(), schematic.getPermission());
                    if (rst.resultSet()) {
                        Location transmat = rst.getLocation();
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_PREVIEW", schematic.getPermission().toUpperCase(Locale.ROOT));
                        transmat.setYaw(rst.getYaw());
                        transmat.setPitch(player.getLocation().getPitch());
                        // start tracking player
                        plugin.getTrackerKeeper().getPreviewers().put(uuid, new PreviewData(player.getLocation().clone(), player.getGameMode()));
                        // transmat to preview desktop
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            // set gamemode
                            player.setGameMode(GameMode.ADVENTURE);
                            player.playSound(transmat, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            player.teleport(transmat);
                        }, 10L);
                        close(player);
                    }
                } else {
                    TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                    int upgrade = plugin.getArtronConfig().getInt("upgrades." + perm);
                    int needed = (tud.getPrevious().getPermission().equals(schematic.getPermission())) ? upgrade / 2 : upgrade;
                    if (tud.getLevel() >= needed) {
                        tud.setSchematic(schematic);
                        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                        // open the wall block GUI
                        wall(player);
                        if (tud.getPrevious().getPermission().equals("archive")) {
                            new ArchiveUpdate(plugin, uuid.toString(), "ª°º").setInUse();
                        }
                    }
                }
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param player the player using the GUI
     */
    @Override
    public void close(Player player) {
        plugin.getTrackerKeeper().getUpgrades().remove(player.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
    }

    /**
     * Closes the inventory and opens the Wall block selector menu.
     *
     * @param p the player using the GUI
     */
    private void wall(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            ItemStack[] wall_blocks = new TARDISWallsInventory(plugin).getMenu();
            Inventory wall = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "TARDIS Wall Menu");
            wall.setContents(wall_blocks);
            p.openInventory(wall);
        }, 1L);
    }

    /**
     * Closes the inventory and opens the archive consoles menu.
     *
     * @param p the player using the GUI
     */
    private void archive(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            ItemStack[] archive = new TARDISArchiveInventory(plugin, p).getArchives();
            Inventory menu = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "TARDIS Archive");
            menu.setContents(archive);
            p.openInventory(menu);
        }, 1L);
    }

    /**
     * Initiates a TARDIS repair. Resets the console back to the original console schematic, Players must condense all
     * missing blocks - unless the /tardisadmin repair [player] [amount] command has been run, assigning the player a
     * 'free' repair(s).
     */
    private void repair(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            String uuid = p.getUniqueId().toString();
            boolean repair;
            TARDISRepair tr = new TARDISRepair(plugin, p);
            // is it a free repair?
            ResultSetCount rsc = new ResultSetCount(plugin, uuid);
            if (rsc.resultSet() && rsc.getRepair() > 0) {
                // decrement repair
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                HashMap<String, Object> set = new HashMap<>();
                set.put("repair", rsc.getRepair() - 1);
                plugin.getQueryFactory().doUpdate("t_count", set, where);
                repair = true;
            } else {
                // scan console and check condensed blocks
                repair = tr.hasCondensedMissingBlocks();
            }
            if (repair) {
                tr.restore(false);
            }
        }, 1L);
    }

    /**
     * Initiates a TARDIS clean. Removes any blocks that are not part of the original console schematic (missing blocks
     * will not be restored).
     */
    private void clean(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            new TARDISRepair(plugin, p).restore(true);
        }, 1L);
    }
}
