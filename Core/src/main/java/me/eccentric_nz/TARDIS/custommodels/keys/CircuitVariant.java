package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CircuitVariant {

    TELEPATHIC(new NamespacedKey(TARDIS.plugin, "circuit/telepathic")),
    STATTENHEIM(new NamespacedKey(TARDIS.plugin, "circuit/stattenheim")),
    MATERIALISATION(new NamespacedKey(TARDIS.plugin, "circuit/materialisation")),
    LOCATOR(new NamespacedKey(TARDIS.plugin, "circuit/locator")),
    CHAMELEON(new NamespacedKey(TARDIS.plugin, "circuit/chameleon")),
    SONIC(new NamespacedKey(TARDIS.plugin, "circuit/sonic")),
    ADMIN(new NamespacedKey(TARDIS.plugin, "circuit/admin")),
    BIO(new NamespacedKey(TARDIS.plugin, "circuit/bio")),
    REDSTONE(new NamespacedKey(TARDIS.plugin, "circuit/redstone")),
    DIAMOND(new NamespacedKey(TARDIS.plugin, "circuit/diamond")),
    EMERALD(new NamespacedKey(TARDIS.plugin, "circuit/emerald")),
    ARS(new NamespacedKey(TARDIS.plugin, "circuit/ars")),
    TEMPORAL(new NamespacedKey(TARDIS.plugin, "circuit/temporal")),
    MEMORY(new NamespacedKey(TARDIS.plugin, "circuit/memory")),
    INPUT(new NamespacedKey(TARDIS.plugin, "circuit/input")),
    SCANNER(new NamespacedKey(TARDIS.plugin, "circuit/scanner")),
    PERCEPTION(new NamespacedKey(TARDIS.plugin, "circuit/perception")),
    PAINTER(new NamespacedKey(TARDIS.plugin, "circuit/painter")),
    RANDOM(new NamespacedKey(TARDIS.plugin, "circuit/random")),
    INVISIBILITY(new NamespacedKey(TARDIS.plugin, "circuit/invisibility")),
    IGNITE(new NamespacedKey(TARDIS.plugin, "circuit/ignite")),
    RIFT(new NamespacedKey(TARDIS.plugin, "circuit/rift")),
    PICKUP(new NamespacedKey(TARDIS.plugin, "circuit/pickup")),
    GALLIFREY(new NamespacedKey(TARDIS.plugin, "circuit/gallifrey")),
    KNOCKBACK(new NamespacedKey(TARDIS.plugin, "circuit/knockback")),
    BRUSH(new NamespacedKey(TARDIS.plugin, "circuit/brush")),
    CONVERSION(new NamespacedKey(TARDIS.plugin, "circuit/conversion")),
    TELEPATHIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/telepathic_damaged")),
    STATTENHEIM_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/stattenheim_damaged")),
    MATERIALISATION_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/materialisation_damaged")),
    LOCATOR_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/locator_damaged")),
    CHAMELEON_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/chameleon_damaged")),
    SONIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/sonic_damaged")),
    ARS_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/ars_damaged")),
    TEMPORAL_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/temporal_damaged")),
    MEMORY_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/memory_damaged")),
    INPUT_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/input_damaged")),
    SCANNER_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/scanner_damaged")),
    PERCEPTION_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/perception_damaged")),
    RANDOM_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/random_damaged")),
    INVISIBILITY_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/invisibility_damaged")),
    RIFT_DAMAGED(new NamespacedKey(TARDIS.plugin, "circuit/rift_damaged"));

    private final NamespacedKey key;

    CircuitVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
