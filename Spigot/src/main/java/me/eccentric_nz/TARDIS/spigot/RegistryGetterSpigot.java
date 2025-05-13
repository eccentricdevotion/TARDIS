package me.eccentric_nz.TARDIS.spigot;

import me.eccentric_nz.TARDIS.utility.FromRegistry;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.registry.RegistryAware;

public class RegistryGetterSpigot implements FromRegistry {
    @Override
    public NamespacedKey getKey(Keyed keyed) {
        return ((RegistryAware) keyed).getKeyOrThrow();
    }

    @Override
    public String getKeysKey(Keyed keyed) {
        return getKey(keyed).getKey();
    }
}
