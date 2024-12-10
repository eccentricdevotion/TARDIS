package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ChemistryBottle {

    ALUMINIUM_OXIDE(new NamespacedKey(TARDIS.plugin, "aluminium_oxide")),
    AMMONIA(new NamespacedKey(TARDIS.plugin, "ammonia")),
    JAR(new NamespacedKey(TARDIS.plugin, "jar")),
    BENZENE(new NamespacedKey(TARDIS.plugin, "benzene")),
    CRUDE_OIL(new NamespacedKey(TARDIS.plugin, "crude_oil")),
    GLUE(new NamespacedKey(TARDIS.plugin, "glue")),
    HYDROGEN_PEROXIDE(new NamespacedKey(TARDIS.plugin, "hydrogen_peroxide")),
    IRON_SULFIDE(new NamespacedKey(TARDIS.plugin, "iron_sulfide")),
    LATEX(new NamespacedKey(TARDIS.plugin, "latex")),
    LUMINOL(new NamespacedKey(TARDIS.plugin, "luminol")),
    MAGNESIUM_NITRATE(new NamespacedKey(TARDIS.plugin, "magnesium_nitrate")),
    POTASSIUM_IODIDE(new NamespacedKey(TARDIS.plugin, "potassium_iodide")),
    SALT(new NamespacedKey(TARDIS.plugin, "salt")),
    SOAP(new NamespacedKey(TARDIS.plugin, "soap")),
    SODIUM_FLUORIDE(new NamespacedKey(TARDIS.plugin, "sodium_fluoride")),
    SODIUM_HYDRIDE(new NamespacedKey(TARDIS.plugin, "sodium_hydride")),
    SODIUM_HYPOCHLORITE(new NamespacedKey(TARDIS.plugin, "sodium_hypochlorite"));

    private final NamespacedKey key;

    ChemistryBottle(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
