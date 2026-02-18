package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.BookArgumentType;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.book.BookUtility;
import me.eccentric_nz.TARDIS.commands.book.TARDISBook;
import org.bukkit.entity.Player;

public class BookCommandNode {

    private final TARDIS plugin;

    public BookCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisbook")
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardisbook", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .requires(ctx -> ctx.getSender().hasPermission("tardis.book"))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            BookUtility.list(plugin, ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .then(Commands.argument("book_name", new BookArgumentType())
                        .then(Commands.literal("get")
                                .executes(ctx -> {
                                    String b = ctx.getArgument("book_name", String.class);
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    TARDISBook book = new TARDISBook(plugin);
                                    // title, author, filename, player
                                    book.writeBook("Rassilon", b, player);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("start")
                                .executes(ctx -> {
                                    String b = ctx.getArgument("book_name", String.class);
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    BookUtility.start(plugin, b, player);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
