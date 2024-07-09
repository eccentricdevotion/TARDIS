package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.converters.TARDISShopTransfer;
import me.eccentric_nz.TARDIS.database.converters.TARDISVortexManipulatorTransfer;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.files.TARDISAllInOneConfigConverter;
import me.eccentric_nz.tardischemistry.block.ChemistryBlockRecipes;
import me.eccentric_nz.tardischemistry.lab.BleachRecipe;
import me.eccentric_nz.tardischemistry.lab.HeatBlockRunnable;
import me.eccentric_nz.tardischemistry.product.GlowStickRunnable;
import me.eccentric_nz.tardisshop.TARDISShop;
import me.eccentric_nz.tardisshop.TARDISShopDisplayConverter;
import me.eccentric_nz.tardissonicblaster.TARDISSonicBlaster;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;

public class TARDISModuleLoader {

    private final TARDIS plugin;
    private int conversions = 0;

    public TARDISModuleLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        if (plugin.getConfig().getBoolean("modules.chemistry")) {
            new ChemistryBlockRecipes(plugin).addRecipes();
            new BleachRecipe(plugin).setRecipes();
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new GlowStickRunnable(plugin), 200, 200);
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeatBlockRunnable(plugin), 200, 80);
        }
        if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loading Weeping Angels Module");
            new TARDISWeepingAngels(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.weeping_angels")) {
                if (new TARDISAllInOneConfigConverter(plugin).transferConfig(TardisModule.MONSTERS)) {
                    plugin.getConfig().set("conversions.all_in_one.weeping_angels", true);
                    conversions++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loading Vortex Manipulator Module");
            new TARDISVortexManipulator(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.vortex_manipulator")) {
                boolean cvm = new TARDISAllInOneConfigConverter(plugin).transferConfig(TardisModule.VORTEX_MANIPULATOR);
                boolean dvm = new TARDISVortexManipulatorTransfer(plugin).transferData();
                if (cvm && dvm) {
                    plugin.getConfig().set("conversions.all_in_one.vortex_manipulator", true);
                    conversions++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("modules.shop")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loading Shop Module");
            new TARDISShop(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.shop")) {
                boolean cs = new TARDISAllInOneConfigConverter(plugin).transferConfig(TardisModule.SHOP);
                boolean ds = new TARDISShopTransfer(plugin).transferData();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISShopDisplayConverter(plugin), 300L);
                if (cs && ds) {
                    plugin.getConfig().set("conversions.all_in_one.shop", true);
                    conversions++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("modules.sonic_blaster")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loading Sonic Blaster Module");
            new TARDISSonicBlaster(plugin).enable();
            if (!plugin.getConfig().getBoolean("conversions.all_in_one.sonic_blaster")) {
                if (new TARDISAllInOneConfigConverter(plugin).transferConfig(TardisModule.BLASTER)) {
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
