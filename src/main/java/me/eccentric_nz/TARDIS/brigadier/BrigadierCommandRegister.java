package me.eccentric_nz.TARDIS.brigadier;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import me.eccentric_nz.TARDIS.TARDIS;

import java.util.List;

public class BrigadierCommandRegister {

    private final ReloadableRegistrarEvent<Commands> commands;
    private final TARDIS plugin;

    public BrigadierCommandRegister(ReloadableRegistrarEvent<Commands> commands, TARDIS plugin) {
        this.commands = commands;
        this.plugin = plugin;
    }

    public void addAll() {
        commands.registrar().register(new AdminCommandNode(plugin).build(), List.of("tadmin"));
        commands.registrar().register(new AreasCommandNode(plugin).build(), List.of("tarea"));
        commands.registrar().register(new ArtronStorageCommandNode(plugin).build(), List.of("tartron"));
        commands.registrar().register(new BindCommandNode(plugin).build(), List.of("tbind"));
        commands.registrar().register(new BookCommandNode(plugin).build(), List.of("tbook"));
        commands.registrar().register(new CallCommandNode(plugin).build(), List.of("tcall"));
        commands.registrar().register(new ConfigCommandNode(plugin).build(), List.of("tconfig"));
        commands.registrar().register(new GamemodeCommandNode(plugin).build(), List.of("tgm"));
        commands.registrar().register(new SurvivalCommandNode(plugin).build(), List.of());
        commands.registrar().register(new CreativeCommandNode(plugin).build(), List.of());
        commands.registrar().register(new AdventureCommandNode(plugin).build(), List.of());
        commands.registrar().register(new SpectatorCommandNode(plugin).build(), List.of());
        commands.registrar().register(new GiveCommandNode(plugin).build(), List.of("tgive"));
        commands.registrar().register(new GravityCommandNode(plugin).build(), List.of("tgravity"));
        commands.registrar().register(new NetherPortalCommandNode(plugin).build(), List.of("tnetherportal", "tnp"));
        commands.registrar().register(new PlotCommandNode(plugin).build(), List.of("tplot"));
        commands.registrar().register(new RecipeCommandNode(plugin).build(), List.of("trecipe"));
        commands.registrar().register(new SchematicCommandNode(plugin).build(), List.of("ts", "tschematic"));
        commands.registrar().register(new TeleportCommandNode().build(), List.of("tardistp", "ttp"));
        commands.registrar().register(new TimeCommandNode(plugin).build(), List.of("ttime"));
        commands.registrar().register(new TravelCommandNode(plugin).build(), List.of("ttravel"));
        commands.registrar().register(new WorldCommandNode(plugin).build(), List.of("tworld"));
        commands.registrar().register(new QuestionCommandNode(plugin).build(), List.of("t?", "tardishelp"));
    }
}
