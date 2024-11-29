package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ArrowVariant {

    BACK(new NamespacedKey(TARDIS.plugin, "gui/arrow/back")),
    DOWN(new NamespacedKey(TARDIS.plugin, "gui/arrow/down")),
    UP(new NamespacedKey(TARDIS.plugin, "gui/arrow/up")),
    GO(new NamespacedKey(TARDIS.plugin, "gui/arrow/go")),
    REARRANGE(new NamespacedKey(TARDIS.plugin, "gui/arrow/rearrange")),
    YOU_ARE_HERE(new NamespacedKey(TARDIS.plugin, "gui/arrow/you_are_here")),
    SCROLL_DOWN(new NamespacedKey(TARDIS.plugin, "gui/arrow/scroll_down")),
    SCROLL_UP(new NamespacedKey(TARDIS.plugin, "gui/arrow/scroll_up")),
    HANDLES_OPERATOR_ADDITION(new NamespacedKey(TARDIS.plugin, "handles/handles_operator_addition")),
    HANDLES_OPERATOR_SUBTRACTION(new NamespacedKey(TARDIS.plugin, "handles/handles_operator_subtraction")),
    PAGE_ONE(new NamespacedKey(TARDIS.plugin, "gui/page_one")),
    PAGE_TWO(new NamespacedKey(TARDIS.plugin, "gui/page_two")),
    JUDOON_AMMO(new NamespacedKey(TARDIS.plugin, "monster/judoon/judoon_ammo")),
    LESS(new NamespacedKey(TARDIS.plugin, "gui/arrow/less")),
    MORE(new NamespacedKey(TARDIS.plugin, "gui/arrow/more"));

    private final NamespacedKey key;

    ArrowVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
