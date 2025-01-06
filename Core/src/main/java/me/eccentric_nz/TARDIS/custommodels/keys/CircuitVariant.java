package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CircuitVariant {

    TELEPATHIC(new NamespacedKey(TARDIS.plugin, "telepathic")),
    STATTENHEIM(new NamespacedKey(TARDIS.plugin, "stattenheim")),
    MATERIALISATION(new NamespacedKey(TARDIS.plugin, "materialisation")),
    LOCATOR(new NamespacedKey(TARDIS.plugin, "locator")),
    CHAMELEON(new NamespacedKey(TARDIS.plugin, "chameleon")),
    SONIC(new NamespacedKey(TARDIS.plugin, "sonic")),
    ADMIN(new NamespacedKey(TARDIS.plugin, "admin")),
    BIO(new NamespacedKey(TARDIS.plugin, "bio")),
    REDSTONE(new NamespacedKey(TARDIS.plugin, "circuit_redstone")),
    DIAMOND(new NamespacedKey(TARDIS.plugin, "circuit_diamond")),
    EMERALD(new NamespacedKey(TARDIS.plugin, "circuit_emerald")),
    ARS(new NamespacedKey(TARDIS.plugin, "ars")),
    TEMPORAL(new NamespacedKey(TARDIS.plugin, "temporal")),
    MEMORY(new NamespacedKey(TARDIS.plugin, "memory")),
    INPUT(new NamespacedKey(TARDIS.plugin, "input")),
    SCANNER(new NamespacedKey(TARDIS.plugin, "scanner")),
    PERCEPTION(new NamespacedKey(TARDIS.plugin, "perception")),
    PAINTER(new NamespacedKey(TARDIS.plugin, "painter")),
    RANDOM(new NamespacedKey(TARDIS.plugin, "random")),
    INVISIBILITY(new NamespacedKey(TARDIS.plugin, "invisibility")),
    IGNITE(new NamespacedKey(TARDIS.plugin, "ignite")),
    RIFT(new NamespacedKey(TARDIS.plugin, "rift")),
    PICKUP(new NamespacedKey(TARDIS.plugin, "pickup")),
    GALLIFREY(new NamespacedKey(TARDIS.plugin, "gallifrey")),
    KNOCKBACK(new NamespacedKey(TARDIS.plugin, "knockback")),
    BRUSH(new NamespacedKey(TARDIS.plugin, "brush")),
    CONVERSION(new NamespacedKey(TARDIS.plugin, "conversion")),
    TELEPATHIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "telepathic_damaged")),
    STATTENHEIM_DAMAGED(new NamespacedKey(TARDIS.plugin, "stattenheim_damaged")),
    MATERIALISATION_DAMAGED(new NamespacedKey(TARDIS.plugin, "materialisation_damaged")),
    LOCATOR_DAMAGED(new NamespacedKey(TARDIS.plugin, "locator_damaged")),
    CHAMELEON_DAMAGED(new NamespacedKey(TARDIS.plugin, "chameleon_damaged")),
    SONIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "sonic_damaged")),
    ARS_DAMAGED(new NamespacedKey(TARDIS.plugin, "ars_damaged")),
    TEMPORAL_DAMAGED(new NamespacedKey(TARDIS.plugin, "temporal_damaged")),
    MEMORY_DAMAGED(new NamespacedKey(TARDIS.plugin, "memory_damaged")),
    INPUT_DAMAGED(new NamespacedKey(TARDIS.plugin, "input_damaged")),
    SCANNER_DAMAGED(new NamespacedKey(TARDIS.plugin, "scanner_damaged")),
    PERCEPTION_DAMAGED(new NamespacedKey(TARDIS.plugin, "perception_damaged")),
    RANDOM_DAMAGED(new NamespacedKey(TARDIS.plugin, "random_damaged")),
    INVISIBILITY_DAMAGED(new NamespacedKey(TARDIS.plugin, "invisibility_damaged")),
    RIFT_DAMAGED(new NamespacedKey(TARDIS.plugin, "rift_damaged"));

    private final NamespacedKey key;

    CircuitVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
