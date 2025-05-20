package me.eccentric_nz.TARDIS.perms;

import me.eccentric_nz.TARDIS.TARDIS;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.bukkit.entity.Player;

public class FlyingCalculator implements ContextCalculator<Player> {

    private static final String KEY = "is-flying-tardis";

    @Override
    public void calculate(Player target, ContextConsumer contextConsumer) {
        contextConsumer.accept(KEY, String.valueOf(isFlying(target)));
    }

    @Override
    public ContextSet estimatePotentialContexts() {
        ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
        builder.add(KEY, "true");
        builder.add(KEY, "false");
        return builder.build();
    }

    private boolean isFlying(Player target) {
        return TARDIS.plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(target.getUniqueId());
    }
}
