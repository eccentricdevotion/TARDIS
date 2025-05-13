package me.eccentric_nz.TARDIS.utility;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public interface FromRegistry {

    NamespacedKey getKey(Keyed keyed);

    String getKeysKey(Keyed keyed);
}
