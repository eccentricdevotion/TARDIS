---
layout: default
title: Interior Positioning System
---

# TARDIS Interior Positioning System

By default (if you have a multi-world plugin installed) the plugin sets up a special world called **TARDIS\_TimeVortex** that uses the TARDIS Interior Positioning System. This is similar to the many skyblock and plots plugins, in that the TARDIS (when created) is placed in the middle of a 1024 x 1024 block square plot.

The T.I.P.S grid is 20 x 20 plots deep and wide, giving a total of 400 plots. When a TARDIS is deleted, the free plot is reused.

If WorldGuard is also installed on the server, the entire plot is made a protected region, with the Time Lord of the TARDIS its owner. Access to the region is set to DENY for other players, so travel between plots is stopped. When a Time Lord adds a companion, they are added as a region member.

If configured (`allow: wg_flag_set: true`), Time Lords can grant or deny build rights to their companions by issuing the command `/tardisprefs build [on|off]`

### T.I.P.S and Factions

Because T.I.P.S uses WorldGuard to protect the TARDIS, claiming Factions in the TARDIS\_TimeVortex will cause issues when the TARDIS is deleted. It is suggested that you disable Faction claiming in the TARDIS\_TimeVortex world. To do this, edit the `worldsClaimingEnabled` setting in the Factions config file (found in _server/mstore/factions\_mconf/instance.json_) — it should look something like this:

    "worldsClaimingEnabled": {
        "standard": true,
        "exceptions": ["TARDIS_TimeVortex"]
      },

