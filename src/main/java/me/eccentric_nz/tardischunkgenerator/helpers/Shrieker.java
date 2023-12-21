package me.eccentric_nz.tardischunkgenerator.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlockState;

public class Shrieker {

    public static void shriek(Block block) {
        ServerLevel level = ((CraftWorld) block.getWorld()).getHandle();
        BlockPos pos = new BlockPos(block.getX(), block.getY(), block.getZ());
        BlockState state = ((CraftBlockState) block.getState()).getHandle();
        level.setBlock(pos, state.setValue(SculkShriekerBlock.SHRIEKING, true), 2);
        level.scheduleTick(pos, state.getBlock(), 90);
        level.levelEvent(3007, pos, 0);
        level.gameEvent(GameEvent.SHRIEK, pos, GameEvent.Context.of((Entity) null));
    }
}
