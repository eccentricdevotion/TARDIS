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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.converters.ShopTransfer;
import me.eccentric_nz.TARDIS.database.converters.VortexManipulatorTransfer;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.files.AllInOneConfigConverter;
import me.eccentric_nz.tardischemistry.block.ChemistryBlockRecipes;
import me.eccentric_nz.tardischemistry.lab.BleachRecipe;
import me.eccentric_nz.tardischemistry.lab.HeatBlockRunnable;
import me.eccentric_nz.tardischemistry.microscope.Microscope;
import me.eccentric_nz.tardischemistry.product.GlowStickRunnable;
import me.eccentric_nz.tardisregeneration.TARDISRegeneration;
import me.eccentric_nz.tardisshop.TARDISShop;
import me.eccentric_nz.tardisshop.TARDISShopDisplayConverter;
import me.eccentric_nz.tardissonicblaster.TARDISSonicBlaster;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.TARDISEntityRegister;

public class TARDISModuleLoader {

    private final TARDIS plugin;
    private int conversions = 0;

    public TARDISModuleLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        if (plugin.getConfig().getBoolean("modules.chemistry")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.CHEMISTRY, "Loading Chemistry Module");
            new Microscope(plugin).enable();
            new ChemistryBlockRecipes(plugin).addRecipes();
            new BleachRecipe(plugin).setRecipes();
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new GlowStickRunnable(plugin), 200, 200);
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeatBlockRunnable(plugin), 200, 80);
        }
        if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.MONSTERS, "Loading Weeping Angels Module");
            // inject custom entity classes
            new TARDISEntityRegister().inject();
            new TARDISWeepingAngels(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.weeping_angels")) {
                if (new AllInOneConfigConverter(plugin).transferConfig(TardisModule.MONSTERS)) {
                    plugin.getConfig().set("conversions.all_in_one.weeping_angels", true);
                    conversions++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.VORTEX_MANIPULATOR, "Loading Vortex Manipulator Module");
            new TARDISVortexManipulator(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.vortex_manipulator")) {
                boolean cvm = new AllInOneConfigConverter(plugin).transferConfig(TardisModule.VORTEX_MANIPULATOR);
                boolean dvm = new VortexManipulatorTransfer(plugin).transferData();
                if (cvm && dvm) {
                    plugin.getConfig().set("conversions.all_in_one.vortex_manipulator", true);
                    conversions++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("modules.regeneration")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.REGENERATION, "Loading Regeneration Module");
            new TARDISRegeneration(plugin).enable();
        }
        if (plugin.getConfig().getBoolean("modules.shop")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.SHOP, "Loading Shop Module");
            new TARDISShop(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.shop")) {
                boolean cs = new AllInOneConfigConverter(plugin).transferConfig(TardisModule.SHOP);
                boolean ds = new ShopTransfer(plugin).transferData();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISShopDisplayConverter(plugin), 300L);
                if (cs && ds) {
                    plugin.getConfig().set("conversions.all_in_one.shop", true);
                    conversions++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("modules.sonic_blaster")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.BLASTER, "Loading Sonic Blaster Module");
            new TARDISSonicBlaster(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.sonic_blaster")) {
                if (new AllInOneConfigConverter(plugin).transferConfig(TardisModule.BLASTER)) {
                    plugin.getConfig().set("conversions.all_in_one.sonic_blaster", true);
                    conversions++;
                }
            }
        }
        if (conversions > 0) {
            plugin.saveConfig();
        }
    }
}
