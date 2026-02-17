package me.eccentric_nz.TARDIS.brigadier.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class SaveSuggestions {

    public static CompletableFuture<Suggestions> get(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        Player player = (Player) ctx.getSource().getExecutor();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(TARDIS.plugin, where, false);
        if (rst.resultSet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("tardis_id", rst.getTardis_id());
            ResultSetDestinations rs = new ResultSetDestinations(TARDIS.plugin, map, true);
            if (rs.resultSet()) {
                for (HashMap<String, String> save : rs.getData()) {
                    builder.suggest(save.get("dest_name"));
                }
            }
        }
        return builder.buildFuture();
    }
}
