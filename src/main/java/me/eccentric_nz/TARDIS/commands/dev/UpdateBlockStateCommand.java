package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class UpdateBlockStateCommand {

    private final TARDIS plugin;

    public UpdateBlockStateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean refresh(Player player) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16);
        BlockState state = targetBlock.getState();
        state.update(true);
        return true;
    }
}
