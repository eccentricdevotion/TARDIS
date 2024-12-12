package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ArrowVariant {

    DOWN(new NamespacedKey(TARDIS.plugin, "button_down")),
    UP(new NamespacedKey(TARDIS.plugin, "button_up")),
    GO(new NamespacedKey(TARDIS.plugin, "button_go")),
    REARRANGE(new NamespacedKey(TARDIS.plugin, "button_rearrange")),
    YOU_ARE_HERE(new NamespacedKey(TARDIS.plugin, "button_you_are_here")),
    SCROLL_DOWN(new NamespacedKey(TARDIS.plugin, "button_scroll_down")),
    SCROLL_UP(new NamespacedKey(TARDIS.plugin, "button_scroll_up")),
    HANDLES_OPERATOR_ADDITION(new NamespacedKey(TARDIS.plugin, "handles_operator_addition")),
    HANDLES_OPERATOR_SUBTRACTION(new NamespacedKey(TARDIS.plugin, "handles_operator_subtraction")),
    PAGE_ONE(new NamespacedKey(TARDIS.plugin, "button_page_one")),
    PAGE_TWO(new NamespacedKey(TARDIS.plugin, "button_page_two")),
    JUDOON_AMMO(new NamespacedKey(TARDIS.plugin, "judoon_ammo")),
    LESS(new NamespacedKey(TARDIS.plugin, "button_less")),
    MORE(new NamespacedKey(TARDIS.plugin, "button_more"));

    private final NamespacedKey key;

    ArrowVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
