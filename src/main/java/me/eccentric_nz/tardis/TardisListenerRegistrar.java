/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis;

import me.eccentric_nz.tardis.advanced.*;
import me.eccentric_nz.tardis.arch.*;
import me.eccentric_nz.tardis.ars.TardisArsListener;
import me.eccentric_nz.tardis.ars.TardisArsMapListener;
import me.eccentric_nz.tardis.artron.TardisArtronCapacitorListener;
import me.eccentric_nz.tardis.artron.TardisArtronFurnaceListener;
import me.eccentric_nz.tardis.artron.TardisCondenserListener;
import me.eccentric_nz.tardis.chameleon.*;
import me.eccentric_nz.tardis.chemistry.block.ChemistryBlockListener;
import me.eccentric_nz.tardis.chemistry.compound.CompoundGuiListener;
import me.eccentric_nz.tardis.chemistry.compound.GlueListener;
import me.eccentric_nz.tardis.chemistry.constructor.ConstructorGuiListener;
import me.eccentric_nz.tardis.chemistry.creative.CreativeGuiListener;
import me.eccentric_nz.tardis.chemistry.element.ElementGuiListener;
import me.eccentric_nz.tardis.chemistry.formula.FormulaViewerListener;
import me.eccentric_nz.tardis.chemistry.inventory.InventoryHelper;
import me.eccentric_nz.tardis.chemistry.lab.CureBrewingListener;
import me.eccentric_nz.tardis.chemistry.lab.IceBombListener;
import me.eccentric_nz.tardis.chemistry.lab.LabGuiListener;
import me.eccentric_nz.tardis.chemistry.lab.SuperFertiliserListener;
import me.eccentric_nz.tardis.chemistry.product.BalloonListener;
import me.eccentric_nz.tardis.chemistry.product.GlowStickListener;
import me.eccentric_nz.tardis.chemistry.product.ProductGuiListener;
import me.eccentric_nz.tardis.chemistry.product.SparklerListener;
import me.eccentric_nz.tardis.chemistry.reducer.ReducerGuiListener;
import me.eccentric_nz.tardis.commands.config.TardisConfigMenuListener;
import me.eccentric_nz.tardis.commands.preferences.TardisHumListener;
import me.eccentric_nz.tardis.commands.preferences.TardisKeyMenuListener;
import me.eccentric_nz.tardis.commands.preferences.TardisPrefsMenuListener;
import me.eccentric_nz.tardis.commands.utils.TardisWeatherListener;
import me.eccentric_nz.tardis.companiongui.TardisCompanionAddGuiListener;
import me.eccentric_nz.tardis.companiongui.TardisCompanionGuiListener;
import me.eccentric_nz.tardis.control.TardisControlListener;
import me.eccentric_nz.tardis.control.TardisControlMenuListener;
import me.eccentric_nz.tardis.desktop.TardisArchiveMenuListener;
import me.eccentric_nz.tardis.desktop.TardisThemeMenuListener;
import me.eccentric_nz.tardis.desktop.TardisWallMenuListener;
import me.eccentric_nz.tardis.display.TardisDisplayListener;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.InventoryManager;
import me.eccentric_nz.tardis.flight.TardisHandbrakeListener;
import me.eccentric_nz.tardis.flight.TardisManualFlightListener;
import me.eccentric_nz.tardis.flight.TardisRegulatorListener;
import me.eccentric_nz.tardis.handles.TardisHandlesEventListener;
import me.eccentric_nz.tardis.handles.TardisHandlesListener;
import me.eccentric_nz.tardis.handles.TardisHandlesProgramListener;
import me.eccentric_nz.tardis.handles.TardisHandlesSavedListener;
import me.eccentric_nz.tardis.howto.TardisRecipeMenuListener;
import me.eccentric_nz.tardis.howto.TardisSeedMenuListener;
import me.eccentric_nz.tardis.howto.TardisWallFloorMenuListener;
import me.eccentric_nz.tardis.info.TardisInformationSystemListener;
import me.eccentric_nz.tardis.junk.TardisJunkControlListener;
import me.eccentric_nz.tardis.lazarus.TardisLazarusGuiListener;
import me.eccentric_nz.tardis.lazarus.TardisLazarusListener;
import me.eccentric_nz.tardis.listeners.*;
import me.eccentric_nz.tardis.move.*;
import me.eccentric_nz.tardis.planets.*;
import me.eccentric_nz.tardis.recipes.TardisRecipeCategoryListener;
import me.eccentric_nz.tardis.recipes.TardisRecipeInventoryListener;
import me.eccentric_nz.tardis.rooms.TardisJettisonSeeder;
import me.eccentric_nz.tardis.rooms.TardisRoomSeeder;
import me.eccentric_nz.tardis.schematic.TardisSchematicListener;
import me.eccentric_nz.tardis.siegemode.TardisBreedingListener;
import me.eccentric_nz.tardis.siegemode.TardisGrowthListener;
import me.eccentric_nz.tardis.siegemode.TardisSiegeListener;
import me.eccentric_nz.tardis.sonic.*;
import me.eccentric_nz.tardis.transmat.TardisTransmatGuiListener;
import me.eccentric_nz.tardis.travel.TardisAreaSignListener;
import me.eccentric_nz.tardis.travel.TardisTerminalListener;
import me.eccentric_nz.tardis.update.TardisUpdateListener;

import java.util.Objects;

/**
 * Registers all the listeners for the various events required to use the tardis.
 *
 * @author eccentric_nz
 */
class TardisListenerRegistrar {

    private final TardisPlugin plugin;

    TardisListenerRegistrar(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers all the listeners for the various events required to use the TARDIS.
     */
    TardisInformationSystemListener registerListeners() {
        plugin.getPluginManager().registerEvents(new TardisBlockBreakListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisBlockPlaceListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            plugin.getPluginManager().registerEvents(new TardisPoliceBoxDoorListener(plugin), plugin);
            if (plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                plugin.getPluginManager().registerEvents(new TardisAnyoneDoorListener(plugin), plugin);
                plugin.getPluginManager().registerEvents(new TardisAnyoneMoveListener(plugin), plugin);
            } else {
                plugin.getPluginManager().registerEvents(new TardisDoorWalkListener(plugin), plugin);
                plugin.getPluginManager().registerEvents(new TardisMoveListener(plugin), plugin);
            }
        } else {
            plugin.getPluginManager().registerEvents(new TardisDoorClickListener(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisHangingListener(plugin), plugin);
        TardisSonicListener sonicListener = new TardisSonicListener(plugin);
        plugin.getPluginManager().registerEvents(sonicListener, plugin);
        TardisRenderRoomListener rendererListener = new TardisRenderRoomListener(plugin);
        plugin.getPluginManager().registerEvents(rendererListener, plugin);
        plugin.getGeneralKeeper().setRendererListener(rendererListener);
        TardisControlListener buttonListener = new TardisControlListener(plugin);
        plugin.getPluginManager().registerEvents(buttonListener, plugin);
        plugin.getPluginManager().registerEvents(new TardisArsListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisArsMapListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisConfigMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisAnvilListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisAreaListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisAreaSignListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisArtronCapacitorListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisArtronFurnaceListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisBeaconColouringListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisBindListener(plugin), plugin);
        if (!plugin.getDifficulty().equals(Difficulty.HARD)) {
            plugin.getPluginManager().registerEvents(new TardisBiomeReaderListener(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisBlockDamageListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisBlockPhysicsListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisChameleonListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisPoliceBoxListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisChameleonConstructorListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisChameleonConstructorOpenCloseListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisChameleonHelpListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisChameleonTemplateListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisChatListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getPluginManager().registerEvents(new TardisCircuitRepairListener(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisCompanionGuiListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisCompanionAddGuiListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisCondenserListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisConsoleCloseListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisConsoleListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisConsoleSwitchListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisControlMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisCraftListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisCreeperDeathListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisDiskCraftListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisDisplayListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisEjectListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisEntityGriefListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisExplosionListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisFakeChatListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisFarmBlockListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisFireListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisGlassesListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisGameModeSwitcher(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisGravityWellListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisHandbrakeListener(plugin), plugin);
        if (plugin.getHandlesConfig().getBoolean("enabled")) {
            plugin.getPluginManager().registerEvents(new TardisHandlesListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new TardisHandlesEventListener(), plugin);
            plugin.getPluginManager().registerEvents(new TardisHandlesProgramListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new TardisHandlesSavedListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("preferences.nerf_pistons.enabled")) {
            plugin.getPluginManager().registerEvents(new TardisPistonHarvesterListener(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisHorseListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisHotbarListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisHumListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisIceMeltListener(plugin), plugin);
        TardisInformationSystemListener info = new TardisInformationSystemListener(plugin);
        plugin.getPluginManager().registerEvents(info, plugin);
        plugin.getPluginManager().registerEvents(new TardisJettisonSeeder(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisJoinListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisJunkControlListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisKeyboardListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisKeyMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisLightningListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisMakePresetListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisManualFlightListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisMinecartListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
            plugin.getPluginManager().registerEvents(new TardisPerceptionFilterListener(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisPaperBagListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisPistonListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisPortalListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisPrefsMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisPresetListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisQuitListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRecipeListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRecipeInventoryListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRecipeCategoryListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRegulatorListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRemoteKeyListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRedstoneListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRoomSeeder(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSaveSignListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSchematicListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSeedBlockListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            plugin.getPluginManager().registerEvents(new TardisSiegeListener(plugin), plugin);
            if (plugin.getConfig().getInt("siege.breeding") > 0) {
                plugin.getPluginManager().registerEvents(new TardisBreedingListener(plugin), plugin);
            }
            if (plugin.getConfig().getInt("siege.growth") > 0) {
                plugin.getPluginManager().registerEvents(new TardisGrowthListener(plugin), plugin);
            }
        }
        plugin.getPluginManager().registerEvents(new TardisSleepListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSmelterListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSmithingListener(), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicEntityListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicConfiguratorMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicGeneratorMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicGeneratorListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicActivatorListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicSorterListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSonicUpgradeListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSpawnListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisStattenheimListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisStorageListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTagListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTelepathicListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTeleportListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTemporalListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTemporalLocatorListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisThemeMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisArchiveMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTerminalListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTimeLordDeathListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisTransmatGuiListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisUpdateListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisVaultListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisWallMenuListener(plugin), plugin);
        // howto
        plugin.getPluginManager().registerEvents(new TardisWallFloorMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisRecipeMenuListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisSeedMenuListener(plugin), plugin);
        if (plugin.getPluginManager().isPluginEnabled("Multiverse-Adventure")) {
            plugin.getPluginManager().registerEvents(new TardisWorldResetListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            plugin.getPluginManager().registerEvents(new TardisZeroRoomChatListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("arch.enabled")) {
            plugin.getPluginManager().registerEvents(new TardisFobWatchListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new TardisSelectWatchListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new TardisRespawnListener(plugin), plugin);
            if (plugin.getConfig().getBoolean("arch.switch_inventory")) {
                if (!plugin.getInventoryManager().equals(InventoryManager.NONE)) {
                    plugin.getPluginManager().registerEvents(new TardisInventoryPluginHelper(plugin), plugin);
                }
            }
        }
        plugin.getPluginManager().registerEvents(new TardisLazarusListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisLazarusGuiListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisItemFrameListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.wg_flag_set") && plugin.getPluginManager().isPluginEnabled("WorldGuard")) {
            plugin.getPluginManager().registerEvents(new TardisAntiBuildListener(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisPlayerKickListener(plugin), plugin);
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.enabled")) {
            plugin.debug("Skaro enabled, registering planet event listeners");
            plugin.getPluginManager().registerEvents(new TardisSkaroChunkPopulateListener(plugin), plugin);
            if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.acid")) {
                plugin.getPluginManager().registerEvents(new TardisAcidWater(plugin), plugin);
            }
            if (plugin.getPluginManager().getPlugin("TARDISWeepingAngels") != null && Objects.requireNonNull(plugin.getPluginManager().getPlugin("TARDISWeepingAngels")).isEnabled()) {
                plugin.getPluginManager().registerEvents(new TardisSkaroSpawnListener(plugin), plugin);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_siluria.enabled")) {
            plugin.debug("Siluria enabled, registering planet event listeners");
            plugin.getPluginManager().registerEvents(new TardisSiluriaChunkPopulateListener(plugin), plugin);
            if (plugin.getPluginManager().getPlugin("TARDISWeepingAngels") != null && Objects.requireNonNull(plugin.getPluginManager().getPlugin("TARDISWeepingAngels")).isEnabled()) {
                plugin.getPluginManager().registerEvents(new TardisSiluriaSpawnListener(plugin), plugin);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_gallifrey.enabled")) {
            plugin.debug("Gallifrey enabled, registering planet event listeners");
            plugin.getPluginManager().registerEvents(new TardisGallifreySpawnListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new TardisGallifreyChunkPopulateListener(plugin), plugin);
        }
        if (plugin.getPlanetsConfig().getBoolean("switch_resource_packs")) {
            plugin.getPluginManager().registerEvents(new TardisResourcePackSwitcher(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisItemSpawnListener(plugin), plugin);
        plugin.getPluginManager().registerEvents(new TardisAccessoryListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.chemistry")) {
            plugin.getPluginManager().registerEvents(new ChemistryBlockListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new CreativeGuiListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new ElementGuiListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new ConstructorGuiListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new CompoundGuiListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new ReducerGuiListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new ProductGuiListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new LabGuiListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new FormulaViewerListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new GlowStickListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new SparklerListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new IceBombListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new SuperFertiliserListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new CureBrewingListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new GlueListener(), plugin);
            plugin.getPluginManager().registerEvents(new BalloonListener(plugin), plugin);
            plugin.getPluginManager().registerEvents(new InventoryHelper(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") || plugin.getConfig().getBoolean("travel.allow_nether_after_visit")) {
            plugin.getPluginManager().registerEvents(new TardisWorldChangeListener(plugin), plugin);
        }
        plugin.getPluginManager().registerEvents(new TardisWeatherListener(plugin), plugin);
        return info;
    }
}
