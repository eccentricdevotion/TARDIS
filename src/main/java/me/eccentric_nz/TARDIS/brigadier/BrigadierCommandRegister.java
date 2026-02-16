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
        commands.registrar().register(new SchematicCommandNode(plugin).build(), List.of("ts", "tschematic"));
    }
}
