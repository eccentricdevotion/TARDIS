package me.eccentric_nz.TARDIS;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Bootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
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
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new TARDIS();
    }
}
