package me.eccentric_nz.TARDIS;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.eccentric_nz.TARDIS.brigadier.BrigadierCommandRegister;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Bootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
//        Component author = Component.text("eccentric_nz", NamedTextColor.GRAY);
//        context.getLifecycleManager().registerEventHandler(RegistryEvents.PAINTING_VARIANT.compose().newHandler(event -> {
//            event.registry().register(
//                    PaintingVariantKeys.create(Key.key("tardis:chevrolet")),
//                    p -> p.assetId(Key.key("tardis:chevrolet"))
//                            .width(3)
//                            .height(2)
//                            .author(author)
//                            .title(Component.text("1957 Chevrolet Bel Air", NamedTextColor.GRAY))
//            );
//            event.registry().register(
//                    PaintingVariantKeys.create(Key.key("tardis:lava")),
//                    p -> p.assetId(Key.key("tardis:lava"))
//                            .width(2)
//                            .height(2)
//                            .author(author)
//                            .title(Component.text("Lava", NamedTextColor.GRAY))
//            );
//        }));
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY.newHandler(
                event -> {
                    try {
                        // retrieve the URI of the datapack folder.
                        URI uri = this.getClass().getResource("/datapack").toURI();
                        // discover the pack. The ID is set to "provided", which indicates to
                        // a server owner that your plugin includes this data pack.
                        event.registrar().discoverPack(uri, "provided");
                    } catch (URISyntaxException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ));
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            // register commands
            new BrigadierCommandRegister(commands).addAll();
        });
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new TARDIS();
    }
}
