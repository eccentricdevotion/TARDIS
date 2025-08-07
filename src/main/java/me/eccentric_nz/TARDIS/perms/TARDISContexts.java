package me.eccentric_nz.TARDIS.perms;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TARDISContexts {

    public void register() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms luckPerms = provider.getProvider();
            luckPerms.getContextManager().registerCalculator(new FlyingCalculator());
        }
    }
}
