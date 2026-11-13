/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.console.CustomiseConsoleInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCount;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.schematic.archive.ArchiveUpdate;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
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
public class DesktopThemeMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public DesktopThemeMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof PluginThemeInventory)
                && !(event.getInventory().getHolder(false) instanceof CustomThemeInventory)
                && !(event.getInventory().getHolder(false) instanceof FadeThemeInventory)
        ) {
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
        ItemStack choice = event.getView().getItem(slot);
        event.setCancelled(true);
        switch (slot) {
            case 45 -> {
            }
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
            case 49 -> {
                // customise console
                InventoryHolder customise = new CustomiseConsoleInventory(plugin);
                player.openInventory(customise.getInventory());
            }
            case 50 -> {
                // get player upgrade data
                UpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(player.getUniqueId());
                // switch page
                player.openInventory(new PluginThemeInventory(plugin, player, tud.getPrevious().getPermission(), tud.getLevel()).getInventory());
            }
            case 51 -> {
                // get player upgrade data
                UpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(player.getUniqueId());
                // switch page
                player.openInventory(new CustomThemeInventory(plugin, player, tud.getPrevious().getPermission(), tud.getLevel()).getInventory());
            }
            case 52 -> {
                // get player upgrade data
                UpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(player.getUniqueId());
                // switch page
                player.openInventory(new FadeThemeInventory(plugin, player, tud.getPrevious().getPermission(), tud.getLevel()).getInventory());
            }
            case 53 -> close(player); // close
            default -> {
                if (choice == null) {
                    return;
                }
                Schematic schematic;
                if (choice.getType() == Material.LIGHT_GRAY_BUNDLE) {
                    schematic = Desktops.schematicFor(Material.ORANGE_POPLAR_LEAVES);
                } else {
                    // get material of selected console
                    // remember the upgrade choice
                    schematic = Desktops.schematicFor(choice.getType());
                }
                UUID uuid = player.getUniqueId();
                if (click.equals(ClickType.SHIFT_LEFT) && plugin.getConfig().getBoolean("desktop.previews")) {
                    // get the transmat location
                    ResultSetTransmat rst = new ResultSetTransmat(plugin, schematic.getPreview(), schematic.getPermission());
                    if (rst.resultSet()) {
                        Location transmat = rst.getLocation();
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_PREVIEW", schematic.getPermission().toUpperCase(Locale.ROOT));
                        transmat.setYaw(rst.getYaw());
                        transmat.setPitch(player.getLocation().getPitch());
                        // get tardis id
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", uuid.toString());
                        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
                        rs.resultSet();
                        // start tracking player
                        plugin.getTrackerKeeper().getPreviewers().put(uuid, new PreviewData(player.getLocation().clone(), player.getGameMode(), rs.getTardis_id()));
                        // transmat to preview desktop
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            // set gamemode
                            player.setGameMode(GameMode.ADVENTURE);
                            player.playSound(transmat, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            player.teleport(transmat);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREVIEW_DONE");
                        }, 10L);
                        close(player);
                    }
                } else {
                    if (!TARDISPermission.hasPermission(player, "tardis." + schematic.getPermission())) {
                        return;
                    }
                    UpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                    int upgrade = plugin.getArtronConfig().getInt("upgrades." + schematic.getPermission());
                    int needed = (tud.getPrevious().getPermission().equals(schematic.getPermission())) ? upgrade / 2 : upgrade;
                    if (tud.getLevel() >= needed) {
                        tud.setSchematic(schematic);
                        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                        if (schematic.getPermission().equals("fade")) {
                            // get the selected variant
                            StructureVariant variant = variantFromSlot(choice);
                            tud.setVariant(variant);
                            plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                            // just build the fade desktop
                            new DesktopThemeProcessor(plugin, uuid).changeDesktop();
                            // close the GUI
                            close(player);
                        } else {
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
    }

    private StructureVariant variantFromSlot(ItemStack choice) {
        String v = ComponentUtils.stripColour(choice.getData(DataComponentTypes.CUSTOM_NAME));
        return plugin.getBuildKeeper().getFadeVariants().getLookup().get(TARDISStringUtils.toEnumUppercase(v));
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
     * @param player the player using the GUI
     */
    private void wall(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.closeInventory();
            player.openInventory(new WallsInventory(plugin, "TARDIS Wall Menu").getInventory());
        }, 1L);
    }

    /**
     * Closes the inventory and opens the archive consoles menu.
     *
     * @param player the player using the GUI
     */
    private void archive(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.closeInventory();
            player.openInventory(new ArchiveInventory(plugin, player).getInventory());
        }, 1L);
    }

    /**
     * Initiates a TARDIS repair. Resets the console back to the original console schematic, Players must condense all
     * missing blocks - unless the /tardisadmin repair [player] [amount] command has been run, assigning the player a
     * 'free' repair(s).
     */
    private void repair(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.closeInventory();
            String uuid = player.getUniqueId().toString();
            boolean repair;
            DesktopRepair tr = new DesktopRepair(plugin, player);
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
    private void clean(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.closeInventory();
            new DesktopRepair(plugin, player).restore(true);
        }, 1L);
    }
}
