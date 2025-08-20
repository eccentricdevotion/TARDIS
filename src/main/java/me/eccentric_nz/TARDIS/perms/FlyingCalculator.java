/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
