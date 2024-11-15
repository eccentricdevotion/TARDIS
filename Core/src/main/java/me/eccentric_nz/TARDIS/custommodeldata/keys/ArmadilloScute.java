package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ArmadilloScute {

    ASTROSEXTANT_RECTIFIER(new NamespacedKey(TARDIS.plugin, "block/controls/astrosextant_rectifier")),
    GRAVITIC_ANOMOLISER(new NamespacedKey(TARDIS.plugin, "block/controls/gravitic_anomoliser")),
    ABSOLUTE_TESSERACTULATOR(new NamespacedKey(TARDIS.plugin, "block/controls/absolute_tesseractulator")),
    CONSOLE_LAMP(new NamespacedKey(TARDIS.plugin, "block/controls/console_lamp")),
    HELMIC_REGULATOR_0(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_0")),
    HELMIC_REGULATOR_1(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_1")),
    HELMIC_REGULATOR_2(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_2")),
    HELMIC_REGULATOR_3(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_3")),
    HELMIC_REGULATOR_4(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_4")),
    HELMIC_REGULATOR_5(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_5")),
    HELMIC_REGULATOR_6(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_6")),
    HELMIC_REGULATOR_7(new NamespacedKey(TARDIS.plugin, "block/controls/helmic_regulator_7"));

    private final NamespacedKey key;

    ArmadilloScute(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
