package me.eccentric_nz.TARDIS.chameleon.utils;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;

public record CustomPreset(EnumMap<COMPASS, TARDISChameleonColumn> blueprint,
                           EnumMap<COMPASS, TARDISChameleonColumn> stained,
                           EnumMap<COMPASS, TARDISChameleonColumn> glass,
                           List<String> lines,
                           Material icon) {
}
