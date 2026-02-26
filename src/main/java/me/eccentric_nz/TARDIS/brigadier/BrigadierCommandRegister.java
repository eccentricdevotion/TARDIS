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
        commands.registrar().register(new DevCommandNode(plugin).build(), List.of("tdev"));
        commands.registrar().register(new DisplayCommandNode(plugin).build(), List.of("tdisplay"));
        commands.registrar().register(new GamemodeCommandNode(plugin).build(), List.of("tgm"));
        commands.registrar().register(new SurvivalCommandNode().build(), List.of());
        commands.registrar().register(new CreativeCommandNode().build(), List.of());
        commands.registrar().register(new AdventureCommandNode().build(), List.of());
        commands.registrar().register(new SpectatorCommandNode().build(), List.of());
        commands.registrar().register(new GiveCommandNode(plugin).build(), List.of("tgive"));
        commands.registrar().register(new GravityCommandNode(plugin).build(), List.of("tgravity"));
        commands.registrar().register(new HandlesCommandNode(plugin).build(), List.of("tardishandles", "thandles"));
        commands.registrar().register(new InfoCommandNode(plugin).build(), List.of());
        commands.registrar().register(new JunkCommandNode(plugin).build(), List.of("tjunk"));
        commands.registrar().register(new NetherPortalCommandNode(plugin).build(), List.of("tnetherportal", "tnp"));
        commands.registrar().register(new PlotCommandNode(plugin).build(), List.of("tplot"));
        commands.registrar().register(new RecipeCommandNode(plugin).build(), List.of("trecipe"));
        commands.registrar().register(new PreferencesCommandNode(plugin).build(), List.of("tprefs"));
        commands.registrar().register(new RemoteCommandNode(plugin).build(), List.of("tremote"));
        commands.registrar().register(new SayCommandNode(plugin).build(), List.of("tsay"));
        commands.registrar().register(new SchematicCommandNode(plugin).build(), List.of("ts", "tschematic"));
        commands.registrar().register(new SudoCommandNode(plugin).build(), List.of("tsudo"));
        commands.registrar().register(new TeleportCommandNode().build(), List.of("tardistp", "ttp"));
        commands.registrar().register(new TimeCommandNode(plugin).build(), List.of("ttime"));
        commands.registrar().register(new TravelCommandNode(plugin).build(), List.of("ttravel"));
        commands.registrar().register(new WorldCommandNode(plugin).build(), List.of("tworld"));
        commands.registrar().register(new QuestionCommandNode(plugin).build(), List.of("t?", "tardishelp"));
        if (plugin.getConfig().getBoolean("modules.chemistry")) {
            commands.registrar().register(new ChemistryCommandNode(plugin).build(), List.of("tchemistry"));
        }
        if (plugin.getConfig().getBoolean("modules.regeneration")) {
            commands.registrar().register(new RegenerationCommandNode(plugin).build(), List.of("regeneration", "regen"));
        }
        if (plugin.getConfig().getBoolean("modules.shop")) {
            commands.registrar().register(new ShopCommandNode(plugin).build(), List.of("tshop"));
        }
        if (plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            commands.registrar().register(new VortexManipulatorCommandNode(plugin).build(), List.of("vortexmainpulator", "tvm"));
        }
        if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
            commands.registrar().register(new MonstersCommandNode(plugin).build(), List.of("tardisweepingangels"));
        }
    }
}
