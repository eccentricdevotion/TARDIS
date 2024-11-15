package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GlowstoneDust {

    TELEPATHIC(new NamespacedKey(TARDIS.plugin, "item/circuit/telepathic")),
    STATTENHEIM(new NamespacedKey(TARDIS.plugin, "item/circuit/stattenheim")),
    MATERIALISATION(new NamespacedKey(TARDIS.plugin, "item/circuit/materialisation")),
    LOCATOR(new NamespacedKey(TARDIS.plugin, "item/circuit/locator")),
    CHAMELEON(new NamespacedKey(TARDIS.plugin, "item/circuit/chameleon")),
    SONIC(new NamespacedKey(TARDIS.plugin, "item/circuit/sonic")),
    ADMIN(new NamespacedKey(TARDIS.plugin, "item/circuit/admin")),
    BIO(new NamespacedKey(TARDIS.plugin, "item/circuit/bio")),
    REDSTONE(new NamespacedKey(TARDIS.plugin, "item/circuit/redstone")),
    DIAMOND(new NamespacedKey(TARDIS.plugin, "item/circuit/diamond")),
    EMERALD(new NamespacedKey(TARDIS.plugin, "item/circuit/emerald")),
    ARS(new NamespacedKey(TARDIS.plugin, "item/circuit/ars")),
    TEMPORAL(new NamespacedKey(TARDIS.plugin, "item/circuit/temporal")),
    MEMORY(new NamespacedKey(TARDIS.plugin, "item/circuit/memory")),
    INPUT(new NamespacedKey(TARDIS.plugin, "item/circuit/input")),
    SCANNER(new NamespacedKey(TARDIS.plugin, "item/circuit/scanner")),
    PERCEPTION(new NamespacedKey(TARDIS.plugin, "item/circuit/perception")),
    PAINTER(new NamespacedKey(TARDIS.plugin, "item/circuit/painter")),
    RANDOM(new NamespacedKey(TARDIS.plugin, "item/circuit/random")),
    INVISIBILITY(new NamespacedKey(TARDIS.plugin, "item/circuit/invisibility")),
    IGNITE(new NamespacedKey(TARDIS.plugin, "item/circuit/ignite")),
    RIFT(new NamespacedKey(TARDIS.plugin, "item/circuit/rift")),
    PICKUP(new NamespacedKey(TARDIS.plugin, "item/circuit/pickup")),
    GALLIFREY(new NamespacedKey(TARDIS.plugin, "item/circuit/gallifrey")),
    KNOCKBACK(new NamespacedKey(TARDIS.plugin, "item/circuit/knockback")),
    BRUSH(new NamespacedKey(TARDIS.plugin, "item/circuit/brush")),
    CONVERSION(new NamespacedKey(TARDIS.plugin, "item/circuit/conversion")),
    TELEPATHIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/telepathic_damaged")),
    STATTENHEIM_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/stattenheim_damaged")),
    MATERIALISATION_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/materialisation_damaged")),
    LOCATOR_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/locator_damaged")),
    CHAMELEON_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/chameleon_damaged")),
    SONIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/sonic_damaged")),
    ARS_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/ars_damaged")),
    TEMPORAL_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/temporal_damaged")),
    MEMORY_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/memory_damaged")),
    INPUT_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/input_damaged")),
    SCANNER_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/scanner_damaged")),
    PERCEPTION_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/perception_damaged")),
    RANDOM_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/random_damaged")),
    INVISIBILITY_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/invisibility_damaged")),
    RIFT_DAMAGED(new NamespacedKey(TARDIS.plugin, "item/circuit/rift_damaged"));

    private final NamespacedKey key;

    GlowstoneDust(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
