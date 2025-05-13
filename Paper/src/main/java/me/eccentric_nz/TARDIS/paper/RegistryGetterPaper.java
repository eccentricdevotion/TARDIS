package me.eccentric_nz.TARDIS.paper;

import me.eccentric_nz.TARDIS.utility.FromRegistry;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public class RegistryGetterPaper implements FromRegistry {

    @Override
    public NamespacedKey getKey(Keyed keyed) {
        return keyed.getKey();
    }

    @Override
    public String getKeysKey(Keyed keyed) {
        return getKey(keyed).getKey();
    }
}
