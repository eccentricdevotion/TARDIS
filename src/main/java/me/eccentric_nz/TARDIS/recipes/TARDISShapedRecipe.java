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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.recipes.shaped.*;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISShapedRecipe {

    private final TARDIS plugin;
    private final HashMap<String, ShapedRecipe> shapedRecipes;

    public TARDISShapedRecipe(TARDIS plugin) {
        this.plugin = plugin;
        shapedRecipes = new HashMap<>();
    }

    public void addShapedRecipes() {
        new AcidBatteryRecipe(plugin).addRecipe();
        new ArtronCapacitorRecipe(plugin).addRecipe();
        new ArtronStorageCellRecipe(plugin).addRecipe();
        new AuthorisedControlDiskRecipe(plugin).addRecipe();
        new BioscannerCircuitRecipe(plugin).addRecipe();
        new BlackBowTieRecipe(plugin).addRecipe();
        new BlankStorageDiskRecipe(plugin).addRecipe();
        new BlueBowTieRecipe(plugin).addRecipe();
        new BrownBowTieRecipe(plugin).addRecipe();
        new BrushCircuitRecipe(plugin).addRecipe();
        new ConsoleRecipe(plugin).addRecipes();
        new ConsoleRusticRecipe(plugin).addRecipe();
        new ConversionCircuitRecipe(plugin).addRecipe();
        new CustardCreamRecipe(plugin).addRecipe();
        new CyanBowTieRecipe(plugin).addRecipe();
        new DiamondDisruptorCircuitRecipe(plugin).addRecipe();
        new EmeraldEnvironmentCircuitRecipe(plugin).addRecipe();
        new ExteriorLampLevelSwitchRecipe(plugin).addRecipe();
        new FishFingerRecipe(plugin).addRecipe();
        new FobWatchRecipe(plugin).addRecipe();
        new GreenBowTieRecipe(plugin).addRecipe();
        new GreyBowTieRecipe(plugin).addRecipe();
        new HandlesRecipe(plugin).addRecipe();
        new IgniteCircuitRecipe(plugin).addRecipe();
        new InteriorLightLevelSwitchRecipe(plugin).addRecipe();
        new JammyDodgerRecipe(plugin).addRecipe();
        new KnockbackCircuitRecipe(plugin).addRecipe();
        new LightBlueBowTieRecipe(plugin).addRecipe();
        new LightGreyBowTieRecipe(plugin).addRecipe();
        new LimeBowTieRecipe(plugin).addRecipe();
        new MagentaBowTieRecipe(plugin).addRecipe();
        new MonitorFrameRecipe(plugin).addRecipe();
        new OrangeBowTieRecipe(plugin).addRecipe();
        new PainterCircuitRecipe(plugin).addRecipe();
        new PaperBagRecipe(plugin).addRecipe();
        new PerceptionCircuitRecipe(plugin).addRecipe();
        new PerceptionFilterRecipe(plugin).addRecipe();
        new PickupArrowsCircuitRecipe(plugin).addRecipe();
        new PinkBowTieRecipe(plugin).addRecipe();
        new PurpleBowTieRecipe(plugin).addRecipe();
        new RedBowTieRecipe(plugin).addRecipe();
        new RedstoneActivatorCircuitRecipe(plugin).addRecipe();
        new RiftCircuitRecipe(plugin).addRecipe();
        new RiftManipulatorRecipe(plugin).addRecipe();
        new RustPlagueSwordRecipe(plugin).addRecipe();
        new ServerAdminCircuitRecipe(plugin).addRecipe();
        new SonicDockRecipe(plugin).addRecipe();
        new SonicGeneratorRecipe(plugin).addRecipe();
        new SonicOscillatorRecipe(plugin).addRecipe();
        new SonicScrewdriverRecipe(plugin).addRecipe();
        new StattenheimRemoteRecipe(plugin).addRecipe();
        new TARDISARSCircuitRecipe(plugin).addRecipe();
        new TARDISArtronFurnaceRecipe(plugin).addRecipe();
        new TARDISBiomeReaderRecipe(plugin).addRecipe();
        new TARDISChameleonCircuitRecipe(plugin).addRecipe();
        new TARDISCommunicatorRecipe(plugin).addRecipe();
        new TARDISInputCircuitRecipe(plugin).addRecipe();
        new TARDISInvisibilityCircuitRecipe(plugin).addRecipe();
        new TARDISKeyRecipe(plugin).addRecipe();
        new TARDISLocatorCircuitRecipe(plugin).addRecipe();
        new TARDISLocatorRecipe(plugin).addRecipe();
        new TARDISMaterialisationCircuitRecipe(plugin).addRecipe();
        new TARDISMemoryCircuitRecipe(plugin).addRecipe();
        new TARDISMonitorRecipe(plugin).addRecipe();
        new TARDISRandomiserCircuitRecipe(plugin).addRecipe();
        new TARDISRemoteKeyRecipe(plugin).addRecipe();
        new TARDISScannerCircuitRecipe(plugin).addRecipe();
        new TARDISStattenheimCircuitRecipe(plugin).addRecipe();
        new TARDISTelepathicCircuitRecipe(plugin).addRecipe();
        new TARDISTemporalCircuitRecipe(plugin).addRecipe();
        new ThreeDGlassesRecipe(plugin).addRecipe();
        new TimeEngineRecipe(plugin).addRecipe();
        new TimeRotorConsoleRecipe(plugin).addRecipe();
        new TimeRotorDeltaRecipe(plugin).addRecipe();
        new TimeRotorEarlyRecipe(plugin).addRecipe();
        new TimeRotorEleventhRecipe(plugin).addRecipe();
        new TimeRotorEngineRecipe(plugin).addRecipe();
        new TimeRotorHospitalRecipe(plugin).addRecipe();
        new TimeRotorTenthRecipe(plugin).addRecipe();
        new TimeRotorTwelfthRecipe(plugin).addRecipe();
        new TimeRotorCustomRecipe(plugin).addRecipes();
        new TimeRotorRusticRecipe(plugin).addRecipe();
        new WhiteBowTieRecipe(plugin).addRecipe();
        new YellowBowTieRecipe(plugin).addRecipe();
        new DoorCustomRecipe(plugin).addRecipes();
    }

    public HashMap<String, ShapedRecipe> getShapedRecipes() {
        return shapedRecipes;
    }
}
