package me.eccentric_nz.TARDIS.brigadier.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmatList;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class TransmatSuggestions {

    public static CompletableFuture<Suggestions> get(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        Player player = (Player) ctx.getSource().getExecutor();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(TARDIS.plugin, where, false);
        if (rst.resultSet()) {
            ResultSetTransmatList rs = new ResultSetTransmatList(TARDIS.plugin, rst.getTardis_id());
            if (rs.resultSet()) {
                for (Transmat t : rs.getData()) {
                    builder.suggest(t.name());
                }
            }
        }
        return builder.buildFuture();
    }
}
