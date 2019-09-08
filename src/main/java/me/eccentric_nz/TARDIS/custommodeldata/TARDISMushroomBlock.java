package me.eccentric_nz.TARDIS.custommodeldata;

import org.bukkit.block.data.MultipleFacing;

import java.util.Arrays;
import java.util.List;

public class TARDISMushroomBlock {

    private static final List<String> vanillaBrown = Arrays.asList(
			"minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=false]",
			"minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=false]",
			"minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=true]",
			"minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=false]",
			"minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=true]",
			"minecraft:brown_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=false]",
			"minecraft:brown_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=true]",
			"minecraft:brown_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=false]",
			"minecraft:brown_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=false]",
			"minecraft:brown_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=false]",
			"minecraft:brown_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]"
	);

    public static boolean isVanillaBrownMushroomState(MultipleFacing multipleFacing) {
        return (vanillaBrown.contains(multipleFacing.getAsString()));
    }

    private static final List<String> vanillaRed = Arrays.asList(
			"minecraft:red_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=false]",
			"minecraft:red_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=true]",
			"minecraft:red_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=true]",
			"minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=false]",
			"minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=true]",
			"minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=false]",
			"minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=true]",
			"minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=false,west=false]",
			"minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=false,west=true]",
			"minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=false]",
			"minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=true]",
			"minecraft:red_mushroom_block[down=false,east=true,north=false,south=false,up=false,west=false]",
			"minecraft:red_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=false]",
			"minecraft:red_mushroom_block[down=false,east=true,north=false,south=true,up=false,west=false]",
			"minecraft:red_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=false]",
			"minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=false,west=false]",
			"minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=false]",
			"minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]"
	);

    public static boolean isVanillaRedMushroomState(MultipleFacing multipleFacing) {
        return (vanillaRed.contains(multipleFacing.getAsString()));
    }

    private static final List<String> vanillaStem = Arrays.asList(
			"minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=false]",
			"minecraft:mushroom_stem[down=false,east=true,north=true,south=true,up=false,west=true]",
			"minecraft:mushroom_stem[down=true,east=true,north=true,south=true,up=true,west=true]"
	);

    public static boolean isVanillaMushroomStemState(MultipleFacing multipleFacing) {
        return (vanillaStem.contains(multipleFacing.getAsString()));
    }
}
