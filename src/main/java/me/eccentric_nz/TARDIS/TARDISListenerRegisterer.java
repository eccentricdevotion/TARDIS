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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.ARS.TARDISARSListener;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMapListener;
import me.eccentric_nz.TARDIS.advanced.*;
import me.eccentric_nz.TARDIS.arch.*;
import me.eccentric_nz.TARDIS.artron.TARDISArtronCapacitorListener;
import me.eccentric_nz.TARDIS.artron.TARDISArtronFurnaceListener;
import me.eccentric_nz.TARDIS.artron.TARDISCondenserListener;
import me.eccentric_nz.TARDIS.chameleon.*;
import me.eccentric_nz.TARDIS.chemistry.block.ChemistryBlockListener;
import me.eccentric_nz.TARDIS.chemistry.compound.CompoundGUIListener;
import me.eccentric_nz.TARDIS.chemistry.compound.GlueListener;
import me.eccentric_nz.TARDIS.chemistry.constructor.ConstructorGUIListener;
import me.eccentric_nz.TARDIS.chemistry.creative.CreativeGUIListener;
import me.eccentric_nz.TARDIS.chemistry.element.ElementGUIListener;
import me.eccentric_nz.TARDIS.chemistry.formula.FormulaViewerListener;
import me.eccentric_nz.TARDIS.chemistry.inventory.InventoryHelper;
import me.eccentric_nz.TARDIS.chemistry.lab.CureBrewingListener;
import me.eccentric_nz.TARDIS.chemistry.lab.IceBombListener;
import me.eccentric_nz.TARDIS.chemistry.lab.LabGUIListener;
import me.eccentric_nz.TARDIS.chemistry.lab.SuperFertisliserListener;
import me.eccentric_nz.TARDIS.chemistry.product.BalloonListener;
import me.eccentric_nz.TARDIS.chemistry.product.GlowStickListener;
import me.eccentric_nz.TARDIS.chemistry.product.ProductGUIListener;
import me.eccentric_nz.TARDIS.chemistry.product.SparklerListener;
import me.eccentric_nz.TARDIS.chemistry.reducer.ReducerGUIListener;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminMenuListener;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISHumListener;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISKeyMenuListener;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuListener;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionAddGUIListener;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionGUIListener;
import me.eccentric_nz.TARDIS.control.TARDISControlListener;
import me.eccentric_nz.TARDIS.control.TARDISControlMenuListener;
import me.eccentric_nz.TARDIS.desktop.TARDISArchiveMenuListener;
import me.eccentric_nz.TARDIS.desktop.TARDISThemeMenuListener;
import me.eccentric_nz.TARDIS.desktop.TARDISWallMenuListener;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.INVENTORY_MANAGER;
import me.eccentric_nz.TARDIS.flight.TARDISHandbrakeListener;
import me.eccentric_nz.TARDIS.flight.TARDISManualFlightListener;
import me.eccentric_nz.TARDIS.flight.TARDISRegulatorListener;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesEventListener;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesListener;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProgramListener;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesSavedListener;
import me.eccentric_nz.TARDIS.howto.TARDISRecipeMenuListener;
import me.eccentric_nz.TARDIS.howto.TARDISSeedMenuListener;
import me.eccentric_nz.TARDIS.howto.TARDISWallFloorMenuListener;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import me.eccentric_nz.TARDIS.junk.TARDISJunkControlListener;
import me.eccentric_nz.TARDIS.lazarus.TARDISLazarusGUIListener;
import me.eccentric_nz.TARDIS.lazarus.TARDISLazarusListener;
import me.eccentric_nz.TARDIS.listeners.*;
import me.eccentric_nz.TARDIS.move.*;
import me.eccentric_nz.TARDIS.planets.*;
import me.eccentric_nz.TARDIS.rooms.TARDISJettisonSeeder;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomSeeder;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicListener;
import me.eccentric_nz.TARDIS.siegemode.TARDISBreedingListener;
import me.eccentric_nz.TARDIS.siegemode.TARDISGrowthListener;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeListener;
import me.eccentric_nz.TARDIS.sonic.*;
import me.eccentric_nz.TARDIS.transmat.TARDISTransmatGUIListener;
import me.eccentric_nz.TARDIS.travel.TARDISAreaSignListener;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalListener;
import me.eccentric_nz.TARDIS.update.TARDISUpdateListener;

/**
 * Registers all the listeners for the various events required to use the TARDIS.
 *
 * @author eccentric_nz
 */
class TARDISListenerRegisterer {

    private final TARDIS plugin;

    TARDISListenerRegisterer(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers all the listeners for the various events required to use the TARDIS.
     */
    TARDISInformationSystemListener registerListeners() {
        plugin.getPM().registerEvents(new TARDISBlockBreakListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBlockPlaceListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            if (plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                plugin.getPM().registerEvents(new TARDISAnyoneDoorListener(plugin), plugin);
                plugin.getPM().registerEvents(new TARDISAnyoneMoveListener(plugin), plugin);
            } else {
                plugin.getPM().registerEvents(new TARDISDoorWalkListener(plugin), plugin);
                plugin.getPM().registerEvents(new TARDISMoveListener(plugin), plugin);
            }
        } else {
            plugin.getPM().registerEvents(new TARDISDoorClickListener(plugin), plugin);
        }
        TARDISSonicListener sonicListener = new TARDISSonicListener(plugin);
        plugin.getPM().registerEvents(sonicListener, plugin);
        TARDISRenderRoomListener rendererListener = new TARDISRenderRoomListener(plugin);
        plugin.getPM().registerEvents(rendererListener, plugin);
        plugin.getGeneralKeeper().setRendererListener(rendererListener);
        TARDISControlListener buttonListener = new TARDISControlListener(plugin);
        plugin.getPM().registerEvents(buttonListener, plugin);
        plugin.getPM().registerEvents(new TARDISARSListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISARSMapListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAdminMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAnvilListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAreaListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAreaSignListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISArtronCapacitorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISArtronFurnaceListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBeaconColouringListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBindListener(plugin), plugin);
        if (!plugin.getDifficulty().equals(DIFFICULTY.HARD)) {
            plugin.getPM().registerEvents(new TARDISBiomeReaderListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISBlockDamageListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBlockPhysicsListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPoliceBoxListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonConstructorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonConstructorOpenCloseListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonHelpListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChameleonTemplateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChatListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getPM().registerEvents(new TARDISCircuitRepairListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISCompanionGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISCompanionAddGUIListener(plugin), plugin);
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
        plugin.getPM().registerEvents(new TARDISGameModeSwitcher(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISGravityWellListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHandbrakeListener(plugin), plugin);
        if (plugin.getHandlesConfig().getBoolean("enabled")) {
            plugin.getPM().registerEvents(new TARDISHandlesListener(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISHandlesEventListener(), plugin);
            plugin.getPM().registerEvents(new TARDISHandlesProgramListener(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISHandlesSavedListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("preferences.nerf_pistons.enabled")) {
            plugin.getPM().registerEvents(new TARDISPistonHarvesterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISHorseListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHotbarListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHumListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISIceMeltListener(plugin), plugin);
        TARDISInformationSystemListener info = new TARDISInformationSystemListener(plugin);
        plugin.getPM().registerEvents(info, plugin);
        plugin.getPM().registerEvents(new TARDISJettisonSeeder(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISJoinListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISJunkControlListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISKeyboardListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISKeyMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISLightningListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISMakePresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISManualFlightListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISMinecartListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
            plugin.getPM().registerEvents(new TARDISPerceptionFilterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISPaperBagListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPistonListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPrefsMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISQuitListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRecipeListener(plugin), plugin);
        if (plugin.getPM().isPluginEnabled("ProjectRassilon")) {
            plugin.getPM().registerEvents(new TARDISRegenerationListener(plugin), plugin);
        }
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
        plugin.getPM().registerEvents(new TARDISSleepListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSmelterListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicEntityListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicGeneratorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicGeneratorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicActivatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicSorterListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicUpgradeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSpawnListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISStattenheimListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISStorageListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTagListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTelepathicListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTeleportListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTemporalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTemporalLocatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISThemeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISArchiveMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTerminalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTimeLordDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTransmatGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISUpdateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISVaultListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISWallMenuListener(plugin), plugin);
        // howto
        plugin.getPM().registerEvents(new TARDISWallFloorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRecipeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSeedMenuListener(plugin), plugin);
        if (getNPCManager()) {
            plugin.getPM().registerEvents(new TARDISNPCListener(plugin), plugin);
        }
        if (plugin.getPM().isPluginEnabled("Multiverse-Adventure")) {
            plugin.getPM().registerEvents(new TARDISWorldResetListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISZeroRoomChatListener(plugin), plugin);
        if (plugin.getPM().isPluginEnabled("ProtocolLib")) {
            if (plugin.getConfig().getBoolean("allow.zero_room")) {
                new TARDISZeroRoomPacketListener(plugin);
            }
        }
        if (plugin.getConfig().getBoolean("arch.enabled")) {
            plugin.getPM().registerEvents(new TARDISFobWatchListener(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISSelectWatchListener(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISRespawnListener(plugin), plugin);
            if (plugin.getConfig().getBoolean("arch.switch_inventory")) {
                if (!plugin.getInvManager().equals(INVENTORY_MANAGER.NONE)) {
                    plugin.getPM().registerEvents(new TARDISInventoryPluginHelper(plugin), plugin);
                }
            }
        }
        plugin.getPM().registerEvents(new TARDISLazarusListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISLazarusGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISItemFrameListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.wg_flag_set") && plugin.getPM().isPluginEnabled("WorldGuard")) {
            plugin.getPM().registerEvents(new TARDISAntiBuildListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISPlayerKickListener(plugin), plugin);
        if (plugin.getPlanetsConfig().getBoolean("planets.Skaro.enabled")) {
            plugin.getPM().registerEvents(new TARDISSkaroChunkPopulateListener(plugin), plugin);
            if (plugin.getPlanetsConfig().getBoolean("planets.Skaro.acid")) {
                plugin.getPM().registerEvents(new TARDISAcidWater(plugin), plugin);
            }
            if (plugin.getPM().getPlugin("TARDISWeepingAngels") != null && plugin.getPM().getPlugin("TARDISWeepingAngels").isEnabled()) {
                plugin.getPM().registerEvents(new TARDISSkaroSpawnListener(plugin), plugin);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets.Siluria.enabled")) {
            plugin.getPM().registerEvents(new TARDISSiluriaChunkPopulateListener(plugin), plugin);
            if (plugin.getPM().getPlugin("TARDISWeepingAngels") != null && plugin.getPM().getPlugin("TARDISWeepingAngels").isEnabled()) {
                plugin.getPM().registerEvents(new TARDISSiluriaSpawnListener(plugin), plugin);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets.Gallifrey.enabled")) {
            plugin.getPM().registerEvents(new TARDISGallifreySpawnListener(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISGallifreyChunkPopulateListener(plugin), plugin);
        }
        if (plugin.getPlanetsConfig().getBoolean("switch_resource_packs")) {
            plugin.getPM().registerEvents(new TARDISResourcePackSwitcher(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISItemSpawnListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAccessoryListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.chemistry")) {
            plugin.getPM().registerEvents(new ChemistryBlockListener(plugin), plugin);
            plugin.getPM().registerEvents(new CreativeGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new ElementGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new ConstructorGUIListener(), plugin);
            plugin.getPM().registerEvents(new CompoundGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new ReducerGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new ProductGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new LabGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new FormulaViewerListener(plugin), plugin);
            plugin.getPM().registerEvents(new GlowStickListener(plugin), plugin);
            plugin.getPM().registerEvents(new SparklerListener(plugin), plugin);
            plugin.getPM().registerEvents(new IceBombListener(plugin), plugin);
            plugin.getPM().registerEvents(new SuperFertisliserListener(plugin), plugin);
            plugin.getPM().registerEvents(new CureBrewingListener(plugin), plugin);
            plugin.getPM().registerEvents(new GlueListener(), plugin);
            plugin.getPM().registerEvents(new BalloonListener(plugin), plugin);
            plugin.getPM().registerEvents(new InventoryHelper(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") || plugin.getConfig().getBoolean("travel.allow_end_after_visit")) {
            plugin.getPM().registerEvents(new TARDISWorldChangeListener(plugin), plugin);
        }
        return info;
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
