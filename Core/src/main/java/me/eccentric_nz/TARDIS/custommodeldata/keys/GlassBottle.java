package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GlassBottle {

    ALUMINIUM_OXIDE(new NamespacedKey(TARDIS.plugin, "item/compounds/aluminium_oxide")),
    AMMONIA(new NamespacedKey(TARDIS.plugin, "item/compounds/ammonia")),
    JAR(new NamespacedKey(TARDIS.plugin, "item/compounds/jar")),
    BENZENE(new NamespacedKey(TARDIS.plugin, "item/compounds/benzene")),
    CRUDE_OIL(new NamespacedKey(TARDIS.plugin, "item/compounds/crude_oil")),
    GLUE(new NamespacedKey(TARDIS.plugin, "item/compounds/glue")),
    HYDROGEN_PEROXIDE(new NamespacedKey(TARDIS.plugin, "item/compounds/hydrogen_peroxide")),
    IRON_SULFIDE(new NamespacedKey(TARDIS.plugin, "item/compounds/iron_sulfide")),
    LATEX(new NamespacedKey(TARDIS.plugin, "item/compounds/latex")),
    LUMINOL(new NamespacedKey(TARDIS.plugin, "item/compounds/luminol")),
    MAGNESIUM_NITRATE(new NamespacedKey(TARDIS.plugin, "item/compounds/magnesium_nitrate")),
    POTASSIUM_IODIDE(new NamespacedKey(TARDIS.plugin, "item/compounds/potassium_iodide")),
    SALT(new NamespacedKey(TARDIS.plugin, "item/compounds/salt")),
    SOAP(new NamespacedKey(TARDIS.plugin, "item/compounds/soap")),
    SODIUM_FLUORIDE(new NamespacedKey(TARDIS.plugin, "item/compounds/sodium_fluoride")),
    SODIUM_HYDRIDE(new NamespacedKey(TARDIS.plugin, "item/compounds/sodium_hydride")),
    SODIUM_HYPOCHLORITE(new NamespacedKey(TARDIS.plugin, "item/compounds/sodium_hypochlorite"));

    private final NamespacedKey key;

    GlassBottle(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
