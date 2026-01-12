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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.ARS.ARSListener;
import me.eccentric_nz.TARDIS.ARS.ARSMapListener;
import me.eccentric_nz.TARDIS.ARS.relocator.RoomRelocatorListener;
import me.eccentric_nz.TARDIS.advanced.*;
import me.eccentric_nz.TARDIS.arch.*;
import me.eccentric_nz.TARDIS.areas.EditAreasListener;
import me.eccentric_nz.TARDIS.areas.TARDISAreaListener;
import me.eccentric_nz.TARDIS.artron.ArtronCapacitorListener;
import me.eccentric_nz.TARDIS.artron.ArtronCondenserListener;
import me.eccentric_nz.TARDIS.artron.ArtronFurnaceListener;
import me.eccentric_nz.TARDIS.artron.BucketListener;
import me.eccentric_nz.TARDIS.autonomous.AutonomousGUIListener;
import me.eccentric_nz.TARDIS.camera.DismountListener;
import me.eccentric_nz.TARDIS.chameleon.construct.ChameleonConstructorListener;
import me.eccentric_nz.TARDIS.chameleon.construct.ChameleonConstructorOpenCloseListener;
import me.eccentric_nz.TARDIS.chameleon.gui.*;
import me.eccentric_nz.TARDIS.chameleon.shell.PlayerShellListener;
import me.eccentric_nz.TARDIS.chameleon.shell.ShellLoaderListener;
import me.eccentric_nz.TARDIS.commands.config.TARDISConfigMenuListener;
import me.eccentric_nz.TARDIS.commands.utils.weather.WeatherListener;
import me.eccentric_nz.TARDIS.companionGUI.CompanionAddGUIListener;
import me.eccentric_nz.TARDIS.companionGUI.CompanionGUIListener;
import me.eccentric_nz.TARDIS.console.ConsoleInteractionListener;
import me.eccentric_nz.TARDIS.console.CustomiseConsoleListener;
import me.eccentric_nz.TARDIS.console.telepathic.TelepathicBiomeListener;
import me.eccentric_nz.TARDIS.console.telepathic.TelepathicGUIListener;
import me.eccentric_nz.TARDIS.console.telepathic.TelepathicStructureListener;
import me.eccentric_nz.TARDIS.control.ControlListener;
import me.eccentric_nz.TARDIS.control.ControlMenuListener;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockListener;
import me.eccentric_nz.TARDIS.desktop.ArchiveMenuListener;
import me.eccentric_nz.TARDIS.desktop.DesktopThemeMenuListener;
import me.eccentric_nz.TARDIS.desktop.WallMenuListener;
import me.eccentric_nz.TARDIS.display.TARDISDisplayListener;
import me.eccentric_nz.TARDIS.enumeration.InventoryManager;
import me.eccentric_nz.TARDIS.flight.FlightGamemodeListener;
import me.eccentric_nz.TARDIS.flight.HandbrakeListener;
import me.eccentric_nz.TARDIS.flight.ManualFlightListener;
import me.eccentric_nz.TARDIS.flight.RegulatorListener;
import me.eccentric_nz.TARDIS.flight.vehicle.PlayerInputListener;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleLoadListener;
import me.eccentric_nz.TARDIS.handles.HandlesEventListener;
import me.eccentric_nz.TARDIS.handles.HandlesListener;
import me.eccentric_nz.TARDIS.handles.HandlesProgramListener;
import me.eccentric_nz.TARDIS.handles.HandlesSavedListener;
import me.eccentric_nz.TARDIS.howto.RecipeMenuListener;
import me.eccentric_nz.TARDIS.howto.SeedMenuListener;
import me.eccentric_nz.TARDIS.howto.WallFloorMenuListener;
import me.eccentric_nz.TARDIS.info.TARDISIndexFileEntryListener;
import me.eccentric_nz.TARDIS.info.TARDISIndexFileListener;
import me.eccentric_nz.TARDIS.info.TARDISIndexFileSectionListener;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import me.eccentric_nz.TARDIS.junk.JunkControlListener;
import me.eccentric_nz.TARDIS.lazarus.LazarusChoiceListener;
import me.eccentric_nz.TARDIS.lazarus.LazarusGUIListener;
import me.eccentric_nz.TARDIS.lazarus.LazarusListener;
import me.eccentric_nz.TARDIS.lazarus.LazarusSkinsListener;
import me.eccentric_nz.TARDIS.lights.*;
import me.eccentric_nz.TARDIS.listeners.*;
import me.eccentric_nz.TARDIS.listeners.controls.*;
import me.eccentric_nz.TARDIS.mobfarming.FarmingMenuListener;
import me.eccentric_nz.TARDIS.mobfarming.HappyGhastLeashListener;
import me.eccentric_nz.TARDIS.move.AnyoneDoorListener;
import me.eccentric_nz.TARDIS.move.AnyoneMoveListener;
import me.eccentric_nz.TARDIS.move.DoorWalkListener;
import me.eccentric_nz.TARDIS.move.MoveListener;
import me.eccentric_nz.TARDIS.particles.ParticleGUIListener;
import me.eccentric_nz.TARDIS.planets.*;
import me.eccentric_nz.TARDIS.playerprefs.*;
import me.eccentric_nz.TARDIS.recipes.TARDISRecipeCategoryListener;
import me.eccentric_nz.TARDIS.recipes.TARDISRecipeInventoryListener;
import me.eccentric_nz.TARDIS.rooms.JettisonSeeder;
import me.eccentric_nz.TARDIS.rooms.RoomSeeder;
import me.eccentric_nz.TARDIS.rooms.architectural.ArchitecturalBlueprintsListener;
import me.eccentric_nz.TARDIS.rooms.eye.ArtronCapacitorStorageListener;
import me.eccentric_nz.TARDIS.rooms.eye.EyeDamageListener;
import me.eccentric_nz.TARDIS.rooms.eye.SpaceHelmetListener;
import me.eccentric_nz.TARDIS.rooms.laundry.WashingMachineListener;
import me.eccentric_nz.TARDIS.rooms.library.LibraryListener;
import me.eccentric_nz.TARDIS.rooms.smelter.SmelterListener;
import me.eccentric_nz.TARDIS.schematic.SchematicWandListener;
import me.eccentric_nz.TARDIS.siegemode.BreedingListener;
import me.eccentric_nz.TARDIS.siegemode.GrowthListener;
import me.eccentric_nz.TARDIS.siegemode.SiegeListener;
import me.eccentric_nz.TARDIS.skins.SkinListener;
import me.eccentric_nz.TARDIS.skins.tv.TVListener;
import me.eccentric_nz.TARDIS.skins.tv.TVSkinListener;
import me.eccentric_nz.TARDIS.sonic.*;
import me.eccentric_nz.TARDIS.transmat.TransmatGUIListener;
import me.eccentric_nz.TARDIS.travel.TARDISAreaSignListener;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalListener;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesListener;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetListener;
import me.eccentric_nz.TARDIS.universaltranslator.TranslateChatListener;
import me.eccentric_nz.TARDIS.update.TARDISUpdateListener;
import me.eccentric_nz.TARDIS.upgrades.SystemTreeListener;
import me.eccentric_nz.TARDIS.utility.ChunkCleanListener;
import me.eccentric_nz.tardischemistry.block.ChemistryBlockListener;
import me.eccentric_nz.tardischemistry.compound.CompoundGUIListener;
import me.eccentric_nz.tardischemistry.compound.GlueListener;
import me.eccentric_nz.tardischemistry.constructor.ConstructorGUIListener;
import me.eccentric_nz.tardischemistry.creative.CreativeGUIListener;
import me.eccentric_nz.tardischemistry.element.ElementGUIListener;
import me.eccentric_nz.tardischemistry.formula.FormulaViewerListener;
import me.eccentric_nz.tardischemistry.inventory.InventoryHelper;
import me.eccentric_nz.tardischemistry.lab.CureBrewingListener;
import me.eccentric_nz.tardischemistry.lab.IceBombListener;
import me.eccentric_nz.tardischemistry.lab.LabGUIListener;
import me.eccentric_nz.tardischemistry.lab.SuperFertisliserListener;
import me.eccentric_nz.tardischemistry.product.BalloonListener;
import me.eccentric_nz.tardischemistry.product.GlowStickListener;
import me.eccentric_nz.tardischemistry.product.ProductGUIListener;
import me.eccentric_nz.tardischemistry.product.SparklerListener;
import me.eccentric_nz.tardischemistry.reducer.ReducerGUIListener;
import org.bukkit.World;

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
    void registerListeners() {
        plugin.getPM().registerEvents(new PlayerInputListener(plugin), plugin);
        plugin.getPM().registerEvents(new VehicleLoadListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBonemealListener(plugin), plugin);
        plugin.getPM().registerEvents(new EditAreasListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBlockBreakListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBlockPlaceListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("preferences.clean")) {
            plugin.getPM().registerEvents(new ChunkCleanListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("preferences.open_door_policy")) {
            plugin.getPM().registerEvents(new AnyoneDoorListener(plugin), plugin);
            plugin.getPM().registerEvents(new AnyoneMoveListener(plugin), plugin);
        } else {
            plugin.getPM().registerEvents(new DoorWalkListener(plugin), plugin);
            plugin.getPM().registerEvents(new MoveListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISHangingListener(plugin), plugin);
        SonicListener sonicListener = new SonicListener(plugin);
        plugin.getPM().registerEvents(sonicListener, plugin);
        TARDISRenderRoomListener rendererListener = new TARDISRenderRoomListener(plugin);
        plugin.getPM().registerEvents(rendererListener, plugin);
        plugin.getGeneralKeeper().setRendererListener(rendererListener);
        ControlListener buttonListener = new ControlListener(plugin);
        plugin.getPM().registerEvents(buttonListener, plugin);
        plugin.getPM().registerEvents(new ARSListener(plugin), plugin);
        plugin.getPM().registerEvents(new ARSMapListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("modules.blueprints")) {
            plugin.getPM().registerEvents(new ArchitecturalBlueprintsListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("allow.autonomous")) {
            plugin.getPM().registerEvents(new AutonomousGUIListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new FarmingMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISConfigMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAnvilListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAreaListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISAreaSignListener(plugin), plugin);
        plugin.getPM().registerEvents(new ArtronCapacitorListener(plugin), plugin);
        plugin.getPM().registerEvents(new ArtronCapacitorStorageListener(plugin), plugin);
        plugin.getPM().registerEvents(new ArtronFurnaceListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBeaconColouringListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBindListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("difficulty.biome_reader")) {
            plugin.getPM().registerEvents(new TARDISBiomeReaderListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISBlockDamageListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISBlockPhysicsListener(plugin), plugin);
        plugin.getPM().registerEvents(new BucketListener(plugin), plugin);
        plugin.getPM().registerEvents(new ChameleonListener(plugin), plugin);
        plugin.getPM().registerEvents(new ModelledPresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new ChameleonConstructorListener(plugin), plugin);
        plugin.getPM().registerEvents(new ChameleonConstructorOpenCloseListener(plugin), plugin);
        plugin.getPM().registerEvents(new ChameleonHelpListener(plugin), plugin);
        plugin.getPM().registerEvents(new ChameleonTemplateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISChatListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getPM().registerEvents(new CircuitRepairListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new ColourPickerListener(plugin), plugin);
        plugin.getPM().registerEvents(new CompanionGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new CompanionAddGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new ArtronCondenserListener(plugin), plugin);
        plugin.getPM().registerEvents(new AdvancedConsoleCloseListener(plugin), plugin);
        plugin.getPM().registerEvents(new AdvancedConsoleListener(plugin), plugin);
        plugin.getPM().registerEvents(new AdvancedConsoleSwitchListener(plugin), plugin);
        plugin.getPM().registerEvents(new ControlMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISCreeperDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new CustomiseConsoleListener(plugin), plugin);
        plugin.getPM().registerEvents(new DiskCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new DismountListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISDisplayBlockListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISDisplayListener(plugin), plugin);
        plugin.getPM().registerEvents(new ConsoleInteractionListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISEjectListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISEntityGriefListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISExplosionAndDamageListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("eye_of_harmony.player_damage")) {
            plugin.getPM().registerEvents(new EyeDamageListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new ArchFakeChatListener(plugin), plugin);
        plugin.getPM().registerEvents(new SonicFarmBlockListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISFireListener(plugin), plugin);
        plugin.getPM().registerEvents(new FlightGamemodeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISGlassesListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISGameModeSwitcher(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISGravityWellListener(plugin), plugin);
        plugin.getPM().registerEvents(new HandbrakeListener(plugin), plugin);
        if (plugin.getHandlesConfig().getBoolean("enabled")) {
            plugin.getPM().registerEvents(new HandlesListener(plugin), plugin);
            plugin.getPM().registerEvents(new HandlesEventListener(), plugin);
            plugin.getPM().registerEvents(new HandlesProgramListener(plugin), plugin);
            plugin.getPM().registerEvents(new HandlesSavedListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("preferences.nerf_pistons.enabled")) {
            plugin.getPM().registerEvents(new TARDISPistonHarvesterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISRideableMobListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHotbarListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHumListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISIceMeltListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISInformationSystemListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISIndexFileListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISIndexFileSectionListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISIndexFileEntryListener(plugin), plugin);
        plugin.getPM().registerEvents(new JettisonSeeder(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISJoinListener(plugin), plugin);
        plugin.getPM().registerEvents(new JunkControlListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISKeyboardListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISKeyMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new HappyGhastLeashListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISLightningListener(plugin), plugin);
        // lights GUI
        plugin.getPM().registerEvents(new LightsGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new LightLevelsGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new LightSequenceGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new VariableLightBlocksListener(plugin), plugin);
        plugin.getPM().registerEvents(new VariableLightBlocksListener(plugin), plugin);
        plugin.getPM().registerEvents(new LightEmittingListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISMakePresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new ManualFlightListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISMinecartListener(plugin), plugin);
        plugin.getPM().registerEvents(new MonsterFireListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
            plugin.getPM().registerEvents(new TARDISPerceptionFilterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISPaperBagListener(plugin), plugin);
        plugin.getPM().registerEvents(new ParticleGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPortalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISPrefsMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISGeneralPrefsListener(plugin), plugin);
        plugin.getPM().registerEvents(new BlockPresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new CustomPresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISQuitListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRecipeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRecipeInventoryListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRecipeCategoryListener(plugin), plugin);
        plugin.getPM().registerEvents(new RegulatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRemoteKeyListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISRedstoneListener(plugin), plugin);
        plugin.getPM().registerEvents(new RoomRelocatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new RoomSeeder(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSavesListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSavesPlanetListener(plugin), plugin);
        plugin.getPM().registerEvents(new SchematicWandListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSeedBlockListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            plugin.getPM().registerEvents(new SiegeListener(plugin), plugin);
            if (plugin.getConfig().getInt("siege.breeding") > 0) {
                plugin.getPM().registerEvents(new BreedingListener(plugin), plugin);
            }
            if (plugin.getConfig().getInt("siege.growth") > 0) {
                plugin.getPM().registerEvents(new GrowthListener(plugin), plugin);
            }
        }
        plugin.getPM().registerEvents(new TARDISSleepListener(plugin), plugin);
        plugin.getPM().registerEvents(new SmelterListener(plugin), plugin);
        plugin.getPM().registerEvents(new SpaceHelmetListener(), plugin);
        plugin.getPM().registerEvents(new LibraryListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSmithingListener(), plugin);
        plugin.getPM().registerEvents(new SonicEntityListener(plugin), plugin);
        plugin.getPM().registerEvents(new SonicConfiguratorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new SonicGeneratorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new SonicGeneratorListener(plugin), plugin);
        plugin.getPM().registerEvents(new SonicActivatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new SonicSorterListener(plugin), plugin);
        plugin.getPM().registerEvents(new SonicUpgradeListener(), plugin);
        plugin.getPM().registerEvents(new TARDISSpawnListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISStattenheimListener(plugin), plugin);
        plugin.getPM().registerEvents(new StorageListener(plugin), plugin);
        plugin.getPM().registerEvents(new SystemTreeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTagListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTelepathicListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTeleportListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVSkinListener(plugin), plugin);
        plugin.getPM().registerEvents(new SkinListener(), plugin);
        plugin.getPM().registerEvents(new ElytraListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTemporalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTemporalLocatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new DesktopThemeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TranslateChatListener(plugin), plugin);
        plugin.getPM().registerEvents(new ArchiveMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTerminalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISTimeLordDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TransmatGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISUpdateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISVaultListener(plugin), plugin);
        plugin.getPM().registerEvents(new WallMenuListener(plugin), plugin);
        // howto
        plugin.getPM().registerEvents(new WallFloorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new RecipeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new SeedMenuListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            plugin.getPM().registerEvents(new TARDISZeroRoomChatListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("arch.enabled")) {
            plugin.getPM().registerEvents(new FobWatchListener(plugin), plugin);
            plugin.getPM().registerEvents(new SelectFobWatchListener(plugin), plugin);
            plugin.getPM().registerEvents(new ArchRespawnListener(plugin), plugin);
            if (plugin.getConfig().getBoolean("arch.switch_inventory")) {
                if (!plugin.getInvManager().equals(InventoryManager.NONE)) {
                    plugin.getPM().registerEvents(new InventoryPluginHelper(plugin), plugin);
                }
            }
        }
        plugin.getPM().registerEvents(new LazarusListener(plugin), plugin);
        plugin.getPM().registerEvents(new LazarusChoiceListener(plugin), plugin);
        plugin.getPM().registerEvents(new LazarusGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new LazarusSkinsListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISItemFrameUpdateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISDirectionFrameListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISHandlesFrameListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISLightLevelFrameListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISMonitorFrameListener(plugin), plugin);
        plugin.getPM().registerEvents(new PlayerShellListener(plugin), plugin);
        plugin.getPM().registerEvents(new WashingMachineListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.wg_flag_set") && plugin.getPM().isPluginEnabled("WorldGuard")) {
            plugin.getPM().registerEvents(new TARDISAntiBuildListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISPlayerKickListener(plugin), plugin);
        // telepathic listeners
        plugin.getPM().registerEvents(new TelepathicGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TelepathicBiomeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TelepathicStructureListener(plugin), plugin);
        if (plugin.getPlanetsConfig().getBoolean("planets.telos.enabled")) {
            plugin.debug("Telos enabled, registering planet event listeners");
            if (plugin.getPlanetsConfig().getBoolean("planets.telos.vastial.enabled")) {
                plugin.getPM().registerEvents(new TARDISVastialListener(plugin), plugin);
            }
            if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                plugin.getPM().registerEvents(new TARDISTelosSpawnListener(plugin), plugin);
            }
            // set world time to twilight
            World telos = plugin.getServer().getWorld("telos");
            if (telos!= null) {
                telos.setTime(13000L);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets.skaro.enabled")) {
            plugin.debug("Skaro enabled, registering planet event listeners");
            if (plugin.getPlanetsConfig().getBoolean("planets.skaro.acid")) {
                plugin.getPM().registerEvents(new TARDISAcidWater(plugin), plugin);
            }
            if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                plugin.getPM().registerEvents(new TARDISSkaroSpawnListener(plugin), plugin);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets.siluria.enabled") && plugin.getConfig().getBoolean("modules.weeping_angels")) {
            plugin.debug("Siluria enabled, registering planet event listeners");
            plugin.getPM().registerEvents(new TARDISSiluriaSpawnListener(plugin), plugin);
        }
        if (plugin.getPlanetsConfig().getBoolean("planets.gallifrey.enabled")) {
            plugin.debug("Gallifrey enabled, registering planet event listeners");
            plugin.getPM().registerEvents(new TARDISGallifreySpawnListener(plugin), plugin);
            plugin.getPM().registerEvents(new GallifreyTradeSelectListener(plugin), plugin);
        }
        if (plugin.getPlanetsConfig().getBoolean("switch_resource_packs")) {
            plugin.getPM().registerEvents(new TARDISResourcePackSwitcher(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TARDISItemSpawnListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("modules.chemistry")) {
            plugin.getPM().registerEvents(new ChemistryBlockListener(plugin), plugin);
            plugin.getPM().registerEvents(new CreativeGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new ElementGUIListener(plugin), plugin);
            plugin.getPM().registerEvents(new ConstructorGUIListener(plugin), plugin);
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
        if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") || plugin.getConfig().getBoolean("travel.allow_nether_after_visit")) {
            plugin.getPM().registerEvents(new TARDISWorldChangeListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("police_box.load_shells")) {
            plugin.getPM().registerEvents(new ShellLoaderListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new WeatherListener(plugin), plugin);
    }
}
