package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public record GUIData(int customModelData, int slot, Material material) {

    public String getName() {
        String s = toString();
        return TARDISStringUtils.sentenceCase(s);
    }
}
