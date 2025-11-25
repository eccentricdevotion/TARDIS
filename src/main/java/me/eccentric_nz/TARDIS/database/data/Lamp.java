package me.eccentric_nz.TARDIS.database.data;

import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.Nullable;

public record Lamp(Block block, @Nullable Material materialOn, @Nullable Material materialOff, float percentage) {

}
