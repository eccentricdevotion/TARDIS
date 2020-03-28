---
layout: default
title: API
---

# TARDIS API

The TARDIS plugin now includes a publically accessible API for plugin developers to use.

As of TARDIS v3.2-beta-1 the API can be accessed in your plugin like so:
```java
public class ExamplePlugin extends JavaPlugin {
    private TardisAPI tardisAPI;
    private TARDIS tardis;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // Get TARDIS
        Plugin p = pm.getPlugin("TARDIS");
        if (p == null) {
            System.err.println("Cannot find TARDIS!");
            pm.disablePlugin(this);
            return;
        }
        tardis = (TARDIS) p;
        // get the API
        tardisAPI = tardis.getTardisAPI();
    }

    public TardisAPI getTardisAPI() {
        return tardisAPI;
    }
}
```
You can auto-generate a plugin that hooks into the TARDIS API at the [Bukkit plugin starter](http://www.thenosefairy.co.nz/plugin_starter.php) page. Make sure to select the _TARDIS API_ checkbox.

The Java Doc for the API is available here: [TARDIS API docs](http://thenosefairy.co.nz/TARDIS_java_docs/me/eccentric_nz/TARDIS/api/TardisAPI.html)

An extensive working example plugin can be found here: [TARDISVortexManipulator source code](https://github.com/eccentricdevotion/TARDISVortexManipulator)
