package me.eccentric_nz.TARDIS.lazarus;

import org.bukkit.entity.Wolf;

import java.util.HashMap;
import java.util.List;

public class LazarusWolf {

    public static final HashMap<String, Wolf.Variant> VARIANTS = new HashMap<>() {{
        put("PALE", Wolf.Variant.PALE);
        put("ASHEN", Wolf.Variant.ASHEN);
        put("BLACK", Wolf.Variant.BLACK);
        put("CHESTNUT", Wolf.Variant.CHESTNUT);
        put("RUSTY", Wolf.Variant.RUSTY);
        put("SNOWY", Wolf.Variant.SNOWY);
        put("SPOTTED", Wolf.Variant.SPOTTED);
        put("STRIPED", Wolf.Variant.STRIPED);
        put("WOODS", Wolf.Variant.WOODS);
    }};

    public static final List<String> NAMES = List.of("PALE", "ASHEN", "BLACK", "CHESTNUT", "RUSTY", "SNOWY", "SPOTTED", "STRIPED", "WOODS");
}
