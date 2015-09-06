/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.ARS.TARDISARSListener;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMapListener;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitRepairListener;
import me.eccentric_nz.TARDIS.advanced.TARDISConsoleCloseListener;
import me.eccentric_nz.TARDIS.advanced.TARDISConsoleListener;
import me.eccentric_nz.TARDIS.advanced.TARDISConsoleSwitchListener;
import me.eccentric_nz.TARDIS.advanced.TARDISDiskCraftListener;
import me.eccentric_nz.TARDIS.advanced.TARDISStorageListener;
import me.eccentric_nz.TARDIS.arch.TARDISFakeChatListener;
import me.eccentric_nz.TARDIS.arch.TARDISFobWatchListener;
import me.eccentric_nz.TARDIS.arch.TARDISGMIHelper;
import me.eccentric_nz.TARDIS.arch.TARDISMVIHelper;
import me.eccentric_nz.TARDIS.arch.TARDISRespawnListener;
import me.eccentric_nz.TARDIS.arch.TARDISSelectWatchListener;
import me.eccentric_nz.TARDIS.artron.TARDISArtronCapacitorListener;
import me.eccentric_nz.TARDIS.artron.TARDISArtronFurnaceListener;
import me.eccentric_nz.TARDIS.artron.TARDISCondenserListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonConstructorListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonHelpListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonTemplateListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISPageThreeListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISPresetListener;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminMenuListener;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISKeyMenuListener;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuListener;
import me.eccentric_nz.TARDIS.control.TARDISControlMenuListener;
import me.eccentric_nz.TARDIS.desktop.TARDISThemeMenuListener;
import me.eccentric_nz.TARDIS.desktop.TARDISWallMenuListener;
import me.eccentric_nz.TARDIS.flight.TARDISHandbrakeListener;
import me.eccentric_nz.TARDIS.flight.TARDISManualFlightListener;
import me.eccentric_nz.TARDIS.flight.TARDISRegulatorListener;
import me.eccentric_nz.TARDIS.howto.TARDISChameleonWallMenuListener;
import me.eccentric_nz.TARDIS.howto.TARDISRecipeMenuListener;
import me.eccentric_nz.TARDIS.howto.TARDISSeedMenuListener;
import me.eccentric_nz.TARDIS.howto.TARDISWallFloorMenuListener;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import me.eccentric_nz.TARDIS.junk.TARDISJunkControlListener;
import me.eccentric_nz.TARDIS.lazarus.TARDISLazarusGUIListener;
import me.eccentric_nz.TARDIS.lazarus.TARDISLazarusListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAntiBuildListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAnvilListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBeaconColouringListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBindListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockBreakListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockDamageListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPhysicsListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPlaceListener;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISChatListener;
import me.eccentric_nz.TARDIS.listeners.TARDISChunkListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCraftListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCreeperDeathListener;
import me.eccentric_nz.TARDIS.listeners.TARDISEjectListener;
import me.eccentric_nz.TARDIS.listeners.TARDISEntityGriefListener;
import me.eccentric_nz.TARDIS.listeners.TARDISExplosionListener;
import me.eccentric_nz.TARDIS.listeners.TARDISFireListener;
import me.eccentric_nz.TARDIS.listeners.TARDISGlassesListener;
import me.eccentric_nz.TARDIS.listeners.TARDISGravityWellListener;
import me.eccentric_nz.TARDIS.listeners.TARDISHorseListener;
import me.eccentric_nz.TARDIS.listeners.TARDISHotbarListener;
import me.eccentric_nz.TARDIS.listeners.TARDISIceMeltListener;
import me.eccentric_nz.TARDIS.listeners.TARDISItemFrameListener;
import me.eccentric_nz.TARDIS.listeners.TARDISJoinListener;
import me.eccentric_nz.TARDIS.listeners.TARDISKeyboardListener;
import me.eccentric_nz.TARDIS.listeners.TARDISKeyboardPacketListener;
import me.eccentric_nz.TARDIS.listeners.TARDISLightningListener;
import me.eccentric_nz.TARDIS.listeners.TARDISMakePresetListener;
import me.eccentric_nz.TARDIS.listeners.TARDISMinecartListener;
import me.eccentric_nz.TARDIS.listeners.TARDISNPCListener;
import me.eccentric_nz.TARDIS.listeners.TARDISPerceptionFilterListener;
import me.eccentric_nz.TARDIS.listeners.TARDISPistonHarvesterListener;
import me.eccentric_nz.TARDIS.listeners.TARDISPistonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISQuitListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRecipeListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRedstoneListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRemoteKeyListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRenderRoomListener;
import me.eccentric_nz.TARDIS.listeners.TARDISSaveSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import me.eccentric_nz.TARDIS.listeners.TARDISSeedBlockListener;
import me.eccentric_nz.TARDIS.listeners.TARDISSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISSpawnListener;
import me.eccentric_nz.TARDIS.listeners.TARDISStattenheimListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTagListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTeleportListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTemporalListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTemporalLocatorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTimeLordDeathListener;
import me.eccentric_nz.TARDIS.listeners.TARDISUpdateListener;
import me.eccentric_nz.TARDIS.listeners.TARDISVaultListener;
import me.eccentric_nz.TARDIS.listeners.TARDISWorldResetListener;
import me.eccentric_nz.TARDIS.listeners.TARDISZeroRoomChatListener;
import me.eccentric_nz.TARDIS.listeners.TARDISZeroRoomPacketListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorClickListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorWalkListener;
import me.eccentric_nz.TARDIS.move.TARDISMoveListener;
import me.eccentric_nz.TARDIS.rooms.TARDISJettisonSeeder;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomSeeder;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicListener;
import me.eccentric_nz.TARDIS.siegemode.TARDISBreedingListener;
import me.eccentric_nz.TARDIS.siegemode.TARDISGrowthListener;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeListener;
import me.eccentric_nz.TARDIS.sonic.TARDISFarmBlockListener;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicEntityListener;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicListener;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicMenuListener;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicSorterListener;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicUpgradeListener;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalListener;

/**
 * Registers all the listeners for the various events required to use the
 * TARDIS.
 *
 * @author eccentric_nz
 */
public class TARDISListenerRegisterer {

    private final TARDIS plugin;

    public TARDISListenerRegisterer(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers all the listeners for the various events required to use the
     * TARDIS.
     */
    public void registerListeners() {
        if (plugin.getConfig().getBoolean("creation.use_block_stack")) {
            plugin.getPM().registerEvents(new TARDISBlockPlaceListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISBlockBreakListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            plugin.getPM().registerEvents(new TARDISDoorWalkListener(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISMoveListener(plugin), plugin);
        } else {
            plugin.getPM().registerEvents(new TARDISDoorClickListener(plugin), plugin);
        }
        TARDISSonicListener sonicListener = new TARDISSonicListener(plugin);
        plugin.getPM().registerEvents(sonicListener, plugin);
        plugin.getGeneralKeeper().setSonicListener(sonicListener);
        TARDISRenderRoomListener rendererListener = new TARDISRenderRoomListener(plugin);
        plugin.getPM().registerEvents(rendererListener, plugin);
        plugin.getGeneralKeeper().setRendererListener(rendererListener);
        TARDISButtonListener buttonListener = new TARDISButtonListener(plugin);
        plugin.getPM().registerEvents(buttonListener, plugin);
        plugin.getGeneralKeeper().setButtonListener(buttonListener);
        TARDISScannerListener scannerListener = new TARDISScannerListener(plugin);
        plugin.getPM().registerEvents(scannerListener, plugin);
        plugin.getGeneralKeeper().setScannerListener(scannerListener);
        plugin.getPM().registerEvents(new TARDISARSListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISARSMapListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAdminMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAnvilListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAreaListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAreaSignListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISArtronCapacitorListener(plugin), plugin);
        if (plugin.isHelperOnServer()) {
            plugin.getPM().registerEvents(new TARDISArtronFurnaceListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISBeaconColouringListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBindListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBlockDamageListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBlockPhysicsListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonConstructorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonHelpListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonTemplateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChatListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChunkListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getPM().registerEvents(new TARDISCircuitRepairListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISCondenserListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISConsoleCloseListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISConsoleListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISConsoleSwitchListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISControlMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISCreeperDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISDiskCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISEjectListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISEntityGriefListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISExplosionListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISFakeChatListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISFarmBlockListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISFireListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISGlassesListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISGravityWellListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHandbrakeListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("preferences.nerf_pistons.enabled")) {
            plugin.getPM().registerEvents(new TARDISPistonHarvesterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISHorseListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHotbarListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISIceMeltListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISInformationSystemListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISJettisonSeeder(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISJoinListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISJunkControlListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISKeyboardListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISKeyMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISLightningListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISMakePresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISManualFlightListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISMinecartListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPageThreeListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
            plugin.getPM().registerEvents(new TARDISPerceptionFilterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISPistonListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPrefsMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISQuitListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRecipeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRegulatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRemoteKeyListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRedstoneListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRoomSeeder(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSaveSignListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSchematicListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSeedBlockListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            plugin.getPM().registerEvents(new TARDISSiegeListener(plugin), plugin);
            if (plugin.getConfig().getInt("siege.breeding") > 0) {
                plugin.getPM().registerEvents(new TARDISBreedingListener(plugin), plugin);
            }
            if (plugin.getConfig().getInt("siege.growth") > 0) {
                plugin.getPM().registerEvents(new TARDISGrowthListener(plugin), plugin);
            }
        }
        plugin.getPM().registerEvents(new TARDISSignListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicEntityListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicSorterListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicUpgradeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSpawnListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISStattenheimListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISStorageListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTagListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTeleportListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTemporalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTemporalLocatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISThemeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTerminalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTimeLordDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISUpdateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISVaultListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISWallMenuListener(plugin), plugin);
        // howto
        plugin.getPM().registerEvents(new TARDISWallFloorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonWallMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRecipeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSeedMenuListener(plugin), plugin);
        if (getNPCManager()) {
            plugin.getPM().registerEvents(new TARDISNPCListener(plugin), plugin);
        }
        if (plugin.getPM().isPluginEnabled("Multiverse-Adventure")) {
            plugin.getPM().registerEvents(new TARDISWorldResetListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            plugin.getPM().registerEvents(new TARDISZeroRoomChatListener(plugin), plugin);
            if (plugin.getPM().isPluginEnabled("ProtocolLib")) {
                new TARDISZeroRoomPacketListener(plugin);
            }
        }
        if (plugin.getPM().isPluginEnabled("ProtocolLib")) {
            TARDISKeyboardPacketListener kpl = new TARDISKeyboardPacketListener(plugin);
            plugin.getPM().registerEvents(kpl, plugin);
            kpl.startSignPackets();
            if (plugin.isDisguisesOnServer()) {
                if (plugin.getConfig().getBoolean("arch.enabled")) {
                    plugin.getPM().registerEvents(new TARDISFobWatchListener(plugin), plugin);
                    plugin.getPM().registerEvents(new TARDISSelectWatchListener(plugin), plugin);
                    plugin.getPM().registerEvents(new TARDISRespawnListener(plugin), plugin);
                    if (plugin.getConfig().getBoolean("arch.switch_inventory")) {
                        if (plugin.getPM().isPluginEnabled("Multiverse-Inventories")) {
                            plugin.getPM().registerEvents(new TARDISMVIHelper(plugin), plugin);
                        }
                        if (plugin.getPM().isPluginEnabled("GameModeInventories")) {
                            plugin.getPM().registerEvents(new TARDISGMIHelper(plugin), plugin);
                        }
                    }
                }
                plugin.getPM().registerEvents(new TARDISLazarusListener(plugin), plugin);
                plugin.getPM().registerEvents(new TARDISLazarusGUIListener(plugin), plugin);
            }
        }
        plugin.getPM().registerEvents(new TARDISItemFrameListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.wg_flag_set") && plugin.getPM().isPluginEnabled("WorldGuard")) {
            plugin.getPM().registerEvents(new TARDISAntiBuildListener(plugin), plugin);
        }
    }

    private boolean getNPCManager() {
        if (plugin.getPM().getPlugin("Citizens") != null && plugin.getPM().getPlugin("Citizens").isEnabled()) {
            if (plugin.getConfig().getBoolean("allow.emergency_npc")) {
                plugin.debug("Enabling Emergency Programme One!");
            }
            return true;
        } else {
            if (plugin.getConfig().getBoolean("allow.emergency_npc")) {
                plugin.debug("Emergency Programme One was disabled as it requires the Citizens plugin!");
            }
            // set emergency_npc false as Citizens not found
            plugin.getConfig().set("allow.emergency_npc", false);
            plugin.saveConfig();
            return false;
        }
    }
}
