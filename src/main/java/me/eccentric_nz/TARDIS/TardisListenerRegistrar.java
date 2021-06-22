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
import me.eccentric_nz.tardis.chemistry.lab.SuperFertisliserListener;
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
        plugin.getPM().registerEvents(new TardisBlockBreakListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisBlockPlaceListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            plugin.getPM().registerEvents(new TardisPoliceBoxDoorListener(plugin), plugin);
            if (plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                plugin.getPM().registerEvents(new TardisAnyoneDoorListener(plugin), plugin);
                plugin.getPM().registerEvents(new TardisAnyoneMoveListener(plugin), plugin);
            } else {
                plugin.getPM().registerEvents(new TardisDoorWalkListener(plugin), plugin);
                plugin.getPM().registerEvents(new TardisMoveListener(plugin), plugin);
            }
        } else {
            plugin.getPM().registerEvents(new TardisDoorClickListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisHangingListener(plugin), plugin);
        TardisSonicListener sonicListener = new TardisSonicListener(plugin);
        plugin.getPM().registerEvents(sonicListener, plugin);
        TardisRenderRoomListener rendererListener = new TardisRenderRoomListener(plugin);
        plugin.getPM().registerEvents(rendererListener, plugin);
        plugin.getGeneralKeeper().setRendererListener(rendererListener);
        TardisControlListener buttonListener = new TardisControlListener(plugin);
        plugin.getPM().registerEvents(buttonListener, plugin);
        plugin.getPM().registerEvents(new TardisArsListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisArsMapListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisConfigMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisAnvilListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisAreaListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisAreaSignListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisArtronCapacitorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisArtronFurnaceListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisBeaconColouringListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisBindListener(plugin), plugin);
        if (!plugin.getDifficulty().equals(Difficulty.HARD)) {
            plugin.getPM().registerEvents(new TardisBiomeReaderListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisBlockDamageListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisBlockPhysicsListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisChameleonListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisPoliceBoxListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisChameleonConstructorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisChameleonConstructorOpenCloseListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisChameleonHelpListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisChameleonTemplateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisChatListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getPM().registerEvents(new TardisCircuitRepairListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisCompanionGuiListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisCompanionAddGuiListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisCondenserListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisConsoleCloseListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisConsoleListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisConsoleSwitchListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisControlMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisCreeperDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisDiskCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisDisplayListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisEjectListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisEntityGriefListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisExplosionListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisFakeChatListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisFarmBlockListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisFireListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisGlassesListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisGameModeSwitcher(plugin), plugin);
        plugin.getPM().registerEvents(new TardisGravityWellListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisHandbrakeListener(plugin), plugin);
        if (plugin.getHandlesConfig().getBoolean("enabled")) {
            plugin.getPM().registerEvents(new TardisHandlesListener(plugin), plugin);
            plugin.getPM().registerEvents(new TardisHandlesEventListener(), plugin);
            plugin.getPM().registerEvents(new TardisHandlesProgramListener(plugin), plugin);
            plugin.getPM().registerEvents(new TardisHandlesSavedListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("preferences.nerf_pistons.enabled")) {
            plugin.getPM().registerEvents(new TardisPistonHarvesterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisHorseListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisHotbarListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisHumListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisIceMeltListener(plugin), plugin);
        TardisInformationSystemListener info = new TardisInformationSystemListener(plugin);
        plugin.getPM().registerEvents(info, plugin);
        plugin.getPM().registerEvents(new TardisJettisonSeeder(plugin), plugin);
        plugin.getPM().registerEvents(new TardisJoinListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisJunkControlListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisKeyboardListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisKeyMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisLightningListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisMakePresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisManualFlightListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisMinecartListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
            plugin.getPM().registerEvents(new TardisPerceptionFilterListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisPaperBagListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisPistonListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisPortalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisPrefsMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisPresetListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisQuitListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRecipeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRecipeInventoryListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRecipeCategoryListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRegulatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRemoteKeyListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRedstoneListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRoomSeeder(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSaveSignListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSchematicListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSeedBlockListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            plugin.getPM().registerEvents(new TardisSiegeListener(plugin), plugin);
            if (plugin.getConfig().getInt("siege.breeding") > 0) {
                plugin.getPM().registerEvents(new TardisBreedingListener(plugin), plugin);
            }
            if (plugin.getConfig().getInt("siege.growth") > 0) {
                plugin.getPM().registerEvents(new TardisGrowthListener(plugin), plugin);
            }
        }
        plugin.getPM().registerEvents(new TardisSleepListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSmelterListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSmithingListener(), plugin);
        plugin.getPM().registerEvents(new TardisSonicEntityListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSonicConfiguratorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSonicGeneratorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSonicGeneratorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSonicActivatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSonicMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSonicSorterListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSonicUpgradeListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSpawnListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisStattenheimListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisStorageListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTagListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTelepathicListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTeleportListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTemporalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTemporalLocatorListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisThemeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisArchiveMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTerminalListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTimeLordDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisTransmatGuiListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisUpdateListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisVaultListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisWallMenuListener(plugin), plugin);
        // howto
        plugin.getPM().registerEvents(new TardisWallFloorMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisRecipeMenuListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisSeedMenuListener(plugin), plugin);
        if (plugin.getPM().isPluginEnabled("Multiverse-Adventure")) {
            plugin.getPM().registerEvents(new TardisWorldResetListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            plugin.getPM().registerEvents(new TardisZeroRoomChatListener(plugin), plugin);
        }
        if (plugin.getConfig().getBoolean("arch.enabled")) {
            plugin.getPM().registerEvents(new TardisFobWatchListener(plugin), plugin);
            plugin.getPM().registerEvents(new TardisSelectWatchListener(plugin), plugin);
            plugin.getPM().registerEvents(new TardisRespawnListener(plugin), plugin);
            if (plugin.getConfig().getBoolean("arch.switch_inventory")) {
                if (!plugin.getInventoryManager().equals(InventoryManager.NONE)) {
                    plugin.getPM().registerEvents(new TardisInventoryPluginHelper(plugin), plugin);
                }
            }
        }
        plugin.getPM().registerEvents(new TardisLazarusListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisLazarusGuiListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisItemFrameListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.wg_flag_set") && plugin.getPM().isPluginEnabled("WorldGuard")) {
            plugin.getPM().registerEvents(new TardisAntiBuildListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisPlayerKickListener(plugin), plugin);
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.enabled")) {
            plugin.debug("Skaro enabled, registering planet event listeners");
            plugin.getPM().registerEvents(new TardisSkaroChunkPopulateListener(plugin), plugin);
            if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.acid")) {
                plugin.getPM().registerEvents(new TardisAcidWater(plugin), plugin);
            }
            if (plugin.getPM().getPlugin("TARDISWeepingAngels") != null && Objects.requireNonNull(plugin.getPM().getPlugin("TARDISWeepingAngels")).isEnabled()) {
                plugin.getPM().registerEvents(new TardisSkaroSpawnListener(plugin), plugin);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_siluria.enabled")) {
            plugin.debug("Siluria enabled, registering planet event listeners");
            plugin.getPM().registerEvents(new TardisSiluriaChunkPopulateListener(plugin), plugin);
            if (plugin.getPM().getPlugin("TARDISWeepingAngels") != null && Objects.requireNonNull(plugin.getPM().getPlugin("TARDISWeepingAngels")).isEnabled()) {
                plugin.getPM().registerEvents(new TardisSiluriaSpawnListener(plugin), plugin);
            }
        }
        if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_gallifrey.enabled")) {
            plugin.debug("Gallifrey enabled, registering planet event listeners");
            plugin.getPM().registerEvents(new TardisGallifreySpawnListener(plugin), plugin);
            plugin.getPM().registerEvents(new TardisGallifreyChunkPopulateListener(plugin), plugin);
        }
        if (plugin.getPlanetsConfig().getBoolean("switch_resource_packs")) {
            plugin.getPM().registerEvents(new TardisResourcePackSwitcher(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisItemSpawnListener(plugin), plugin);
        plugin.getPM().registerEvents(new TardisAccessoryListener(plugin), plugin);
        if (plugin.getConfig().getBoolean("allow.chemistry")) {
            plugin.getPM().registerEvents(new ChemistryBlockListener(plugin), plugin);
            plugin.getPM().registerEvents(new CreativeGuiListener(plugin), plugin);
            plugin.getPM().registerEvents(new ElementGuiListener(plugin), plugin);
            plugin.getPM().registerEvents(new ConstructorGuiListener(plugin), plugin);
            plugin.getPM().registerEvents(new CompoundGuiListener(plugin), plugin);
            plugin.getPM().registerEvents(new ReducerGuiListener(plugin), plugin);
            plugin.getPM().registerEvents(new ProductGuiListener(plugin), plugin);
            plugin.getPM().registerEvents(new LabGuiListener(plugin), plugin);
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
            plugin.getPM().registerEvents(new TardisWorldChangeListener(plugin), plugin);
        }
        plugin.getPM().registerEvents(new TardisWeatherListener(plugin), plugin);
        return info;
    }
}
